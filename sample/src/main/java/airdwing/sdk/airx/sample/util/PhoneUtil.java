package airdwing.sdk.airx.sample.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by caojin on 2017/6/16.
 */

public class PhoneUtil {
    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(TELEPHONY_SERVICE))
                .getDeviceId();
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }
}
