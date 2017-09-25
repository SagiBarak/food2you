package com.sagib.food2you.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sagib on 09/09/2017.
 */

public class Order implements Parcelable {
    String fullName;
    String phoneNumber;
    String address;
    String houseNumber;
    String aptNumber;
    String floorNumber;
    String city;
    String futureHour;
    String futureDate;
    String orderTime;
    String notes;
    ArrayList<Product> products;
    int totalPrice;
    String orderUID;
    String orderNumber;

    public String getFutureDate() {
        return futureDate;
    }

    public void setFutureDate(String futureDate) {
        this.futureDate = futureDate;
    }

    public String getOrderUID() {
        return orderUID;
    }

    public void setOrderUID(String orderUID) {
        this.orderUID = orderUID;
    }

    @Override
    public String toString() {
        return "Order{" +
                "fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", aptNumber='" + aptNumber + '\'' +
                ", floorNumber='" + floorNumber + '\'' +
                ", city='" + city + '\'' +
                ", futureHour='" + futureHour + '\'' +
                ", futureDate='" + futureDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", notes='" + notes + '\'' +
                ", products=" + products +
                ", totalPrice=" + totalPrice +
                ", orderUID='" + orderUID + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                '}';
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Order(String fullName, String phoneNumber, String address, String houseNumber, String aptNumber, String floorNumber, String city, String futureHour, String futureDate, String orderTime, String notes, ArrayList<Product> products, int totalPrice, String orderUID, String orderNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.houseNumber = houseNumber;
        this.aptNumber = aptNumber;
        this.floorNumber = floorNumber;
        this.city = city;
        this.futureHour = futureHour;
        this.orderTime = orderTime;
        this.notes = notes;
        this.products = products;
        this.totalPrice = totalPrice;
        this.futureDate = futureDate;
        this.orderUID = orderUID;
        this.orderNumber = orderNumber;


    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAptNumber() {
        return aptNumber;
    }

    public void setAptNumber(String aptNumber) {
        this.aptNumber = aptNumber;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFutureHour() {
        return futureHour;
    }

    public void setFutureHour(String futureHour) {
        this.futureHour = futureHour;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.address);
        dest.writeString(this.houseNumber);
        dest.writeString(this.aptNumber);
        dest.writeString(this.floorNumber);
        dest.writeString(this.city);
        dest.writeString(this.futureHour);
        dest.writeString(this.futureDate);
        dest.writeString(this.orderTime);
        dest.writeString(this.notes);
        dest.writeTypedList(this.products);
        dest.writeInt(this.totalPrice);
        dest.writeString(this.orderUID);
        dest.writeString(this.orderNumber);
    }

    protected Order(Parcel in) {
        this.fullName = in.readString();
        this.phoneNumber = in.readString();
        this.address = in.readString();
        this.houseNumber = in.readString();
        this.aptNumber = in.readString();
        this.floorNumber = in.readString();
        this.city = in.readString();
        this.futureHour = in.readString();
        this.futureDate = in.readString();
        this.orderTime = in.readString();
        this.notes = in.readString();
        this.products = in.createTypedArrayList(Product.CREATOR);
        this.totalPrice = in.readInt();
        this.orderUID = in.readString();
        this.orderNumber = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
