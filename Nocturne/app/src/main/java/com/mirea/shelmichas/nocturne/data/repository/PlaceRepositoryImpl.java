package com.mirea.shelmichas.nocturne.data.repository;

import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.mirea.shelmichas.nocturne.domain.repository.PlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlaceRepositoryImpl implements PlaceRepository {

    private final List<Place> places;

    public PlaceRepositoryImpl() {
        places = new ArrayList<>();
        places.add(new Place(1, "Пражский град", "Крупнейший древний замок мира, красиво подсвеченный ночью",
                "Прага", "Чехия", "замок", "", 1344));
        places.add(new Place(2, "Собор Парижской Богоматери", "Готический собор с витражами и ночной подсветкой",
                "Париж", "Франция", "собор", "", 1163));
        places.add(new Place(3, "Старый театр", "Знаменитый оперный театр", "Вена", "Австрия",
                "театр", "", 1869));
        places.add(new Place(4, "Лунный мост", "Мост, отражающий луну в водах озера", "Киото", "Япония",
                "мост", "", 1650));
    }

    @Override
    public List<Place> getPlaces() {
        return places;
    }

    @Override
    public Place getPlace(int id) {
        for (Place place : places) {
            if (place.getId() == id) {
                return place;
            }
        }
        return null;
    }

    @Override
    public List<Place> searchPlaces(String query) {
        List<Place> result = new ArrayList<>();
        if (query == null || query.isEmpty()) {
            return result;
        }
        String lowerQuery = query.toLowerCase(Locale.getDefault());
        for (Place place : places) {
            if (place.getName().toLowerCase(Locale.getDefault()).contains(lowerQuery)) {
                result.add(place);
            }
        }
        return result;
    }
}