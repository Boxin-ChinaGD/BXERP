package com.bx.erp.view.presentation;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.widget.TextView;

import com.bx.erp.R;
import com.bx.erp.model.Commodity;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.view.activity.BaseActivity;
import com.bx.erp.view.adapter.CustomerCommodityRecyclerViewAdapter;

import java.util.List;

import static com.bx.erp.R.id.total_money;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CustomerCommodityListPresentation extends BasePresentation {
    private RecyclerView commodityRecyclerView;
    private TextView totalMoney;
    private TextView totalMoneyTV;
    private boolean isUpdateUI = false;

    private double totalMoneyDouble = 0.00;
    private DecimalFormat df = new DecimalFormat("######0.00");
    private CustomerCommodityRecyclerViewAdapter customerCommodityRecyclerViewAdapter;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CustomerCommodityListPresentation(Context context, Display display) {
        super(context, display);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CustomerCommodityListPresentation(Context context, Display display, boolean isUpdateUI) {
        super(context, display);
        this.isUpdateUI = isUpdateUI;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_commoditylist_layout);

        initView();
        showCommodityList(BaseActivity.showCommList);
    }

    private void initView() {
        commodityRecyclerView = findViewById(R.id.customer_rv_commodity);
        totalMoney = findViewById(R.id.total_money);
        totalMoneyTV = findViewById(R.id.total_money_tv);
        if (isUpdateUI) {
            totalMoneyTV.setText("应付：￥");
        }
    }

    private void showCommodityList(List<Commodity> list) {
        totalMoney.setText(GeneralUtil.formatToShow(BaseActivity.retailTrade.getAmount()));
        commodityRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        commodityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        customerCommodityRecyclerViewAdapter = new CustomerCommodityRecyclerViewAdapter(list, getContext());

        RecyclerView.ItemDecoration itemDecoration = commodityRecyclerView.getItemDecorationAt(0);
        commodityRecyclerView.removeItemDecoration(itemDecoration);
        customerCommodityRecyclerViewAdapter.notifyDataSetChanged();

        commodityRecyclerView.setAdapter(customerCommodityRecyclerViewAdapter);
    }

    @Override
    public void onSelect(boolean isShow) {

    }
}
