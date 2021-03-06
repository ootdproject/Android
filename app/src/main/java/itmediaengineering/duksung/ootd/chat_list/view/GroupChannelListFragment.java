package itmediaengineering.duksung.ootd.chat_list.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.syncmanager.ChannelCollection;
import com.sendbird.syncmanager.ChannelEventAction;
import com.sendbird.syncmanager.SendBirdSyncManager;
import com.sendbird.syncmanager.handler.ChannelCollectionHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.BaseApplication;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.chat_list.adapter.GroupChannelListAdapter;
import itmediaengineering.duksung.ootd.main.tab.detail.view.PostDetailActivity;
import itmediaengineering.duksung.ootd.utils.ConnectionManager;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;

import static android.app.Activity.RESULT_OK;

public class GroupChannelListFragment extends Fragment {

    public static final String EXTRA_GROUP_CHANNEL_URL = "GROUP_CHANNEL_URL";
    private static final int INTENT_REQUEST_NEW_GROUP_CHANNEL = 302;

    private static final int CHANNEL_LIST_LIMIT = 15;
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHANNEL_LIST";
    private static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_LIST";

    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";

    @BindView(R.id.recycler_group_channel_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_layout_group_channel_list)
    SwipeRefreshLayout mSwipeRefresh;

    private LinearLayoutManager mLayoutManager;
    private GroupChannelListAdapter mChannelListAdapter;
    private ChannelCollection mChannelCollection;

