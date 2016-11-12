package com.blundell.hangovercures.comments;

import java.util.Comparator;

import org.joda.time.DateTime;

public enum SortOrder implements Comparator<ViewComment> {

    NEWEST_FIRST(new NewestFirstComparator()),
    MOST_POPULAR_FIRST(new MostPopularFirstComparator());

    private final Comparator<ViewComment> comparator;

    SortOrder(Comparator<ViewComment> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(ViewComment lhs, ViewComment rhs) {
        return comparator.compare(lhs, rhs);
    }

    static class NewestFirstComparator implements Comparator<ViewComment> {
        @Override
        public int compare(ViewComment lhs, ViewComment rhs) {
            DateTime lhsCommentedAt = lhs.getCommentedAt();
            DateTime rhsCommentedAt = rhs.getCommentedAt();
            return lhsCommentedAt.isAfter(rhsCommentedAt) ? -1 : 1;
        }
    }

    static class MostPopularFirstComparator implements Comparator<ViewComment> {
        @Override
        public int compare(ViewComment lhs, ViewComment rhs) {
            int lhsVotes = lhs.getVotesUp() - lhs.getVotesDown();
            int rhsVotes = rhs.getVotesUp() - rhs.getVotesDown();
            return lhsVotes > rhsVotes ? -1 : 1;
        }
    }
}
