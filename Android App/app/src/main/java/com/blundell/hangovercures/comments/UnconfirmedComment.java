package com.blundell.hangovercures.comments;

import org.joda.time.DateTime;

public class UnconfirmedComment implements ViewComment {

    private final String author;
    private final DateTime commentedAt;
    private final String message;

    public UnconfirmedComment(String author, DateTime commentedAt, String message) {
        this.author = author;
        this.commentedAt = commentedAt;
        this.message = message;
    }

    @Override
    public Comment.Id getId() {
        throw new IllegalStateException("Unconfirmed comments should never have an id.");
    }

    @Override
    public boolean isConfirmed() {
        return false;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public DateTime getCommentedAt() {
        return commentedAt;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ViewComment voteUp() {
        throw new IllegalStateException("Can't vote on an unconfirmed comment as the server does not know about it.");
    }

    @Override
    public ViewComment voteDown() {
        throw new IllegalStateException("Can't vote on an unconfirmed comment as the server does not know about it.");
    }

    @Override
    public int getVotesUp() {
        return 0;
    }

    @Override
    public int getVotesDown() {
        return 0;
    }

    @Override
    public boolean isEqualTo(ViewComment viewComment) {
        return author.equals(viewComment.getAuthor())
            && commentedAt.equals(viewComment.getCommentedAt())
            && message.equals(viewComment.getMessage());
    }
}
