package com.blundell.hangovercures.details;

import android.support.annotation.NonNull;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.Log;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FirebaseDetailsRepository implements DetailsRepository {

    private static final String BASE = "https://hangovercures.firebaseio.com";

    private final Log log;

    public FirebaseDetailsRepository(Log log) {
        this.log = log;
    }

    @Override
    public void saveCureRating(final Cure.Id id, final int rating) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        int remoteId = id.asInt() - 1;
        if (currentUser == null) {
            signInBeforeSaveRatingToFirebase(firebaseAuth, remoteId, rating);
        } else {
            String userId = currentUser.getUid();
            saveRatingToFirebase(remoteId, userId, rating);
        }
    }

    private void signInBeforeSaveRatingToFirebase(FirebaseAuth firebase, final int remoteId, final int rating) {
        firebase.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String userId = task.getResult().getUser().getUid();
                        saveRatingToFirebase(remoteId, userId, rating);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        log.e("auth error " + e);
                        // Failing to sign in means they could be offline (and the current session has expired)
                        // We will either use cached data or show a blank screen
                    }
                });
    }

    private void saveRatingToFirebase(final int remoteId, String userId, final int rating) {
        DatabaseReference userCureRef = FirebaseDatabase.getInstance().getReferenceFromUrl(BASE + "/users/" + userId + "/archive-cures/" + remoteId + "/rating");
        userCureRef.setValue(rating);

        DatabaseReference archiveCureRef = FirebaseDatabase.getInstance().getReferenceFromUrl(BASE + "/archive-cures/" + remoteId);
        final DatabaseReference globalRatingRef = archiveCureRef.child("global_rating");
        globalRatingRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double globalRating = (double) dataSnapshot.getValue();

                        double newGlobalRating = (globalRating + rating) / 2;
                        // TODO this seems like domain logic it probably shouldn't sit here in the adapter
                        if (newGlobalRating < 0) {
                            newGlobalRating = 0;
                        } else if (newGlobalRating > 5) {
                            newGlobalRating = 5;
                        }

                        globalRatingRef.setValue(newGlobalRating);
                        log.d("New rating for remote " + remoteId + " id saved " + newGlobalRating);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // not sure when cancelled will happen, but meh don't do anything
                        // nobody will notice :o ...
                    }

                }
        );
    }

    @Override
    public CureRating retrieveCureRating(Cure.Id id) {
        final CountDownLatch latch = new CountDownLatch(1);
        final CureRating[] cureRatings = new CureRating[1];
        getFirebaseCureRating(
                id.asInt(),
                new Callback() {
                    @Override
                    public void onLoaded(FirebaseCureRating firebaseCureRating) {
                        CureRating cureRating = marshallToCureRating(firebaseCureRating);
                        cureRatings[0] = cureRating;
                        latch.countDown();
                    }
                }
        );

        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // we will return an empty list
        }

        return cureRatings[0];
    }

    private void getFirebaseCureRating(int id, Callback callback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        int remoteId = id - 1;
        if (currentUser == null) {
            signInBeforeRetrieveCureRating(firebaseAuth, remoteId, callback);
        } else {
            String userId = currentUser.getUid();
            retrieveCureRating(remoteId, userId, callback);
        }
    }

    private void signInBeforeRetrieveCureRating(FirebaseAuth firebase, final int remoteId, final Callback callback) {
        firebase.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String userId = task.getResult().getUser().getUid();
                        retrieveCureRating(remoteId, userId, callback);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        log.e("auth error " + e);
                        // Failing to sign in means they could be offline (and the current session has expired)
                        // We will either use cached data or show a blank screen
                    }
                });
    }

    private void retrieveCureRating(int remoteId, String userId, final Callback callback) {
        DatabaseReference userCureRef = FirebaseDatabase.getInstance().getReferenceFromUrl(BASE + "/users/" + userId + "/archive-cures/" + remoteId);
        userCureRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FirebaseCureRating rating = dataSnapshot.getValue(FirebaseCureRating.class);
                        callback.onLoaded(rating);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // not sure when cancelled will happen, but meh don't do anything
                        // nobody will notice :o ...
                    }

                });
    }

    private CureRating marshallToCureRating(FirebaseCureRating firebaseCureRating) {
        if (firebaseCureRating == null) { //i.e. they haven't set one yet, can prob do this better
            return new CureRating(0);
        } else {
            return new CureRating(firebaseCureRating.rating);
        }
    }

    public static class FirebaseCureRating {
        public int rating;

        public FirebaseCureRating() {
            // empty default constructor, necessary for Firebase to be able to deserialize
        }
    }

    private interface Callback {
        void onLoaded(FirebaseCureRating firebaseCureRating);
    }
}
