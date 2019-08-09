package itmediaengineering.duksung.ootd.chat_list.presenter;

import itmediaengineering.duksung.ootd.chat_list.adapter.ChatAdapterContract;
import itmediaengineering.duksung.ootd.data.chat.ChatDTO;

public interface ChatContract {
    interface View {
        void toast(String msg);
        //void onUnauthorizedError();
        //void onUnknownError();
        void onSuccessGetChatList();
        //void onConnectFail();
        void startChat(ChatDTO chat);
        //void onNotFound();
    }

    interface Presenter {
        void getCreatedChats();
        void attachView(ChatContract.View view);
        void detachView();
        void setAdapterView(ChatAdapterContract.View adapterView);
        void setAdapterModel(ChatAdapterContract.Model adapterModel);
    }
}