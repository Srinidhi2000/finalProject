package com.example.android.project.specificvenue;



public class VenueDetails {
    private String name,phone,latitude,longitude,address,category,rating,description,status,url;
    public VenueDetails(String name, String phone, String latitude,
                        String longitude, String address, String category, String rating, String description, String status, String url) {
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.category = category;
        this.rating = rating;
        this.description = description;
        this.status = status;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }
}
