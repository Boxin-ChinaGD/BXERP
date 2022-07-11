package com.bx.erp.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bx.erp.R;
import com.bx.erp.event.RetailTradeHttpEvent;
import com.bx.erp.view.adapter.DialogBillRecyclerViewAdapter;
import com.bx.erp.view.adapter.DialogBillRetailTradeRecyclerViewAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Bill_Fragment extends BaseFragment1 {
    Unbinder unbinder;
    @BindView(R.id.deleteBill)
    TextView deleteBill;
    @BindView(R.id.continueRetail)
    TextView continueRetail;
    @BindView(R.id.rv_retailtrade)
    RecyclerView rv_retailtrade;
    @BindView(R.id.rv_commodity)
    RecyclerView rv_commodity;

    private DialogBillRetailTradeRecyclerViewAdapter dialogBillRetailTradeRecyclerViewAdapter;
    private DialogBillRecyclerViewAdapter dialogBillRecyclerViewAdapter;
    private int isSelectNum;
    private boolean isSelect = false;

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onRetailTradeHttpEvent(RetailTradeHttpEvent event) {
        event.onEvent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        rv_retailtrade.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_commodity.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        dialogBillRetailTradeRecyclerViewAdapter = new DialogBillRetailTradeRecyclerViewAdapter(BaseActivity.retailTradeHoldBillList, getActivity());
        rv_retailtrade.setAdapter(dialogBillRetailTradeRecyclerViewAdapter);
        dialogBillRetailTradeRecyclerViewAdapter.notifyDataSetChanged();
        dialogBillRetailTradeRecyclerViewAdapter.setOnItemListener(getOnItemListener());

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.deleteBill, R.id.continueRetail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.deleteBill: // 删除
                if (!isSelect) {
                    showToastMessage("请先选择要操作的零售单");
                } else {
                    isSelect = false;
                    BaseActivity.retailTradeHoldBillList.remove(isSelectNum);
                    //
                    dialogBillRetailTradeRecyclerViewAdapter = new DialogBillRetailTradeRecyclerViewAdapter(BaseActivity.retailTradeHoldBillList, getActivity());
                    rv_retailtrade.setAdapter(dialogBillRetailTradeRecyclerViewAdapter);
                    dialogBillRetailTradeRecyclerViewAdapter.notifyDataSetChanged();
                    //
                    dialogBillRecyclerViewAdapter = new DialogBillRecyclerViewAdapter(null, getActivity());
                    rv_commodity.setAdapter(dialogBillRecyclerViewAdapter);
                    dialogBillRecyclerViewAdapter.notifyDataSetChanged();
                    dialogBillRetailTradeRecyclerViewAdapter.setOnItemListener(getOnItemListener());
                }
                break;
            case R.id.continueRetail: // 继续结账
                if (!isSelect) {
                    showToastMessage("请先选择要操作的零售单");
                } else {
                    isSelect = false;
                    if (BaseActivity.retailTrade != null && BaseActivity.retailTrade.getListSlave1() != null && BaseActivity.retailTrade.getListSlave1().size() > 0) {
                        showToastMessage("收银界面存在未结零售单，请先处理");
                    } else {
                        BaseActivity.retailTrade = BaseActivity.retailTradeHoldBillList.get(isSelectNum);
                        BaseActivity.showCommList = BaseActivity.retailTrade.getCommodityListOfHeldBill();
                        BaseActivity.retailTradeHoldBillList.remove(isSelectNum);
                        //
                    }
                    Base1FragmentActivity activity = (Base1FragmentActivity) getActivity();
                    activity.sale_linear.performClick();
                }
                break;
        }
    }

    private DialogBillRetailTradeRecyclerViewAdapter.OnItemListener getOnItemListener() {
        return new DialogBillRetailTradeRecyclerViewAdapter.OnItemListener() {
            @Override
            public void onClick(DialogBillRetailTradeRecyclerViewAdapter.MyViewHolder holder, int position) {
                dialogBillRetailTradeRecyclerViewAdapter.setDefItem(position);
                dialogBillRetailTradeRecyclerViewAdapter.notifyDataSetChanged();
                if (BaseActivity.retailTradeHoldBillList.get(position).isSelect) {
                    isSelect = false;
                    //选中状态点击之后改为未选中
                    BaseActivity.retailTradeHoldBillList.get(position).isSelect = false;
                    dialogBillRetailTradeRecyclerViewAdapter.notifyDataSetChanged();

                    dialogBillRecyclerViewAdapter = new DialogBillRecyclerViewAdapter(null, getActivity());
                    rv_commodity.setAdapter(dialogBillRecyclerViewAdapter);
                    dialogBillRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    //未选中状态点击之后改为选中
                    isSelect = true;
                    BaseActivity.retailTradeHoldBillList.get(position).isSelect = true;
                    isSelectNum = position;

                    dialogBillRecyclerViewAdapter = new DialogBillRecyclerViewAdapter(BaseActivity.retailTradeHoldBillList.get(position).getCommodityListOfHeldBill(), getActivity());
                    rv_commodity.setAdapter(dialogBillRecyclerViewAdapter);
                    dialogBillRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
