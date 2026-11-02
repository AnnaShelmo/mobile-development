package com.mirea.shelmichas.nocturne.data.storage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

// Запросы к таблице избранного.
@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    List<FavoriteEntity> getAll();

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE placeId = :placeId)")
    boolean isFavorite(int placeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE placeId = :placeId")
    void delete(int placeId);
}
