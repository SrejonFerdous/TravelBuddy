package com.example.travelbuddy;

public class carData {
    private String name;
    private String place;
    private String rating;
    private String price;
    private String numberOfSeats;
    private String distancePrice;
    private String imageUrl;
    private String uniqueKey;

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    private boolean reserved;

    public carData(String uniqueKey, String name, String place, String rating, String price, String numberOfSeats, String distancePrice, String imageUrl, boolean reserved) {
        this.uniqueKey=uniqueKey;
        this.name = name;
        this.place = place;
        this.rating = rating;
        this.price = price;
        this.numberOfSeats = numberOfSeats;
        this.distancePrice = distancePrice;
        this.imageUrl = imageUrl;
        this.reserved=reserved;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getRating() {
        return rating;
    }

    public String getPrice() {
        return price;
    }

    public String getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getDistancePrice() {
        return distancePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isReserved() {
        return reserved;
    }

}
