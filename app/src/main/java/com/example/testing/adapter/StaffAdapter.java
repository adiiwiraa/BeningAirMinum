package com.example.testing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testing.R;
import com.example.testing.model.Produk;
import com.example.testing.model.Staff;
import com.example.testing.model.User;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ProdukViewHolder> {

    private Context context;
    private List<User> staffList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User staff);
    }

    public StaffAdapter(Context context, List<User> staffList, OnItemClickListener listener) {
        this.context = context;
        this.staffList = staffList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staff_item, parent, false);
        return new ProdukViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        User staff = staffList.get(position);
        holder.bind(staff);
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class ProdukViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPhone, tvEmail, tvPosition;
        public ImageView ivProfile;

        public ProdukViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPosition = itemView.findViewById(R.id.tv_position);
            ivProfile = itemView.findViewById(R.id.iv_profile_photo);

            itemView.setOnClickListener(v -> listener.onItemClick((User) itemView.getTag()));
        }

        public void bind(User staff) {
            itemView.setTag(staff);
            tvName.setText(staff.getName());
            tvPhone.setText(String.valueOf(staff.getPhone()));
            tvEmail.setText(staff.getEmail());
            tvPosition.setText(staff.getPosition());
            if (staff.getPhoto() != null) {
                Glide.with(ivProfile.getContext()).load(staff.getPhoto()).into(ivProfile);
            } else {
                Glide.with(ivProfile.getContext()).load(R.drawable.baseline_account_circle_24).into(ivProfile);
            }
        }
    }
}
