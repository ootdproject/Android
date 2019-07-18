package itmediaengineering.duksung.ootd.main.tab.upload.presenter;

import itmediaengineering.duksung.ootd.data.feed.Post;

public interface UploadContract {
    interface View {
        void toast(String msg);
        void resumeUpLoadFragment(int code);
        void connectFail();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void upload(Post post);
    }
}
