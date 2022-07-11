package com.bx.erp.view.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bx.erp.R;


/**
 * Created by WPNA on 2020/3/29.
 */

public class Num_EngKeyboard extends LinearLayout implements View.OnClickListener {
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t0,num_change_symbol,num_sure,num_point,
            tq,tw,te,tr,tt,ty,tu,ti,to,tp,ta,ts,td,tf,tg,th,tj,tk,tl,tz,tx,tc,tv,tb,tn,tm,num_change_en,en_change_num,
            en_sure,en_point,symbol_change_num,symbol_change_en,en_change_symbol,
            symbol_1,symbol_2,symbol_3,symbol_4,symbol_5,symbol_6,symbol_7,symbol_8,symbol_9,symbol_10,symbol_11,symbol_12,symbol_13,
            symbol_14,symbol_15,symbol_16,symbol_17,symbol_18,symbol_19,symbol_20;
    LinearLayout num_delete,symbol_delete;
    View numberkeyboard,english_keyboard,symbolkeyboard;
    ImageView english_delete,Capitalization,en_space,num_space,symbol_space;
    private OnKeyBoardclickListener onclickListener;
    private OnSureListener sureListener;
    private String inputstring="";
    private boolean canSeeSymbol = true;//设置是否符号键盘可见，可用来限制输入
    private boolean canSeeEn = true;//设置是否英文键盘可见

    public Num_EngKeyboard(Context context) {
        super(context);
    }

