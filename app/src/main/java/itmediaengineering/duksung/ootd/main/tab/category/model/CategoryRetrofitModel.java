package itmediaengineering.duksung.ootd.main.tab.category.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.retrofit.LocationApi;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import itmediaengineering.duksung.ootd.retrofit.WeatherApi;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import itmediaengineering.duksung.ootd.utils.SortType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRetrofitModel {
    private CategoryCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;

    public CategoryRetrofitModel(){
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(CategoryCallback.RetrofitCallback callback){
        this.callback = callback;
    }

    public void getCategoryPosts(String category, int page){
        final String auth = PreferenceUtils.getAuth();
        Call<List<Post>> call = retrofitService.getCategoryPosts(auth, category, page, SortType.DESC.key);
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
