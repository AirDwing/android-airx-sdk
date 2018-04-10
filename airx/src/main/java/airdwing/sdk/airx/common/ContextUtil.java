package airdwing.sdk.airx.common;

import android.content.Context;

/**
 * Created by caojin on 2016/10/24.
 */

public class ContextUtil {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getApplicationContext() {
        return mContext;
    }
}

