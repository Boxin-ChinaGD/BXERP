//package com.bx.erp.view.adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bx.erp.R;
//import com.bx.erp.view.activity.SetupActivity;
//
//public class SetupLeftListviewAdapter extends BaseAdapter {
//    private Context context;
//    private int[] set_up_left_image;
//    private String[] set_up_left_str;
//    public static int mPosition;
//
//    public SetupLeftListviewAdapter(Context context, int[] set_up_left_image, String[] set_up_left_str) {
//        this.context = context;
//        this.set_up_left_image = set_up_left_image;
//        this.set_up_left_str = set_up_left_str;
//    }
//
//    @Override
//    public int getCount() {
//        return set_up_left_image.length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return set_up_left_image[position];
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        convertView = LayoutInflater.from(context).inflate(R.layout.set_up_left_item_layout, null);
//        TextView item_name = convertView.findViewById(R.id.item_name);
//        ImageView item_image = convertView.findViewById(R.id.item_image);
//        mPosition = position;
//        item_name.setText(set_up_left_str[position]);
//        item_image.setImageResource(set_up_left_image[position]);
//        if (position == SetupActivity.mPosition) {
//            convertView.setBackgroundColor(Color.parseColor("#FFDAB9"));
//        } else {
//            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
//        return convertView;
//    }
//}
