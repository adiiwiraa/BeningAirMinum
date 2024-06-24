package com.example.testing.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Produk;

import java.text.NumberFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailProdukActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private TextView tvNamaProduk, tvTanggalMasuk, tvIdProduk, tvStokProduk, tvHargaProduk, tvTanggalUpdate;
    private Button btnEdit, btnHapus;
    private CircleImageView civProduct;
    private int produkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        dbHelper = new DBHelper(this);

        tvNamaProduk = findViewById(R.id.tvNamaProduk);
        tvTanggalMasuk = findViewById(R.id.tv_tanggal_dibuat);
        tvTanggalUpdate = findViewById(R.id.tv_tanggal_update);
        tvIdProduk = findViewById(R.id.tv_id);
        tvStokProduk = findViewById(R.id.tv_stok);
        tvHargaProduk = findViewById(R.id.tv_harga);
        civProduct = findViewById(R.id.product_image);
        btnEdit = findViewById(R.id.btn_edit);
        btnHapus = findViewById(R.id.btn_hapus);

        produkId = getIntent().getIntExtra("produkId", -1);

        if (produkId != -1) {
            Produk produk = dbHelper.getProdukById(produkId);
            if (produk != null) {
                displayProdukDetails(produk);
            }
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProduk(produkId);
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteProduk();
            }
        });
    }

    private void displayProdukDetails(Produk produk) {
        tvNamaProduk.setText(produk.getNamaProduk());
        tvIdProduk.setText(String.valueOf(produk.getProdukId()));
        tvStokProduk.setText(String.valueOf(produk.getStokProduk()));
        tvHargaProduk.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(produk.getHargaProduk()));
        tvTanggalMasuk.setText(produk.getTanggalMasuk());
        tvTanggalUpdate.setText(produk.getTanggalUpdate());
        if (produk.getFotoProduk() != null) {
            Glide.with(DetailProdukActivity.this).load(produk.getFotoProduk()).into(civProduct);
        } else {
            Glide.with(DetailProdukActivity.this).load(R.drawable.icon_photo).into(civProduct);
        }
    }

    private void editProduk(Integer produkId) {
        Intent intent = new Intent(this, EditProdukActivity.class);
        intent.putExtra("produkId", produkId);
        startActivityForResult(intent, 2); // 2 adalah request code untuk edit produk
    }

    private void confirmDeleteProduk() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Produk")
                .setMessage("Apakah Anda yakin ingin menghapus produk ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteProdukById(produkId);
                        Toast.makeText(DetailProdukActivity.this, "Produk berhasil dihapus!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Produk produk = dbHelper.getProdukById(produkId);
            if (produk != null) {
                displayProdukDetails(produk);
                setResult(RESULT_OK); // Set result OK so that the DashboardFragment knows to refresh
            }
        }
    }
}

