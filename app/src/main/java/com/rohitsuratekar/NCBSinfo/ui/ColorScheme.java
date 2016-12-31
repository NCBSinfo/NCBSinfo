package com.rohitsuratekar.NCBSinfo.ui;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.R;

public class ColorScheme {

    private Context context;

    public ColorScheme(Context context) {
        this.context = context;
    }

    public int getPrimary() {
        return R.color.colorPrimary;
    }

    public int getAccent() {
        return R.color.colorAccent;
    }

    public int getPrimaryDark() {
        return R.color.colorPrimaryDark;
    }

    public int getPrimaryLight() {
        return R.color.colorPrimaryLight;
    }

    public int getLightText() {
        return R.color.colorLightText;
    }

    public int getPrimaryText() {
        return R.color.colorPrimaryText;
    }

    public int getSecondaryText() {
        return R.color.colorSecondaryText;
    }

}
