package com.rohitsuratekar.NCBSinfo.gcm;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;

public class FlagHandler extends AppCompatActivity{
    String FLAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gcm_flag_handler);
        Intent intent = getIntent();
        FLAG = intent.getExtras().getString(GCMConstants.FLAG_CODE,"0");
        Intent returnintent = new Intent(FlagHandler.this,Home.class);
        if (FLAG.equals("clearToken")){
            deleteData();
        }
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(GCMConstants.START_FLAG, false).apply();
        startActivity(returnintent);
    }

    public void deleteData(){
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.GCM_TOPIC_CODE).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.GCM_API_KEY).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.GCM_MAX_DAILYQUOTA).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.GCM_USER_EXTRA).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.GCM_USER_LOGTABLE).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_MODERATORLOGIN).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DECODED_CLIENTID).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DECODED_SECRET).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_ACCESS_TOKEN).apply();
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove(GCMConstants.DATA_REFRESH_TOKEN).apply();

        CookieSyncManager.createInstance(getBaseContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}
