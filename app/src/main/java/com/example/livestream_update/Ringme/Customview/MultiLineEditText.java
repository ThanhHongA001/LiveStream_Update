package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * Created by ThanhNT on 1/21/2015.
 */
public class MultiLineEditText extends EmoEditText {
    private static final String TAG = MultiLineEditText.class.getSimpleName();
    private boolean mIsSend = false;
    private int mActionEditerInfo;

    public MultiLineEditText(Context context) {
        super(context);
    }

    public MultiLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiLineEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * actionEditerInfo ko can quan tam neu isSend = false
     * */

    public void setEditerAction(boolean isSend, int actionEditerInfo) {
        mIsSend = isSend;
        mActionEditerInfo = actionEditerInfo;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);

        if (mIsSend) {
            int imeActions = outAttrs.imeOptions & EditorInfo.IME_MASK_ACTION;
            if ((imeActions & mActionEditerInfo) != 0) {
                // clear the existing action
                outAttrs.imeOptions ^= imeActions;
                // set the DONE action
                outAttrs.imeOptions |= mActionEditerInfo;
            }
            if ((outAttrs.imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
                outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
            }
        }

        return connection;
    }
}