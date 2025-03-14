package com.example.livestream_update.Ringme.LiveStream.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.R;
import com.vtm.ringme.customview.TagGroup;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.livestream.listener.MessageActionListener;
import com.vtm.ringme.livestream.model.LiveStreamMessage;
import com.vtm.ringme.utils.ToastUtils;

public class MsgSayHiHolder extends BaseMessageLiveStreamHolder {

    private TagGroup tagGroup;
    private TextView tvSayHi;
    private AppCompatActivity activity;
    private MessageActionListener listenerMsg;
    private String tag1 = "";
    private String tag2 = "";
    private String tag3 = "";
    private String tag4 = "";
    private LiveStreamMessage data;

    public MsgSayHiHolder(View itemView, AppCompatActivity act, Video video, MessageActionListener listener) {
        super(itemView);
        this.activity = act;
        this.listenerMsg = listener;
        tvSayHi = itemView.findViewById(R.id.tvSayHi);
        tagGroup = itemView.findViewById(R.id.tagSayHi);
        tag1 = activity.getString(R.string.ls_hi_vn);
        tag2 = activity.getString(R.string.ls_hi_en);
        tag4 = activity.getString(R.string.ls_hi_enter_text);
        String nameChannel = "";
        if (video != null && video.getChannel() != null) {
            nameChannel = video.getChannel().getName();
        }
        tvSayHi.setText(String.format(activity.getString(R.string.ls_say_hi), nameChannel));
        tag3 = String.format(activity.getString(R.string.ls_hi_channel), nameChannel);
        bindData();
    }

    @Override
    public void setElement(Object obj, int pos) {
        if (obj instanceof LiveStreamMessage) {
            data = (LiveStreamMessage) obj;
        }
        bindData();
    }

    private void bindData() {
        if (tagGroup != null) {
            tagGroup.removeAllViews();
            if (data == null) {
                if (!TextUtils.isEmpty(tag1)) tagGroup.appendTagWithState(tag1, true);
                if (!TextUtils.isEmpty(tag2)) tagGroup.appendTagWithState(tag2, true);
                if (!TextUtils.isEmpty(tag3)) tagGroup.appendTagWithState(tag3, true);
                if (!TextUtils.isEmpty(tag4)) tagGroup.appendTagWithState(tag4, true);
            } else {
                if (!TextUtils.isEmpty(tag1))
                    tagGroup.appendTagWithState(tag1, data.isEnableHello());
                if (!TextUtils.isEmpty(tag2)) tagGroup.appendTagWithState(tag2, data.isEnableHi());
                if (!TextUtils.isEmpty(tag3))
                    tagGroup.appendTagWithState(tag3, data.isEnableHiName());
                if (!TextUtils.isEmpty(tag4)) tagGroup.appendTagWithState(tag4, true);
            }
            tagGroup.setOnTagClickListener((tagView, tag) -> {
                if (listenerMsg != null && !TextUtils.isEmpty(tag) && activity != null) {
                    if (tag.equals(tag1)) {
                        tagView.setDisabled();
                        if (data != null) {
                            if (!data.isEnableHello()) {
                                ToastUtils.showToast(activity.getApplicationContext(),activity.getString(R.string.msg_waning_click_say_hi));
                                return;
                            }
                            data.setEnableHello(false);
                        }
                        listenerMsg.onQuickSendText(tag);
                    } else if (tag.equals(tag2)) {
                        tagView.setDisabled();
                        if (data != null) {
                            if (!data.isEnableHi()) {
                                ToastUtils.showToast(activity.getApplicationContext(),activity.getString(R.string.msg_waning_click_say_hi));
                                return;
                            }
                            data.setEnableHi(false);
                        }
                        listenerMsg.onQuickSendText(tag);
                    } else if (tag.equals(tag3)) {
                        if (data != null) {
                            if (!data.isEnableHiName()) {
                                ToastUtils.showToast(activity.getApplicationContext(),activity.getString(R.string.msg_waning_click_say_hi));
                                return;
                            }
                            data.setEnableHiName(false);
                        }
                        tagView.setDisabled();
                        listenerMsg.onQuickSendText(tag);
                    } else if (tag.equals(tag4)) {
                        listenerMsg.onClickShowKeyboard();
                    }
                }
            });
        }
    }
}
