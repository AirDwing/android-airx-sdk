package airdwing.sdk.airx.sample.callback;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import airdwing.sdk.airx.error.AirXError;
import airdwing.sdk.airx.sample.bean.ResponseOverview;
import airdwing.sdk.airx.sample.thirdparty.json.JsonUtil;
import airdwing.sdk.airx.sample.thirdparty.json.TypeBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by caojin on 2017/6/23.
 */

public abstract class JsonCallback<T> implements Callback<ResponseBody> {

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()){
            try {
                ResponseOverview base = JsonUtil.fromJson(response.body().string(), ResponseOverview.class);
                if (ResponseOverview.hasData(base)) {
                    String content = base.getData();
                    Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    if (type instanceof Class) {
                        onSuccess((T) JsonUtil.fromJson(content, type), base);
                    } else if (type instanceof ParameterizedType) {
                        onSuccess((T) JsonUtil.fromJson(content, TypeBuilder.newInstance(List.class)
                                .addTypeParam(((ParameterizedType) type).getActualTypeArguments()[0])
                                .build()), base);
                    }
                } else {
                    onSuccess(null, base);
                }
            } catch (IOException e) {
                e.printStackTrace();
                onFailure(e);
                onFinished();
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

    protected abstract void onSuccess(T response, ResponseOverview base);

    protected void onFinished() {}

    protected void onFailure(Throwable t) {}
}

