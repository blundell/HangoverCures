package com.blundell.hangovercures.archive;

import com.blundell.hangovercures.Cure;
import com.google.android.gms.ads.AdRequest;

import java.io.Closeable;
import java.util.List;

class ArchiveMvp {
    public interface Model extends Closeable {
        void loadCures(CuresLoadedCallback callback);

        void loadAdvertRequest(AdvertRequestLoadedCallback callback);

        @Override
        void close();

        interface CuresLoadedCallback {
            void onLoaded(List<Cure> hangoverCures);
        }

        interface AdvertRequestLoadedCallback {
            void onLoaded(AdRequest adRequest);
        }
    }

    public interface View {
        void create();

        void show(List<Cure> hangoverCures);

        void show(AdRequest advertRequest);

        void resumeAdvert();

        void pauseAdvert();

        void destroyAdvert();

        void navigateToDetailsFor(Cure cure);
    }

    public interface Presenter {
        void onCreate();

        void onSelected(Cure cure);

        void onResume();

        void onPause();

        void onDestroy();
    }
}
