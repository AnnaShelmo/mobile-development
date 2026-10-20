package com.mirea.shelmichas.nocturne.data.storage.room;

import com.mirea.shelmichas.nocturne.data.storage.PlaceStorage;
import com.mirea.shelmichas.nocturne.data.storage.models.Place;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//Реализация хранилища через Room + seed-данные
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
        seed.add(new PlaceEntity(1, "Собор Парижской Богоматери",
                "Туманный готический собор, взметнувший каменные кружева над Сеной",
                "Париж", "Франция", "готический собор",
                "https://i.pinimg.com/1200x/b0/72/f2/b072f2726f481b21d9bfaa8542a62be5.jpg",
                1163, date));
        seed.add(new PlaceEntity(2, "Замок Бран",
                "Одинокий замок Дракулы на скале, окутанный карпатским туманом",
                "Брашов", "Румыния", "готический замок",
                "https://wikiway.com/upload/hl-photo/454/65f/zamok-drakuly_108.jpg",
                1377, date));
        seed.add(new PlaceEntity(3, "Кёльнский собор",
                "Чёрные шпили, уходящие в свинцовое небо над Рейном",
                "Кёльн", "Германия", "готический собор",
                "https://i.pinimg.com/736x/3e/19/8b/3e198bb6dbfc6fdc7e66e7d1e4fdd5f8.jpg",
                1248, date));
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