package com.thahaseen.showcart;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    int id;
    String name;
    String price;

    public Product(){}

    public Product(int id, String name, String price){
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(price);
    }

    private Product(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
