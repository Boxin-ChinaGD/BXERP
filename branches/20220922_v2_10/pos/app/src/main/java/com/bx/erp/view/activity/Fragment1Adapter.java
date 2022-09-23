package com.bx.erp.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WPNA on 2020/2/21.
 */

public class Fragment1Adapter extends FragmentPagerAdapter {

    private List<Fragment> FragmentList;
    private Main1Activity main1Activity;
    private RetrieveCommodityInventory1Activity inventory1Fragment;
    private CheckList_Fragment1 checkListFragment;
    private Bill_Fragment billFragment;
//    private ReportLoss_Fragment reportLossFragment;
    private SmallSheet1Activity SmallSheet1Activity;
    private ResetBaseData1Activity dataProcessingFragment;

    public Fragment1Adapter(FragmentManager fm) {
        super(fm);
        main1Activity = new Main1Activity();//销售页面
        inventory1Fragment = new RetrieveCommodityInventory1Activity();//库存页面
        checkListFragment = new CheckList_Fragment1();
        billFragment = new Bill_Fragment();
//        reportLossFragment = new ReportLoss_Fragment();
        SmallSheet1Activity = new SmallSheet1Activity();
        dataProcessingFragment = new ResetBaseData1Activity();

        FragmentList = new ArrayList<>();
        FragmentList.add(main1Activity);
        FragmentList.add(inventory1Fragment);
        FragmentList.add(checkListFragment);
//        FragmentList.add(reportLossFragment);
        FragmentList.add(SmallSheet1Activity);
        FragmentList.add(dataProcessingFragment);
        FragmentList.add(billFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }
}
