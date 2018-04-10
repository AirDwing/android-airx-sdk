package airdwing.sdk.airx.sample;

import android.app.Application;

import airdwing.sdk.airx.AirX;

/**
 * Created by caojin on 2017/5/25.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // replace with your own config
        AirX.getInstance().init(this, "SecretId", "SecretKey");
        AirX.getInstance().setConnectTimeout(10);
        AirX.getInstance().setReadTimeout(10);
        AirX.getInstance().setDebugMode(BuildConfig.DEBUG);
    }
}
