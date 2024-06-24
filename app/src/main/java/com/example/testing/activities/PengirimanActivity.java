package com.example.testing.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Order;
import com.example.testing.utils.DateUtils;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class PengirimanActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnTerima, btnBatal;
    private ImageView ivBack, ivQr;
    private String orderId;
    private DBHelper dbHelper;
    private Order order;
    private TextView tvNama, tvEmail, tvAlamat, tvJumlah, tvKodeOrder, tvDriver, tvStatus, tvTotal, tvNamaProduk, tvHargaSatuan, tvWaktuPesan, tvTextWaktuSelesai, tvWaktuSelesai, tvMetodePembayaran, tvQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengiriman);
        dbHelper = new DBHelper(this);
        tvNama = findViewById(R.id.tv_nama);
        tvEmail = findViewById(R.id.tv_email);
        tvAlamat = findViewById(R.id.tv_alamat);
        tvJumlah = findViewById(R.id.tv_jumlah);
        tvKodeOrder = findViewById(R.id.tv_kode_pesanan);
        tvDriver = findViewById(R.id.tv_driver);
        tvStatus = findViewById(R.id.tv_status);
        tvTotal = findViewById(R.id.tv_total);
        tvNamaProduk = findViewById(R.id.tv_produk);
        tvHargaSatuan = findViewById(R.id.tv_harga_satuan);
        tvWaktuPesan = findViewById(R.id.tv_waktu_pesan);
        tvTextWaktuSelesai = findViewById(R.id.tv_text_waktu_selesai);
        tvWaktuSelesai = findViewById(R.id.tv_waktu_selesai);
        tvQr = findViewById(R.id.tv_qr);
        tvMetodePembayaran = findViewById(R.id.tv_metode_pembayaran);

        orderId = getIntent().getStringExtra("orderId");
        order = dbHelper.getOrder(orderId);

        tvNama.setText(order.getName());
        tvEmail.setText(order.getEmail());
        tvAlamat.setText(order.getAddress());
        tvJumlah.setText(Integer.toString(order.getQuantity()));
        tvKodeOrder.setText(order.getOrderId());
        tvDriver.setText(order.getDriverName());
        tvStatus.setText(order.getStatus());
        tvNamaProduk.setText(order.getOrderName());
        tvWaktuPesan.setText(order.getCreatedAt());
        tvMetodePembayaran.setText(order.getPaymentType());
        tvHargaSatuan.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(order.getTotal() / order.getQuantity()));
        tvTotal.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(order.getTotal()));

        btnTerima = (Button) findViewById(R.id.btn_terima);
        btnBatal = (Button) findViewById(R.id.btn_cancel);
        ivBack = findViewById(R.id.iv_back);
        ivQr = findViewById(R.id.iv_qr);
        btnTerima.setOnClickListener(this);
        btnBatal.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        if (Objects.equals(order.getStatus(), "Cancelled")) {
            tvStatus.setTextColor(Color.parseColor("#D00303"));
            tvTextWaktuSelesai.setText("Dibatalkan Pada: ");
            tvWaktuSelesai.setText(order.getFinishedAt());
        } else if (Objects.equals(order.getStatus(), "In Process")) {
            tvStatus.setTextColor(Color.parseColor("#D2AF02"));
            tvTextWaktuSelesai.setText("Selesai Pada: ");
            tvWaktuSelesai.setText("-");

            if (Objects.equals(order.getPaymentType(), "QRIS")) {
                ivQr.setVisibility(View.VISIBLE);
                tvQr.setVisibility(View.VISIBLE);
            } else {
                ivQr.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
            }

        } else if (Objects.equals(order.getStatus(), "Completed")) {
            tvStatus.setTextColor(Color.parseColor("#0FBE03"));
            tvTextWaktuSelesai.setText("Selesai Pada: ");
            tvWaktuSelesai.setText(order.getFinishedAt());
        }

        if ("Completed".equals(order.getStatus()) || "Cancelled".equals(order.getStatus())) {
            btnTerima.setVisibility(View.GONE);
            btnBatal.setVisibility(View.GONE);
        } else {
            btnTerima.setVisibility(View.VISIBLE);
            btnBatal.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_terima) {
            showConfirmationDialog(order, "Anda yakin ingin menyelesaikan pesanan?", "Complete");
        } else if (v.getId() == R.id.btn_cancel) {
            showConfirmationDialog(order, "Anda yakin ingin membatalkan pesanan?", "Cancelled");
        } else if (v.getId() == R.id.iv_back) {
            finish();
        }
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
                        order.setFinishedAt(DateUtils.getCurrentTime());
                        dbHelper.updateOrder(order);
                        finish();
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