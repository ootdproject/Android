package itmediaengineering.duksung.ootd.chat_list.adapter;

import java.util.ArrayList;


public interface ChatAdapterContract {
    interface View {
        //void setOnPositionListener(OnPositionListener onPositionListener);
        void setOnClickListener(OnChatItemClickListener onClickListener);
        void notifyAdapter();
    }

    interface Model {
        void getChat();
        //void setFeeds(ArrayList items);
        void addChat(ArrayList items);
        void deleteChat();
    }
}
