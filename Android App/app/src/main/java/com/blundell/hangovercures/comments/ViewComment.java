package com.blundell.hangovercures.comments;

import org.joda.time.DateTime;

public interface ViewComment {

    Comment.Id getId(); // foobar this method should not exist, it couples the two domain objects

    boolean isConfirmed();

    String getAuthor();

    DateTime getCommentedAt();

    String getMessage();

    ViewComment voteUp();

    ViewComment voteDown();

    int getVotesUp();

    int getVotesDown();

    boolean isEqualTo(ViewComment viewComment);
}
