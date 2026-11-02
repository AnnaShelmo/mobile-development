package com.mirea.shelmichas.fragmentmanagerapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.Map;

public class CountryDetailsFragment extends Fragment {


    private static final Map<String, String> DETAILS = new HashMap<>();

    //детали стран
    static {
        DETAILS.put("Россия", "Россия\nСтолица: Москва\nНаселение: ~146 млн\nЯзык: русский");
        DETAILS.put("Франция", "Франция\nСтолица: Париж\nНаселение: ~68 млн\nЯзык: французский");
        DETAILS.put("Германия", "Германия\nСтолица: Берлин\nНаселение: ~84 млн\nЯзык: немецкий");
        DETAILS.put("Италия", "Италия\nСтолица: Рим\nНаселение: ~59 млн\nЯзык: итальянский");
        DETAILS.put("Испания", "Испания\nСтолица: Мадрид\nНаселение: ~48 млн\nЯзык: испанский");
        DETAILS.put("Чехия", "Чехия\nСтолица: Прага\nНаселение: ~10 млн\nЯзык: чешский");
    }

    private ShareViewModel viewModel;

    // отрисовка
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(CountryDetailsFragment.class.getSimpleName(), "onCreateView");
        return inflater.inflate(R.layout.fragment_country_details, container, false);
    }

    // заполнение строки
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        TextView textView = view.findViewById(R.id.textViewCountryDetails);
        // получить записанную страну и вывести детали
        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), country -> {
            Log.d(CountryDetailsFragment.class.getSimpleName(), country);
            textView.setText(DETAILS.get(country));
        });
    }
}
