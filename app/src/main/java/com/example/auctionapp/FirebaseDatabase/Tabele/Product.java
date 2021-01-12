package com.example.auctionapp.FirebaseDatabase.Tabele;

import com.google.firebase.database.Exclude;

public class Product {
    private String denumire,pret,descriere;
    private String imgUrl;
    //
    private String primaryKey;

    public Product(){}

    public Product(String denumire, String pret, String descriere, String imgUrl, String primaryKey) {
        this.denumire = denumire;
        this.pret = pret;
        this.descriere = descriere;
        this.imgUrl = imgUrl;
        this.primaryKey = primaryKey;
    }

    public Product(String denumire, String pret, String descriere, String imgUrl) {
        if(denumire.trim().equals("")){
            this.denumire="Unnamed";
        }
        if(descriere.trim().equals("")){
            this.descriere="NoDescriptionAvaildable";
        }
        this.denumire = denumire;
        this.pret = pret;
        this.descriere = descriere;
        this.imgUrl = imgUrl;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Exclude
    public String getPrimaryKey() {
        return primaryKey;
    }

    @Exclude
    public void setKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
