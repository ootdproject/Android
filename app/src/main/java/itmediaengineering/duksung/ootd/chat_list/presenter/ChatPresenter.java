package itmediaengineering.duksung.ootd.chat_list.presenter;

import itmediaengineering.duksung.ootd.chat_list.adapter.ChatAdapterContract;
import itmediaengineering.duksung.ootd.chat_list.adapter.OnChatItemClickListener;

public class ChatPresenter implements ChatContract.Presenter, OnChatItemClickListener {
    private static final String TAG = ChatPresenter.class.getSimpleName();
    private ChatContract.View view;
    private ChatAdapterContract.View adapterView;
    private ChatAdapterContract.Model adapterModel;

    @Override
    public void onItemClick() {
        // 채팅?
        view.startChat(null);
    }

    @Override
    public void getCreatedChats() {

    }

    @Override
    public void attachView(ChatContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setAdapterView(ChatAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnClickListener(this);
    }

    @Override
    public void setAdapterModel(ChatAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }
}
