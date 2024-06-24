package com.example.testing.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testing.R;
import com.example.testing.activities.DetailStaffActivity;
import com.example.testing.adapter.StaffAdapter;
import com.example.testing.db.DBHelper;
import com.example.testing.model.User;
import com.example.testing.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class StaffFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private StaffAdapter staffAdapter;
    private List<User> staffListRaw;
    private List<User> staffList;
    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(requireContext());
        sessionManager = new SessionManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff, container, false);
        recyclerViewOrders = view.findViewById(R.id.recyclerViewStaff);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadOrders();

        return view;
    }

    private void loadOrders() {
        staffListRaw = dbHelper.getAllUsers();
        staffList = new ArrayList<>();
        for (User staff : staffListRaw) {
            if (staff.getLevel() == 2) {
                staffList.add(staff);
            }
        }
        staffAdapter = new StaffAdapter(requireContext(), staffList, staff -> {
            Intent intent = new Intent(requireContext(), DetailStaffActivity.class);
            intent.putExtra("staffId", staff.getUserId());
            startActivity(intent);
        });
        recyclerViewOrders.setAdapter(staffAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            loadOrders();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }
}