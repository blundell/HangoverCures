package com.blundell.hangovercures.details;

import com.blundell.hangovercures.Cure;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class DetailsAnalyticsView implements DetailsMvp.View {

    private final Tracker tracker;
    private final DetailsMvp.View view;

    public DetailsAnalyticsView(Tracker tracker, DetailsMvp.View view) {
        this.tracker = tracker;
        this.view = view;
    }

    @Override
    public void create() {
        tracker.setScreenName("Details");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        view.create();
    }

    @Override
    public void show(Cure cure) {
        tracker.send(
            new HitBuilders.EventBuilder()
                .setCategory("Details")
                .setAction("View")
                .setLabel(cure.getTitle())
                .build()
        );
        view.show(cure);
    }

    @Override
    public void show(AdRequest advertRequest) {
        view.show(advertRequest);
    }

    @Override
    public void resumeAdvert() {
        view.resumeAdvert();
    }

    @Override
    public void pauseAdvert() {
        view.pauseAdvert();
    }

    @Override
    public void destroyAdvert() {
        view.destroyAdvert();
    }

    @Override
    public void show(CureRating cureRating) {
        view.show(cureRating);
    }

    @Override
    public void navigateToCommentsFor(Cure cure) {
        view.navigateToCommentsFor(cure);
    }
}
