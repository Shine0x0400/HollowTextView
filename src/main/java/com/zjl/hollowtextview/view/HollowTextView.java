package com.zjl.hollowtextview.view;

import com.zjl.hollowtextview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * A custom text view, can display text in transparent, just like hollow effect.
 * <p>For now, not support android:paddingTop and android:paddingBottom attributes! The text will
 * always drawn in vertical center</p>
 * Created by zjl on 16/3/22.
 */
public class HollowTextView extends View {
    private static final String TAG = "HollowTextView";

    private Context mContext;

    // XML attributes
    private String mText;

    private int mTextSize = 15;
    private int mBgColor = 0x99ffffff;
    private int mCornerRadius = 0;
    private boolean mIsTopLeftRound = false;
    private boolean mIsTopRightRound = false;
    private boolean mIsBottomLeftRound = false;
    private boolean mIsBottomRightRound = false;

    // content bitmap paint config
    private Paint mTextPaint;
    private Paint mBgPaint;
    private Paint mCornerPaint;

    private Bitmap mContentBitmap;
    private int mWidth;
    private int mHeight;
    private Canvas mBitmapCanvas;
    private RectF mBitmapRectF;

    public HollowTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public HollowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.HollowTextView,
                0, 0);

        try {
            mText = a.getString(R.styleable.HollowTextView_text);
            mTextSize = a.getDimensionPixelSize(R.styleable.HollowTextView_textSize, mTextSize);
            mBgColor = a.getColor(R.styleable.HollowTextView_bgColor, mBgColor);
            mCornerRadius = a.getDimensionPixelSize(R.styleable.HollowTextView_cornerRadius,
                    mCornerRadius);
            mIsTopLeftRound = a.getBoolean(R.styleable.HollowTextView_roundTopLeft, false);
            mIsTopRightRound = a.getBoolean(R.styleable.HollowTextView_roundTopRight, false);
            mIsBottomLeftRound = a.getBoolean(R.styleable.HollowTextView_roundBottomLeft, false);
            mIsBottomRightRound = a.getBoolean(R.styleable.HollowTextView_roundBottomRight, false);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setAntiAlias(true);

        mCornerPaint = new Paint();
        mCornerPaint.setColor(mBgColor);
        mCornerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));

        mBitmapRectF = new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mWidth = (int) mTextPaint.measureText(mText) + getPaddingLeft() + getPaddingRight();
        mHeight = mTextSize + getPaddingTop() + getPaddingBottom();

        mWidth = measureDimension(mWidth, widthMeasureSpec);
        mHeight = measureDimension(mHeight, heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
        Log.d(TAG, "----onMeasure---- the last width=" + mWidth + ", height=" + mHeight);
    }

    private int measureDimension(int defSize, int measureSpec) {
        int result = defSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
        case MeasureSpec.UNSPECIFIED:
            result = defSize;
            break;
        case MeasureSpec.AT_MOST:
            result = Math.min(defSize, specSize);
            break;
        case MeasureSpec.EXACTLY:
            result = specSize;
            break;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "----onSizeChanged----[w=" + w + ",h=" + h + ",oldw=" + oldw + ",oldh=" + oldh
                + "]");

        mContentBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBitmapCanvas = new Canvas(mContentBitmap);
        mBitmapRectF.set(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "----onDraw----");

        if (!TextUtils.isEmpty(mText)) {
            drawContentBitmap();
            canvas.drawBitmap(mContentBitmap, 0, 0, null);
        }
    }

    private void drawContentBitmap() {
        Log.i(TAG, "----drawContentBitmap----");
        if (!TextUtils.isEmpty(mText)) {

            mBitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            if (mCornerRadius > 0) {
                mBitmapCanvas.drawRoundRect(mBitmapRectF, mCornerRadius, mCornerRadius, mBgPaint);

                if (!mIsTopLeftRound) {
                    mBitmapCanvas.drawRect(0, 0, mCornerRadius, mCornerRadius, mCornerPaint);
                }
                if (!mIsTopRightRound) {
                    mBitmapCanvas.drawRect(mWidth - mCornerRadius, 0, mWidth, mCornerRadius,
                            mCornerPaint);
                }
                if (!mIsBottomLeftRound) {
                    mBitmapCanvas.drawRect(0, mHeight - mCornerRadius, mCornerRadius, mHeight,
                            mCornerPaint);
                }
                if (!mIsBottomRightRound) {
                    mBitmapCanvas.drawRect(mWidth - mCornerRadius, mHeight - mCornerRadius, mWidth,
                            mHeight, mCornerPaint);
                }

            } else {
                mBitmapCanvas.drawColor(mBgColor);
            }

            // draw text in vertical center
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float y = ((int) (mHeight - fontMetrics.ascent) >> 1) - 3;

            mBitmapCanvas.drawText(mText, getPaddingLeft(), y, mTextPaint);
        }
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        if (text == null || text.equals(mText)) {
            return;
        }

        this.mText = text;

        requestLayout();
        invalidate();
    }
}
