package itmediaengineering.duksung.ootd.main.tab.detail.presenter;
import itmediaengineering.duksung.ootd.data.chat.ChatDTO;

public interface PostDetailContract {
    interface View {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        void onSuccessGetChatPost();
        //void onConnectFail();
        void startChat(ChatDTO chat);
        //void onNotFound();
    }

    interface Presenter {
        void openChat();
        //void attachView(ChatContract.View view);
        //void detachView();
    }
}
