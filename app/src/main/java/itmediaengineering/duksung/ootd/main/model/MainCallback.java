package itmediaengineering.duksung.ootd.main.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.location.Document;
import itmediaengineering.duksung.ootd.data.weather.Item;

public interface MainCallback {
    interface RetrofitCallback{
        void onSuccess(int code, List<Item> weather, List<Document> location);
        void onFailure();
    }
}
