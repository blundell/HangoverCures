package com.blundell.hangovercures.comments;

import com.blundell.hangovercures.Cure;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

public class CommentsAnalyticsView implements CommentsMvp.View {

    private final Tracker tracker;
    private final CommentsMvp.View view;

    public CommentsAnalyticsView(Tracker tracker, CommentsMvp.View view) {
        this.tracker = tracker;
        this.view = view;
    }

    @Override
    public void create() {
        tracker.setScreenName("Comments");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        view.create();
    }

    @Override
    public void show(Cure cure) {
        view.show(cure);
    }

    @Override
    public void show(String username) {
        view.show(username);
    }

    @Override
    public void show(AdRequest advertRequest) {
        view.show(advertRequest);
    }

    @Override
    public void show(List<ViewComment> comments) {
        view.show(comments);
    }

    @Override
    public void showInteractionsFor(ViewComment comment) {
        tracker.setScreenName("View Comment");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        view.showInteractionsFor(comment);
    }

    @Override
    public void update(ViewComment comment) {
        view.update(comment);
    }

    @Override
    public void notifyAlreadyVoted() {
        view.notifyAlreadyVoted();
    }

}
