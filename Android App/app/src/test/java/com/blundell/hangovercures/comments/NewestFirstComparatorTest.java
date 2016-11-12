package com.blundell.hangovercures.comments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class NewestFirstComparatorTest {

    @Test
    public void listsAreSortedMostNewestFirst() throws Exception {
        List<ViewComment> list = new ArrayList<>();
        list.add(getViewComment("A", "2016-09-30"));
        list.add(getViewComment("B", "2016-10-31"));
        list.add(getViewComment("C", "2016-12-31"));
        list.add(getViewComment("D", "2016-11-30"));
        list.add(getViewComment("E", "2016-09-30"));

        Collections.sort(list, SortOrder.NEWEST_FIRST);

        assertOrder(list, "C", "D", "B", "A", "E");
    }

    @Test
    public void whenLeftHasNewerDate_thenLeftIsPreferred() throws Exception {
        ViewComment lhs = getViewComment("2016-12-31");
        ViewComment rhs = getViewComment("2016-12-30");

        int result = SortOrder.NEWEST_FIRST.compare(lhs, rhs);

        assertEquals(-1, result);
    }

    @Test
    public void whenRightHasNewerDate_thenRightIsPreferred() throws Exception {
        ViewComment lhs = getViewComment("2016-12-30");
        ViewComment rhs = getViewComment("2016-12-31");

        int result = SortOrder.NEWEST_FIRST.compare(lhs, rhs);

        assertEquals(1, result);
    }

    @Test
    public void whenSameDate_thenRightIsPreferred() throws Exception {
        ViewComment lhs = getViewComment("2016-12-31");
        ViewComment rhs = getViewComment("2016-12-31");

        int result = SortOrder.NEWEST_FIRST.compare(lhs, rhs);

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

    private ViewComment getViewComment(String commentedAt) {
        return getViewComment("", commentedAt);
    }

    private ViewComment getViewComment(final String id, final String commentedAt) {
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
                return DateTime.parse(commentedAt);
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
                return 0;
            }

            @Override
            public int getVotesDown() {
                return 0;
            }

            @Override
            public boolean isEqualTo(ViewComment viewComment) {
                return false;
            }
        };
    }
}
