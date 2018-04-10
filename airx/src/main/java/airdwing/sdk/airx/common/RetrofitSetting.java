package airdwing.sdk.airx.common;

import retrofit2.Retrofit;

/**
 * Created by caojin on 2017/3/14.
 */

public class RetrofitSetting {
    private static final String HOST = "api.airdwing.com";

    public static Retrofit getCommonSetting() {
        return new Retrofit.Builder()
                .baseUrl(getURL())
                .client(new OkHttpClientBuilder().addCommonParam().addCustomInterceptor().addLog().build())
                .build();
    }

    public static Retrofit getNoSignSetting() {
        return new Retrofit.Builder()
                .baseUrl(getURL())
                .client(new OkHttpClientBuilder().addCustomInterceptor().addLog().build())
                .build();
    }

    public static Retrofit getDownloadSetting(OnProgressUpdateListener listener) {
        return getDownloadSetting(null, listener);
    }

    public static Retrofit getDownloadSetting(String baseUrl, OnProgressUpdateListener listener) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl == null ? getURL() : baseUrl)
                .client(new OkHttpClientBuilder()
                        .addProgressListener(listener)
                        .build())
                .build();
    }

    private static String getURL() {
        // scheme://host:port/path?query
        return "https://" + HOST + "/";
    }
}
