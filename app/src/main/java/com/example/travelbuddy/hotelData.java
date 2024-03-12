package com.example.travelbuddy;

public class hotelData {
    private String name,rating,hotelImage,price,numberOfBeds,numberOfPeople,place="0",checkInOut="0",key,uniqueCode;
    private boolean wifi;
    private boolean pets;
    private boolean ac;
    private boolean parking;
    private boolean pool;
    private boolean spa;
    private boolean reserved;
    private boolean favourite;


    public hotelData(String name, String rating, String hotelImage, String price, String numberOfBeds, String numberOfPeople, String place, String checkInOut, String key, String uniqueCode, boolean favourite, boolean reserved, boolean wifi, boolean pets, boolean ac, boolean parking, boolean pool, boolean spa) {
        this.name = name;
        this.rating = rating;
        this.hotelImage = hotelImage;
        this.price = price;
        this.numberOfBeds = numberOfBeds;
        this.numberOfPeople = numberOfPeople;
        this.place=place;
        this.checkInOut=checkInOut;
        this.key=key;
        this.uniqueCode=uniqueCode;
        this.favourite=favourite;
        this.reserved=reserved;
        this.wifi = wifi;
        this.pets = pets;
        this.ac = ac;
        this.parking = parking;
        this.pool = pool;
        this.spa = spa;
    }


    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }


    public String getUniqueCode() {
        return uniqueCode;
    }

    public String getKey() {
        return key;
    }

    public String getPlace() {
        return place;
    }

    public String getCheckInOut() {
        return checkInOut;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public String getPrice() {
        return price;
    }

    public String getNumberOfBeds() {
        return numberOfBeds;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public boolean isWifi() {
        return wifi;
    }

    public boolean isPets() {
        return pets;
    }

    public boolean isAc() {
        return ac;
    }

    public boolean isParking() {
        return parking;
    }

    public boolean isPool() {
        return pool;
    }

    public boolean isSpa() {
        return spa;
    }

    public boolean isReserved() {
        return reserved;
    }

    public boolean isFavourite() {
        return favourite;
    }

}
