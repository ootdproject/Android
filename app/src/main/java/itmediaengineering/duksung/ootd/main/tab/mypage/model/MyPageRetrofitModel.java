package itmediaengineering.duksung.ootd.main.tab.mypage.model;

import java.util.ArrayList;
import java.util.List;

import itmediaengineering.duksung.ootd.data.mygallery.GalleryResponse;
import itmediaengineering.duksung.ootd.data.mygallery.Photo;
import itmediaengineering.duksung.ootd.data.mygallery.Photos;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;
import itmediaengineering.duksung.ootd.retrofit.RetrofitService;
import itmediaengineering.duksung.ootd.retrofit.RetrofitServiceManager;
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

    public void getGallery() {

        Call<List<Post>> call = retrofitService.getMyPosts("eyJ0eXAiOiJKV1QiLCJyZWdEYXRlIjoxNTYyNDg3NjM3NjMwLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwibWVtYmVyIjp7InByb3ZpZGVyVHlwZSI6IkdPT0dMRSIsInByb3ZpZGVyVXNlcklkIjoic3RyaW5nMSIsIm1lbWJlcklkIjoyOSwidG9rZW4iOiJzdHJpbmcxIiwibmlja25hbWUiOiJuaWNrbmFtZTEiLCJnZW5kZXIiOiJ3b21hbiIsImNyZWF0ZWRBdCI6eyJ5ZWFyIjoyMDE5LCJtb250aCI6IkpVTFkiLCJtb250aFZhbHVlIjo3LCJkYXlPZk1vbnRoIjo3LCJob3VyIjoxNywibWludXRlIjoxNywic2Vjb25kIjo0OCwibmFubyI6MCwiZGF5T2ZXZWVrIjoiU1VOREFZIiwiZGF5T2ZZZWFyIjoxODgsImNocm9ub2xvZ3kiOnsiaWQiOiJJU08iLCJjYWxlbmRhclR5cGUiOiJpc284NjAxIn19LCJ1cGRhdGVkQXQiOnsieWVhciI6MjAxOSwibW9udGgiOiJKVUxZIiwibW9udGhWYWx1ZSI6NywiZGF5T2ZNb250aCI6NywiaG91ciI6MTcsIm1pbnV0ZSI6MTcsInNlY29uZCI6NDgsIm5hbm8iOjAsImRheU9mV2VlayI6IlNVTkRBWSIsImRheU9mWWVhciI6MTg4LCJjaHJvbm9sb2d5Ijp7ImlkIjoiSVNPIiwiY2FsZW5kYXJUeXBlIjoiaXNvODYwMSJ9fX19.xyFqEVjc-8QDwoIj5Jx6OfTmx3HMxkfpW54bfWqVNfU"
                , "vrUMvaDqKiVJT6y3EAzy4F9JMtu1");
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
