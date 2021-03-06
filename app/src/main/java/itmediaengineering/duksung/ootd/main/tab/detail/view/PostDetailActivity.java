package itmediaengineering.duksung.ootd.main.tab.detail.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    public static final String START_ACTIVITY_FROM_POST_DETAIL = "START_ACTIVITY_FROM_POST_DETAIL";

    static final int STATE_SELECT_USERS = 0;
    static final int STATE_SELECT_DISTINCT = 1;

    /*@BindView(R.id.detail_activity_back_btn)
    ImageView backBtn;*/
    @BindView(R.id.ivActivityMaterialDetail)
    ImageView postImgView;
    /*@BindView(R.id.detail_activity_user_profile_img)
    ImageView userImg;*/
    /*@BindView(R.id.detail_activity_nickname_view)
    TextView userNick;*/
    @BindView(R.id.tbActivityMaterialDetail)
    Toolbar toolbar;
    @BindView(R.id.detail_activity_post_title)
    TextView postTitle;
    @BindView(R.id.detail_activity_user)
    TextView postUserId;
    @BindView(R.id.detail_activity_cost_view)
    TextView postCost;
    @BindView(R.id.detail_activity_dong_and_update_time)
    TextView dongAndTime;
    @BindView(R.id.detail_activity_description_view)
    TextView postDesc;
    @BindView(R.id.fabActivityMaterialDetail)
    FloatingActionButton chattingBtn;

    //SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

    private List<String> mSelectedIds;
    private boolean mIsDistinct = true;
    private Member postWriter;

    private int mCurrentState;

    private List<String> selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Post post = intent.getParcelableExtra("post");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setTitle("판매 정보");
            //actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }

        Glide.with(this)
                .load(post.getImageUrl())
                .into(postImgView);

        //userNick.setText(post.getMember().getNickname());
        postTitle.setText(post.getTitle());
        postUserId.setText("판매자 : " + post.getMember().getNickname());
        postCost.setText(post.getCost());
        dongAndTime.setText(post.getDong() + "  |  " + post.getCreatedAt().split("T")[0]);
        postDesc.setText(post.getDescription());

        mSelectedIds = new ArrayList<>();
        mSelectedIds.add(post.getMember().getProviderUserId());
    }

    @OnClick(R.id.fabActivityMaterialDetail)
    public void onChattingBtnClick() {
        /*String userId = PreferenceUtils.getProviderUserId();
        String userNickname = PreferenceUtils.getNickname();

        connectToSendBird(userId, userNickname);*/

        if (mCurrentState == STATE_SELECT_USERS) {
            mIsDistinct = PreferenceUtils.getGroupChannelDistinct();
            createGroupChannel(mSelectedIds, mIsDistinct);

            /*Intent intent = new Intent(PostDetailActivity.this, ChatActivity.class);
            intent.putExtra("chatName", userNick.getText().toString());
            startActivity(intent);*/
        }
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
            intent.putExtra(START_ACTIVITY_FROM_POST_DETAIL, true);
            setResult(RESULT_OK, intent);
            startActivity(intent);
            //finish();
        });
    }

    private void setState(int state) {
        if (state == STATE_SELECT_USERS) {
            mCurrentState = STATE_SELECT_USERS;
        } else if (state == STATE_SELECT_DISTINCT) {
            mCurrentState = STATE_SELECT_DISTINCT;
        }
    }

    public interface onBackPressedListener {
        boolean onBack();
    }
    private PostDetailActivity.onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(PostDetailActivity.onBackPressedListener listener) {
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
}
