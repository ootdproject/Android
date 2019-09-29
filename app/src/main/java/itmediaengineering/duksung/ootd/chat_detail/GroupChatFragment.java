package itmediaengineering.duksung.ootd.chat_detail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sendbird.android.AdminMessage;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.Member;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.UserMessage;
import com.sendbird.syncmanager.MessageCollection;
import com.sendbird.syncmanager.MessageEventAction;
import com.sendbird.syncmanager.MessageFilter;
import com.sendbird.syncmanager.SendBirdSyncManager;
import com.sendbird.syncmanager.handler.CompletionHandler;
import com.sendbird.syncmanager.handler.MessageCollectionCreateHandler;
import com.sendbird.syncmanager.handler.MessageCollectionHandler;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import itmediaengineering.duksung.ootd.BaseApplication;
import itmediaengineering.duksung.ootd.MainActivity;
import itmediaengineering.duksung.ootd.R;
import itmediaengineering.duksung.ootd.chat_detail.adapter.GroupChatAdapter;
import itmediaengineering.duksung.ootd.chat_list.view.ChatListActivity;
import itmediaengineering.duksung.ootd.chat_list.view.GroupChannelListFragment;
import itmediaengineering.duksung.ootd.utils.FileUtils;
import itmediaengineering.duksung.ootd.utils.MediaPlayerActivity;
import itmediaengineering.duksung.ootd.utils.PhotoViewerActivity;
import itmediaengineering.duksung.ootd.utils.PreferenceUtils;
import itmediaengineering.duksung.ootd.utils.TextUtils;
import itmediaengineering.duksung.ootd.utils.UrlPreviewInfo;
import itmediaengineering.duksung.ootd.utils.WebUtils;
import itmediaengineering.duksung.ootd.utils.ConnectionManager;

public class GroupChatFragment extends Fragment {

    private static final String LOG_TAG = GroupChatFragment.class.getSimpleName();

    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHAT";
    private static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_CHAT";

    private static final int STATE_NORMAL = 0;
    private static final int STATE_EDIT = 1;

    private static final String STATE_CHANNEL_URL = "STATE_CHANNEL_URL";
    private static final int INTENT_REQUEST_CHOOSE_MEDIA = 301;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;
    static final String EXTRA_CHANNEL_URL = "EXTRA_CHANNEL_URL";

    private InputMethodManager mIMM;
    private HashMap<BaseChannel.SendFileMessageWithProgressHandler, FileMessage> mFileProgressHandlerMap;

