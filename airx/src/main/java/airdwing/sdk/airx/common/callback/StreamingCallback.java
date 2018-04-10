package airdwing.sdk.airx.common.callback;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import airdwing.sdk.airx.error.AirXError;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by caojin on 2017/3/9.
 */

public abstract class StreamingCallback extends BaseCallback {
    private File file;

    public StreamingCallback(File file) {
        this.file = file;
    }

    public StreamingCallback(File dir, String fileName){
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.file = new File(dir, fileName);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
        if (response.isSuccessful()){
            new AsyncTask<Void, Integer, Boolean>(){
                @Override
                protected Boolean doInBackground(Void... params) {
                    try {
                        save2File(response, file);
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (aBoolean) {
                        onSuccess(null);
                    } else {
                        onFailure(new Exception(AirXError.SAVE_FILE_FAILED));
                    }
                    onFinished();
                }
            }.execute();
        } else {
            onFailure(new Exception(AirXError.buildMsg(response)));
            onFinished();
        }
    }

    private static void save2File(Response<ResponseBody> response, File file) throws IOException {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = response.body().byteStream();
            out = new FileOutputStream(file);
            byte[] buffer = new byte[2048];
            int length = -1;
            while ((length = in.read(buffer)) != -1){
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
