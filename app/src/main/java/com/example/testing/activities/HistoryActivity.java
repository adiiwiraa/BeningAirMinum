package com.example.testing.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.testing.R;
import com.example.testing.adapter.OrderAdapter;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Order;
import com.example.testing.session.SessionManager;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    private void loadOrders() {
        orderList = dbHelper.getAllOrders(sessionManager.getUserId());
        orderAdapter = new OrderAdapter(this, orderList, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                Intent intent = new Intent(HistoryActivity.this, PengirimanActivity.class);
                intent.putExtra("orderId", order.getOrderId());
                startActivity(intent);
            }

            @Override
            public void onCancelClick(Order order) {
                showConfirmationDialog(order, "Anda yakin ingin membatalkan pesanan?", "Cancelled");
            }

            @Override
            public void onCompleteClick(Order order) {
                showConfirmationDialog(order, "Anda yakin ingin menyelesaikan pesanan?", "Complete");
            }
        });
        recyclerViewOrders.setAdapter(orderAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void showConfirmationDialog(final Order order, String message, final String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (action.equals("Cancelled")) {
                            order.setStatus("Cancelled");
                        } else if (action.equals("Complete")) {
                            order.setStatus("Completed");
                        }
                        dbHelper.updateOrder(order);
                        loadOrders();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

}
