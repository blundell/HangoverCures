package com.blundell.hangovercures.comments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MostPopularFirstComparatorTest {

    @Test
    public void listsAreSortedMostPositiveAggregatedVotesFirst() throws Exception {
        List<ViewComment> list = new ArrayList<>();
        list.add(getViewComment("A", 1, 5));
        list.add(getViewComment("B", 5, 5));
        list.add(getViewComment("C", 5, 1));
        list.add(getViewComment("D", 3, 1));
        list.add(getViewComment("E", 0, 99));

        Collections.sort(list, SortOrder.MOST_POPULAR_FIRST);

        assertOrder(list, "C", "D", "B", "A", "E");
    }

    @Test
    public void whenLeftHasMostUpVotes_thenLeftIsPreferred() throws Exception {
        ViewComment lhs = getViewComment(2, 1);
        ViewComment rhs = getViewComment(1, 1);

        int result = SortOrder.MOST_POPULAR_FIRST.compare(lhs, rhs);

        assertEquals(-1, result);
    }

    @Test
    public void whenRightHasMostUpVotes_thenRightIsPreferred() throws Exception {
        ViewComment lhs = getViewComment(1, 1);
        ViewComment rhs = getViewComment(2, 1);

        int result = SortOrder.MOST_POPULAR_FIRST.compare(lhs, rhs);

        assertEquals(1, result);
    }

    @Test
    public void whenSameVotes_thenRightIsPreferred() throws Exception {
        ViewComment lhs = getViewComment(1, 1);
        ViewComment rhs = getViewComment(1, 1);

        int result = SortOrder.MOST_POPULAR_FIRST.compare(lhs, rhs);

        assertEquals(1, result);
    }

    private void assertOrder(List<ViewComment> list, String... ids) {
        for (int i = 0; i < ids.length; i++) {
            assertEquals(list.get(i).getId().toString(), ids[i]);
        }
        String message = "assertOrder expects you assert the whole list (" + list.size() + "). " +
            "Only got (" + ids.length + ") ids.";
        assertEquals(message, list.size(), ids.length);
    }

    private ViewComment getViewComment(final int votesUp, final int votesDown) {
        return getViewComment("", votesUp, votesDown);
    }

    private ViewComment getViewComment(final String id, final int votesUp, final int votesDown) {
        return new ViewComment() {
            @Override
            public Comment.Id getId() {
                return new Comment.Id(id);
            }

            @Override
            public boolean isConfirmed() {
                return false;
            }

            @Override
            public String getAuthor() {
                return null;
            }

            @Override
            public DateTime getCommentedAt() {
                return null;
            }

            @Override
            public String getMessage() {
                return null;
            }

            @Override
            public ViewComment voteUp() {
                return null;
            }

            @Override
            public ViewComment voteDown() {
                return null;
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
            public boolean isEqualTo(ViewComment viewComment) {
                return false;
            }
        };
    }
}
