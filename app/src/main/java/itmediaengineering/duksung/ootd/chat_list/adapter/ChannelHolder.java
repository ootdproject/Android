package itmediaengineering.duksung.ootd.chat_list.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.UserMessage;
import com.stfalcon.multiimageview.MultiImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.utils.DateUtils;
import itmediaengineering.duksung.ootd.utils.TextUtils;
import itmediaengineering.duksung.ootd.utils.TypingIndicator;

public class ChannelHolder extends RecyclerView.ViewHolder {

    private Context context;

    @BindView(R.id.text_group_channel_list_topic)
    TextView topicText;
    @BindView(R.id.text_group_channel_list_message)
    TextView lastMessageText;
    @BindView(R.id.text_group_channel_list_unread_count)
    TextView unreadCountText;
    @BindView(R.id.text_group_channel_list_date)
    TextView dateText;
    @BindView(R.id.text_group_channel_list_member_count)
    TextView memberCountText;
    @BindView(R.id.image_group_channel_list_cover)
    MultiImageView coverImage;
    @BindView(R.id.container_group_channel_list_typing_indicator)
    LinearLayout typingIndicatorContainer;

    private ConcurrentHashMap<SimpleTarget<Bitmap>, Integer> mSimpleTargetIndexMap;
    private ConcurrentHashMap<SimpleTarget<Bitmap>, GroupChannel> mSimpleTargetGroupChannelMap;
    private ConcurrentHashMap<String, Integer> mChannelImageNumMap;
    private ConcurrentHashMap<String, ImageView> mChannelImageViewMap;
    private ConcurrentHashMap<String, SparseArray<Bitmap>> mChannelBitmapMap;

    ChannelHolder(
            final Context context,
            ViewGroup parent,
            ConcurrentHashMap<SimpleTarget<Bitmap>, Integer> simpleTargetIndexMap,
            ConcurrentHashMap<SimpleTarget<Bitmap>, GroupChannel> simpleTargetGroupChannelMap,
            ConcurrentHashMap<String, Integer> channelImageNumMap,
            ConcurrentHashMap<String, ImageView> channelImageViewMap,
            ConcurrentHashMap<String, SparseArray<Bitmap>> channelBitmapMap)
    {
        super(LayoutInflater.from(context).inflate(R.layout.list_item_group_channel, parent, false));
        ButterKnife.bind(this, itemView);

        mSimpleTargetIndexMap = simpleTargetIndexMap;
        mSimpleTargetGroupChannelMap = simpleTargetGroupChannelMap;
        mChannelImageNumMap = channelImageNumMap;
        mChannelImageViewMap = channelImageViewMap;
        mChannelBitmapMap = channelBitmapMap;
        this.context = context;
    }

