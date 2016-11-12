package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.Cure;
import com.google.android.gms.ads.AdRequest;

import java.io.Closeable;
import java.util.List;

class CommentsMvp {

    public interface Model extends Closeable {

        void saveComment(Cure.Id id, ViewComment comment, CommentSavedCallback callback);

        void loadUser(UserLoadedCallback callback);

        void loadAdvertRequest(AdvertRequestLoadedCallback callback);

        void loadCommentsFor(Cure cure, SortOrder sortOrder, CommentsLoadedCallback callback);

        void saveVoteFor(Cure.Id id, ViewComment comment, Vote down, CommentUpdatedCallback callback);

        boolean canVoteOnCommentWith(Cure.Id cureId, Comment.Id commentId);

        interface CommentSavedCallback {
            void onSaved(ViewComment comment);
        }

        interface UserLoadedCallback {
            void onLoaded(String username); // TODO this can be an object containing also users profile pic
        }

        interface AdvertRequestLoadedCallback {
            void onLoaded(AdRequest advertRequest);
        }

        interface CommentsLoadedCallback {
            void onLoaded(List<ViewComment> comments);
        }

        interface CommentUpdatedCallback {
            void onUpdated(ViewComment comment);
        }

    }

    public interface View {
        void create();

        void show(Cure cure);

        void show(String username);

        void show(AdRequest advertRequest);

        void show(List<ViewComment> comments);

        void showInteractionsFor(ViewComment comment);

        void update(ViewComment comment);

        void notifyAlreadyVoted();
    }

    public interface Presenter {
        void onCreate(Cure cure);

        void onCommented(String comment);

        void onSelected(ViewComment comment);

        void onSelectedVoteUp(ViewComment comment);

        void onSelectedVoteDown(ViewComment comment);

        void onSelectedFilterCommentsByMostPopularFirst();

        void onSelectedFilterCommentByNewestFirst();
    }

}
