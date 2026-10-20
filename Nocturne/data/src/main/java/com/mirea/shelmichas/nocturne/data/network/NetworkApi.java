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
        mockServerData.add(new Place(1, "Собор Парижской Богоматери",
                "Туманный готический собор, взметнувший каменные кружева над Сеной",
                "Париж", "Франция", "готический собор",
                "https://i.pinimg.com/1200x/b0/72/f2/b072f2726f481b21d9bfaa8542a62be5.jpg",
                1163, date));
        mockServerData.add(new Place(2, "Нотр-Дам де Реймс",
                "Коронационный собор с химерами, хранящий тень французских королей",
                "Реймс", "Франция", "готический собор",
                "https://i.pinimg.com/1200x/51/7c/56/517c56d1b6e4dd08719eddd0dc0ba248.jpg",
                1275, date));
        mockServerData.add(new Place(3, "Кёльнский собор",
                "Чёрные шпили, уходящие в свинцовое небо над Рейном",
                "Кёльн", "Германия", "готический собор",
                "https://i.pinimg.com/736x/3e/19/8b/3e198bb6dbfc6fdc7e66e7d1e4fdd5f8.jpg",
                1248, date));
        mockServerData.add(new Place(4, "Миланский собор",
                "Мраморный лес из сотни шпилей, мерцающий в ночной тишине",
                "Милан", "Италия", "готический собор",
                "https://i.pinimg.com/736x/9d/3c/1a/9d3c1a0dce4142e8a7eb304ba95b4ffe.jpg",
                1386, date));
        mockServerData.add(new Place(5, "Замок Бран",
                "Одинокий замок Дракулы на скале, окутанный карпатским туманом",
                "Брашов", "Румыния", "готический замок",
                "https://wikiway.com/upload/hl-photo/454/65f/zamok-drakuly_108.jpg",
                1377, date));
        mockServerData.add(new Place(6, "Вестминстерское аббатство",
                "Древние готические своды, где покоятся тени английских монархов",
                "Лондон", "Великобритания", "готическое аббатство",
                "https://i.pinimg.com/1200x/a4/2b/ef/a42bef585b048adfa4829e9d1574a686.jpg",
                960, date));
        mockServerData.add(new Place(7, "Шартрский собор",
                "Собор с лабиринтом на полу и витражами цвета ночного пламени",
                "Шартр", "Франция", "готический собор",
                "https://avatars.mds.yandex.net/get-vertis-journal/4466156/ugc-image_1752227094219/1600x1600",
                1194, date));
        mockServerData.add(new Place(8, "Замок Нойшванштайн",
                "Сказочный замок в тумане альпийских вершин",
                "Швангау", "Германия", "замок",
                "https://fs.tonkosti.ru/9u/yh/9uyhbfe1y74gkogo80g88wgwc.jpg",
                1869, date));
        mockServerData.add(new Place(9, "Собор Святого Вита",
                "Готический собор в сердце Праги, где короли принимали корону",
                "Прага", "Чехия", "готический собор",
                "https://avatars.mds.yandex.net/i?id=a0c2719649012b2058939b5e7c0dbb82_l-5232078-images-thumbs&n=13",
                1344, date));
        mockServerData.add(new Place(10, "Кентерберийский собор",
                "Массивные готические башни над древней паломнической землёй",
                "Кентербери", "Великобритания", "готический собор",
                "https://i.pinimg.com/originals/ee/c0/f8/eec0f873b1561baace9c47a84cc1e946.jpg?nii=t",
                1070, date));
        mockServerData.add(new Place(11, "Бургосский собор",
                "Испанская готика, устремлённая ввысь над сумрачными улицами",
                "Бургос", "Испания", "готический собор",
                "https://avatars.mds.yandex.net/get-altay/5490109/2a0000017dafd69f1f5ad6a6b25fc78fe17e/orig",
                1221, date));
        mockServerData.add(new Place(12, "Базилика Санта-Кроче",
                "Флорентийская базилика с мраморными надгробьями в полумраке",
                "Флоренция", "Италия", "готическая базилика",
                "https://avatars.mds.yandex.net/i?id=2988cc94387f24ef76fa8df551157b89_l-5221562-images-thumbs&n=13",
                1294, date));
    }

    // Ответ сервиса на запрос каталога
    public List<Place> getPlaces() {
        return mockServerData;
    }

    // получение места по айди
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
