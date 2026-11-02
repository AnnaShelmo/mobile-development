package com.mirea.shelmichas.resultapifragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

/**
 * Экран с полем ввода и кнопкой.
 * Отправляет введённый текст в BottomSheetFragment через Fragment Result API.
 */
public class DataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        Button button = view.findViewById(R.id.buttonOpenBottomSheet);
        button.setOnClickListener(click -> {
            // 1. Берём текст из поля ввода
            String text = ((EditText) view.findViewById(R.id.editTextInfo)).getText().toString();

            // 2. Кладём текст в Bundle под ключом "key"
            Bundle bundle = new Bundle();
            bundle.putString("key", text);

            // 3. Отправляем результат в ДОЧЕРНИЙ FragmentManager под ключом "requestKey".
            //    Слушателя ещё нет, поэтому результат будет храниться здесь до его появления.
            getChildFragmentManager().setFragmentResult("requestKey", bundle);

            // 4. Показываем шторку в ТОМ ЖЕ дочернем менеджере —
            //    поэтому её getParentFragmentManager() и есть тот менеджер, куда мы положили результат.
            BottomSheetFragment bottomSheet = new BottomSheetFragment();
            bottomSheet.show(getChildFragmentManager(), "ModalBottomSheet");
        });

        return view;
    }
}
