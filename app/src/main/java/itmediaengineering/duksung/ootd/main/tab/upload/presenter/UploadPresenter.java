package itmediaengineering.duksung.ootd.main.tab.upload.presenter;

import itmediaengineering.duksung.ootd.data.feed.Post;
import itmediaengineering.duksung.ootd.main.tab.upload.model.UploadRetrofitCallback;
import itmediaengineering.duksung.ootd.main.tab.upload.model.UploadRetrofitModel;
import itmediaengineering.duksung.ootd.retrofit.ResponseCode;

public class UploadPresenter implements UploadContract.Presenter, UploadRetrofitCallback.RetrofitCallback {

        private UploadContract.View view;
        private UploadRetrofitModel retrofitModel;

    public UploadPresenter() {
            retrofitModel = new UploadRetrofitModel();
            retrofitModel.setCallback(this);
        }

        @Override
        public void onSuccess(int code){
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
                //view.onSuccessGetList();
                view.resumeUpLoadFragment(code);
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

        @Override
        public void upload(Post post) {
            retrofitModel.upload(post);
        }
}
