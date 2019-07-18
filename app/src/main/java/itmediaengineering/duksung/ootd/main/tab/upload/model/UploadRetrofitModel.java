package itmediaengineering.duksung.ootd.main.tab.upload.model;

import com.google.gson.JsonObject;

import itmediaengineering.duksung.ootd.data.feed.Post;
import itmediaengineering.duksung.ootd.login.model.LoginCallback;
import itmediaengineering.duksung.ootd.login.model.LoginRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadRetrofitModel {
    private static final String TAG = UploadRetrofitModel.class.getSimpleName();
    private UploadRetrofitCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public UploadRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(UploadRetrofitCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void upload(Post post) {
        Call<Void> call = retrofitService.upload("auth", new JsonObject(), "providerUserId");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == ResponseCode.UNDOCUMENTED) {
                    callback.onSuccess(ResponseCode.UNDOCUMENTED); // 계정권한이 유효하지 않음
                    return;
                }
                if (response.code() == ResponseCode.BAD_REQUEST) {
                    callback.onSuccess(ResponseCode.BAD_REQUEST);
                    return;
                }
                if (response.code() == ResponseCode.NOT_FOUND) { // 멤버를 찾을 수 없음
                    callback.onSuccess(ResponseCode.NOT_FOUND);  // 이 부분은 수정해야 할듯
                    return;
                }
                callback.onSuccess(ResponseCode.CREATED);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