    @BindView(R.id.layout_group_chat_root)
    RelativeLayout mRootLayout;
    @BindView(R.id.recycler_group_chat)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout_group_chat_current_event)
    View mCurrentEventLayout;
    @BindView(R.id.text_group_chat_current_event)
    TextView mCurrentEventText;
    @BindView(R.id.edittext_group_chat_message)
    EditText mMessageEditText;
    @BindView(R.id.button_group_chat_send)
    Button mMessageSendButton;
    @BindView(R.id.button_group_chat_upload)
    ImageButton mUploadFileButton;

    private GroupChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;

    private GroupChannel mChannel;
    private String mChannelUrl;

    private boolean mIsTyping;

    private int mCurrentState = STATE_NORMAL;
    private BaseMessage mEditingMessage = null;

    final MessageFilter mMessageFilter = new MessageFilter(BaseChannel.MessageTypeFilter.ALL, null, null);
    private MessageCollection mMessageCollection;

    /**
     * To create an instance of this fragment, a Channel URL should be required.
     */
    public static GroupChatFragment newInstance(@NonNull String channelUrl) {
        GroupChatFragment fragment = new GroupChatFragment();

        Bundle args = new Bundle();
        args.putString(GroupChannelListFragment.EXTRA_GROUP_CHANNEL_URL, channelUrl);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIMM = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mFileProgressHandlerMap = new HashMap<>();

        if (savedInstanceState != null) {
            mChannelUrl = savedInstanceState.getString(STATE_CHANNEL_URL);
        } else {
            mChannelUrl = getArguments().getString(GroupChannelListFragment.EXTRA_GROUP_CHANNEL_URL);
        }

        Log.d(LOG_TAG, mChannelUrl);

        mChatAdapter = new GroupChatAdapter(getActivity());
        setUpChatListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_chat, container, false);
        ButterKnife.bind(this, rootView);
        setRetainInstance(true);

        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mMessageSendButton.setEnabled(true);
                } else {
                    mMessageSendButton.setEnabled(false);
                }
            }
        });

        mMessageSendButton.setEnabled(false);
        mMessageSendButton.setOnClickListener(v -> {
            if (mCurrentState == STATE_EDIT) {
                String userInput = mMessageEditText.getText().toString();
                if (userInput.length() > 0) {
                    if (mEditingMessage != null) {
                        editMessage(mEditingMessage, userInput);
                    }
                }
                setState(STATE_NORMAL, null, -1);
            } else {
                String userInput = mMessageEditText.getText().toString();
                if (userInput.length() > 0) {
                    sendUserMessage(userInput);
                    mMessageEditText.setText("");
                }
            }
        });

        mUploadFileButton.setOnClickListener(v -> requestMedia());

        mIsTyping = false;
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mIsTyping) {
                    setTypingStatus(true);
                }

                if (s.length() == 0) {
                    setTypingStatus(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setUpRecyclerView();
        setHasOptionsMenu(true);

        return rootView;
    }

    private void createMessageCollection(String channelUrl) {
        if (SendBird.getConnectionState() != SendBird.ConnectionState.OPEN) {
            MessageCollection.create(channelUrl, mMessageFilter, Long.MAX_VALUE, new MessageCollectionCreateHandler() {
                @Override
                public void onResult(MessageCollection messageCollection, SendBirdException e) {
                    if (e == null) {
                        if (mMessageCollection == null) {
                            mMessageCollection = messageCollection;
                            mMessageCollection.setCollectionHandler(mMessageCollectionHandler);
                            mChannel = mMessageCollection.getChannel();

                            if (getActivity() == null) {
                                return;
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mChatAdapter.setChannel(mChannel);
                                    updateActionBarTitle();
                                }
                            });

                            mMessageCollection.fetch(MessageCollection.Direction.PREVIOUS, new CompletionHandler() {
                                @Override
                                public void onCompleted(SendBirdException e) {
                                    if (getActivity() == null) {
                                        return;
                                    }

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mChatAdapter.markAllMessagesAsRead();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });
        } else {
            GroupChannel.getChannel(channelUrl, new GroupChannel.GroupChannelGetHandler() {
                @Override
                public void onResult(GroupChannel groupChannel, SendBirdException e) {
                    if (e == null) {
                        mChannel = groupChannel;
                        mChatAdapter.setChannel(mChannel);
                        updateActionBarTitle();

                        if (mMessageCollection == null) {
                            mMessageCollection = new MessageCollection(groupChannel, mMessageFilter, Long.MAX_VALUE);
                            mMessageCollection.setCollectionHandler(mMessageCollectionHandler);

                            mMessageCollection.fetch(MessageCollection.Direction.PREVIOUS, new CompletionHandler() {
                                @Override
                                public void onCompleted(SendBirdException e) {
                                    if (getActivity() == null) {
                                        return;
                                    }

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mChatAdapter.markAllMessagesAsRead();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    private void refresh() {
        if (mChannel != null) {
            mChannel.refresh(new GroupChannel.GroupChannelRefreshHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        // Error!
                        e.printStackTrace();
                        return;
                    }

                    updateActionBarTitle();
                }
            });

            // call fetch(NEXT) to get missed message when app is offline.
            if (mMessageCollection != null) {
                mMessageCollection.fetch(MessageCollection.Direction.NEXT, null);
            }
        } else {
            createMessageCollection(mChannelUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String userId = PreferenceUtils.getUserId();
        SendBirdSyncManager.setup(getActivity().getApplicationContext(), userId, new CompletionHandler() {
            @Override
            public void onCompleted(SendBirdException e) {
                if (getActivity() == null) {
                    return;
                }

                ((BaseApplication)getActivity().getApplication()).setSyncManagerSetup(true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createMessageCollection(mChannelUrl);
                        ConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER_ID, new ConnectionManager.ConnectionManagementHandler() {
                            @Override
                            public void onConnected(boolean reconnect) {
                                refresh();
                            }
                        });
                    }
                });
            }
        });

        mChatAdapter.setContext(getActivity()); // Glide bug fix (java.lang.IllegalArgumentException: You cannot start a load for a destroyed activity)

        // Gets channel from URL user requested

        Log.d(LOG_TAG, mChannelUrl);

        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
            }

            @Override
            public void onReadReceiptUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    mChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTypingStatusUpdated(GroupChannel channel) {
                if (channel.getUrl().equals(mChannelUrl)) {
                    List<Member> typingUsers = channel.getTypingMembers();
                    displayTyping(typingUsers);
                }
            }
        });
    }

    @Override
    public void onPause() {
        setTypingStatus(false);

        ConnectionManager.removeConnectionManagementHandler(CONNECTION_HANDLER_ID);
        SendBird.removeChannelHandler(CHANNEL_HANDLER_ID);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Save messages to cache.
        if (mMessageCollection != null) {
            mMessageCollection.setCollectionHandler(null);
            mMessageCollection.remove();
        }

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_CHANNEL_URL, mChannelUrl);

        super.onSaveInstanceState(outState);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_group_chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_group_channel_invite) {
            Intent intent = new Intent(getActivity(), InviteMemberActivity.class);
            intent.putExtra(EXTRA_CHANNEL_URL, mChannelUrl);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_group_channel_view_members) {
            Intent intent = new Intent(getActivity(), MemberListActivity.class);
            intent.putExtra(EXTRA_CHANNEL_URL, mChannelUrl);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Set this as true to restore background connection management.
        SendBird.setAutoBackgroundDetection(true);

        if (requestCode == INTENT_REQUEST_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            // If user has successfully chosen the image, show a dialog to confirm upload.
            if (data == null) {
                Log.d(LOG_TAG, "data is null!");
                return;
            }

            sendFileWithThumbnail(data.getData());
        }
    }

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChatAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mMessageCollection.fetch(MessageCollection.Direction.NEXT, null);
                    }

                    if (mLayoutManager.findLastVisibleItemPosition() == mChatAdapter.getItemCount() - 1) {
                        mMessageCollection.fetch(MessageCollection.Direction.PREVIOUS, null);
                    }
                }
            }
        });
    }

    private void setUpChatListAdapter() {
        mChatAdapter.setItemClickListener(new GroupChatAdapter.OnItemClickListener() {
            @Override
            public void onUserMessageItemClick(UserMessage message) {
                // Restore failed message and remove the failed message from list.
                if (mChatAdapter.isFailedMessage(message)) {
                    retryFailedMessage(message);
                    return;
                }

                // Message is sending. Do nothing on click event.
                if (mChatAdapter.isTempMessage(message)) {
                    return;
                }


                if (message.getCustomType().equals(GroupChatAdapter.URL_PREVIEW_CUSTOM_TYPE)) {
                    try {
                        UrlPreviewInfo info = new UrlPreviewInfo(message.getData());
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.getUrl()));
                        startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFileMessageItemClick(FileMessage message) {
                // Load media chooser and remove the failed message from list.
                if (mChatAdapter.isFailedMessage(message)) {
                    retryFailedMessage(message);
                    return;
                }

                // Message is sending. Do nothing on click event.
                if (mChatAdapter.isTempMessage(message)) {
                    return;
                }


                onFileMessageClicked(message);
            }
        });

        mChatAdapter.setItemLongClickListener(new GroupChatAdapter.OnItemLongClickListener() {
            @Override
            public void onUserMessageItemLongClick(UserMessage message, int position) {
                showMessageOptionsDialog(message, position);
            }

            @Override
            public void onFileMessageItemLongClick(FileMessage message) {
            }

            @Override
            public void onAdminMessageItemLongClick(AdminMessage message) {
            }
        });
    }

    private void showMessageOptionsDialog(final BaseMessage message, final int position) {
        String[] options = new String[] { "Edit message", "Delete message" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setState(STATE_EDIT, message, position);
                } else if (which == 1) {
                    deleteMessage(message);
                }
            }
        });
        builder.create().show();
    }

    private void setState(int state, BaseMessage editingMessage, final int position) {
        switch (state) {
            case STATE_NORMAL:
                mCurrentState = STATE_NORMAL;
                mEditingMessage = null;

                mUploadFileButton.setVisibility(View.VISIBLE);
                mMessageSendButton.setText("SEND");
                mMessageEditText.setText("");
                break;

            case STATE_EDIT:
                mCurrentState = STATE_EDIT;
                mEditingMessage = editingMessage;

                mUploadFileButton.setVisibility(View.GONE);
                mMessageSendButton.setText("SAVE");
                String messageString = ((UserMessage)editingMessage).getMessage();
                if (messageString == null) {
                    messageString = "";
                }
                mMessageEditText.setText(messageString);
                if (messageString.length() > 0) {
                    mMessageEditText.setSelection(0, messageString.length());
                }

                mMessageEditText.requestFocus();
                mMessageEditText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIMM.showSoftInput(mMessageEditText, 0);

                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRecyclerView.scrollToPosition(position);
                            }
                        }, 500);
                    }
                }, 100);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((ChatListActivity)context).setOnBackPressedListener(() -> {
            if (mCurrentState == STATE_EDIT) {
                GroupChatFragment.this.setState(STATE_NORMAL, null, -1);
                return true;
            }

            mIMM.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
            return false;
        });
    }

    private void retryFailedMessage(final BaseMessage message) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Retry?")
                .setPositiveButton(R.string.resend_message, (dialog, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        if (message instanceof UserMessage) {
                            String userInput = ((UserMessage) message).getMessage();
                            sendUserMessage(userInput);
                        } else if (message instanceof FileMessage) {
                            Uri uri = mChatAdapter.getTempFileMessageUri(message);
                            sendFileWithThumbnail(uri);
                        }

                        mChatAdapter.removeFailedMessage(message);
                    }
                })
                .setNegativeButton(R.string.delete_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            mChatAdapter.removeFailedMessage(message);
                        }
                    }
                }).show();
    }

    /**
     * Display which users are typing.
     * If more than two users are currently typing, this will state that "multiple users" are typing.
     *
     * @param typingUsers The list of currently typing users.
     */
    private void displayTyping(List<Member> typingUsers) {

        if (typingUsers.size() > 0) {
            mCurrentEventLayout.setVisibility(View.VISIBLE);
            String string;

            if (typingUsers.size() == 1) {
                string = typingUsers.get(0).getNickname() + " is typing";
            } else if (typingUsers.size() == 2) {
                string = typingUsers.get(0).getNickname() + " " + typingUsers.get(1).getNickname() + " is typing";
            } else {
                string = "Multiple users are typing";
            }
            mCurrentEventText.setText(string);
        } else {
            mCurrentEventLayout.setVisibility(View.GONE);
        }
    }

    private void requestMedia() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            Intent intent = new Intent();

            // Pick images or videos
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setType("*/*");
                String[] mimeTypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            } else {
                intent.setType("image/* video/*");
            }

            intent.setAction(Intent.ACTION_GET_CONTENT);

            // Always show the chooser (if there are multiple options available)
            startActivityForResult(Intent.createChooser(intent, "Select Media"), INTENT_REQUEST_CHOOSE_MEDIA);

            // Set this as false to maintain connection
            // even when an external Activity is started.
            SendBird.setAutoBackgroundDetection(false);
        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Snackbar.make(mRootLayout, "Storage access permissions are required to upload/download files.",
                    Snackbar.LENGTH_LONG)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            // Permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void onFileMessageClicked(FileMessage message) {
        String type = message.getType().toLowerCase();
        if (type.startsWith("image")) {
            Intent i = new Intent(getActivity(), PhotoViewerActivity.class);
            i.putExtra("url", message.getUrl());
            i.putExtra("type", message.getType());
            startActivity(i);
        } else if (type.startsWith("video")) {
            Intent intent = new Intent(getActivity(), MediaPlayerActivity.class);
            intent.putExtra("url", message.getUrl());
            startActivity(intent);
        } else {
            showDownloadConfirmDialog(message);
        }
    }

    private void showDownloadConfirmDialog(final FileMessage message) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If storage permissions are not granted, request permissions at run-time,
            // as per < API 23 guidelines.
            requestStoragePermissions();
        } else {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Download file?")
                    .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                FileUtils.downloadFile(getActivity(), message.getUrl(), message.getName());
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).show();
        }

    }

    private void updateActionBarTitle() {
        String title = "";

        if(mChannel != null) {
            title = TextUtils.getGroupChannelTitle(mChannel);
        }

        // Set action bar title to name of channel
        if (getActivity() != null) {
            ((ChatListActivity)getActivity()).setActionBarTitle(title);
        }
    }

    private void sendUserMessageWithUrl(final String text, String url) {
        if (mChannel == null) {
            return;
        }

        new WebUtils.UrlPreviewAsyncTask() {
            @Override
            protected void onPostExecute(UrlPreviewInfo info) {
                if (mChannel == null) {
                    return;
                }

                UserMessage tempUserMessage = null;
                BaseChannel.SendUserMessageHandler handler = new BaseChannel.SendUserMessageHandler() {
                    @Override
                    public void onSent(UserMessage userMessage, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            Log.e(LOG_TAG, e.toString());
                            Toast.makeText(
                                    getActivity(),
                                    "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();

                            // remove preview message from MessageCollection
                            if (mMessageCollection != null) {
                                mMessageCollection.deleteMessage(userMessage);
                            }

                            // add failed message to feedAdapter
                            mChatAdapter.addFirst(userMessage);
                            mChatAdapter.markMessageFailed(userMessage.getRequestId());
                            return;
                        }

                        // Update a sent message to RecyclerView
                        mChatAdapter.markMessageSent(userMessage);
                    }
                };

                try {
                    // Sending a message with URL preview information and custom type.
                    String jsonString = info.toJsonString();
                    tempUserMessage = mChannel.sendUserMessage(text, jsonString, GroupChatAdapter.URL_PREVIEW_CUSTOM_TYPE, handler);
                } catch (Exception e) {
                    // Sending a message without URL preview information.
                    tempUserMessage = mChannel.sendUserMessage(text, handler);
                }


                // Display a user message to RecyclerView
                if (mMessageCollection != null) {
                    mMessageCollection.appendMessage(tempUserMessage);
                }
            }
        }.execute(url);
    }

    private void sendUserMessage(String text) {
        if (mChannel == null) {
            return;
        }

        List<String> urls = WebUtils.extractUrls(text);
        if (urls.size() > 0) {
            sendUserMessageWithUrl(text, urls.get(0));
            return;
        }

        final UserMessage tempUserMessage = mChannel.sendUserMessage(text, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Log.e(LOG_TAG, e.toString());
                    Toast.makeText(
                            getActivity(),
                            "Send failed with error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();

                    // remove preview message from MessageCollection
                    if (mMessageCollection != null) {
                        mMessageCollection.deleteMessage(userMessage);
                    }

                    // add failed message to feedAdapter
                    mChatAdapter.addFirst(userMessage);
                    mChatAdapter.markMessageFailed(userMessage.getRequestId());
                    return;
                }

                // append sent message.
                if (mMessageCollection != null) {
                    mMessageCollection.appendMessage(userMessage);
                }
            }
        });

        if (mMessageCollection != null) {
            mMessageCollection.appendMessage(tempUserMessage);
        }
    }

    /**
     * Notify other users whether the current user is typing.
     *
     * @param typing Whether the user is currently typing.
     */
    private void setTypingStatus(boolean typing) {
        if (mChannel == null) {
            return;
        }

        if (typing) {
            mIsTyping = true;
            mChannel.startTyping();
        } else {
            mIsTyping = false;
            mChannel.endTyping();
        }
    }

    /**
     * Sends a File Message containing an image file.
     * Also requests thumbnails to be generated in specified sizes.
     *
     * @param uri The URI of the image, which in this case is received through an Intent request.
     */
    private void sendFileWithThumbnail(Uri uri) {
        if (mChannel == null) {
            return;
        }

        // Specify two dimensions of thumbnails to generate
        List<FileMessage.ThumbnailSize> thumbnailSizes = new ArrayList<>();
        thumbnailSizes.add(new FileMessage.ThumbnailSize(240, 240));
        thumbnailSizes.add(new FileMessage.ThumbnailSize(320, 320));

        Hashtable<String, Object> info = FileUtils.getFileInfo(getActivity(), uri);

        if (info == null) {
            Toast.makeText(getActivity(), "Extracting file information failed.", Toast.LENGTH_LONG).show();
            return;
        }

        final String path = (String) info.get("path");
        final File file = new File(path);
        final String name = file.getName();
        final String mime = (String) info.get("mime");
        final int size = (Integer) info.get("size");

        if (path.equals("")) {
            Toast.makeText(getActivity(), "File must be located in local storage.", Toast.LENGTH_LONG).show();
        } else {
            BaseChannel.SendFileMessageWithProgressHandler progressHandler = new BaseChannel.SendFileMessageWithProgressHandler() {
                @Override
                public void onProgress(int bytesSent, int totalBytesSent, int totalBytesToSend) {
                    FileMessage fileMessage = mFileProgressHandlerMap.get(this);
                    if (fileMessage != null && totalBytesToSend > 0) {
                        int percent = (totalBytesSent * 100) / totalBytesToSend;
                        mChatAdapter.setFileProgressPercent(fileMessage, percent);
                    }
                }

                @Override
                public void onSent(FileMessage fileMessage, SendBirdException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        // remove preview message from MessageCollection
                        if (mMessageCollection != null) {
                            mMessageCollection.deleteMessage(fileMessage);
                        }

                        // add failed message to feedAdapter
                        mChatAdapter.addFirst(fileMessage);
                        mChatAdapter.markMessageFailed(fileMessage.getRequestId());
                        return;
                    }

                    // append sent message.
                    if (mMessageCollection != null) {
                        mMessageCollection.appendMessage(fileMessage);
                    }
                }
            };

            // Send image with thumbnails in the specified dimensions
            FileMessage tempFileMessage = mChannel.sendFileMessage(file, name, mime, size, "", null, thumbnailSizes, progressHandler);

            mFileProgressHandlerMap.put(progressHandler, tempFileMessage);

            mChatAdapter.addTempFileMessageInfo(tempFileMessage, uri);

            if (mMessageCollection != null) {
                mMessageCollection.appendMessage(tempFileMessage);
            }
        }
    }

    private void editMessage(final BaseMessage message, String editedMessage) {
        if (mChannel == null) {
            return;
        }

        mChannel.updateUserMessage(message.getMessageId(), editedMessage, null, null, new BaseChannel.UpdateUserMessageHandler() {
            @Override
            public void onUpdated(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(getActivity(), "Error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mMessageCollection != null) {
                    mMessageCollection.updateMessage(userMessage);
                }
            }
        });
    }

    /**
     * Deletes a message within the channel.
     * Note that users can only delete messages sent by oneself.
     *
     * @param message The message to delete.
     */
    private void deleteMessage(final BaseMessage message) {
        if (mChannel == null) {
            return;
        }

        mChannel.deleteMessage(message, new BaseChannel.DeleteMessageHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(getActivity(), "Error " + e.getCode() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    MessageCollectionHandler mMessageCollectionHandler = new MessageCollectionHandler() {
        @Override
        public void onMessageEvent(MessageCollection collection, final List<BaseMessage> messages, final MessageEventAction action) {
            Log.d("SyncManager", "onMessageEvent: size = " + messages.size() + ", action = " + action);

            if (getActivity() == null) {
                return;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (action) {
                        case INSERT:
                            mChatAdapter.insert(messages);
                            mChatAdapter.markAllMessagesAsRead();
                            break;

                        case REMOVE:
                            mChatAdapter.remove(messages);
                            break;

                        case UPDATE:
                            mChatAdapter.update(messages);
                            break;

                        case CLEAR:
                            mChatAdapter.clear();
                            break;
                    }
                }
            });
        }
    };
}
