package itmediaengineering.duksung.ootd.main.tab.category.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.weather.Item;

public interface CategoryCallback {
    interface RetrofitCallback{
        void onSuccess(int code, List<Post> posts);
        void onFailure();
    }
}
