package com.example.livestream_update.Ringme.Glide.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.R;
import com.vtm.ringme.adapter.ContextMenuAdapter;
import com.vtm.ringme.customview.EllipsisTextView;
import com.vtm.ringme.livestream.listener.ClickListener;
import com.vtm.ringme.model.ItemContextMenu;

import java.util.ArrayList;

/**
 * Created by toanvk2 on 7/9/14.
 */
public class PopupContextMenuFragment extends Dialog {
    private String menuTitle = "test";
    private ContextMenuAdapter menuAdapter = null;
    private ClickListener.IconListener clickHandler = null;
    private AppCompatActivity mContext;


    public PopupContextMenuFragment(AppCompatActivity activity, String title, ArrayList<ItemContextMenu> listItem,
                                    ClickListener.IconListener callBack) {
        super(activity, R.style.DialogFullscreen);
        this.mContext = activity;
        this.menuAdapter = new ContextMenuAdapter(activity);
        this.menuTitle = title;
        this.clickHandler = callBack;
        this.menuAdapter.setListItem(listItem);
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rm_popup_context_menu);

        findComponentViews();
    }

    private void findComponentViews() {
        EllipsisTextView title = findViewById(R.id.context_menu_title);
        if (menuTitle != null) {
            title.setVisibility(View.VISIBLE);
            title.setEmoticon(mContext, menuTitle, menuTitle.hashCode(), menuTitle);
        } else {
            title.setVisibility(View.GONE);
        }
        ListView listItem = findViewById(R.id.context_menu_listview);
        // add footer and header
        listItem.setAdapter(menuAdapter);
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemContextMenu item = (ItemContextMenu) parent.getItemAtPosition(position);
                if (clickHandler != null) {
                    clickHandler.onIconClickListener(view, item.getObj(), item.getActionTag());
                }
                dismiss();
            }
        });
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogFullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = initControl(inflater, container);
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(true);
        }
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
        return v;
    }*/

}