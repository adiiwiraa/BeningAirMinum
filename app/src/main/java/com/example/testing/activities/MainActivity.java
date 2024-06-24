package com.example.testing.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testing.R;
import com.example.testing.session.SessionManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button BtnPesanGalon, BtnProfileMove, BtnHistoryMove;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        BtnPesanGalon = findViewById(R.id.btn_pesan_galon);
        BtnPesanGalon.setOnClickListener(this);
        BtnProfileMove = findViewById(R.id.btn_profile_move);
        BtnProfileMove.setOnClickListener(this);
        BtnHistoryMove = findViewById(R.id.btn_history_move);
        BtnHistoryMove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_pesan_galon) {
            Intent movePesanGalon = new Intent(MainActivity.this, PemesananActivity.class);
            startActivity(movePesanGalon);
        } else if (v.getId() == R.id.btn_profile_move) {
            Intent moveProfile = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(moveProfile);
        } else if (v.getId() == R.id.btn_history_move) {
            Intent moveHistory = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(moveHistory);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
