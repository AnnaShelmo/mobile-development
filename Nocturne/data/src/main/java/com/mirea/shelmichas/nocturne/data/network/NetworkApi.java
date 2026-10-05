package com.mirea.shelmichas.nocturne.data.network;

import com.mirea.shelmichas.nocturne.data.storage.models.Place;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Имитация сетевого сервиса. Возвращает "замоканные" данные вместо реального
// HTTP-запроса в json-формате.
public class NetworkApi {

    private final List<Place> mockServerData;

    public NetworkApi() {
        String date = LocalDate.now().toString();
        mockServerData = new ArrayList<>();
        mockServerData.add(new Place(101, "Красная площадь", "Главная площадь столицы",
                "Москва", "Россия", "площадь", "", 1500, date));
        mockServerData.add(new Place(102, "Эрмитаж", "Один из крупнейших музеев мира",
                "Санкт-Петербург", "Россия", "музей", "", 1764, date));
        mockServerData.add(new Place(103, "Софийский собор", "Древний православный храм",
                "Киев", "Украина", "собор", "", 1037, date));
        mockServerData.add(new Place(104, "Эйфелева башня", "Металлическая башня в центре Парижа",
                "Париж", "Франция", "достопримечательность", "", 1889, date));
    }

    public List<Place> searchPlaces(String query) {
        List<Place> result = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return result;
        }
        String lowerQuery = query.trim().toLowerCase(Locale.getDefault());
        for (Place place : mockServerData) {
            // Ищем по названию, городу или стране
            if (contains(place.getName(), lowerQuery)
                    || contains(place.getCity(), lowerQuery)
                    || contains(place.getCountry(), lowerQuery)) {
                result.add(place);
            }
        }
        return result;
    }

    private boolean contains(String value, String lowerQuery) {
        return value != null && value.toLowerCase(Locale.getDefault()).contains(lowerQuery);
    }
}