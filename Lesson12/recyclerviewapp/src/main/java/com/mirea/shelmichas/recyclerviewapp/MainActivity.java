package com.mirea.shelmichas.recyclerviewapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        List<HistoricalEvent> events = getListData();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new HistoricalEventRecyclerViewAdapter(events));
    }

    private List<HistoricalEvent> getListData() {
        List<HistoricalEvent> list = new ArrayList<>();
        list.add(new HistoricalEvent("Крещение Руси", "event_baptism_rusi",
                "988 год — принятие христианства князем Владимиром"));
        list.add(new HistoricalEvent("Куликовская битва", "event_kulikovo_battle",
                "1380 год — разгром войск Мамая Дмитрием Донским"));
        list.add(new HistoricalEvent("Открытие Америки", "event_columbus",
                "1492 год — экспедиция Христофора Колумба"));
        list.add(new HistoricalEvent("Основание Санкт-Петербурга", "event_saint_petersburg",
                "1703 год — город заложен Петром I"));
        list.add(new HistoricalEvent("Бородинское сражение", "event_borodino",
                "1812 год — битва с армией Наполеона"));
        list.add(new HistoricalEvent("Отмена крепостного права", "event_serfdom_abolition",
                "1861 год — манифест Александра II"));
        list.add(new HistoricalEvent("Октябрьская революция", "event_october_revolution",
                "1917 год — приход к власти большевиков"));
        list.add(new HistoricalEvent("Первый полёт человека в космос", "event_space_flight",
                "1961 год — полёт Юрия Гагарина"));
        list.add(new HistoricalEvent("Победа в Великой Отечественной войне", "event_victory_1945",
                "1945 год — окончание войны в Европе"));
        list.add(new HistoricalEvent("Высадка человека на Луну", "event_moon_landing",
                "1969 год — миссия «Аполлон-11»"));
        list.add(new HistoricalEvent("Падение Берлинской стены", "event_berlin_wall",
                "1989 год — открытие границы между ГДР и ФРГ"));
        list.add(new HistoricalEvent("Принятие Конституции РФ", "event_constitution",
                "1993 год — всенародное голосование"));
        return list;
    }
}
