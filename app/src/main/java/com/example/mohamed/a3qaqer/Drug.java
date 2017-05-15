package com.example.mohamed.a3qaqer;

/**
 * Created by mohamed on 14/05/2017.
 */

public class Drug {
    private String imageUrl;
    private String tarkez;
    private String type;
    private String drugName;
    private Pharmacy pharmacy;

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTarkez() {
        return tarkez;
    }

    public void setTarkez(String tarkez) {
        this.tarkez = tarkez;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }
}
