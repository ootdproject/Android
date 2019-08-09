package itmediaengineering.duksung.ootd.main.tab.feed.model;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.api.SharePreferenceManager;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.post.PostResponse;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedRetrofitModel {
    private FeedRetrofitCallback.RetrofitCallback callback;
    private RetrofitService retrofitService;
    private static final String Authorization = "eyJ0eXAiOiJKV1QiLCJyZWdEYXRlIjoxNTYyNDg3NjM3NjMwLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwibWVtYmVyIjp7InByb3ZpZGVyVHlwZSI6IkdPT0dMRSIsInByb3ZpZGVyVXNlcklkIjoic3RyaW5nMSIsIm1lbWJlcklkIjoyOSwidG9rZW4iOiJzdHJpbmcxIiwibmlja25hbWUiOiJuaWNrbmFtZTEiLCJnZW5kZXIiOiJ3b21hbiIsImNyZWF0ZWRBdCI6eyJ5ZWFyIjoyMDE5LCJtb250aCI6IkpVTFkiLCJtb250aFZhbHVlIjo3LCJkYXlPZk1vbnRoIjo3LCJob3VyIjoxNywibWludXRlIjoxNywic2Vjb25kIjo0OCwibmFubyI6MCwiZGF5T2ZXZWVrIjoiU1VOREFZIiwiZGF5T2ZZZWFyIjoxODgsImNocm9ub2xvZ3kiOnsiaWQiOiJJU08iLCJjYWxlbmRhclR5cGUiOiJpc284NjAxIn19LCJ1cGRhdGVkQXQiOnsieWVhciI6MjAxOSwibW9udGgiOiJKVUxZIiwibW9udGhWYWx1ZSI6NywiZGF5T2ZNb250aCI6NywiaG91ciI6MTcsIm1pbnV0ZSI6MTcsInNlY29uZCI6NDgsIm5hbm8iOjAsImRheU9mV2VlayI6IlNVTkRBWSIsImRheU9mWWVhciI6MTg4LCJjaHJvbm9sb2d5Ijp7ImlkIjoiSVNPIiwiY2FsZW5kYXJUeXBlIjoiaXNvODYwMSJ9fX19.xyFqEVjc-8QDwoIj5Jx6OfTmx3HMxkfpW54bfWqVNfU";

    public FeedRetrofitModel() {
        retrofitService = RetrofitServiceManager.getRetrofitInstance();
    }

    public void setCallback(FeedRetrofitCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void getPosts() {
        List<Post> posts = new ArrayList<>();
        /*posts.add(new Post("ds_1", "https://scontent-lga3-1.cdninstagram.com/vp/196eceb769645674672b62923264e6d5/5D5C4E1D/t51.2885-15/e35/50533026_237089167167041_3844409858136011663_n.jpg?_nc_ht=scontent-lga3-1.cdninstagram.com"));
        posts.add(new Post("ds_2", "https://scontent-lga3-1.cdninstagram.com/vp/44d378eab651886cd925c40c2862b3c1/5D3D26C9/t51.2885-15/e35/50543163_240536733501785_3566039216138549427_n.jpg?_nc_ht=scontent-lga3-1.cdninstagram.com"));
        posts.add(new Post("ds_3", "https://usercontents-c.styleshare.kr/images/24295429/1280x-"));
        posts.add(new Post("ds_4", "https://usercontents-c.styleshare.kr/images/36724718/640x640"));
        posts.add(new Post("ds_5","https://scontent-sjc3-1.cdninstagram.com/vp/a7b48dfdff5d8ed9590ec801bdfbb822/5D74F782/t51.2885-15/e35/p1080x1080/54238006_2813505512208263_5380136515054087532_n.jpg?_nc_ht=scontent-sjc3-1.cdninstagram.com"));
        posts.add(new Post("ds_1", "https://scontent-lga3-1.cdninstagram.com/vp/196eceb769645674672b62923264e6d5/5D5C4E1D/t51.2885-15/e35/50533026_237089167167041_3844409858136011663_n.jpg?_nc_ht=scontent-lga3-1.cdninstagram.com"));
        posts.add(new Post("ds_2", "https://scontent-lga3-1.cdninstagram.com/vp/44d378eab651886cd925c40c2862b3c1/5D3D26C9/t51.2885-15/e35/50543163_240536733501785_3566039216138549427_n.jpg?_nc_ht=scontent-lga3-1.cdninstagram.com"));
        posts.add(new Post("ds_3", "https://usercontents-c.styleshare.kr/images/24295429/1280x-"));
        posts.add(new Post("ds_4", "https://usercontents-c.styleshare.kr/images/36724718/640x640"));
        posts.add(new Post("ds_5","https://scontent-sjc3-1.cdninstagram.com/vp/a7b48dfdff5d8ed9590ec801bdfbb822/5D74F782/t51.2885-15/e35/p1080x1080/54238006_2813505512208263_5380136515054087532_n.jpg?_nc_ht=scontent-sjc3-1.cdninstagram.com"));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));
        posts.add(new Post("test_user",null));*/

        //callback.onSuccess(ResponseCode.SUCCESS, posts);
        //String ordering = sort == 0 ? "start_time" : "-created_at";
        //String token = SharePreferenceManager.getString("Token");
        Call<List<Post>> call = retrofitService.getPosts(Authorization);
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
