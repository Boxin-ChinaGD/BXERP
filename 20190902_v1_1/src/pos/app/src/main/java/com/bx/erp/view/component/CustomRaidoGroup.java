package com.bx.erp.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.lang.reflect.Field;

/**
 * Created by WPNA on 2020/4/6.
 */

public class CustomRaidoGroup extends GridLayout{

        // holds the checked id; the selection is empty by default
        private int mCheckedId = -1;
        private boolean mProtectFromCheckedChange = false;
        private OnCheckedChangeListener mOnCheckedChangeListener;

        private CustomRadioButtom.OnCheckedChangeListener mChildOnCheckedChangeListener;
        private PassThroughHierarchyChangeListener mPassThroughListener;

        public CustomRaidoGroup(Context context) {
            super(context);
            init();
        }

        public CustomRaidoGroup(Context context, AttributeSet attrs) {
            super(context, attrs);
            // retrieve selected radio button as requested by the user in the
            // XML layout file
            TypedArray attributes = context.obtainStyledAttributes(
                    attrs, getInternalRS("styleable", "RadioGroup"), getInternalR("attr", "radioButtonStyle"), 0);

            int value = attributes.getResourceId(getInternalR("styleable", "RadioGroup_checkedButton"), View.NO_ID);
            if (value != View.NO_ID) {
                mCheckedId = value;
            }
            attributes.recycle();
            init();
        }

        private void init() {
            mChildOnCheckedChangeListener = new CheckedStateTracker();
            mPassThroughListener = new PassThroughHierarchyChangeListener();
            super.setOnHierarchyChangeListener(mPassThroughListener);
        }

        /**
         * <p>Register radiobutton_unselect callback to be invoked when the checked radio button
         * changes in this group.</p>
         *
         * @param listener the callback to call on checked state change
         */
        public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
            mOnCheckedChangeListener = listener;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
            // the user listener is delegated to our pass-through listener
            mPassThroughListener.mOnHierarchyChangeListener = listener;
        }

        /**
         * <p>Interface definition for radiobutton_unselect callback to be invoked when the checked
         * radio button changed in this group.</p>
         */
        public interface OnCheckedChangeListener {
            /**
             * <p>Called when the checked radio button has changed. When the
             * selection is cleared, checkedId is -1.</p>
             *
             * @param group     the group in which the checked radio button has changed
             * @param checkedId the unique identifier of the newly checked radio button
             */
            public void onCheckedChanged(View group, @IdRes int checkedId);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();

            // checks the appropriate radio button as requested in the XML file
            if (mCheckedId != -1) {
                mProtectFromCheckedChange = true;
                setCheckedStateForView(mCheckedId, true);
                mProtectFromCheckedChange = false;
                setCheckedId(mCheckedId);
            }
        }

        private class CheckedStateTracker implements CustomRadioButtom.OnCheckedChangeListener {

            @Override
            public void onCheckedChanged(View buttonView, boolean isChecked) {
                // prevents from infinite recursion
                if (mProtectFromCheckedChange) {
                    return;
                }

                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;

                int id = buttonView.getId();
                setCheckedId(id);
            }
        }

        @Override
        public void addView(View child, int index, ViewGroup.LayoutParams params) {
            if (child instanceof CustomRadioButtom) {
                final CustomRadioButtom button = (CustomRadioButtom) child;
                if (button.isChecked()) {
                    mProtectFromCheckedChange = true;
                    if (mCheckedId != -1) {
                        setCheckedStateForView(mCheckedId, false);
                    }
                    mProtectFromCheckedChange = false;
                    setCheckedId(button.getId());
                }
            }

            super.addView(child, index, params);
        }

        /**
         * <p>Sets the selection to the radio button whose identifier is passed in
         * parameter. Using -1 as the selection identifier clears the selection;
         * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
         *
         * @param id the unique id of the radio button to select in this group
         * @see #getCheckedRadioButtonId()
         * @see #clearCheck()
         */
        public void check(@IdRes int id) {
            // don't even bother
            if (id != -1 && (id == mCheckedId)) {
                return;
            }

            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }

            if (id != -1) {
                setCheckedStateForView(id, true);
            }

            setCheckedId(id);
        }

        private void setCheckedId(@IdRes int id) {
            mCheckedId = id;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
            }
        }

        private void setCheckedStateForView(int viewId, boolean checked) {
            View checkedView = findViewById(viewId);
            if (checkedView != null && checkedView instanceof CustomRadioButtom) {
                ((CustomRadioButtom) checkedView).setChecked(checked);
            }
        }

        /**
         * <p>Returns the identifier of the selected radio button in this group.
         * Upon empty selection, the returned value is -1.</p>
         *
         * @return the unique id of the selected radio button in this group
         * @attr ref android.R.styleable#RadioGroup_checkedButton
         * @see #check(int)
         * @see #clearCheck()
         */
        @IdRes
        public int getCheckedRadioButtonId() {
            return mCheckedId;
        }

        /**
         * <p>Clears the selection. When the selection is cleared, no radio button
         * in this group is selected and {@link #getCheckedRadioButtonId()} returns
         * null.</p>
         *
         * @see #check(int)
         * @see #getCheckedRadioButtonId()
         */
        public void clearCheck() {
            check(-1);
        }

        /**
         * <p>A pass-through listener acts upon the events and dispatches them
         * to another listener. This allows the table layout to set its own internal
         * hierarchy change listener without preventing the user to setup his.</p>
         */
        private class PassThroughHierarchyChangeListener implements
                ViewGroup.OnHierarchyChangeListener {
            private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

            /**
             * {@inheritDoc}
             */
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onChildViewAdded(View parent, View child) {
                if (parent == CustomRaidoGroup.this && child instanceof CustomRadioButtom) {
                    int id = child.getId();
                    // generates an id if it's missing
                    if (id == View.NO_ID) {
                        id = View.generateViewId();
                        child.setId(id);
                    }
                    ((CustomRadioButtom) child).setOnCheckedChangeWidgetListener(
                            mChildOnCheckedChangeListener);
                }

                if (mOnHierarchyChangeListener != null) {
                    mOnHierarchyChangeListener.onChildViewAdded(parent, child);
                }
            }

            /**
             * {@inheritDoc}
             */
            public void onChildViewRemoved(View parent, View child) {
                if (parent == CustomRaidoGroup.this && child instanceof CustomRadioButtom) {
                    ((CustomRadioButtom) child).setOnCheckedChangeWidgetListener(null);
                }
                if (mOnHierarchyChangeListener != null) {
                    mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
                }
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
}
