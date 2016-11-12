package com.blundell.hangovercures.comments;

import org.joda.time.DateTime;

public class ConfirmedComment implements ViewComment {

    private final String id;
    private final String author;
    private final DateTime commentedAt;
    private final String message;
    private final int votesUp;
    private final int votesDown;

    public ConfirmedComment(String id, String author, DateTime commentedAt, String message, int votesUp, int votesDown) {
        this.id = id;
        this.author = author;
        this.commentedAt = commentedAt;
        this.message = message;
        this.votesUp = votesUp;
        this.votesDown = votesDown;
    }

    @Override
    public Comment.Id getId() {
        return new Comment.Id(id);
    }

    @Override
    public boolean isConfirmed() {
        return true;
    }

    @Override
    public int getVotesUp() {
        return votesUp;
    }

    @Override
    public int getVotesDown() {
        return votesDown;
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
        return new ConfirmedComment(id, author, commentedAt, message, votesUp + 1, votesDown);
    }

    @Override
    public ViewComment voteDown() {
        return new ConfirmedComment(id, author, commentedAt, message, votesUp, votesDown + 1);
    }

    @Override
    public boolean isEqualTo(ViewComment viewComment) {
        return viewComment instanceof ConfirmedComment
            && id.equals(((ConfirmedComment) viewComment).id)
            && author.equals(viewComment.getAuthor())
            && commentedAt.equals(viewComment.getCommentedAt())
            && message.equals(viewComment.getMessage());
    }

}
