package itmediaengineering.duksung.ootd.main.tab.upload.presenter;

import java.io.File;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.post.PostRequest;

public interface UploadContract {
    interface View {
        void toast(String msg);
        void onUploadSuccess();
        void resumeUpLoadFragment(int code);
        void connectFail();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void upload(PostRequest postRequest, File file);
        void editPostContents(PostRequest post, int postId);
    }
}
