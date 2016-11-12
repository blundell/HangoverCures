package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.AndroidLog;
import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.HangoverCuresApplication;
import com.blundell.hangovercures.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import org.joda.time.DateTime;

class CommentsPresenter implements
    CommentsMvp.Presenter,
    CommentsMvp.Model.AdvertRequestLoadedCallback,
    CommentsMvp.Model.UserLoadedCallback,
    CommentsMvp.Model.CommentSavedCallback,
    CommentsMvp.Model.CommentsLoadedCallback,
    CommentsMvp.Model.CommentUpdatedCallback {

    private final CommentsMvp.Model model;
    private final CommentsMvp.View view;

    private String username;
    private Cure cure;

    public static CommentsMvp.Presenter newInstance(HangoverCuresApplication dependencyProvider, CommentsMvp.View view) {
        Log log = new AndroidLog();
        FirebaseCommentRepository firebase = new FirebaseCommentRepository();
        SharedPrefsVoteRepository sharedPrefsVoteRepository = SharedPrefsVoteRepository.newInstance(dependencyProvider.getApplicationContext());
        CommentUseCase commentUseCase = new CommentUseCase(firebase);
        VotingUseCase votingUseCase = new VotingUseCase(sharedPrefsVoteRepository);
        UseCaseModelAdapter useCaseModelAdapter = new UseCaseModelAdapter(commentUseCase, votingUseCase);
        CommentsMvp.Model model = new CommentsModel(useCaseModelAdapter, log);
        Tracker tracker = dependencyProvider.getAnalyticsTracker();
        return new CommentsPresenter(model, new CommentsAnalyticsView(tracker, view));
    }

    CommentsPresenter(CommentsMvp.Model model, CommentsMvp.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void onCreate(Cure cure) {
        this.cure = cure;
        view.create();
        view.show(cure);
        model.loadAdvertRequest(this);
        model.loadUser(this);
        model.loadCommentsFor(cure, SortOrder.MOST_POPULAR_FIRST, this);
    }

    @Override
    public void onLoaded(AdRequest advertRequest) {
        view.show(advertRequest);
    }

    @Override
    public void onLoaded(String username) {
        this.username = username;
        view.show(username);
    }

    @Override
    public void onCommented(String commentText) {
        if (commentText.trim().isEmpty()) {
            return;
        }
        ViewComment viewComment = new UnconfirmedComment(username, DateTime.now(), commentText);
        view.update(viewComment);
        model.saveComment(cure.getId(), viewComment, this);
    }

    @Override
    public void onSelected(ViewComment comment) {
        view.showInteractionsFor(comment);
    }

    @Override
    public void onSelectedVoteUp(ViewComment comment) {
        boolean canVote = model.canVoteOnCommentWith(cure.getId(), comment.getId());
        if (canVote) {
            ViewComment votedComment = comment.voteUp(); // TODO surely this is the models job to decide what a vote is (i.e. + 1)
            view.update(votedComment);
            model.saveVoteFor(cure.getId(), votedComment, Vote.UP, this);
        } else {
            view.notifyAlreadyVoted();
        }
    }

    @Override
    public void onSelectedVoteDown(ViewComment comment) {
        boolean canVote = model.canVoteOnCommentWith(cure.getId(), comment.getId());
        if (canVote) {
            ViewComment votedComment = comment.voteDown();
            view.update(votedComment);
            model.saveVoteFor(cure.getId(), votedComment, Vote.DOWN, this);
        } else {
            view.notifyAlreadyVoted();
        }
    }

    @Override
    public void onSelectedFilterCommentsByMostPopularFirst() {
        model.loadCommentsFor(cure, SortOrder.MOST_POPULAR_FIRST, this);
    }

    @Override
    public void onSelectedFilterCommentByNewestFirst() {
        model.loadCommentsFor(cure, SortOrder.NEWEST_FIRST, this);
    }

    @Override
    public void onSaved(ViewComment comment) {
        view.update(comment);
    }

    @Override
    public void onLoaded(List<ViewComment> comments) {
        view.show(comments);
    }

    @Override
    public void onUpdated(ViewComment comment) {
        // don't really care :-) for example this is after we have saved an up vote
    }
}
