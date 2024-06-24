package com.example.testing.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.R;
import com.example.testing.model.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.OrderHistoryViewHolder> {

    private Context context;
    private List<Order> orderList;
    private UserHistoryAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Order order);
        void onCancelClick(Order order);
        void onCompleteClick(Order order);
    }

    public UserHistoryAdapter(Context context, List<Order> orderList, UserHistoryAdapter.OnItemClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserHistoryAdapter.OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new UserHistoryAdapter.OrderHistoryViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHistoryAdapter.OrderHistoryViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewJumlah, textViewStatus, textViewTotal;
        public Button buttonCancelOrder, buttonCompleteOrder;

        public OrderHistoryViewHolder(@NonNull View itemView, final UserHistoryAdapter.OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewJumlah = itemView.findViewById(R.id.textViewJumlah);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            buttonCancelOrder = itemView.findViewById(R.id.buttonCancelOrder);
            buttonCompleteOrder = itemView.findViewById(R.id.buttonCompleteOrder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick((Order) itemView.getTag());
                }
            });

            buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCancelClick((Order) itemView.getTag());
                }
            });

            buttonCompleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCompleteClick((Order) itemView.getTag());
                }
            });
        }

        public void bind(Order order) {
            itemView.setTag(order);
            textViewName.setText(order.getName());
            textViewStatus.setText(order.getCreatedAt());
            textViewJumlah.setText(order.getStatus());
            textViewTotal.setText(order.getOrderName());

            if (Objects.equals(order.getStatus(), "Cancelled")) {
                textViewJumlah.setTextColor(Color.parseColor("#D00303"));
            } else if (Objects.equals(order.getStatus(), "In Process")) {
                textViewJumlah.setTextColor(Color.parseColor("#D2AF02"));
            } else if (Objects.equals(order.getStatus(), "Completed")) {
                textViewJumlah.setTextColor(Color.parseColor("#0FBE03"));
            }

            if ("Completed".equals(order.getStatus()) || "Cancelled".equals(order.getStatus())) {
                buttonCancelOrder.setVisibility(View.GONE);
                buttonCompleteOrder.setVisibility(View.GONE);
            } else {
                buttonCancelOrder.setVisibility(View.VISIBLE);
                buttonCompleteOrder.setVisibility(View.VISIBLE);
            }
        }
    }

}
