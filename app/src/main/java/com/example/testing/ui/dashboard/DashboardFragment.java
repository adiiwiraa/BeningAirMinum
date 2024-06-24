package com.example.testing.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testing.R;
import com.example.testing.activities.DetailProdukActivity;
import com.example.testing.adapter.StockAdapter;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Produk;
import com.example.testing.session.SessionManager;

import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private StockAdapter stockAdapter;
    private List<Produk> produkList;
    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(requireContext());
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerViewOrders = view.findViewById(R.id.recyclerViewStok);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadOrders();

        return view;
    }

    private void loadOrders() {
        produkList = dbHelper.getAllProduk();
        stockAdapter = new StockAdapter(requireContext(), produkList, new StockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Produk produk) {
                Intent intent = new Intent(requireContext(), DetailProdukActivity.class);
                intent.putExtra("produkId", produk.getProdukId());
                startActivityForResult(intent, 1); // 1 adalah request code untuk detail produk
            }
        });
        recyclerViewOrders.setAdapter(stockAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            loadOrders(); // Muat ulang data setelah kembali dari DetailProdukActivity
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

}