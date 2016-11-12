package com.blundell.hangovercures.details;

import com.blundell.hangovercures.Cure;
import com.google.android.gms.ads.AdRequest;

import java.io.Closeable;

class DetailsMvp {

    interface Model extends Closeable {
        void saveRating(Cure cure, int newRating);

        @Override
        void close();

        void loadAdvertRequest(AdvertRequestLoadedCallback callback);

        void loadRatingFor(Cure cure, CureRatingLoadedCallback callback);

        interface AdvertRequestLoadedCallback {
            void onLoaded(AdRequest adRequest);
        }

        interface CureRatingLoadedCallback {
            void onLoaded(CureRating cureRating);
        }
    }

    interface View {
        void create();

        void show(Cure cure);

        void show(AdRequest advertRequest);

        void resumeAdvert();

        void pauseAdvert();

        void destroyAdvert();

        void show(CureRating cureRating);

        void navigateToCommentsFor(Cure cure);
    }

    interface Presenter {
        void onCreate(Cure cure);

        void onNewRating(double rating);

        void onResume();

        void onPause();

        void onDestroy();

        void onSelectedViewCommentsFor(Cure cure);
    }

}
