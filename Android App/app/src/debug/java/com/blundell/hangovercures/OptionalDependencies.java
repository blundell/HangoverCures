package com.blundell.hangovercures;

import android.content.Context;

import com.facebook.stetho.Stetho;

class OptionalDependencies {

    public void initialise(Context context) {
        Stetho.initializeWithDefaults(context);
    }

}
