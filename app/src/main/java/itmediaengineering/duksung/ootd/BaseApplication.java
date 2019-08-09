package itmediaengineering.duksung.ootd;

import android.app.Application;

import com.sendbird.android.SendBird;
import com.sendbird.syncmanager.SendBirdSyncManager;

import itmediaengineering.duksung.ootd.utils.PreferenceUtils;

public class BaseApplication extends Application {
    private static final String APP_ID = "38381D0D-658D-432F-9300-B8FD669A7897";
    public static final String VERSION = "1.0.0";
    private boolean mIsSyncManagerSetup = false;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.init(getApplicationContext());

        SendBird.init(APP_ID, getApplicationContext());
        SendBirdSyncManager.setLoggerLevel(98765);
    }

    public boolean isSyncManagerSetup() {
        return mIsSyncManagerSetup;
    }

    public void setSyncManagerSetup(boolean syncManagerSetup) {
        mIsSyncManagerSetup = syncManagerSetup;
    }
}
