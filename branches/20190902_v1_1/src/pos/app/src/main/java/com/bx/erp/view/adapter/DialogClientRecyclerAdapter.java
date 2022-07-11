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
import com.bx.erp.model.Vip;

import java.util.List;

public class DialogClientRecyclerAdapter extends RecyclerView.Adapter<DialogClientRecyclerAdapter.MyViewHolder> {
    List<Vip> list;//存放数据
    Context context;
    String name;
    private int defItem = -1;
    private DialogClientRecyclerAdapter.OnItemListener onItemListener;

    public DialogClientRecyclerAdapter(List<Vip> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemListener(DialogClientRecyclerAdapter.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener {
        void onClick(DialogClientRecyclerAdapter.MyViewHolder holder, int position);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
    }

    @Override
    public DialogClientRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DialogClientRecyclerAdapter.MyViewHolder holder = new DialogClientRecyclerAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.alertdialog_client_rv_item, parent, false));
        return holder;
    }

    //在这里可以获得每个子项里面的控件的实例，比如这里的TextView,子项本身的实例是itemView，
    // 在这里对获取对象进行操作
    //holder.itemView是子项视图的实例，holder.textView是子项内控件的实例
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.id.setTextColor(Color.DKGRAY);
        holder.number.setTextColor(Color.DKGRAY);
        holder.name.setTextColor(Color.DKGRAY);
        holder.contact_number.setTextColor(Color.DKGRAY);
        holder.id.setText(list.get(position).getNumber() + "");
        holder.name.setText(list.get(position).getName());
        holder.contact_number.setText(list.get(position).getEmail());
        Vip vip = list.get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.color.tableSpacing);
        }
        if (defItem != -1) {
            if (defItem == position) {
                // 点击的位置
                if (vip.isSelect == true) {
                    // 选中状态
                    holder.itemView.setBackgroundResource(R.color.tableSelected);
                    holder.id.setTextColor(Color.BLACK);
                    holder.number.setTextColor(Color.BLACK);
                    holder.name.setTextColor(Color.BLACK);
                    holder.contact_number.setTextColor(Color.BLACK);
                    name=list.get(position).getName();
                } else {
                    if(position%2==0){
                        holder.itemView.setBackgroundColor(Color.WHITE);
                    }else {
                        holder.itemView.setBackgroundColor(Color.parseColor("#F8F8FF"));
                    }

                }
            } else {
                // 没有点击的位置都变成默认背景
                if(position%2==0){
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }else {
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
    }

    //要显示的子项数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //这里定义的是子项的类，不要在这里直接对获取对象进行操作
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, number, name, contact_number;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            number = itemView.findViewById(R.id.num);
            name = itemView.findViewById(R.id.name);
            contact_number = itemView.findViewById(R.id.contact_number);
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

    public String getName() {
        return name;
    }

}