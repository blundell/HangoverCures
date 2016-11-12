package com.blundell.hangovercures.archive;

public interface User {

    boolean isSignedIn();

    String getUserId();

    class SignedInUser implements User {

        private final String userId;

        public SignedInUser(String userId) {
            this.userId = userId;
        }

        @Override
        public boolean isSignedIn() {
            return true;
        }

        @Override
        public String getUserId() {
            return userId;
        }
    }

    class SignedOutUser implements User {

        @Override
        public boolean isSignedIn() {
            return false;
        }

        @Override
        public String getUserId() {
            throw new IllegalStateException("A non signed out user does not have a user id. Did you check isSignedIn()?");
        }
    }
}
