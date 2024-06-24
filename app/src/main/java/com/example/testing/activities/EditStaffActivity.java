package com.example.testing.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Produk;
import com.example.testing.model.Staff;
import com.example.testing.model.User;
import com.example.testing.utils.DateUtils;
import com.example.testing.utils.ImageUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditStaffActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText etNamaStaff, etJabatanStaff, etEmailStaff, etTeleponStaff, etPasswordStaff;
    private TextView tvNamaStaff, tvJabatanStaff, tvIdStaff;
    private int staffId;
    private ImageView ivEdit, ivDelete, ivProfile;
    private CircleImageView civEdit, civDelete;
    private byte[] photo;
    private User loadedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);

        dbHelper = new DBHelper(this);

        etNamaStaff = findViewById(R.id.etNamaStaff);
        etJabatanStaff = findViewById(R.id.etJabatanStaff);
        etEmailStaff = findViewById(R.id.etEmailStaff);
        etTeleponStaff = findViewById(R.id.etTeleponStaff);
        etPasswordStaff = findViewById(R.id.etPasswordStaff);
        tvNamaStaff = findViewById(R.id.tvNamaStaff);
        tvIdStaff = findViewById(R.id.tvIdStaff);
        tvJabatanStaff = findViewById(R.id.tvJabatanStaff);
        ivProfile = findViewById(R.id.profile_image);
        ivEdit = findViewById(R.id.iv_edit);
        ivDelete = findViewById(R.id.iv_delete);
        civEdit = findViewById(R.id.civ_edit);
        civDelete = findViewById(R.id.civ_delete);
        Button btnSimpan = findViewById(R.id.btn_simpan);

        staffId = getIntent().getIntExtra("staffId", -1);

        if (staffId != -1) {
            loadedUser = dbHelper.getUserById(staffId);
            if (loadedUser != null) {
                etNamaStaff.setText(loadedUser.getName());
                etJabatanStaff.setText(loadedUser.getPosition());
                etEmailStaff.setText(loadedUser.getEmail());
                etTeleponStaff.setText(loadedUser.getPhone());
                etPasswordStaff.setText(loadedUser.getPassword());
                tvNamaStaff.setText(loadedUser.getName());
                tvIdStaff.setText(String.valueOf(loadedUser.getUserId()));
                tvJabatanStaff.setText(loadedUser.getPosition());

                if (loadedUser.getPhoto() != null) {
                    Glide.with(EditStaffActivity.this).load(loadedUser.getPhoto()).into(ivProfile);
                    civDelete.setVisibility(View.VISIBLE);
                    ivDelete.setVisibility(View.VISIBLE);
                    civDelete.setOnClickListener(view -> {
                        showConfirmationDialog("Yakin hapus foto profil?");
                    });
                } else {
                    civDelete.setVisibility(View.GONE);
                    ivDelete.setVisibility(View.GONE);
                }

                civEdit.setOnClickListener(view -> {
                    openFileChooser();
                });
            }
        }

        btnSimpan.setOnClickListener(v -> saveStaff());
    }

    private void saveStaff() {
        String namaStaff = etNamaStaff.getText().toString().trim();
        String jabatanStaff = etJabatanStaff.getText().toString().trim();
        String emailStaff = etEmailStaff.getText().toString().trim();
        String teleponStaff = etTeleponStaff.getText().toString().trim();
        String passwordStaff = etPasswordStaff.getText().toString().trim();

        if (namaStaff.isEmpty() || jabatanStaff.isEmpty() || emailStaff.isEmpty() || teleponStaff.isEmpty() || passwordStaff.isEmpty()) {
            Toast.makeText(EditStaffActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            loadedUser.setName(namaStaff);
            loadedUser.setPosition(jabatanStaff);
            loadedUser.setEmail(emailStaff);
            loadedUser.setPhone(teleponStaff);
            loadedUser.setPassword(passwordStaff);
            loadedUser.setPhoto(photo);
            loadedUser.setUpdatedAt(DateUtils.getCurrentTime());
            dbHelper.updateUser(loadedUser);
            Toast.makeText(this, "Staff berhasil diperbarui!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // Get file size in bytes
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                int fileSizeInBytes = inputStream.available();
                inputStream.close();

                // Convert to megabytes
                double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);

                // Load bitmap and check orientation
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                bitmap = ImageUtils.rotateImageIfRequired(bitmap, imageUri, EditStaffActivity.this);

                // Check if file size is more than 2MB
                if (fileSizeInMB > 2.0) {
                    // Compress the image
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int quality = 90; // Initial quality
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

                    // Reduce quality until the image size is under 2MB
                    while (outputStream.toByteArray().length > 2 * 1024 * 1024) {
                        outputStream.reset();
                        quality -= 10;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    }

                    // Get the compressed image
                    photo = outputStream.toByteArray();
                    Glide.with(this).load(photo).into(ivProfile);
                } else {
                    // Use the original image
                    photo = ImageUtils.convertBitmapToByteArray(bitmap);
                    Glide.with(this).load(imageUri).into(ivProfile);
                }
                civDelete.setVisibility(View.VISIBLE);
                ivDelete.setVisibility(View.VISIBLE);
                civDelete.setOnClickListener(view -> {
                    showConfirmationDialog("Yakin hapus foto profil?");
                });
            } catch (IOException e) {
                Snackbar snackbar = Snackbar.make(ivProfile.getRootView(), "Gagal mengambil foto.", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(ivProfile.getContext(), R.color.snackbarred));
                snackbar.show();
            }
        }
    }

    private void showConfirmationDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        photo = null;
                        Glide.with(EditStaffActivity.this).load(R.drawable.baseline_account_circle_24).into(ivProfile);
                        civDelete.setVisibility(View.GONE);
                        ivDelete.setVisibility(View.GONE);
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