package itmediaengineering.duksung.ootd.chat_list.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>
        implements ChatAdapterContract.View, ChatAdapterContract.Model{
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void setOnClickListener(OnChatItemClickListener onClickListener) {

    }

    @Override
    public void notifyAdapter() {

    }

    @Override
    public void getChat() {

    }

    @Override
    public void addChat(ArrayList items) {

    }

    @Override
    public void deleteChat() {

    }
}
