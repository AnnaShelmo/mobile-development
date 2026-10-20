package com.mirea.shelmichas.nocturne.data.storage.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
//Таблица мест в БД
@Entity(tableName = "places")
public class PlaceEntity {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String city;
    private String country;
    private String category;
    private String imageUrl;
    private int year;
    private String localDate;

    public PlaceEntity(int id, String name, String description, String city, String country,
                       String category, String imageUrl, int year, String localDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.country = country;
        this.category = category;
        this.imageUrl = imageUrl;
        this.year = year;
        this.localDate = localDate;
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

    public String getLocalDate() {
        return localDate;
    }
}