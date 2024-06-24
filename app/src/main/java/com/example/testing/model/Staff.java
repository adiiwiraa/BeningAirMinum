package com.example.testing.model;

public class Staff {
    private int staffId;
    private String namaStaff;
    private String jabatan;
    private String telepon;
    private String email;
    private String password;

    public Staff(int staffId, String namaStaff, String jabatan, String telepon, String email, String password) {
        this.staffId = staffId;
        this.namaStaff = namaStaff;
        this.jabatan = jabatan;
        this.telepon = telepon;
        this.email = email;
        this.password = password;
    }

    public Staff(String namaStaff, String jabatan, String telepon, String email, String password) {
        this.namaStaff = namaStaff;
        this.jabatan = jabatan;
        this.telepon = telepon;
        this.email = email;
        this.password = password;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getNamaStaff() {
        return namaStaff;
    }

    public void setNamaStaff(String namaStaff) {
        this.namaStaff = namaStaff;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
