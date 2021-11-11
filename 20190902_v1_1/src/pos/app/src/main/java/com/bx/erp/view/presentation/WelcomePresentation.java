package com.bx.erp.view.presentation;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Display;

import com.bx.erp.R;

public class WelcomePresentation extends BasePresentation {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public WelcomePresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_welcome_layout);
    }

    @Override
    public void onSelect(boolean isShow) {

    }
}
