package com.example.livestream_update.Ringme.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vtm.ringme.holder.ContextMenuHolder;
import com.vtm.ringme.model.ItemContextMenu;

import java.util.ArrayList;

/**
 * Created by toanvk2 on 1/27/15.
 */
public class ContextMenuAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ItemContextMenu> mItems = new ArrayList<>();
    private Context mContext;

    public ContextMenuAdapter(Context context) {
        mContext = context;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // set list item
    public void setListItem(ArrayList<ItemContextMenu> list) {
        mItems = list;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        ItemContextMenu item = (ItemContextMenu) getItem(position);
        return item.getActionTag();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemContextMenu item = (ItemContextMenu) getItem(position);
        ContextMenuHolder holder;
        if (convertView == null) {
            holder = new ContextMenuHolder(mContext);
            holder.initHolder(parent, convertView, position, layoutInflater);
        } else {
            holder = (ContextMenuHolder) convertView.getTag();
        }
        holder.setElemnts(item);
        return holder.getConvertView();
    }
}