    /**
     * Binds views in the ViewHolder to information contained within the Group Channel.
     * // @param context
     * @param channel
     * @param clickListener A listener that handles simple clicks.
     * @param longClickListener A listener that handles long clicks.
     */
    public void bind(int position, final GroupChannel channel,
              @Nullable final GroupChannelListAdapter.OnItemClickListener clickListener,
              @Nullable final GroupChannelListAdapter.OnItemLongClickListener longClickListener) {
        topicText.setText(TextUtils.getGroupChannelTitle(channel));
        memberCountText.setText(String.valueOf(channel.getMemberCount()));

        setChannelImage(context, position, channel, coverImage);

        int unreadCount = channel.getUnreadMessageCount();
        // If there are no unread messages, hide the unread count badge.
        if (unreadCount == 0) {
            unreadCountText.setVisibility(View.INVISIBLE);
        } else {
            unreadCountText.setVisibility(View.VISIBLE);
            unreadCountText.setText(String.valueOf(channel.getUnreadMessageCount()));
        }

        BaseMessage lastMessage = channel.getLastMessage();
        if (lastMessage != null) {
            dateText.setVisibility(View.VISIBLE);
            lastMessageText.setVisibility(View.VISIBLE);

            // Display information about the most recently sent message in the channel.
            dateText.setText(String.valueOf(DateUtils.formatDateTime(lastMessage.getCreatedAt())));

            // Bind last message text according to the type of message. Specifically, if
            // the last message is a File Message, there must be special formatting.
            if (lastMessage instanceof UserMessage) {
                lastMessageText.setText(((UserMessage) lastMessage).getMessage());
            } else if (lastMessage instanceof AdminMessage) {
                lastMessageText.setText(((AdminMessage) lastMessage).getMessage());
            } else {
                String lastMessageString = String.format(
                        context.getString(R.string.group_channel_list_file_message_text),
                        ((FileMessage) lastMessage).getSender().getNickname());
                lastMessageText.setText(lastMessageString);
            }
        } else {
            dateText.setVisibility(View.INVISIBLE);
            lastMessageText.setVisibility(View.INVISIBLE);
        }

        /*
         * Set up the typing indicator.
         * A typing indicator is basically just three dots contained within the layout
         * that animates. The animation is implemented in the {@link TypingIndicator#animate() class}
         */
        ArrayList<ImageView> indicatorImages = new ArrayList<>();
        indicatorImages.add(typingIndicatorContainer.findViewById(R.id.typing_indicator_dot_1));
        indicatorImages.add(typingIndicatorContainer.findViewById(R.id.typing_indicator_dot_2));
        indicatorImages.add(typingIndicatorContainer.findViewById(R.id.typing_indicator_dot_3));

        TypingIndicator indicator = new TypingIndicator(indicatorImages, 600);
        indicator.animate();

        // debug
//            typingIndicatorContainer.setVisibility(View.VISIBLE);
//            lastMessageText.setText(("Someone is typing"));

        // If someone in the channel is typing, display the typing indicator.
        if (channel.isTyping()) {
            typingIndicatorContainer.setVisibility(View.VISIBLE);
            lastMessageText.setText(("Someone is typing"));
        } else {
            // Display typing indicator only when someone is typing
            typingIndicatorContainer.setVisibility(View.GONE);
        }

        // Set an OnClickListener to this item.
        if (clickListener != null) {
            itemView.setOnClickListener(v -> clickListener.onItemClick(channel));
        }

        // Set an OnLongClickListener to this item.
        if (longClickListener != null) {
            itemView.setOnLongClickListener(v -> {
                longClickListener.onItemLongClick(channel);

                // return true if the callback consumed the long click
                return true;
            });
        }
    }

    private void setChannelImage(Context context, int position, GroupChannel channel, MultiImageView multiImageView) {
        List<Member> members = channel.getMembers();
        int size = members.size();
        if (size >= 1) {
            int imageNum = size;
            if (size >= 4) {
                imageNum = 4;
            }

            if (!mChannelImageNumMap.containsKey(channel.getUrl())) {
                mChannelImageNumMap.put(channel.getUrl(), imageNum);
                mChannelImageViewMap.put(channel.getUrl(), multiImageView);

                multiImageView.clear();

                for (int index = 0; index < imageNum; index++) {
                    SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, Transition<? super Bitmap> glideAnimation) {
                            GroupChannel channel = mSimpleTargetGroupChannelMap.get(this);
                            Integer index = mSimpleTargetIndexMap.get(this);
                            if (channel != null && index != null) {
                                SparseArray<Bitmap> bitmapSparseArray = mChannelBitmapMap.get(channel.getUrl());
                                if (bitmapSparseArray == null) {
                                    bitmapSparseArray = new SparseArray<>();
                                    mChannelBitmapMap.put(channel.getUrl(), bitmapSparseArray);
                                }
                                bitmapSparseArray.put(index, bitmap);

                                Integer num = mChannelImageNumMap.get(channel.getUrl());
                                if (num != null && num == bitmapSparseArray.size()) {
                                    MultiImageView multiImageView = (MultiImageView) mChannelImageViewMap.get(channel.getUrl());
                                    if (multiImageView != null) {
                                        for (int i = 0; i < bitmapSparseArray.size(); i++) {
                                            multiImageView.addImage(bitmapSparseArray.get(i));
                                        }
                                    }
                                }
                            }
                        }
                    };

                    mSimpleTargetIndexMap.put(simpleTarget, index);
                    mSimpleTargetGroupChannelMap.put(simpleTarget, channel);

                    RequestOptions myOptions = new RequestOptions()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

                    Glide.with(context)
                            .asBitmap()
                            .load(members.get(index).getProfileUrl())
                            .apply(myOptions)
                            .into(simpleTarget);
                }
            } else {
                SparseArray<Bitmap> bitmapSparseArray = mChannelBitmapMap.get(channel.getUrl());
                if (bitmapSparseArray != null) {
                    Integer num = mChannelImageNumMap.get(channel.getUrl());
                    if (num != null && num == bitmapSparseArray.size()) {
                        multiImageView.clear();

                        for (int i = 0; i < bitmapSparseArray.size(); i++) {
                            multiImageView.addImage(bitmapSparseArray.get(i));
                        }
                    }
                }
            }
        }
    }
}
