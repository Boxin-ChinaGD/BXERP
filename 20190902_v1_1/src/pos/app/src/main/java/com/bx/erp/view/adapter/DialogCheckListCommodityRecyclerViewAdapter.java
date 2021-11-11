package com.bx.erp.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bx.erp.R;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.utils.GeneralUtil;

import java.util.List;

import static android.view.View.GONE;

public class DialogCheckListCommodityRecyclerViewAdapter extends RecyclerView.Adapter<DialogCheckListCommodityRecyclerViewAdapter.MyViewHolder> {
    List<RetailTradeCommodity> list;
    Context context;
    private int defItem = -1;
    private OnItemListener onItemListener;
    public String num_str;
    int pos;

    private ModifyCountInterface modifyCountInterface;

    public DialogCheckListCommodityRecyclerViewAdapter(List<RetailTradeCommodity> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public interface OnItemListener {
        void onClick(MyViewHolder holder, int position);
    }

    public void setDefItem(int position) {
        this.defItem = position;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    public String getNum_str() {
        return num_str;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.alertdialog_check_list_commodity_rv_iteml, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.serial_number.setTextColor(Color.DKGRAY);
        holder.commodity_name.setTextColor(Color.DKGRAY);
        holder.unit_price.setTextColor(Color.DKGRAY);
        holder.number.setTextColor(Color.DKGRAY);
        holder.total_money.setTextColor(Color.DKGRAY);

        holder.serial_number.setText(String.valueOf(list.get(position).getNum()));
        holder.commodity_name.setText(list.get(position).getName());
        holder.unit_price.setText(GeneralUtil.formatToShow(list.get(position).getPriceReturn()));
        holder.change_number.setText(String.valueOf(list.get(position).getNO()));
        holder.change_number.setFocusable(false);//不可获取焦点
        holder.change_number.setShowSoftInputOnFocus(false);//点击后不调出软键盘
        holder.total_money.setText(GeneralUtil.formatToShow(GeneralUtil.mul(list.get(position).getPriceReturn(), Integer.valueOf(list.get(position).getNO()))));
        holder.number.setText(holder.change_number.getText().toString());
        RetailTradeCommodity commodity = list.get(position);
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.color.tableSpacing);
        }
        if (defItem != -1) {
            if (defItem == position) {
                //点击的位置
                if (commodity.isSelect == true) {
                    //选中状态
                    holder.itemView.setBackgroundResource(R.color.tableSelected);
                    holder.serial_number.setTextColor(Color.BLACK);
                    holder.commodity_name.setTextColor(Color.BLACK);
                    holder.unit_price.setTextColor(Color.BLACK);
                    holder.total_money.setTextColor(Color.BLACK);
                    if (RetailTradeCommodity.isRetailTradeCommodityNumberEditable) {
                        holder.number.setVisibility(GONE);
                        holder.change_number_layout.setVisibility(View.VISIBLE);
                        holder.number.setText(holder.change_number.getText().toString());
                    } else {
                        holder.number.setVisibility(View.VISIBLE);
                        holder.change_number_layout.setVisibility(View.GONE);
                        holder.number.setText(holder.change_number.getText().toString());
                    }
                    pos = position;
                } else {
                    //选中之后点击取消状态
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(Color.WHITE);
                    } else {
                        holder.itemView.setBackgroundColor(Color.parseColor("#F8F8F8"));
                    }
                    holder.number.setVisibility(View.VISIBLE);
                    holder.change_number_layout.setVisibility(GONE);
                    holder.number.setText(holder.change_number.getText().toString());
                }
            } else {
                //未选中，未取消状态 或是 选中后在选中其他的item
                // 没有点击的位置都变成默认背景
                if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                } else {
                    holder.itemView.setBackgroundColor(Color.parseColor("#F8F8FF"));
                }
                holder.number.setVisibility(View.VISIBLE);
                holder.change_number_layout.setVisibility(GONE);
                list.get(position).isSelect = false;
                holder.number.setText(holder.change_number.getText().toString());
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
        holder.add_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                modifyCountInterface.doIncrease(position, holder.change_number, holder.total_money, motionEvent);
                return true;
            }
        });
        holder.reduce_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                modifyCountInterface.doDecrease(position, holder.change_number, holder.total_money, motionEvent);
                return true;
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

//    public void setIfEditNumber() {
//
//        if (RetailTradeCommodity.isRetailTradeCommodityNumberEditable) {
//            MyViewHolder.number.setVisibility(GONE);
//            holder.change_number_layout.setVisibility(View.VISIBLE);
//            holder.number.setText(holder.change_number.getText().toString());
//        } else {
//            holder.number.setVisibility(View.VISIBLE);
//            holder.change_number_layout.setVisibility(View.GONE);
//            holder.number.setText(holder.change_number.getText().toString());
//        }
//    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serial_number, commodity_name, unit_price, number, total_money, reduce_number, add_number;
        LinearLayout change_number_layout;
        EditText change_number;

        public MyViewHolder(View itemView) {
            super(itemView);
            serial_number = itemView.findViewById(R.id.tv_line_num);
            commodity_name = itemView.findViewById(R.id.commodity_name);
            unit_price = itemView.findViewById(R.id.unit_price);
            number = itemView.findViewById(R.id.number);
            total_money = itemView.findViewById(R.id.total_money);
            change_number_layout = itemView.findViewById(R.id.change_number_layout);
            change_number = itemView.findViewById(R.id.change_number);
            reduce_number = itemView.findViewById(R.id.reduce_number);
            add_number = itemView.findViewById(R.id.add_number);
        }
    }

    public interface ModifyCountInterface {
        void doIncrease(int position, View showCountView, View total_money, MotionEvent event);
        void doDecrease(int position, View showCountView, View total_money, MotionEvent event);
    }

    public int getPos() {
        return pos;
    }
}
