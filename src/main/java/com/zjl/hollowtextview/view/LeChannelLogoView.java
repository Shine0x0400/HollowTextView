package com.zjl.hollowtextview.view;

import com.zjl.hollowtextview.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by zjl on 16/3/31.
 */
public class LeChannelLogoView extends RelativeLayout {
    private Context mContext;

    // xml
    private HollowTextView mCategoryText;
    private TextView mNumberText;
    private TextView mProgramText;

    private String mCategoryStr;
    private int mNumber;
    private String mProgramStr;

    public LeChannelLogoView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public LeChannelLogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public LeChannelLogoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        inflate(mContext, R.layout.le_channel_logo_view, this);

        mCategoryText = (HollowTextView) findViewById(R.id.channel_category);
        mNumberText = (TextView) findViewById(R.id.channel_number);
        mProgramText = (TextView) findViewById(R.id.channel_program_name);
    }

    private void setCategory(String category) {
        mCategoryText.setText(category);
    }

    private void setChannelNumber(String number) {
        mNumberText.setText(number);
    }

    private void setProgram(String program) {
        mProgramText.setText(program);
    }

    /**
     * update ui with fresh content
     * @param category
     *            the channel category name
     * @param number
     *            the channel number
     * @param program
     *            the channel program name
     */
    public void updateContent(String category, String number, String program) {
        setCategory(category);
        setChannelNumber(number);
        setProgram(program);
    }

}
