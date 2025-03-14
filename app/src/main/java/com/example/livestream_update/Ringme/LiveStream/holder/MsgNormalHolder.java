package com.example.livestream_update.Ringme.LiveStream.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.customview.TagTextView;
import com.vtm.ringme.helper.TimeHelper;
import com.vtm.ringme.imageview.CircleImageView;
import com.vtm.ringme.livestream.listener.MessageActionListener;
import com.vtm.ringme.livestream.listener.OnSingleClickListener;
import com.vtm.ringme.livestream.listener.SmartTextClickListener;
import com.vtm.ringme.livestream.model.LiveStreamMessage;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.model.TagRingMe;
import com.vtm.ringme.utils.Utilities;

/**
 * Created by thanhnt72 on 8/15/2019.
 */

public class MsgNormalHolder extends BaseMessageLiveStreamHolder {

    private AppCompatActivity activity;
    private MessageActionListener listenerMsg;
    private ApplicationController app;

    private View viewAvatar;
    private CircleImageView ivAvatar;
    private TextView tvAvatar;
    private TextView tvName;
    private View layoutReply;
    private TextView tvQuoteName;
    private TextView tvQuoteContent;
    private TextView tvNumberLike;
    private ImageView ivLikeCmt;
    private TagTextView tvContent;
    private TextView tvTime;
    private TextView tvReply;

    private LiveStreamMessage message;
    private ReengAccount account;
    private TagRingMe.OnClickTag onClickTag;
    private SmartTextClickListener smartTextClickListener;

    public MsgNormalHolder(View itemView, AppCompatActivity act, MessageActionListener listener
            , TagRingMe.OnClickTag onClickTag, SmartTextClickListener smartTextClickListener) {
        super(itemView);
        this.activity = act;
        this.app = (ApplicationController) activity.getApplication();
        account = app.getCurrentAccount();
        this.listenerMsg = listener;
        this.onClickTag = onClickTag;
        this.smartTextClickListener = smartTextClickListener;

        viewAvatar = itemView.findViewById(R.id.viewAvatar);
        ivAvatar = itemView.findViewById(R.id.ivAvatar);
        tvAvatar = itemView.findViewById(R.id.tvAvatar);
        tvName = itemView.findViewById(R.id.tvName);
        layoutReply = itemView.findViewById(R.id.layoutReply);
        tvQuoteName = itemView.findViewById(R.id.tvQuoteName);
        tvQuoteContent = itemView.findViewById(R.id.tvQuoteContent);
        tvNumberLike = itemView.findViewById(R.id.tvNumberLike);
        ivLikeCmt = itemView.findViewById(R.id.ivLikeCmt);
        tvContent = itemView.findViewById(R.id.tvContent);
        tvContent.setMaxLines(10);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvReply = itemView.findViewById(R.id.tvReply);
    }

    @Override
    public void setElement(Object obj, final int pos) {
        message = (LiveStreamMessage) obj;
        String content = message.getContent();
        if (TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        } else {
            if (Utilities.notEmpty(message.getListTag())) {
                tvContent.setEmoticonWithTag(app, content, content.hashCode(), content, message.getListTag(), onClickTag, smartTextClickListener);
            } else {
                tvContent.setEmoticon(app, content, content.hashCode(), content, smartTextClickListener);
            }
        }

        long deltaTimeServer = message.getTimeServer() - System.currentTimeMillis();
        tvTime.setText(TimeHelper.caculateTimeFeed(tvTime.getContext(), message.getTimeStamp(), deltaTimeServer));
        int sizeAvatar = (int) activity.getResources().getDimension(R.dimen.avatar_small_size);

        if (message.getMsisdn() != null) {
            if (message.getMsisdn().equals(account.getJidNumber())) {
                tvName.setText(account.getName());
            } else {
                if (!message.isGetContactPhoneDone()) {
                    message.setGetContactPhoneDone(true);
                }

                PhoneNumber phoneNumber = message.getPhoneNumber();
                if (phoneNumber == null) {
                    if (TextUtils.isEmpty(message.getNameSender()))
                        tvName.setText(Utilities.hidenPhoneNumber(message.getMsisdn()));
                    else
                        tvName.setText(message.getNameSender());
                } else {
                    tvName.setText(phoneNumber.getName());
                }
            }
        }

        LiveStreamMessage quotedMessage = message.getQuotedMessage();
        if (quotedMessage == null)
            layoutReply.setVisibility(View.GONE);
        else {
            if (quotedMessage.getMsisdn().equals(account.getJidNumber())) {
                tvQuoteName.setText(account.getName());
            } else {
                if (!quotedMessage.isGetContactPhoneDone()) {
                    quotedMessage.setGetContactPhoneDone(true);
                }
                PhoneNumber phoneNumQuote = quotedMessage.getPhoneNumber();
                if (phoneNumQuote == null)
                    if (TextUtils.isEmpty(quotedMessage.getNameSender()))
                        tvQuoteName.setText(Utilities.hidenPhoneNumber(quotedMessage.getMsisdn()));
                    else
                        tvQuoteName.setText(quotedMessage.getNameSender());
                else tvQuoteName.setText(phoneNumQuote.getName());

            }
            tvQuoteContent.setText(quotedMessage.getContent());
            layoutReply.setVisibility(View.VISIBLE);
        }

        if (message.getCountLike() > 0) {
            tvNumberLike.setText(message.getCountLike() + "");
            tvNumberLike.setVisibility(View.VISIBLE);
        } else {
            tvNumberLike.setText("");
            tvNumberLike.setVisibility(View.GONE);
        }
        if (message.isLike())
            ivLikeCmt.setImageResource(R.drawable.rm_ic_onmedia_like_press);
        else
            ivLikeCmt.setImageResource(R.drawable.rm_ic_onmedia_like);

        tvReply.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (listenerMsg != null) listenerMsg.onReplyMessage(message, pos);
            }
        });

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
                    tvNumberLike.setVisibility(View.VISIBLE);
                } else {
                    tvNumberLike.setText("");
                    tvNumberLike.setVisibility(View.GONE);
                }

                if (listenerMsg != null)
                    listenerMsg.onClickLikeMessage(message, pos);
            }
        });

        viewAvatar.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (listenerMsg != null)
                    listenerMsg.onClickUser(message, pos);
            }
        });

        tvQuoteContent.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (tvQuoteContent.getMaxLines() == 1)
                    tvQuoteContent.setMaxLines(10);
                else
                    tvQuoteContent.setMaxLines(1);
            }
        });
    }
}
