package com.mirea.shelmichas.nocturne.domain.models;

public class Place {
    private int id;
    private String name;
    private String description;
    private String city;
    private String country;
    private String category;
    private String imageUrl;
    private int year;

    public Place(int id, String name, String description, String city, String country,
                 String category, String imageUrl, int year) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.country = country;
        this.category = category;
        this.imageUrl = imageUrl;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getYear() {
        return year;
    }
}