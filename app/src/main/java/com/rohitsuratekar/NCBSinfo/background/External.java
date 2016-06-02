package com.rohitsuratekar.NCBSinfo.background;

import android.os.Bundle;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.constants.ExternalConstants;
import com.rohitsuratekar.NCBSinfo.constants.General;

public class External {
    Bundle data;
    public External(Bundle data) {
        this.data = data;
        if (data.getString(ExternalConstants.GEN_EXTERNAL_CODE,"null").equals(ExternalConstants.CONFERENCE_CAMP2016)){
            CAMP2016();
        }
    }

    private void CAMP2016(){
        Log.i("This is:", data.getString(General.GEN_NOTIFY_MESSAGE,"null"));
    }
}
