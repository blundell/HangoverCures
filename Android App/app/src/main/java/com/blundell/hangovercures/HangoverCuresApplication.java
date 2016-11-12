package com.blundell.hangovercures;

import android.app.Application;

import com.blundell.hangovercures.free.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

public class HangoverCuresApplication extends Application {

    private Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        new OptionalDependencies().initialise(this);
    }

    public Tracker getAnalyticsTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.google_analytics_tracking);
        }
        return tracker;
    }
}
