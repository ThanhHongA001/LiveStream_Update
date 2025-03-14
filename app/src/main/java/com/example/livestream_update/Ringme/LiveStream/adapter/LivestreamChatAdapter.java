package com.example.livestream_update.Ringme.LiveStream.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.livestream.holder.LivestreamChatHolder;
import com.vtm.ringme.livestream.listener.LivestreamCommentActionListener;
import com.vtm.ringme.livestream.listener.ReactionCommentClickListener;
import com.vtm.ringme.livestream.model.LivestreamChatModel;
import com.vtm.ringme.livestream.model.ReactCommentNotification;
import com.vtm.ringme.values.Constants;


import java.text.DecimalFormat;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class LivestreamChatAdapter extends RecyclerView.Adapter<LivestreamChatHolder> {
    public static final int TYPE_LIVE = 1;
    public static final int TYPE_FUTURE = 0;
    public static final int TYPE_VOD = 5;

    List<LivestreamChatModel> chatModels;
    Activity context;
    LivestreamCommentActionListener commentActionListener;
    boolean isShowAll = false;
    int screenType;
    ReactionCommentAdapter reactionCommentAdapter;
    ReactionCommentClickListener reactionCommentClickListener;
    private boolean isFuture;
    private int type;

    public LivestreamChatAdapter(List<LivestreamChatModel> chatModels, Activity context, int screenType, int type) {
        this.chatModels = chatModels;
        this.context = context;
        this.screenType = screenType;
        this.type = type;
    }

    public void setReactionCommentClickListener(ReactionCommentClickListener reactionCommentClickListener) {
        this.reactionCommentClickListener = reactionCommentClickListener;
    }

    public void setCommentActionListener(LivestreamCommentActionListener commentActionListener) {
        this.commentActionListener = commentActionListener;
    }

    public void addMessage(LivestreamChatModel livestreamChatModel) {
        chatModels.add(livestreamChatModel);
        notifyItemChanged(chatModels.size() - 1);
    }

    public List<LivestreamChatModel> getChatModels() {
        return chatModels;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteComment(String commentId) {
        LivestreamChatModel deletedComment = new LivestreamChatModel();
        for (LivestreamChatModel model : chatModels) {
            if (model.getChatMessage().getsIdMessage() != null && model.getChatMessage().getsIdMessage().equals(commentId)) {
                deletedComment = model;
            }

            if (model.getChatMessage().getSmsgId() != null && model.getChatMessage().getSmsgId().equals(commentId)) {
                deletedComment = model;
            }
        }
        chatModels.remove(deletedComment);
        notifyDataSetChanged();
    }

    @NonNull
    @androidx.annotation.NonNull
    @Override
    public LivestreamChatHolder onCreateViewHolder(@NonNull @androidx.annotation.NonNull ViewGroup viewGroup, int i) {
        int layoutId = 0;
        switch (type) {
            case TYPE_LIVE:
                layoutId = R.layout.rm_holder_livestream_chat_message;
                break;
            case TYPE_FUTURE:
                layoutId = R.layout.rm_holder_livestream_chat_message_future;
                break;
            case TYPE_VOD:
                layoutId = R.layout.rm_holder_livestream_chat_message_vod;
                break;
            default:
                layoutId = 0;
                break;
        }
        return new LivestreamChatHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(layoutId, viewGroup, false));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull @androidx.annotation.NonNull LivestreamChatHolder livestreamChatHolder, int i) {
        LivestreamChatModel chatModel = chatModels.get(i);
        livestreamChatHolder.popupReaction.animate().scaleX(0f).setDuration(0);
        livestreamChatHolder.popupReaction.animate().scaleY(0f).setDuration(0);

        if (screenType == 0) {
            if (type == TYPE_VOD || type == TYPE_FUTURE) {
                livestreamChatHolder.layoutComment.setCardBackgroundColor(Color.parseColor("#00000000"));
                livestreamChatHolder.getMessLayout().setBackgroundColor(Color.parseColor("#00000000"));
            } else {
                livestreamChatHolder.layoutComment.setCardBackgroundColor(Color.parseColor("#3A3A3C"));
                livestreamChatHolder.getMessLayout().setBackgroundColor(Color.parseColor("#303030"));
            }
        } else {
            livestreamChatHolder.layoutComment.setCardBackgroundColor(Color.parseColor("#003A3A3C"));
            livestreamChatHolder.getMessLayout().setBackgroundColor(Color.parseColor("#66303030"));
        }

        switch (chatModel.getType()) {
            case Constants.WebSocket.typeMessage:
                if (chatModel.getChatMessage().getNumberReaction() < 1 || type != TYPE_LIVE || chatModel.getChatMessage().getReactions() == null) {
                    livestreamChatHolder.layoutListReaction.setVisibility(View.GONE);
                    livestreamChatHolder.btnReaction.setVisibility(View.GONE);
                } else {
                    livestreamChatHolder.btnReaction.setVisibility(View.VISIBLE);
                    livestreamChatHolder.layoutListReaction.setVisibility(View.VISIBLE);
                    livestreamChatHolder.tvReactionNumber.setText(String.valueOf(chatModel.getChatMessage().getNumberReaction()));
                    reactionCommentAdapter = new ReactionCommentAdapter();
                    reactionCommentAdapter.setList(chatModel.getChatMessage().getReactions().getListReaction());
                    livestreamChatHolder.rcvListReaction.setAdapter(reactionCommentAdapter);
                    livestreamChatHolder.rcvListReaction.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                }
                Glide.with(livestreamChatHolder.itemView.getContext()).load(chatModel.getChatMessage().getAvatar()).centerCrop().placeholder(R.drawable.rm_ic_avatar_default).into(livestreamChatHolder.getAvatar());
                livestreamChatHolder.getChatMessage().setText(chatModel.getChatMessage().getMsgBody());
                livestreamChatHolder.getChatUserName().setText(chatModel.getChatMessage().getUserName());
                livestreamChatHolder.layoutStarNumber.setVisibility(View.GONE);
                livestreamChatHolder.imageGift.setVisibility(View.GONE);
                livestreamChatHolder.getMessLayout().setBackgroundResource(R.color.transparent);
                livestreamChatHolder.getChatMessage().postDelayed(() -> {
                    Layout l = livestreamChatHolder.getChatMessage().getLayout();
                    if (l != null) {
                        int lines = l.getLineCount();
                        if (lines > 0) {
                            if (l.getEllipsisCount(lines - 1) > 0)
                                livestreamChatHolder.getTvSeeMore().setVisibility(View.VISIBLE);
                            else livestreamChatHolder.getTvSeeMore().setVisibility(View.GONE);
                        }
                    }
                }, 250);

                livestreamChatHolder.getTvSeeMore().setOnClickListener(view -> {
                    if (!isShowAll) {
                        livestreamChatHolder.getChatMessage().setMaxLines(Integer.MAX_VALUE);
                        livestreamChatHolder.getTvSeeMore().setText(R.string.see_less);
                    } else {
                        livestreamChatHolder.getChatMessage().setMaxLines(3);
                        livestreamChatHolder.getTvSeeMore().setText(R.string.see_more);
                    }
                    isShowAll = !isShowAll;
                });
                String msisdn = ApplicationController.self().getJidNumber();
                Log.e("TAG-live", "onBindViewHolder: "+type);
                if (chatModel.getChatMessage().getUserId().equals(msisdn) && type == TYPE_LIVE) {
                    livestreamChatHolder.btnDelete.setVisibility(View.VISIBLE);
                    Log.e("TAG-live", "onBindViewHolder2: "+type);
                } else {
                    livestreamChatHolder.btnDelete.setVisibility(View.GONE);
                    Log.e("TAG-live", "onBindViewHolder3: "+type);
                }

                livestreamChatHolder.btnDelete.setOnClickListener(view -> {
                    String chatId = "";
                    if (!TextUtils.isEmpty(chatModel.getChatMessage().getSmsgId())) {
                        chatId = chatModel.getChatMessage().getSmsgId();
                    } else if (!TextUtils.isEmpty(chatModel.getChatMessage().getsIdMessage())) {
                        chatId = chatModel.getChatMessage().getsIdMessage();
                    }
                    commentActionListener.onClickDeleteComment(chatId, chatModel.getChatMessage().getAvatar(), chatModel.getChatMessage().getUserName());
                });
                livestreamChatHolder.getAvatar().setOnClickListener(view -> {
                        if (!chatModel.getChatMessage().getUserId().equals(msisdn))
                            commentActionListener.onClickUserProfile(chatModel.getChatMessage().getUserId(), chatModel.getChatMessage().getUserName());
                });
                livestreamChatHolder.btnCancelReact.setOnClickListener(view -> {
                    String chatId = "";
                    if (!TextUtils.isEmpty(chatModel.getChatMessage().getSmsgId())) {
                        chatId = chatModel.getChatMessage().getSmsgId();
                    } else if (!TextUtils.isEmpty(chatModel.getChatMessage().getsIdMessage())) {
                        chatId = chatModel.getChatMessage().getsIdMessage();
                    }
                    if (chatModel.getReactionType() != 0) {
//                        chatModel.setReactionType(0);
//                        notifyItemChanged(i);
                        reactionCommentClickListener.onClickReactionComment(chatModel.getReactionType(), chatId, "UNLIKE", i);
                    }
                    hideView(livestreamChatHolder.popupReaction);
                });
                livestreamChatHolder.btnLike.setOnClickListener(view -> {
                    String chatId = "";
                    if (!TextUtils.isEmpty(chatModel.getChatMessage().getSmsgId())) {
                        chatId = chatModel.getChatMessage().getSmsgId();
                    } else if (!TextUtils.isEmpty(chatModel.getChatMessage().getsIdMessage())) {
                        chatId = chatModel.getChatMessage().getsIdMessage();
                    }
//                    chatModel.setReactionType(1);
                    hideView(livestreamChatHolder.popupReaction);
//                    notifyItemChanged(i);
                    reactionCommentClickListener.onClickReactionComment(1, chatId, "LIKE", i);
                });
                livestreamChatHolder.btnHeart.setOnClickListener(view -> {
                    String chatId = "";
                    if (!TextUtils.isEmpty(chatModel.getChatMessage().getSmsgId())) {
                        chatId = chatModel.getChatMessage().getSmsgId();
                    } else if (!TextUtils.isEmpty(chatModel.getChatMessage().getsIdMessage())) {
                        chatId = chatModel.getChatMessage().getsIdMessage();
                    }
//                    chatModel.setReactionType(2);
                    hideView(livestreamChatHolder.popupReaction);
//                    notifyItemChanged(i);
                    reactionCommentClickListener.onClickReactionComment(2, chatId, "LIKE", i);
                });
                livestreamChatHolder.btnHaha.setOnClickListener(view -> {
                    String chatId = "";
                    if (!TextUtils.isEmpty(chatModel.getChatMessage().getSmsgId())) {
                        chatId = chatModel.getChatMessage().getSmsgId();
                    } else if (!TextUtils.isEmpty(chatModel.getChatMessage().getsIdMessage())) {
                        chatId = chatModel.getChatMessage().getsIdMessage();
                    }
//                    chatModel.setReactionType(3);
                    hideView(livestreamChatHolder.popupReaction);
//                    notifyItemChanged(i);
                    reactionCommentClickListener.onClickReactionComment(3, chatId, "LIKE", i);
                });
                livestreamChatHolder.btnWow.setOnClickListener(view -> {
                    String chatId = "";
                    if (!TextUtils.isEmpty(chatModel.getChatMessage().getSmsgId())) {
                        chatId = chatModel.getChatMessage().getSmsgId();
                    } else if (!TextUtils.isEmpty(chatModel.getChatMessage().getsIdMessage())) {
                        chatId = chatModel.getChatMessage().getsIdMessage();
                    }
//                    chatModel.setReactionType(4);
                    hideView(livestreamChatHolder.popupReaction);
//                    notifyItemChanged(i);
                    reactionCommentClickListener.onClickReactionComment(4, chatId, "LIKE", i);
                });
                livestreamChatHolder.btnSad.setOnClickListener(view -> {
                    String chatId = "";
                    if (!TextUtils.isEmpty(chatModel.getChatMessage().getSmsgId())) {
                        chatId = chatModel.getChatMessage().getSmsgId();
                    } else if (!TextUtils.isEmpty(chatModel.getChatMessage().getsIdMessage())) {
                        chatId = chatModel.getChatMessage().getsIdMessage();
                    }
//                    chatModel.setReactionType(5);
                    hideView(livestreamChatHolder.popupReaction);
//                    notifyItemChanged(i);
                    reactionCommentClickListener.onClickReactionComment(5, chatId, "LIKE", i);
                });
                livestreamChatHolder.btnAngry.setOnClickListener(view -> {
                    String chatId = "";
                    if (!TextUtils.isEmpty(chatModel.getChatMessage().getSmsgId())) {
                        chatId = chatModel.getChatMessage().getSmsgId();
                    } else if (!TextUtils.isEmpty(chatModel.getChatMessage().getsIdMessage())) {
                        chatId = chatModel.getChatMessage().getsIdMessage();
                    }
//                    chatModel.setReactionType(6);
                    hideView(livestreamChatHolder.popupReaction);
//                    notifyItemChanged(i);
                    reactionCommentClickListener.onClickReactionComment(6, chatId, "LIKE", i);
                });

                if (chatModel.getReactionType() == 0) {
                    livestreamChatHolder.imvReaction.setImageResource(R.drawable.rm_ic_reaction_comment_default);
                } else if (chatModel.getReactionType() == 1) {
                    livestreamChatHolder.imvReaction.setImageResource(R.drawable.rm_ic_react_like);
                } else if (chatModel.getReactionType() == 2) {
                    livestreamChatHolder.imvReaction.setImageResource(R.drawable.rm_ic_react_heart);
                } else if (chatModel.getReactionType() == 3) {
                    livestreamChatHolder.imvReaction.setImageResource(R.drawable.rm_ic_react_haha);
                } else if (chatModel.getReactionType() == 4) {
                    livestreamChatHolder.imvReaction.setImageResource(R.drawable.rm_ic_react_wow);
                } else if (chatModel.getReactionType() == 5) {
                    livestreamChatHolder.imvReaction.setImageResource(R.drawable.rm_ic_react_sad);
                } else if (chatModel.getReactionType() == 6) {
                    livestreamChatHolder.imvReaction.setImageResource(R.drawable.rm_ic_react_angry);
                }

                livestreamChatHolder.btnReaction.setOnClickListener(view -> {
                    livestreamChatHolder.popupReaction.setVisibility(View.VISIBLE);
                    livestreamChatHolder.popupReaction.animate().scaleX(1f).setDuration(250);
                    livestreamChatHolder.popupReaction.animate().scaleY(1f).setDuration(250);
                });
                livestreamChatHolder.layoutComment.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (type == TYPE_LIVE) {
                            livestreamChatHolder.popupReaction.setVisibility(View.VISIBLE);
                            livestreamChatHolder.popupReaction.animate().scaleX(1f).setDuration(250);
                            livestreamChatHolder.popupReaction.animate().scaleY(1f).setDuration(250);
                            return true;
                        } else return false;
                    }
                });
                break;
            case Constants.WebSocket.typeGift:
                Glide.with(livestreamChatHolder.itemView.getContext()).load(chatModel.getStreamGift().getAvatar()).centerCrop().placeholder(R.drawable.rm_ic_avatar_default).into(livestreamChatHolder.getAvatar());
                livestreamChatHolder.getChatMessage().setText(chatModel.getStreamGift().getMessage());
                livestreamChatHolder.getChatUserName().setText(chatModel.getStreamGift().getUserName());
                livestreamChatHolder.getMessLayout().setBackgroundResource(R.drawable.rm_bg_donate_livestream);
                livestreamChatHolder.layoutStarNumber.setVisibility(View.GONE);
                livestreamChatHolder.imageGift.setVisibility(View.VISIBLE);
                livestreamChatHolder.btnReaction.setVisibility(View.GONE);
                livestreamChatHolder.btnDelete.setVisibility(View.GONE);
                try {
                    Glide.with(livestreamChatHolder.itemView.getContext())
                                    .load(chatModel.getStreamGift().getGiftImg())
                                    .placeholder(R.drawable.rm_place_holder_round)
                                     .into(livestreamChatHolder.imageGift);
                    livestreamChatHolder.tvAmount.setText(shortenNumber(Long.parseLong(String.valueOf(chatModel.getStreamGift().getAmountStar()))));
                } catch (Exception e) {
                    livestreamChatHolder.tvAmount.setText("null");
                }
                livestreamChatHolder.getChatMessage().postDelayed(() -> {
                    Layout l = livestreamChatHolder.getChatMessage().getLayout();
                    if (l != null) {
                        int lines = l.getLineCount();
                        if (lines > 0) {
                            if (l.getEllipsisCount(lines - 1) > 0)
                                livestreamChatHolder.getTvSeeMore().setVisibility(View.VISIBLE);
                            else livestreamChatHolder.getTvSeeMore().setVisibility(View.GONE);
                        }
                    }
                }, 250);

                livestreamChatHolder.getTvSeeMore().setOnClickListener(view -> {
                    if (!isShowAll) {
                        livestreamChatHolder.getChatMessage().setMaxLines(Integer.MAX_VALUE);
                        livestreamChatHolder.getTvSeeMore().setText(R.string.see_less);
                    } else {
                        livestreamChatHolder.getChatMessage().setMaxLines(3);
                        livestreamChatHolder.getTvSeeMore().setText(R.string.see_more);
                    }
                    isShowAll = !isShowAll;
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatModels == null ? 0 : chatModels.size();
    }

    private void hideView(View view) {
        view.animate().scaleX(0f).setDuration(250);
        view.animate().scaleY(0f).setDuration(250);
        view.postDelayed(() -> view.setVisibility(View.GONE), 250);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateReaction(ReactCommentNotification event) {
        for (LivestreamChatModel item : chatModels) {
            if ((!TextUtils.isEmpty(item.getChatMessage().getSmsgId()) && item.getChatMessage().getSmsgId().equals(event.getsIdMessage()))
                    || (!TextUtils.isEmpty(item.getChatMessage().getsIdMessage()) && item.getChatMessage().getsIdMessage().equals(event.getsIdMessage()))) {
                item.getChatMessage().setReactions(event.getReacts());
                item.getChatMessage().setNumberReaction(event.getNumberReaction());
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void setReact(int position, int type) {
        chatModels.get(position).setReactionType(type);
        notifyItemChanged(position);
    }

    private String shortenNumber(long value) {
        String shortenValue = "";
        if (value < 1000) {
            shortenValue = String.valueOf(value);
        } else if (value < 999999) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000) + "K";
        } else if (value < 999999999) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000000) + "M";
        } else if (value < Long.parseLong("999999999999")) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000000000) + "B";
        } else if (value < Long.parseLong("999999999999999")) {
            shortenValue = new DecimalFormat("#.##").format((double) value / Long.parseLong("1000000000000")) + "T";
        } else {
            shortenValue = new DecimalFormat("#.## ").format((double) value / Long.parseLong("1000000000000000")) + "Q";
        }
        return shortenValue;
    }
}
