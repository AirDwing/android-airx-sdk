package airdwing.sdk.airx.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import airdwing.sdk.airx.common.OnProgressUpdateListener;
import airdwing.sdk.airx.common.ProgressRequestBody;
import airdwing.sdk.airx.common.RetrofitSetting;
import airdwing.sdk.airx.error.AirXError;
import airdwing.sdk.airx.service.AirXService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by caojin on 2017/5/25.
 */

public class AirXApi {
    private static AirXService service = RetrofitSetting.getCommonSetting().create(AirXService.class);

    public static void noSignGet(String path, Map<String, Object> queryMap, Callback callback) {
        RetrofitSetting.getNoSignSetting().create(AirXService.class).get(path, queryMap).enqueue(callback);
    }

    /**
     * small file download also use this
     * @param path
     * @param queryMap
     * @return
     */
    public static void get(String path, Map<String, Object> queryMap, Callback callback) {
        service.get(path, queryMap).enqueue(callback);
    }

    public static void post(String path, Map<String, Object> fieldMap, Callback callback) {
        service.post(path, fieldMap).enqueue(callback);
    }

    public static void upload(String path, File file, Map<String, String> fieldMap, Callback callback) {
        if (!file.exists() || file.isDirectory()) {
            throw new RuntimeException(AirXError.UPLOAD_FILE_NOT_EXIST);
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<String> keySet = fieldMap.keySet();
        for (String key : keySet) {
            String value = fieldMap.get(key);
            partMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        service.upload(path, partFile, partMap).enqueue(callback);
    }

    public static void upload(String path, File file, Map<String, String> fieldMap, OnProgressUpdateListener listener, Callback callback) {
        if (!file.exists() || file.isDirectory()) {
            throw new RuntimeException(AirXError.UPLOAD_FILE_NOT_EXIST);
        }
        RequestBody requestFile = new ProgressRequestBody(file, MediaType.parse("multipart/form-data"), listener);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        Set<String> keySet = fieldMap.keySet();
        for (String key : keySet) {
            String value = fieldMap.get(key);
            partMap.put(key, RequestBody.create(MediaType.parse("multipart/form-data"), value));
        }
        service.upload(path, partFile, partMap).enqueue(callback);
    }

    /**
     * for big file downloading
     * @param listener
     * @return
     */
    public static void download(String path, Map<String, Object> paramMap, OnProgressUpdateListener listener, Callback callback) {
        download(null, path, paramMap, listener, callback);
    }

    /**
     * if your download base url is different
     * @param baseUrl
     * @param path
     * @param paramMap
     * @param listener
     * @return
     */
    public static void download(String baseUrl, String path, Map<String, Object> paramMap, OnProgressUpdateListener listener, Callback callback) {
        RetrofitSetting.getDownloadSetting(baseUrl, listener)
                .create(AirXService.class)
                .download(path, paramMap)
                .enqueue(callback);
    }

    public static Response<ResponseBody> post(String path, Map<String, Object> fieldMap) throws IOException {
        return service.post(path, fieldMap).execute();
    }

    public static Response<ResponseBody> get(String path, Map<String, Object> queryMap) throws IOException {
        return service.get(path, queryMap).execute();
    }

    public static Response<ResponseBody> noSignGet(String path, Map<String, Object> queryMap) throws IOException {
        return RetrofitSetting.getNoSignSetting().create(AirXService.class).get(path, queryMap).execute();
    }
}
