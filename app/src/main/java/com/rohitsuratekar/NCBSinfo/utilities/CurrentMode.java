package com.rohitsuratekar.NCBSinfo.utilities;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

public class CurrentMode implements AppConstants {

    String mode;
    int icon;
    String warningMessage;
    boolean isOffline;

    public CurrentMode(Context context) {

        String modeString = new Preferences(context).app().getMode();

        if (modeString.equals(modes.OFFLINE.getValue())) {
            this.mode = modes.OFFLINE.getValue();
            this.icon = R.drawable.icon_wifi_off;
            this.warningMessage = context.getString(R.string.warning_mode_change_offline);
            this.isOffline = true;
        } else if (modeString.equals(modes.ONLINE.getValue())) {
            this.mode = modes.ONLINE.getValue();
            this.icon = R.drawable.icon_wifi_on;
            this.warningMessage = context.getString(R.string.warning_mode_change_online);
            this.isOffline = false;
        }

    }

    public String getMode() {
        return mode;
    }

    public int getIcon() {
        return icon;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public String getWarningMessage() {
        return warningMessage;
    }
}
