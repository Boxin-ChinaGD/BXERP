//package com.bx.erp.view.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.tu.loadingdialog.LoadingDailog;
//import com.bx.erp.R;
//import com.bx.erp.helper.Constants;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import butterknife.ButterKnife;
//
//public class SetupRightFragment extends Fragment implements View.OnClickListener {
//    public static final String TAG = "SetupRightFragment";
//    private String left_item_name;
//    //
//    LinearLayout report_form;
//    LinearLayout cashier_setting;
//    LinearLayout inventory_setting;
//    LinearLayout printing_peripherals;
//    LinearLayout member;
//    LinearLayout store_information;
//    LinearLayout data_processing;
//    LinearLayout about;
//    //
//    public static LoadingDailog dialog;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        EventBus.getDefault().post(this);
//
//        View view = inflater.inflate(R.layout.set_up_right_layout, null);
//        report_form = view.findViewById(R.id.report_form);
//        cashier_setting = view.findViewById(R.id.cashier_setting);
//        inventory_setting = view.findViewById(R.id.inventory_setting);
//        printing_peripherals = view.findViewById(R.id.printing_peripherals);
//        member = view.findViewById(R.id.member);
//        store_information = view.findViewById(R.id.store_information);
//        data_processing = view.findViewById(R.id.data_processing);
//        about = view.findViewById(R.id.about);
//
//        //得到数据
//        initView();
//
//        return view;
//    }
//
//    private void initView() {
//        left_item_name = getArguments().getString(TAG);
//        if (left_item_name.equals("报表")) {
//            report_form.setVisibility(View.VISIBLE);
//            cashier_setting.setVisibility(View.GONE);
//            inventory_setting.setVisibility(View.GONE);
//            printing_peripherals.setVisibility(View.GONE);
//            member.setVisibility(View.GONE);
//            store_information.setVisibility(View.GONE);
//            data_processing.setVisibility(View.GONE);
//            about.setVisibility(View.GONE);
//        } else if (left_item_name.equals("收银设置")) {
//            report_form.setVisibility(View.GONE);
//            cashier_setting.setVisibility(View.VISIBLE);
//            inventory_setting.setVisibility(View.GONE);
//            printing_peripherals.setVisibility(View.GONE);
//            member.setVisibility(View.GONE);
//            store_information.setVisibility(View.GONE);
//            data_processing.setVisibility(View.GONE);
//            about.setVisibility(View.GONE);
//        } else if (left_item_name.equals("库存设置")) {
//            report_form.setVisibility(View.GONE);
//            cashier_setting.setVisibility(View.GONE);
//            inventory_setting.setVisibility(View.VISIBLE);
//            printing_peripherals.setVisibility(View.GONE);
//            member.setVisibility(View.GONE);
//            store_information.setVisibility(View.GONE);
//            data_processing.setVisibility(View.GONE);
//            about.setVisibility(View.GONE);
//        } else if (left_item_name.equals("打印与外设")) {
//            report_form.setVisibility(View.GONE);
//            cashier_setting.setVisibility(View.GONE);
//            inventory_setting.setVisibility(View.GONE);
//            printing_peripherals.setVisibility(View.VISIBLE);
//            member.setVisibility(View.GONE);
//            store_information.setVisibility(View.GONE);
//            data_processing.setVisibility(View.GONE);
//            about.setVisibility(View.GONE);
//            //
//            TextView printer = printing_peripherals.findViewById(R.id.printer);
//            TextView small_ticket = printing_peripherals.findViewById(R.id.small_ticket);
//            //
//            printer.setOnClickListener(this);
//            if(Constants.getCurrentStaff().getRoleID() == Constants.cashier_Role_ID) {
//                //如果是收银员，设置颜色
//                small_ticket.setTextColor(getResources().getColor(R.color.label));
//            }
//            small_ticket.setOnClickListener(this);
//        } else if (left_item_name.equals("会员")) {
//            report_form.setVisibility(View.GONE);
//            cashier_setting.setVisibility(View.GONE);
//            inventory_setting.setVisibility(View.GONE);
//            printing_peripherals.setVisibility(View.GONE);
//            member.setVisibility(View.VISIBLE);
//            store_information.setVisibility(View.GONE);
//            data_processing.setVisibility(View.GONE);
//            about.setVisibility(View.GONE);
//        } else if (left_item_name.equals("店铺信息")) {
//            report_form.setVisibility(View.GONE);
//            cashier_setting.setVisibility(View.GONE);
//            inventory_setting.setVisibility(View.GONE);
//            printing_peripherals.setVisibility(View.GONE);
//            member.setVisibility(View.GONE);
//            store_information.setVisibility(View.VISIBLE);
//            data_processing.setVisibility(View.GONE);
//            about.setVisibility(View.GONE);
//        } else if (left_item_name.equals("数据处理")) {
//            report_form.setVisibility(View.GONE);
//            cashier_setting.setVisibility(View.GONE);
//            inventory_setting.setVisibility(View.GONE);
//            printing_peripherals.setVisibility(View.GONE);
//            member.setVisibility(View.GONE);
//            store_information.setVisibility(View.GONE);
//            data_processing.setVisibility(View.VISIBLE);
//            about.setVisibility(View.GONE);
//
//            TextView syncBaseData = data_processing.findViewById(R.id.sync_base_data);
//            //
//            syncBaseData.setOnClickListener(this);
//        } else if (left_item_name.equals("关于")) {
//            report_form.setVisibility(View.GONE);
//            cashier_setting.setVisibility(View.GONE);
//            inventory_setting.setVisibility(View.GONE);
//            printing_peripherals.setVisibility(View.GONE);
//            member.setVisibility(View.GONE);
//            store_information.setVisibility(View.GONE);
//            data_processing.setVisibility(View.GONE);
//            about.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.printer:
//                Intent printerIntent = new Intent(getActivity(), PrinterActivity.class);
//                startActivity(printerIntent);
//                break;
//            case R.id.small_ticket:
//                if (Constants.getCurrentStaff().getRoleID() != Constants.cashier_Role_ID) {
//                    Intent smallSheetIntent = new Intent(getActivity(), SmallSheetActivity.class);
//                    startActivity(smallSheetIntent);
//                }else{
//                    Toast.makeText(getActivity(), "权限不足", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.sync_base_data:
//                Intent syncBaseDataIntent = new Intent(getActivity(), ResetBaseDataActivity.class);
//                startActivity(syncBaseDataIntent);
//                break;
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}