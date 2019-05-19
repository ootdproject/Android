package itmediaengineering.duksung.ootd.main.tab.mypage.model;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.data.mygallery.GalleryResponse;
import itmediaengineering.duksung.ootd.data.mygallery.Photo;
import itmediaengineering.duksung.ootd.data.mygallery.Photos;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageRetrofitModel {
    private MyPageRetrofitCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;
    private static final String API_KEY = "b727dc9341180f8b23d6b4a3043f687e";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";

    public MyPageRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(MyPageRetrofitCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void getGallery() {

        Call<GalleryResponse> call = retrofitService.getGallery(
                        API_KEY,
                        FETCH_RECENTS_METHOD,
                        "json",
                        "1",
                        "url_s");
        call.enqueue(new Callback<GalleryResponse>() {
            @Override
            public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onSuccess(ResponseCode.NOT_FOUND, null);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
                    return;
                }
                List<Photo> posts = response.body().getPhotos().getPhoto();
                callback.onSuccess(ResponseCode.SUCCESS, posts);
            }

            @Override
            public void onFailure(Call<GalleryResponse> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }
}
