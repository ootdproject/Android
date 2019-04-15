package itmediaengineering.duksung.ootd.feed.model;

import java.util.List;

import itmediaengineering.duksung.ootd.api.SharePreferenceManager;
import itmediaengineering.duksung.ootd.data.feed.Post;
import itmediaengineering.duksung.ootd.data.feed.PostResponse;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import retrofit2.Call;

public class FeedRetrofitModel {
    private FeedRetrofitCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public FeedRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(FeedRetrofitCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void getPosts(String search, int sort, int page) {
        String ordering = sort == 0 ? "start_time" : "-created_at";
        String token = SharePreferenceManager.getString("Token");
//        Call<PostResponse> call = retrofitService.getPosts(token, search, page);
//        call.enqueue(new Callback<PostResponse>() {
//            @Override
//            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
//                if (response.code() == ResponseCode.NOT_FOUND) {
//                    callback.onSuccess(ResponseCode.NOT_FOUND, null);
//                    return;
//                }
//                if (response.code() == ResponseCode.UNAUTHORIZED) {
//                    callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
//                    return;
//                }
//                List<Post> posts = response.body().getResult().getData();
//                callback.onSuccess(ResponseCode.SUCCESS, posts);
//            }
//
//            @Override
//            public void onFailure(Call<PostResponse> call, Throwable t) {
//                t.printStackTrace();
//                callback.onFailure();
//            }
//        });
    }
}
