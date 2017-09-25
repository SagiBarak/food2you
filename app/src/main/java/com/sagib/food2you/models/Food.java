package com.sagib.food2you.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sagib on 09/09/2017.
 */

public class Food implements Parcelable {
    String name;
    String description;
    int price;
    String imgUrl;
    boolean isAddonAvailable;

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imgUrl='" + imgUrl + '\'' +
                ", isAddonAvailable=" + isAddonAvailable +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isAddonAvailable() {
        return isAddonAvailable;
    }

    public void setAddonAvailable(boolean addonAvailable) {
        isAddonAvailable = addonAvailable;
    }

    public Food(String name, String description, int price, String imgUrl, boolean isAddonAvailable) {

        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.isAddonAvailable = isAddonAvailable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.price);
        dest.writeString(this.imgUrl);
        dest.writeByte(this.isAddonAvailable ? (byte) 1 : (byte) 0);
    }

    protected Food(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.price = in.readInt();
        this.imgUrl = in.readString();
        this.isAddonAvailable = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