    public Num_EngKeyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Num_EngKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.number_english_keyboard, this);
        Findview();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!canSeeEn){ //判断是否显示英文键盘
            numberkeyboard.setVisibility(VISIBLE);
            english_keyboard.setVisibility(GONE);
        }
    }

    public void setCanSeeSymbol(boolean canSeeSymbol) {
        this.canSeeSymbol = canSeeSymbol;
    }

    public void setCanSeeEn(boolean canSeeEn) {
        this.canSeeEn = canSeeEn;
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
        en_space = findViewById(R.id.en_space);
        num_space = findViewById(R.id.num_space);
        symbol_space = findViewById(R.id.symbol_space);
        num_change_symbol = findViewById(R.id.num_change_symbol);
        num_sure = findViewById(R.id.num_sure);
        en_sure = findViewById(R.id.en_sure);
        en_point = findViewById(R.id.en_point);
        tq = findViewById(R.id.t_q);
        tw = findViewById(R.id.t_w);
        te = findViewById(R.id.t_e);
        tr = findViewById(R.id.t_r);
        tt = findViewById(R.id.t_t);
        ty = findViewById(R.id.t_y);
        tu = findViewById(R.id.t_u);
        ti = findViewById(R.id.t_i);
        to = findViewById(R.id.t_o);
        tp = findViewById(R.id.t_p);
        ta = findViewById(R.id.t_a);
        ts = findViewById(R.id.t_s);
        td = findViewById(R.id.t_d);
        tf = findViewById(R.id.t_f);
        tg = findViewById(R.id.t_g);
        th = findViewById(R.id.t_h);
        tj = findViewById(R.id.t_j);
        tk = findViewById(R.id.t_k);
        tl = findViewById(R.id.t_l);
        tz = findViewById(R.id.t_z);
        tx = findViewById(R.id.t_x);
        tc = findViewById(R.id.t_c);
        tv = findViewById(R.id.t_v);
        tb = findViewById(R.id.t_b);
        tn = findViewById(R.id.t_n);
        tm = findViewById(R.id.t_m);
        english_delete = findViewById(R.id.english_delete);
        Capitalization = findViewById(R.id.Capitalization);
        num_change_en = findViewById(R.id.num_change_en);
        symbol_change_num = findViewById(R.id.symbol_change_num);
        symbol_change_en = findViewById(R.id.symbol_change_en);
        en_change_symbol = findViewById(R.id.en_change_symbol);
        num_point = findViewById(R.id.num_point);
        num_delete = findViewById(R.id.num_delete);
        english_keyboard = findViewById(R.id.englishkeyboard);
        numberkeyboard = findViewById(R.id.numberkeyboard);
        symbolkeyboard = findViewById(R.id.symbolkeyboard);
        en_change_num = findViewById(R.id.en_change_num);
        symbol_delete = findViewById(R.id.symbol_delete);
        symbol_1= findViewById(R.id.symbol_1);
        symbol_2= findViewById(R.id.symbol_2);
        symbol_3= findViewById(R.id.symbol_3);
        symbol_4= findViewById(R.id.symbol_4);
        symbol_5= findViewById(R.id.symbol_5);
        symbol_6= findViewById(R.id.symbol_6);
        symbol_7= findViewById(R.id.symbol_7);
        symbol_8= findViewById(R.id.symbol_8);
        symbol_9= findViewById(R.id.symbol_9);
        symbol_10= findViewById(R.id.symbol_10);
        symbol_11= findViewById(R.id.symbol_11);
        symbol_12= findViewById(R.id.symbol_12);
        symbol_13= findViewById(R.id.symbol_13);
        symbol_14= findViewById(R.id.symbol_14);
        symbol_15= findViewById(R.id.symbol_15);
        symbol_16= findViewById(R.id.symbol_16);
        symbol_17= findViewById(R.id.symbol_17);
        symbol_18= findViewById(R.id.symbol_18);
        symbol_19= findViewById(R.id.symbol_19);
        symbol_20= findViewById(R.id.symbol_20);

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
        num_point.setOnClickListener(this);
        num_delete.setOnClickListener(this);
        num_change_symbol.setOnClickListener(this);
        num_sure.setOnClickListener(this);
        en_space.setOnClickListener(this);
        num_space.setOnClickListener(this);
        symbol_space.setOnClickListener(this);
        en_sure.setOnClickListener(this);

        tq.setOnClickListener(this);
        tw.setOnClickListener(this);
        te.setOnClickListener(this);
        tr.setOnClickListener(this);
        tt.setOnClickListener(this);
        ty.setOnClickListener(this);
        tu.setOnClickListener(this);
        ti.setOnClickListener(this);
        to.setOnClickListener(this);
        tp.setOnClickListener(this);
        ta.setOnClickListener(this);
        ts.setOnClickListener(this);
        td.setOnClickListener(this);
        tf.setOnClickListener(this);
        tg.setOnClickListener(this);
        th.setOnClickListener(this);
        tj.setOnClickListener(this);
        tk.setOnClickListener(this);
        tl.setOnClickListener(this);
        tz.setOnClickListener(this);
        tx.setOnClickListener(this);
        tc.setOnClickListener(this);
        tv.setOnClickListener(this);
        tb.setOnClickListener(this);
        tn.setOnClickListener(this);
        tm.setOnClickListener(this);
        english_delete.setOnClickListener(this);
        symbol_delete.setOnClickListener(this);
        num_change_en.setOnClickListener(this);
        symbol_change_num.setOnClickListener(this);
        symbol_change_en.setOnClickListener(this);
        en_change_symbol.setOnClickListener(this);
        Capitalization.setOnClickListener(this);
        en_change_num.setOnClickListener(this);
        en_point.setOnClickListener(this);
        symbol_1.setOnClickListener(this);
        symbol_2.setOnClickListener(this);
        symbol_3.setOnClickListener(this);
        symbol_4.setOnClickListener(this);
        symbol_5.setOnClickListener(this);
        symbol_6.setOnClickListener(this);
        symbol_7.setOnClickListener(this);
        symbol_8.setOnClickListener(this);
        symbol_9.setOnClickListener(this);
        symbol_10.setOnClickListener(this);
        symbol_11.setOnClickListener(this);
        symbol_12.setOnClickListener(this);
        symbol_13.setOnClickListener(this);
        symbol_14.setOnClickListener(this);
        symbol_15.setOnClickListener(this);
        symbol_16.setOnClickListener(this);
        symbol_17.setOnClickListener(this);
        symbol_18.setOnClickListener(this);
        symbol_19.setOnClickListener(this);
        symbol_20.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.en_sure://英文键盘的确定
            case R.id.num_sure://数字键盘的确定
                sureListener.onClick();
                break;
            case R.id.symbol_space://符号键盘空格
            case R.id.num_space://数字键盘的空格
            case R.id.en_space://英文键盘的空格
                setInput(" ");
                break;
            case R.id.symbol_delete://符号键盘删除
            case R.id.english_delete://英文键盘删除
            case R.id.num_delete://数字键盘的删除
                if (inputstring!=null && !inputstring.equals("")){
                    inputstring = inputstring.substring(0,inputstring.length()-1);
                    onclickListener.onClick(inputstring);
                }
                break;
            case R.id.Capitalization://大小写
                break;
            case R.id.symbol_change_en://符号键盘转英文
            case R.id.num_change_en://数字键盘转英文键盘按钮
                if (canSeeEn) {
                    english_keyboard.setVisibility(VISIBLE);
                    numberkeyboard.setVisibility(GONE);
                    symbolkeyboard.setVisibility(GONE);
                }
                break;
            case R.id.symbol_change_num://符号键盘转数字
            case R.id.en_change_num://英文键盘转数字键盘按钮
                english_keyboard.setVisibility(GONE);
                numberkeyboard.setVisibility(VISIBLE);
                symbolkeyboard.setVisibility(GONE);
                break;
            case R.id.num_change_symbol://数字转符号
            case R.id.en_change_symbol://英文转符号
                if (canSeeSymbol) {
                    english_keyboard.setVisibility(GONE);
                    numberkeyboard.setVisibility(GONE);
                    symbolkeyboard.setVisibility(VISIBLE);
                }
                break;
            default:
                TextView textView = (TextView)view;
                setInput(textView.getText().toString());
        }
    }

    private void setInput(String s) {
        if (onclickListener!=null) {
            inputstring += s;
            onclickListener.onClick(inputstring);
        }
    }

    public void setInputstring(String inputstring){
        this.inputstring = inputstring;
    }

    public interface OnKeyBoardclickListener{
        void onClick(String input);
    }//输入字符按键的接口

    public interface OnSureListener{
        void onClick();
    }//确定按钮的接口

    public void setOnNumberclickListener(OnKeyBoardclickListener listener){
        onclickListener = listener;
    }

    public void setOnSureListener(OnSureListener listener){
        sureListener = listener;
    }
}
