package com.sagib.food2you.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    Food food;
    boolean hasAddon;
    int productPrice;
    boolean fixed;
    String notes;
    int qty;

    public Product(Food food, boolean hasAddon, int productPrice, boolean fixed, String notes, int qty) {
        this.food = food;
        this.hasAddon = hasAddon;
        this.productPrice = productPrice;
        this.fixed = fixed;
        this.notes = notes;
        this.qty = qty;
        if (hasAddon)
            this.productPrice = (food.getPrice() + 3) * qty;
        if (!hasAddon)
            this.productPrice = food.getPrice() * qty;


    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public boolean isHasAddon() {
        return hasAddon;
    }

    public void setHasAddon(boolean hasAddon) {
        this.hasAddon = hasAddon;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "food=" + food +
                ", hasAddon=" + hasAddon +
                ", productPrice=" + productPrice +
                ", fixed=" + fixed +
                ", notes='" + notes + '\'' +
                ", qty=" + qty +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.food, flags);
        dest.writeByte(this.hasAddon ? (byte) 1 : (byte) 0);
        dest.writeInt(this.productPrice);
        dest.writeByte(this.fixed ? (byte) 1 : (byte) 0);
        dest.writeString(this.notes);
        dest.writeInt(this.qty);
    }

    protected Product(Parcel in) {
        this.food = in.readParcelable(Food.class.getClassLoader());
        this.hasAddon = in.readByte() != 0;
        this.productPrice = in.readInt();
        this.fixed = in.readByte() != 0;
        this.notes = in.readString();
        this.qty = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
