package com.example.livestream_update.Ringme.LiveStream.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.ringme.ApplicationController;
import com.vtm.R;
import com.vtm.ringme.customview.TagGroup;
import com.vtm.ringme.livestream.listener.MessageActionListener;
import com.vtm.ringme.livestream.listener.OnSingleClickListener;
import com.vtm.ringme.livestream.model.LiveStreamMessage;
import com.vtm.ringme.imageview.CircleImageView;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.utils.Utilities;

public class MsgFriendWatchHolder extends BaseMessageLiveStreamHolder {

    private TextView tvSayHi;
    private TagGroup tagGroup;
    private CircleImageView ivAvatar;
    private TextView tvAvatar;
    private AppCompatActivity activity;
    private ApplicationController app;
    private MessageActionListener listenerMsg;
    private LiveStreamMessage message;

    public MsgFriendWatchHolder(View itemView, AppCompatActivity act, MessageActionListener listener) {
        super(itemView);
        this.activity = act;
        app = (ApplicationController) activity.getApplication();
        this.listenerMsg = listener;
        tagGroup = itemView.findViewById(R.id.tag_group);
        tvSayHi = itemView.findViewById(R.id.tvSayHi);
        ivAvatar = itemView.findViewById(R.id.ivAvatar);
        tvAvatar = itemView.findViewById(R.id.tvAvatar);
        String s1 = activity.getString(R.string.ls_invite_friend);
        String s2 = activity.getString(R.string.ls_share_social);
        tagGroup.setTags(s1, s2);
        tagGroup.setOnTagClickListener((tagView, tag) -> {
            if (listenerMsg != null && !TextUtils.isEmpty(tag))
                if (tag.equals(activity.getString(R.string.ls_invite_friend)))
                    listenerMsg.onInviteFriend();
                else
                    listenerMsg.onShareVideo(true);
        });
        ivAvatar.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (listenerMsg != null && message != null)
                    listenerMsg.onClickUser(message, getAdapterPosition());
            }
        });
    }

    @Override
    public void setElement(Object obj, int pos) {
        message = (LiveStreamMessage) obj;
        int sizeAvatar = (int) activity.getResources().getDimension(R.dimen.avatar_small_size);
        if (!message.isGetContactPhoneDone()) {
            message.setPhoneNumber(app.getPhoneNumberFromNumber(message.getMsisdn()));
            message.setGetContactPhoneDone(true);
        }

        PhoneNumber phoneNumber = message.getPhoneNumber();
        String friendName;
        if (phoneNumber == null) {
            if (TextUtils.isEmpty(message.getNameSender()))
                friendName = Utilities.hidenPhoneNumber(message.getMsisdn());
            else
                friendName = message.getNameSender();
        } else {
            friendName = phoneNumber.getName();
        }
        tvSayHi.setText(String.format(activity.getString(R.string.ls_friend_watch), friendName));
    }
}
