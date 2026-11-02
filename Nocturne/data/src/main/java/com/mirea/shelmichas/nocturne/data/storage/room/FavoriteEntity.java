package com.mirea.shelmichas.nocturne.data.storage.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Отдельная таблица избранного: храним только id места.
@Entity(tableName = "favorites")
public class FavoriteEntity {
    @PrimaryKey
    private int placeId;
    private String addedAt;

    public FavoriteEntity(int placeId, String addedAt) {
        this.placeId = placeId;
        this.addedAt = addedAt;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getAddedAt() {
        return addedAt;
    }
}
