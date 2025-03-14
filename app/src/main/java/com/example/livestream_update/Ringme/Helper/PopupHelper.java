package com.example.livestream_update.Ringme.Helper;


/**
 * Created by ThaoDV on 6/6/14.
 */

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.fragment.PopupContextMenuFragment;
import com.vtm.ringme.livestream.listener.ClickListener;
import com.vtm.ringme.model.ItemContextMenu;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.Constants;

import java.util.ArrayList;


public class PopupHelper {
    private static final String TAG = PopupHelper.class.getSimpleName();
    private static PopupHelper mInstance = null;
    private Context mContext;
    private AppCompatActivity mCurrentActivity;


    public static PopupHelper getInstance() {
        if (mInstance == null) {
            synchronized (PopupHelper.class) {
                if (mInstance == null) {
                    mInstance = new PopupHelper();
                }
            }
        }
        return mInstance;
    }

    private PopupHelper() {
        //singleton constructor
        Log.i(TAG, "Creator!!");
    }


    public void setContext(Context context) {
        mContext = context;
    }


    //get list item menu smart text
    private ArrayList<ItemContextMenu> getListItemMenuSmartPhone(ApplicationController application, String content,
                                                                 PhoneNumber phone, boolean isUserRingMe, String numberJid) {
        ArrayList<ItemContextMenu> listItem = new ArrayList<>();
        ItemContextMenu copyItem = new ItemContextMenu(
                application.getString(R.string.copy), -1, content, Constants.MENU.COPY);
        ItemContextMenu addContact = new ItemContextMenu(application.getString(R.string.add_contact),
                -1, content, Constants.MENU.ADD_CONTACT);
        ItemContextMenu reengChat = new ItemContextMenu(application.getString(R.string.free_chat), -1, numberJid, Constants.MENU.REENG_CHAT);
        ItemContextMenu callItem = new ItemContextMenu(application.getString(R.string.call), -1, numberJid, Constants.MENU.MENU_CALL);
        ItemContextMenu callFree = new ItemContextMenu(application.getString(R.string.call_option_free), -1, numberJid, Constants.MENU.MENU_CALL_FREE);
        ItemContextMenu callOut = new ItemContextMenu(application.getString(R.string.call_option_out), -1, numberJid, Constants.MENU.MENU_CALL_OUT);
        listItem.add(copyItem);
        if (phone != null) {
            ItemContextMenu viewDetail = new ItemContextMenu(application.getString(R.string
                    .view_contact_detail), -1, phone, Constants.MENU.VIEW_CONTATCT_DETAIL);
            listItem.add(viewDetail);
        } else {
            listItem.add(addContact);
        }
        return listItem;
    }




    public void showContextMenuSmartText(final AppCompatActivity activity, final FragmentManager fragmentManager,
                                         final String title, final String content, int type, final ClickListener.IconListener callBack) {
        final ApplicationController application = (ApplicationController) mContext.getApplicationContext();
        if (type == Constants.SMART_TEXT.TYPE_NUMBER) {
            Phonenumber.PhoneNumber phoneNumberProtocol = PhoneNumberHelper.getInstant().
                    getPhoneNumberProtocol(application.getPhoneUtil(), content, application.getRegionCode());
            String myNumber = application.getJidNumber();
            PhoneNumber phone = application.getPhoneNumberFromNumber(content);
            if (content.equals(myNumber) || phone != null ||
                    !PhoneNumberHelper.getInstant().isValidPhoneNumber(application.getPhoneUtil(), phoneNumberProtocol)) {
                ArrayList<ItemContextMenu> listItems = new ArrayList<>();
                if (content.equals(myNumber) || !PhoneNumberHelper.getInstant().
                        isValidPhoneNumber(application.getPhoneUtil(), phoneNumberProtocol)) {
                    ItemContextMenu copyItem = new ItemContextMenu(
                            activity.getString(R.string.copy), -1, content, Constants.MENU.COPY);
                    listItems.add(copyItem);
                } else {
                    listItems = getListItemMenuSmartPhone(application, content, phone, false, content);
                }
                PopupContextMenuFragment mPopupContextMenu = new PopupContextMenuFragment(activity, title, listItems, callBack);
                mPopupContextMenu.show();
            } else {// goi api
                final String numberJid = PhoneNumberHelper.getInstant().getNumberJidFromNumberE164(
                        application.getPhoneUtil().format(phoneNumberProtocol, PhoneNumberUtil.PhoneNumberFormat.E164));
                ArrayList<String> listNumbers = new ArrayList<>();
                listNumbers.add(numberJid);
            }
        } else {
            ArrayList<ItemContextMenu> listItems = getListItemMenuSmartText(application, content, type);
            PopupContextMenuFragment mPopupContextMenu = new PopupContextMenuFragment(activity, title, listItems, callBack);
            mPopupContextMenu.show();
        }
    }


    private ArrayList<ItemContextMenu> getListItemMenuSmartText(ApplicationController application, String content, int type) {
        ArrayList<ItemContextMenu> listItem = new ArrayList<>();
        ItemContextMenu copyItem = new ItemContextMenu(
                application.getString(R.string.copy), -1, content, Constants.MENU.COPY);
        listItem.add(copyItem);
        if (type == Constants.SMART_TEXT.TYPE_EMAIL) {
            ItemContextMenu sendEmail = new ItemContextMenu(application.getString(R.string.send_mail), -1,
                    content, Constants.MENU.SEND_EMAIL);
            listItem.add(sendEmail);
        } else if (type == Constants.SMART_TEXT.TYPE_URL) {
            ItemContextMenu viewURL = new ItemContextMenu(application.getString(R.string.view_url), -1, content,
                    Constants.MENU.GO_URL);
            listItem.add(viewURL);
            if (application.isEnableOnMedia()) {
                ItemContextMenu shareOnMedia = new ItemContextMenu(application.getString(R.string
                        .onmedia_share_from_url), -1, content, Constants.MENU.SHARE_ONMEDIA);
                listItem.add(shareOnMedia);
            }
            if (TextHelper.getInstant().isYoutubeUrl(content)) {
                ItemContextMenu shareSieuHai = new ItemContextMenu(application.getString(R.string.share_sieu_hai),
                        -1, content, Constants.MENU.SHARE_SIEU_HAI);
                listItem.add(shareSieuHai);
            }
        }
        return listItem;
    }
}
