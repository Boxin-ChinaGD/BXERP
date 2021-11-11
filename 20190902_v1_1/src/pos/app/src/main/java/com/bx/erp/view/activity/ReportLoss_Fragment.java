package com.bx.erp.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bx.erp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by WPNA on 2020/2/26.
 */

public class ReportLoss_Fragment extends BaseFragment1 {
    @BindView(R.id.category)
    LinearLayout category;
    @BindView(R.id.linear_bottom)
    LinearLayout linearBottom;
    @BindView(R.id.null_view)
    View nullView;
    @BindView(R.id.add_reportloss_page)
    FrameLayout addReportlossPage;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reportloss_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.add_reportloss, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_reportloss:
                category.setVisibility(View.GONE);
                linearBottom.setVisibility(View.GONE);
                addReportlossPage.setVisibility(View.VISIBLE);
                nullView.setVisibility(View.VISIBLE);
                setAnimation(addReportlossPage);
                break;
            case R.id.cancel:
                category.setVisibility(View.VISIBLE);
                linearBottom.setVisibility(View.VISIBLE);
                addReportlossPage.setVisibility(View.GONE);
                nullView.setVisibility(View.GONE);
                setAnimation(addReportlossPage);
                break;
        }
    }

}
