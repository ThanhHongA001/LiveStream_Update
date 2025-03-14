package com.example.livestream_update.Ringme.LiveStream.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.adapter.TagOnMediaAdapter;
import com.vtm.ringme.base.BaseFragment;
import com.vtm.ringme.base.CustomLinearLayoutManager;
import com.vtm.ringme.customview.RoundLinearLayout;
import com.vtm.ringme.customview.RoundRelativeLayout;
import com.vtm.ringme.customview.TagGroup;
import com.vtm.ringme.customview.TagTextView;
import com.vtm.ringme.customview.TagsCompletionView;
import com.vtm.ringme.helper.DeepLinkHelper;
import com.vtm.ringme.helper.EventOnMediaHelper;
import com.vtm.ringme.helper.NavigateActivityHelper;
import com.vtm.ringme.helper.NetworkHelper;
import com.vtm.ringme.helper.PopupHelper;
import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.helper.TimeHelper;
import com.vtm.ringme.helper.UrlConfigHelper;
import com.vtm.ringme.imageview.CircleImageView;
import com.vtm.ringme.livestream.LiveStreamActivity;
import com.vtm.ringme.livestream.adapter.MessageLevel2Adapter;
import com.vtm.ringme.livestream.adapter.MessageLiveStreamAdapter;
import com.vtm.ringme.livestream.listener.ClickListener;
import com.vtm.ringme.livestream.listener.MessageActionListener;
import com.vtm.ringme.livestream.listener.MessageListener;
import com.vtm.ringme.livestream.listener.OnSingleClickListener;
import com.vtm.ringme.livestream.listener.SmartTextClickListener;
import com.vtm.ringme.livestream.listener.SocketEvent;
import com.vtm.ringme.livestream.model.ConfigLiveComment;
import com.vtm.ringme.livestream.model.LiveStreamMessage;
import com.vtm.ringme.livestream.network.APICallBack;
import com.vtm.ringme.livestream.network.RetrofitClientInstance;
import com.vtm.ringme.livestream.network.parse.RestListLiveStreamMessage;
import com.vtm.ringme.livestream.socket.SocketManager;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.model.TagRingMe;
import com.vtm.ringme.model.UserInfo;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.utils.InputMethodUtils;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.ToastUtils;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CommentLiveStreamFragment extends BaseFragment implements MessageListener, MessageActionListener
        , TagRingMe.OnClickTag, ClickListener.IconListener, SmartTextClickListener {

    private static final String TAG = CommentLiveStreamFragment.class.getSimpleName();
    private static final int TIME_SHOW_LIKE_VIDEO = 120000;//2 phut
    private static final int TIME_SHOW_FOLLOW_VIDEO = 180000;//3 phut
    private static CommentLiveStreamFragment instance;
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tvAvatar)
    TextView tvAvatar;
    @BindView(R.id.viewAvatar)
    FrameLayout viewAvatar;
    @BindView(R.id.etComment)
    TagsCompletionView etComment;
    @BindView(R.id.ivLike)
    ImageView ivLike;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.btnSend)
    ImageView btnSend;
    @BindView(R.id.layout_comment)
    View layoutComment;
    @BindView(R.id.viewComment)
    RoundRelativeLayout viewComment;
    @BindView(R.id.viewUnreadMsg)
    RoundLinearLayout viewUnreadMsg;
    Unbinder unbinder;
    @BindView(R.id.spaceFake)
    RelativeLayout spaceFake;
    @BindView(R.id.rootView)
    View rootView;
    @BindView(R.id.layout_reply_comment)
    View layoutReplyComment;
    @BindView(R.id.btnClose)
    ImageView btnClose;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.viewAvatarComment)
    View viewAvatarComment;
    @BindView(R.id.ivAvatarComment)
    CircleImageView ivAvatarComment;
    @BindView(R.id.tvAvatarComment)
    TextView tvAvatarComment;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvNumberLike)
    TextView tvNumberLike;
    @BindView(R.id.ivLikeCmt)
    ImageView ivLikeCmt;
    @BindView(R.id.tvContent)
    TagTextView tvContent;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvReply)
    TextView tvReply;
    @BindView(R.id.tvDot)
    TextView tvDot;
    @BindView(R.id.rvCommentLevel2)
    RecyclerView rvCommentLevel2;
    @BindView(R.id.layout_warning_spam)
    View viewWarningSpam;
    @BindView(R.id.tv_warning_spam)
    TextView tvMsgSpam;

    private AppCompatActivity activity;
    private ApplicationController app;
    private Video currentVideo;
    private LiveStreamMessage messageReply = null;
    private int currentPosMessageReply = 0;
    private ArrayList<LiveStreamMessage> messages = new ArrayList<>();
    private ArrayList<LiveStreamMessage> messagesLevel2 = new ArrayList<>();
    private MessageLiveStreamAdapter adapter;
    private MessageLevel2Adapter adapterLevel2;
    private LinearLayoutManager layoutManager;
    private boolean isShowViewUnread = false;
    private Handler handler;
    private ArrayList<String> friendList = new ArrayList<>();
    private ArrayList<LiveStreamMessage> comments = new ArrayList<>();
    private TagOnMediaAdapter adapterUserTag;
    private EventOnMediaHelper eventOnMediaHelper;
    private LiveStreamMessage lastComment;
    private boolean enableClickTag = true;
    private Runnable showKeyboardRunnable = new Runnable() {
        @Override
        public void run() {
            if (etComment != null) {

                etComment.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                etComment.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                /*
                 * hiển thị keyboard
                 */
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);

            }
        }
    };
    private ConfigLiveComment configLiveComment;
    private boolean isBadWord = false;

    public static CommentLiveStreamFragment newInstance(Video video, ConfigLiveComment config) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.TabVideo.VIDEO, video);
        bundle.putSerializable("CONFIG_LIVE", config);
        CommentLiveStreamFragment fragment = new CommentLiveStreamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CommentLiveStreamFragment self() {
        return instance;
    }

    public synchronized boolean checkSendComment(EditText editText, String comment) {
        if (app == null || activity == null || activity.isFinishing())
            return false;
        if (((LiveStreamActivity) activity).isBlockSpam()) return false;
        String content;
        if (editText != null) content = editText.getText().toString();
        else content = comment;
        if (TextUtils.isEmpty(content)) return false;

        if (!NetworkHelper.isConnectInternet(activity)) {
            ToastUtils.showToast(activity.getApplicationContext(), getString(R.string.no_connectivity_check_again));
            return false;
        }
        String tmp = content.replaceAll(" ", "");
        if (TextUtils.isEmpty(tmp)) {
            ToastUtils.showToast(activity.getApplicationContext(), getString(R.string.msg_warning_comment));
            return false;
        }

        //check spam
        long currentTime = System.currentTimeMillis();
        if (lastComment != null && content.equals(lastComment.getContent()) && (currentTime - lastComment.getTimeStamp() < 5000L)) {
            ToastUtils.showToast(activity.getApplicationContext(), getString(R.string.msg_warning_duplicate_comment));
            return false;
        }
        if (comments != null && comments.size() >= 10) {
            LiveStreamMessage topMsg = comments.get(0);
            if (topMsg != null && (Math.abs(currentTime - topMsg.getTimeStamp()) <= 30000L)) {
                // show view spam
                hideKeyboard();
                if (viewComment != null) viewComment.setEnabled(false);
                viewWarningSpam.setVisibility(View.VISIBLE);
                tvMsgSpam.setText(getString(R.string.msg_warning_spam, 5));
                CountDownTimer downTimer = new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.i(TAG, "CountDownTimer msg_warning_spam: " + millisUntilFinished);
                        if (tvMsgSpam != null)
                            tvMsgSpam.setText(getString(R.string.msg_warning_spam, (int) (millisUntilFinished / 1000) + 1));
                    }

                    @Override
                    public void onFinish() {
                        if (viewComment != null) viewComment.setEnabled(true);
                        if (tvMsgSpam != null)
                            tvMsgSpam.setText(getString(R.string.msg_warning_spam, 1));
                        if (viewWarningSpam != null) viewWarningSpam.setVisibility(View.GONE);
                        if (comments != null) comments.clear();
                        if (activity instanceof LiveStreamActivity)
                            ((LiveStreamActivity) activity).setBlockSpam(false);
                    }
                };
                ((LiveStreamActivity) activity).setBlockSpam(true);
                downTimer.start();
                return false;
            } else {
                comments.remove(0);
            }
        }

        if (editText != null) {
            //check từ xấu
            isBadWord = TextHelper.getInstant().checkBadWordToSendComment(editText);
            if (isBadWord) {
                if (btnSend != null) btnSend.setEnabled(false);
                ToastUtils.showToast(activity.getApplicationContext(), activity.getString(R.string.msg_warning_bad_word));
                return false;
            }
        }
        return true;
    }

    @Override
    public String getName() {
        return "CommentLiveStreamFragment";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) getActivity();
        app = (ApplicationController) activity.getApplication();
    }

    @Override
    public int getResIdView() {
        return R.layout.rm_fragment_comment_livestream;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Serializable serializable;
            serializable = bundle.getSerializable(Constants.TabVideo.VIDEO);
            if (serializable instanceof Video) currentVideo = (Video) serializable;
            serializable = bundle.getSerializable("CONFIG_LIVE");
            if (serializable instanceof ConfigLiveComment)
                configLiveComment = (ConfigLiveComment) serializable;
            if (currentVideo != null) currentVideo.setPlaying(true);
        }

        intAdapter();
        initSocket();
        initData();
        instance = this;
        eventOnMediaHelper = new EventOnMediaHelper(activity);
        return view;
    }

    private void initData() {
        handler = new Handler();
        handler.postDelayed(() -> {
            if (currentVideo != null) loadComment(currentVideo.getId());
        }, 500);

        if (currentVideo.isLike())
            ivLike.setImageResource(R.drawable.rm_ic_onmedia_like_press);
        else
            ivLike.setImageResource(R.drawable.rm_ic_onmedia_like);

        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isBadWord) {
                    if (btnSend != null) btnSend.setEnabled(true);
                    if (etComment != null) {
                        etComment.setTextColor(Color.WHITE);
                    }
                    isBadWord = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (TextUtils.isEmpty(str)) {
                    if (messageReply != null)
                        btnSend.setColorFilter(ContextCompat.getColor(activity, R.color.gray));
                    else
                        hideButtonSend();
                } else {
                    showButtonSend();
                    btnSend.setColorFilter(ContextCompat.getColor(activity, R.color.bg_kakoak));
                }
            }
        });
    }

    private void initSocket() {
        if (configLiveComment != null && currentVideo != null)
            SocketManager.getInstance().connectWebSocket(configLiveComment.getDomainWS(), currentVideo.getId());
    }

    private void intAdapter() {
        layoutManager = new CustomLinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvMessage.setLayoutManager(layoutManager);
        adapter = new MessageLiveStreamAdapter(activity, messages, currentVideo, this, this);
        adapter.setSmartTextClickListener(this);
        rvMessage.setAdapter(adapter);

        ArrayList<PhoneNumber> listPhone = app.getListNumberUseRingMe();
        if (listPhone == null) listPhone = new ArrayList<>();
        adapterUserTag = new TagOnMediaAdapter(app, listPhone, etComment);
        etComment.setAdapter(adapterUserTag);
        etComment.setThreshold(0);
        adapterUserTag.setListener(count -> {
            Log.i(TAG, "onChangeItem: " + count);
            if (count > 2) {
                int height = activity.getResources().getDimensionPixelOffset(R.dimen.max_height_drop_down_tag);
                etComment.setDropDownHeight(height);
            } else
                etComment.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        });

        rvMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                int visibleItemCount = recyclerView.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
//                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + 1)) {
//                    viewUnreadMsg.setVisibility(View.GONE);
//                }

                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisible > 0 && lastVisible < totalItemCount - 2)
                    isShowViewUnread = true;
                else {
                    viewUnreadMsg.setVisibility(View.GONE);
                    isShowViewUnread = false;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        SocketManager.getInstance().disconnect();
        LiveStreamMessage message = new LiveStreamMessage();
        message.setType(LiveStreamMessage.TYPE_UNSUBSCRIBE);
        message.setRoomId(currentVideo.getId());
        ((LiveStreamActivity) activity).postMessage(message);
        friendList.clear();
        if (unbinder != null) unbinder.unbind();
        instance = null;
        super.onDestroyView();
    }

    @OnClick({R.id.ivLike, R.id.ivShare, R.id.spaceFake, R.id.btnSend, R.id.viewUnreadMsg, R.id.viewAvatar, R.id.viewAvatarComment, R.id.btnClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivLike:

                ((LiveStreamActivity) activity).likeVideo();
                break;
            case R.id.ivShare:
                ((LiveStreamActivity) activity).shareVideo(false);
                break;
            case R.id.btnSend:
                if (checkSendComment(etComment, "")) {
                    String message = TextHelper.trimTextOnMedia(etComment.getTextTag());
                    if (messageReply != null) {
                        messageReply.setMsisdn(app.getJidNumber());
                        messageReply.setNameSender(app.getUserName());
                        messageReply.setLevelMessage(LiveStreamMessage.LEVEL_MESSAGE_2);
                        messageReply.setTimeStamp(System.currentTimeMillis());

                        message = TextHelper.getInstant().filterSensitiveWords(message);
                        messageReply.setContent(message);
                        onSendMessage(messageReply);
                    } else {
                        LiveStreamMessage liveStreamMessage = LiveStreamMessage.createMyLiveStreamMessage(LiveStreamMessage.TYPE_NORMAL, message, currentVideo.getId(), null, app);
                        comments.add(liveStreamMessage);
                        onSendMessage(liveStreamMessage);
                        lastComment = liveStreamMessage;
                    }
                    resetParams();
                }
                break;
            case R.id.spaceFake:
                onClickShowKeyboard();
                break;
            case R.id.viewAvatar:

//                NavigateActivityHelper.navigateToMyProfile(activity);
                break;
            case R.id.viewUnreadMsg:
                viewUnreadMsg.setVisibility(View.GONE);
                if (rvMessage != null)
                    rvMessage.scrollToPosition(adapter.getItemCount() - 1);
                break;
            case R.id.btnClose:
                resetParamsReply();
                break;
            case R.id.viewAvatarComment:
                if (messageReply != null)
                    onClickLikeMessage(messageReply, currentPosMessageReply);
                break;
        }
    }

    private void resetParams() {
        hideKeyboard();
        hideButtonSend();
        if (etComment != null) {
            etComment.resetObject();
        }
    }

    public void resetParamsReply() {
        hideKeyboard();
        hideButtonSend();
        messageReply = null;
        currentPosMessageReply = 0;
        layoutReplyComment.setVisibility(View.GONE);
        rvMessage.setVisibility(View.VISIBLE);
        etComment.setHint(activity.getString(R.string.onmedia_hint_enter_comment));
    }

    private void showButtonSend() {
        ivShare.setVisibility(View.GONE);
        ivLike.setVisibility(View.GONE);
        btnSend.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutComment.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF, R.id.btnSend);
        layoutComment.setLayoutParams(params);
    }

    private void hideButtonSend() {
        ivShare.setVisibility(View.VISIBLE);
        ivLike.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutComment.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF, R.id.ivLike);
        layoutComment.setLayoutParams(params);
    }

    private void onScrollToBottom() {
//        int totalItemCount = layoutManager.getItemCount();
//        int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
        if (viewUnreadMsg != null) {
            if (isShowViewUnread) {
                //Show xem binh luan moi nhat
                if (layoutReplyComment != null && layoutReplyComment.getVisibility() == View.GONE)
                    viewUnreadMsg.setVisibility(View.VISIBLE);
            } else {
                viewUnreadMsg.setVisibility(View.GONE);
                if (rvMessage != null)
                    rvMessage.scrollToPosition(adapter.getItemCount() - 1);
            }
        }
    }

    public int getHeightViewComment() {
        return viewComment.getHeight();
    }

    public void updateEdittextComment(String text) {
        etComment.setText(text);
        if (!TextUtils.isEmpty(text))
            etComment.setSelection(text.length() - 1);
        else
            etComment.setSelection(0);
    }

    public void updateStatusLikeUi() {
        if (currentVideo.isLike())
            ivLike.setImageResource(R.drawable.rm_ic_onmedia_like_press);
        else
            ivLike.setImageResource(R.drawable.rm_ic_onmedia_like);

        //Update like o message
        for (int i = 0; i < messages.size(); i++) {
            LiveStreamMessage item = messages.get(i);
            if (item.getType() == LiveStreamMessage.TYPE_LIKE_VIDEO) {
//                item.setLike(currentVideo.isLike() ? 1 : 0);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    public void updateStatusFollowUi() {
        //Update like o message
        for (int i = 0; i < messages.size(); i++) {
            LiveStreamMessage item = messages.get(i);
            if (item.getType() == LiveStreamMessage.TYPE_FOLLOW_CHANNEL) {
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onNewMessage(LiveStreamMessage message) {
        //Tin nhan moi
        messages.add(message);
        adapter.notifyItemInserted(messages.size() - 1);
        onScrollToBottom();
    }

    @Override
    public void onGetListMessage(ArrayList<LiveStreamMessage> messages) {
        int lastItem = this.messages.size() - 1;
        if (lastItem <= 0) lastItem = 0;
        this.messages.addAll(messages);
        adapter.notifyItemRangeInserted(lastItem, messages.size());
        onScrollToBottom();
    }

    @Override
    public void onClickUser(LiveStreamMessage message, int pos) {


        String jidNumber = app.getJidNumber();
        if (message.getMsisdn().equals(jidNumber)) {

        } else {
            PhoneNumber phoneNumber = app.getPhoneNumberFromNumber(message
                    .getMsisdn());
            if (phoneNumber != null && phoneNumber.getId() != null) {
                navigateToContactDetail(phoneNumber);
            } else {
                navigateToStrangerDetail(message.getMsisdn(), message.getNameSender(), message.getLastAvatar(), "");
            }
        }
    }

    private void navigateToContactDetail(PhoneNumber phoneNumber) {

    }

    private void navigateToStrangerDetail(String friendJid, String friendName,
                                          String friendChangeAvatar, String status) {

    }

    @Override
    public void onQuickSendText(String content) {
//        LiveStreamMessage liveStreamMessage = LiveStreamMessage.createMyLiveStreamMessage(LiveStreamMessage.TYPE_NORMAL, content, currentVideo.getId(), null, app);
//        messages.add(liveStreamMessage);
//        adapter.notifyItemInserted(messages.size() - 1);
//        onScrollToBottom();
        if (checkSendComment(null, content)) {
            LiveStreamMessage liveStreamMessage = LiveStreamMessage.createMyLiveStreamMessage(LiveStreamMessage.TYPE_NORMAL, content, currentVideo.getId(), null, app);
            comments.add(liveStreamMessage);
            lastComment = liveStreamMessage;
            ((LiveStreamActivity) activity).postMessage(liveStreamMessage);
        }
    }

    @Override
    public void onClickShowKeyboard() {


        if (((LiveStreamActivity) activity).isFullScreen()) {
            InputMethodUtils.showSoftKeyboard(activity, etComment);
            etComment.requestFocus();
        } else {
            showKeyboard();
        }

        if (messageReply != null) {
            etComment.setHint(activity.getString(R.string.comment_level2_hint));
        } else {
            etComment.setHint(activity.getString(R.string.onmedia_hint_enter_comment));
        }
    }

    @Override
    public void onReplyMessage(LiveStreamMessage message, int pos) {
        Log.i(TAG, "onReplyMessage");

        if (message.getLevelMessage() == LiveStreamMessage.LEVEL_MESSAGE_2) {
            for (LiveStreamMessage item : messages) {
                if (item.getId() != null && item.getId().equals(message.getQuotedMessage().getId())) {
                    messageReply = LiveStreamMessage.clone(item);
                    break;
                }
            }
            if (messageReply == null)
                messageReply = LiveStreamMessage.clone(message.getQuotedMessage());
        } else {
            messageReply = LiveStreamMessage.clone(message);
        }

        currentPosMessageReply = pos;
        loadDataReplyComment(messageReply);

//        Show keyboard
        onClickShowKeyboard();

        if (viewUnreadMsg.getVisibility() == View.VISIBLE) {
            viewUnreadMsg.setVisibility(View.GONE);
            if (rvMessage != null)
                rvMessage.scrollToPosition(adapter.getItemCount() - 1);
        }

//        int height = ((LiveStreamActivity) activity).getHeightBoxComment();
//        BottomSheetReplyComment.showContextMenu(activity, message, height, pos, this);
    }

    @Override
    public void onReplyMessageLevel2(LiveStreamMessage message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserInfo userInfo = new UserInfo();
                userInfo.setMsisdn(message.getMsisdn());
                userInfo.setName(message.getNameSender());
                PhoneNumber phoneNumber = app.getPhoneNumberFromNumber(userInfo.getMsisdn());
                if (phoneNumber == null) {
                    phoneNumber = new PhoneNumber();
                    phoneNumber.setJidNumber(userInfo.getMsisdn());
                    phoneNumber.setName(userInfo.getName());
                    phoneNumber.setNameUnicode(userInfo.getName());
                    phoneNumber.setNickName(userInfo.getName());
                }
                etComment.resetObject();
                etComment.setSelectedObject(phoneNumber);
                etComment.addObject(phoneNumber);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodUtils.showSoftKeyboard(activity, etComment);
                int selection = etComment.getText().toString().length();
                Log.i(TAG, "selection: " + selection);
                etComment.setSelection(selection);
                etComment.requestFocus();
            }
        }, 100);
    }

    @Override
    public void onInviteFriend() {
        Log.i(TAG, "onInviteFriend");


        ((LiveStreamActivity) activity).shareVideo(false);
    }

    @Override
    public void onShareVideo(boolean shareSocial) {
        Log.i(TAG, "onShareVideo: " + shareSocial);


        ((LiveStreamActivity) activity).shareVideo(shareSocial);
    }

    @Override
    public void onFollowChannel(LiveStreamMessage message, TagGroup tagGroup) {
        Log.i(TAG, "onFollowChannel");


        if (currentVideo != null && currentVideo.getChannel() != null && ((LiveStreamActivity) activity).getCurrentVideo() != null && ((LiveStreamActivity) activity).getCurrentVideo().getChannel() != null) {
            currentVideo.getChannel().setFollow(!currentVideo.getChannel().isFollow());
            ((LiveStreamActivity) activity).getCurrentVideo().getChannel().setFollow(currentVideo.getChannel().isFollow());
            if (currentVideo.getChannel().isFollow()) {
                String s1 = activity.getString(R.string.onmedia_unfollow);
                tagGroup.setTags(s1);
            } else {
                String s1 = activity.getString(R.string.onmedia_follow);
                tagGroup.setTags(s1);
            }
        }

        if (currentVideo != null && currentVideo.getChannel() != null)
            ((LiveStreamActivity) activity).subscribeChannel(currentVideo.getChannel());
    }

    @Override
    public void onLikeVideo(TagGroup tagGroup) {

        ((LiveStreamActivity) activity).likeVideo();
    }

    @Override
    public void onSendMessage(LiveStreamMessage message) {
        ((LiveStreamActivity) activity).postMessage(message);

        //Fake hien len luon
//        messages.add(message);
//        adapter.notifyItemInserted(messages.size() - 1);
//        onScrollToBottom();
    }

    @Override
    public void onClickLikeMessage(LiveStreamMessage message, int pos) {

        int type;
        if (message.isLike())
            type = LiveStreamMessage.TYPE_LIKE_COMMENT;
        else
            type = LiveStreamMessage.TYPE_UNLIKE_COMMENT;

        LiveStreamMessage liveStreamMessage = LiveStreamMessage.createMyLiveStreamMessage(type, message.getContent(), message.getRoomId(), null, app);
        liveStreamMessage.setId(message.getId());
        liveStreamMessage.setRowId(message.getRowId());
        ((LiveStreamActivity) activity).postMessage(liveStreamMessage);

        //Neu like o man hinh comment cap 2 thi cap nhat o ngoai
//        adapter.notifyItemChanged(pos);
        for (int i = 0; i < messages.size(); i++) {
            LiveStreamMessage item = messages.get(i);
            if (item.getId() != null && message.getId() != null && item.getId().equals(message.getId())) {
                item.setLike(message.isLike() ? 1 : 0);
                item.setCountLike(message.getCountLike());
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private String lastCmtId;
    private String lastRowId;

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LiveStreamMessage message) {
        if (message != null) {
            if (message.getType() == LiveStreamMessage.TYPE_NORMAL) {
                if (Utilities.notEmpty(lastCmtId) && Utilities.notEmpty(lastRowId) && lastCmtId.equals(message.getId()) && lastRowId.equals(message.getRowId())) {
                    Log.e(TAG, "Trùng comment: " + message);
                } else {
                    lastCmtId = message.getId();
                    lastRowId = message.getRowId();
                    messages.add(message);
                    adapter.notifyItemInserted(messages.size() - 1);
                    onScrollToBottom();
                    //Cap nhat giao dien comment cap 2 neu dang comment cap 2
                    if (messageReply != null && message.getLevelMessage() == LiveStreamMessage.LEVEL_MESSAGE_2) {
                        messagesLevel2.add(LiveStreamMessage.clone(message));
                        adapterLevel2.notifyItemInserted(messagesLevel2.size() - 1);
                    }
                }
            } else if (message.getType() == LiveStreamMessage.TYPE_SAY_HI) {
                //Ban be thi moi hien
                String msisdn = app.getJidNumber();
                String phoneNumber = message.getMsisdn();
                if (Utilities.notEmpty(phoneNumber) && !phoneNumber.equals(msisdn) /*&& !friendList.contains(phoneNumber)*/) {
                    boolean check = true;
                    if (Utilities.notEmpty(messages)) {
                        LiveStreamMessage lastMsg = messages.get(messages.size() - 1);
                        if (lastMsg != null) {
                            if (lastMsg.getType() == LiveStreamMessage.TYPE_FRIEND_WATCH && phoneNumber.equals(lastMsg.getMsisdn())) {
                                check = false;
                            }
                        }
                    }
                    if (check) {
                        LiveStreamMessage liveStreamMessage = LiveStreamMessage.createMyLiveStreamMessage(LiveStreamMessage.TYPE_FRIEND_WATCH, "", currentVideo.getId(), null, app);
                        liveStreamMessage.setMsisdn(phoneNumber);
                        liveStreamMessage.setNameSender(message.getNameSender());
                        messages.add(liveStreamMessage);
                        adapter.notifyItemInserted(messages.size() - 1);
                        onScrollToBottom();
                        friendList.add(message.getMsisdn());
                    }
                }
            } else if (message.getType() == LiveStreamMessage.TYPE_LIKE_COMMENT) {
                String msisdn = app.getJidNumber();
                if (!message.getMsisdn().equals(msisdn) && !TextUtils.isEmpty(message.getId())) {
                    for (int i = 0; i < messages.size(); i++) {
                        LiveStreamMessage item = messages.get(i);
                        if (message.getId().equals(item.getId())) {
                            item.setCountLike(item.getCountLike() + 1);
                            adapter.notifyItemChanged(i);

                            //Set count like cho comment cap 2
                            if (messageReply != null && messageReply.getId().equals(message.getId())) {
                                messageReply.setCountLike(messageReply.getCountLike() + 1);
                                if (messageReply.getCountLike() > 0)
                                    tvNumberLike.setText(messageReply.getCountLike() + "");
                                else
                                    tvNumberLike.setText("");
                            }
                            break;
                        }
                    }

                    //Update man hinh comment cap 2
                    for (int i = 0; i < messagesLevel2.size(); i++) {
                        LiveStreamMessage item = messagesLevel2.get(i);
                        if (item.getId() != null && message.getId() != null && item.getId().equals(message.getId())) {
                            item.setCountLike(item.getCountLike() + 1);
                            adapterLevel2.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            } else if (message.getType() == LiveStreamMessage.TYPE_UNLIKE_COMMENT) {
                String msisdn = app.getJidNumber();
                if (!message.getMsisdn().equals(msisdn) && !TextUtils.isEmpty(message.getId())) {
                    for (int i = 0; i < messages.size(); i++) {
                        LiveStreamMessage item = messages.get(i);
                        if (message.getId().equals(item.getId())) {
                            item.setCountLike(item.getCountLike() - 1);
                            if (item.getCountLike() < 0)
                                item.setCountLike(0);
                            adapter.notifyItemChanged(i);

                            //Set count like cho comment cap 2
                            if (messageReply != null && messageReply.getId().equals(message.getId())) {
                                messageReply.setCountLike(messageReply.getCountLike() - 1);
                                if (messageReply.getCountLike() > 0)
                                    tvNumberLike.setText(messageReply.getCountLike() + "");
                                else
                                    tvNumberLike.setText("");
                            }
                            break;
                        }
                    }

                    //Update man hinh comment cap 2
                    for (int i = 0; i < messagesLevel2.size(); i++) {
                        LiveStreamMessage item = messagesLevel2.get(i);
                        if (item.getId() != null && message.getId() != null && item.getId().equals(message.getId())) {
                            item.setCountLike(item.getCountLike() - 1);
                            if (item.getCountLike() < 0)
                                item.setCountLike(0);
                            adapterLevel2.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            } else if (message.getType() == LiveStreamMessage.TYPE_COUNT_LIVE) {
                ((LiveStreamActivity) activity).setCountLive(message.getCountLike());
            }
            EventBus.getDefault().removeStickyEvent(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SocketEvent message) {
        if (message != null) {
            if (message.getType() == SocketEvent.TYPE_OPEN)
            //Danh dau dang xem
            {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Phai login moi gui
                        LiveStreamMessage liveStreamMessage = LiveStreamMessage.createMyLiveStreamMessage(LiveStreamMessage.TYPE_SAY_HI, "", currentVideo.getId(), null, app);
                        ((LiveStreamActivity) activity).postMessage(liveStreamMessage);
                    }
                }, 3000);
            }
            EventBus.getDefault().removeStickyEvent(message);
        }
    }

    private void loadComment(String roomId) {
        if (app == null || activity == null || activity.isFinishing() || configLiveComment == null || currentVideo == null)
            return;
        ReengAccount reengAccount = app.getCurrentAccount();
        RetrofitClientInstance retrofitClientInstance = new RetrofitClientInstance(configLiveComment);
        retrofitClientInstance.getMessages(roomId, reengAccount.getJidNumber(), new APICallBack<RestListLiveStreamMessage>() {
            @Override
            public void onResponse(retrofit2.Response response) {
                if (response != null && response.body() != null) {
                    RestListLiveStreamMessage restListLiveStreamMessage = (RestListLiveStreamMessage) response.body();
                    onGetListMessage(restListLiveStreamMessage.getData());

                    //Bat buoc phai login roi
                    //Show ban tin Hi
                    LiveStreamMessage liveStreamMessage = LiveStreamMessage.createMyLiveStreamMessage(LiveStreamMessage.TYPE_SAY_HI, "", currentVideo.getId(), null, app);
                    messages.add(liveStreamMessage);
                    adapter.notifyItemInserted(messages.size() - 1);
                    onScrollToBottom();

                    //Show line like video
                    handler.postDelayed(() -> {
                        if (!((LiveStreamActivity) activity).isLikedVideo() && !currentVideo.isLike()) {
                            LiveStreamMessage liveStreamMessage1 = LiveStreamMessage.createMyLiveStreamMessage(LiveStreamMessage.TYPE_LIKE_VIDEO, "", currentVideo.getId(), null, app);
                            liveStreamMessage1.setCurrentVideo(currentVideo);
                            messages.add(liveStreamMessage1);
                            adapter.notifyItemInserted(messages.size() - 1);
                            onScrollToBottom();
                        }
                    }, TIME_SHOW_LIKE_VIDEO);

                    //Show line follow video
                    handler.postDelayed(() -> {
                        if (!((LiveStreamActivity) activity).isFollowedVideo() && currentVideo.getChannel() != null && !currentVideo.getChannel().isFollow()) {
                            LiveStreamMessage liveStreamMessage12 = LiveStreamMessage.createMyLiveStreamMessage(LiveStreamMessage.TYPE_FOLLOW_CHANNEL, "", currentVideo.getId(), null, app);
                            liveStreamMessage12.setCurrentVideo(currentVideo);
                            messages.add(liveStreamMessage12);
                            adapter.notifyItemInserted(messages.size() - 1);
                            onScrollToBottom();
                        }
                    }, TIME_SHOW_FOLLOW_VIDEO);
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    private void showKeyboard() {
        if (etComment != null && showKeyboardRunnable != null) {
            etComment.postDelayed(showKeyboardRunnable, 300L);
        }
    }

    private void hideKeyboard() {
        InputMethodUtils.hideSoftKeyboard(etComment, activity);
    }

    public View getRootView() {
        return rootView;
    }

    public boolean isShowBoxReplyComment() {
        return messageReply != null;
    }

    /*Xu ly comment cap 2*/
    public void loadDataReplyComment(LiveStreamMessage message) {
        layoutReplyComment.setVisibility(View.VISIBLE);
        rvMessage.setVisibility(View.GONE);

        String content = message.getContent();
        if (TextUtils.isEmpty(content)) {
//            content = mRes.getString(R.string.connections_share_uppercase);
            tvContent.setText(content);
        } else {
            if (message.getListTag() == null || message.getListTag().isEmpty()) {
                tvContent.setEmoticon(app, content, content.hashCode(), content);
            } else {
                tvContent.setEmoticonWithTag(app, content, content.hashCode(), content,
                        message.getListTag(), this);
            }
        }

        long deltaTimeServer = message.getTimeServer() - System.currentTimeMillis();
        tvTime.setText(TimeHelper.caculateTimeFeed(tvTime.getContext(), message.getTimeStamp(), deltaTimeServer));
        int sizeAvatar = (int) activity.getResources().getDimension(R.dimen.avatar_small_size);

        ReengAccount account = app.getCurrentAccount();
        if (message.getMsisdn() != null) {
            if (message.getMsisdn().equals(account.getJidNumber())) {
                tvName.setText(account.getName());

                tvTitle.setText(activity.getString(R.string.reply_comment_title, account.getName()));
            } else {
                if (!message.isGetContactPhoneDone()) {
                    message.setGetContactPhoneDone(true);
                }

                PhoneNumber phoneNumber = message.getPhoneNumber();
                if (phoneNumber == null) {
                    if (TextUtils.isEmpty(message.getNameSender())) {
                        tvName.setText(Utilities.hidenPhoneNumber(message.getMsisdn()));
                        tvTitle.setText(activity.getString(R.string.reply_comment_title, Utilities.hidenPhoneNumber(message.getMsisdn())));
                    } else {
                        tvName.setText(message.getNameSender());
                        tvTitle.setText(activity.getString(R.string.reply_comment_title, message.getNameSender()));
                    }
                } else {
                    tvName.setText(phoneNumber.getName());

                    tvTitle.setText(activity.getString(R.string.reply_comment_title, phoneNumber.getName()));
                }
            }
        }

        ivLike.setVisibility(View.GONE);
        ivShare.setVisibility(View.GONE);
        btnSend.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutComment.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF, R.id.btnSend);
        layoutComment.setLayoutParams(params);

        if (message.getCountLike() > 0)
            tvNumberLike.setText(message.getCountLike() + "");
        else
            tvNumberLike.setText("");
        if (message.isLike())
            ivLikeCmt.setImageResource(R.drawable.rm_ic_onmedia_like_press);
        else
            ivLikeCmt.setImageResource(R.drawable.rm_ic_onmedia_like);
        ivLikeCmt.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                message.setLike(message.isLike() ? 0 : 1);
                if (message.isLike()) {
                    ivLikeCmt.setImageResource(R.drawable.rm_ic_onmedia_like_press);
                    message.setCountLike(message.getCountLike() + 1);
                } else {
                    ivLikeCmt.setImageResource(R.drawable.rm_ic_onmedia_like);
                    message.setCountLike(message.getCountLike() - 1);
                    if (message.getCountLike() < 0)
                        message.setCountLike(0);
                }
                if (message.getCountLike() > 0) {
                    tvNumberLike.setText(message.getCountLike() + "");
                } else
                    tvNumberLike.setText("");

                onClickLikeMessage(message, currentPosMessageReply);
            }
        });

        tvReply.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                onReplyMessageLevel2(messageReply);
            }
        });

        loadCommentLevel2(message.getId());
    }

    private void loadCommentLevel2(String id) {
        rvCommentLevel2.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new CustomLinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvCommentLevel2.setLayoutManager(layoutManager);
        adapterLevel2 = new MessageLevel2Adapter(activity, messagesLevel2, this, this);
        rvCommentLevel2.setAdapter(adapterLevel2);
        if (app == null || activity == null || activity.isFinishing() || configLiveComment == null)
            return;
        ReengAccount reengAccount = app.getCurrentAccount();
        RetrofitClientInstance retrofitClientInstance = new RetrofitClientInstance(configLiveComment);
        retrofitClientInstance.getMessagesLevel2(id, currentVideo.getId(), reengAccount.getJidNumber(), new APICallBack<RestListLiveStreamMessage>() {
            @Override
            public void onResponse(retrofit2.Response response) {
                if (response != null && response.body() != null) {
                    RestListLiveStreamMessage restListLiveStreamMessage = (RestListLiveStreamMessage) response.body();
                    messagesLevel2.clear();
                    messagesLevel2.addAll(restListLiveStreamMessage.getData());
                    adapterLevel2.notifyDataSetChanged();

                    int totalLike = restListLiveStreamMessage.getTotalLike();
                    messageReply.setCountLike(totalLike);
                    if (messageReply.getCountLike() > 0) {
                        tvNumberLike.setText(messageReply.getCountLike() + "");
                    } else
                        tvNumberLike.setText("");

                    if (rvCommentLevel2 != null) {
                        rvCommentLevel2.scrollToPosition(adapterLevel2.getItemCount() - 1);
                        rvCommentLevel2.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }

    public void onChangeOrientation(boolean isLandscape, boolean isVerticalVideo, int widthScreen, int heightScreen) {
        if (isVerticalVideo) {
            rootView.setBackgroundResource(R.drawable.rm_bg_trans_livestream);
            viewComment.setBackgroundColor(getResources().getColor(R.color.transparent));
            viewComment.setPadding(0, 0, 0, 0);
            viewComment.requestLayout();
            rvMessage.setPadding(0, 0, 0, 0);
            rvMessage.requestLayout();
            layoutReplyComment.setPadding(0, 0, 0, 0);
            layoutReplyComment.requestLayout();
        } else {
            if (isLandscape) {
                resetParamsReply();
                int rvMessageMargin = heightScreen / 8;
                rootView.setBackgroundResource(R.drawable.rm_bg_trans_livestream);
                viewComment.setBackgroundResource(R.drawable.rm_bg_trans_livestream);
                viewComment.setPadding(rvMessageMargin, 0, rvMessageMargin, 0);
                viewComment.requestLayout();
                rvMessage.setPadding(rvMessageMargin, 0, rvMessageMargin, 0);
                rvMessage.requestLayout();
                layoutReplyComment.setPadding(rvMessageMargin, 0, rvMessageMargin, 0);
                layoutReplyComment.requestLayout();
            } else {
                rootView.setBackgroundColor(getResources().getColor(R.color.transparent));
                viewComment.setBackgroundColor(getResources().getColor(R.color.transparent));
                viewComment.setPadding(0, 0, 0, 0);
                viewComment.requestLayout();
                rvMessage.setPadding(0, 0, 0, 0);
                rvMessage.requestLayout();
                layoutReplyComment.setPadding(0, 0, 0, 0);
                layoutReplyComment.requestLayout();
            }
        }
        onScrollToBottom();
    }

    @Override
    public void onInternetChanged() {
        if (NetworkHelper.isConnectInternet(activity) && currentVideo != null) {
            if (!SocketManager.getInstance().isConnected() && configLiveComment != null)
                SocketManager.getInstance().connectWebSocket(configLiveComment.getDomainWS(), currentVideo.getId());
            if (Utilities.isEmpty(messages)) loadComment(currentVideo.getId());
        }
    }

    @Override
    public void OnClickUser(String msisdn, String name) {
        if (app == null || activity == null || activity.isFinishing())
            return;
        if (enableClickTag) {
            enableClickTag = false;

            String jidNumber = app.getJidNumber();
            if (msisdn.equals(jidNumber)) {
            } else {
                PhoneNumber phoneNumber = app.getPhoneNumberFromNumber(msisdn);
                if (phoneNumber != null && phoneNumber.getId() != null) {
                    navigateToContactDetail(phoneNumber);
                } else {
                    navigateToStrangerDetail(msisdn, name, "", "");
                }
            }
        }
    }

    @Override
    public void onSmartTextClick(String content, int type) {
        if (type == Constants.SMART_TEXT.TYPE_KAKOAK) {
            DeepLinkHelper.getInstance().openSchemaLink(activity, content);
        } else if (type == Constants.SMART_TEXT.TYPE_URL) {
            Utilities.processOpenLink(app, activity, content);
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            PopupHelper.getInstance().showContextMenuSmartText(activity, fragmentManager, content, content, type, this);
        }
    }

    @Override
    public void onIconClickListener(View view, Object entry, int menuId) {
        switch (menuId) {
            case Constants.MENU.COPY:
                TextHelper.copyToClipboard(activity, (String) entry);
                break;
            case Constants.MENU.GO_URL:
                UrlConfigHelper.gotoWebViewOnMedia(app, activity, (String) entry);
                break;
            case Constants.MENU.SHARE_ONMEDIA:
                break;
            case Constants.MENU.SEND_EMAIL:
                NavigateActivityHelper.navigateToSendEmail(activity, (String) entry);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        enableClickTag = true;
    }
}
