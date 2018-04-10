package airdwing.sdk.airx.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import airdwing.sdk.airx.api.AirXApi;
import airdwing.sdk.airx.common.OnProgressUpdateListener;
import airdwing.sdk.airx.common.callback.BaseCallback;
import airdwing.sdk.airx.common.callback.StreamingCallback;
import airdwing.sdk.airx.error.AirXError;
import airdwing.sdk.airx.sample.bean.ResponseOverview;
import airdwing.sdk.airx.sample.bean.User;
import airdwing.sdk.airx.sample.callback.JsonCallback;
import airdwing.sdk.airx.sample.util.PhoneUtil;
import airdwing.sdk.airx.util.AirXUtil;
import airdwing.sdk.airx.util.AuthCode;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnProgressUpdateListener {
    private static final String TAG = "MainActivity";

    private EditText et_username;
    private EditText et_password;
    private EditText et_code;
    private Spinner sp_upload_type;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_check).setOnClickListener(this);
        findViewById(R.id.btn_upload).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_download).setOnClickListener(this);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_code = findViewById(R.id.et_code);
        sp_upload_type = findViewById(R.id.sp_upload_type);
        requestPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // check user
            case R.id.btn_check:
                if (TextUtils.isEmpty(et_username.getText().toString())) {
                    Log.e(TAG, "username needed");
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("username", et_username.getText().toString());
                AirXApi.get("/user/check", map, new BaseCallback() {
                    @Override
                    protected void onSuccess(String response) {}
                });
                break;
            // upload file
            case R.id.btn_upload:
                Map<String, String> uploadMap = new HashMap<>();
                uploadMap.put("auth", this.user.getAuth());
                uploadMap.put("type", "userverify");
                AirXApi.upload("/upload", getTestFile(), uploadMap, new BaseCallback() {
                    @Override
                    protected void onSuccess(String response) {}
                });
                break;
            case R.id.btn_register:
                if (TextUtils.isEmpty(et_username.getText().toString())) {
                    Log.e(TAG, "username needed");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    Log.e(TAG, "username needed");
                    return;
                }
                if (TextUtils.isEmpty(et_code.getText().toString())) {
                    Log.e(TAG, "code needed");
                    return;
                }
                Map<String, Object> registerMap = new HashMap<>();
                registerMap.put("code", et_code.getText().toString());
                registerMap.put("device", PhoneUtil.getPhoneModel());
                registerMap.put("guid", PhoneUtil.getIMEI(this));
                String key = AirXUtil.getKey();
                registerMap.put("key", key);
                registerMap.put("mobile", et_username.getText().toString());
                registerMap.put("password", AuthCode.encode(et_password.getText().toString(), key, 3600));
                AirXApi.post("/user/register/mobile", registerMap, new BaseCallback() {
                    @Override
                    protected void onSuccess(String response) {}
                });
                break;
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_username.getText().toString())) {
                    Log.e(TAG, "username needed");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    Log.e(TAG, "username needed");
                    return;
                }
                Map<String, Object> loginMap = new HashMap<>();
                loginMap.put("guid", PhoneUtil.getIMEI(this));
                String key1 = AirXUtil.getKey();
                loginMap.put("key", key1);
                loginMap.put("password", AuthCode.encode(et_password.getText().toString(), key1, 3600));
                loginMap.put("username", et_username.getText().toString());
                AirXApi.post("/user/login", loginMap, new JsonCallback<User>() {
                    @Override
                    protected void onSuccess(User response, ResponseOverview base) {
                        if (response != null) {
                            user = response;
                        } else {
                            Log.d(TAG, "onSuccess: " + AirXError.getDetail(base.getCode()));
                        }
                    }
                });
                break;
            // send verification code
            case R.id.btn_send:
                if (TextUtils.isEmpty(et_username.getText().toString())) {
                    Log.e(TAG, "username needed");
                    return;
                }
                Map<String, Object> sendMap = new HashMap<>();
                sendMap.put("guid", PhoneUtil.getIMEI(this));
                sendMap.put("mobile", 17602558233L);
                AirXApi.post("/sms/send", sendMap, new BaseCallback() {
                    @Override
                    protected void onSuccess(String response) {}
                });
                break;
            case R.id.btn_download:
                HashMap<String, Object> downloadMap = new HashMap<>();
                File dir = Environment.getExternalStoragePublicDirectory(getString(R.string.app_name));
                AirXApi.download("http://msoftdl.360.cn/", "/mobile/shouji360/360safesis/360MobileSafe_7.7.1.1011.apk", downloadMap, this, new StreamingCallback(dir, "download.apk") {
                    @Override
                    protected void onSuccess(String response) {}
                });
                break;
        }
    }

    public File getTestFile(){
        String fileName = null;
        switch (sp_upload_type.getSelectedItemPosition()) {
            case 0:
                fileName = "upload_png.png";
                break;
            case 1:
                fileName = "upload_jpg.jpg";
                break;
            case 2:
                fileName = "upload_jpeg.jpeg";
                break;
        }
        File dest = null;
        FileOutputStream dstOut = null;
        BufferedInputStream srcIn = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(getString(R.string.app_name));
            if (!dir.exists()) {
                dir.mkdir();
            }
            dest = new File(dir, fileName);
            if (dest.exists()) {
                return dest;
            }
            dstOut = new FileOutputStream(dest);

            srcIn = new BufferedInputStream(getResources().getAssets().open(fileName));
            byte[] buff = new byte[1024];
            int byteRead;
            while ((byteRead = srcIn.read(buff, 0, buff.length)) != -1) {
                dstOut.write(buff, 0, byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dstOut != null) {
                try {
                    dstOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (srcIn != null) {
                try {
                    srcIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dest;
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE}, 0);
        }
    }

    @Override
    public void onProgressUpdate(float percent, boolean finished) {
        Log.d(TAG, "onProgressUpdate: " + percent + ":" + finished);
    }
}
