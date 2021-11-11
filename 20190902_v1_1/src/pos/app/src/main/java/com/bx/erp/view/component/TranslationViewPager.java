package com.bx.erp.view.component;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TranslationViewPager {

    private ViewPager viewPager;
    private List<Integer> image_id;
    private List<ImageView> ImageView = new ArrayList<>();//所有的图片的集合
    private List<ImageView> Check_and_UnCheck = new ArrayList<>();//所有的圆点ImageView的集合
    private Timer timer;
    private TimerTask timerTask;
    private List<Integer> Check_and_UnCheck_Image;
    private Handlers handler = new Handlers();
    private Adapter adapter;
    private Context context;


    /**
     *
     * @param viewPager     滑动控件
     * @param image_id      轮播图图片
     * @param Check_and_UnCheck_Image       选择和未选择的小圆点图片集合,第一个为未选择图片id，第二个为选择图片id
     */
    public TranslationViewPager(Context context, ViewPager viewPager, List<Integer> image_id, List<Integer> Check_and_UnCheck_Image, LinearLayout pointLayout){
        this.context = context;
        this.viewPager = viewPager;
        this.image_id = image_id;
//        this.ImageView =ImageView;
        this.Check_and_UnCheck_Image = Check_and_UnCheck_Image;
//        this.Check_and_UnCheck = Check_and_UnCheck;

        for (int i = 0; i < image_id.size(); i++) {     //循环添加图片控件到集合中
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(params);
            imageView.setImageResource(image_id.get(i));
            this.ImageView.add(imageView);
        }

        for (int i = 0; i < image_id.size()-2; i++) {
            ImageView pointView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.rightMargin = 40;

            pointView.setLayoutParams(layoutParams);
            pointView.setImageResource(Check_and_UnCheck_Image.get(0));
            pointLayout.addView(pointView);
            Check_and_UnCheck.add(pointView);
        }
    }

    public void Create_Translation_Image() {
        adapter = new Adapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1,false);

        OnPageChangeListener();

        ViewPager_Touch();

        if (timer == null){
            TimeTask(2000);
        }
    }

    private void OnPageChangeListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < Check_and_UnCheck.size(); i++) {//将所有圆点设置为未选择状态
                    Check_and_UnCheck.get(i).setImageResource(Check_and_UnCheck_Image.get(0));
                }
                    if (position == 0){
                        Check_and_UnCheck.get(Check_and_UnCheck.size()-1).setImageResource(Check_and_UnCheck_Image.get(1));
                    }else if (position == image_id.size()-1){
                        Check_and_UnCheck.get(0).setImageResource(Check_and_UnCheck_Image.get(1));
                    }else {
                        Check_and_UnCheck.get(position-1).setImageResource(Check_and_UnCheck_Image.get(1));
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state==0) {
                    if (viewPager.getCurrentItem() == 0) {

                        viewPager.setCurrentItem(image_id.size()-2, false);
                    }
                    if (viewPager.getCurrentItem() == image_id.size()-1){
                        viewPager.setCurrentItem(1, false);
                    }
                }
            }
        });
    }

    private void ViewPager_Touch() {
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        timer.cancel();
                        timer.purge();
                        timerTask.cancel();

                        break;

                    case MotionEvent.ACTION_UP:

                        TimeTask(2000);

                        break;

                    case MotionEvent.ACTION_MOVE:

                        break;
                    default:
                        break;


                }


                return false;
            }
        });
    }

    private void TimeTask(int delay) {
        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);
            }
        };

        timer.schedule(timerTask,delay,3000);
    }

    public void Destory(){
        if (timer!=null){
            timerTask.cancel();
            timer.cancel();
            timer.purge();
        }
    }

    class Handlers extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                    if (viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(image_id.size()-2,false);
                    }else if (viewPager.getCurrentItem() == image_id.size()-1){
                        viewPager.setCurrentItem(1, false);
                    }else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
            }
        }
    }

    class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return image_id.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(ImageView.get(position));
            return ImageView.get(position);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
