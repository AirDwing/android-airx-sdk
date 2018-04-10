package airdwing.sdk.airx.common.callback;

import java.io.IOException;

import airdwing.sdk.airx.error.AirXError;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by caojin on 2017/3/9.
 */

public abstract class BaseCallback implements Callback<ResponseBody> {

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()){
            try {
                onSuccess(response.body().string());
            } catch (IOException e) {
                onFailure(e);
            }
        } else {
            onFailure(new Exception(AirXError.buildMsg(response)));
        }
        onFinished();
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        onFailure(t);
        onFinished();
    }

    protected abstract void onSuccess(String response);

    protected void onFinished() {}

    protected void onFailure(Throwable t) {}
}
