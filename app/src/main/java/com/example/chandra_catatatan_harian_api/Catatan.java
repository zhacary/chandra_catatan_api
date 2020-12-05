package com.example.chandra_catatatan_harian_api;

public class Catatan {
    String id,tanggal,catat;
    public Catatan(String id, String tanggal,String catat){
        this.id = id;
        this.tanggal = tanggal;
        this.catat = catat;
    }

    public String getId(){
        return id;
    }
    public String getTanggal(){
        return tanggal;
    }
    public String getCatat(){
        return catat;
    }
}
