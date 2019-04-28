package itmediaengineering.duksung.ootd.feed.model;

import java.util.ArrayList;
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
        //retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(FeedRetrofitCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void getPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("ds_1", "https://scontent-lga3-1.cdninstagram.com/vp/196eceb769645674672b62923264e6d5/5D5C4E1D/t51.2885-15/e35/50533026_237089167167041_3844409858136011663_n.jpg?_nc_ht=scontent-lga3-1.cdninstagram.com"));
        posts.add(new Post("ds_2", "https://scontent-lga3-1.cdninstagram.com/vp/44d378eab651886cd925c40c2862b3c1/5D3D26C9/t51.2885-15/e35/50543163_240536733501785_3566039216138549427_n.jpg?_nc_ht=scontent-lga3-1.cdninstagram.com"));
        posts.add(new Post("ds_3", "https://usercontents-c.styleshare.kr/images/24295429/1280x-"));
        posts.add(new Post("ds_4", "https://usercontents-c.styleshare.kr/images/36724718/640x640"));
        posts.add(new Post("ds_5",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));

        callback.onSuccess(ResponseCode.SUCCESS, posts);
        /*String ordering = sort == 0 ? "start_time" : "-created_at";
        String token = SharePreferenceManager.getString("Token");
        Call<PostResponse> call = retrofitService.getPosts(token, search, page);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.code() == ResponseCode.NOT_FOUND) {
                    callback.onSuccess(ResponseCode.NOT_FOUND, null);
                    return;
                }
                if (response.code() == ResponseCode.UNAUTHORIZED) {
                    callback.onSuccess(ResponseCode.UNAUTHORIZED, null);
                    return;
                }
                List<Post> posts = response.body().getResult().getData();
                callback.onSuccess(ResponseCode.SUCCESS, posts);
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure();
            }
        });*/
    }
}
