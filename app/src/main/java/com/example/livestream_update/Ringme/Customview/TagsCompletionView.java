package com.example.livestream_update.Ringme.Customview;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.QwertyKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vtm.R;
import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.helper.emoticon.EmoticonUtils;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

import java.util.ArrayList;
import java.util.List;


/**
 * Sample token completion view for basic contact info
 * <p/>
 * Created on 9/12/13.
 *
 * @author mgod
 */
public class TagsCompletionView extends TokenCompleteTextView {

    private TextMenuSelect textMenuSelect;

    private static final String TAG = TagsCompletionView.class.getSimpleName();

    public TagsCompletionView(Context context) {
        super(context);
    }

    public TagsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(Object object) {
        PhoneNumber p = (PhoneNumber) object;
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) l.inflate(R.layout.rm_v15_contact_token, (ViewGroup) TagsCompletionView.this
                .getParent(), false);
        ((TextView) view.findViewById(R.id.name)).setText(p.getName());
        return view;
    }

    public void setTextMenuSelect(TextMenuSelect textMenuSelect) {
        this.textMenuSelect = textMenuSelect;
    }

    protected void init() {
        setTokenizer(new CommaTokenizer());
        objects = new ArrayList<>();
        assert null != getText();
        spanWatcher = new TokenSpanWatcher();

        resetListeners();

        if (Build.VERSION.SDK_INT >= 11) {
            setTextIsSelectable(false);
        }
        setLongClickable(true);

        //In theory, get the soft keyboard to not supply suggestions. very unreliable < API 11
        setInputType(getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        setHorizontallyScrolling(false);

        setOnEditorActionListener(this);


        //We had _Parent style during initialization to handle an edge case in the parent
        //now we can switch to Clear, usually the best choice
        setDeletionStyle(TokenDeleteStyle.Clear);
        initialized = true;
    }


    @Override
    public boolean onEditorAction(TextView view, int action, KeyEvent keyEvent) {
        if (action == 1010) {// action send when keyboard landscap
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
            return true;
        }
        return false;
    }

    @Override
    protected Object defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not

        PhoneNumber item = new PhoneNumber();
        item.setName(completionText);
        return item;
    }

    protected SpannableStringBuilder buildSpannableForText(CharSequence text) {
        //Add a sentinel , at the beginning so the user can remove an inner token and keep auto-completing
        //This is a hack to work around the fact that the tokenizer cannot directly detect spans
        String textTag = PREFIX_TAG + ((PhoneNumber) selectedObject).getName();
        return new SpannableStringBuilder(textTag + tokenizer.terminateToken(text));
    }

    @Override
    protected void replaceText(CharSequence text) {

        clearComposingText();
        SpannableStringBuilder ssb = buildSpannableForText(text);
        TokenImageSpan tokenSpan = buildSpanForObject(selectedObject);

        Editable editable = getText();
        int end = getSelectionEnd();
        int start = tokenizer.findTokenStart(editable, end);
        if (start < prefix.length()) {
            start = prefix.length();
        }


        String original = TextUtils.substring(editable, start, end);
        int sub = original.indexOf("@");
        if (sub > 0) {
            start = start + sub;
        }
//        if (start<0||end<0) return;
        Log.i(TAG, "replaceText----> start index: " + start + ", end index: " + end + ", text replace: "
                + ssb + ", CharSequence: " + text);
        if (editable != null) {
            if (tokenSpan == null) {
                editable.replace(start, end, " ");
            } else if (!allowDuplicates && objects.contains(tokenSpan.getToken())) {
                editable.replace(start, end, " ");
            } else {
                QwertyKeyListener.markAsReplaced(editable, start, end, original);
                editable.replace(start, end, ssb);
                //xoa dau space cuoi cung, sau do moi setSpan
                editable.delete(start + ssb.length() - 1, start + ssb.length());
                editable.setSpan(tokenSpan, start, start + ssb.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public ArrayList<PhoneNumber> getUserInfo() {
        ArrayList<PhoneNumber> userList = new ArrayList<>();

        List<Object> data = getObjects();
        for (int i = 0; i < data.size(); i++) {
            userList.add((PhoneNumber) data.get(i));
        }

        return userList;
    }

    private boolean useJid;

    public void setUseJid(boolean useJid) {
        this.useJid = useJid;
    }

    public String getTextTag() {
        String list = EmoticonUtils.getRawTextFromSpan(getText());
        ArrayList<PhoneNumber> tags = getUserInfo();
        if (tags.isEmpty()) return list;
        for (int i = 0; i < tags.size(); i++) {
            PhoneNumber us = tags.get(i);
//            if (us.getId().equals("0")) continue;
            String textReplace = us.getName() + ",";
            String textName;
            if (useJid) {
                textName = us.getJidNumber();
            } else {
                if (TextUtils.isEmpty(us.getNickName())) {
                    textName = Utilities.hidenPhoneNumber(us.getJidNumber());
                } else {
                    textName = us.getNickName();
                }
            }
            if (textName != null)
                list = list.replace(textReplace, textName);
        }
//        Log.i(TAG, "tagend " + tags.size() + " / list " + list);
//        list = list.replace("#tag#,", "");
        return list;
    }

    public void resetObject() {
        setText("");
        resetListeners();
        objects.clear();
        selectedObject = null;
        setSelection(0);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean returnValue;
        switch (id) {
            case android.R.id.cut:
                String contentCut = getTextTag();
                //TODO super chỗ này máy sony 8.0 bị crash
//                returnValue = super.onTextContextMenuItem(id);
                returnValue = false;
                TextHelper.copyToClipboard(getContext(), contentCut);
                setText("");
                //copyContentToClipboard(true);
                break;
            case android.R.id.copy:
                String contentCopy = getTextTag();
                returnValue = super.onTextContextMenuItem(id);
                TextHelper.copyToClipboard(getContext(), contentCopy);
                //copyContentToClipboard(false);
                break;
            case android.R.id.paste:
                returnValue = super.onTextContextMenuItem(id);
                if (textMenuSelect != null) {
                    textMenuSelect.onMenuPaste();
                }
                break;
            default:
                returnValue = super.onTextContextMenuItem(id);
                break;
        }
        return returnValue;
    }


    public interface TextMenuSelect {
        void onMenuPaste();
    }
}
