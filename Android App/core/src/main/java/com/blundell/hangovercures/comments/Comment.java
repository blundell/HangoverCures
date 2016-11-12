package com.blundell.hangovercures.comments;

import org.joda.time.DateTime;

public class Comment {

    private final Id id;
    private final String username;
    private final DateTime commentedAt;
    private final String message;
    private final int votesUp;
    private final int votesDown;

    public Comment(Id id, String username, DateTime commentedAt, String message, int votesUp, int votesDown) {
        this.id = id;
        this.username = username;
        this.commentedAt = commentedAt;
        this.message = message;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
    }

    public Id getId() {
        return id;
    }

    public String getAuthor() {
        return username;
    }

    public DateTime getCommentedAt() {
        return commentedAt;
    }

    public String getMessage() {
        return message;
    }

    public int getVotesUp() {
        return votesUp;
    }

    public int getVotesDown() {
        return votesDown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Comment comment = (Comment) o;

        return id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + commentedAt.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + votesUp;
        result = 31 * result + votesDown;
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", commentedAt=" + commentedAt +
            ", message='" + message + '\'' +
            ", votesUp=" + votesUp +
            ", votesDown=" + votesDown +
            '}';
    }

    public static class Id {
        private final String rawValue;

        public Id(String rawValue) {
            this.rawValue = rawValue;
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

        @Override
        public String toString() {
            return rawValue;
        }
    }
}
