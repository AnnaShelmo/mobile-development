package com.mirea.shelmichas.resultapifragmentapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Нижняя шторка (Bottom Sheet). Получает текст через Fragment Result API и показывает его.
 * Прямой ссылки на DataFragment нет — обмен идёт через FragmentManager.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {

    private TextView textViewResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Слушатель с ТЕМ ЖЕ ключом "requestKey", что и в DataFragment.
        // Результат доставляется, когда фрагмент доходит до состояния STARTED,
        // то есть после onCreateView — поэтому textViewResult уже существует.
        getParentFragmentManager().setFragmentResultListener("requestKey", this,
                (requestKey, bundle) -> {
                    // Достаём текст по тому же ключу "key", под которым его положили
                    String text = bundle.getString("key");
                    Log.d(BottomSheetFragment.class.getSimpleName(), "Get text " + text);

                    if (textViewResult != null) {
                        textViewResult.setText(text);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Макет шторки: заголовок + TextView с полученным текстом
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        textViewResult = view.findViewById(R.id.textViewResult);
        return view;
    }
}
