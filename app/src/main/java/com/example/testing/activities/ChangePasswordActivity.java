package com.example.testing.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testing.R;
import com.example.testing.db.DBHelper;
import com.example.testing.model.User;
import com.example.testing.session.SessionManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private Button btnChange;
    private SessionManager sessionManager;
    private DBHelper dbHelper;
    private User user;
    private EditText etPassword, etConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        etPassword = findViewById(R.id.password);
        etConfirm = findViewById(R.id.password_confirm);

        sessionManager = new SessionManager(this);
        dbHelper = new DBHelper(this);

        int userId = sessionManager.getUserId();
        user = dbHelper.getUserById(userId);

        btnChange = findViewById(R.id.btn_change);
        btnChange.setOnClickListener(view -> {
            if (!etPassword.getText().toString().equals(etConfirm.getText().toString())) {
                Toast.makeText(this, "Password tidak sesuai.", Toast.LENGTH_SHORT).show();
            } else {
                user.setPassword(etPassword.getText().toString());
                dbHelper.updateUser(user);
                Toast.makeText(this, "Password berhasil diubah.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}