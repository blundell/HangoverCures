package com.blundell.hangovercures.archive;

import com.blundell.hangovercures.AndroidLog;
import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.HangoverCuresApplication;
import com.blundell.hangovercures.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

class ArchivePresenter implements
    ArchiveMvp.Presenter,
    ArchiveMvp.Model.CuresLoadedCallback,
    ArchiveMvp.Model.AdvertRequestLoadedCallback {

    private final ArchiveMvp.Model model;
    private final ArchiveMvp.View view;

    public static ArchiveMvp.Presenter newInstance(HangoverCuresApplication dependencyProvider, ArchiveMvp.View view) {
        Log logger = new AndroidLog();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ArchiveRepository repository = new FirebaseArchiveRepository(firebaseAuth);
        ArchiveUseCase archiveUseCase = new ArchiveUseCase(repository);
        UseCaseModelAdapter modelAdapter = new UseCaseModelAdapter(archiveUseCase);
        ArchiveMvp.Model model = new ArchiveModel(modelAdapter, logger);
        Tracker tracker = dependencyProvider.getAnalyticsTracker();
        return new ArchivePresenter(model, new ArchiveAnalyticsView(tracker, view));
    }

    ArchivePresenter(ArchiveMvp.Model model, ArchiveMvp.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.create();
        model.loadAdvertRequest(this);
    }

    @Override
    public void onLoaded(AdRequest advertRequest) {
        view.show(advertRequest);
    }

    @Override
    public void onResume() {
        model.loadCures(this);

        view.resumeAdvert();
    }

    @Override
    public void onLoaded(List<Cure> hangoverCures) {
        view.show(hangoverCures);
    }

    @Override
    public void onSelected(Cure cure) {
        view.navigateToDetailsFor(cure);
    }

    @Override
    public void onPause() {
        view.pauseAdvert();
    }

    @Override
    public void onDestroy() {
        view.destroyAdvert();
    }
}
