package com.example.testing.ui.dashboard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.activities.EditProdukActivity;
import com.example.testing.activities.EditProfileActivity;
import com.example.testing.db.DBHelper;
import com.example.testing.model.Produk;
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

public class AddStockFragment extends Fragment {

    private EditText etProductName, etHargaBarang, etJumlahBarang;
    private Button btnTambahkan;
    private DBHelper dbHelper;
    private ImageView ivEdit, ivDelete, ivProfile;
    private CircleImageView civEdit, civDelete;
    private byte[] photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_stock, container, false);

        etProductName = view.findViewById(R.id.etProductName);
        etHargaBarang = view.findViewById(R.id.etHargaBarang);
        etJumlahBarang = view.findViewById(R.id.etJumlahBarang);
        btnTambahkan = view.findViewById(R.id.btn_tambahkan);
        ivProfile = view.findViewById(R.id.profile_image);
        ivEdit = view.findViewById(R.id.iv_edit);
        ivDelete = view.findViewById(R.id.iv_delete);
        civEdit = view.findViewById(R.id.civ_edit);
        civDelete = view.findViewById(R.id.civ_delete);

        if (photo != null) {
            Glide.with(requireContext()).load(photo).into(ivProfile);
            civDelete.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
            civDelete.setOnClickListener(view1 -> {
                showConfirmationDialog("Yakin hapus foto produk?");
            });
        } else {
            civDelete.setVisibility(View.GONE);
            ivDelete.setVisibility(View.GONE);
        }

        civEdit.setOnClickListener(view1 -> {
            openFileChooser();
        });

        btnTambahkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        return view;
    }

    private void addProduct() {
        String namaBarang = etProductName.getText().toString().trim();
        String hargaBarangStr = etHargaBarang.getText().toString().trim();
        String jumlahBarangStr = etJumlahBarang.getText().toString().trim();

        if (namaBarang.isEmpty() || hargaBarangStr.isEmpty() || jumlahBarangStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double hargaBarang = Double.parseDouble(hargaBarangStr);
        int jumlahBarang = Integer.parseInt(jumlahBarangStr);

        Produk produk = new Produk(1, namaBarang, jumlahBarang, hargaBarang, photo, DateUtils.getCurrentTime(), DateUtils.getCurrentTime());

        dbHelper.addProduk(produk);

        Toast.makeText(requireContext(), "Product added successfully", Toast.LENGTH_SHORT).show();

        etProductName.setText("");
        etHargaBarang.setText("");
        etJumlahBarang.setText("");
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    private void showConfirmationDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(message)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        photo = null;
                        Glide.with(requireContext()).load(R.drawable.icon_photo).into(ivProfile);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // Get file size in bytes
                InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                int fileSizeInBytes = inputStream.available();
                inputStream.close();

                // Convert to megabytes
                double fileSizeInMB = fileSizeInBytes / (1024.0 * 1024.0);

                // Load bitmap and check orientation
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                bitmap = ImageUtils.rotateImageIfRequired(bitmap, imageUri, requireContext());

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
                    showConfirmationDialog("Yakin hapus foto produk?");
                });
            } catch (IOException e) {
                Snackbar snackbar = Snackbar.make(ivProfile.getRootView(), "Gagal mengambil foto.", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(ContextCompat.getColor(ivProfile.getContext(), R.color.snackbarred));
                snackbar.show();
            }
        }
    }

}
