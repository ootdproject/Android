package itmediaengineering.duksung.ootd.main.tab.upload.presenter;

import java.io.File;

import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.data.post.PostRequest;
import itmediaengineering.duksung.ootd.main.tab.upload.model.UploadRetrofitCallback;
import itmediaengineering.duksung.ootd.main.tab.upload.model.UploadRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class UploadPresenter implements
        UploadContract.Presenter, UploadRetrofitCallback.RetrofitCallback {

    private UploadContract.View view;
    private UploadRetrofitModel retrofitModel;

    public UploadPresenter() {
        retrofitModel = new UploadRetrofitModel();
        retrofitModel.setCallback(this);
    }

    @Override
    public void upload(PostRequest postRequest, File file) {
        retrofitModel.upload(postRequest, file);
    }

    @Override
    public void editPostContents(PostRequest post, int postId) {
        retrofitModel.editPostContents(post, postId);
    }

    @Override
    public void onSuccess(int code) {
        if (code == ResponseCode.NOT_FOUND) {
            //view.onNotFound();
            return;
        }
        if (code == ResponseCode.BAD_REQUEST) {
            //view.onUnauthorizedError();
            return;
        }
        if (code == ResponseCode.UNDOCUMENTED) {
            //view.onUnauthorizedError();
            return;
        }
        if (code == ResponseCode.CREATED) {
            //Log.d(TAG, posts.get(0).toString());
            //adapterModel.addPosts(new ArrayList(posts));
            view.onUploadSuccess();
            //view.resumeUpLoadFragment(code);
            return;
        }
    }

    @Override
    public void onFailure() {
        view.connectFail();
    }

    @Override
    public void attachView(UploadContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
