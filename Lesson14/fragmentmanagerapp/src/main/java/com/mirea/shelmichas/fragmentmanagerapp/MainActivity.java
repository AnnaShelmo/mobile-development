package com.mirea.shelmichas.fragmentmanagerapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Фрагменты НЕ общаются напрямую: общие данные лежат в ShareViewModel.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);                  // рисуем содержимое под системными панелями
        setContentView(R.layout.activity_main);   // список подключён в XML, контейнер деталей пустой

        // Добавляем отступы, чтобы текст не заезжал под строку состояния и панель навигации
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Только при первом создании: при повороте savedInstanceState != null, и FragmentManager восстановит фрагмент сам, повторно добавлять не нужно.
        // находит контейнер и добавляет CountryDetailsFragment (нижнюю часть) в контейнер деталей динамически — транзакцией FragmentManager (метод replace)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.detailsContainerView, new CountryDetailsFragment(), "details")
                    .commit();
        }
    }
}
