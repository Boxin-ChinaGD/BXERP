package com.bx.erp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bx.erp.R;
import com.bx.erp.event.BaseEvent;
import com.bx.erp.event.VipHttpEvent;
import com.sunmi.printerhelper.utils.AidlUtil;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import sunmi.sunmiui.dialog.DialogCreater;
import sunmi.sunmiui.dialog.ListDialog;

/**
 * 提供测试打印机的界面。
 */
public class PrinterActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private Logger log = Logger.getLogger(this.getClass());
    private TextView mTextView1, mTextView2;
    private CheckBox mCheckBox1, mCheckBox2;
    private EditText mEditText;
    private LinearLayout mLayout, mLinearLayout;
    private int record;
    private boolean isBold, isUnderLine;

    private String[] mStrings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "CP928", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "Windows-775", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer);

        record = 17;
        isBold = false;
        isUnderLine = false;
        mTextView1 = (TextView) findViewById(R.id.text_text_character);
        mTextView2 = (TextView) findViewById(R.id.text_text_size);
        mCheckBox1 = (CheckBox) findViewById(R.id.text_bold);
        mCheckBox2 = (CheckBox) findViewById(R.id.text_underline);
        mEditText = (EditText) findViewById(R.id.text_text);

        mLinearLayout = (LinearLayout) findViewById(R.id.text_all);
        mLayout = (LinearLayout) findViewById(R.id.text_set);

        mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mLinearLayout.getWindowVisibleDisplayFrame(r);
                if (r.bottom < 800) {
                    mLayout.setVisibility(View.VISIBLE);
                } else {
                    mLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mCheckBox1.setOnCheckedChangeListener(this);
        mCheckBox2.setOnCheckedChangeListener(this);


        findViewById(R.id.text_character).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListDialog listDialog = DialogCreater.createListDialog(PrinterActivity.this, getResources().getString(R.string.characterset), getResources().getString(R.string.cancel), mStrings);
                listDialog.setItemClickListener(new ListDialog.ItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        mTextView1.setText(mStrings[position]);
                        record = position;
                        listDialog.cancel();
                    }
                });
                listDialog.show();
            }
        });

        findViewById(R.id.text_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSeekBarDialog(PrinterActivity.this, getResources().getString(R.string.size_text), 12, 36, mTextView2);
            }
        });

//        findViewById(R.id.printer_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PrinterActivity.this, SetupActivity.class);
//                PrinterActivity.this.finish();
//                startActivity(intent);
//            }
//        });
        AidlUtil.getInstance().initPrinter();
    }

    public void onClick(View view) throws RemoteException {
        String content = mEditText.getText().toString();

        float size = Integer.parseInt(mTextView2.getText().toString());
        if (appApplication.isAidl()) {
            AidlUtil.getInstance().linewrap(3);
            AidlUtil.getInstance().printText(content, size, isBold, 17, isUnderLine);
            AidlUtil.getInstance().linewrap(6);
            AidlUtil.getInstance().cutPaper();
        } else {
        }
    }

    /**
     * 自定义的seekbar dialog
     *
     * @param context
     * @param title
     * @param min
     * @param max
     * @param set
     */
    private void showSeekBarDialog(Context context, String title, final int min, final int max, final TextView set) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.printer_textsize_dailog, null);
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
                set.setText(tv_result.getText());
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.text_bold:
                isBold = isChecked;
                break;
            case R.id.text_underline:
                isUnderLine = isChecked;
                break;
            default:
                break;
        }
    }

    //设置全屏显示。隐藏通知栏和底部虚拟按键，不需要考虑到activity的生命周期
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(VipHttpEvent event) {
        if (event.getId() == BaseEvent.EVENT_ID_PrinterActivity) {

        } else {
            StackTraceElement ste = new Exception().getStackTrace()[1];
            log.info(ste.getClassName() + "." + ste.getMethodName() + "未处理的Event，ID=" + event.getId());
        }
    }
}
