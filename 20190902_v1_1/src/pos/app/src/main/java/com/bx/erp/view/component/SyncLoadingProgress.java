package com.bx.erp.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.bx.erp.R;

public class SyncLoadingProgress extends View {
    private boolean mRunning = false;

    private int[] mStrokeWidthArr;
    private int mMaxStrokeWidth;
    private int mRippleCount;
    private int mRippleSpacing;

    private Paint mPaint;
    private Bitmap mBitmap;

    private int mWidth;
    private int mHeight;

    public SyncLoadingProgress(Context context) {
        this(context, null);
    }

    public SyncLoadingProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SyncLoadingProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SyncLoadingProgress);
        int waveColor = typedArray.getColor(R.styleable.SyncLoadingProgress_rippleColor,
                ContextCompat.getColor(context, R.color.colorPrimary));
        Drawable drawable = typedArray.getDrawable(R.styleable.SyncLoadingProgress_rippleCenterIcon);
        mRippleCount = typedArray.getInt(R.styleable.SyncLoadingProgress_rippleCount, 2);
        mRippleSpacing = typedArray.getDimensionPixelSize(R.styleable.SyncLoadingProgress_rippleSpacing, 16);
        mRunning = typedArray.getBoolean(R.styleable.SyncLoadingProgress_rippleAutoRunning, false);
        typedArray.recycle();

        mBitmap = ((BitmapDrawable) drawable).getBitmap();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(waveColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = (mRippleCount * mRippleSpacing + mBitmap.getWidth() / 2) * 2;
        mWidth = resolveSize(size, widthMeasureSpec);
        mHeight = resolveSize(size, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        mMaxStrokeWidth = (mWidth - mBitmap.getWidth()) / 2;
        initArray();
    }

    private void initArray() {
        mStrokeWidthArr = new int[mRippleCount];
        for (int i = 0; i < mStrokeWidthArr.length; i++) {
            mStrokeWidthArr[i] = -mMaxStrokeWidth / mRippleCount * i;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        if (mRunning) {
            drawRipple(canvas);
            postInvalidateDelayed(80);//修改水波纹的速度
        }
    }

    private void drawBitmap(Canvas canvas) {
        int left = (mWidth - mBitmap.getWidth()) / 2;
        int top = (mHeight - mBitmap.getHeight()) / 2;
        canvas.drawBitmap(mBitmap, left, top, null);
    }

    private void drawRipple(Canvas canvas) {
        for (int strokeWidth : mStrokeWidthArr) {
            if (strokeWidth < 0) {
                continue;
            }
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setAlpha(255 - 255 * strokeWidth / mMaxStrokeWidth);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mBitmap.getWidth() / 2 + strokeWidth / 2,
                    mPaint);
        }

        for (int i = 0; i < mStrokeWidthArr.length; i++) {
            if ((mStrokeWidthArr[i] += 4) > mMaxStrokeWidth) {
                mStrokeWidthArr[i] = 0;
            }
        }
    }

    public void start() {
        mRunning = true;
        postInvalidate();
    }

    public void stop() {
        mRunning = false;
        initArray();
        postInvalidate();
    }
}
