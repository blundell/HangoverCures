package com.blundell.hangovercures.details;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.Log;
import com.google.android.gms.ads.AdRequest;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DetailsModel implements DetailsMvp.Model {

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private final UseCaseModelAdapter modelAdapter;
    private final Log log;

    public DetailsModel(UseCaseModelAdapter modelAdapter, Log log) {
        this.modelAdapter = modelAdapter;
        this.log = log;
    }

    @Override
    public void saveRating(Cure cure, int newRating) {
        Subscription subscription = Observable.create(modelAdapter.saveRating(cure, newRating))
            .subscribeOn(Schedulers.io())
            .subscribe();
        subscriptions.add(subscription);
    }

    @Override
    public void close() {
        subscriptions.clear();
    }

    @Override
    public void loadAdvertRequest(final AdvertRequestLoadedCallback callback) {
        Subscription subscription = Observable.create(modelAdapter.createAdvertRequest())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Observer<AdRequest>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        // oh well no adverts
                        log.e("Failed in get adverts request", e);
                    }

                    @Override
                    public void onNext(AdRequest adRequest) {
                        callback.onLoaded(adRequest);
                    }
                }
            );
        subscriptions.add(subscription);
    }

    @Override
    public void loadRatingFor(Cure cure, final CureRatingLoadedCallback callback) {
        Subscription subscription = Observable.create(modelAdapter.createCureRatingRequest(cure))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Observer<CureRating>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        log.e("failed to load cure rating.", e);
                    }

                    @Override
                    public void onNext(CureRating cureRating) {
                        callback.onLoaded(cureRating);
                    }
                }
            );
        subscriptions.add(subscription);
    }
}
