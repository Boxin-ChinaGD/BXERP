package com.bx.erp.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bx.erp.R;

import java.lang.reflect.Field;

public class CustomRadioButtom extends FrameLayout implements Checkable {
    private boolean mChecked;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private boolean clickable = true;

    public CustomRadioButtom(Context context) {
        this(context, null);
    }

    public CustomRadioButtom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRadioButtom(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CustomRadioButtom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        defStyleAttr = getInternalR("attr", "radioButtonStyle");
        final TypedArray a = context.obtainStyledAttributes(
                attrs, getInternalRS("styleable", "CompoundButton"), defStyleAttr, defStyleRes);
        if (a.hasValue(getInternalR("styleable", "CompoundButton_checked"))) {
            final boolean checked = a.getBoolean(getInternalR("styleable", "CompoundButton_checked"), false);
            setChecked(checked);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (clickable) {
                    performClick();
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        this.clickable = clickable;
    }

    /**
     * Register radiobutton_unselect callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Register radiobutton_unselect callback to be invoked when the checked state of this button
     * changes. This callback is used for internal purpose only.
     *
     * @param listener the callback to call on checked state change
     * @hide
     */
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            setSelected(checked);
            refreshDrawableState();
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setSelected(mChecked);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setSelected(selected);
        }
    }

    public int getInternalR(String v1, String v2) {
        int titleStyle = 0;
        try {
            Class clasz = Class.forName("com.android.internal.R$" + v1);//styleable
            Field field = clasz.getDeclaredField(v2);
            field.setAccessible(true);
            titleStyle = (Integer) field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return titleStyle;
    }

    public int[] getInternalRS(String v1, String v2) {
        int[] textAppearanceStyleArr = new int[0];
        try {
            Class clasz = Class.forName("com.android.internal.R$" + v1);//styleable
            Field field = clasz.getDeclaredField(v2);
            field.setAccessible(true);
            textAppearanceStyleArr = (int[]) field.get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return textAppearanceStyleArr;
    }

    public void setBackground(int radiobutton_select) {

    }

    /**
     * Interface definition for radiobutton_unselect callback to be invoked when the checked state
     * of radiobutton_unselect compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of radiobutton_unselect compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(View buttonView, boolean isChecked);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public boolean performClick() {
        toggle();

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes radiobutton_unselect sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @Override
    public void toggle() {
        if (!isChecked()) {
            setChecked(!mChecked);
        }
    }
}