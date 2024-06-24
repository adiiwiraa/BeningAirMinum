package com.example.testing.ui.dashboard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testing.R;
import com.example.testing.activities.DetailStaffActivity;
import com.example.testing.activities.HistoryActivity;
import com.example.testing.activities.PengirimanActivity;
import com.example.testing.adapter.OrderAdapter;
import com.example.testing.adapter.StaffAdapter;
import com.example.testing.adapter.UserHistoryAdapter;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Order;
import com.example.testing.model.User;

import java.util.ArrayList;
import java.util.List;

public class PemesananFragment extends Fragment {

    RecyclerView rvOrder;
    private UserHistoryAdapter uhAdapter;
    private List<Order> orderListRaw;
    private List<Order> orderList;
    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pemesanan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvOrder = view.findViewById(R.id.rv_order);
        rvOrder.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void loadOrders() {
        orderListRaw = dbHelper.getAllOrders();
        orderList = orderListRaw;
        uhAdapter = new UserHistoryAdapter(requireContext(), orderList,  new UserHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Order order) {
                Intent intent = new Intent(requireContext(), PengirimanActivity.class);
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
        rvOrder.setAdapter(uhAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            loadOrders();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    private void showConfirmationDialog(final Order order, String message, final String action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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