package com.example.livestream_update.Ringme.Dialog;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vtm.R;
import com.vtm.databinding.RmBottomSheetLivestreamFutureChatBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.livestream.adapter.LivestreamChatAdapter;
import com.vtm.ringme.livestream.model.LiveStreamBlockNotification;
import com.vtm.ringme.livestream.model.LivestreamChatModel;
import com.vtm.ringme.livestream.socket.SocketManagerV2;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.ToastUtils;
import com.vtm.ringme.utils.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;


public class LivestreamFutureChatBottomSheetDialog extends BottomSheetDialogFragment {

    private final LivestreamModel livestream;
    private RmBottomSheetLivestreamFutureChatBinding binding;
    private final List<LivestreamChatModel> chatModels;
    private LivestreamChatAdapter adapterV2;
    private final boolean isReachBottom = true;
    private String avatarUrl = "";
    private final LivestreamFutureActivity activity;

    public LivestreamFutureChatBottomSheetDialog(@NonNull LivestreamFutureActivity context, LivestreamModel livestream, List<LivestreamChatModel> chatModels) {
        this.livestream = livestream;
        this.activity = context;
        this.chatModels = chatModels;
    }

    public static LivestreamFutureChatBottomSheetDialog newInstance(LivestreamFutureActivity context, LivestreamModel livestream, List<LivestreamChatModel> chatModels) {
        LivestreamFutureChatBottomSheetDialog fragment = new LivestreamFutureChatBottomSheetDialog(context, livestream, chatModels);
        return fragment;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.BitelBottomSheetStyle);
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = RmBottomSheetLivestreamFutureChatBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());
        Window window = activity.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnDismissListener(dialogInterface -> dismiss());
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        return dialog;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        adapterV2 = new LivestreamChatAdapter(chatModels, activity, livestream.getScreenType(), LivestreamChatAdapter.TYPE_FUTURE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setStackFromEnd(true);
        binding.rcvChat.setLayoutManager(layoutManager);
        binding.rcvChat.setAdapter(adapterV2);
        binding.cmtNumber.setText(Utilities.shortenLongNumber(chatModels.size()) + (chatModels.size() > 1 ? " Comments" : " Comment"));
        if (livestream.getEnableChat() == 0) {
            binding.tvDisableChat.setVisibility(View.VISIBLE);
            binding.rcvChat.setVisibility(View.GONE);
        } else {
            binding.tvDisableChat.setVisibility(View.GONE);
            binding.rcvChat.setVisibility(View.VISIBLE);
        }

        setupChatController();
        binding.btnSendReal.setOnClickListener(view -> onClickSend());
        binding.btnSendFake.setOnClickListener(view -> onClickSend());
        Glide.with(activity)
                .load(avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.rm_ic_place_holder_user)
                .into(binding.imgAvatarFake);
        Glide.with(activity)
                .load(avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.rm_ic_place_holder_user)
                .into(binding.imgAvatarReal);
        binding.voteBar.setVisibility(activity.hasVote ? View.VISIBLE : View.GONE);
        binding.voteBar.setOnClickListener(view -> {
            activity.openVoteDialog();
            Objects.requireNonNull(getDialog()).dismiss();
        });
    }

    private void setupChatController() {
        binding.inputCommentFake.setOnClickListener(view -> {
            if (!livestream.isBlockComment()) {
                binding.loutChatReal.setVisibility(View.VISIBLE);
                binding.inputCommentReal.requestFocus();
                KeyboardUtils.showSoftInput();
            } else {
                ToastUtils.showToast(activity, activity.getString(R.string.you_are_block_full));
                binding.inputCommentFake.setText(activity.getString(R.string.you_are_block));
            }
        });

        binding.inputCommentReal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String content = Objects.requireNonNull(binding.inputCommentReal.getText()).toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    binding.btnSendReal.setImageResource(R.drawable.rm_ic_send_comment_livestream_future_active);
                    binding.btnSendFake.setImageResource(R.drawable.rm_ic_send_comment_livestream_future_active);
                } else {
                    binding.btnSendReal.setImageResource(R.drawable.rm_ic_send_comment_livestream_future_inactive);
                    binding.btnSendFake.setImageResource(R.drawable.rm_ic_send_comment_livestream_future_inactive);
                }
                binding.inputCommentFake.setText(content);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        handleKeyboardShow();
    }

    private void handleKeyboardShow() {
        KeyboardUtils.registerSoftInputChangedListener(Objects.requireNonNull(activity.getWindow()), height -> {
            binding.loutChatReal.setVisibility(height > 0 ? View.VISIBLE : View.GONE);
            binding.loutChatFake.setVisibility(height > 0 ? View.GONE : View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.rcvChat.getLayoutParams();
            layoutParams.height = height > 0 ? dpToPixels(100) : dpToPixels(400);
            binding.rcvChat.setLayoutParams(layoutParams);
            Log.d("rcvHeight", "handleKeyboardShow: " + binding.rcvChat.getLayoutParams().height);
        });
    }

    private int dpToPixels(int dp) {
        Resources r = activity.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private void onClickSend() {
        if (TextUtils.isEmpty(Objects.requireNonNull(binding.inputCommentReal.getText()).toString().trim())) {
        } else {
            if (livestream.getEnableChat() == 2) {
                ToastUtils.showToast(activity, activity.getString(R.string.only_fan));
            } else {
                if (livestream.getEnableChat() == 1) {
                    if (SocketManagerV2.getInstance().isConnected()) {
                        SocketManagerV2.getInstance().sendMessage(binding.inputCommentReal.getText().toString().trim());
                        binding.inputCommentReal.setText("");
                    } else {
                        ToastUtils.showToast(activity, activity.getString(R.string.error_message_default));
                    }
                }
            }
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(this, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void addComment(LivestreamChatModel chatModel) {
        adapterV2.notifyItemChanged(chatModels.size() - 1);
        binding.rcvChat.smoothScrollToPosition(adapterV2.getItemCount() - 1);
        binding.cmtNumber.setText(Utilities.shortenLongNumber(chatModels.size()) + (chatModels.size() > 1 ? " Comments" : " Comment"));
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveBlockNotification(LiveStreamBlockNotification event) {
        if (event.getBlockId().equals("3")) {
            adapterV2.deleteComment(event.getChatId());
            binding.cmtNumber.setText(Utilities.shortenLongNumber(chatModels.size()) + (chatModels.size() > 1 ? " Comments" : " Comment"));
            EventBus.getDefault().removeStickyEvent(event);
        } else if (event.getUserId().equals(ApplicationController.self().getJidNumber())) {
            if (event.getBlockId().equals("5")) {
                binding.inputCommentReal.setText("");
                binding.inputCommentFake.setText(activity.getString(R.string.you_are_block));
                binding.inputCommentFake.setOnClickListener(view -> {
                    ToastUtils.showToast(activity, activity.getString(R.string.you_are_block_full));
                });
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = activity.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(activity);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else if (event.getBlockId().equals("6")) {
                binding.inputCommentFake.setText("");
                binding.inputCommentFake.setOnClickListener(view -> {
                    binding.inputCommentReal.requestFocus();
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(binding.loutChatReal, InputMethodManager.SHOW_IMPLICIT);
                });
                ToastUtils.showToast(activity, activity.getString(R.string.unblock_live_chat_notify_message));
            }
        }
        EventBus.getDefault().removeStickyEvent(event);
    }
}
