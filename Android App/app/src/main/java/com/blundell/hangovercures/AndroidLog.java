package com.blundell.hangovercures;

import com.blundell.hangovercures.free.BuildConfig;

public class AndroidLog implements Log {

    @Override
    public void d(String message) {
        DebugLog.d(message);
    }

    @Override
    public void e(String message) {
        DebugLog.e(message);
    }

    @Override
    public void e(String message, Throwable e) {
        DebugLog.e(message, e);
    }

    public static class DebugLog {

        private static final boolean DEBUG = BuildConfig.DEBUG;

        public static final String TAG = "HangoverCures";

        public static void d(String message) {
            if (DEBUG) {
                android.util.Log.d(TAG, message);
            }
        }

        public static void i(String message) {
            if (DEBUG) {
                android.util.Log.i(TAG, message);
            }
        }

        public static void e(String message) {
            e(message, null);
        }

        public static void e(String message, Throwable e) {
            if (DEBUG) {
                android.util.Log.e(TAG, message, e);
            }
        }
    }
}
