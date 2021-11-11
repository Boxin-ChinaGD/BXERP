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

public class CustomerCommodityRecyclerViewAdapter extends RecyclerView.Adapter<CustomerCommodityRecyclerViewAdapter.MyViewHolder> {
    List<Commodity> list;
    Context context;

    public CustomerCommodityRecyclerViewAdapter(List<Commodity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.customer_commodity_rv_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.number.setTextColor(Color.BLACK);
        holder.name.setTextColor(Color.BLACK);
        holder.quantity.setTextColor(Color.BLACK);
        holder.retailPrice.setTextColor(Color.BLACK);
        holder.subtotal.setTextColor(Color.BLACK);

        holder.number.setText(String.valueOf(list.get(position).getNumber()));
        holder.name.setText(list.get(position).getName());
        holder.quantity.setText(String.valueOf(list.get(position).getCommodityQuantity()));
        holder.retailPrice.setText(GeneralUtil.formatToShow(list.get(position).getPriceRetail()));
        holder.discount.setText(GeneralUtil.formatToShow(list.get(position).getDiscount()));
        holder.subtotal.setText(GeneralUtil.formatToShow(list.get(position).getSubtotal()));

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.color.tableSpacing);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, name, quantity, retailPrice, discount, subtotal;

        public MyViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            retailPrice = itemView.findViewById(R.id.retailprice);
            discount = itemView.findViewById(R.id.discount);
            subtotal = itemView.findViewById(R.id.subtotal);
        }
    }
}
