package itmediaengineering.duksung.ootd.main.model;

import java.util.List;

import itmediaengineering.duksung.ootd.data.location.Document;

public interface LocationCallback {
    interface RetrofitCallback{
        void onSuccess(int code, List<Document> documents);
        void onFailure();
    }
}
