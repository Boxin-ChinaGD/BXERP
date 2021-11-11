package com.bx.erp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bx.erp.R;
import com.bx.erp.model.Commodity;
import com.bx.erp.utils.GeneralUtil;

import java.util.List;

public class CommodityRecyclerViewAdapter extends RecyclerView.Adapter<CommodityRecyclerViewAdapter.MyViewHolder> {
    List<Commodity> list;
    Context context;
    private int defItem = -1;
    private OnItemListener onItemListener;
    private OnItemLongListener onItemLongListener;

    public CommodityRecyclerViewAdapter(List<Commodity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public void setOnItemLongListener(OnItemLongListener onItemLongListener) {
        this.onItemLongListener = onItemLongListener;
    }

    public interface OnItemListener {
        void onClick(MyViewHolder holder, int position);
    }

    public interface OnItemLongListener {
        void onLongClick(MyViewHolder holder, int position);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.commodity_rv_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.num.setTextColor(Color.DKGRAY);
        holder.bar_code.setTextColor(Color.DKGRAY);
        holder.name_attr.setTextColor(Color.DKGRAY);
        holder.unit.setTextColor(Color.DKGRAY);
        holder.goods_quantity.setTextColor(Color.DKGRAY);
        holder.unit_price.setTextColor(Color.DKGRAY);
        holder.discount.setTextColor(Color.DKGRAY);
        holder.after_discount.setTextColor(Color.DKGRAY);
        holder.total_money.setTextColor(Color.DKGRAY);
        holder.remark.setTextColor(Color.DKGRAY);
        //此处的内容待修改
        holder.num.setText(String.valueOf(list.get(position).getNumber()));
        holder.bar_code.setText(list.get(position).getBarcode());
        holder.name_attr.setText(list.get(position).getName());
        holder.unit.setText(list.get(position).getPackageUnit());
        holder.goods_quantity.setText(String.valueOf(list.get(position).getCommodityQuantity()));
        holder.unit_price.setText(GeneralUtil.formatToShow(list.get(position).getPriceRetail()));
        holder.discount.setText(GeneralUtil.formatToShow(list.get(position).getDiscount()));
        holder.after_discount.setText(GeneralUtil.formatToShow(list.get(position).getAfter_discount()));
        holder.total_money.setText(GeneralUtil.formatToShow(list.get(position).getSubtotal()));
        holder.remark.setText(list.get(position).getTag());
        Commodity commodity = list.get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.color.tableSpacing);
        }
        if (defItem != -1) {
            if (defItem == position) {
                // 点击的位置
                if (commodity.isSelect == true) {
                    // 选中状态
                    holder.itemView.setBackgroundResource(R.color.tableSelected);
                    holder.num.setTextColor(Color.BLACK);
                    holder.bar_code.setTextColor(Color.BLACK);
                    holder.name_attr.setTextColor(Color.BLACK);
                    holder.unit.setTextColor(Color.BLACK);
                    holder.goods_quantity.setTextColor(Color.BLACK);
                    holder.unit_price.setTextColor(Color.BLACK);
                    holder.discount.setTextColor(Color.BLACK);
                    holder.after_discount.setTextColor(Color.BLACK);
                    holder.total_money.setTextColor(Color.BLACK);
                    holder.remark.setTextColor(Color.BLACK);
                } else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(Color.WHITE);
                    } else {
                        holder.itemView.setBackgroundColor(Color.parseColor("#F8F8FF"));
                    }
                }
            } else {
                // 没有点击的位置都变成默认背景
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                } else {
                    holder.itemView.setBackgroundResource(R.color.tableSpacing);
                }
                list.get(position).isSelect = false;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemListener != null) {
                    onItemListener.onClick(holder, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongListener != null) {
                    onItemLongListener.onLongClick(holder, position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < list.size() && position >= 0) {
            list.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView num, bar_code, name_attr, unit, goods_quantity, unit_price, discount, after_discount, total_money, remark, isflag;

        public MyViewHolder(View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.commodity_id);
            bar_code = itemView.findViewById(R.id.bar_code);
            name_attr = itemView.findViewById(R.id.name_attr);
            unit = itemView.findViewById(R.id.unit);
            goods_quantity = itemView.findViewById(R.id.number);
            unit_price = itemView.findViewById(R.id.unit_price);
            discount = itemView.findViewById(R.id.discount);
            after_discount = itemView.findViewById(R.id.after_discount);
            total_money = itemView.findViewById(R.id.total_money);
            remark = itemView.findViewById(R.id.remark);
            isflag = itemView.findViewById(R.id.isflag);
        }
    }

}
