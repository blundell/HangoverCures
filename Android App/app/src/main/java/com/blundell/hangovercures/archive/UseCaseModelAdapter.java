package com.blundell.hangovercures.archive;

import com.blundell.hangovercures.*;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Here we convert from the inner model of our use case model, to the model of MVP
 */
class UseCaseModelAdapter {
    private final ArchiveUseCase useCase;

    public UseCaseModelAdapter(ArchiveUseCase useCase) {
        this.useCase = useCase;
    }

    public Observable.OnSubscribe<List<Cure>> getCures() {
        return new Observable.OnSubscribe<List<Cure>>() {
            @Override
            public void call(Subscriber<? super List<Cure>> subscriber) {
                subscriber.onNext(useCase.getCures());
                subscriber.onCompleted();
            }
        };
    }

    public Observable.OnSubscribe<AdRequest> getAdvertRequest() {
        return new Observable.OnSubscribe<AdRequest>() {
            @Override
            public void call(Subscriber<? super AdRequest> subscriber) {
                AdRequest.Builder builder = new AdRequest.Builder();
                AdvertRequest advertRequest = useCase.createAdvertRequest();
                if (advertRequest.shouldUseEmulatorForTestRequests()) {
                    builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
                }
                AdRequest adRequest = builder.build();
                subscriber.onNext(adRequest);
                subscriber.onCompleted();
            }
        };
    }
}
