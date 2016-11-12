package com.blundell.hangovercures;

import java.io.Serializable;

public class Cure implements Serializable {

    private final Cure.Id id;
    private final String title;
    private final String description;
    private final Rating globalRating;

    public Cure(Cure.Id id, String title, String description, Rating globalRating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.globalRating = globalRating;
    }

    public Id getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Rating getGlobalRating() {
        return globalRating;
    }

    public static class Id implements Serializable {
        private final String rawValue;

        public Id(long rawValue) {
            this.rawValue = String.valueOf(rawValue);
        }

        public Id(String rawValue) {
            this.rawValue = rawValue;
        }

        public int asInt() {
            return Integer.valueOf(rawValue);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Id id = (Id) o;

            return rawValue.equals(id.rawValue);

        }

        @Override
        public int hashCode() {
            return rawValue.hashCode();
        }
    }
}
