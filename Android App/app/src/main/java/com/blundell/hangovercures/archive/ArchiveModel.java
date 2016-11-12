package com.blundell.hangovercures.archive;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.Log;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class ArchiveModel implements ArchiveMvp.Model {

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private final UseCaseModelAdapter modelAdapter;
    private final Log log;

    public ArchiveModel(UseCaseModelAdapter modelAdapter, Log log) {
        this.modelAdapter = modelAdapter;
        this.log = log;
    }

    @Override
    public void loadCures(final CuresLoadedCallback callback) {
        Subscription subscription = Observable.create(modelAdapter.getCures())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                new Observer<List<Cure>>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO, fail gracefully
                        log.e("Failed to get cures.", e);
                    }

                    @Override
                    public void onNext(List<Cure> cures) {
                        callback.onLoaded(cures);
                    }
                }
            );
        subscriptions.add(subscription);
    }

    @Override
    public void loadAdvertRequest(final AdvertRequestLoadedCallback callback) {
        Observable.create(modelAdapter.getAdvertRequest())
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
    }

    @Override
    public void close() {
        subscriptions.clear();
    }
}
