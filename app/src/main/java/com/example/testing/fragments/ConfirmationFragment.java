package com.example.testing.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Produk;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ConfirmationFragment extends DialogFragment {

    private DBHelper dbHelper;
    Button btnBatal;
    Button btnPesan;
    private LinearLayout productContainer;
    TextView tvTotal;
    TextView tvMetode;
    public String metodePembayaran;
    public Set<Map.Entry<Integer, Integer>> pesanan;
    double total = 0;
    private OnOptionDialogListener onOptionDialogListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(getActivity());

        btnBatal = view.findViewById(R.id.btn_batal);
        btnPesan = view.findViewById(R.id.btn_pesan);
        tvTotal = view.findViewById(R.id.tv_total);
        tvMetode = view.findViewById(R.id.tv_metode);
        productContainer = view.findViewById(R.id.productContainer);

        total = 0;
        productContainer.removeAllViews();

        for (Map.Entry<Integer, Integer> entry : pesanan) {
            Produk product = dbHelper.getProdukById(entry.getKey());
            View productView = getLayoutInflater().inflate(R.layout.item_confirmation, null);
            TextView productName = productView.findViewById(R.id.productName);
            TextView productPrice = productView.findViewById(R.id.productPrice);
            TextView tvJumlah = productView.findViewById(R.id.tv_jumlah);

            productName.setText(product.getNamaProduk());
            productPrice.setText("x" + entry.getValue());
            tvJumlah.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(product.getHargaProduk() * entry.getValue()));
            total += product.getHargaProduk() * entry.getValue();

            productContainer.addView(productView);
        }

        tvMetode.setText(metodePembayaran);
        tvTotal.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(total));

        btnBatal.setOnClickListener(view1 -> {
            getDialog().cancel();
        });

        btnPesan.setOnClickListener(view1 -> {
            onOptionDialogListener.saveOrder();
            getDialog().cancel();
        });

    }

    public void setOnOptionDialogListener(OnOptionDialogListener onOptionDialogListener) {
        this.onOptionDialogListener = onOptionDialogListener;
    }

    public interface OnOptionDialogListener {
        void saveOrder();
    }

}