    public static GroupChannelListFragment newInstance() {
        GroupChannelListFragment fragment = new GroupChannelListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("LIFECYCLE", "GroupChannelListFragment onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_group_channel_list, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this, rootView);

        // Change action bar title
        //((ChatListActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.all_group_channels));

        mSwipeRefresh.setOnRefreshListener(() -> {
            mSwipeRefresh.setRefreshing(true);
            refresh();
        });

        mChannelListAdapter = new GroupChannelListAdapter(getActivity());

        setUpRecyclerView();
        setUpChannelListAdapter();

        return rootView;
    }

    @Override
    public void onResume() {
        Log.d("LIFECYCLE", "GroupChannelListFragment onResume()");

        String userId = PreferenceUtils.getUserId();

        SendBirdSyncManager.setup(getActivity().getApplicationContext(), userId, e -> {
            if (getActivity() == null) {
                return;
            }

            ((BaseApplication)getActivity().getApplication()).setSyncManagerSetup(true);
            getActivity().runOnUiThread(() -> {
                if (SendBird.getConnectionState() != SendBird.ConnectionState.OPEN) {
                    refresh();
                }

                ConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER_ID, reconnect -> refresh());
            });
        });

        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("LIFECYCLE", "GroupChannelListFragment onPause()");
        if (SendBird.getConnectionState() != SendBird.ConnectionState.OPEN) {

        }

        ConnectionManager.removeConnectionManagementHandler(CONNECTION_HANDLER_ID);
        if (mChannelCollection != null) {
            mChannelCollection.setCollectionHandler(null);
            mChannelCollection.remove();
        }

        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_REQUEST_NEW_GROUP_CHANNEL) {
            if (resultCode == RESULT_OK) {
                // Channel successfully created
                // Enter the newly created channel.
                String newChannelUrl = data.getStringExtra(PostDetailActivity.EXTRA_NEW_CHANNEL_URL);
                if (newChannelUrl != null) {
                    enterGroupChannel(newChannelUrl);
                }
            } else {
                Log.d("GrChLIST", "resultCode not STATUS_OK");
            }
        }
    }

    // Sets up recycler view
    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChannelListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // If user scrolls to bottom of the list, loads more channels.
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mLayoutManager.findLastVisibleItemPosition() == mChannelListAdapter.getItemCount() - 1) {
                        if (mChannelCollection != null) {
                            mChannelCollection.fetch(e -> {
                                if (mSwipeRefresh.isRefreshing()) {
                                    mSwipeRefresh.setRefreshing(false);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    // Sets up channel list feedAdapter
    private void setUpChannelListAdapter() {
        mChannelListAdapter.setOnItemClickListener(channel -> enterGroupChannel(channel));

        mChannelListAdapter.setOnItemLongClickListener(channel -> showChannelOptionsDialog(channel));
    }

    /**
     * Displays a dialog listing channel-specific options.
     */
    private void showChannelOptionsDialog(final GroupChannel channel) {
        String[] options;
        final boolean pushCurrentlyEnabled = channel.isPushEnabled();

        options = pushCurrentlyEnabled
                ? new String[]{"Leave channel", "Turn push notifications OFF"}
                : new String[]{"Leave channel", "Turn push notifications ON"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Channel options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Show a dialog to confirm that the user wants to leave the channel.
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Leave channel " + channel.getName() + "?")
                                .setPositiveButton("Leave", (dialog1, which1) -> leaveChannel(channel))
                                .setNegativeButton("Cancel", null)
                                .create().show();
                    } else if (which == 1) {
                        setChannelPushPreferences(channel, !pushCurrentlyEnabled);
                    }
                });
        builder.create().show();
    }

    /**
     * Turns push notifications on or off for a selected channel.
     * @param channel   The channel for which push preferences should be changed.
     * @param on    Whether to set push notifications on or off.
     */
    private void setChannelPushPreferences(final GroupChannel channel, final boolean on) {
        // Change push preferences.
        channel.setPushPreference(on, e -> {
            if (e != null) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            String toast = on
                    ? "Push notifications have been turned ON"
                    : "Push notifications have been turned OFF";

            Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT)
                    .show();
        });
    }

    /**
     * Enters a Group Channel. Upon entering, a GroupChatFragment will be inflated
     * to display messages within the channel.
     *
     * @param channel The Group Channel to enter.
     */
    void enterGroupChannel(GroupChannel channel) {
        final String channelUrl = channel.getUrl();

        enterGroupChannel(channelUrl);
    }

    /**
     * Enters a Group Channel with a URL.
     *
     * @param channelUrl The URL of the channel to enter.
     */
    void enterGroupChannel(String channelUrl) {
        /*GroupChatFragment fragment = GroupChatFragment.newInstance(channelUrl);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_group_channel, fragment)
                .addToBackStack(null)
                .commit();*/
        Intent intent = new Intent(
                getContext(), ChatListActivity.class);
        intent.putExtra(EXTRA_NEW_CHANNEL_URL, channelUrl);
        startActivity(intent);
    }

    private void refresh() {
        refreshChannelList(CHANNEL_LIST_LIMIT);
    }

    /**
     * Creates a new query to get the list of the user's Group Channels,
     * then replaces the existing dataset.
     *
     * @param numChannels The number of channels to load.
     */
    private void refreshChannelList(int numChannels) {
        if (mChannelCollection != null) {
            mChannelCollection.remove();
        }

        mChannelListAdapter.clearMap();
        mChannelListAdapter.clearChannelList();
        GroupChannelListQuery query = GroupChannel.createMyGroupChannelListQuery();
        query.setLimit(numChannels);
        mChannelCollection = new ChannelCollection(query);
        mChannelCollection.setCollectionHandler(mChannelCollectionHandler);
        mChannelCollection.fetch(e -> {
            if (mSwipeRefresh.isRefreshing()) {
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    /**
     * Leaves a Group Channel.
     *
     * @param channel The channel to leave.
     */
    private void leaveChannel(final GroupChannel channel) {
        channel.leave(e -> {
            if (e != null) {
                // Error!
                return;
            }

            // Re-query message list
            refresh();
        });
    }

    ChannelCollectionHandler mChannelCollectionHandler = new ChannelCollectionHandler() {
        @Override
        public void onChannelEvent(final ChannelCollection channelCollection,
                                   final List<GroupChannel> list,
                                   final ChannelEventAction channelEventAction) {
            Log.d("SyncManager", "onChannelEvent: size = " + list.size() + ", action = " + channelEventAction);
            if (getActivity() == null) {
                return;
            }

            getActivity().runOnUiThread(() -> {
                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }

                switch (channelEventAction) {
                    case INSERT:
                        mChannelListAdapter.clearMap();
                        mChannelListAdapter.insertChannels(list, channelCollection.getQuery().getOrder());
                        break;

                    case UPDATE:
                        mChannelListAdapter.clearMap();
                        mChannelListAdapter.updateChannels(list);
                        break;

                    case MOVE:
                        mChannelListAdapter.clearMap();
                        mChannelListAdapter.moveChannels(list, channelCollection.getQuery().getOrder());
                        break;

                    case REMOVE:
                        mChannelListAdapter.clearMap();
                        mChannelListAdapter.removeChannels(list);
                        break;

                    case CLEAR:
                        mChannelListAdapter.clearMap();
                        mChannelListAdapter.clearChannelList();
                        break;
                }
            });
        }
    };
}
