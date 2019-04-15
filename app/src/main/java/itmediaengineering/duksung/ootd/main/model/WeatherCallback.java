package itmediaengineering.duksung.ootd.main.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.weather.Item;

public interface WeatherCallback {
    interface RetrofitCallback{
        void onSuccess(int code, List<Item> items);
        void onFailure();
    }
}
