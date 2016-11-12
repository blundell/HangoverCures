package com.blundell.hangovercures.archive;

import com.blundell.hangovercures.AdvertRequest;
import com.blundell.hangovercures.Cure;

import java.util.List;

class ArchiveUseCase {

    private static final int MAX_RETRIES = 3;

    private final ArchiveRepository repository;

    private int signInAttempt;

    public ArchiveUseCase(ArchiveRepository repository) {
        this.repository = repository;
    }

    public List<Cure> getCures() {
        signInAttempt++;
        if (signInAttempt > MAX_RETRIES) {
            throw new UseCaseRequestFailedException();
        }
        User user = repository.getUser();
        if (user.isSignedIn()) {
            signInAttempt = 0;
            return repository.getCures();
        } else {
            repository.signInUser();
            return getCures(); // recursion ew
        }
    }

    public AdvertRequest createAdvertRequest() {
        return new AdvertRequest();
    }

    public static class UseCaseRequestFailedException extends RuntimeException {

        public UseCaseRequestFailedException() {
            super("Backend failure, we tried multiple times.");
        }

    }
}
