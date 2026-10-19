package com.mirea.shelmichas.listviewapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // источник данных: авторы и книги
    private final String[] authors = {
            "Михаил Булгаков", "Фёдор Достоевский", "Лев Толстой", "Лев Толстой",
            "Николай Гоголь", "Иван Тургенев", "Иван Гончаров", "Михаил Лермонтов",
            "Александр Пушкин", "Антон Чехов", "Михаил Шолохов", "Борис Пастернак",
            "Александр Солженицын", "Владимир Набоков", "Виктор Пелевин",
            "Аркадий и Борис Стругацкие", "Владимир Сорокин", "Джордж Оруэлл",
            "Олдос Хаксли", "Франц Кафка", "Альбер Камю", "Жан-Поль Сартр",
            "Габриэль Гарсиа Маркес", "Хорхе Луис Борхес", "Хулио Кортасар",
            "Умберто Эко", "Джон Толкин", "Джеймс Джойс", "Марсель Пруст",
            "Томас Манн", "Герман Гессе", "Харуки Мураками", "Уильям Фолкнер",
            "Эрнест Хемингуэй", "Сергей Довлатов"
    };

    private final String[] books = {
            "Мастер и Маргарита", "Преступление и наказание", "Война и мир",
            "Анна Каренина", "Мёртвые души", "Отцы и дети", "Обломов",
            "Герой нашего времени", "Евгений Онегин", "Вишнёвый сад", "Тихий Дон",
            "Доктор Живаго", "Архипелаг ГУЛАГ", "Лолита", "Generation «П»",
            "Пикник на обочине", "Теллурия", "1984", "О дивный новый мир",
            "Процесс", "Посторонний", "Тошнота", "Сто лет одиночества",
            "Сад расходящихся тропок", "Игра в классики", "Имя розы",
            "Властелин колец", "Улисс", "В поисках утраченного времени",
            "Волшебная гора", "Игра в бисер", "Норвежский лес", "Шум и ярость",
            "Прощай, оружие!", "Заповедник"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.book_list_view); // получение элемента

        // ArrayAdapter связывает массив с элементами списка и пишет данные в text1, проходимся циклом по спискам
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, authors) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // super уже заполнил text1 (автор), дозаполняем text2 (книга)
                View view = super.getView(position, convertView, parent);
                TextView authorView = view.findViewById(android.R.id.text1);
                TextView bookView = view.findViewById(android.R.id.text2);
                authorView.setText(authors[position]); // добавлем текст, сверху автор, снизу произведение
                bookView.setText(books[position]);
                return view;
            }
        };

        listView.setAdapter(adapter);// передаём адаптер списку
    }
}
