package com.example.testing.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Produk;
import com.example.testing.model.Staff;
import com.example.testing.model.User;

public class DetailStaffActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private TextView tvNama, tvId, tvPosisi, tvEmail, tvNoHp, tvDibuat, tvDiupdate;
    private Button btnEdit, btnHapus;
    private ImageView ivFoto;
    private int staffId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_staff);

        dbHelper = new DBHelper(this);

        ivFoto = findViewById(R.id.iv_foto);
        tvNama = findViewById(R.id.tv_nama);
        tvId = findViewById(R.id.tv_id);
        tvPosisi = findViewById(R.id.tv_posisi);
        tvEmail = findViewById(R.id.tv_email);
        tvNoHp = findViewById(R.id.tv_no_hp);
        tvDibuat = findViewById(R.id.tv_tanggal_buat);
        tvDiupdate = findViewById(R.id.tv_tanggal_update);

        btnEdit = findViewById(R.id.btn_edit);
        btnHapus = findViewById(R.id.btn_hapus);

        staffId = getIntent().getIntExtra("staffId", -1);

        if (staffId != -1) {
            User staff = dbHelper.getUserById(staffId);
            if (staff != null) {
                displayStaffDetails(staff);
            }
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editStaff(staffId);
            }
        });

        btnHapus.setOnClickListener(v -> confirmDeleteStaff());
    }

    private void displayStaffDetails(User staff) {
        if (staff.getPhoto() != null) {
            Glide.with(DetailStaffActivity.this).load(staff.getPhoto()).into(ivFoto);
        } else {
            Glide.with(DetailStaffActivity.this).load(R.drawable.baseline_account_circle_24).into(ivFoto);
        }
        tvNama.setText(staff.getName());
        tvId.setText(String.valueOf(staff.getUserId()));
        tvPosisi.setText(staff.getPosition());
        tvEmail.setText(staff.getEmail());
        tvNoHp.setText(staff.getPhone());
        tvDiupdate.setText(staff.getUpdatedAt());
        tvDibuat.setText(staff.getCreatedAt());
    }

    private void editStaff(Integer produkId) {
        Intent intent = new Intent(this, EditStaffActivity.class);
        intent.putExtra("staffId", produkId);
        startActivityForResult(intent, 2); // 2 adalah request code untuk edit produk
    }

    private void confirmDeleteStaff() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Produk")
                .setMessage("Apakah Anda yakin ingin menghapus staff ini?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    dbHelper.deleteStaffById(staffId);
                    Toast.makeText(DetailStaffActivity.this, "Staff berhasil dihapus!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            User staff = dbHelper.getUserById(staffId);
            if (staff != null) {
                displayStaffDetails(staff);
                setResult(RESULT_OK); // Set result OK so that the DashboardFragment knows to refresh
            }
        }
    }
}