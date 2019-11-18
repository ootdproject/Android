package itmediaengineering.duksung.ootd.main.tab.category.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.main.tab.category.presenter.CategoryType;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
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

    public void getCategoryPosts(String category, int page, CategoryType categoryType){
        final String auth = PreferenceUtils.getAuth();

        final Callback<List<Post>> callback = new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    CategoryRetrofitModel.this.callback.onSuccess(ResponseCode.NOT_FOUND, null);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    CategoryRetrofitModel.this.callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
                    return;
                }
                List<Post> posts = response.body();
                CategoryRetrofitModel.this.callback.onSuccess(ResponseCode.SUCCESS, posts);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                CategoryRetrofitModel.this.callback.onFailure();
            }
        };

        final Call<List<Post>> call = categoryType.type == CategoryType.categoryA.type ?
                retrofitService.getCategoryAPosts(auth, category, page, SortType.DESC.key, 100) :
                retrofitService.getCategoryBPosts(auth, category, page, SortType.DESC.key, 100);

        call.enqueue(callback);
    }
}
