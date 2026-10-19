package com.mirea.shelmichas.nocturne.data.network;

import com.mirea.shelmichas.nocturne.data.storage.models.Place;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Имитация сетевого сервиса. Возвращает "замоканные" данные вместо реального
// HTTP-запроса в json-формате. Ответы двух эндпоинтов:
// getPlaces() — каталог, searchPlaces() — результаты поиска.
public class NetworkApi {

    private final List<Place> mockServerData;

    public NetworkApi() {
        String date = LocalDate.now().toString();
        mockServerData = new ArrayList<>();
        mockServerData.add(new Place(1, "Красная площадь", "Главная площадь страны у стен Кремля",
                "Москва", "Россия", "площадь", "place_red_square", 1561, date));
        mockServerData.add(new Place(2, "Эрмитаж", "Один из крупнейших музеев мира",
                "Санкт-Петербург", "Россия", "музей", "place_hermitage", 1762, date));
        mockServerData.add(new Place(3, "Эйфелева башня", "Металлическая башня в центре Парижа",
                "Париж", "Франция", "достопримечательность", "place_eiffel_tower", 1889, date));
        mockServerData.add(new Place(4, "Пражский град", "Крупнейший древний замок мира",
                "Прага", "Чехия", "замок", "place_prague_castle", 880, date));
        mockServerData.add(new Place(5, "Собор Парижской Богоматери", "Готический собор с витражами",
                "Париж", "Франция", "собор", "place_notre_dame", 1163, date));
        mockServerData.add(new Place(6, "Колизей", "Древнеримский амфитеатр для зрелищ",
                "Рим", "Италия", "амфитеатр", "place_colosseum", 80, date));
        mockServerData.add(new Place(7, "Венская опера", "Знаменитый оперный театр",
                "Вена", "Австрия", "театр", "place_vienna_opera", 1869, date));
        mockServerData.add(new Place(8, "Тадж-Махал", "Мавзолей-мечеть из белого мрамора",
                "Агра", "Индия", "мавзолей", "place_taj_mahal", 1653, date));
        mockServerData.add(new Place(101, "Софийский собор", "Древний православный храм с мозаиками",
                "Киев", "Украина", "собор", "place_saint_sophia", 1037, date));
        mockServerData.add(new Place(102, "Вестминстерский дворец", "Резиденция парламента у Темзы",
                "Лондон", "Великобритания", "дворец", "place_westminster", 1870, date));
        mockServerData.add(new Place(103, "Статуя Свободы", "Монумент в бухте Нью-Йорка",
                "Нью-Йорк", "США", "монумент", "place_statue_liberty", 1886, date));
        mockServerData.add(new Place(104, "Сиднейский оперный театр", "Театр с парусообразными крышами",
                "Сидней", "Австралия", "театр", "place_sydney_opera", 1973, date));
    }

    // Ответ сервиса на запрос каталога
    public List<Place> getPlaces() {
        return mockServerData;
    }

    public Place getPlace(int id) {
        for (Place place : mockServerData) {
            if (place.getId() == id) {
                return place;
            }
        }
        return null;
    }

    // Ответ сервиса на запрос поиска
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
