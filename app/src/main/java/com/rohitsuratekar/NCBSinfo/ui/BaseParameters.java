package com.rohitsuratekar.NCBSinfo.ui;

import android.content.Context;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 17-07-16.
 */
public class BaseParameters {

    Context context;

    public BaseParameters(Context context) {
        this.context = context;
    }

    public int startTransition() {
        return android.R.anim.fade_in;
    }

    public int stopTransition() {
        return android.R.anim.fade_out;
    }


}
