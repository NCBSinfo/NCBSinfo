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

        switch (new Preferences(context).app().getMode()) {
            case OFFLINE:
                this.mode = modes.OFFLINE.getValue();
                this.icon = R.drawable.icon_wifi_off;
                this.warningMessage = context.getString(R.string.warning_mode_change_offline);
                this.isOffline = true;
                break;
            case ONLINE:
                this.mode = modes.ONLINE.getValue();
                this.icon = R.drawable.icon_wifi_on;
                this.warningMessage = context.getString(R.string.warning_mode_change_online);
                this.isOffline = false;
                break;
            case UNKNOWN:
                this.mode = modes.UNKNOWN.getValue();
                this.icon = R.drawable.icon_warning;
                this.warningMessage = context.getString(R.string.warning_mode_change_online);
                this.isOffline = true;
                break;
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
