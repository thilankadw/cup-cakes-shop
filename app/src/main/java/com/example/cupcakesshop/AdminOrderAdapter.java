package com.example.cupcakesshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {

    private List<Order> orderList;
    private LayoutInflater inflater;
    private static String[] statusOptions = {"Pending", "Processing", "Delivered", "Completed"};

    public AdminOrderAdapter(Context context, List<Order> orderList) {
        this.orderList = orderList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_admin_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderId.setText(order.getOrderId());
        holder.orderName.setText(order.getName());
        holder.orderCategory.setText(order.getCategory());
        holder.orderQuantity.setText(String.valueOf(order.getQuantity()));
        holder.orderTotal.setText(String.valueOf(order.getPrice()));
        int statusPosition = getStatusPosition(order.getStatus());
        holder.orderStatusSpinner.setSelection(statusPosition);
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView orderName;
        TextView orderCategory;
        TextView orderQuantity;
        TextView orderTotal;
        Spinner orderStatusSpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderName = itemView.findViewById(R.id.orderName);
            orderCategory = itemView.findViewById(R.id.orderCategory);
            orderQuantity = itemView.findViewById(R.id.orderQuantity);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            orderStatusSpinner = itemView.findViewById(R.id.orderStatusSpinner);
            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item, statusOptions);
            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            orderStatusSpinner.setAdapter(statusAdapter);
        }
    }

    private int getStatusPosition(String status) {
        for (int i = 0; i < statusOptions.length; i++) {
            if (statusOptions[i].equals(status)) {
                return i;
            }
        }
        return 0; // Default to the first position if status not found
    }
}
