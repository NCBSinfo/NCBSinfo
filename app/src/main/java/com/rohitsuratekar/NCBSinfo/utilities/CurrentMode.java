package com.rohitsuratekar.NCBSinfo.utilities;

import android.content.Context;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.interfaces.User;

public class CurrentMode implements User{

    String mode;
    int icon;
    String warningMessage;
    boolean isOffline;

    public CurrentMode(Context context) {
        switch (PreferenceManager.getDefaultSharedPreferences(context).getString(MODE, OFFLINE)){
            case OFFLINE:
                this.mode = OFFLINE;
                this.icon = R.drawable.icon_wifi_off;
                this.warningMessage = context.getString(R.string.warning_mode_change_offline);
                this.isOffline = true;
                break;
            case ONLINE:
                this.mode = ONLINE;
                this.icon = R.drawable.icon_wifi_on;
                this.warningMessage = context.getString(R.string.warning_mode_change_online);
                this.isOffline = false;
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
