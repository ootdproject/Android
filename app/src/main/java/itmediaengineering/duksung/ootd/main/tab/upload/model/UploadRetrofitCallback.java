package itmediaengineering.duksung.ootd.main.tab.upload.model;

public interface UploadRetrofitCallback {
    interface RetrofitCallback {
        void onSuccess(int code);
        void onFailure();
    }
}
