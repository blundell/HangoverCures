package com.blundell.hangovercures.details;

import com.blundell.hangovercures.AndroidLog;
import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.HangoverCuresApplication;
import com.blundell.hangovercures.Log;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.analytics.Tracker;

public class DetailsPresenter implements
    DetailsMvp.Presenter,
    DetailsMvp.Model.AdvertRequestLoadedCallback,
    DetailsMvp.Model.CureRatingLoadedCallback {

    private final DetailsMvp.Model model;
    private final DetailsMvp.View view;
    private Cure cure;

    public static DetailsMvp.Presenter newInstance(HangoverCuresApplication dependencyProvider, DetailsMvp.View view) {
        Log log = new AndroidLog();
        DetailsRepository repository = new FirebaseDetailsRepository(log);
        DetailsUseCase useCase = new DetailsUseCase(repository);
        UseCaseModelAdapter useCaseModelAdapter = new UseCaseModelAdapter(useCase);
        DetailsMvp.Model model = new DetailsModel(useCaseModelAdapter, log);
        Tracker tracker = dependencyProvider.getAnalyticsTracker();
        return new DetailsPresenter(model, new DetailsAnalyticsView(tracker, view));
    }

    DetailsPresenter(DetailsMvp.Model model, DetailsMvp.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void onCreate(Cure cure) {
        this.cure = cure;
        view.create();
        view.show(cure);
        model.loadAdvertRequest(this);
        model.loadRatingFor(cure, this);
    }

    @Override
    public void onLoaded(AdRequest adRequest) {
        view.show(adRequest);
    }

    @Override
    public void onLoaded(CureRating cureRating) {
        view.show(cureRating);
    }

    @Override
    public void onNewRating(double rating) {
        model.saveRating(cure, (int) rating);
    }

    @Override
    public void onResume() {
        view.resumeAdvert();
    }

    @Override
    public void onPause() {
        view.pauseAdvert();
    }

    @Override
    public void onDestroy() {
        view.destroyAdvert();
    }

    @Override
    public void onSelectedViewCommentsFor(Cure cure) {
        view.navigateToCommentsFor(cure);
    }
}
