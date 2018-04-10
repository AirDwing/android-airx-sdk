package airdwing.sdk.airx;

/**
 * Created by caojin on 2017/5/25.
 */

import android.content.Context;

import airdwing.sdk.airx.common.ContextUtil;
import okhttp3.Interceptor;

/**
 * should be initialed in your application's onCreate() method
 */
public class AirX {
    private static final int DEFAULT_CONNECT_TIME_OUT = 10;
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private boolean debug;

    private String secretId;
    private String secretKey;
    private long connectTimeout = DEFAULT_CONNECT_TIME_OUT;
    private long readTimeout = DEFAULT_READ_TIME_OUT;

    private Interceptor interceptor;

    private static AirX instance = new AirX();

    private AirX() {}

    public static AirX getInstance() {
        return instance;
    }

    public void init(Context context, String secretId, String secretKey) {
        ContextUtil.init(context);
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    public String getSecretId() {
        return secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setDebugMode(boolean debug) {
        this.debug = debug;
    }

    public boolean getDebugMode() {
        return this.debug;
    }

    /**
     * @param connectTimeout    in second
     */
    public void setConnectTimeout(long connectTimeout) {
        if (connectTimeout < 0) {
            return;
        }
        this.connectTimeout = connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    /**
     * @param readTimeout    in second
     */
    public void setReadTimeout(long readTimeout) {
        if (readTimeout < 0) {
            return;
        }
        this.readTimeout = readTimeout;
    }

    public void setInterceptor(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Interceptor getInterceptor() {
        return this.interceptor;
    }
}
