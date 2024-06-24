package com.example.testing.model;

public class Produk {
    private int produkId;
    private String namaProduk;
    private int stokProduk;
    private double hargaProduk;
    private byte[] fotoProduk;
    private String tanggalMasuk;
    private String tanggalUpdate;

    public byte[] getFotoProduk() {
        return fotoProduk;
    }

    public void setFotoProduk(byte[] fotoProduk) {
        this.fotoProduk = fotoProduk;
    }

    public Produk(int produkId, String namaProduk, int stokProduk, double hargaProduk, byte[] fotoProduk, String tanggalMasuk, String tanggalUpdate) {
        this.produkId = produkId;
        this.namaProduk = namaProduk;
        this.stokProduk = stokProduk;
        this.hargaProduk = hargaProduk;
        this.fotoProduk = fotoProduk;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalUpdate = tanggalUpdate;
    }

    public String getTanggalUpdate() {
        return tanggalUpdate;
    }

    public void setTanggalUpdate(String tanggalUpdate) {
        this.tanggalUpdate = tanggalUpdate;
    }

    public int getProdukId() {
        return produkId;
    }

    public void setProdukId(int produkId) {
        this.produkId = produkId;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getStokProduk() {
        return stokProduk;
    }

    public void setStokProduk(int stokProduk) {
        this.stokProduk = stokProduk;
    }

    public double getHargaProduk() {
        return hargaProduk;
    }

    public void setHargaProduk(double hargaProduk) {
        this.hargaProduk = hargaProduk;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(String tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }
}
