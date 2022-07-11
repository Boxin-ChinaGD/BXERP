//package com.bx.erp.view.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
//import android.view.Display;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.bx.erp.R;
//import com.bx.erp.utils.ScreenManager;
//import com.bx.erp.view.adapter.SetupLeftListviewAdapter;
//import com.bx.erp.view.presentation.CustomerCommodityListPresentation;
//import com.bx.erp.view.presentation.PaymentSuccessPresentation;
//import com.bx.erp.view.presentation.WelcomePresentation;
//
//import org.apache.log4j.Logger;
//
//public class SetupActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
//    private Logger log = Logger.getLogger(this.getClass());
//    //    private int[] set_up_left_image = {R.drawable.report_form, R.drawable.cashier_setting, R.drawable.inventory_setting, R.drawable.printing_peripherals, R.drawable.member, R.drawable.store_information, R.drawable.data_processing, R.drawable.about};
//    private int[] set_up_left_image = {R.drawable.printing_peripherals, R.drawable.data_processing};
//    //    private String[] set_up_left_str = {"报表", "收银设置", "库存设置", "打印与外设", "会员", "店铺信息", "数据处理", "关于"};
//    private String[] set_up_left_str = {"打印与外设", "数据处理"};
//    private ListView set_up_left_listview;
//    private SetupLeftListviewAdapter setupLeftListviewAdapter;
//    private SetupRightFragment setupRightFragment;
//    public static int mPosition;
//    TextView set_up_back;
//
//    private WelcomePresentation welcomePresentation = null;
//    private CustomerCommodityListPresentation customerCommodityListPresentation = null;
//    private PaymentSuccessPresentation paymentSuccessPresentation = null;
//    private ScreenManager screenManager = ScreenManager.getInstance();
//    public static boolean isVertical = false;
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.set_up);
//
//        initPresentationData(getApplicationContext());
//        isShowToCustomer(getApplicationContext());
//
//        set_up_back = (TextView) findViewById(R.id.set_up_back);
//        set_up_back.setOnClickListener(this);
//        initView();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.set_up_back:
//                Intent intent = new Intent(SetupActivity.this, MainActivity.class);
//                SetupActivity.this.finish();
//                startActivity(intent);
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void initView() {
//        set_up_left_listview = (ListView) findViewById(R.id.set_up_left);
//        setupLeftListviewAdapter = new SetupLeftListviewAdapter(this, set_up_left_image, set_up_left_str);
//        set_up_left_listview.setAdapter(setupLeftListviewAdapter);
//        set_up_left_listview.setOnItemClickListener(this);
//        //创建Fragment对象
//        setupRightFragment = new SetupRightFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.set_up_right, setupRightFragment);
//        //通过Bundle传值给setupRightFragment
//        Bundle bundle = new Bundle();
//        bundle.putString(SetupRightFragment.TAG, set_up_left_str[mPosition]);
//        setupRightFragment.setArguments(bundle);
//        fragmentTransaction.commit();
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        //拿到当前位置
//        mPosition = position;
//        //即时刷新adapter
//        setupLeftListviewAdapter.notifyDataSetChanged();
//        for (int i = 0; i < set_up_left_str.length; i++) {
//            setupRightFragment = new SetupRightFragment();
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.set_up_right, setupRightFragment);
//            Bundle bundle = new Bundle();
//            bundle.putString(SetupRightFragment.TAG, set_up_left_str[position]);
//            setupRightFragment.setArguments(bundle);
//            fragmentTransaction.commit();
//        }
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (BaseActivity.showCommList != null) {
//            if (BaseActivity.showCommList.size() > 0) {
//                if (customerCommodityListPresentation != null) {
//                    customerCommodityListPresentation.show();
//                }
//            } else {
//                if (welcomePresentation != null) {
//                    welcomePresentation.show();
//                }
//            }
//        } else {
//            if (welcomePresentation != null) {
//                welcomePresentation.show();
//            }
//        }
//    }
//
//    /**
//     * 判断商品数量和状态，选择哪个presentation展示给客户
//     */
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    protected void isShowToCustomer(Context context) {
//        initPresentationData(context);
//        if (BaseActivity.showCommList.size() > 0) {
//            if (customerCommodityListPresentation != null) {
//                customerCommodityListPresentation.show();
//            }
//        } else {
//            if (welcomePresentation != null) {
//                welcomePresentation.show();
//            }
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    protected void initPresentationData(Context context) {
//        screenManager.init(context);
//        Display[] displays = screenManager.getDisplays();
//        log.info("屏幕数量" + displays.length);
//        for (int i = 0; i < displays.length; i++) {
//            log.info("屏幕" + displays[i]);
//        }
//        Display display = screenManager.getPresentationDisplays();
//        if (display != null && !isVertical) {
//            welcomePresentation = new WelcomePresentation(this, display);
//            customerCommodityListPresentation = new CustomerCommodityListPresentation(this, display);
//            paymentSuccessPresentation = new PaymentSuccessPresentation(this, display);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        SetupActivity.this.finish();
//        startActivity(intent);
//    }
//}
