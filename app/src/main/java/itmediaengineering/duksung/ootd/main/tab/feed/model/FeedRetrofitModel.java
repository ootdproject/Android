package itmediaengineering.duksung.ootd.main.tab.feed.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import itmediaengineering.duksung.ootd.utils.LikeType;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import itmediaengineering.duksung.ootd.utils.SortType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedRetrofitModel {
    private FeedRetrofitCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;
    public FeedRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(FeedRetrofitCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void getPosts(int page) {
        final String auth = PreferenceUtils.getAuth();
        Call<List<Post>> call = retrofitService.getPosts(auth, page, SortType.DESC.key, true);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onSuccess(ResponseCode.NOT_FOUND, null);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
                    return;
                }
                List<Post> posts = response.body();
                callback.onSuccess(ResponseCode.SUCCESS, posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void getQueryLocationPosts(String dong, int page){
        final String auth = PreferenceUtils.getAuth();
        Call<List<Post>> call = retrofitService.getGuPosts(auth, dong, page, SortType.DESC.key);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onSuccess(ResponseCode.NOT_FOUND, null);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
                    return;
                }
                List<Post> posts = response.body();
                callback.onSuccess(ResponseCode.SUCCESS, posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void getRecommendLocationPosts(String dong, int page){
        final String auth = PreferenceUtils.getAuth();
        Call<List<Post>> call = retrofitService.getRecommendGuPosts(auth, dong, page, SortType.DESC2.key, 5);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onSuccessRecommendGuPost(ResponseCode.NOT_FOUND, null);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    callback.onSuccessRecommendGuPost(ResponseCode.UNAUTHORIZED, null);
                    return;
                }
                List<Post> posts = response.body();
                callback.onSuccessRecommendGuPost(ResponseCode.SUCCESS, posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void likePost(int postId, LikeType likeType){
        final String auth = PreferenceUtils.getAuth();
        final String pUid = PreferenceUtils.getProviderUserId();

        final Callback<Void> callback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    FeedRetrofitModel.this.callback.onSuccessLikePost(ResponseCode.NOT_FOUND);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    FeedRetrofitModel.this.callback.onSuccessLikePost(ResponseCode.UNAUTHORIZED);
                    return;
                }
                FeedRetrofitModel.this.callback.onSuccessLikePost(ResponseCode.SUCCESS);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                FeedRetrofitModel.this.callback.onFailure();
            }
        };

        final Call<Void> call = likeType.key == true ?
                retrofitService.likePost(auth, postId, pUid) :
                retrofitService.unlikePost(auth, postId, pUid);

        call.enqueue(callback);

    }
}
