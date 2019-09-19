package com.app.pesona;

public class ExampleItem {
    private String mImageUrl;
    private String mCreator;
    private String mAlamat;
    private String mDeksripsi;
    private String mLatitude;
    private String mLongitude;
    private String mNotelp;

    public ExampleItem(String imageUrl, String creator, String alamat, String deksripsi, String latitude, String longitude, String notelp) {
        mImageUrl = imageUrl;
        mCreator = creator;
        mAlamat = alamat;
        mDeksripsi = deksripsi;
        mLatitude = latitude;
        mLongitude = longitude;
        mNotelp = notelp;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getCreator() {
        return mCreator;
    }

    public String getAlamat() { return mAlamat; }

    public String getdeksripsi() { return mDeksripsi; }

    public String getlatitude() { return mLatitude; }

    public String getlongitude() { return mLongitude; }

    public String getmNotelp() { return mNotelp;}
}