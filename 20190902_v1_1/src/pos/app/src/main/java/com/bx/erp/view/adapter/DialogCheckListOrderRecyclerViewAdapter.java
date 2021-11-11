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

public class DialogCheckListOrderRecyclerViewAdapter extends RecyclerView.Adapter<DialogCheckListOrderRecyclerViewAdapter.MyViewHolder> {
    List<RetailTrade> list;
    Context context;
    int pos;
    private int defItem = -1;
    private OnItemListener onItemListener;

    public  DialogCheckListOrderRecyclerViewAdapter(List<RetailTrade> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public void setDefSelect(int position) {
        this.defItem = position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.alertdialog_check_list_order_rv_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.retailTradeSN.setTextColor(Color.DKGRAY);
        holder.settlement_time.setTextColor(Color.DKGRAY);
        holder.total_money.setTextColor(Color.DKGRAY);
        holder.retailTradeSN.setText(String.valueOf(list.get(position).getSn()));
        holder.settlement_time.setText(Constants.getSimpleDateFormat().format(list.get(position).getSaleDatetime()));
        holder.total_money.setText(GeneralUtil.formatToShow(list.get(position).getAmount()));
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
                    holder.itemView.setBackgroundResource(R.color.tableSelected);
                    holder.retailTradeSN.setTextColor(Color.BLACK);
                    holder.settlement_time.setTextColor(Color.BLACK);
                    holder.total_money.setTextColor(Color.BLACK);
                    pos = position;
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
                    holder.itemView.setBackgroundColor(Color.parseColor("#F8F8FF"));
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
        void onClick(MyViewHolder holder, int position);
    }

    public int getPos() {
        return pos;
    }
}
