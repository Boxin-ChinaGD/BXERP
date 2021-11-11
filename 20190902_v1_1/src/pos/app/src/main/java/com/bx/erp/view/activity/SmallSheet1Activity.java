package com.bx.erp.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.R;
import com.bx.erp.bo.BaseHttpBO;
import com.bx.erp.bo.BaseSQLiteBO;
import com.bx.erp.bo.ConfigGeneralHttpBO;
import com.bx.erp.bo.ConfigGeneralSQLiteBO;
import com.bx.erp.bo.NtpHttpBO;
import com.bx.erp.bo.SmallSheetHttpBO;
import com.bx.erp.bo.SmallSheetSQLiteBO;
import com.bx.erp.common.GlobalController;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.ConfigGeneralHttpEvent;
import com.bx.erp.event.SmallSheetHttpEvent;
import com.bx.erp.event.UI.BaseSQLiteEvent;
import com.bx.erp.event.UI.ConfigGeneralSQLiteEvent;
import com.bx.erp.event.UI.SmallSheetSQLiteEvent;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ConfigGeneral;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.SmallSheetFrame;
import com.bx.erp.model.SmallSheetText;
import com.bx.erp.presenter.BasePresenter;
import com.bx.erp.presenter.ConfigGeneralPresenter;
import com.bx.erp.presenter.SmallSheetFramePresenter;
import com.bx.erp.presenter.SmallSheetTextPresenter;
import com.bx.erp.utils.GeneralUtil;
import com.bx.erp.utils.ImgUtil;
import com.bx.erp.utils.NetworkUtils;
import com.sunmi.printerhelper.utils.AidlUtil;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.bx.erp.bo.BaseSQLiteBO.CASE_ConfigGeneral_RetrieveNByConditions;
import static com.bx.erp.bo.BaseSQLiteBO.CASE_SmallSheetFrame_RetrieveNByConditions;
import static com.bx.erp.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done;
import static com.bx.erp.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_CreateMasterSlaveAsync_Done;
import static com.bx.erp.event.UI.BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_UpdateMasterSlaveAsync;

/**
 * Created by WPNA on 2020/2/29.
 */

public class SmallSheet1Activity extends BaseFragment1 implements View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {
    private static Logger log = Logger.getLogger(SmallSheet1Activity.class);
    public static final long Default_SmallSheetID_INPos = 1l; // 该值为默认的小票格式的ID，不可删除该值的小票格式。若为指定小票格式时默认使用该值的小票格式
    public static final int Default_SmallSheetListSlveSize = 20; // 默认小票格式的从表的size大小  去除了支付宝支付后默认的从表size大小就变成了20

    private final String defaultText = "";
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    final int CUT_BIG_OK = 1 << 4;
    private final int CUT_BIG_REQUEST = 1 << 6;
    @BindView(R.id.footer1_layout)
    RelativeLayout footer1Layout;
    @BindView(R.id.bottom_blank_line_layout)
    RelativeLayout bottomBlankLineLayout;
    @BindView(R.id.ticket_bottom_layout)
    RelativeLayout ticketBottomLayout;
    @BindView(R.id.fourth_box)
    LinearLayout fourthBox;
    @BindView(R.id.smallsheet_to_update)
    TextView smallsheetToUpdate;
    @BindView(R.id.cancel_update)
    TextView cancelUpdate;
    @BindView(R.id.Mask)
    View Mask;
    private Bitmap bmpFrameLogo;
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
    private File cameraFile;

    /**
     * 小票格式模板中可编辑位置的文本框的最大长度
     */
    private final static int MAX_Length_EditText = 30;
    /**
     * 文本框中内容居左时的重力
     */
    private final static int GRAVITY_LEFT = 51;
    /**
     * 文本框中内容居中时的重力
     */
    private final static int GRAVITY_CENTRE = 17;
    /**
     * 文本框中内容居右时的重力
     */
    private final static int GRAVITY_RIGHT = 53;


    //输入表情前的光标位置
    private int cursorPos;
    //输入表情前EditText中的文本
    private String inputAfterText;
    //是否重置了EditText的内容
    private boolean resetText;

    //输入表情前的光标位置
    private int designTicketBottom_cursorPos;
    //输入表情前EditText中的文本
    private String designTicketBottom_inputAfterText;
    //是否重置了EditText的内容
    private boolean designTicketBottom_resetText;

    //输入表情前的光标位置
    private int designFooter1_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter1_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter1_resetText;

    //输入表情前的光标位置
    private int designFooter2_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter2_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter2_resetText;

    //输入表情前的光标位置
    private int designFooter3_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter3_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter3_resetText;

    //输入表情前的光标位置
    private int designFooter4_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter4_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter4_resetText;

    //输入表情前的光标位置
    private int designFooter5_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter5_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter5_resetText;

    //输入表情前的光标位置
    private int designFooter6_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter6_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter6_resetText;

    //输入表情前的光标位置
    private int designFooter7_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter7_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter7_resetText;

    //输入表情前的光标位置
    private int designFooter8_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter8_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter8_resetText;

    //输入表情前的光标位置
    private int designFooter9_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter9_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter9_resetText;

    //输入表情前的光标位置
    private int designFooter10_cursorPos;
    //输入表情前EditText中的文本
    private String designFooter10_inputAfterText;
    //是否重置了EditText的内容
    private boolean designFooter10_resetText;
    //记录当前选择的小票格式的position ,为了在修改小票格式后，点击取消修改时，重新加载回原来的小票格式，仅此而已
    private int SelectPosition;

    @BindView(R.id.template_printer_size)
    TextView templatePrinterSize;
    @BindView(R.id.design_top_logo)
    RelativeLayout designTopLogo;
    @BindView(R.id.design_if_top_logo)
    TextView designIfTopLogo;
    @BindView(R.id.design_header)
    EditText designHeader;
    @BindView(R.id.design_footer1)
    EditText designFooter1;
    @BindView(R.id.design_footer2_layout)
    RelativeLayout designFooter2_layout;
    @BindView(R.id.designFooter2)
    EditText designFooter2;
    @BindView(R.id.design_delete_footer2)
    ImageView designDeleteFooter2;
    @BindView(R.id.design_footer2_bottom_view)
    View designFooter2BottomView;
    @BindView(R.id.design_footer3_layout)
    RelativeLayout designFooter3Layout;
    @BindView(R.id.design_footer3)
    EditText designFooter3;
    @BindView(R.id.design_delete_footer3)
    ImageView designDeleteFooter3;
    @BindView(R.id.design_footer3_bottom_view)
    View designFooter3BottomView;
    @BindView(R.id.design_footer4_layout)
    RelativeLayout designFooter4Layout;
    @BindView(R.id.design_footer4)
    EditText designFooter4;
    @BindView(R.id.design_delete_footer4)
    ImageView designDeleteFooter4;
    @BindView(R.id.design_footer4_bottom_view)
    View designFooter4BottomView;
    @BindView(R.id.design_footer5_layout)
    RelativeLayout designFooter5Layout;
    @BindView(R.id.design_footer5)
    EditText designFooter5;
    @BindView(R.id.design_delete_footer5)
    ImageView designDeleteFooter5;
    @BindView(R.id.design_footer5_bottom_view)
    View designFooter5BottomView;
    @BindView(R.id.design_footer6_layout)
    RelativeLayout designFooter6Layout;
    @BindView(R.id.design_footer6)
    EditText designFooter6;
    @BindView(R.id.design_delete_footer6)
    ImageView designDeleteFooter6;
    @BindView(R.id.design_footer6_bottom_view)
    View designFooter6BottomView;
    @BindView(R.id.design_footer7_layout)
    RelativeLayout designFooter7Layout;
    @BindView(R.id.design_footer7)
    EditText designFooter7;
    @BindView(R.id.design_delete_footer7)
    ImageView designDeleteFooter7;
    @BindView(R.id.design_footer7_bottom_view)
    View designFooter7BottomView;
    @BindView(R.id.design_footer8_layout)
    RelativeLayout designFooter8Layout;
    @BindView(R.id.design_footer8)
    EditText designFooter8;
    @BindView(R.id.design_delete_footer8)
    ImageView designDeleteFooter8;
    @BindView(R.id.design_footer8_bottom_view)
    View designFooter8BottomView;
    @BindView(R.id.design_footer9_layout)
    RelativeLayout designFooter9Layout;
    @BindView(R.id.design_footer9)
    EditText designFooter9;
    @BindView(R.id.design_delete_footer9)
    ImageView designDeleteFooter9;
    @BindView(R.id.design_footer9_bottom_view)
    View designFooter9BottomView;
    @BindView(R.id.design_footer10_layout)
    RelativeLayout designFooter10Layout;
    @BindView(R.id.design_footer10)
    EditText designFooter10;
    @BindView(R.id.design_delete_footer10)
    ImageView designDeleteFooter10;
    @BindView(R.id.design_footer10_bottom_view)
    View designFooter10BottomView;
    @BindView(R.id.design_bottom_blank_line)
    EditText designBottomBlankLine;
    @BindView(R.id.add_design_bottom_blank_line_number)
    TextView addDesignBottomBlankLineNumber;
    @BindView(R.id.reduce_design_bottom_blank_line_number)
    TextView reduceDesignBottomBlankLineNumber;
    @BindView(R.id.template_logo_layout)
    LinearLayout templateLogoLayout;
    @BindView(R.id.template_logo)
    ImageView templateLogo;
    @BindView(R.id.template_header)
    TextView templateHeader;
    @BindView(R.id.template_document_number)
    TextView templateDocumentNumber;
    @BindView(R.id.template_date)
    TextView templateDate;
    @BindView(R.id.template_payment_method1)
    TextView templatePaymentMethod1;
    //    @BindView(R.id.payment_method2)
//    TextView payment_method2;
    @BindView(R.id.template_payment_method3)
    TextView templatePaymentMethod3;
    @BindView(R.id.template_footer1)
    TextView templateFooter1;
    @BindView(R.id.template_footer2)
    TextView templateFooter2;
    @BindView(R.id.template_footer3)
    TextView templateFooter3;
    @BindView(R.id.template_footer4)
    TextView templateFooter4;
    @BindView(R.id.template_footer5)
    TextView templateFooter5;
    @BindView(R.id.template_footer6)
    TextView templateFooter6;
    @BindView(R.id.template_footer7)
    TextView templateFooter7;
    @BindView(R.id.template_footer8)
    TextView templateFooter8;
    @BindView(R.id.template_footer9)
    TextView templateFooter9;
    @BindView(R.id.template_footer10)
    TextView templateFooter10;
    @BindView(R.id.add_footer)
    ImageView add_footer;
    @BindView(R.id.text_size)
    LinearLayout controlTextSize;
    @BindView(R.id.show_control_text_size)
    TextView showControlTextSize;
    @BindView(R.id.control_bold)
    TextView controlBold;
    @BindView(R.id.control_keep_left)
    ImageView controlKeepLeft;
    @BindView(R.id.control_keep_center)
    ImageView controlKeepCenter;
    @BindView(R.id.control_keep_right)
    ImageView controlKeepRight;
    @BindView(R.id.template_goods_name)
    TextView templateGoodsName;
    @BindView(R.id.template_goods_number)
    TextView templateGoodsNumber;
    @BindView(R.id.template_subtotal)
    TextView templateSubtotal;
    @BindView(R.id.template_goods_name_layout)
    LinearLayout templateGoodsNameLayout;
    @BindView(R.id.template_goods_number_layout)
    LinearLayout templateGoodsNumberLayout;
    @BindView(R.id.template_subtotal_layout)
    LinearLayout templateSubtotalLayout;
    @BindView(R.id.template_first_goods_name)
    TextView templateFirstGoodsName;
    @BindView(R.id.template_second_goods_name)
    TextView templateSecondGoodsName;
    @BindView(R.id.template_third_goods_name)
    TextView templateThirdGoodsName;
    @BindView(R.id.template_first_goods_number)
    TextView templateFirstGoodsNumber;
    @BindView(R.id.template_second_goods_number)
    TextView templateSecondGoodsNumber;
    @BindView(R.id.template_third_goods_numebr)
    TextView templateThirdGoodsNumber;
    @BindView(R.id.template_first_subtotal)
    TextView templateFirstSubtotal;
    @BindView(R.id.template_second_subtoatl)
    TextView templateSecondSubtotal;
    @BindView(R.id.template_third_subtotal)
    TextView templateThirdSubtotal;
    @BindView(R.id.total_money)
    TextView templateTotalMoney;
    @BindView(R.id.payment_method)
    TextView templatePaymentMethod;
    @BindView(R.id.template_discont)
    TextView templateDiscount;
    @BindView(R.id.template_payable)
    TextView templatePayable;
    @BindView(R.id.template_ticket_bottom)
    TextView templateTicketBottom;
    @BindView(R.id.design_ticket_bottom)
    EditText designTicketBottom;
    @BindView(R.id.print_test)
    TextView print_test;
    @BindView(R.id.left_shift)
    ImageView left_shift;
    @BindView(R.id.right_shift)
    ImageView right_shift;
    @BindView(R.id.smallsheet_create)
    TextView smallsheet_create;
    @BindView(R.id.printFormatVersion)
    Spinner printFormatVersion;
    @BindView(R.id.smallsheet_update)
    TextView smallsheet_update;
    @BindView(R.id.smallsheet_delete)
    TextView smallsheet_delete;
    @BindView(R.id.loading_config_data)
    Button loadingConfigData;
    @BindView(R.id.create_confirm)
    TextView createConfirm;
    @BindView(R.id.create_cancle)
    TextView createCancle;
    @BindView(R.id.useCurrentFormat)
    Button useForm;
    @BindView(R.id.design_delimiterToRepeat)
    TextView designDelimiterToRepeat;
    @BindView(R.id.delimiter_diy1)
    TextView delimiterDiy1;
    @BindView(R.id.delimiter_diy2)
    TextView delimiterDiy2;
    @BindView(R.id.delimiter_diy3)
    TextView delimiterDiy3;
    @BindView(R.id.delimiter_diy4)
    TextView delimiterDiy4;
    @BindView(R.id.delimiter_diy5)
    TextView delimiterDiy5;
    @BindView(R.id.delimiter_diy6)
    TextView delimiterDiy6;
    Unbinder unbinder;

    ImageView selected_no_logo;
    ImageView selected_from_album;
    AlertDialog top_logo_dialog;

    /**
     * 小票格式的内容的行数。将来如果增加更多的行，这个数目也要增加
     */
    private final static int ROW_NO = 20;
    private int[] left_shift_blank_number = new int[ROW_NO];
    private int[] right_shift_blank_number = new int[ROW_NO];

    private ArrayAdapter<String> spinner_Adapter;//打印格式版本数组
    private List<SmallSheetText> smallSheetTextLists = new ArrayList<>();
    private List<Long> smallSheetFrame_IDList = new ArrayList<Long>();//保存数据库中所有的小票格式FrameID

    private Uri selectImg;
    private Long selectedFrameID;//当前展示的小票格式ID

    private SmallSheetFramePresenter smallSheetFramePresenter;
    private SmallSheetTextPresenter smallSheetTextPresenter;
    private static SmallSheetSQLiteBO smallSheetSQLiteBO = null;
    private static SmallSheetHttpBO smallSheetHttpBO = null;
    private static SmallSheetSQLiteEvent smallSheetSQLiteEvent = null;
    private static SmallSheetHttpEvent smallSheetHttpEvent = null;

