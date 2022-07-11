package com.bx.erp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.R;
import com.bx.erp.model.Commodity;
import com.bx.erp.model.CommodityShopInfo;
import com.bx.erp.utils.GeneralUtil;

import java.util.List;

public class DialogCommodityRecyclerViewAdapter1 extends RecyclerView.Adapter<DialogCommodityRecyclerViewAdapter1.MyViewHolder> {
    List<Commodity> list;//存放数据
    Context context;
    String code;
    int pos;
    private int defItem = -1;
    private DialogCommodityRecyclerViewAdapter1.OnItemListener onItemListener;

    public int isShowInventory = 0;

    public DialogCommodityRecyclerViewAdapter1(List<Commodity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemListener(DialogCommodityRecyclerViewAdapter1.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        void onClick(DialogCommodityRecyclerViewAdapter1.MyViewHolder holder, int position);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.alertdialog_commodity_rv_item1, parent, false));
        return holder;
    }

    //在这里可以获得每个子项里面的控件的实例，比如这里的TextView,子项本身的实例是itemView，
// 在这里对获取对象进行操作
    //holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
    //position是点击位置
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (isShowInventory == 0) {
            holder.inventory.setVisibility(View.GONE);
        } else {
            holder.inventory.setVisibility(View.VISIBLE);
        }
        holder.num.setTextColor(Color.DKGRAY);
        holder.bar_code.setTextColor(Color.DKGRAY);
        holder.name_attr.setTextColor(Color.DKGRAY);
        holder.unit.setTextColor(Color.DKGRAY);
        holder.retail_price.setTextColor(Color.DKGRAY);
        holder.inventory.setTextColor(Color.DKGRAY);
        //设置textView显示内容为list里的对应项
        holder.num.setText(String.valueOf(list.get(position).getNumber()));
        holder.bar_code.setText(list.get(position).getBarcode());
        holder.name_attr.setText(list.get(position).getName());
        holder.unit.setText(String.valueOf(list.get(position).getPackageUnit()));
        CommodityShopInfo commodityShopInfo = (CommodityShopInfo)list.get(position).getListSlave2().get(0);
        holder.retail_price.setText(GeneralUtil.formatToShow(commodityShopInfo.getPriceRetail()));
        holder.inventory.setText(String.valueOf(commodityShopInfo.getNO()));
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.color.tableSpacing);
        }
        Commodity commodity = list.get(position);
        if (defItem != -1) {
            if (defItem == position) {
                // 点击的位置
                if (commodity.isSelect == true) {
                    // 选中状态
                    holder.itemView.setBackgroundResource(R.color.blue);
                    holder.num.setTextColor(Color.BLACK);
                    holder.bar_code.setTextColor(Color.BLACK);
                    holder.name_attr.setTextColor(Color.BLACK);
                    holder.unit.setTextColor(Color.BLACK);
                    holder.retail_price.setTextColor(Color.BLACK);
                    holder.inventory.setTextColor(Color.BLACK);
                    code = holder.bar_code.getText().toString();
                    pos = position;
                } else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(Color.WHITE);
                    } else {
                        holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    }
                }
            } else {
//              没有点击的位置都变成默认背景
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5"));
                }
                list.get(position).isSelect = false;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener != null) {
                    onItemListener.onClick(holder, position);
                    InputMethodManager imm = (InputMethodManager)
                            context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    //要显示的子项数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView num, bar_code, name_attr, unit, retail_price, inventory;

        public MyViewHolder(View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.num);
            bar_code = itemView.findViewById(R.id.bar_code);
            name_attr = itemView.findViewById(R.id.name_attr);
            unit = itemView.findViewById(R.id.unit);
            retail_price = itemView.findViewById(R.id.retail_price);
            inventory = itemView.findViewById(R.id.inventory);
        }
    }

    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
//    public boolean addItem(int position, String msg) {
//        if (position < list.size() && position >= 0) {
//            list.add(position, msg);
//            notifyItemInserted(position);
//            return true;
//        }
//        return false;
//    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < list.size() && position >= 0) {
            list.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    //清空显示数据
    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }

    public String getCode() {
        return code;
    }

    public int getPos() {
        return pos;
    }

    public void setIsShowInventory(int isShowInventory) {
        this.isShowInventory = isShowInventory;
    }

}
