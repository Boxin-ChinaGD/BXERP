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

import com.bx.erp.R;
import com.bx.erp.helper.Constants;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.utils.GeneralUtil;

import java.util.List;

/**
 * Created by WPNA on 2020/3/5.
 */

public class DialogCheckListOrderRecyclerViewAdapter1 extends RecyclerView.Adapter<DialogCheckListOrderRecyclerViewAdapter1.MyViewHolder>{
    List<RetailTrade> list;
    Context context;
    int pos;
    private int defItem = -1;
    private DialogCheckListOrderRecyclerViewAdapter1.OnItemListener onItemListener;

    public  DialogCheckListOrderRecyclerViewAdapter1(List<RetailTrade> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemListener(DialogCheckListOrderRecyclerViewAdapter1.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public void setDefSelect(int position) {
        this.defItem = position;
    }

    @NonNull
    @Override
    public DialogCheckListOrderRecyclerViewAdapter1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DialogCheckListOrderRecyclerViewAdapter1.MyViewHolder holder = new DialogCheckListOrderRecyclerViewAdapter1.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.alertdialog_check_list_order_rv_item1, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DialogCheckListOrderRecyclerViewAdapter1.MyViewHolder holder, final int position) {

        holder.retailTradeSN.setText(String.valueOf(list.get(position).getSn()));
        holder.settlement_time.setText(Constants.getSimpleDateFormat().format(list.get(position).getSaleDatetime()));
        holder.total_money.setText(GeneralUtil.formatToShow(list.get(position).getAmount()));
        RetailTrade retailTrade = list.get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(0xFFFFFFFF);
        } else {
            holder.itemView.setBackgroundColor(0xFFF0F5F7);
        }
        if (defItem != -1) {
            if (defItem == position) {
                // 点击的位置
                if (retailTrade.isSelect == true) {
                    // 选中状态
                    holder.itemView.setBackgroundColor(0xFF2196F3);
                    holder.retailTradeSN.setTextColor(Color.WHITE);
                    holder.settlement_time.setTextColor(Color.WHITE);
                    holder.total_money.setTextColor(Color.WHITE);
                    pos = position;
                } else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(0xFFFFFFFF);
                        holder.retailTradeSN.setTextColor(Color.parseColor("#263238"));
                        holder.settlement_time.setTextColor(Color.parseColor("#263238"));
                        holder.total_money.setTextColor(Color.parseColor("#263238"));
                    } else {
                        holder.itemView.setBackgroundColor(0xFFF0F5F7);
                        holder.retailTradeSN.setTextColor(Color.parseColor("#263238"));
                        holder.settlement_time.setTextColor(Color.parseColor("#263238"));
                        holder.total_money.setTextColor(Color.parseColor("#263238"));
                    }
                }
            } else {
                // 没有点击的位置都变成默认背景
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                    holder.retailTradeSN.setTextColor(Color.parseColor("#263238"));
                    holder.settlement_time.setTextColor(Color.parseColor("#263238"));
                    holder.total_money.setTextColor(Color.parseColor("#263238"));
                } else {
                    holder.itemView.setBackgroundColor(0xFFF0F5F7);
                    holder.retailTradeSN.setTextColor(Color.parseColor("#263238"));
                    holder.settlement_time.setTextColor(Color.parseColor("#263238"));
                    holder.total_money.setTextColor(Color.parseColor("#263238"));
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

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView retailTradeSN, settlement_time, total_money;

        public MyViewHolder(View itemView) {
            super(itemView);
            retailTradeSN = itemView.findViewById(R.id.retailtrade_sn);
            settlement_time = itemView.findViewById(R.id.settlement_time);
            total_money = itemView.findViewById(R.id.total_money);
        }
    }

    public interface OnItemListener {
        void onClick(DialogCheckListOrderRecyclerViewAdapter1.MyViewHolder holder, int position);
    }

    public int getPos() {
        return pos;
    }
}