    private ConfigGeneralPresenter configGeneralPresenter;
    private static ConfigGeneralSQLiteBO configGeneralSQLiteBO = null;
    private static ConfigGeneralHttpBO configGeneralHttpBO = null;
    private static ConfigGeneralSQLiteEvent configGeneralSQLiteEvent = null;
    private static ConfigGeneralHttpEvent configGeneralHttpEvent = null;

    private static NetworkUtils networkUtils = new NetworkUtils();
    /**
     * 小票格式中的页眉、页脚、底部的文本内容长度不能超过100。如果修改此值，则.xml中的控件的MaxLength属性也要同步修改
     */
    private static int MAX_LENGTH_Content = 100;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.smallsheet1, container, false);
        unbinder = ButterKnife.bind(this, view);
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            log.info("sunmi the sn:" + (String) get.invoke(c, "ro.serialno"));
            log.info("sunmi First four characters:"
                    + (String) ((String) get.invoke(c, "ro.serialno")).substring(0, 4));
        } catch (Exception e) {
            e.printStackTrace();
        }

        addListener();

        cameraFile = new File(Environment.getExternalStorageDirectory().getPath(), "logo.jpg");
        if (cameraFile.exists()) {
            cameraFile.delete();
        }
        for (int i = 0; i < 17; i++) {
            left_shift_blank_number[i] = 0;
            right_shift_blank_number[i] = 0;
        }

        smallSheetFramePresenter = GlobalController.getInstance().getSmallSheetFramePresenter();
        smallSheetTextPresenter = GlobalController.getInstance().getSmallSheetTextPresenter();
        initEventAndBO();

        initDesignHeaderFooterEtc();

        if (currentSmallSheetID == BaseSQLiteBO.INVALID_INT_ID) {
            currentSmallSheetID = retrieveCurrentSmallSheetIDFromConfig();
        }
        if (currentSmallSheetID != BaseSQLiteBO.INVALID_INT_ID) {
            initSmallSheetData(currentSmallSheetID);
        } else {
            Toast.makeText(getActivity(), "请同步小票格式！", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addListener() {
        designTopLogo.setOnClickListener(this);
        designHeader.setOnClickListener(this);
        add_footer.setOnClickListener(this);
        designDeleteFooter2.setOnClickListener(this);
        designDeleteFooter3.setOnClickListener(this);
        designDeleteFooter4.setOnClickListener(this);
        designDeleteFooter5.setOnClickListener(this);
        designDeleteFooter6.setOnClickListener(this);
        designDeleteFooter7.setOnClickListener(this);
        designDeleteFooter8.setOnClickListener(this);
        designDeleteFooter9.setOnClickListener(this);
        designDeleteFooter10.setOnClickListener(this);
        templateHeader.setOnClickListener(this);
        templateDocumentNumber.setOnClickListener(this);
        templateDate.setOnClickListener(this);
        templateGoodsName.setOnClickListener(this);
        templateGoodsNumber.setOnClickListener(this);
        templateSubtotal.setOnClickListener(this);
        templateGoodsNameLayout.setOnClickListener(this);
        templateGoodsNumberLayout.setOnClickListener(this);
        templateSubtotalLayout.setOnClickListener(this);
        templateTotalMoney.setOnClickListener(this);
        templatePaymentMethod.setOnClickListener(this);
        templateDiscount.setOnClickListener(this);
        templatePayable.setOnClickListener(this);
        templatePaymentMethod1.setOnClickListener(this);
//        payment_method2.setOnClickListener(this);
        templatePaymentMethod3.setOnClickListener(this);
        templateFooter1.setOnClickListener(this);
        templateFooter2.setOnClickListener(this);
        templateFooter3.setOnClickListener(this);
        templateFooter4.setOnClickListener(this);
        templateFooter5.setOnClickListener(this);
        templateFooter6.setOnClickListener(this);
        templateFooter7.setOnClickListener(this);
        templateFooter8.setOnClickListener(this);
        templateFooter9.setOnClickListener(this);
        templateFooter10.setOnClickListener(this);
        templateLogo.setOnClickListener(this);
        print_test.setOnClickListener(this);
        templateTicketBottom.setOnClickListener(this);
        left_shift.setOnClickListener(this);
        right_shift.setOnClickListener(this);
        smallsheet_create.setOnClickListener(this);
        smallsheet_update.setOnClickListener(this);
        smallsheet_delete.setOnClickListener(this);
        loadingConfigData.setOnClickListener(this);
        createConfirm.setOnClickListener(this);
        createCancle.setOnClickListener(this);
        printFormatVersion.setOnItemSelectedListener(this);
        useForm.setOnClickListener(this);
        designBottomBlankLine.setEnabled(false);//不出现光标
        designBottomBlankLine.setShowSoftInputOnFocus(false);//点击不出现系统键盘
        addDesignBottomBlankLineNumber.setOnClickListener(this);
        reduceDesignBottomBlankLineNumber.setOnClickListener(this);
        smallsheetToUpdate.setOnClickListener(this);
        cancelUpdate.setOnClickListener(this);
        Mask.setOnClickListener(this);
    }

    /**
     * 初始化Header、Footer等
     */
    private void initDesignHeaderFooterEtc() {
        designDelimiterToRepeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = designDelimiterToRepeat.getText().toString();
                try {
                    // 这个if判断主要是区分分隔符是英文字符还是中文字符还是图案，英文字符getBytes("utf-8").length = 1，getBytes("gbk").length == 1；中文字符getBytes("utf-8").length = 3，getBytes("gbk").length == 2；图案getBytes("utf-8").length = 3，getBytes("gbk").length == 1
                    if (designDelimiterToRepeat.getText().toString().getBytes("utf-8").length == 3 && designDelimiterToRepeat.getText().toString().getBytes("gbk").length == 1) {
                        designDelimiterToRepeat.setText("");
                        Toast.makeText(getActivity(), "分隔线不能是图案", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int j = 1; j <= 8; j++) {
                            str = str.concat(str);
                        }
                        delimiterDiy1.setText(str);
                        delimiterDiy2.setText(str);
                        delimiterDiy3.setText(str);
                        delimiterDiy4.setText(str);
                        delimiterDiy5.setText(str);
                        delimiterDiy6.setText(str);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        designHeader.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!resetText) {
                    cursorPos = designHeader.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(cursorPos, cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designHeader.setText(inputAfterText);
                                CharSequence text = designHeader.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                templateHeader.setVisibility(View.VISIBLE);
                                delimiterDiy2.setVisibility(View.VISIBLE);
                                if (!"".equals(designHeader.getText().toString())) {
                                    templateHeader.setText(designHeader.getText().toString());
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            // 创建完小票后，count应该是0(count代表输入的长度)，但是实际显示会是charSequence的长度
                            // charSequence.subSequence(cursorPos, cursorPos + count)会数组越界。这种不算非法输入，不用提示信息
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            designHeader.setText(inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        templateHeader.setVisibility(View.VISIBLE);
                        delimiterDiy2.setVisibility(View.VISIBLE);
                        if (!"".equals(designHeader.getText().toString())) {
                            templateHeader.setText(designHeader.getText().toString());
                        }
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if ("".equals(editable.toString())) {
                    templateHeader.setVisibility(View.GONE);
                    delimiterDiy2.setVisibility(View.GONE);
                    templateHeader.setText(editable);
                }
            }
        });
        designTicketBottom.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designTicketBottom_resetText) {
                    designTicketBottom_cursorPos = designTicketBottom.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designTicketBottom_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designTicketBottom_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designTicketBottom_cursorPos, designTicketBottom_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designTicketBottom_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designTicketBottom.setText(designTicketBottom_inputAfterText);
                                CharSequence text = designTicketBottom.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designTicketBottom.getText().toString())) {
                                    templateTicketBottom.setText(designTicketBottom.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            designTicketBottom.setText(designTicketBottom_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designTicketBottom.getText().toString())) {
                            templateTicketBottom.setText(designTicketBottom.getText().toString());
                        }
                    }
                } else {
                    designTicketBottom_resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter1_resetText) {
                    designFooter1_cursorPos = designFooter1.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter1_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter1_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter1_cursorPos, designFooter1_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter1_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter1.setText(designFooter1_inputAfterText);
                                CharSequence text = designFooter1.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter1.getText().toString())) {
                                    templateFooter1.setText(designFooter1.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            System.out.println("进入这里");
                            //      designFooter1.setText(designFooter1_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter1.getText().toString())) {
                            templateFooter1.setText(designFooter1.getText().toString());
                        }
                    }
                } else {
                    designFooter1_resetText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter2_resetText) {
                    designFooter2_cursorPos = designFooter2.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter2_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter2_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter2_cursorPos, designFooter2_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter2_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter2.setText(designFooter2_inputAfterText);
                                CharSequence text = designFooter1.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter2.getText().toString())) {
                                    templateFooter2.setText(designFooter2.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //  designFooter2.setText(designFooter2_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter2.getText().toString())) {
                            templateFooter2.setText(designFooter2.getText().toString());
                        }
                    }
                } else {
                    designFooter2_resetText = false;
                }
                setFooterTextChange(designFooter2, templateFooter2, designFooter2_layout, designFooter2BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter3.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter3_resetText) {
                    designFooter3_cursorPos = designFooter3.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter3_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter3_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter3_cursorPos, designFooter3_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter3_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter3.setText(designFooter3_inputAfterText);
                                CharSequence text = designFooter1.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter3.getText().toString())) {
                                    templateFooter3.setText(designFooter3.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //  designFooter3.setText(designFooter3_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter3.getText().toString())) {
                            templateFooter3.setText(designFooter3.getText().toString());
                        }
                    }
                } else {
                    designFooter3_resetText = false;
                }
                setFooterTextChange(designFooter3, templateFooter3, designFooter3Layout, designFooter3BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter4.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter4_resetText) {
                    designFooter4_cursorPos = designFooter4.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter4_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter4_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter4_cursorPos, designFooter4_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter4_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter4.setText(designFooter4_inputAfterText);
                                CharSequence text = designFooter4.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter4.getText().toString())) {
                                    templateFooter4.setText(designFooter4.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //   designFooter4.setText(designFooter4_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter4.getText().toString())) {
                            templateFooter4.setText(designFooter4.getText().toString());
                        }
                    }
                } else {
                    designFooter4_resetText = false;
                }
                setFooterTextChange(designFooter4, templateFooter4, designFooter4Layout, designFooter4BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter5.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter5_resetText) {
                    designFooter5_cursorPos = designFooter5.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter5_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter5_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter5_cursorPos, designFooter5_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter5_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter5.setText(designFooter5_inputAfterText);
                                CharSequence text = designFooter5.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter5.getText().toString())) {
                                    templateFooter5.setText(designFooter5.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //   designFooter5.setText(designFooter5_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter5.getText().toString())) {
                            templateFooter5.setText(designFooter5.getText().toString());
                        }
                    }
                } else {
                    designFooter5_resetText = false;
                }
                setFooterTextChange(designFooter5, templateFooter5, designFooter5Layout, designFooter5BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter6.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter6_resetText) {
                    designFooter6_cursorPos = designFooter6.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter6_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter6_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter6_cursorPos, designFooter6_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter6_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter6.setText(designFooter6_inputAfterText);
                                CharSequence text = designFooter6.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter6.getText().toString())) {
                                    templateFooter6.setText(designFooter6.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //      designFooter6.setText(designFooter6_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter6.getText().toString())) {
                            templateFooter6.setText(designFooter6.getText().toString());
                        }
                    }
                } else {
                    designFooter6_resetText = false;
                }
                setFooterTextChange(designFooter6, templateFooter6, designFooter6Layout, designFooter6BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter7.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter7_resetText) {
                    designFooter7_cursorPos = designFooter7.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter7_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter7_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter7_cursorPos, designFooter7_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter7_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter7.setText(designFooter7_inputAfterText);
                                CharSequence text = designFooter7.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter7.getText().toString())) {
                                    templateFooter7.setText(designFooter7.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //     designFooter7.setText(designFooter7_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter7.getText().toString())) {
                            templateFooter7.setText(designFooter7.getText().toString());
                        }
                    }
                } else {
                    designFooter7_resetText = false;
                }
                setFooterTextChange(designFooter7, templateFooter7, designFooter7Layout, designFooter7BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter8.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter8_resetText) {
                    designFooter8_cursorPos = designFooter8.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter8_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter8_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter8_cursorPos, designFooter8_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter8_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter8.setText(designFooter8_inputAfterText);
                                CharSequence text = designFooter8.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter8.getText().toString())) {
                                    templateFooter4.setText(designFooter8.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //    designFooter8.setText(designFooter8_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter8.getText().toString())) {
                            templateFooter8.setText(designFooter8.getText().toString());
                        }
                    }
                } else {
                    designFooter8_resetText = false;
                }
                setFooterTextChange(designFooter8, templateFooter8, designFooter8Layout, designFooter8BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter9.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter9_resetText) {
                    designFooter9_cursorPos = designFooter9.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter9_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter9_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter9_cursorPos, designFooter9_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter9_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter9.setText(designFooter9_inputAfterText);
                                CharSequence text = designFooter9.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter9.getText().toString())) {
                                    templateFooter9.setText(designFooter9.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //    designFooter9.setText(designFooter9_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter9.getText().toString())) {
                            templateFooter9.setText(designFooter9.getText().toString());
                        }
                    }
                } else {
                    designFooter9_resetText = false;
                }
                setFooterTextChange(designFooter9, templateFooter9, designFooter9Layout, designFooter9BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        designFooter10.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!designFooter10_resetText) {
                    designFooter10_cursorPos = designFooter10.getSelectionEnd();
                    // 这里用charSequence.toString()而不直接用charSequence是因为如果用charSequence，
                    // 那么，inputAfterText和charSequence在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    designFooter10_inputAfterText = charSequence.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (!designFooter10_resetText) {
                    if (count >= 2) {//表情符号的字符长度最小为2
                        try {
                            CharSequence input = charSequence.subSequence(designFooter10_cursorPos, designFooter10_cursorPos + count);
                            if (containsEmoji(input.toString()) || isSpecialEmoji(input)) {
                                designFooter10_resetText = true;
                                Toast.makeText(getActivity(), "不支持输入Emoji表情符号", Toast.LENGTH_SHORT).show();
                                //是表情符号就将文本还原为输入表情符号之前的内容
                                designFooter10.setText(designFooter10_inputAfterText);
                                CharSequence text = designFooter10.getText();
                                if (text instanceof Spannable) {
                                    Spannable spanText = (Spannable) text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            } else { // 没有包含表情包
                                if (!"".equals(designFooter10.getText().toString())) {
                                    templateFooter10.setText(designFooter10.getText().toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "非法输入", Toast.LENGTH_SHORT).show();
                            //      designFooter10.setText(designFooter10_inputAfterText);
                            return;
                        }
                    } else { // 输入不是表情包或删除字符
                        if (!"".equals(designFooter10.getText().toString())) {
                            templateFooter10.setText(designFooter10.getText().toString());
                        }
                    }
                } else {
                    designFooter10_resetText = false;
                }
                setFooterTextChange(designFooter10, templateFooter10, designFooter10Layout, designFooter10BottomView);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initEventAndBO() {
        if (smallSheetSQLiteEvent == null) {
            smallSheetSQLiteEvent = new SmallSheetSQLiteEvent();
            smallSheetSQLiteEvent.setId(BaseEvent.EVENT_ID_SmallSheetActivity);
        }
        if (smallSheetHttpEvent == null) {
            smallSheetHttpEvent = new SmallSheetHttpEvent();
            smallSheetHttpEvent.setId(BaseEvent.EVENT_ID_SmallSheetActivity);
        }
        if (smallSheetSQLiteBO == null) {
            smallSheetSQLiteBO = new SmallSheetSQLiteBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        if (smallSheetHttpBO == null) {
            smallSheetHttpBO = new SmallSheetHttpBO(GlobalController.getInstance().getContext(), smallSheetSQLiteEvent, smallSheetHttpEvent);
        }
        smallSheetSQLiteEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetSQLiteEvent.setHttpBO(smallSheetHttpBO);
        smallSheetHttpEvent.setSqliteBO(smallSheetSQLiteBO);
        smallSheetHttpEvent.setHttpBO(smallSheetHttpBO);

        configGeneralPresenter = GlobalController.getInstance().getConfigGeneralPresenter();
        if (configGeneralSQLiteEvent == null) {
            configGeneralSQLiteEvent = new ConfigGeneralSQLiteEvent();
            configGeneralSQLiteEvent.setId(BaseEvent.EVENT_ID_SmallSheetActivity);
        }
        if (configGeneralHttpEvent == null) {
            configGeneralHttpEvent = new ConfigGeneralHttpEvent();
            configGeneralHttpEvent.setId(BaseEvent.EVENT_ID_SmallSheetActivity);
        }
        if (configGeneralSQLiteBO == null) {
            configGeneralSQLiteBO = new ConfigGeneralSQLiteBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        if (configGeneralHttpBO == null) {
            configGeneralHttpBO = new ConfigGeneralHttpBO(GlobalController.getInstance().getContext(), configGeneralSQLiteEvent, configGeneralHttpEvent);
        }
        configGeneralSQLiteEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
        configGeneralHttpEvent.setSqliteBO(configGeneralSQLiteBO);
        configGeneralHttpEvent.setHttpBO(configGeneralHttpBO);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetSQLiteEvent(SmallSheetSQLiteEvent event) {
        System.out.println("ssssssssssssssssssssssonSmallSheetSQLiteEvent");
        if (event.getId() == BaseEvent.EVENT_ID_SmallSheetActivity) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
                if (event.getStatus() == BaseEvent.EnumEventStatus.EES_SQLite_DoneApplyServerData) {
                    onDoneApplyServerDataSuccess(event);
                } else {
                    log.error("联网情况下未处理的情况，event=" + event.toString());
                }
            } else if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                if (event.getEventTypeSQLite() == ESET_SmallSheet_CreateMasterSlaveAsync_Done) {
                    closeLoadingDialog(loadingDailog);
                    selectedFrameID = Long.valueOf(event.getBaseModel1().getID());
                    log.info("创建在本地，无法上传到服务器");
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } else if (event.getEventTypeSQLite() == ESET_SmallSheet_UpdateMasterSlaveAsync) {
                    closeLoadingDialog(loadingDailog);
                    Message message = new Message();
                    message.what = 3;
                    message.obj = "网络连接错误，已更新本地小票格式";
                    handler.sendMessage(message);
                    log.info("已更新本地，无法上传到服务器");
                } else if (event.getEventTypeSQLite() == ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done) {
                    onBeforeDeleteServerDone(event);
                } else {
                    log.error("断网情况下未处理的情况，event=" + event.toString());
                }
            } else {
                log.error("未预料到的情况！event=" + event.toString());
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    private void onBeforeDeleteServerDone(SmallSheetSQLiteEvent event) {
        closeLoadingDialog(loadingDailog);
        log.info("在本地添加删除标记，为真正删除");
        //
        ConfigGeneral configGeneral = new ConfigGeneral();
        configGeneral.setSql("where F_Name = ?");
        configGeneral.setConditions(new String[]{"ACTIVE_SMALLSHEET_ID"});
        List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
        configGeneral = configGeneralList.get(0); //第0张必然是系统默认使用的那张
        if (configGeneral != null) {
            if (configGeneral.getValue().equals(String.valueOf(event.getBaseModel1().getID()))) {
                configGeneral.setValue(String.valueOf(Default_SmallSheetID_INPos));
                configGeneral.setReturnObject(1); //...
                configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
//                            configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
                if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral)) {
                    log.error("更新默认小票格式失败！configGeneral=" + configGeneral);
//                                Toast.makeText(appApplication, "更新默认小票格式失败！", Toast.LENGTH_SHORT).show();
//                                closeLoadingDialog(loadingDailog);//运行在非UI线程
                }
                currentSmallSheetID = Integer.valueOf(String.valueOf(Default_SmallSheetID_INPos));// 更新当前小票格式。不管上面的configGeneralSQLiteBO.updateAsync()成功还是失败
            }
        }
        Message message = new Message();
        message.what = 6;
        handler.sendMessage(message);
    }

    private void onDoneApplyServerDataSuccess(SmallSheetSQLiteEvent event) {
        if (BasePresenter.SYNC_Type_U.equals(smallSheetHttpEvent.getSyncType())) {
            closeLoadingDialog(loadingDailog);
//                    bmpFrameLogo = null;
            smallSheetHttpEvent.setSyncType("");
//                    Toast.makeText(appApplication, "成功修改小票格式", Toast.LENGTH_SHORT).show();
            Message message = new Message();
            message.what = 4;
            handler.sendMessage(message);
        } else if (BasePresenter.SYNC_Type_C.equals(smallSheetHttpEvent.getSyncType())) {
            closeLoadingDialog(loadingDailog);
            bmpFrameLogo = null;
            smallSheetHttpEvent.setSyncType("");
            selectedFrameID = Long.valueOf(event.getBaseModel1().getID());
            //初始化下拉框选择小票版本
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        } else if (BasePresenter.SYNC_Type_D.equals(smallSheetHttpEvent.getSyncType())) {
            bmpFrameLogo = null;
            smallSheetHttpEvent.setSyncType("");

            ConfigGeneral configGeneral = new ConfigGeneral();
            configGeneral.setSql("where F_Name = ?");
            configGeneral.setConditions(new String[]{"ACTIVE_SMALLSHEET_ID"});
            List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
            configGeneral = configGeneralList.get(0);
            if (configGeneral != null) {
                if (configGeneral.getValue().equals(String.valueOf(event.getBaseModel1().getID()))) {
                    configGeneral.setValue(String.valueOf(Default_SmallSheetID_INPos));
                    configGeneral.setReturnObject(1); //...
                    configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
//                            configGeneralSQLiteEvent.setHttpBO(configGeneralHttpBO);
                    if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneral)) {
                        log.error("更新默认小票格式失败！configGeneral=" + configGeneral);
//                                Toast.makeText(appApplication, "update ConfigGeneral失败！", Toast.LENGTH_SHORT).show();
//                                closeLoadingDialog(loadingDailog);
                    }
                    currentSmallSheetID = Integer.valueOf(String.valueOf(Default_SmallSheetID_INPos));// 更新当前小票格式。不管上面的configGeneralSQLiteBO.updateAsync()成功还是失败

                    return;
                }
            }

            //删除之后选择最新的小票格式
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        } else if (event.getEventTypeSQLite() == BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsyncC_Done) {
            closeLoadingDialog(loadingDailog);
            //初始化下拉框选择小票版本
            Message message = new Message();
            message.what = 5;
            handler.sendMessage(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSmallSheetHttpEvent(SmallSheetHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SmallSheetActivity) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {

            } else if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_WrongFormatForInputField) {
                Message message = new Message();
                message.what = 3;
                message.obj = "小票格式数目超过上限，无法创建，请先进行同步。";
                handler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = 3;
                message.obj = "网络连接错误，你的操作已在本地生效";
                handler.sendMessage(message);
            }
        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigGeneralSQLiteEvent(ConfigGeneralSQLiteEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SmallSheetActivity) {
            event.onEvent();
            if ("UPDATE_DONE".equals(event.getData())) { // TODO
                closeLoadingDialog(loadingDailog);
                event.setData("");
                if (currentSmallSheetID != BaseSQLiteBO.INVALID_ID) {
                    Toast.makeText(getActivity(), "设置当前使用小票格式成功", Toast.LENGTH_SHORT).show();
                }

                if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_InvalidSession) {
                    Toast.makeText(getActivity(), "网络不可用，请在网络恢复后重试。", Toast.LENGTH_SHORT).show();
                } else {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfigGeneralHttpEvent(ConfigGeneralHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_SmallSheetActivity) {
            event.onEvent();
            if (event.getLastErrorCode() == ErrorInfo.EnumErrorCode.EC_NoError) {
            } else {
                closeLoadingDialog(loadingDailog);
                Toast.makeText(getActivity(), event.getLastErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            log.info("未处理的Event，ID=" + event.getId());
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.design_top_logo:
                onClickSelectLogo();
                break;
            case R.id.add_footer:
                if (designFooter2_layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter2_layout, designFooter2BottomView, designFooter2);
                    setAddFooterView();
                } else if (designFooter3Layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter3Layout, designFooter3BottomView, designFooter3);
                    setAddFooterView();
                } else if (designFooter4Layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter4Layout, designFooter4BottomView, designFooter4);
                    setAddFooterView();
                } else if (designFooter5Layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter5Layout, designFooter5BottomView, designFooter5);
                    setAddFooterView();
                } else if (designFooter6Layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter6Layout, designFooter6BottomView, designFooter6);
                    setAddFooterView();
                } else if (designFooter7Layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter7Layout, designFooter7BottomView, designFooter7);
                    setAddFooterView();
                } else if (designFooter8Layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter8Layout, designFooter8BottomView, designFooter8);
                    setAddFooterView();
                } else if (designFooter9Layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter9Layout, designFooter9BottomView, designFooter9);
                    setAddFooterView();
                } else if (designFooter10Layout.getVisibility() == View.GONE) {
                    setFooterLayoutIsVisible(designFooter10Layout, designFooter10BottomView, designFooter10);
                    setAddFooterView();
                }
                break;
            case R.id.design_delete_footer2:
                delete_footer(templateFooter2, designFooter2, designFooter2_layout, designFooter2BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.design_delete_footer3:
                delete_footer(templateFooter3, designFooter3, designFooter3Layout, designFooter3BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.design_delete_footer4:
                delete_footer(templateFooter4, designFooter4, designFooter4Layout, designFooter4BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.design_delete_footer5:
                delete_footer(templateFooter5, designFooter5, designFooter5Layout, designFooter5BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.design_delete_footer6:
                delete_footer(templateFooter6, designFooter6, designFooter6Layout, designFooter6BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.design_delete_footer7:
                delete_footer(templateFooter7, designFooter7, designFooter7Layout, designFooter7BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.design_delete_footer8:
                delete_footer(templateFooter8, designFooter8, designFooter8Layout, designFooter8BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.design_delete_footer9:
                delete_footer(templateFooter9, designFooter9, designFooter9Layout, designFooter9BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.design_delete_footer10:
                delete_footer(templateFooter10, designFooter10, designFooter10Layout, designFooter10BottomView);
                add_footer.setVisibility(View.VISIBLE);
                break;
            case R.id.template_header:
                setTextBackgroudColor();
                templateHeader.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateHeader.setTextColor(Color.WHITE);
                setBoldButtonColor(templateHeader);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateHeader);
                    }
                });
                setTextGravity(templateHeader);
                break;
            case R.id.template_document_number:
                setTextBackgroudColor();
                templateDocumentNumber.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateDocumentNumber.setTextColor(Color.WHITE);
                setBoldButtonColor(templateDocumentNumber);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateDocumentNumber);
                    }
                });
                setTextGravity(templateDocumentNumber);
                break;
            case R.id.template_date:
                setTextBackgroudColor();
                templateDate.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateDate.setTextColor(Color.WHITE);
                setBoldButtonColor(templateDate);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateDate);
                    }
                });
                setTextGravity(templateDate);
                break;
            //
            case R.id.template_goods_name:
                selectGoodsListLayout(templateGoodsName, null);
                break;
            case R.id.template_goods_number:
                selectGoodsListLayout(templateGoodsNumber, null);
                break;
            case R.id.template_subtotal:
                selectGoodsListLayout(templateSubtotal, null);
                break;
            case R.id.template_goods_name_layout:
                selectGoodsListLayout(templateFirstGoodsName, templateGoodsNameLayout);
                break;
            case R.id.template_goods_number_layout:
                selectGoodsListLayout(templateFirstGoodsNumber, templateGoodsNumberLayout);
                break;
            case R.id.template_subtotal_layout:
                selectGoodsListLayout(templateFirstSubtotal, templateSubtotalLayout);
                break;
            //
            case R.id.total_money:
                setTextBackgroudColor();
                templateTotalMoney.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateTotalMoney.setTextColor(Color.WHITE);
                setBoldButtonColor(templateTotalMoney);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateTotalMoney);
                    }
                });
                setTextGravity(templateTotalMoney);
                break;
            case R.id.payment_method:
                setTextBackgroudColor();
                templatePaymentMethod.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templatePaymentMethod.setTextColor(Color.WHITE);
                setBoldButtonColor(templatePaymentMethod);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templatePaymentMethod);
                    }
                });
                setTextGravity(templatePaymentMethod);
                break;
            case R.id.template_discont:
                setTextBackgroudColor();
                templateDiscount.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateDiscount.setTextColor(Color.WHITE);
                setBoldButtonColor(templateDiscount);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateDiscount);
                    }
                });
                setTextGravity(templateDiscount);
                break;
            case R.id.template_payable:
                setTextBackgroudColor();
                templatePayable.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templatePayable.setTextColor(Color.WHITE);
                setBoldButtonColor(templatePayable);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templatePayable);
                    }
                });
                setTextGravity(templatePayable);
                break;
            case R.id.template_payment_method1:
                setTextBackgroudColor();
                templatePaymentMethod1.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templatePaymentMethod1.setTextColor(Color.WHITE);
                setBoldButtonColor(templatePaymentMethod1);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templatePaymentMethod1);
                    }
                });
                setTextGravity(templatePaymentMethod1);
                break;
