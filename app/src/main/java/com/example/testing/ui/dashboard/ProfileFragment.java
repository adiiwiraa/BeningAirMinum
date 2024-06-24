package com.example.testing.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.activities.ChangePasswordActivity;
import com.example.testing.activities.EditProfileActivity;
import com.example.testing.activities.LoginActivity;
import com.example.testing.activities.ProfileActivity;
import com.example.testing.db.DBHelper;
import com.example.testing.model.User;
import com.example.testing.session.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Button BtnEditProfile, BtnSignOut, btnChangePassword;
    private TextView tvName, tvAddress;
    private SessionManager sessionManager;
    private DBHelper dbHelper;
    private CircleImageView civProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BtnEditProfile = view.findViewById(R.id.edit_profile);
        btnChangePassword = view.findViewById(R.id.change_password);
        BtnEditProfile.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        tvName = view.findViewById(R.id.tv_name);
        tvAddress = view.findViewById(R.id.tv_address);
        BtnSignOut = view.findViewById(R.id.sign_out);
        BtnSignOut.setOnClickListener(this);
        civProfile = view.findViewById(R.id.profile_image);
        sessionManager = new SessionManager(getContext());
        dbHelper = new DBHelper(getContext());
        setupInterface();
    }

    private void logoutUser() {
        sessionManager.logoutUser();
        Intent moveSignOut = new Intent(getContext(), LoginActivity.class);
        moveSignOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(moveSignOut);
    }

    private void setupInterface(){
        User user = dbHelper.getUserById(sessionManager.getUserId());
        String name = user.getName();
        tvName.setText(name);
        tvAddress.setText(user.getAddress());
        if (user.getPhoto() != null) {
            Glide.with(getContext()).load(user.getPhoto()).into(civProfile);
        } else {
            Glide.with(getContext()).load(R.drawable.baseline_account_circle_24).into(civProfile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupInterface();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_profile) {
            Intent moveEditProfil = new Intent(getContext(), EditProfileActivity.class);
            startActivity(moveEditProfil);
        }
        else if (v.getId() == R.id.change_password) {
            Intent moveChangePassword = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(moveChangePassword);
        }
        else if (v.getId() == R.id.sign_out) {
            logoutUser();
        }
    }
}