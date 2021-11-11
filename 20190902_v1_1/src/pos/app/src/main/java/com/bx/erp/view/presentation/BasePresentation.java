package com.bx.erp.view.presentation;

import android.app.Presentation;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.Display;
import android.view.View;

import com.bx.erp.helper.BasePresentationHelper;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public abstract class BasePresentation extends Presentation implements View.OnClickListener {
    private static final String TAG = "BasePresentation";
    public boolean isShow;
    int index;
    public View.OnClickListener onClickListener;
    public BasePresentationHelper helper = BasePresentationHelper.getInstance();

    public BasePresentation(Context outerContext, Display display) {
        super(outerContext, display);
        index = helper.add(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setOnClickListener(@Nullable View.OnClickListener l) {
        onClickListener = l;
    }

    @Override
    public void hide() {
        super.hide();
        helper.hide(this);
        isShow = false;
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

    public abstract void onSelect(boolean isShow);
}
