package com.bx.erp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bx.erp.R;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.RetailTrade;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by BOXIN on 2020/7/8.
 */

public class DialogBillRetailTradeRecyclerViewAdapter extends RecyclerView.Adapter<DialogBillRetailTradeRecyclerViewAdapter.MyViewHolder> {
    List<RetailTrade> list;
    Context context;
    private int defItem = -1;
    private OnItemListener onItemListener;
    private SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_Default7);

    public interface OnItemListener {
        void onClick(DialogBillRetailTradeRecyclerViewAdapter.MyViewHolder holder, int position);
    }

    public DialogBillRetailTradeRecyclerViewAdapter(List<RetailTrade> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setDefItem(int position) {
        this.defItem = position;
    }

    public void setOnItemListener(DialogBillRetailTradeRecyclerViewAdapter.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public DialogBillRetailTradeRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DialogBillRetailTradeRecyclerViewAdapter.MyViewHolder holder = new DialogBillRetailTradeRecyclerViewAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.bill_retailtrade_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.sn.setTextColor(Color.DKGRAY);
        holder.holdBillTime.setTextColor(Color.DKGRAY);
        holder.firstCommodity.setTextColor(Color.DKGRAY);
        holder.vipMobile.setTextColor(Color.DKGRAY);
        holder.sn.setText(list.get(position).getSn());
        holder.holdBillTime.setText(sdf.format(list.get(position).getHoldBillTime()));
        holder.firstCommodity.setText(list.get(position).getFirstCommodityName());
        holder.vipMobile.setText(list.get(position).getVip() == null ? "" : list.get(position).getVip().getMobile());
        RetailTrade retailTrade = list.get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.color.tableSpacing);
        }
        if (defItem != -1) {
            if (defItem == position) {
                // 点击的位置
                if (retailTrade.isSelect == true) {
                    // 选中状态
                    holder.itemView.setBackgroundColor(0xFF2196F3);
                    holder.sn.setTextColor(Color.WHITE);
                    holder.holdBillTime.setTextColor(Color.WHITE);
                    holder.firstCommodity.setTextColor(Color.WHITE);
                    holder.vipMobile.setTextColor(Color.WHITE);
                } else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(0xFFFFFFFF);
                        holder.sn.setTextColor(Color.parseColor("#263238"));
                        holder.holdBillTime.setTextColor(Color.parseColor("#263238"));
                        holder.firstCommodity.setTextColor(Color.parseColor("#263238"));
                        holder.vipMobile.setTextColor(Color.parseColor("#263238"));
                    } else {
                        holder.itemView.setBackgroundColor(0xFFF0F5F7);
                        holder.sn.setTextColor(Color.parseColor("#263238"));
                        holder.holdBillTime.setTextColor(Color.parseColor("#263238"));
                        holder.firstCommodity.setTextColor(Color.parseColor("#263238"));
                        holder.vipMobile.setTextColor(Color.parseColor("#263238"));
                    }
                }
            } else {
                // 没有点击的位置都变成默认背景
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    holder.sn.setTextColor(Color.parseColor("#263238"));
                    holder.holdBillTime.setTextColor(Color.parseColor("#263238"));
                    holder.firstCommodity.setTextColor(Color.parseColor("#263238"));
                    holder.vipMobile.setTextColor(Color.parseColor("#263238"));
                } else {
                    holder.itemView.setBackgroundColor(0xFFF0F5F7);
                    holder.sn.setTextColor(Color.parseColor("#263238"));
                    holder.holdBillTime.setTextColor(Color.parseColor("#263238"));
                    holder.firstCommodity.setTextColor(Color.parseColor("#263238"));
                    holder.vipMobile.setTextColor(Color.parseColor("#263238"));
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sn, holdBillTime, firstCommodity, vipMobile;

        public MyViewHolder(View itemView) {
            super(itemView);
            sn = itemView.findViewById(R.id.sn);
            holdBillTime = itemView.findViewById(R.id.holdBillTime);
            firstCommodity = itemView.findViewById(R.id.firstCommodity);
            vipMobile = itemView.findViewById(R.id.vipMobile);
        }
    }
}
