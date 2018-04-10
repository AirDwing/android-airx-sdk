package airdwing.sdk.airx.common;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import airdwing.sdk.airx.AirX;
import airdwing.sdk.airx.error.AirXError;
import airdwing.sdk.airx.util.Sign;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by caojin on 2017/3/8.
 */

public class OkHttpClientBuilder {
    public static final String KEY_SECRET_ID = "SecretId";
    public static final String KEY_TIMESTAMP = "Timestamp";
    public static final String KEY_NONCE = "Nonce";
    public static final String KEY_SIGNATURE_METHOD = "SignatureMethod";
    public static final String KEY_SIGNATURE = "Signature";
    public static final String HMACSHA256 = "HmacSHA256";
    public static final String HMACSHA1 = "HmacSHA1";
    public static final String KEY_AUTH = "auth";

    private OkHttpClient.Builder builder;

    public OkHttpClientBuilder(){
        builder = new OkHttpClient.Builder();
    }

    /**
     * base setting
     * @return
     */
    private OkHttpClientBuilder addSetting(){
        builder.connectTimeout(AirX.getInstance().getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(AirX.getInstance().getReadTimeout(), TimeUnit.SECONDS);
        return this;
    }

    /**
     * for https
     */
    private OkHttpClientBuilder addSecurity() {
        return this;
    }

    public OkHttpClientBuilder addCommonParam() {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if ("GET".equalsIgnoreCase(request.method())) {
                    return chain.proceed(request.newBuilder().url(buildCommonGetParam(request, null)).build());
                }
                if ("POST".equalsIgnoreCase(request.method())) {
                    if (request.body() instanceof FormBody) {
                        return chain.proceed(request.newBuilder().post(buildCommonPostParam(request, null)).build());
                    }
                }
                return chain.proceed(request);
            }
        });
        return this;
    }

    /**
     * add common header for request
     * @return
     */
    public OkHttpClientBuilder addCommonHeader() {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .build();
                return chain.proceed(request);
            }
        });
        return this;
    }

    /**
     * print log when in debug mode
     * @return
     */
    public OkHttpClientBuilder addLog(){
        if (AirX.getInstance().getDebugMode()) {
            builder.addInterceptor(new HttpLoggingInterceptor().
                    setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return this;
    }

    /**
     * listener for the process when doing long time things,
     * for example, downloading file or pic.
     * @param listener
     * @return
     */
    public OkHttpClientBuilder addProgressListener(final OnProgressUpdateListener listener) {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), listener))
                        .build();
            }
        });
        return this;
    }

    public OkHttpClientBuilder addCustomInterceptor() {
        Interceptor interceptor = AirX.getInstance().getInterceptor();
        if (interceptor != null) {
            builder.addInterceptor(interceptor);
        }
        return this;
    }

    public OkHttpClient build() {
        addSetting();
        return this.builder.build();
    }

    public static FormBody buildCommonPostParam(Request request, String newAuth) {
        FormBody body = (FormBody) request.body();
        FormBody.Builder builder = new FormBody.Builder();
        TreeMap<String, Object> rawMap = new TreeMap<>();
        for (int i = 0;i < body.size();i++) {
            String key = body.name(i);
            if (KEY_SIGNATURE.equals(key) || KEY_NONCE.equals(key) || (newAuth != null && KEY_AUTH.equals(key))) {
                continue;
            }
            String value = body.value(i);
            builder.add(key, value);
            rawMap.put(key, value);
        }
        if (newAuth != null) {
            rawMap.put(KEY_AUTH, newAuth);
            builder.add(KEY_AUTH, newAuth);
        }
        if (!rawMap.containsKey(KEY_SECRET_ID)) {
            String secretId = AirX.getInstance().getSecretId();
            rawMap.put(KEY_SECRET_ID, secretId);
            builder.add(KEY_SECRET_ID, secretId);
        }
        if (!rawMap.containsKey(KEY_TIMESTAMP)) {
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            rawMap.put(KEY_TIMESTAMP, timestamp);
            builder.add(KEY_TIMESTAMP, timestamp);
        }
        if (!rawMap.containsKey(KEY_NONCE)) {
            String nonce = String.valueOf(new Random().nextInt(java.lang.Integer.MAX_VALUE));
            builder.add(KEY_NONCE, nonce);
            rawMap.put(KEY_NONCE, nonce);
        }
        String signatureMethod = null;
        if (!rawMap.containsKey(KEY_SIGNATURE_METHOD)) {
            builder.add(KEY_SIGNATURE_METHOD, HMACSHA256);
            rawMap.put(KEY_SIGNATURE_METHOD, HMACSHA256);
            signatureMethod = HMACSHA256;
        } else {
            String temp = (String) rawMap.get(KEY_SIGNATURE_METHOD);
            if (!HMACSHA1.equals(temp) && !HMACSHA256.equals(temp)) {
                throw new RuntimeException(AirXError.WRONG_SIGNATURE_METHOD);
            }
            signatureMethod = temp;
        }
        String plainText = Sign.makeSignPlainText(rawMap, request.method(), request.url().host(), buildPath(request.url().pathSegments()));
        try {
            builder.add(KEY_SIGNATURE, Sign.sign(plainText, AirX.getInstance().getSecretKey(), signatureMethod));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    public static HttpUrl buildCommonGetParam(Request request, String newAuth) {
        HttpUrl url = request.url();

        int size = url.querySize();
        TreeMap<String, Object> rawMap = new TreeMap<>();
        for (int i = 0;i < size;i++) {
            rawMap.put(url.queryParameterName(i), url.queryParameterValue(i));
        }
        rawMap.remove(KEY_SIGNATURE);
        rawMap.remove(KEY_NONCE);
        if (newAuth != null) {
            rawMap.remove(KEY_AUTH);
        }

        Set<String> keySet = url.queryParameterNames();
        HttpUrl.Builder builder = url.newBuilder();
        builder.removeAllQueryParameters(KEY_SIGNATURE);
        builder.removeAllQueryParameters(KEY_NONCE);
        if (newAuth != null) {
            builder.removeAllQueryParameters(KEY_AUTH);
            if (!keySet.contains(KEY_AUTH)) {
                builder.addQueryParameter(KEY_AUTH, newAuth);
                rawMap.put(KEY_AUTH, newAuth);
            }
        }
        if (!keySet.contains(KEY_SECRET_ID)) {
            String secretId = AirX.getInstance().getSecretId();
            builder.addQueryParameter(KEY_SECRET_ID, secretId);
            rawMap.put(KEY_SECRET_ID, secretId);
        }
        if (!keySet.contains(KEY_TIMESTAMP)) {
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            builder.addQueryParameter(KEY_TIMESTAMP, timestamp);
            rawMap.put(KEY_TIMESTAMP, timestamp);
        }
        if (!keySet.contains(KEY_NONCE)) {
            String nonce = String.valueOf(new Random().nextInt(java.lang.Integer.MAX_VALUE));
            builder.addQueryParameter(KEY_NONCE, nonce);
            rawMap.put(KEY_NONCE, nonce);
        }
        String signatureMethod = null;
        if (!keySet.contains(KEY_SIGNATURE_METHOD)) {
            builder.addQueryParameter(KEY_SIGNATURE_METHOD, HMACSHA256);
            rawMap.put(KEY_SIGNATURE_METHOD, HMACSHA256);
            signatureMethod = HMACSHA256;
        } else {
            List<String> signatureMethodList = url.queryParameterValues(KEY_SIGNATURE_METHOD);
            if (!signatureMethodList.contains(HMACSHA1) && !signatureMethodList.contains(HMACSHA256)) {
                throw new RuntimeException(AirXError.WRONG_SIGNATURE_METHOD);
            }
            signatureMethod = signatureMethodList.get(0);
        }
        String plainText = Sign.makeSignPlainText(rawMap, request.method(), url.host(), buildPath(request.url().pathSegments()));
        try {
            builder.addQueryParameter(KEY_SIGNATURE, Sign.sign(plainText, AirX.getInstance().getSecretKey(), signatureMethod));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    private static String buildPath(List<String> rawList) {
        StringBuilder sb = new StringBuilder();
        for (String s : rawList) {
            sb.append("/" + s);
        }
        return sb.toString();
    }
}
