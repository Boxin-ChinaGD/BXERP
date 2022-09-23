package com.bx.erp.view.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Scroller;

import com.bx.erp.R;

/**
 * Created by WPNA on 2020/3/29.
 * 此类定义数字和英文字符的键盘，抛弃原生虚拟键盘
 */

public class Number_EnglishBoardDialog extends Dialog implements Handler.Callback, Num_EngKeyboard.OnKeyBoardclickListener {

    private Num_EngKeyboard num_engKeyboard;
    private int deviationX,deviationY;
    private String gettext;
    private boolean canSeeSymbol,canSeeEn;

    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;
    private Scroller scroller;
    private Handler handler;
    private int oldx,oldy;
    private final static int SCROLLER_HANDLER = 0x711;
    private float startx, starty;
    float changeX;
    float changeY;
    private Display display;

    private static Number_EnglishBoardDialog dialog ;
    private outputStringListener outputListener;

    public static Number_EnglishBoardDialog createdialog(FragmentActivity activity){
        if (dialog == null){
            synchronized (Number_EnglishBoardDialog.class){
                if (dialog == null){
                    dialog = new Number_EnglishBoardDialog(activity);
                }
            }
        }
        return dialog;
    }

    public Number_EnglishBoardDialog setGettext(String gettext) {
        this.gettext = gettext;
        return dialog;
    }

    public Number_EnglishBoardDialog setCanSeeEn(boolean canSeeEn) {
        this.canSeeEn = canSeeEn;
        return dialog;
    }

    public Number_EnglishBoardDialog setCanSeeSymbol(boolean canSeeSymbol) {
        this.canSeeSymbol = canSeeSymbol;
        return dialog;
    }

    public Number_EnglishBoardDialog setDeviation(int x,int y){
        deviationX = x;
        deviationY = y;
        return dialog;
    }

    public static Number_EnglishBoardDialog getdialog(){
        return dialog;
    }

    public Number_EnglishBoardDialog setOutputListener(outputStringListener outputListener) {
        this.outputListener = outputListener;
        return dialog;
    }

    public void setdialogcancel(){
        dialog = null;
    }

    private Number_EnglishBoardDialog(FragmentActivity activity) {
        super(activity);
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        scroller = new Scroller(activity);
        handler = new Handler(this);
        WindowManager manager = activity.getWindowManager();
        display = manager.getDefaultDisplay();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除局部背景
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);//透明背景
        View decorView = window.getDecorView();      //隐藏虚拟按键全屏显示
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.number_englishboarddialog);
        num_engKeyboard = findViewById(R.id.num_engkeyboard);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        layoutParams = (WindowManager.LayoutParams) getWindow().getDecorView().getLayoutParams();
        layoutParams.width = (int) (display.getWidth() * 0.5);//设置宽
        layoutParams.height = (int) (display.getHeight() * 0.3);//设置高
        windowManager.updateViewLayout(getWindow().getDecorView(),layoutParams);
        oldx = layoutParams.x;
        oldy = layoutParams.y;

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (display.getWidth() * 0.5);//设置宽
        params.height = (int) (display.getHeight() * 0.3);//设置高
        params.dimAmount = 0f;
        if (deviationY > 1080 / 2){
            params.y = deviationY / 2;
        }else {
            params.y = deviationY * 2;
        }
        params.x = deviationX;

        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.LEFT|Gravity.TOP);

        num_engKeyboard.setOnNumberclickListener(this);
        num_engKeyboard.setOnSureListener(new Num_EngKeyboard.OnSureListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });

        num_engKeyboard.setInputstring(gettext);
        num_engKeyboard.setCanSeeEn(canSeeEn);
        num_engKeyboard.setCanSeeSymbol(canSeeSymbol);
    }

    public void Showdialog(){
        dialog.show();
    }

    @Override
    public void dismiss() {
        scroller.abortAnimation();
        handler.removeMessages(SCROLLER_HANDLER);
        super.dismiss();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = event.getX();
                starty = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                changeX = event.getX() - startx;
                changeY = event.getY() - starty;
                translateXY(changeX,changeY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
        }
        return super.onTouchEvent(event);
    }

    private void translateXY(float x, float y) {
        //x轴超出屏幕时回归
        if (layoutParams.x > display.getWidth() * 0.5){
            layoutParams.x = (int) (display.getWidth() * 0.5);
        }else if (layoutParams.x <0){
            layoutParams.x = 0;
        }
        //y轴超出屏幕时回归
        if (layoutParams.y > display.getWidth() - display.getHeight() * 0.3){
            layoutParams.y = (int) (display.getHeight() * 0.3);
        }else if (layoutParams.y < 0){
            layoutParams.y = 0;
        }
        scroller.startScroll(layoutParams.x, layoutParams.y, (int) x, (int) y);
        handler.obtainMessage(SCROLLER_HANDLER).sendToTarget();
    }

    //用于拖动后将键盘弹回原位置
    public void BackTo() {
        scroller.startScroll(layoutParams.x, layoutParams.y, oldx - layoutParams.x, oldy - layoutParams.y);
        handler.obtainMessage(SCROLLER_HANDLER).sendToTarget();
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == SCROLLER_HANDLER && scroller.computeScrollOffset()) {
            layoutParams.x = scroller.getCurrX();
            layoutParams.y = scroller.getCurrY();
            windowManager.updateViewLayout(getWindow().getDecorView(), layoutParams);
            handler.obtainMessage(SCROLLER_HANDLER).sendToTarget();
        }
        return false;
    }

    //数字被点击后，从此处传入，再从接口中传出到edittext
    @Override
    public void onClick(String input) {
        if (outputListener != null)
            outputListener.outputString(input);
    }

    public interface outputStringListener{
        void outputString(String output);
    };




}
