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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ProdukViewHolder> {

    private Context context;
    private List<Produk> produkList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Produk produk);
    }

    public StockAdapter(Context context, List<Produk> produkList, OnItemClickListener listener) {
        this.context = context;
        this.produkList = produkList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stock_item, parent, false);
        return new ProdukViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        Produk produk = produkList.get(position);
        holder.bind(produk);
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public static class ProdukViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName, textViewStatus, textViewTotal, textViewHarga;
        public ImageView ivFotoProduk;

        public ProdukViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            textViewHarga = itemView.findViewById(R.id.textViewHarga);
            ivFotoProduk = itemView.findViewById(R.id.iv_product_photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick((Produk) itemView.getTag());
                }
            });
        }

        public void bind(Produk produk) {
            itemView.setTag(produk);
            textViewName.setText(produk.getNamaProduk());
            textViewStatus.setText(String.valueOf(produk.getProdukId()));
            textViewTotal.setText(String.valueOf(produk.getStokProduk()));
            textViewHarga.setText(NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(produk.getHargaProduk()));
            if (produk.getFotoProduk() != null) {
                Glide.with(ivFotoProduk.getContext()).load(produk.getFotoProduk()).into(ivFotoProduk);
            } else {
                Glide.with(ivFotoProduk.getContext()).load(R.drawable.icon_photo).into(ivFotoProduk);
            }
        }
    }
}
