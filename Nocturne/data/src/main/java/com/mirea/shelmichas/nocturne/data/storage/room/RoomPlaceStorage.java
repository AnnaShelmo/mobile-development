package com.mirea.shelmichas.nocturne.data.storage.room;

import com.mirea.shelmichas.nocturne.data.storage.PlaceStorage;
import com.mirea.shelmichas.nocturne.data.storage.models.Place;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomPlaceStorage implements PlaceStorage {

    private final PlaceDao placeDao;

    public RoomPlaceStorage(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    @Override
    public List<Place> getPlaces() {
        if (placeDao.count() == 0) {
            placeDao.insertAll(seedPlaces());
        }
        return mapToModelList(placeDao.getAll());
    }

    @Override
    public Place getPlace(int id) {
        return mapToModel(placeDao.getById(id));
    }

    @Override
    public List<Place> searchPlaces(String query) {
        return mapToModelList(placeDao.search(query));
    }

    @Override
    public boolean savePlace(Place place) {
        placeDao.insert(mapToEntity(place));
        return true;
    }

    private List<PlaceEntity> seedPlaces() {
        String date = LocalDate.now().toString();
        List<PlaceEntity> seed = new ArrayList<>();
        seed.add(new PlaceEntity(1, "Пражский град", "Крупнейший древний замок мира",
                "Прага", "Чехия", "замок", "", 1344, date));
        seed.add(new PlaceEntity(2, "Собор Парижской Богоматери", "Готический собор с витражами",
                "Париж", "Франция", "собор", "", 1163, date));
        seed.add(new PlaceEntity(3, "Старый театр", "Знаменитый оперный театр",
                "Вена", "Австрия", "театр", "", 1869, date));
        return seed;
    }

    private PlaceEntity mapToEntity(Place place) {
        return new PlaceEntity(place.getId(), place.getName(), place.getDescription(),
                place.getCity(), place.getCountry(), place.getCategory(), place.getImageUrl(),
                place.getYear(), place.getLocalDate());
    }

    private Place mapToModel(PlaceEntity entity) {
        return new Place(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getCity(), entity.getCountry(), entity.getCategory(), entity.getImageUrl(),
                entity.getYear(), entity.getLocalDate());
    }

    private List<Place> mapToModelList(List<PlaceEntity> entities) {
        List<Place> result = new ArrayList<>();
        for (PlaceEntity entity : entities) {
            result.add(mapToModel(entity));
        }
        return result;
    }
}