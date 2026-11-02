package com.mirea.shelmichas.fragmentmanagerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * Верхний фрагмент: список стран.
 * При клике просто кладёт выбранную страну в общую ViewModel.
 */
public class CountryListFragment extends Fragment {

    // Данные списка
    private static final String[] COUNTRIES = {
            "Россия", "Франция", "Германия", "Италия", "Испания", "Чехия"
    };

    private ShareViewModel viewModel;

    // отрисовка пустых строк
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(CountryListFragment.class.getSimpleName(), "onCreateView");
        // Раздуваем макет фрагмента (внутри ListView) и возвращаем его как View фрагмента
        return inflater.inflate(R.layout.fragment_country_list, container, false);
    }

    // заполнение строк
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        // Простой адаптер: массив строк -> пункты ListView
        ListView listView = view.findViewById(R.id.listViewCountries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, COUNTRIES);
        listView.setAdapter(adapter);

        // добавляем слушатель, клик по стране: пишем её в ViewModel — детали обновятся сами
        listView.setOnItemClickListener((parent, itemView, position, id) ->
                viewModel.selectItem(COUNTRIES[position]));
    }
}
