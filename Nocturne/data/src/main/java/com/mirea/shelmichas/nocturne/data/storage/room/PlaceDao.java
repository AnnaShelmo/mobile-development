package com.mirea.shelmichas.nocturne.data.storage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
// Запросы к таблице
@Dao
public interface PlaceDao {

    @Query("SELECT * FROM places")
    List<PlaceEntity> getAll();

    @Query("SELECT * FROM places WHERE id = :id")
    PlaceEntity getById(int id);

    @Query("SELECT * FROM places WHERE name LIKE '%' || :query || '%'")
    List<PlaceEntity> search(String query);

    @Query("SELECT COUNT(*) FROM places")
    int count();

    @Insert
    void insert(PlaceEntity place);

    @Insert
    void insertAll(List<PlaceEntity> places);
}