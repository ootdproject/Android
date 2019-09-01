package itmediaengineering.duksung.ootd.main.tab.mypage.model;

import android.annotation.SuppressLint;

import com.google.gson.JsonObject;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.mypage.presenter.SaleType;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import itmediaengineering.duksung.ootd.utils.SortType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageRetrofitModel {
    private MyPageRetrofitCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public MyPageRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(MyPageRetrofitCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    @SuppressLint("Assert")
    public void getMyPosts(SaleType saleType) {
        final String auth = PreferenceUtils.getAuth();
        final String providerUserId = PreferenceUtils.getProviderUserId();

        assert !(auth.isEmpty() || providerUserId.isEmpty());

        final Callback<List<Post>> callback = new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    MyPageRetrofitModel.this.callback.onSuccess(ResponseCode.NOT_FOUND, null);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    MyPageRetrofitModel.this.callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
                    return;
                }
                List<Post> posts = response.body();
                MyPageRetrofitModel.this.callback.onSuccess(ResponseCode.SUCCESS, posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                MyPageRetrofitModel.this.callback.onFailure();
            }
        };

        final Call<List<Post>> call = saleType.isPick != null ?
                retrofitService.getMyPickPosts(auth, saleType.isPick, providerUserId) :
                retrofitService.getMyPosts(auth, providerUserId, saleType.isSale);

        call.enqueue(callback);
    }

    public void editPostSaleToSold(Post post){
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

        Call<Void> call = retrofitService.editPost(auth, post.getId(), jsonObject, providerUserId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == ResponseCode.UNDOCUMENTED) {
                    callback.onSuccessEditSaleState(ResponseCode.UNDOCUMENTED); // 계정권한이 유효하지 않음
                    return;
                }
                if (response.code() == ResponseCode.BAD_REQUEST) {
                    callback.onSuccessEditSaleState(ResponseCode.BAD_REQUEST);
                    return;
                }
                if (response.code() == ResponseCode.NOT_FOUND) { // 멤버를 찾을 수 없음
                    callback.onSuccessEditSaleState(ResponseCode.NOT_FOUND);  // 이 부분은 수정해야 할듯
                    return;
                }
                callback.onSuccessEditSaleState(ResponseCode.SUCCESS);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void deleteMyPost(Post post){
        final String auth = PreferenceUtils.getAuth();
        final String providerUserId = PreferenceUtils.getProviderUserId();
        Call<Void> call = retrofitService.deleteMyPost(auth, post.getId(), providerUserId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onSuccessDeleteMyPost(ResponseCode.NOT_FOUND);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    callback.onSuccessDeleteMyPost(ResponseCode.UNAUTHORIZED);
                    return;
                }
                callback.onSuccessDeleteMyPost(ResponseCode.SUCCESS);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
