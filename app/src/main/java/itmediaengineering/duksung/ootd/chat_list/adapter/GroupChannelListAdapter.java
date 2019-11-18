package itmediaengineering.duksung.ootd.chat_list.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.Member;
import com.sendbird.android.UserMessage;
import com.stfalcon.multiimageview.MultiImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.utils.DateUtils;
import itmediaengineering.duksung.ootd.utils.SyncManagerUtils;
import itmediaengineering.duksung.ootd.utils.TextUtils;
import itmediaengineering.duksung.ootd.utils.TypingIndicator;

/**
 * Displays a list of Group Channels within a SendBird application.
 */
public class GroupChannelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<GroupChannel> mChannelList;
    private Context mContext;
    public ConcurrentHashMap<SimpleTarget<Bitmap>, Integer> mSimpleTargetIndexMap;
    public ConcurrentHashMap<SimpleTarget<Bitmap>, GroupChannel> mSimpleTargetGroupChannelMap;
    public ConcurrentHashMap<String, Integer> mChannelImageNumMap;
    public ConcurrentHashMap<String, ImageView> mChannelImageViewMap;
    public ConcurrentHashMap<String, SparseArray<Bitmap>> mChannelBitmapMap;

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(GroupChannel channel);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(GroupChannel channel);
    }

    public GroupChannelListAdapter(Context context) {
        mContext = context;

        mSimpleTargetIndexMap = new ConcurrentHashMap<>();
        mSimpleTargetGroupChannelMap = new ConcurrentHashMap<>();
        mChannelImageNumMap = new ConcurrentHashMap<>();
        mChannelImageViewMap = new ConcurrentHashMap<>();
        mChannelBitmapMap = new ConcurrentHashMap<>();

        mChannelList = new ArrayList<>();
    }

    public void clearMap() {
        mSimpleTargetIndexMap.clear();
        mSimpleTargetGroupChannelMap.clear();
        mChannelImageNumMap.clear();
        mChannelImageViewMap.clear();
        mChannelBitmapMap.clear();
    }

    public void clearChannelList() {
        mChannelList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChannelHolder(
                mContext,
                parent,
                mSimpleTargetIndexMap,
                mSimpleTargetGroupChannelMap,
                mChannelImageNumMap,
                mChannelImageViewMap,
                mChannelBitmapMap);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ChannelHolder) holder).bind(position, mChannelList.get(position), mItemClickListener, mItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }

    public void insertChannels(List<GroupChannel> channels, GroupChannelListQuery.Order order) {
        for (GroupChannel newChannel : channels) {
            int index = SyncManagerUtils.findIndexOfChannel(mChannelList, newChannel, order);
            mChannelList.add(index, newChannel);
        }

        notifyDataSetChanged();
    }

    public void updateChannels(List<GroupChannel> channels) {
        for (GroupChannel updatedChannel : channels) {
            int index = SyncManagerUtils.getIndexOfChannel(mChannelList, updatedChannel);
            if (index != -1) {
                mChannelList.set(index, updatedChannel);
                notifyItemChanged(index);
            }
        }
    }

    public void moveChannels(List<GroupChannel> channels, GroupChannelListQuery.Order order) {
        for (GroupChannel movedChannel: channels) {
            int fromIndex = SyncManagerUtils.getIndexOfChannel(mChannelList, movedChannel);
            int toIndex = SyncManagerUtils.findIndexOfChannel(mChannelList, movedChannel, order);
            if (fromIndex != -1) {
                mChannelList.remove(fromIndex);
                mChannelList.add(toIndex, movedChannel);
                notifyItemMoved(fromIndex, toIndex);
                notifyItemChanged(toIndex);
            }
        }
    }

    public void removeChannels(List<GroupChannel> channels) {
        for (GroupChannel removedChannel : channels) {
            int index = SyncManagerUtils.getIndexOfChannel(mChannelList, removedChannel);
            if (index != -1) {
                mChannelList.remove(index);
                notifyItemRemoved(index);
            }
        }
    }

    // If the channel is not in the list yet, adds it.
    // If it is, finds the channel in current list, and replaces it.
    // Moves the updated channel to the front of the list.
    void updateOrInsert(BaseChannel channel) {
        if (!(channel instanceof GroupChannel)) {
            return;
        }

        GroupChannel groupChannel = (GroupChannel) channel;

        for (int i = 0; i < mChannelList.size(); i++) {
            if (mChannelList.get(i).getUrl().equals(groupChannel.getUrl())) {
                mChannelList.remove(mChannelList.get(i));
                mChannelList.add(0, groupChannel);
                notifyDataSetChanged();
                Log.v(GroupChannelListAdapter.class.getSimpleName(), "Channel replaced.");
                return;
            }
        }

        mChannelList.add(0, groupChannel);
        notifyDataSetChanged();
    }

    void update(List<GroupChannel> channels) {
        for (GroupChannel channel : channels) {
            for (int i = 0; i < mChannelList.size(); i++) {
                if (mChannelList.get(i).getUrl().equals(channel.getUrl())) {
                    notifyItemChanged(i);
                }
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mItemLongClickListener = listener;
    }
}