//            case R.id.payment_method2:
//                setTextBackgroudColor();
//                payment_method2.setBackgroundColor(Color.RED);
//                setBoldButtonColor(payment_method2);
//                controlBold.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        clickBold(payment_method2);
//                    }
//                });
//                setTextGravity(payment_method2);
//                break;
            case R.id.template_payment_method3:
                setTextBackgroudColor();
                templatePaymentMethod3.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templatePaymentMethod3.setTextColor(Color.WHITE);
                setBoldButtonColor(templatePaymentMethod3);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templatePaymentMethod3);
                    }
                });
                setTextGravity(templatePaymentMethod3);
                break;
            case R.id.template_footer1:
                setTextBackgroudColor();
                templateFooter1.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter1.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter1);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter1);
                    }
                });
                setTextGravity(templateFooter1);
                break;
            case R.id.template_footer2:
                setTextBackgroudColor();
                templateFooter2.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter2.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter2);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter2);
                    }
                });
                setTextGravity(templateFooter2);
                break;
            case R.id.template_footer3:
                setTextBackgroudColor();
                templateFooter3.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter3.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter3);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter3);
                    }
                });
                setTextGravity(templateFooter3);
                break;
            case R.id.template_footer4:
                setTextBackgroudColor();
                templateFooter4.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter4.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter4);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter4);
                    }
                });
                setTextGravity(templateFooter4);
                break;
            case R.id.template_footer5:
                setTextBackgroudColor();
                templateFooter5.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter5.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter5);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter5);
                    }
                });
                setTextGravity(templateFooter5);
                break;
            case R.id.template_footer6:
                setTextBackgroudColor();
                templateFooter6.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter6.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter6);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter6);
                    }
                });
                setTextGravity(templateFooter6);
                break;
            case R.id.template_footer7:
                setTextBackgroudColor();
                templateFooter7.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter7.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter7);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter7);
                    }
                });
                setTextGravity(templateFooter7);
                break;
            case R.id.template_footer8:
                setTextBackgroudColor();
                templateFooter8.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter8.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter8);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter8);
                    }
                });
                setTextGravity(templateFooter8);
                break;
            case R.id.template_footer9:
                setTextBackgroudColor();
                templateFooter9.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter9.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter9);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter9);
                    }
                });
                setTextGravity(templateFooter9);
                break;
            case R.id.template_footer10:
                setTextBackgroudColor();
                templateFooter10.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateFooter10.setTextColor(Color.WHITE);
                setBoldButtonColor(templateFooter10);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateFooter10);
                    }
                });
                setTextGravity(templateFooter10);
                break;
            case R.id.template_logo:
                setTextBackgroudColor();
                templateLogo.setBackgroundColor(Color.RED);
                controlKeepLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controlKeepLeft.setImageResource(R.drawable.left_align_click);
                        controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
                        controlKeepRight.setImageResource(R.drawable.right_align_unclick);
                        templateLogoLayout.setGravity(Gravity.LEFT);
                    }
                });
                controlKeepCenter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
                        controlKeepCenter.setImageResource(R.drawable.center_align_click);
                        controlKeepRight.setImageResource(R.drawable.right_align_unclick);
                        templateLogoLayout.setGravity(Gravity.CENTER);
                    }
                });
                controlKeepRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
                        controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
                        controlKeepRight.setImageResource(R.drawable.right_align_click);
                        templateLogoLayout.setGravity(Gravity.RIGHT);
                    }
                });
                break;
            case R.id.template_ticket_bottom:
                setTextBackgroudColor();
                templateTicketBottom.setBackgroundColor(getResources().getColor(R.color.blue, null));
                templateTicketBottom.setTextColor(Color.WHITE);
                setBoldButtonColor(templateTicketBottom);
                controlBold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickBold(templateTicketBottom);
                    }
                });
                setTextGravity(templateTicketBottom);
                break;
            case R.id.print_test:
                onPrinterTest();
                break;
            case R.id.smallsheet_create:
                SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
                smallSheetFrame.setSql("Where F_SyncType != ?");
                smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
                List<SmallSheetFrame> ssfList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
                if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
                    log.info("查找本地的小票格式失败！！");
                    break;
                }
                log.info("当前小票格式的数量有：" + ssfList.size());
                if (ssfList.size() >= Integer.valueOf(ConfigGeneral.Config_SmallSheetNumber_Value)) {
                    Toast.makeText(getActivity(), "超过能存在小票格式的数量！！！", Toast.LENGTH_SHORT).show();
                    log.info("超过能存在小票格式的数量！！！");
                    break;
                }
                //初始化一张空白的小票格式编辑界面，修改按钮UI（隐藏“新建”，“保存修改”，“删除”按钮，展示“确认”，“取消”按钮）
                initRightContent();
                smallsheet_create.setVisibility(View.GONE);
                smallsheet_update.setVisibility(View.GONE);
                smallsheet_delete.setVisibility(View.GONE);
                createConfirm.setVisibility(View.VISIBLE);
                createCancle.setVisibility(View.VISIBLE);
                useForm.setVisibility(View.GONE);
                loadingConfigData.setVisibility(View.GONE);//隐藏同步按钮
                smallsheetToUpdate.setVisibility(View.GONE);//隐藏修改格式按钮
                Mask.setVisibility(View.GONE);//隐藏蒙版
                //
                initRightContent();

                break;
            case R.id.smallsheet_update://保存修改按钮
                onUpdateSmallSheetInUI();
                break;
            case R.id.smallsheet_delete:
                onDeleteSmallSheetInUI();
                break;
            case R.id.loading_config_data:
                HideVirtualKeyBoard();
                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_General);
                // 重置小票格式
                SmallSheetFrame resetSmallSheetFrame = new SmallSheetFrame();
                resetSmallSheetFrame.setPageIndex(BaseHttpBO.FIRST_PAGE_Index_Default);
                resetSmallSheetFrame.setPageSize(BaseHttpBO.PAGE_SIZE_LoadPageByPage);
                smallSheetSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_SmallSheet_RefreshByServerDataAsync_Done);
                smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToApplyServerData);
                if (!smallSheetHttpBO.retrieveNAsyncC(BaseHttpBO.INVALID_CASE_ID, resetSmallSheetFrame)) {
                    closeLoadingDialog(loadingDailog);
                    Toast.makeText(getActivity(), "网络故障！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.create_confirm:
                onConfirmCreateInUI();
                break;
            case R.id.create_cancle://点击“新建”按钮后的“取消”按钮
                //修改按钮UI（展示“新建”，“保存修改”，“删除”按钮，隐藏“确认”，“取消”按钮）
                smallsheet_create.setVisibility(View.VISIBLE);
                smallsheet_update.setVisibility(View.GONE);
                smallsheet_delete.setVisibility(View.VISIBLE);
                useForm.setVisibility(View.VISIBLE);
                createConfirm.setVisibility(View.GONE);
                createCancle.setVisibility(View.GONE);
                smallsheetToUpdate.setVisibility(View.VISIBLE);//显示修改格式按钮
                loadingConfigData.setVisibility(View.VISIBLE);//显示同步按钮
                Mask.setVisibility(View.VISIBLE);//显示蒙版
                //
                if (selectedFrameID != null) {
                    initSmallSheetData(Integer.valueOf(String.valueOf(selectedFrameID)));
                }
                break;
            case R.id.useCurrentFormat:
                // 检查网络是否连接正常
                if ((GlobalController.getInstance().getSessionID() == null || !networkUtils.isNetworkAvalible(getActivity().getApplicationContext()))) {
                    Toast.makeText(getActivity(), "网络连接不可用，不能进行小票格式设置。", Toast.LENGTH_SHORT).show();
                    GlobalController.getInstance().setSessionID(null);
                    break;
                }
                ConfigGeneral configGeneral = new ConfigGeneral();
                configGeneral.setSql("where F_Name = ?");
                configGeneral.setConditions(new String[]{"ACTIVE_SMALLSHEET_ID"});
                List<ConfigGeneral> configGeneralList = (List<ConfigGeneral>) configGeneralPresenter.retrieveNObjectSync(CASE_ConfigGeneral_RetrieveNByConditions, configGeneral);
                configGeneralList.get(0).setReturnObject(1);//...
                configGeneralList.get(0).setValue(String.valueOf(selectedFrameID));
                configGeneralSQLiteEvent.setEventTypeSQLite(BaseSQLiteEvent.EnumSQLiteEventType.ESET_ConfigGeneral_UpdateAsync);
                if (!configGeneralSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, configGeneralList.get(0))) {
                    Toast.makeText(getActivity(), "设置默认小票格式失败！", Toast.LENGTH_SHORT).show();
                } else {
                    currentSmallSheetID = selectedFrameID.intValue();// 更新当前小票格式
                }

                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_SettingSmallSheetConfigGeneral);
                break;
            case R.id.add_design_bottom_blank_line_number:
                if (designBottomBlankLine.getText().toString().equals("5")) {
                    Toast.makeText(getActivity(), SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom, Toast.LENGTH_SHORT).show();
                } else {
                    designBottomBlankLine.setText(String.valueOf(Integer.valueOf(designBottomBlankLine.getText().toString()) + 1));
                }
                break;
            case R.id.reduce_design_bottom_blank_line_number:
                if (designBottomBlankLine.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), SmallSheetFrame.FIELD_ERROR_countOfBlankLineAtBottom, Toast.LENGTH_SHORT).show();
                } else {
                    designBottomBlankLine.setText(String.valueOf(Integer.valueOf(designBottomBlankLine.getText().toString()) - 1));
                }
                break;
            case R.id.smallsheet_to_update://修改格式按钮
                smallsheet_update.setVisibility(View.VISIBLE);//显示保存修改按钮
                cancelUpdate.setVisibility(View.VISIBLE);//显示取消修改按钮
                smallsheet_create.setVisibility(View.GONE);//隐藏新建格式按钮
                loadingConfigData.setVisibility(View.GONE);//隐藏同步按钮
                useForm.setVisibility(View.GONE);//隐藏使用该格式按钮
                Mask.setVisibility(View.GONE);//隐藏蒙版
                break;
            case R.id.cancel_update://取消修改按钮
                smallsheet_update.setVisibility(View.GONE);//隐藏保存修改按钮
                cancelUpdate.setVisibility(View.GONE);//隐藏取消修改按钮
                smallsheet_create.setVisibility(View.VISIBLE);//显示新建格式按钮
                loadingConfigData.setVisibility(View.VISIBLE);//显示同步按钮
                Mask.setVisibility(View.VISIBLE);//显示蒙版
                useForm.setVisibility(View.VISIBLE);//显示使用该格式按钮
                showSelectedSmallSheet(SelectPosition);

            default:
                break;
        }
    }

    private void onConfirmCreateInUI() {
        loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_CreatingSmallSheet);

        //修改按钮UI（展示“新建”，“保存修改”，“删除”按钮，隐藏“确认”，“取消”按钮）
        smallsheet_create.setVisibility(View.VISIBLE);
        smallsheet_update.setVisibility(View.GONE);
        smallsheet_delete.setVisibility(View.VISIBLE);
        useForm.setVisibility(View.VISIBLE);
        createConfirm.setVisibility(View.GONE);
        createCancle.setVisibility(View.GONE);
        smallsheetToUpdate.setVisibility(View.VISIBLE);
        loadingConfigData.setVisibility(View.VISIBLE);
        Mask.setVisibility(View.VISIBLE);

        //找到最大的ID，生成临时ID，作为临时对象的ID
        long maxFrameIDInSQLite = smallSheetFramePresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        if (smallSheetFramePresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            return;
        }

        long maxTextIDInSQLite = smallSheetTextPresenter.generateTmpRowID(BaseModel.FIELD_NAME_syncDatetime);
        if (smallSheetTextPresenter.getLastErrorCode() != ErrorInfo.EnumErrorCode.EC_NoError) {
            return;
        }

        SmallSheetFrame ssf = new SmallSheetFrame();
        if (bmpFrameLogo != null) {
            ssf.setLogo(GeneralUtil.bitmapToString(bmpFrameLogo));
        }
        List<SmallSheetText> smallSheetTextList = getSmallSheetTextFromView();
        for (int i = 0; i < smallSheetTextList.size(); i++) {
            smallSheetTextList.get(i).setID(maxTextIDInSQLite + i);
            smallSheetTextList.get(i).setFrameId(maxTextIDInSQLite);
        }

        ssf.setListSlave1(smallSheetTextList);
        ssf.setReturnObject(1);//...
        ssf.setID(maxFrameIDInSQLite);
        ssf.setCountOfBlankLineAtBottom(Integer.valueOf(designBottomBlankLine.getText().toString()));
        ssf.setDelimiterToRepeat(designDelimiterToRepeat.getText().toString());
        ssf.setCreateDatetime(new Date(System.currentTimeMillis() + NtpHttpBO.TimeDifference));
        smallSheetSQLiteEvent.setEventTypeSQLite(ESET_SmallSheet_CreateMasterSlaveAsync_Done);
        smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
        if (!smallSheetSQLiteBO.createAsync(BaseSQLiteBO.INVALID_CASE_ID, ssf)) {
            //提示创建失败
//                    Toast.makeText(this, "小票格式创建失败!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage(), Toast.LENGTH_SHORT).show();
            closeLoadingDialog(loadingDailog);
            return;
        }
        return;
    }

    private void onDeleteSmallSheetInUI() {
        smallsheet_delete.setEnabled(false);
        System.out.println("要删除的ID：" + selectedFrameID);
        if (selectedFrameID != null) {
            // 判断是否为不可删除的默认小票ID
            if (selectedFrameID != Default_SmallSheetID_INPos) {
                loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_DeletingSmallSheet);
                //查找到展示的是哪一个小票格式
                SmallSheetFrame deleteSSF = new SmallSheetFrame();
                deleteSSF.setID(selectedFrameID);
                deleteSSF.setDelimiterToRepeat("");
                deleteSSF.setReturnObject(1);//...
                //根据现在的frame拿到所有的text，用于设置updateSST的ID
                deleteSSF.setSql(" where F_FrameID = ? ");
                deleteSSF.setConditions(new String[]{String.valueOf(deleteSSF.getID())});
                List<SmallSheetText> deleteSST = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, deleteSSF);

                if (selectedFrameID != null) {
                    deleteSSF.setListSlave1(deleteSST);
                    deleteSSF.setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
                } else {
                    closeLoadingDialog(loadingDailog);
                    Toast.makeText(getActivity(), "没有可以删除的小票格式", Toast.LENGTH_SHORT).show();
                    smallsheet_delete.setEnabled(true);
                    return;
                }
                //在本地设置SyncType和SyncDatetime
                smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
                smallSheetSQLiteEvent.setEventTypeSQLite(ESET_SmallSheet_BeforeDeleteMasterSlaveAsync_Done);
                if (!smallSheetSQLiteBO.deleteAsync(BaseSQLiteBO.INVALID_CASE_ID, deleteSSF)) {
                    //提示删除失败
                    closeLoadingDialog(loadingDailog);
                    Toast.makeText(getActivity(), "小票格式删除失败!", Toast.LENGTH_SHORT).show();
                    smallsheet_delete.setEnabled(true);
                    return;
                }
            } else {
                Toast.makeText(getActivity(), "不允许删除默认小票格式！", Toast.LENGTH_SHORT).show();
                smallsheet_delete.setEnabled(true);
            }
        } else {
            Toast.makeText(getActivity(), "请选择一张有效的小票格式！", Toast.LENGTH_SHORT).show();
            smallsheet_delete.setEnabled(true);
        }
    }

    private void onUpdateSmallSheetInUI() {
        if (selectedFrameID != null) {
            loadingDailog = createWaitingUI(loadingDailog, LOADING_MSG_UpdatingSmallSheet);
            //根据UI获取到对应的内容进行修改
            SmallSheetFrame updateSSF = new SmallSheetFrame();
            updateSSF.setID(selectedFrameID);
            updateSSF.setReturnObject(1);//...
            updateSSF.setCountOfBlankLineAtBottom(Integer.valueOf(designBottomBlankLine.getText().toString()));
            updateSSF.setDelimiterToRepeat(designDelimiterToRepeat.getText().toString());
            if (bmpFrameLogo != null) {
                updateSSF.setLogo(GeneralUtil.bitmapToString(bmpFrameLogo));
            } else {
                updateSSF.setLogo("");
            }
            List<SmallSheetText> updateSST = getSmallSheetTextFromView();

            //根据现在的frame拿到所有的text，用于设置updateSST的ID
            updateSSF.setSql(" where F_FrameID = ? ");
            updateSSF.setConditions(new String[]{String.valueOf(updateSSF.getID())});
            List<SmallSheetText> updateTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, updateSSF);
            for (int i = 0; i < updateSST.size(); i++) {
                updateSST.get(i).setID(updateTextList.get(i).getID());
                updateSST.get(i).setFrameId(updateSSF.getID());
            }
            updateSSF.setListSlave1(updateSST);
            updateSSF.setSlaveCreated(BaseModel.EnumBoolean.EB_Yes.getIndex());
            //
            smallSheetSQLiteEvent.setStatus(BaseEvent.EnumEventStatus.EES_SQLite_ToDo);
            smallSheetSQLiteEvent.setEventTypeSQLite(ESET_SmallSheet_UpdateMasterSlaveAsync);
            if (!smallSheetSQLiteBO.updateAsync(BaseSQLiteBO.INVALID_CASE_ID, updateSSF)) {
//                        Toast.makeText(appApplication, "小票格式更新失败!", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), smallSheetSQLiteBO.getSqLiteEvent().getLastErrorMessage(), Toast.LENGTH_SHORT).show();
                closeLoadingDialog(loadingDailog);
            }
        } else {
            Toast.makeText(getActivity(), "请选择中一张有效的小票格式", Toast.LENGTH_SHORT).show();
        }
        loadingConfigData.setVisibility(View.VISIBLE);
        smallsheet_create.setVisibility(View.VISIBLE);
        smallsheetToUpdate.setVisibility(View.VISIBLE);
        cancelUpdate.setVisibility(View.GONE);
        smallsheet_update.setVisibility(View.GONE);
        useForm.setVisibility(View.VISIBLE);
        Mask.setVisibility(View.VISIBLE);
    }

    /**
     * 检查页眉、页脚、底部文本内容长度
     */
    private boolean checkTextContentLength(String s) {
        if (s.length() < MAX_LENGTH_Content) {
            return true;
        }
        return false;
    }

    /**
     * 测试打印机的打印功能是否正常
     */
    private void onPrinterTest() {
        if (checkTextContentLength(templateHeader.getText().toString())
                && checkTextContentLength(templateFooter1.getText().toString())
                && checkTextContentLength(templateFooter2.getText().toString())
                && checkTextContentLength(templateFooter3.getText().toString())
                && checkTextContentLength(templateFooter4.getText().toString())
                && checkTextContentLength(templateFooter5.getText().toString())
                && checkTextContentLength(templateFooter6.getText().toString())
                && checkTextContentLength(templateFooter7.getText().toString())
                && checkTextContentLength(templateFooter8.getText().toString())
                && checkTextContentLength(templateFooter9.getText().toString())
                && checkTextContentLength(templateFooter10.getText().toString())
                && checkTextContentLength(templateTicketBottom.getText().toString())) {
            try {
                /**
                 * 每一行判断控件的状态进行设
                 */
                //如果logo是自定义，就打印图片，以及打印分割线
                if ("自定义".equals(designIfTopLogo.getText().toString())) {
                    AidlUtil.getInstance().printBitmap(bmpFrameLogo);
                    AidlUtil.getInstance().printDivider(designDelimiterToRepeat.getText().toString());
                }
                if (!"".equals(templateHeader.getText().toString())) {
                    AidlUtil.getInstance().printText(templateHeader.getText().toString(), templateHeader.getTextSize(), templateHeader.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateHeader.getGravity(), false);
                    AidlUtil.getInstance().printDivider(designDelimiterToRepeat.getText().toString());
                }
                AidlUtil.getInstance().printText(templateDocumentNumber.getText().toString(), templateDocumentNumber.getTextSize(), templateDocumentNumber.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateDocumentNumber.getGravity(), false);
                AidlUtil.getInstance().printText(templateDate.getText().toString(), templateDate.getTextSize(), templateDate.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateDate.getGravity(), false);
                AidlUtil.getInstance().printDivider(designDelimiterToRepeat.getText().toString());

                String[] goods_attrs = new String[]{"商品名称", "数量", "小计"};
                String[] first_goods = new String[]{"商品A", "x1", "1020"};
                String[] second_goods = new String[]{"商品B", "x5", "5500"};
                String[] third_goods = new String[]{"商品C", "x3", "3"};
                int[] colsWidthArrc = new int[]{2, 1, 1};//每一列所占权重
                int[] colsAlign = new int[]{0, 1, 2};//每一列对齐方式
                AidlUtil.getInstance().printTable(goods_attrs, colsWidthArrc, colsAlign);
                AidlUtil.getInstance().printTable(first_goods, colsWidthArrc, colsAlign);
                AidlUtil.getInstance().printTable(second_goods, colsWidthArrc, colsAlign);
                AidlUtil.getInstance().printTable(third_goods, colsWidthArrc, colsAlign);

                AidlUtil.getInstance().printDivider(designDelimiterToRepeat.getText().toString());

                AidlUtil.getInstance().printText(templateTotalMoney.getText().toString(), templateTotalMoney.getTextSize(), templateTotalMoney.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateTotalMoney.getGravity(), false);
                AidlUtil.getInstance().printText(templatePaymentMethod.getText().toString(), templatePaymentMethod.getTextSize(), templatePaymentMethod.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templatePaymentMethod.getGravity(), false);
                AidlUtil.getInstance().printText(templateDiscount.getText().toString(), templateDiscount.getTextSize(), templateDiscount.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateDiscount.getGravity(), false);
                AidlUtil.getInstance().printText(templatePayable.getText().toString(), templatePayable.getTextSize(), templatePayable.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templatePayable.getGravity(), false);
                AidlUtil.getInstance().printText(templatePaymentMethod1.getText().toString(), templatePaymentMethod1.getTextSize(), templatePaymentMethod1.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templatePaymentMethod1.getGravity(), false);
//                    AidlUtil.getInstance().printText(payment_method2.getText().toString(), payment_method2.getTextSize(), payment_method2.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), payment_method2.getGravity(), false);
                AidlUtil.getInstance().printText(templatePaymentMethod3.getText().toString(), templatePaymentMethod3.getTextSize(), templatePaymentMethod3.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templatePaymentMethod3.getGravity(), false);
                AidlUtil.getInstance().printDivider(designDelimiterToRepeat.getText().toString());
                if (templateFooter1.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter1.getText().toString(), templateFooter1.getTextSize(), templateFooter1.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter1.getGravity(), false);
                }
                if (templateFooter2.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter2.getText().toString(), templateFooter2.getTextSize(), templateFooter2.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter2.getGravity(), false);
                }
                if (templateFooter3.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter3.getText().toString(), templateFooter3.getTextSize(), templateFooter3.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter3.getGravity(), false);
                }
                if (templateFooter4.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter4.getText().toString(), templateFooter4.getTextSize(), templateFooter4.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter4.getGravity(), false);
                }
                if (templateFooter5.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter5.getText().toString(), templateFooter5.getTextSize(), templateFooter5.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter5.getGravity(), false);
                }
                if (templateFooter6.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter6.getText().toString(), templateFooter6.getTextSize(), templateFooter6.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter6.getGravity(), false);
                }
                if (templateFooter7.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter7.getText().toString(), templateFooter7.getTextSize(), templateFooter7.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter7.getGravity(), false);
                }
                if (templateFooter8.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter8.getText().toString(), templateFooter8.getTextSize(), templateFooter8.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter8.getGravity(), false);
                }
                if (templateFooter9.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter9.getText().toString(), templateFooter9.getTextSize(), templateFooter9.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter9.getGravity(), false);
                }
                if (templateFooter10.getVisibility() == View.VISIBLE) {
                    AidlUtil.getInstance().printText(templateFooter10.getText().toString(), templateFooter10.getTextSize(), templateFooter10.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateFooter10.getGravity(), false);
                }
                AidlUtil.getInstance().printText(templateTicketBottom.getText().toString(), templateTicketBottom.getTextSize(), templateTicketBottom.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD)), templateTicketBottom.getGravity(), false);
                // 判断如何底部空行是空字符串则默认给0，不是则赋予原值
                AidlUtil.getInstance().linewrap(designBottomBlankLine.getText().toString().trim().equals("") ? 0 : Integer.valueOf(designBottomBlankLine.getText().toString()));
                AidlUtil.getInstance().cutPaper();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "页尾、页脚、底部所输入的字符不能超过" + MAX_LENGTH_Content + "个，请重新输入" + MAX_LENGTH_Content + "个以内的字符", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击右边的打印格式
     */
    private void onClickPrinterSize() {
        AlertDialog.Builder printer_size_builder = new AlertDialog.Builder(getContext());
        View printer_size_view = View.inflate(getActivity().getApplicationContext(), R.layout.choose_printer_size_dialog, null);
        printer_size_builder.setView(printer_size_view);
        printer_size_builder.setCancelable(false);
        final AlertDialog printer_size_dialog = printer_size_builder.create();
        printer_size_dialog.show();
        printer_size_dialog.getWindow().setLayout(520, 370);
        //
        TextView top = printer_size_view.findViewById(R.id.top);
        LinearLayout printer_size1 = printer_size_view.findViewById(R.id.select1);
        TextView printer_size1_tv = printer_size_view.findViewById(R.id.select1_tv);
        final ImageView selected_printer_size1 = printer_size_view.findViewById(R.id.selected1);
        LinearLayout printer_size2 = printer_size_view.findViewById(R.id.select2);
        TextView printer_size2_tv = printer_size_view.findViewById(R.id.select2_tv);
        final ImageView selected_printer_size2 = printer_size_view.findViewById(R.id.selected2);
        TextView printer_size_cancel = printer_size_view.findViewById(R.id.cancel);
        //
        printer_size1_tv.setText("58mm");
        printer_size2_tv.setText("80mm");
        if ("58mm".equals(templatePrinterSize.getText().toString())) {
            selected_printer_size1.setVisibility(View.VISIBLE);
            selected_printer_size2.setVisibility(View.INVISIBLE);
        } else if ("80mm".equals(templatePrinterSize.getText().toString())) {
            selected_printer_size1.setVisibility(View.INVISIBLE);
            selected_printer_size2.setVisibility(View.VISIBLE);
        }
        //
        printer_size1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_printer_size1.setVisibility(View.VISIBLE);
                selected_printer_size2.setVisibility(View.INVISIBLE);
                templatePrinterSize.setText("58mm");
                printer_size_dialog.dismiss();
            }
        });
        printer_size2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_printer_size1.setVisibility(View.INVISIBLE);
                selected_printer_size2.setVisibility(View.VISIBLE);
                templatePrinterSize.setText("80mm");
                printer_size_dialog.dismiss();
            }
        });
        printer_size_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printer_size_dialog.dismiss();
            }
        });
    }

    /**
     * 点击右边设置logo
     */
    private void onClickSelectLogo() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;//屏幕宽度
        int screenHeight = dm.heightPixels;//屏幕高度
        AlertDialog.Builder top_logo_builder = new AlertDialog.Builder(getContext());
        View top_logo_view = View.inflate(getActivity().getApplicationContext(), R.layout.choose_printer_size_dialog, null);
        top_logo_builder.setView(top_logo_view);
        top_logo_builder.setCancelable(false);
        top_logo_dialog = top_logo_builder.create();
        top_logo_dialog.show();
        //
        WindowManager.LayoutParams params = top_logo_dialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.5);
        params.height = (int) (screenHeight * 0.55);
        top_logo_dialog.getWindow().setAttributes(params);
        //
        LinearLayout no_logo = top_logo_view.findViewById(R.id.select1);
        TextView no_logo_tv = top_logo_view.findViewById(R.id.select1_tv);
        selected_no_logo = top_logo_view.findViewById(R.id.selected1);
        LinearLayout from_album = top_logo_view.findViewById(R.id.select2);
        TextView from_album_tv = top_logo_view.findViewById(R.id.select2_tv);
        selected_from_album = top_logo_view.findViewById(R.id.selected2);
        TextView top_logo_cancel = top_logo_view.findViewById(R.id.cancel);
        //
        no_logo_tv.setText("无");
        from_album_tv.setText("从相册选择");
        //
        if ("无".equals(designIfTopLogo.getText().toString())) {
            selected_no_logo.setVisibility(View.VISIBLE);
            selected_from_album.setVisibility(View.INVISIBLE);
            templateLogo.setVisibility(View.GONE);
            bmpFrameLogo = null;
            templateLogo.setImageBitmap(null);
        } else if ("自定义".equals(designIfTopLogo.getText().toString())) {
            selected_no_logo.setVisibility(View.INVISIBLE);
            selected_from_album.setVisibility(View.VISIBLE);
            templateLogo.setVisibility(View.VISIBLE);
        }
        no_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                templateLogoLayout.setVisibility(View.GONE);
                templateLogo.setVisibility(View.GONE);
                designIfTopLogo.setText("无");
                bmpFrameLogo = null;
                templateLogo.setImageBitmap(null);
                selected_no_logo.setVisibility(View.VISIBLE);
                selected_from_album.setVisibility(View.INVISIBLE);
                top_logo_dialog.dismiss();
            }
        });
        from_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_EXTERNAL_STORAGE = 1;
                String[] PERMISSIONS_STORAGE = {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );
                } else {
                    templateLogoLayout.setVisibility(View.VISIBLE);
                    templateLogo.setVisibility(View.VISIBLE);
                    designIfTopLogo.setText("自定义");
                    selected_no_logo.setVisibility(View.INVISIBLE);
                    selected_from_album.setVisibility(View.VISIBLE);
                    Intent i = new Intent(Intent.ACTION_PICK, null);
                    i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(i, CUT_BIG_REQUEST);
                    top_logo_dialog.dismiss();
                }
            }
        });
        top_logo_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                top_logo_dialog.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        dealWithResult(requestCode, grantResults);
    }

    //我们接着需要根据requestCode和grantResults(授权结果)做相应的后续处理
    private void dealWithResult(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == 0) {//用户允许权限
                templateLogoLayout.setVisibility(View.VISIBLE);
                templateLogo.setVisibility(View.VISIBLE);
                designIfTopLogo.setText("自定义");
                selected_no_logo.setVisibility(View.INVISIBLE);
                selected_from_album.setVisibility(View.VISIBLE);
                Intent i = new Intent(Intent.ACTION_PICK, null);
                i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(i, CUT_BIG_REQUEST);
                top_logo_dialog.dismiss();
            } else {//用户拒绝权限

            }
        }
    }

    /**
     * 设置页脚是否可见是否可编辑, 若是可见可编辑
     *
     * @param footer          页脚
     * @param view            页脚分割线
     * @param footer_editText 页脚编辑控件
     */
    private void setFooterLayoutIsVisible(RelativeLayout footer, View view, EditText footer_editText) {
        footer.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 选择小票格式左边预览部分的商品部分的效果 因为商品部分有TextView 和 Layout, 所以参数有两个
     *
     * @param textView 商品部分的TextView
     * @param layout   商品部分的layout
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void selectGoodsListLayout(TextView textView, LinearLayout layout) {
        setTextBackgroudColor();
        if (layout != null) {
            textView.setBackgroundColor(getResources().getColor(R.color.blue, null));
            layout.setBackgroundColor(getResources().getColor(R.color.blue, null));
        }
        showControlTextSize.setText((int) textView.getTextSize() + "");
        controlKeepLeft.setClickable(false);
        controlKeepCenter.setClickable(false);
        controlKeepRight.setClickable(false);
        controlTextSize.setClickable(false);
        left_shift.setClickable(false);
        right_shift.setClickable(false);
        controlBold.setClickable(false);
    }

    /**
     * 当选中一个text, 点击状态栏的加粗选项, 如果原来的内容为加粗, 点击之后为取消加粗, 背景色改变, 否则反之
     *
     * @param textView 选中的text
     */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void clickBold(TextView textView) {
        if (!textView.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD))) {
//            controlBold.setBackgroundColor(Color.YELLOW);
            controlBold.setBackground(getResources().getDrawable(R.drawable.textview_rounded_click, null));
            controlBold.setTextColor(getResources().getColor(R.color.blue, null));
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
//            controlBold.setBackgroundColor(Color.WHITE);
            controlBold.setBackground(getResources().getDrawable(R.drawable.textview_rounded_unclick, null));
            controlBold.setTextColor(getResources().getColor(R.color.text_bgcolor_darkgray, null));
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    /**
     * 当没有小票格式的时候，初始化右边的界面
     */
    private void initRightContent() {
        designIfTopLogo.setText("无");
        templateLogoLayout.setVisibility(View.GONE);
        designHeader.setText(defaultText);
        templateHeader.setText(designHeader.getText().toString());
        designFooter1.setText(defaultText);
        templateFooter1.setText(designFooter1.getText().toString());
        designTicketBottom.setText(defaultText);
        templateTicketBottom.setText(designTicketBottom.getText().toString());
        bmpFrameLogo = null;
        //...这样初始化，貌似会很低效
        designFooter2.setText(defaultText);
        templateFooter2.setText(designFooter2.getText().toString());
        designFooter3.setText(defaultText);
        templateFooter3.setText(designFooter3.getText().toString());
        designFooter4.setText(defaultText);
        templateFooter4.setText(designFooter4.getText().toString());
        designFooter5.setText(defaultText);
        templateFooter5.setText(designFooter5.getText().toString());
        designFooter6.setText(defaultText);
        templateFooter6.setText(designFooter6.getText().toString());
        designFooter7.setText(defaultText);
        templateFooter7.setText(designFooter7.getText().toString());
        designFooter8.setText(defaultText);
        templateFooter8.setText(designFooter8.getText().toString());
        designFooter9.setText(defaultText);
        templateFooter9.setText(designFooter9.getText().toString());
        designFooter10.setText(defaultText);
        templateFooter10.setText(designFooter10.getText().toString());

        showControlTextSize.setText("30");

        templateHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateDocumentNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateTotalMoney.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templatePaymentMethod.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateDiscount.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templatePayable.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templatePaymentMethod1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
//        payment_method2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templatePaymentMethod3.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter3.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter4.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter5.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter6.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter7.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter8.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter9.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateFooter10.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
        templateTicketBottom.setTextSize(TypedValue.COMPLEX_UNIT_PX, MAX_Length_EditText);
    }

    /**
     * 初始化小票格式UI左边TextView的Text，防止上次小票格式操作时的残留。
     */
    private void initTemplateSmallSheet(List<SmallSheetText> smallSheetTextList) {
        setTemplateContent(templateTotalMoney, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()));
        setTemplateContent(templatePaymentMethod, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()));
        setTemplateContent(templateDiscount, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()));
        setTemplateContent(templatePayable, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()));
        setTemplateContent(templatePaymentMethod1, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()));
        setTemplateContent(templatePaymentMethod3, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()));
        setTemplateContent(templateHeader, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()));
        setTemplateContent(templateFooter1, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()));
        setTemplateContent(templateFooter2, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()));
        setTemplateContent(templateFooter3, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()));
        setTemplateContent(templateFooter4, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()));
        setTemplateContent(templateFooter5, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()));
        setTemplateContent(templateFooter6, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()));
        setTemplateContent(templateFooter7, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()));
        setTemplateContent(templateFooter8, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()));
        setTemplateContent(templateFooter9, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()));
        setTemplateContent(templateFooter10, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()));
        setTemplateContent(templateTicketBottom, smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()));
    }

    /**
     * 当小票格式存在的时候，根据选择的格式的版本，进行内容展示预览
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void showSelectedSmallSheetInUI(SmallSheetFrame smallSheetFrame, List<SmallSheetText> select_smallSheetTextList) {
        if (smallSheetFrame != null) {
            if (smallSheetFrame.getLogo() == null || "".equals(smallSheetFrame.getLogo())) {
                templateLogoLayout.setVisibility(View.GONE);
                designIfTopLogo.setText("无");
                templateLogo.setImageBitmap(null);
                bmpFrameLogo = null;
            } else {
                templateLogoLayout.setVisibility(View.VISIBLE);
                designIfTopLogo.setText("自定义");
                bmpFrameLogo = GeneralUtil.stringToBitmap(smallSheetFrame.getLogo());
                if (bmpFrameLogo != null) {
                    templateLogo.setImageBitmap(bmpFrameLogo);
                    ImgUtil.writeFileByBitmap2(bmpFrameLogo);
                }
            }

            controlBold.setBackground(getResources().getDrawable(R.drawable.textview_rounded_unclick, null));
            controlBold.setTextColor(getResources().getColor(R.color.text_bgcolor_darkgray, null));
            controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
            controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
            controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            setTextBackgroudColor();

            designDelimiterToRepeat.setText(smallSheetFrame.getDelimiterToRepeat());
            designBottomBlankLine.setText(String.valueOf(smallSheetFrame.getCountOfBlankLineAtBottom()));

            if (select_smallSheetTextList.size() > 0) {
                setDesignContent(designHeader, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()));
                setDesignContent(designFooter1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()));
                setDesignContent(designFooter2, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()));//若content为空，预览右边的设置内容不可见
                setDesignContent(designFooter3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()));
                setDesignContent(designFooter4, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()));
                setDesignContent(designFooter5, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()));
                setDesignContent(designFooter6, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()));
                setDesignContent(designFooter7, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()));
                setDesignContent(designFooter8, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()));
                setDesignContent(designFooter9, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()));
                setDesignContent(designFooter10, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()));
                setDesignContent(designTicketBottom, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()));

                setTemplateStyle(templateHeader, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()));
                setTemplateStyle(templateDocumentNumber, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()));
                setTemplateStyle(templateDate, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()));
                setTemplateStyle(templateTotalMoney, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()));
                setTemplateStyle(templatePaymentMethod, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()));
                setTemplateStyle(templateDiscount, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()));
                setTemplateStyle(templatePayable, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()));
                setTemplateStyle(templatePaymentMethod1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()));
//                setTemplateStyle(payment_method2, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.getIndex(SmallSheetText.payment_Method2)));
                setTemplateStyle(templatePaymentMethod3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()));
                setTemplateStyle(templateFooter1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()));
                setTemplateStyle(templateFooter2, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()));
                setTemplateStyle(templateFooter3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()));
                setTemplateStyle(templateFooter4, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()));
                setTemplateStyle(templateFooter5, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()));
                setTemplateStyle(templateFooter6, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()));
                setTemplateStyle(templateFooter7, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()));
                setTemplateStyle(templateFooter8, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()));
                setTemplateStyle(templateFooter9, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()));
                setTemplateStyle(templateFooter10, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()));
                setTemplateStyle(templateTicketBottom, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.getIndex(SmallSheetText.show_Ticket_Bottom)));

                setContentIsBold(templateHeader, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Header.getIndex()));
                setContentIsBold(templateDocumentNumber, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Document_Number.getIndex()));
                setContentIsBold(templateDate, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Date.getIndex()));
                setContentIsBold(templateTotalMoney, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Total_Money.getIndex()));
                setContentIsBold(templatePaymentMethod, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method.getIndex()));
                setContentIsBold(templateDiscount, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Discount.getIndex()));
                setContentIsBold(templatePayable, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payable.getIndex()));
                setContentIsBold(templatePaymentMethod1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method1.getIndex()));
//                setContentIsBold(payment_method2, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.getIndex(SmallSheetText.payment_Method2)));
                setContentIsBold(templatePaymentMethod3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Payment_Method3.getIndex()));
                setContentIsBold(templateFooter1, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer1.getIndex()));
                setContentIsBold(templateFooter2, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer2.getIndex()));
                setContentIsBold(templateFooter3, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer3.getIndex()));
                setContentIsBold(templateFooter4, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer4.getIndex()));
                setContentIsBold(templateFooter5, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer5.getIndex()));
                setContentIsBold(templateFooter6, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer6.getIndex()));
                setContentIsBold(templateFooter7, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer7.getIndex()));
                setContentIsBold(templateFooter8, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer8.getIndex()));
                setContentIsBold(templateFooter9, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer9.getIndex()));
                setContentIsBold(templateFooter10, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Footer10.getIndex()));
                setContentIsBold(templateTicketBottom, select_smallSheetTextList.get(SmallSheetText.EnumSmallSheetTextIndex.Show_Ticket_Bottom.getIndex()));
            } else {
                Toast.makeText(getActivity(), "没有找到对应从表", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 设置右边内容设置的内容
     *
     * @param editText       右边内容设置的控件
     * @param smallSheetText 选择的小票格式版本的Text
     */
    private void setDesignContent(EditText editText, SmallSheetText smallSheetText) {
        if (smallSheetText.getContent() == null || "".equals(smallSheetText.getContent())) {
            editText.setText("");
        } else {
            editText.setText(smallSheetText.getContent());
        }
    }

    /**
     * 设置左边内容设置的内容
     *
     * @param textView       左边内容设置的控件
     * @param smallSheetText 选择的小票格式版本的Text
     */
    private void setTemplateContent(TextView textView, SmallSheetText smallSheetText) {
        if (smallSheetText.getContent() == null || "".equals(smallSheetText.getContent())) {
            textView.setText("");
        } else {
            textView.setText(smallSheetText.getContent());
        }
    }

    /**
     * 设置左边内容的格式
     *
     * @param textView       左边的预览内容的控件
     * @param smallSheetText 选择的小票格式版本的Text
     */
    private void setTemplateStyle(TextView textView, SmallSheetText smallSheetText) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallSheetText.getSize());
        log.info("字体大小：" + textView.getTextSize() + "    " + smallSheetText.getSize());
        textView.setGravity(smallSheetText.getGravity());
    }

    /**
     * 设置左边预览是否对文本进行加粗
     *
     * @param textView       左边文本
     * @param smallSheetText 选择的小票格式版本的Text
     */
    private void setContentIsBold(TextView textView, SmallSheetText smallSheetText) {
        if (smallSheetText.getBold() == 1) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /**
     * 如果右边页脚内容为空，左边的页脚则不显示
     *
     * @param editText
     * @param textView
     */
    public void setFooterTextChange(EditText editText, TextView textView, RelativeLayout relativeLayout, View view) {
        if ("".equals(editText.getText().toString()) || null == editText.getText().toString()) {
            // 这里隐藏掉View即可,无需将text重新设置为空,否则会进行递归调用事件，导致stack过大的问题，从而使程序死掉.
            textView.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(editText.getText().toString());
            // 将有页脚的数据相应的展示出来
            setFooterLayoutIsVisible(relativeLayout, view, editText);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置字体大小对话框
     *
     * @param context
     * @param title
     * @param min
     * @param max
     * @param set
     * @param target
     */
    private void showSeekBarDialog(Context context, String title, final int min, final int max, final TextView set, final TextView target) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_seekbar, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        TextView tv_title = (TextView) view.findViewById(R.id.sb_title);
        TextView tv_start = (TextView) view.findViewById(R.id.sb_start);
        TextView tv_end = (TextView) view.findViewById(R.id.sb_end);
        final TextView tv_result = (TextView) view.findViewById(R.id.sb_result);
        TextView tv_ok = (TextView) view.findViewById(R.id.sb_ok);
        TextView tv_cancel = (TextView) view.findViewById(R.id.sb_cancel);
        SeekBar sb = (SeekBar) view.findViewById(R.id.sb_seekbar);
        tv_title.setText(title);
        tv_start.setText(min + "");
        tv_end.setText(max + "");
        tv_result.setText(set.getText());
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target.setTextSize(TypedValue.COMPLEX_UNIT_PX, Float.parseFloat(tv_result.getText().toString()));//因为gettextsize的单位为px，所以在settextsize的时候需要保证单位一样，取出来的值才会一样
                log.info("target.size = " + target.getTextSize() + "     " + tv_result.getText().toString());
                set.setText(tv_result.getText().toString());
                dialog.cancel();
            }
        });
        sb.setMax(max - min);
        sb.setProgress(Integer.parseInt(set.getText().toString()) - min);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int rs = min + progress;
                tv_result.setText(rs + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        dialog.show();
    }

    /**
     * 设置选中的内容的属性，包括字号，居中（靠左，靠右）
     *
     * @param text
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setTextGravity(final TextView text) {
        showControlTextSize.setText((int) text.getTextSize() + "");
        controlTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSeekBarDialog(getContext(), getResources().getString(R.string.size_text), 12, 56, showControlTextSize, text);
            }
        });
        setBoldButtonColor(text);
        if (text.getGravity() == GRAVITY_LEFT) {
            controlKeepLeft.setImageResource(R.drawable.left_align_click);
            controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
            controlKeepRight.setImageResource(R.drawable.right_align_unclick);
        }
        if (text.getGravity() == GRAVITY_CENTRE) {
            controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
            controlKeepCenter.setImageResource(R.drawable.center_align_click);
            controlKeepRight.setImageResource(R.drawable.right_align_unclick);
        }
        if (text.getGravity() == GRAVITY_RIGHT) {
            controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
            controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
            controlKeepRight.setImageResource(R.drawable.right_align_click);
        }
        controlKeepLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(text.getText().toString().replace(" ", ""));
                controlKeepLeft.setImageResource(R.drawable.left_align_click);
                controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
                controlKeepRight.setImageResource(R.drawable.right_align_unclick);
                text.setGravity(Gravity.LEFT);
            }
        });
        controlKeepCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(text.getText().toString().replace(" ", ""));
                controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
                controlKeepCenter.setImageResource(R.drawable.center_align_click);
                controlKeepRight.setImageResource(R.drawable.right_align_unclick);
                text.setGravity(Gravity.CENTER);
            }
        });
        controlKeepRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(text.getText().toString().replace(" ", ""));
                controlKeepLeft.setImageResource(R.drawable.left_align_unclick);
                controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
                controlKeepRight.setImageResource(R.drawable.right_align_click);
                text.setGravity(Gravity.RIGHT);
            }
        });
        left_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getGravity() == GRAVITY_CENTRE || text.getGravity() == GRAVITY_RIGHT) {//居中或靠右，左移
                    switch (text.getId()) {
                        case R.id.template_header:
                            left_shift_blank_number[0]++;
                            log.info("左移次数：" + left_shift_blank_number[0]);
                            moveToLeft(text);
                            break;
                        case R.id.template_document_number:
                            left_shift_blank_number[1]++;
                            log.info("左移次数：" + left_shift_blank_number[1]);
                            moveToLeft(text);
                            break;
                        case R.id.template_date:
                            left_shift_blank_number[2]++;
                            log.info("左移次数：" + left_shift_blank_number[2]);
                            moveToLeft(text);
                            break;
                        case R.id.total_money:
                            left_shift_blank_number[3]++;
                            log.info("左移次数：" + left_shift_blank_number[3]);
                            moveToLeft(text);
                            break;
                        case R.id.payment_method:
                            left_shift_blank_number[4]++;
                            log.info("左移次数：" + left_shift_blank_number[4]);
                            moveToLeft(text);
                            break;
                        case R.id.template_discont:
                            left_shift_blank_number[5]++;
                            log.info("左移次数：" + left_shift_blank_number[5]);
                            moveToLeft(text);
                            break;
                        case R.id.template_payable:
                            left_shift_blank_number[6]++;
                            log.info("左移次数：" + left_shift_blank_number[6]);
                            moveToLeft(text);
                            break;
                        case R.id.template_payment_method1:
                            left_shift_blank_number[7]++;
                            log.info("左移次数：" + left_shift_blank_number[7]);
                            moveToLeft(text);
                            break;
//                        case R.id.payment_method2:
//                            left_shift_blank_number[8]++;
//                            log.info("左移次数：" + left_shift_blank_number[8]);
//                            text.setText(text.getText().toString() + " ");
//                            break;
                        case R.id.template_payment_method3:
                            left_shift_blank_number[8]++;
                            log.info("左移次数：" + left_shift_blank_number[8]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer1:
                            left_shift_blank_number[9]++;
                            log.info("左移次数：" + left_shift_blank_number[9]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer2:
                            left_shift_blank_number[10]++;
                            log.info("左移次数：" + left_shift_blank_number[10]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer3:
                            left_shift_blank_number[11]++;
                            log.info("左移次数：" + left_shift_blank_number[11]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer4:
                            left_shift_blank_number[12]++;
                            log.info("左移次数：" + left_shift_blank_number[12]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer5:
                            left_shift_blank_number[13]++;
                            log.info("左移次数：" + left_shift_blank_number[13]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer6:
                            left_shift_blank_number[14]++;
                            log.info("左移次数：" + left_shift_blank_number[14]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer7:
                            left_shift_blank_number[15]++;
                            log.info("左移次数：" + left_shift_blank_number[15]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer8:
                            left_shift_blank_number[16]++;
                            log.info("左移次数：" + left_shift_blank_number[16]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer9:
                            left_shift_blank_number[17]++;
                            log.info("左移次数：" + left_shift_blank_number[17]);
                            moveToLeft(text);
                            break;
                        case R.id.template_footer10:
                            left_shift_blank_number[18]++;
                            log.info("左移次数：" + left_shift_blank_number[18]);
                            moveToLeft(text);
                            break;
                        case R.id.template_ticket_bottom:
                            left_shift_blank_number[19]++;
                            log.info("左移次数：" + left_shift_blank_number[19]);
                            moveToLeft(text);
                            break;
                    }

                } else if (text.getGravity() == GRAVITY_LEFT) {//如果靠左，左移
                    if (text.getText().toString().indexOf(" ") == -1) {//说明字符串中不含有空格，靠左的text如果不含有空格，说明text没有右移过，不可以进行左移
                    } else {//如果!=-1，说明靠左的text含有空格，text进行过右移，可以进行左移（将右移时候在text前面加的空格减掉）
                        switch (text.getId()) {
                            case R.id.template_header:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[0]--;
                                log.info("左移次数：" + right_shift_blank_number[0]);
                                break;
                            case R.id.template_document_number:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[1]--;
                                log.info("左移次数：" + right_shift_blank_number[1]);
                                break;
                            case R.id.template_date:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[2]--;
                                log.info("左移次数：" + right_shift_blank_number[2]);
                                break;
                            case R.id.total_money:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[3]--;
                                log.info("左移次数：" + right_shift_blank_number[3]);
                                break;
                            case R.id.payment_method:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[4]--;
                                log.info("左移次数：" + right_shift_blank_number[4]);
                                break;
                            case R.id.template_discont:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[5]--;
                                log.info("左移次数：" + right_shift_blank_number[5]);
                                break;
                            case R.id.template_payable:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[6]--;
                                log.info("左移次数：" + right_shift_blank_number[6]);
                                break;
                            case R.id.template_payment_method1:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[7]--;
                                log.info("左移次数：" + right_shift_blank_number[7]);
                                break;
//                            case R.id.payment_method2:
//                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
//                                right_shift_blank_number[8]--;
//                                log.info("左移次数：" + right_shift_blank_number[8]);
//                                break;
                            case R.id.template_payment_method3:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[8]--;
                                log.info("左移次数：" + right_shift_blank_number[8]);
                                break;
                            case R.id.template_footer1:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[9]--;
                                log.info("左移次数：" + right_shift_blank_number[9]);
                                break;
                            case R.id.template_footer2:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[10]--;
                                log.info("左移次数：" + right_shift_blank_number[10]);
                                break;
                            case R.id.template_footer3:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[11]--;
                                log.info("左移次数：" + right_shift_blank_number[11]);
                                break;
                            case R.id.template_footer4:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[12]--;
                                log.info("左移次数：" + right_shift_blank_number[12]);
                                break;
                            case R.id.template_footer5:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[13]--;
                                log.info("左移次数：" + right_shift_blank_number[13]);
                                break;
                            case R.id.template_footer6:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[14]--;
                                log.info("左移次数：" + right_shift_blank_number[14]);
                                break;
                            case R.id.template_footer7:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[15]--;
                                log.info("左移次数：" + right_shift_blank_number[15]);
                                break;
                            case R.id.template_footer8:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[16]--;
                                log.info("左移次数：" + right_shift_blank_number[16]);
                                break;
                            case R.id.template_footer9:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[17]--;
                                log.info("左移次数：" + right_shift_blank_number[17]);
                                break;
                            case R.id.template_footer10:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[18]--;
                                log.info("左移次数：" + right_shift_blank_number[18]);
                                break;
                            case R.id.template_ticket_bottom:
                                text.setText(text.getText().toString().substring(text.getText().toString().indexOf(" ") + 1));
                                right_shift_blank_number[19]--;
                                log.info("左移次数：" + right_shift_blank_number[19]);
                                break;
                        }

                    }
                }
            }
        });
        right_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getGravity() == GRAVITY_CENTRE || text.getGravity() == GRAVITY_LEFT) {//居中或靠左，右移
                    switch (text.getId()) {
                        case R.id.template_header:
                            right_shift_blank_number[0]++;
                            log.info("右移次数：" + right_shift_blank_number[0]);
                            moveToRight(text);
                            break;
                        case R.id.template_document_number:
                            right_shift_blank_number[1]++;
                            log.info("右移次数：" + right_shift_blank_number[1]);
                            moveToRight(text);
                            break;
                        case R.id.template_date:
                            right_shift_blank_number[2]++;
                            log.info("右移次数：" + right_shift_blank_number[2]);
                            moveToRight(text);
                            break;
                        case R.id.total_money:
                            right_shift_blank_number[3]++;
                            log.info("右移次数：" + right_shift_blank_number[3]);
                            moveToRight(text);
                            break;
                        case R.id.payment_method:
                            right_shift_blank_number[4]++;
                            log.info("右移次数：" + right_shift_blank_number[4]);
                            moveToRight(text);
                            break;
                        case R.id.template_discont:
                            right_shift_blank_number[5]++;
                            log.info("右移次数：" + right_shift_blank_number[5]);
                            moveToRight(text);
                            break;
                        case R.id.template_payable:
                            right_shift_blank_number[6]++;
                            log.info("右移次数：" + right_shift_blank_number[6]);
                            moveToRight(text);
                            break;
                        case R.id.template_payment_method1:
                            right_shift_blank_number[7]++;
                            log.info("右移次数：" + right_shift_blank_number[7]);
                            moveToRight(text);
                            break;
//                        case R.id.payment_method2:
//                            right_shift_blank_number[8]++;
//                            log.info("右移次数：" + right_shift_blank_number[8]);
//                            text.setText(" " + text.getText().toString());
//                            break;
                        case R.id.template_payment_method3:
                            right_shift_blank_number[8]++;
                            log.info("右移次数：" + right_shift_blank_number[8]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer1:
                            right_shift_blank_number[9]++;
                            log.info("右移次数：" + right_shift_blank_number[9]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer2:
                            right_shift_blank_number[10]++;
                            log.info("右移次数：" + right_shift_blank_number[10]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer3:
                            right_shift_blank_number[11]++;
                            log.info("右移次数：" + right_shift_blank_number[11]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer4:
                            right_shift_blank_number[12]++;
                            log.info("右移次数：" + right_shift_blank_number[12]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer5:
                            right_shift_blank_number[13]++;
                            log.info("右移次数：" + right_shift_blank_number[13]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer6:
                            right_shift_blank_number[14]++;
                            log.info("右移次数：" + right_shift_blank_number[14]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer7:
                            right_shift_blank_number[15]++;
                            log.info("右移次数：" + right_shift_blank_number[15]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer8:
                            right_shift_blank_number[16]++;
                            log.info("右移次数：" + right_shift_blank_number[16]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer9:
                            right_shift_blank_number[17]++;
                            log.info("右移次数：" + right_shift_blank_number[17]);
                            moveToRight(text);
                            break;
                        case R.id.template_footer10:
                            right_shift_blank_number[18]++;
                            log.info("右移次数：" + right_shift_blank_number[18]);
                            moveToRight(text);
                            break;
                        case R.id.template_ticket_bottom:
                            right_shift_blank_number[19]++;
                            log.info("右移次数：" + right_shift_blank_number[19]);
                            moveToRight(text);
                            break;
                    }
                } else if (text.getGravity() == GRAVITY_RIGHT) {//靠右，右移
                    if (text.getText().toString().indexOf(" ") == -1) {//说明字符串中不含有空格，靠左的text如果不含有空格，说明text没有左移过，不可以进行右移
                    } else {//如果!=-1，说明靠左的text含有空格，text进行过左移，可以进行右移（将左移时候在text后面加的空格减掉）
                        switch (text.getId()) {
                            case R.id.template_header:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[0]--;
                                log.info("右移次数：" + left_shift_blank_number[0]);
                                break;
                            case R.id.template_document_number:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[1]--;
                                log.info("右移次数：" + left_shift_blank_number[1]);
                                break;
                            case R.id.template_date:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[2]--;
                                log.info("右移次数：" + left_shift_blank_number[2]);
                                break;
                            case R.id.total_money:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[3]--;
                                log.info("右移次数：" + left_shift_blank_number[3]);
                                break;
                            case R.id.payment_method:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[4]--;
                                log.info("右移次数：" + left_shift_blank_number[4]);
                                break;
                            case R.id.template_discont:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[5]--;
                                log.info("右移次数：" + left_shift_blank_number[5]);
                                break;
                            case R.id.template_payable:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[6]--;
                                log.info("右移次数：" + left_shift_blank_number[6]);
                                break;
                            case R.id.template_payment_method1:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[7]--;
                                log.info("右移次数：" + left_shift_blank_number[7]);
                                break;
//                            case R.id.payment_method2:
//                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
//                                left_shift_blank_number[8]--;
//                                log.info("右移次数：" + left_shift_blank_number[8]);
//                                break;
                            case R.id.template_payment_method3:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[8]--;
                                log.info("右移次数：" + left_shift_blank_number[8]);
                                break;
                            case R.id.template_footer1:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[9]--;
                                log.info("右移次数：" + left_shift_blank_number[9]);
                                break;
                            case R.id.template_footer2:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[10]--;
                                log.info("右移次数：" + left_shift_blank_number[10]);
                                break;
                            case R.id.template_footer3:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[11]--;
                                log.info("右移次数：" + left_shift_blank_number[11]);
                                break;
                            case R.id.template_footer4:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[12]--;
                                log.info("右移次数：" + left_shift_blank_number[12]);
                                break;
                            case R.id.template_footer5:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[13]--;
                                log.info("右移次数：" + left_shift_blank_number[13]);
                                break;
                            case R.id.template_footer6:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[14]--;
                                log.info("右移次数：" + left_shift_blank_number[14]);
                                break;
                            case R.id.template_footer7:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[15]--;
                                log.info("右移次数：" + left_shift_blank_number[15]);
                                break;
                            case R.id.template_footer8:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[16]--;
                                log.info("右移次数：" + left_shift_blank_number[16]);
                                break;
                            case R.id.template_footer9:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[17]--;
                                log.info("右移次数：" + left_shift_blank_number[17]);
                                break;
                            case R.id.template_footer10:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[18]--;
                                log.info("右移次数：" + left_shift_blank_number[18]);
                                break;
                            case R.id.template_ticket_bottom:
                                text.setText(text.getText().toString().substring(0, text.getText().toString().lastIndexOf(" ")));
                                left_shift_blank_number[19]--;
                                log.info("右移次数：" + left_shift_blank_number[19]);
                                break;
                        }
                    }
                }
            }
        });
    }

    private void moveToLeft(TextView text) {
        log.error("L该TextView里面的文本内容：" + text.getText().toString() + "，长度：" + text.getText().toString().length());
        String str = text.getText().toString().trim();
        if (text.getText().toString().indexOf(str) != -1) {
            text.setText(text.getText().toString() + " ");
            //当内容文本在最左端且内容文本的右端全是空格时，这种情况则为移动到最左端的尽头的情况（例：xxxxx--------------------------，-代表空格），那么把它的重力值设为左重力
            if (text.getText().toString().length() == MAX_Length_EditText && text.getText().toString().indexOf(str) == 0) {
                text.setGravity(GRAVITY_LEFT);
                text.setText(text.getText().toString().trim());
                controlKeepLeft.setImageResource(R.drawable.left_align_click);
                controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
                controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            }
        }
    }

    private void moveToRight(TextView text) {
        log.error("R该TextView里面的文本内容：" + text.getText().toString());
        String str = text.getText().toString().trim();
        if (text.getText().toString().indexOf(str) != -1 && text.getText().toString().indexOf(str) != 30 - str.length()) {
            text.setText(" " + text.getText().toString());
            //当内容文本在最右端且内容文本的左端全是空格时，这种情况则为移动到最右端的尽头的情况（例：-----------------------xxxxx，-代表空格），那么把它的重力值设为右重力
            if (text.getText().toString().indexOf(str) == MAX_Length_EditText - str.length()) {
                text.setGravity(GRAVITY_RIGHT);
                text.setText(text.getText().toString().trim());
                controlKeepLeft.setImageResource(R.drawable.left_align_click);
                controlKeepCenter.setImageResource(R.drawable.center_align_unclick);
                controlKeepRight.setImageResource(R.drawable.right_align_unclick);
            }
        }
    }

    private void setTextBackgroudColor() {
        templateHeader.setBackgroundColor(Color.WHITE);
        templateHeader.setTextColor(Color.BLACK);
        templateDocumentNumber.setBackgroundColor(Color.WHITE);
        templateDocumentNumber.setTextColor(Color.BLACK);
        templateDate.setBackgroundColor(Color.WHITE);
        templateDate.setTextColor(Color.BLACK);
        templateGoodsName.setBackgroundColor(Color.WHITE);
        templateGoodsNumber.setBackgroundColor(Color.WHITE);
        templateSubtotal.setBackgroundColor(Color.WHITE);
        templateGoodsNameLayout.setBackgroundColor(Color.WHITE);
        templateGoodsNumberLayout.setBackgroundColor(Color.WHITE);
        templateSubtotalLayout.setBackgroundColor(Color.WHITE);
        templateTotalMoney.setBackgroundColor(Color.WHITE);
        templateTotalMoney.setTextColor(Color.BLACK);
        templatePaymentMethod.setBackgroundColor(Color.WHITE);
        templatePaymentMethod.setTextColor(Color.BLACK);
        templateDiscount.setBackgroundColor(Color.WHITE);
        templateDiscount.setTextColor(Color.BLACK);
        templatePayable.setBackgroundColor(Color.WHITE);
        templatePayable.setTextColor(Color.BLACK);
        templatePaymentMethod1.setBackgroundColor(Color.WHITE);
        templatePaymentMethod1.setTextColor(Color.BLACK);
//        payment_method2.setBackgroundColor(Color.WHITE);
        templatePaymentMethod3.setBackgroundColor(Color.WHITE);
        templatePaymentMethod3.setTextColor(Color.BLACK);
        templateFooter1.setBackgroundColor(Color.WHITE);
        templateFooter1.setTextColor(Color.BLACK);
        templateFooter2.setBackgroundColor(Color.WHITE);
        templateFooter2.setTextColor(Color.BLACK);
        templateFooter3.setBackgroundColor(Color.WHITE);
        templateFooter3.setTextColor(Color.BLACK);
        templateFooter4.setBackgroundColor(Color.WHITE);
        templateFooter4.setTextColor(Color.BLACK);
        templateFooter5.setBackgroundColor(Color.WHITE);
        templateFooter5.setTextColor(Color.BLACK);
        templateFooter6.setBackgroundColor(Color.WHITE);
        templateFooter6.setTextColor(Color.BLACK);
        templateFooter7.setBackgroundColor(Color.WHITE);
        templateFooter7.setTextColor(Color.BLACK);
        templateFooter8.setBackgroundColor(Color.WHITE);
        templateFooter8.setTextColor(Color.BLACK);
        templateFooter9.setBackgroundColor(Color.WHITE);
        templateFooter9.setTextColor(Color.BLACK);
        templateFooter10.setBackgroundColor(Color.WHITE);
        templateFooter10.setTextColor(Color.BLACK);
        templateLogo.setBackgroundColor(Color.WHITE);
        templateTicketBottom.setBackgroundColor(Color.WHITE);
        templateTicketBottom.setTextColor(Color.BLACK);
    }

    /**
     * 点击小票格式的某一项时，根据是否是加粗的字体，显示加粗按钮的颜色
     *
     * @param textView
     */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setBoldButtonColor(TextView textView) {
        if (textView.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD))) {
//            controlBold.setBackgroundColor(Color.YELLOW);
            controlBold.setBackground(getResources().getDrawable(R.drawable.textview_rounded_click, null));
            controlBold.setTextColor(getResources().getColor(R.color.blue, null));
        } else {
//            controlBold.setBackgroundColor(Color.WHITE);
            controlBold.setBackground(getResources().getDrawable(R.drawable.textview_rounded_unclick, null));
            controlBold.setTextColor(getResources().getColor(R.color.text_bgcolor_darkgray, null));
        }
    }

    private void initSpinner(final int smallSheetFrameID) {
        /**
         * 从本地SQLite中查询到SyncDatetime不为1970的小票格式有几种
         * 根据返回的小票格式数量，动态添加List的值(版本i)
         * 使用的时候，选择需要的版本，根据版本号来显示预览，当点击保存就是修改所有零售单的小票格式
         * 把List设置到Adapter
         */
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("where F_SyncType != ?");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        log.info("Frame中的数据条数：" + frameList.size());
        smallSheetFrame_IDList = new ArrayList<>();
        for (int i = 0; i < frameList.size(); i++) {
            smallSheetFrame_IDList.add(frameList.get(i).getID());//存放所有的小票ID
        }

//        Collections.sort(smallSheetFrame_IDList);//小票ID升序排列
        Collections.reverse(smallSheetFrame_IDList);//小票ID降序排列

        String[] versionArray = new String[smallSheetFrame_IDList.size()];
        final Map<Integer, Integer> position = new HashMap<Integer, Integer>();
        for (int i = smallSheetFrame_IDList.size(); i > 0; i--) {
            versionArray[smallSheetFrame_IDList.size() - i] = "格式 " + i;
            position.put(Integer.valueOf(String.valueOf(smallSheetFrame_IDList.get(i - 1))), i - 1);
        }

        spinner_Adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item2, versionArray);
        spinner_Adapter.setDropDownViewResource(R.layout.spinner_item);
        printFormatVersion.setAdapter(spinner_Adapter);
        if (smallSheetFrameID == BaseSQLiteBO.INVALID_INT_ID || !position.containsKey(smallSheetFrameID)) { // 如果查询SQLite出错，则会传-1，这里对它进行处理 || 如果是还没同步就进去小票页面创建，这里对它进行处理
            printFormatVersion.setSelection(0);
            SelectPosition = 0;
        } else {
            printFormatVersion.post(new Runnable() {    //必须使用post去setselection，否则setSelection会失效;
                @Override
                public void run() {
                    printFormatVersion.setSelection(position.get(smallSheetFrameID)); // 选择显示下拉框的哪个item
                }
            });
            SelectPosition = position.get(smallSheetFrameID);   //将选择的position保存下来
        }
    }

    /**
     * 进入SmallSheetActivity.java，从本地SQLite查找多少条数据，如果没有小票格式存在就需要展示空白的小票格式，否则展示最新的小票格式
     */
    private void initSmallSheetData(int smallSheetFrameID) {
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setSql("where F_SyncType != ?");
        smallSheetFrame.setConditions(new String[]{BasePresenter.SYNC_Type_D});
        List<SmallSheetFrame> frameList = (List<SmallSheetFrame>) smallSheetFramePresenter.retrieveNObjectSync(CASE_SmallSheetFrame_RetrieveNByConditions, smallSheetFrame);
        if (frameList != null) {
            if (frameList.size() > 0) {
                initSpinner(smallSheetFrameID);
            } else {
                initRightContent();
//                smallsheet_update.setVisibility(View.GONE);
//                smallsheet_delete.setVisibility(View.GONE);
//                useForm.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 在UI展示选中版本的小票格式
     *
     * @param position 点击Spinner，选中的item的position
     */
    private void showSelectedSmallSheet(int position) {
        selectedFrameID = smallSheetFrame_IDList.get(position);
        SmallSheetFrame smallSheetFrame = new SmallSheetFrame();
        smallSheetFrame.setID(selectedFrameID);
        //找到对应的SmallSheetFrame
        smallSheetFrame = (SmallSheetFrame) smallSheetFramePresenter.retrieve1ObjectSync(BaseSQLiteBO.INVALID_CASE_ID, smallSheetFrame);
        //找到对应的SmallSheetText
        smallSheetFrame.setSql(" where F_FrameID = ? ");
        smallSheetFrame.setConditions(new String[]{String.valueOf(smallSheetFrame.getID())});
        List<SmallSheetText> smallSheetTextList = (List<SmallSheetText>) smallSheetTextPresenter.retrieveNObjectSync(BaseSQLiteBO.CASE_SmallSheetText_RetrieveNByConditions, smallSheetFrame);
        System.out.println("====smallSheetTextList:" + smallSheetTextList);
        if (smallSheetTextList.size() != 20) {
            //弹窗  点击确定  删除该小票格式
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage("该小票格式暂不可用")
                    .setTitle("提示")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            smallsheet_delete.performClick();
                        }
                    })
                    .show();

        } else {
            // 初始化左边TextView的Text，防止其他小票格式修改时残留
            initTemplateSmallSheet(smallSheetTextList);
            showSelectedSmallSheetInUI(smallSheetFrame, smallSheetTextList);
        }
    }


    //以下部分都是选择顶部logo部分

    /**
     * 根据uri打开裁剪框 因为大小限制，裁剪大图不能直接将数据返回 该方法返回裁剪的图片的uri，不直接返回数据，无大小限制
     *
     * @param uri
     */
    private void clipBigPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 可以选择图片类型，如果是*表明所有类型的图片
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 2);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        // 裁剪时是否保留图片的比例，这里的比例是1:1
        intent.putExtra("scale", true);
        // 是否是圆形裁剪区域，设置了也不一定有效
        // intent.putExtra("circleCrop", true);
        // 设置输出的格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // 是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);// 得到裁剪数据时，自动在该uri路径上保存了图片
        startActivityForResult(intent, CUT_BIG_OK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case CUT_BIG_REQUEST:
                if (null != data) {
                    selectImg = data.getData();
                    clipBigPhoto(selectImg);
                }
                break;
            case CUT_BIG_OK:
                bmpFrameLogo = decodeUriAsBitmap(imageUri);
                if (bmpFrameLogo == null) {
                    break;
                }
                String str_bm = GeneralUtil.bitmapToString(bmpFrameLogo);
                log.info("图片转成String的大小" + str_bm.length());

                if (str_bm.length() > 163840) { //... 公共配置表中有配置图片大小。
                    Toast.makeText(getActivity(), "图片太大", Toast.LENGTH_SHORT).show();
                    bmpFrameLogo = null;
                    templateLogo.setImageBitmap(null);
                    templateLogoLayout.setVisibility(View.GONE);
                    designIfTopLogo.setText("无");
                    str_bm = "";
                } else {
                    templateLogo.setImageBitmap(bmpFrameLogo);
                    ImgUtil.writeFileByBitmap2(bmpFrameLogo);
                }
//                try {
//                    File file = new File(new URI(imageUri.toString()));
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }

                deleFile(imageUri);
                cameraFile.delete();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 删除指定文件
     *
     * @param uri 要删除的文件的uri
     */
    private void deleFile(Uri uri) {
        try {
            File file = new File(new URI(uri.toString()));
            log.info("输出：图片路径：" + file.toString());
            if (file.exists()) {
                file.delete();
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            log.info("decodeUriAsBitmap");
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private int getBitmapMemory(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        return baos.toByteArray().length / 1024;
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 20) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;// 每次都减少10
            log.info("压缩：" + baos.toByteArray().length / 1024);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 从UI上获得smallSheetText的值
     *
     * @return
     */
    protected List<SmallSheetText> getSmallSheetTextFromView() {
        smallSheetTextLists.clear();

        setSmallSheetText(templateHeader);
        setSmallSheetText(templateDocumentNumber);
        setSmallSheetText(templateDate);
        setSmallSheetText(templateTotalMoney);
        setSmallSheetText(templatePaymentMethod);
        setSmallSheetText(templateDiscount);
        setSmallSheetText(templatePayable);
        setSmallSheetText(templatePaymentMethod1);
//        setSmallSheetText(payment_method2);
        setSmallSheetText(templatePaymentMethod3);
        setSmallSheetText(templateFooter1);
        setSmallSheetText(templateFooter2);
        setSmallSheetText(templateFooter3);
        setSmallSheetText(templateFooter4);
        setSmallSheetText(templateFooter5);
        setSmallSheetText(templateFooter6);
        setSmallSheetText(templateFooter7);
        setSmallSheetText(templateFooter8);
        setSmallSheetText(templateFooter9);
        setSmallSheetText(templateFooter10);
        setSmallSheetText(templateTicketBottom);

        return smallSheetTextLists;
    }

    private SmallSheetText setSmallSheetText(TextView textView) {
        SmallSheetText smallSheetText = new SmallSheetText();
        smallSheetText.setContent(textView.getText().toString());
        smallSheetText.setSize(textView.getTextSize());
        log.info(textView.getTextSize());
        if (textView.getTypeface().equals(Typeface.defaultFromStyle(Typeface.BOLD))) {
            smallSheetText.setBold(1);
        } else {
            smallSheetText.setBold(0);
        }
        smallSheetText.setGravity(textView.getGravity());
        smallSheetTextLists.add(smallSheetText);
        return smallSheetText;
    }

    private void delete_footer(TextView textView, EditText editText, RelativeLayout layout, View view) {
        textView.setVisibility(View.GONE);
        textView.setText(defaultText);
        editText.setText(defaultText);
        layout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        showSelectedSmallSheet(position);
        setAddFooterView(); // 打开新的小票格式后，旧的小票格式可能残留了+号不可用，必须令其可用
        SelectPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: // 创建
                    initSpinner(Integer.valueOf(String.valueOf(selectedFrameID)));
                    Toast.makeText(getActivity(), "创建小票格式成功", Toast.LENGTH_SHORT).show();
                    break;
                case 5: // 同步到本地成功后
                    initSpinner(currentSmallSheetID);
                    Toast.makeText(getActivity(), "同步小票格式成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2: // 刷新小票格式页面(删除,使用该格式操作后)
                    initSpinner(currentSmallSheetID);
                    initSmallSheetData(currentSmallSheetID);
                    smallsheet_delete.setEnabled(true);
                    closeLoadingDialog(loadingDailog);
                    break;
                case 3: // 用于中途断网的消息提示
                    smallsheet_delete.setEnabled(true);
                    initSmallSheetData(Integer.valueOf(String.valueOf(selectedFrameID))); // 重新加载小票格式
                    closeLoadingDialog(loadingDailog);
                    Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    // 更新成功后，刷新小票格式页面
                    initSpinner(Integer.valueOf(String.valueOf(selectedFrameID)));
                    initSmallSheetData(Integer.valueOf(String.valueOf(selectedFrameID)));
                    smallsheet_delete.setEnabled(true);
                    Toast.makeText(getActivity(), "修改小票格式成功", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    initSpinner(currentSmallSheetID);
                    initSmallSheetData(currentSmallSheetID);
                    smallsheet_delete.setEnabled(true);
                    closeLoadingDialog(loadingDailog);
                    if ((GlobalController.getInstance().getSessionID() == null || !NetworkUtils.isNetworkAvalible(getActivity().getApplicationContext()))) {
                        Toast.makeText(getActivity(), "网络连接错误，你的操作已在本地生效", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "删除小票格式成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    // 对于输入的是英文，getBytes("gbk")、getBytes("utf-8")的长度分别是1、1
    // 对于输入的是中文，getBytes("gbk")、getBytes("utf-8")的长度分别是1、1分别是3、2
    // 所以getBytes("gbk")、getBytes("utf-8")的长度不是上面的情况，一般是表情包等特殊字符
    // isEmojiCharacter()方法判断不出来的特殊表情包，目前只发现一个,就是getBytes("utf-8")长度等于6
    // 并且getBytes("gbk")长度等于2的表情包
    // 如果发现新的能输入的表情包，可以继续添加判断
    public static boolean isSpecialEmoji(CharSequence input) {
        try {
            return (input.toString().getBytes("utf-8").length == 6 && input.toString().getBytes("gbk").length == 2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        currentSmallSheetID = BaseSQLiteBO.INVALID_INT_ID;
        selectedFrameID = null;
        //  smallSheetTextPresenter = null;
        //smallSheetFramePresenter = null;
        SelectPosition = 0;
        spinner_Adapter = null;
    }
    public void setAddFooterView() {
        if(designFooter2_layout.getVisibility() == View.VISIBLE && designFooter2_layout.getVisibility() == View.VISIBLE
                && designFooter3Layout.getVisibility() == View.VISIBLE && designFooter4Layout.getVisibility() == View.VISIBLE
                && designFooter4Layout.getVisibility() == View.VISIBLE && designFooter5Layout.getVisibility() == View.VISIBLE
                && designFooter6Layout.getVisibility() == View.VISIBLE && designFooter7Layout.getVisibility() == View.VISIBLE
                && designFooter8Layout.getVisibility() == View.VISIBLE && designFooter9Layout.getVisibility() == View.VISIBLE
                && designFooter9Layout.getVisibility() == View.VISIBLE && designFooter10Layout.getVisibility() == View.VISIBLE
        ) {
            add_footer.setVisibility(View.GONE);
        } else {
            add_footer.setVisibility(View.VISIBLE);
        }
    }
}
