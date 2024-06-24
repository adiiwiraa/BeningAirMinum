package com.example.testing.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.activities.MainActivity;
import com.example.testing.db.DBHelper;
import com.example.testing.fragments.ConfirmationFragment;
import com.example.testing.fragments.QrFragment;
import com.example.testing.model.Order;
import com.example.testing.model.Produk;
import com.example.testing.model.Stepper;
import com.example.testing.model.User;
import com.example.testing.session.SessionManager;
import com.example.testing.utils.DateUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PemesananActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private LinearLayout productContainer;
    private Button orderButton;
    private HashMap<Integer, Integer> productQuantities = new HashMap<>();
    private TextView totalBayarTextView;
    private RadioButton rbQris;
    private RadioButton rbCod;
    private SessionManager sessionManager;
    private double total = 0;
    private String metodePembayaran;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);
        productContainer = findViewById(R.id.productContainer);
        orderButton = findViewById(R.id.orderButton);
        totalBayarTextView = findViewById(R.id.totalBayar);
        ivBack = findViewById(R.id.iv_back);

        loadProducts();
        setupPaymentRb();
        orderButton.setOnClickListener(view -> {
            if (total > 0) {
                placeOrder();
            }
        });
        ivBack.setOnClickListener(view -> {
            finish();
        });

    }

    /**
     * Mengatur RadioGroup Pemilihan Pembayaran
     */
    private void setupPaymentRb() {

        View vQris = findViewById(R.id.rb_qris);
        View vCod = findViewById(R.id.rb_cod);

        ImageView ivQris = vQris.findViewById(R.id.icon_transfer);
        TextView tvQris = vQris.findViewById(R.id.tv_transfer);

        ImageView ivCod = vCod.findViewById(R.id.icon_transfer);
        TextView tvCod = vCod.findViewById(R.id.tv_transfer);

        rbQris = vQris.findViewById(R.id.rb_transfer);
        rbCod = vCod.findViewById(R.id.rb_transfer);

        ivQris.setImageResource(R.drawable.baseline_qr_code_24);
        ivCod.setImageResource(R.drawable.cod);
        tvQris.setText("QRIS");
        tvCod.setText("COD");

        List<RadioButton> radioButtons = Arrays.asList(rbQris, rbCod);
        rbCod.setChecked(true);

        for (RadioButton rb : radioButtons) {
            rb.setOnClickListener(view -> {
                for (RadioButton otherRb : radioButtons) {
                    if (otherRb != rb) {
                        otherRb.setChecked(false);
                    }
                }
                rb.setChecked(true);
            });
        }
    }

    private void loadProducts() {
        List<Produk> products = dbHelper.getAllProduk();
        for (Produk product : products) {
            View productView = getLayoutInflater().inflate(R.layout.item_produk_option, null);
            TextView productName = productView.findViewById(R.id.productName);
            TextView productPrice = productView.findViewById(R.id.productPrice);
            TextView productStock = productView.findViewById(R.id.tv_stok);
            ImageView ivProduct = productView.findViewById(R.id.iv_product_photo);
            Stepper stepper = productView.findViewById(R.id.stepper);
            Glide.with(PemesananActivity.this).load(product.getFotoProduk()).into(ivProduct);
            productName.setText(product.getNamaProduk());
            productPrice.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(product.getHargaProduk()));
            productStock.setText("Stok: " + product.getStokProduk());
            stepper.setOnValueChangeListener(value -> {
                productQuantities.put(product.getProdukId(), value);
                updateTotal();
                checkOrderButton();
            });

            productContainer.addView(productView);
        }
    }

    private void updateTotal() {
        total = 0;
        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            Produk product = dbHelper.getProdukById(entry.getKey());
            total += product.getHargaProduk() * entry.getValue();
        }
        if (total > 0) {
            orderButton.setBackgroundColor(getResources().getColor(R.color.my_primary));
        } else {
            orderButton.setBackgroundColor(getResources().getColor(R.color.my_fourth));
        }
        totalBayarTextView.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(total));
    }

    private void checkOrderButton() {
        boolean enableButton = productQuantities.values().stream().anyMatch(quantity -> quantity > 0);
        orderButton.setEnabled(enableButton);
    }

    private void placeOrder() {

        ConfirmationFragment mOptionDialogFragment = new ConfirmationFragment();
        mOptionDialogFragment.pesanan = productQuantities.entrySet();
        if (rbQris.isChecked()) {
            mOptionDialogFragment.metodePembayaran = "QRIS";
        } else {
            mOptionDialogFragment.metodePembayaran = "COD";
        }
        mOptionDialogFragment.setOnOptionDialogListener(new ConfirmationFragment.OnOptionDialogListener() {
            @Override
            public void saveOrder() {

                if (rbQris.isChecked()) {
                    metodePembayaran = "QRIS";
                } else {
                    metodePembayaran = "COD";
                }

                User user = dbHelper.getUserById(sessionManager.getUserId());
                for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                    if (entry.getValue() > 0) {
                        Produk product = dbHelper.getProdukById(entry.getKey());
                        if (entry.getValue() > product.getStokProduk()) {
                            Toast.makeText(PemesananActivity.this, "Pesanan " + product.getNamaProduk() + " Gagal Ditambahkan.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            String staffName = generateStaff();
                            if (Objects.equals(staffName, "")) {
                                Toast.makeText(PemesananActivity.this, "Tidak ada driver yang tersedia.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Order order = new Order(
                                        "1",
                                        user.getUserId(),
                                        user.getName(),
                                        user.getEmail(),
                                        user.getPhone(),
                                        user.getAddress(),
                                        product.getNamaProduk(),
                                        entry.getValue(),
                                        metodePembayaran,
                                        product.getHargaProduk() * entry.getValue(),
                                        "In Process",
                                        staffName,
                                        UUID.randomUUID().toString(),
                                        DateUtils.getCurrentTime(),
                                        DateUtils.getCurrentTime()
                                );
                                dbHelper.addOrder(order);
                                product.setStokProduk(product.getStokProduk() - entry.getValue());
                                dbHelper.updateProduk(product);
                                Toast.makeText(PemesananActivity.this, "Pesanan Berhasil Ditambahkan.", Toast.LENGTH_SHORT).show();
                                if (Objects.equals(metodePembayaran, "QRIS")) {
                                    QrFragment qrFragment = new QrFragment();
                                    qrFragment.show(getSupportFragmentManager(), QrFragment.class.getSimpleName());
                                } else {
                                    finish();
                                }
                            }
                        }
                    }
                }
            }
        });
        mOptionDialogFragment.show(getSupportFragmentManager(), ConfirmationFragment.class.getSimpleName());

        if (!orderButton.isEnabled()) return;

    }

    private String generateStaff() {

        List<User> staffListRaw = dbHelper.getAllUsers();
        List<User> staffList = new ArrayList<>();
        for (User staff : staffListRaw) {
            if (Objects.equals(staff.getPosition(), "Driver")) {
                staffList.add(staff);
            }
        }

        if (staffList.isEmpty()) {
            return "";
        }
        Collections.shuffle(staffList);
        return staffList.get(0).getName();

    }

}
