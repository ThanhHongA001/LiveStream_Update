package com.example.livestream_update.Ringme.LiveStream.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vtm.R;


public class LivestreamChatHolder extends RecyclerView.ViewHolder {
    private TextView chatMessage, chatUserName, tvSeeMore;

    public TextView getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(TextView chatMessage) {
        this.chatMessage = chatMessage;
    }

    public TextView getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(TextView chatUserName) {
        this.chatUserName = chatUserName;
    }

    public RoundedImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(RoundedImageView avatar) {
        this.avatar = avatar;
    }

    private RoundedImageView avatar;

    private TextView sentStars;

    private RelativeLayout messLayout;

    public TextView getSentStars() {
        return sentStars;
    }

    public void setSentStars(TextView giftImg) {
        this.sentStars = giftImg;
    }

    public RelativeLayout getMessLayout() {
        return messLayout;
    }

    public void setMessLayout(RelativeLayout messLayout) {
        this.messLayout = messLayout;
    }

    public TextView getTvSeeMore() {
        return tvSeeMore;
    }

    public void setTvSeeMore(TextView tvSeeMore) {
        this.tvSeeMore = tvSeeMore;
    }
    public CardView layoutComment;
    public AppCompatImageView btnDelete;
    public CardView layoutListReaction;
    public CardView btnReaction;
    public AppCompatImageView imvReaction;
    public TextView tvReactionNumber;
    public AppCompatImageView btnLike, btnHeart, btnHaha, btnWow, btnSad, btnAngry, imageGift;
    public CardView btnCancelReact;
    public LinearLayout popupReaction;
    public RecyclerView rcvListReaction;
    public CardView layoutStarNumber;
    public TextView tvAmount;

    public LivestreamChatHolder(View itemView) {
        super(itemView);
        chatMessage = itemView.findViewById(R.id.chatMessage);
        avatar = itemView.findViewById(R.id.watcherAvatar);
        chatUserName = itemView.findViewById(R.id.chatUsername);
        messLayout = itemView.findViewById(R.id.mess_layout);

        tvSeeMore = itemView.findViewById(R.id.tv_see_more);
        layoutComment = itemView.findViewById(R.id.layout_comment);
        btnDelete = itemView.findViewById(R.id.btn_delete);
        layoutListReaction = itemView.findViewById(R.id.layout_list_reaction);
        btnReaction = itemView.findViewById(R.id.btn_react);
        imvReaction = itemView.findViewById(R.id.imv_reaction);
        tvReactionNumber = itemView.findViewById(R.id.tv_reaction_number);
        btnLike = itemView.findViewById(R.id.btn_like);
        btnHeart = itemView.findViewById(R.id.btn_heart);
        btnHaha = itemView.findViewById(R.id.btn_haha);
        btnWow = itemView.findViewById(R.id.btn_wow);
        btnSad = itemView.findViewById(R.id.btn_sad);
        btnAngry = itemView.findViewById(R.id.btn_angry);
        imageGift = itemView.findViewById(R.id.img_gift);
        btnCancelReact = itemView.findViewById(R.id.btn_cancel_react);
        popupReaction = itemView.findViewById(R.id.popup_reaction);
        rcvListReaction = itemView.findViewById(R.id.rcv_list_reaction);
        layoutStarNumber = itemView.findViewById(R.id.layout_star_number);
        tvAmount = itemView.findViewById(R.id.tv_amount);
    }
}
