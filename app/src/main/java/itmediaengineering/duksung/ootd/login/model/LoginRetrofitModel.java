package itmediaengineering.duksung.ootd.login.model;

import itmediaengineering.duksung.ootd.data.ResponseAuth;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRetrofitModel {
    private static final String TAG = LoginRetrofitModel.class.getSimpleName();
    private LoginCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public LoginRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(LoginCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void login(String userId) {
        Call<ResponseAuth> call = retrofitService.login(userId);
        call.enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                if (response.code() == ResponseCode.BAD_REQUEST) {
                    callback.onSuccess(ResponseCode.BAD_REQUEST, null);
                    return;
                }
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onSuccess(ResponseCode.NOT_FOUND, null);
                    return;
                }
                callback.onSuccess(ResponseCode.CREATED, response.body());
            }

            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
