package com.example.wefix;

public class OrderData {
    private String fullname_user, fullname_toko, address_toko, phone_toko, keterangan, kategori, address, phone, start_date,end_date, status, userid, tokoid,  productid;
    private Integer harga;

    public String getKeterangan() {
        return keterangan;
    }


    public String getPhone() {
        return phone;
    }

    public String getFullname_user() {
        return fullname_user;
    }

    public void setFullname_user(String fullname_user) {
        this.fullname_user = fullname_user;
    }

    public String getFullname_toko() {
        return fullname_toko;
    }

    public void setFullname_toko(String fullname_toko) {
        this.fullname_toko = fullname_toko;
    }

    public String getAddress_toko() {
        return address_toko;
    }

    public void setAddress_toko(String address_toko) {
        this.address_toko = address_toko;
    }

    public String getPhone_toko() {
        return phone_toko;
    }

    public void setPhone_toko(String phone_toko) {
        this.phone_toko = phone_toko;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }


    public String getTokoid() {
        return tokoid;
    }

    public void setTokoid(String tokoid) {
        this.tokoid = tokoid;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
