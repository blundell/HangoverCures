package com.blundell.hangovercures.archive;

import com.blundell.hangovercures.Cure;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

public class ArchiveAnalyticsView implements ArchiveMvp.View {

    private final Tracker tracker;
    private final ArchiveMvp.View view;

    public ArchiveAnalyticsView(Tracker tracker, ArchiveMvp.View view) {
        this.tracker = tracker;
        this.view = view;
    }

    @Override
    public void create() {
        tracker.setScreenName("Archive");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        view.create();
    }

    @Override
    public void show(List<Cure> hangoverCures) {
        view.show(hangoverCures);
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
    public void navigateToDetailsFor(Cure cure) {
        view.navigateToDetailsFor(cure);
    }
}
