package com.bx.erp.view.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bx.erp.R;

/**
 * Created by WPNA on 2020/3/12.
 */

public class KeyBoard extends GridLayout implements View.OnClickListener {
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t0,t10,t20,t50,t100,t_point;
    LinearLayout delete;
    private OnNumberclickListener onclickListener;
    private String add_number="";//金额或数量的edittext的值
    private boolean lastClickIsRMB = false;

    public KeyBoard(Context context) {
        super(context);
    }

    public KeyBoard(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public KeyBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.keyboard,this);
        Findview();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KeyBoard);
        boolean arrayBoolean = array.getBoolean(R.styleable.KeyBoard_canseemoney, false);
        if (arrayBoolean){
            t10.setVisibility(VISIBLE);
            t20.setVisibility(VISIBLE);
            t50.setVisibility(VISIBLE);
            t100.setVisibility(VISIBLE);
        }
    }


    private void Findview() {
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        t6 = findViewById(R.id.t6);
        t7 = findViewById(R.id.t7);
        t8 = findViewById(R.id.t8);
        t9 = findViewById(R.id.t9);
        t0 = findViewById(R.id.t0);
        t10 = findViewById(R.id.t10);
        t20 = findViewById(R.id.t20);
        t50 = findViewById(R.id.t50);
        t100 = findViewById(R.id.t100);
        t_point = findViewById(R.id.t_point);
        delete = findViewById(R.id.delect);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        t9.setOnClickListener(this);
        t0.setOnClickListener(this);
        t10.setOnClickListener(this);
        t20.setOnClickListener(this);
        t50.setOnClickListener(this);
        t100.setOnClickListener(this);
        t_point.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.t1:
                ClickNum("1");
                break;
            case R.id.t2:
                ClickNum("2");
                break;
            case R.id.t3:
                ClickNum("3");
                break;
            case R.id.t4:
                ClickNum("4");
                break;
            case R.id.t5:
                ClickNum("5");
                break;
            case R.id.t6:
                ClickNum("6");
                break;
            case R.id.t7:
                ClickNum("7");
                break;
            case R.id.t8:
                ClickNum("8");
                break;
            case R.id.t9:
                ClickNum("9");
                break;
            case R.id.t0:
                ClickNum("0");
                break;
            case R.id.t10:
                ClickRMB("10");
                break;
            case R.id.t20:
                ClickRMB("20");
                break;
            case R.id.t50:
                ClickRMB("50");
                break;
            case R.id.t100:
                ClickRMB("100");
                break;
            case R.id.t_point:
                if (!add_number.equals("")){
                    if (add_number.indexOf(".") == -1){
                        setNumber(".");
                    }
                }
                break;
            case R.id.delect:
                if (add_number!=null && !add_number.equals("")){
                    add_number = add_number.substring(0,add_number.length()-1);
                    onclickListener.onClick(add_number);
                }
                break;
        }
    }

    //点击键盘人民币时
    private void ClickRMB(String RMB) {
        if (add_number.equals("")) {
            setNumber(RMB);
        } else {
            add_number = "";
            setNumber(RMB);
        }
        lastClickIsRMB = true;
    }
    //点击键盘数字时
    private void ClickNum(String Num) {
        if (lastClickIsRMB){
            add_number = "";
            setNumber(Num);
            lastClickIsRMB = false;
        }else {
            setNumber(Num);
        }
    }

    private void setNumber(String s) {
        if (onclickListener!=null){
            //限制：第一位为0的时候，后面只能输入小数点
            if (!"0".equals(add_number)) {
                //限制只能输两位小数
                if (add_number.indexOf(".") == -1
                        || (add_number.indexOf(".") > -1
                        && (add_number.length() - add_number.indexOf(".")) < 3)) {
                    add_number += s;
                }
                onclickListener.onClick(add_number);
            } else if (".".equals(s)) {
                if (add_number.indexOf(".") == -1
                        || (add_number.indexOf(".") > -1
                        && (add_number.length() - add_number.indexOf(".")) < 3)) {    //没有满足这个条件.查看为什么需要这个条件
                    add_number += s;
                }
                onclickListener.onClick(add_number);
            }
        }

    }

    public interface OnNumberclickListener{
        void onClick(String number);
    }

    public void setOnNumberclickListener(OnNumberclickListener listener){
        onclickListener = listener;
    }

    public void cleanNumber() {
        add_number = "";
    }
}
