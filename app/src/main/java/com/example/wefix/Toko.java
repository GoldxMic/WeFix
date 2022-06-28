package com.example.wefix;

public class Toko {
    public String fullname, id;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Toko(){

    }

    public Toko(String fullname, String id){
        this.fullname = fullname;
        this.id = id;
    }

    @Override
    public String toString() {
        return fullname;
    }
}
