package itmediaengineering.duksung.ootd.main.tab.upload.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;

import itmediaengineering.duksung.ootd.data.post.PostRequest;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadRetrofitModel {
    private static final String TAG = UploadRetrofitModel.class.getSimpleName();
    private UploadRetrofitCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public static final MediaType JSON = MediaType.parse("application/json");

    public UploadRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(UploadRetrofitCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void upload(PostRequest postRequest, File file) {
        final String auth = PreferenceUtils.getAuth();
        final String providerUserId = PreferenceUtils.getProviderUserId();
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("form-data"), file);
        MultipartBody.Part img = MultipartBody.Part.createFormData("file", file.getName() + ".jpg", fileReqBody);

        HashMap<String, String> map = new HashMap<>();
        map.put("categoryA", postRequest.getCategoryA());
        map.put("categoryB", postRequest.getCategoryB());
        map.put("cost", postRequest.getCost());
        map.put("description", postRequest.getDescription());
        map.put("dong", postRequest.getDong());
        map.put("imageUrl", "");
        map.put("sale", "true");
        map.put("title", postRequest.getTitle());
        map.put("color", postRequest.getColor());
        MultipartBody.Part requestJson = MultipartBody.Part.createFormData(
                "postRequest"
                , "postRequest.json"
                , RequestBody.create(JSON, new Gson().toJson(map)));

        Call<ResponseBody> call = retrofitService.createPost(auth, providerUserId, img, requestJson);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void editPostContents(PostRequest post, int postId){
        final String auth = PreferenceUtils.getAuth();
        final String providerUserId = PreferenceUtils.getProviderUserId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty ("categoryA", post.getCategoryA());
        jsonObject.addProperty ("categoryB", post.getCategoryB());
        jsonObject.addProperty ("cost", post.getCost());
        jsonObject.addProperty ("description", post.getDescription());
        jsonObject.addProperty ("dong", post.getDong());
        jsonObject.addProperty("imageUrl", post.getImageUrl());
        jsonObject.addProperty ("sale", post.getSale());
        jsonObject.addProperty ("title", post.getTitle());
        jsonObject.addProperty("color", post.getColor());

        Call<Void> call = retrofitService.editPost(auth, postId, jsonObject, providerUserId);
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
