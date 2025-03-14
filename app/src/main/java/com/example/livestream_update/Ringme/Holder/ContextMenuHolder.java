package com.example.livestream_update.Ringme.Holder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtm.R;
import com.vtm.ringme.model.ItemContextMenu;


/**
 * Created by toanvk2 on 1/27/15.
 */
public class ContextMenuHolder extends AbsContentHolder {
    private TextView mTvwContent;
    private ImageView mImgDetail;
    private ContextMenuHolder mHolder;

    public ContextMenuHolder(Context context) {
        mHolder = this;
    }

    @Override
    public void initHolder(ViewGroup parent, View rowView, int position, LayoutInflater layoutInflater) {
        View convertView = layoutInflater.inflate(R.layout.rm_holder_context_menu, parent, false);
        mHolder.mTvwContent = convertView.findViewById(R.id.item_context_menu_text);
        mHolder.mImgDetail = convertView.findViewById(R.id.item_context_menu_icon);
        convertView.setTag(mHolder);
        setConvertView(convertView);
    }

    public void setElemnts(Object obj) {
        ItemContextMenu item = (ItemContextMenu) obj;
        setViewHolder(item);
    }

    private void setViewHolder(ItemContextMenu item) {
//        mHolder.mTvwSinger.setVisibility(View.GONE);
        mTvwContent.setText(item.getText());
        if (item.getImageRes() != -1) {
            mImgDetail.setVisibility(View.VISIBLE);
            mImgDetail.setImageResource(item.getImageRes());
        } else {
            mImgDetail.setVisibility(View.GONE);
        }
    }
}