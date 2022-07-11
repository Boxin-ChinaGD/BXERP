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
import com.bx.erp.model.Commodity;
import com.bx.erp.utils.GeneralUtil;

import java.util.List;

public class DialogBillRecyclerViewAdapter extends RecyclerView.Adapter<DialogBillRecyclerViewAdapter.MyViewHolder> {
    List<Commodity> list;
    Context context;

    public DialogBillRecyclerViewAdapter(List<Commodity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.bill_rv_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.orderNumber.setTextColor(Color.DKGRAY);
        holder.commodity_name.setTextColor(Color.DKGRAY);
        holder.priceRetail.setTextColor(Color.DKGRAY);
        holder.total_number.setTextColor(Color.DKGRAY);
        holder.subtotal.setTextColor(Color.DKGRAY);
        holder.orderNumber.setText(String.valueOf(position + 1));
        holder.commodity_name.setText(list.get(position).getName());
        holder.priceRetail.setText(GeneralUtil.formatToShow(list.get(position).getPriceRetail()));
        holder.total_number.setText(String.valueOf(list.get(position).getCommodityQuantity()));
        holder.subtotal.setText(GeneralUtil.formatToShow(list.get(position).getSubtotal()));

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.color.tableSpacing);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber, commodity_name, priceRetail, total_number, subtotal;

        public MyViewHolder(View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.orderNumber);
            commodity_name = itemView.findViewById(R.id.commodity_name);
            priceRetail = itemView.findViewById(R.id.priceRetail);
            total_number = itemView.findViewById(R.id.total_number);
            subtotal = itemView.findViewById(R.id.subtotal);
        }
    }
}
