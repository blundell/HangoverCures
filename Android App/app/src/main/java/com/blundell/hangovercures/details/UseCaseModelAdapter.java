package com.blundell.hangovercures.details;

import com.blundell.hangovercures.AdvertRequest;
import com.blundell.hangovercures.Cure;
import com.google.android.gms.ads.AdRequest;

import rx.Observable;
import rx.Subscriber;

class UseCaseModelAdapter {

    private final DetailsUseCase useCase;

    UseCaseModelAdapter(DetailsUseCase useCase) {
        this.useCase = useCase;
    }

    public Observable.OnSubscribe<Void> saveRating(final Cure cure, final int newRating) {
        return new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                useCase.saveCureRating(cure, newRating);
                subscriber.onCompleted();
            }
        };
    }

    public Observable.OnSubscribe<AdRequest> createAdvertRequest() {
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

    public Observable.OnSubscribe<CureRating> createCureRatingRequest(final Cure cure) {
        return new Observable.OnSubscribe<CureRating>() {
            @Override
            public void call(Subscriber<? super CureRating> subscriber) {
                subscriber.onNext(useCase.retrieveCureRating(cure));
                subscriber.onCompleted();
            }
        };
    }
}
