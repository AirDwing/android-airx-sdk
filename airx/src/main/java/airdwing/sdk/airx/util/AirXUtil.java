package airdwing.sdk.airx.util;

import java.util.Random;

/**
 * Created by caojin on 2017/6/27.
 */

public class AirXUtil {
    public static String getKey() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }
}
