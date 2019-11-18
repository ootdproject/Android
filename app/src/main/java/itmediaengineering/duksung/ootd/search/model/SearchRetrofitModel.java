package itmediaengineering.duksung.ootd.search.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import itmediaengineering.duksung.ootd.utils.SortType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRetrofitModel {
    private SearchCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public SearchRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(SearchCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void getSearchPosts(String category, int page, int listCount) {
        final String auth = PreferenceUtils.getAuth();
        Call<List<Post>> call = retrofitService.getCategoryBPosts(auth, category, page, SortType.DESC.key, 100);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onCategorySuccess(ResponseCode.NOT_FOUND, null, 0);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    callback.onCategorySuccess(ResponseCode.UNAUTHORIZED, null, 0);
                    return;
                }
                List<Post> posts = response.body();
                callback.onCategorySuccess(ResponseCode.SUCCESS, posts, listCount);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void getColorSearchPosts(String color, int page, int listCount) {
        final String auth = PreferenceUtils.getAuth();
        Call<List<Post>> call = retrofitService.getColorQueryPosts(auth, color, page, SortType.DESC.key, 100);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onColorSuccess(ResponseCode.NOT_FOUND, null, 0);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    callback.onColorSuccess(ResponseCode.UNAUTHORIZED, null, 0);
                    return;
                }
                List<Post> posts = response.body();
                callback.onColorSuccess(ResponseCode.SUCCESS, posts, listCount);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });
    }

    public void getCategoryAndColorSearchPosts(String category, String color, int page) {
        final String auth = PreferenceUtils.getAuth();
        Call<List<Post>> call = retrofitService.getCategoryAndColorQueryPosts(
                auth, category, color, page, SortType.DESC.key, 100);
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
}
