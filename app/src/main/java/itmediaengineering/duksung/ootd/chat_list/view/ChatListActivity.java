package itmediaengineering.duksung.ootd.chat_list.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.chat_detail.GroupChatFragment;
import itmediaengineering.duksung.ootd.chat_list.presenter.ChatContract;
import itmediaengineering.duksung.ootd.data.chat.ChatDTO;

public class ChatListActivity extends AppCompatActivity
        implements ChatContract.View{

    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";
    public static final String START_ACTIVITY_FROM_POST_DETAIL = "START_ACTIVITY_FROM_POST_DETAIL";

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        Toolbar toolbar = findViewById(R.id.toolbar_group_channel);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.outline_arrow_back_black_24);
        }

        if(!getIntent().getBooleanExtra(START_ACTIVITY_FROM_POST_DETAIL, false)) {
            if (savedInstanceState == null) {
                // Load list of Group Channels
                Fragment fragment = GroupChannelListFragment.newInstance();
                FragmentManager manager = getSupportFragmentManager();
                manager.popBackStack();

                manager.beginTransaction()
                        .replace(R.id.container_group_channel, fragment)
                        .commit();
            }
        }

        final String channelUrl = getIntent().getStringExtra(EXTRA_NEW_CHANNEL_URL);
        if(channelUrl != null) {
            // If started from notification
            Fragment fragment = GroupChatFragment.newInstance(channelUrl);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    //.addToBackStack(null)
                    .commit();
        }
    }

    public interface onBackPressedListener {
        boolean onBack();
    }
    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void onSuccessGetChatList() {

    }

    @Override
    public void startChat(ChatDTO chat) {

    }
}
