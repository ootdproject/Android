package itmediaengineering.duksung.ootd.intro.model;

import com.google.gson.JsonObject;

import itmediaengineering.duksung.ootd.data.User;
import itmediaengineering.duksung.ootd.data.User;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroConnectRetrofitModel {
    private static final String TAG = IntroConnectRetrofitModel.class.getSimpleName();
    private IntroConnectCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public IntroConnectRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(IntroConnectCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void createUser(User user) {
        //JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty ("gender", user.getGender());
        jsonObject.addProperty ("nickname", user.getNickname());
        jsonObject.addProperty ("providerType", "GOOGLE");
        jsonObject.addProperty ("providerUserId", user.getProviderUserId());
        jsonObject.addProperty ("token", "");
        Call<User> call = retrofitService.createUser(jsonObject);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == ResponseCode.BAD_REQUEST) {
                    callback.onSuccess(ResponseCode.BAD_REQUEST, null);
                    return;
                }
                callback.onSuccess(ResponseCode.CREATED, response.body().getNickname());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
