package com.blundell.hangovercures.details;

public class CureRating {
    private final int rating;

    public CureRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CureRating that = (CureRating) o;

        return rating == that.rating;

    }

    @Override
    public int hashCode() {
        return rating;
    }
}
