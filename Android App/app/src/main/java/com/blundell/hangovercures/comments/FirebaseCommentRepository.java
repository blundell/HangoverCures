package com.blundell.hangovercures.comments;

import android.support.annotation.NonNull;
import android.util.Log;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.archive.User;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

public class FirebaseCommentRepository implements CommentRepository {

    private static final int TIMEOUT = 20;
    private static final String BASE = "https://hangovercures.firebaseio.com";

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
                    }
                });
    }

    private interface AuthCallback {
        void onAuthenticated();
    }

    @Override
    public User getUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            return new User.SignedOutUser();
        } else {
            return new User.SignedInUser(currentUser.getUid());
        }
    }

    @Override
    public Comment.Id saveComment(Cure.Id id, Comment comment) {
        String userId = getUser().isSignedIn() ? getUser().getUserId() : "0";
        return saveFirebaseComment(id.asInt(), userId, comment);
    }

    private Comment.Id saveFirebaseComment(int cureId, String userId, Comment comment) {
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReferenceFromUrl(BASE + "/archive-cures-comments/" + cureId);
        DatabaseReference newCommentRef = firebase.push();

        Map<String, Object> firebaseComment = new HashMap<>();
        firebaseComment.put("author", comment.getAuthor());
        firebaseComment.put("user_id", userId);
        firebaseComment.put("text", comment.getMessage());
        firebaseComment.put("commented_at", comment.getCommentedAt().getMillis());
        firebaseComment.put("votes_up", comment.getVotesUp());
        firebaseComment.put("votes_down", comment.getVotesDown());
        newCommentRef.setValue(firebaseComment);

        return new Comment.Id(newCommentRef.getKey());
    }

    @Override
    public List<Comment> readComments(Cure.Id cureId) {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<Comment> comments = new ArrayList<>();
        getFirebaseComments(
                cureId.asInt(),
                new CommentsCallback() {
                    @Override
                    public void onLoaded(List<FirebaseComment> firebaseComments) {
                        List<Comment> cureComments = marshallToComments(firebaseComments);
                        comments.addAll(cureComments);
                        latch.countDown();
                    }
                }
        );

        try {
            latch.await(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // we will return an empty list
        }

        return comments;
    }

    @Override
    public void updateComment(Cure.Id cureId, Comment comment) {
        updateFirebaseComment(cureId.asInt(), comment);
    }

    private void updateFirebaseComment(int cureId, Comment comment) {
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReferenceFromUrl(BASE + "/archive-cures-comments/" + cureId + "/" + comment.getId());
        Map<String, Object> commentUpdate = new HashMap<>();
        commentUpdate.put("text", comment.getMessage());
        commentUpdate.put("votes_up", comment.getVotesUp());
        commentUpdate.put("votes_down", comment.getVotesDown());
        firebase.updateChildren(commentUpdate);
    }

    private void getFirebaseComments(int cureId, final CommentsCallback callback) {
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReferenceFromUrl(BASE + "/archive-cures-comments/" + cureId);
        firebase.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<FirebaseComment> firebaseComments = marshallToFirebaseComments(dataSnapshot);
                        callback.onLoaded(firebaseComments);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Not possible in this usecase ... I think
                    }
                }
        );
    }

    private List<FirebaseComment> marshallToFirebaseComments(DataSnapshot dataSnapshot) {
        List<FirebaseComment> firebaseComments = new ArrayList<>();
        for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
            FirebaseComment firebaseComment = commentSnapshot.getValue(FirebaseComment.class);
            firebaseComment.id = commentSnapshot.getKey();
            firebaseComments.add(firebaseComment);
        }
        return firebaseComments;
    }

    private List<Comment> marshallToComments(List<FirebaseComment> firebaseComments) {
        List<Comment> comments = new ArrayList<>();
        for (FirebaseComment firebaseComment : firebaseComments) {
            Comment comment = marshallToComment(firebaseComment);
            comments.add(comment);
        }
        return comments;
    }

    private Comment marshallToComment(FirebaseComment firebaseComment) {
        Comment.Id id = new Comment.Id(firebaseComment.id);
        String author = firebaseComment.author;
        DateTime commentedAt = new DateTime(firebaseComment.commented_at);
        String text = firebaseComment.text;
        String userId = firebaseComment.user_id; // Maybe one day we'll use this
        int votesDown = firebaseComment.votes_down;
        int votesUp = firebaseComment.votes_up;
        return new Comment(id, author, commentedAt, text, votesUp, votesDown);
    }

    public static class FirebaseComment {

        public String id;
        public String author;
        public long commented_at;
        public String text;
        public String user_id;
        public int votes_down;
        public int votes_up;

        public FirebaseComment() {
            // empty default constructor, necessary for Firebase to be able to deserialize
        }

    }

    private interface CommentsCallback {
        void onLoaded(List<FirebaseComment> cures);
    }
}
