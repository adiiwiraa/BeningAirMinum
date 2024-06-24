package com.example.testing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.User;
import com.example.testing.session.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Button BtnEditProfile, BtnSignOut, btnChangePassword;
    private TextView tvName, tvAddress;
    private SessionManager sessionManager;
    private DBHelper dbHelper;
    private CircleImageView civProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BtnEditProfile = (Button)findViewById(R.id.edit_profile);
        btnChangePassword = (Button)findViewById(R.id.change_password);
        BtnEditProfile.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        tvName = findViewById(R.id.tv_name);
        tvAddress = findViewById(R.id.tv_address);
        BtnSignOut = (Button)findViewById(R.id.sign_out);
        BtnSignOut.setOnClickListener(this);
        civProfile = findViewById(R.id.profile_image);
        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);
        setupInterface();
    }

    private void logoutUser() {
        sessionManager.logoutUser();
        Intent moveSignOut = new Intent(ProfileActivity.this, LoginActivity.class);
        moveSignOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(moveSignOut);
    }

    private void setupInterface(){
        User user = dbHelper.getUserById(sessionManager.getUserId());
        String name = user.getName();
        tvName.setText(name);
        tvAddress.setText(user.getAddress());
        if (user.getPhoto() != null) {
            Glide.with(ProfileActivity.this).load(user.getPhoto()).into(civProfile);
        } else {
            Glide.with(ProfileActivity.this).load(R.drawable.baseline_account_circle_24).into(civProfile);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_profile) {
            Intent moveEditProfil = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(moveEditProfil);
        }
        else if (v.getId() == R.id.change_password) {
            Intent moveChangePassword = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(moveChangePassword);
        }
        else if (v.getId() == R.id.sign_out) {
            logoutUser();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupInterface();
    }
}