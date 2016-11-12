package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.AdvertRequest;
import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.archive.User;

import java.util.List;

class CommentUseCase {

    private static final int MAX_RETRIES = 3;

    private final CommentRepository repository;

    private int signInAttempt;

    public CommentUseCase(CommentRepository repository) {
        this.repository = repository;
    }

    public Comment.Id saveComment(Cure.Id cureId, Comment comment) {
        signInAttempt++;
        if (signInAttempt > MAX_RETRIES) {
            throw new UseCaseRequestFailedException();
        }
        User user = repository.getUser();
        if (user.isSignedIn()) {
            signInAttempt = 0;
            return repository.saveComment(cureId, comment);
        } else {
            repository.signInUser();
            return saveComment(cureId, comment); // recursion ew
        }
    }

    public AdvertRequest createAdvertRequest() {
        return new AdvertRequest();
    }

    public List<Comment> getComments(Cure.Id cureId) {
        signInAttempt++;
        if (signInAttempt > MAX_RETRIES) {
            throw new UseCaseRequestFailedException();
        }
        User user = repository.getUser();
        if (user.isSignedIn()) {
            signInAttempt = 0;
            return repository.readComments(cureId);
        } else {
            repository.signInUser();
            return getComments(cureId); // recursion ew
        }
    }

    public void updateComment(Cure.Id cureId, Comment comment) {
        signInAttempt++;
        if (signInAttempt > MAX_RETRIES) {
            throw new UseCaseRequestFailedException();
        }
        User user = repository.getUser();
        if (user.isSignedIn()) {
            signInAttempt = 0;
            repository.updateComment(cureId, comment);
        } else {
            repository.signInUser();
            updateComment(cureId, comment); // recursion ew
        }
    }

    public static class UseCaseRequestFailedException extends RuntimeException {

        public UseCaseRequestFailedException() {
            super("Backend failure, we tried multiple times.");
        }

    }
}
