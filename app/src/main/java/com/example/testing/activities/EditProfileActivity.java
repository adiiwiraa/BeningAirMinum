package com.example.testing.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Order;
import com.example.testing.model.User;
import com.example.testing.session.SessionManager;
import com.example.testing.utils.DateUtils;
import com.example.testing.utils.ImageUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button BtnSaveProfile;
    private SessionManager sessionManager;
    private DBHelper dbHelper;
    private int userId;
    private User user;
    private EditText etNama, etAlamat, etNoTelp, etEmail;
    private ImageView ivBack, ivEdit, ivDelete, ivProfile;
    private CircleImageView civEdit, civDelete;
    private byte[] photo;
    private TextView tvLastUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        etNama = findViewById(R.id.edit_nama);
        etAlamat = findViewById(R.id.edit_alamat);
        etNoTelp = findViewById(R.id.edit_notelp);
        etEmail = findViewById(R.id.edit_email);
        ivBack = findViewById(R.id.iv_back);
        ivProfile = findViewById(R.id.profile_image);
        ivEdit = findViewById(R.id.iv_edit);
        ivDelete = findViewById(R.id.iv_delete);
        civEdit = findViewById(R.id.civ_edit);
        civDelete = findViewById(R.id.civ_delete);
        tvLastUpdated = findViewById(R.id.tv_waktu_edit);

        BtnSaveProfile = findViewById(R.id.save_edit_profile);
        BtnSaveProfile.setOnClickListener(this);
        userId = sessionManager.getUserId();
        user = dbHelper.getUserById(userId);

        ivBack.setOnClickListener(view -> finish());

        etNama.setText(user.getName());
        etAlamat.setText(user.getAddress());
        etNoTelp.setText(user.getPhone());
        etEmail.setText(user.getEmail());
        tvLastUpdated.setText(user.getUpdatedAt());

        if (user.getPhoto() != null) {
            Glide.with(EditProfileActivity.this).load(user.getPhoto()).into(ivProfile);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.save_edit_profile) {
            if (etNama.getText().toString().isEmpty() || etAlamat.getText().toString().isEmpty() || etNoTelp.getText().toString().isEmpty() || etEmail.getText().toString().isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            } else {
                user.setName(etNama.getText().toString());
                user.setAddress(etAlamat.getText().toString());
                user.setPhone(etNoTelp.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.setPhoto(photo);
                user.setUpdatedAt(DateUtils.getCurrentTime());
                showConfirmationDialog();
            }
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Simpan perubahan?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.updateUser(user);
                        finish();
                        Toast.makeText(EditProfileActivity.this, "Profil berhasil diperbaharui.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
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
                bitmap = ImageUtils.rotateImageIfRequired(bitmap, imageUri, EditProfileActivity.this);

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
                        Glide.with(EditProfileActivity.this).load(R.drawable.baseline_account_circle_24).into(ivProfile);
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