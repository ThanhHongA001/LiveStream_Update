package com.example.livestream_update.Ringme.Adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.customview.TagsCompletionView;
import com.vtm.ringme.holder.OnmediaTagHolder;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by thanhnt72 on 3/7/2016.
 */
public class TagOnMediaAdapter extends FilteredArrayAdapter<PhoneNumber> {

    private static final String TAG = TagOnMediaAdapter.class.getSimpleName();
    public static final int NO_LIMIT_TAG = -1;
    public static final int DISABLE_TAG = 0;
    public static final int DEFAULT_LIMIT_TAG = 5;

    //    private ArrayList<PhoneNumber> listPhoneNumber;
    private final ApplicationController mApp;
    private final TagsCompletionView mEditText;
    private final LayoutInflater mLayoutInflater;
    private int limitTag = DEFAULT_LIMIT_TAG; //default chi cho set 5 tag

    public TagOnMediaAdapter(ApplicationController context,
                             ArrayList<PhoneNumber> listPhoneNumber, TagsCompletionView mEditText) {
        super(context, R.layout.rm_holder_onmedia_user_like, listPhoneNumber);
        Log.i(TAG, "list size: " + listPhoneNumber.size());
        mApp = context;
//        this.listPhoneNumber = listPhoneNumber;
        this.mEditText = mEditText;
        this.mLayoutInflater = (LayoutInflater) mApp
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        /*Log.i(TAG, "mEditText.getObjects().size(): " + mEditText.getObjects().size());
        if (limitTag == 0) {
            return 0;
        }
        if (limitTag > 0 && limitTag < mEditText.getObjects().size()) {
            return 0;
        }*/
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OnmediaTagHolder holder;
        if (convertView == null) {
            holder = new OnmediaTagHolder(mApp);
            holder.initHolder(parent, convertView, position, mLayoutInflater);
        } else {
            holder = (OnmediaTagHolder) convertView.getTag();
        }
        holder.setElemnts(getItem(position));
        return holder.getConvertView();
    }


    @Override
    protected boolean keepObject(PhoneNumber obj, String mask) {
        if (obj.getName() == null || !mask.contains("@") || mEditText.getObjects().contains(obj))
            return false;

        int sub = mask.indexOf("@");
        mask = mask.substring(sub);
        mask = mask.toLowerCase();
        mask = mask.replaceFirst("@", "");

        String[] keymask = mask.split(" ");
        Boolean[] a = new Boolean[keymask.length];

        String name = Utilities.convert(obj.getName()).toLowerCase();

        boolean kt = false;
        if (TextUtils.isEmpty(mask) ) {//&& mApp.isCambodia()
            return true;
        }
        for (int i = 0; i < keymask.length; i++) {
            a[i] = false;//gia tri ban dau
            a[i] = searchContain(name, obj.getJidNumber(), keymask[i]);

            if (!a[i]) return a[i];//neu a[i] =false luon
            else kt = true;
        }

        return kt;
    }

    private boolean searchContain(String value, String msidn, String mask) {
        if (TextUtils.isEmpty(value) || TextUtils.isEmpty(msidn) || TextUtils.isEmpty(mask)) {
            return false;
        }
        boolean kt = value.contains(mask);
        if (!kt) {
            kt = msidn.contains(mask);
        }
        return kt;
    }

    public void setLimitTag(int limitTag) {
        this.limitTag = limitTag;
    }
}
