package com.example.testing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.User;
import com.example.testing.session.SessionManager;

public class SplashscreenActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        // Simulate loading process
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    int userId = sessionManager.getUserId();
                    User user = dbHelper.getUserById(userId);
                    Intent intent;
                    if (user.getLevel() == 1) {
                        intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashscreenActivity.this, DashboardActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000); // 3-second delay
    }
}
