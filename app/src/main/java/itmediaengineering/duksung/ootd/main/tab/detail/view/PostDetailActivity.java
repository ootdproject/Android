package itmediaengineering.duksung.ootd.main.tab.detail.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.chat_list.view.ChatListActivity;
import itmediaengineering.duksung.ootd.data.post.Member;
import itmediaengineering.duksung.ootd.data.post.Post;
import itmediaengineering.duksung.ootd.utils.ConnectionManager;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import itmediaengineering.duksung.ootd.utils.PushUtils;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";

    static final int STATE_SELECT_USERS = 0;
    static final int STATE_SELECT_DISTINCT = 1;

    @BindView(R.id.detail_activity_back_btn)
    ImageView backBtn;
    @BindView(R.id.detail_activity_post_img)
    ImageView postImgView;
    @BindView(R.id.detail_activity_user_profile_img)
    ImageView userImg;
    @BindView(R.id.detail_activity_nickname_view)
    TextView userNick;
    @BindView(R.id.detail_activity_post_title)
    TextView postTitle;
    @BindView(R.id.detail_activity_cost_view)
    TextView postCost;
    @BindView(R.id.detail_activity_dong_and_update_time)
    TextView dongAndTime;
    @BindView(R.id.detail_activity_description_view)
    TextView postDesc;
    @BindView(R.id.detail_activity_chatting_btn)
    TextView chattingBtn;

    //SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

    private List<String> mSelectedIds;
    private boolean mIsDistinct = true;
    private Member postWriter;

    private int mCurrentState;

    private List<String> selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Post post = intent.getParcelableExtra("post");

        Glide.with(this)
                .load(post.getImageUrl())
                .into(postImgView);

        userNick.setText(post.getMember().getNickname());
        postTitle.setText(post.getTitle());
        postCost.setText(post.getCost());
        dongAndTime.setText(post.getDong() + "  |  " + post.getCreatedAt());
        postDesc.setText(post.getDescription());

        mSelectedIds = new ArrayList<>();
    }

    @OnClick(R.id.detail_activity_chatting_btn)
    public void onChattingBtnClick() {
        /*writeNewUser(
                sharedPreferences.getString("uid",""),
                sharedPreferences.getString("email",""),
                sharedPreferences.getString("nickname",""));*/

        //mSelectedIds.add(postWriter.getProviderUserId());
        PreferenceUtils.setUserId("test0802");
        PreferenceUtils.setNickname("vrUMvaDqKiVJT6y3EAzy4F9JMtu1");

        connectToSendBird("test0802", "vrUMvaDqKiVJT6y3EAzy4F9JMtu1");

        mSelectedIds.add("mung");

        if (mCurrentState == STATE_SELECT_USERS) {
            mIsDistinct = PreferenceUtils.getGroupChannelDistinct();
            createGroupChannel(mSelectedIds, mIsDistinct);

            /*Intent intent = new Intent(PostDetailActivity.this, ChatActivity.class);
            intent.putExtra("chatName", userNick.getText().toString());
            startActivity(intent);*/
        }
    }

    @OnClick(R.id.detail_activity_back_btn)
    public void onBackBtnClick() {
        finish();
    }

    private void createGroupChannel(List<String> userIds, boolean distinct) {
        GroupChannel.createChannelWithUserIds(userIds, distinct, (groupChannel, e) -> {
            if (e != null) {
                // Error!
                return;
            }

            Intent intent = new Intent(
                    PostDetailActivity.this, ChatListActivity.class);
            intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
            setResult(RESULT_OK, intent);
            startActivity(intent);
            finish();
        });
    }

    private void setState(int state) {
        if (state == STATE_SELECT_USERS) {
            mCurrentState = STATE_SELECT_USERS;
        } else if (state == STATE_SELECT_DISTINCT) {
            mCurrentState = STATE_SELECT_DISTINCT;
        }
    }

    private void connectToSendBird(final String userId, final String userNickname) {
        // Show the loading indicator
        //showProgressBar(true);
        //mConnectButton.setEnabled(false);

        ConnectionManager.login(userId, (user, e) -> {
            // Callback received; hide the progress bar.
            //showProgressBar(false);

            if (e != null) {
                // Error!
                Toast.makeText(
                        PostDetailActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();

                // Show login failure snackbar
                //showSnackbar("Login to SendBird failed");
                //mConnectButton.setEnabled(true);
                PreferenceUtils.setConnected(false);
                return;
            }

            PreferenceUtils.setConnected(true);

            // Update the user's nickname
            updateCurrentUserInfo(userNickname);
            updateCurrentUserPushToken();

            // Proceed to MainActivity
            /*Intent intent = new Intent(PostDetailActivity.this, MainActivity.class);
            startActivity(intent);
            finish();*/
        });
    }

    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(PostDetailActivity.this, null);
    }

    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, e -> {
            if (e != null) {
                // Error!
                Toast.makeText(
                        PostDetailActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();

                // Show update failed snackbar
                //showSnackbar("Update user nickname failed");

                return;
            }

            PreferenceUtils.setNickname(userNickname);
        });
    }
}
