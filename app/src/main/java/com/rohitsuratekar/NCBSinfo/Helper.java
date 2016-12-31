package com.rohitsuratekar.NCBSinfo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.ui.ColorScheme;
import com.secretbiology.helpers.general.General;

public class Helper {

    public enum COLOR_TYPE {PRIMARY, ACCENT, DARK, LIGHT, LIGHT_TEXT, PRIMARY_TEXT, SECONDARY_TEXT}


    public static void setViewColor(Context context, COLOR_TYPE type, View... img) {
        ColorScheme scheme = new ColorScheme(context);
        int color;
        if (type.equals(COLOR_TYPE.PRIMARY)) {
            color = scheme.getPrimary();
        } else if (type.equals(COLOR_TYPE.ACCENT)) {
            color = scheme.getAccent();
        } else if (type.equals(COLOR_TYPE.DARK)) {
            color = scheme.getPrimaryDark();
        } else if (type.equals(COLOR_TYPE.LIGHT)) {
            color = scheme.getPrimaryLight();
        } else if (type.equals(COLOR_TYPE.LIGHT_TEXT)) {
            color = scheme.getLightText();
        } else if (type.equals(COLOR_TYPE.PRIMARY_TEXT)) {
            color = scheme.getPrimaryText();
        } else if (type.equals(COLOR_TYPE.SECONDARY_TEXT)) {
            color = scheme.getSecondaryText();
        } else {
            color = scheme.getPrimary();
        }

        for (View v : img) {
            if (v instanceof ImageView) {
                ((ImageView) v).setColorFilter(General.getColor(context, color));
            } else if (v instanceof TextView) {
                ((TextView) v).setTextColor(General.getColor(context, color));
            } else {
                v.setBackgroundColor(General.getColor(context, color));
            }
        }
    }


}
