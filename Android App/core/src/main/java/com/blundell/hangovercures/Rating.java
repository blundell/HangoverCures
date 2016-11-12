package com.blundell.hangovercures;

import java.io.Serializable;

public class Rating implements Serializable {

    private final int rating;

    public static Rating from(double rating) {
        return new Rating((int) rating);
    }

    Rating(int rating) {
        this.rating = rating;
    }

    public int asInt() {
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

        Rating rating1 = (Rating) o;

        return rating == rating1.rating;

    }

    @Override
    public int hashCode() {
        return rating;
    }

    @Override
    public String toString() {
        return String.valueOf(rating);
    }
}
