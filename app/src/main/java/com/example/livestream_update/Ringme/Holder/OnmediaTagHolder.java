package com.example.livestream_update.Ringme.Holder;



import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.imageview.CircleImageView;
import com.vtm.ringme.model.PhoneNumber;


/**
 * Created by thanhnt72 on 3/7/2016.
 */
public class OnmediaTagHolder extends AbsContentHolder {


    private static final String TAG = OnmediaTagHolder.class.getSimpleName();
    private CircleImageView mImgAvatar;
    private TextView mTvwName, mTvwAvatar;
    private PhoneNumber mPhoneNumber;
    private ApplicationController mApplication;
    private Resources mRes;

    public OnmediaTagHolder(ApplicationController mApplication) {
        this.mApplication = mApplication;
        mRes = mApplication.getResources();
    }

    @Override
    public void initHolder(ViewGroup parent, View rowView, int position, LayoutInflater layoutInflater) {
        View convertView = layoutInflater.inflate(R.layout.rm_holder_tag_ringme, parent, false);
        mTvwName = (TextView) convertView.findViewById(R.id.tvw_onmedia_user);
        mImgAvatar = (CircleImageView) convertView.findViewById(R.id.img_onmedia_avatar);
        mTvwAvatar = (TextView) convertView.findViewById(R.id.tvw_onmedia_avatar);
        convertView.setTag(this);
        setConvertView(convertView);
    }

    public void setElemnts(Object obj) {
        this.mPhoneNumber = (PhoneNumber) obj;
        mTvwName.setText(mPhoneNumber.getName());
        int size = (int) mRes.getDimension(R.dimen.avatar_small_size);
//        mApplication.setPhoneNumberAvatar(mImgAvatar, mTvwAvatar, mPhoneNumber, size);
    }
}
