package com.blundell.hangovercures.archive;

import android.support.annotation.NonNull;
import android.util.Log;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FirebaseArchiveRepository implements ArchiveRepository {

    private static final int TIMEOUT = 20;
    private static final String BASE = "https://hangovercures.firebaseio.com";

    private final FirebaseAuth firebaseAuth;

    public FirebaseArchiveRepository(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void signInUser() {
        final CountDownLatch latch = new CountDownLatch(1);
        firebaseAuth(
                new AuthCallback() {
                    @Override
                    public void onAuthenticated() {
                        latch.countDown();
                    }
                }
        );

        try {
            latch.await(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // we will fall into the error case
        }
    }

    private void firebaseAuth(final AuthCallback callback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        callback.onAuthenticated();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("XXX", "auth error ", e);
                        // Failing to sign in means they could be offline (and the current session has expired)
                        // We will either use cached data or show a blank screen
                    }
                });
    }

    private interface AuthCallback {
        void onAuthenticated();
    }

    @Override
    public User getUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            return new User.SignedOutUser();
        } else {
            return new User.SignedInUser(currentUser.getUid());
        }
    }

    @Override
    public List<Cure> getCures() {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Cure> cures = new ArrayList<>();
        getFirebaseCures(
                new CuresCallback() {
                    @Override
                    public void onLoaded(List<FirebaseCure> firebaseCures) {
                        List<Cure> hangoverCures = marshallToCures(firebaseCures);
                        cures.addAll(hangoverCures);
                        latch.countDown();
                    }
                }
        );

        try {
            latch.await(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // we will return an empty list
        }

        return cures;
    }

    private void getFirebaseCures(final CuresCallback callback) {
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReferenceFromUrl(BASE + "/archive-cures");
        firebase.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<FirebaseCure> firebaseCures = marshallToFirebaseCures(dataSnapshot);
                        callback.onLoaded(firebaseCures);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Not possible in this usecase ... I think
                    }
                }
        );
    }

    private List<FirebaseCure> marshallToFirebaseCures(DataSnapshot dataSnapshot) {
        List<FirebaseCure> firebaseCures = new ArrayList<>();
        for (DataSnapshot cureSnapshot : dataSnapshot.getChildren()) {
            FirebaseCure firebaseCure = cureSnapshot.getValue(FirebaseCure.class);
            firebaseCures.add(firebaseCure);
        }
        return firebaseCures;
    }

    private List<Cure> marshallToCures(List<FirebaseCure> firebaseCures) {
        List<Cure> hangoverCures = new ArrayList<>();
        for (FirebaseCure firebaseCure : firebaseCures) {
            Cure hangoverCure = marshallToCure(firebaseCure);
            hangoverCures.add(hangoverCure);
        }
        return hangoverCures;
    }

    private Cure marshallToCure(FirebaseCure firebaseCure) {
        Cure.Id id = new Cure.Id(firebaseCure.id);
        String title = firebaseCure.name;
        String description = firebaseCure.description;
        Rating globalRating = Rating.from(firebaseCure.global_rating);
        return new Cure(id, title, description, globalRating);
    }

    public static class FirebaseCure {

        public long id;
        public String name;
        public String description;
        public double global_rating;
        @Deprecated // I was dumb to put this here - its shared amongst everyone
        public double local_rating;
        public long total_votes;

        public FirebaseCure() {
            // empty default constructor, necessary for Firebase to be able to deserialize
        }

    }

    private interface CuresCallback {
        void onLoaded(List<FirebaseCure> cures);
    }
}
