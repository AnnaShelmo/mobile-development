package com.mirea.shelmichas.fragmentmanagerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Общая ViewModel двух фрагментов, живёт в области видимости Activity: оба фрагмента получают ОДИН и тот же экземпляр,поэтому могут обмениваться данными, ничего не зная друг о друге.
 */
public class ShareViewModel extends ViewModel {

    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();// хранит выбранную страну; Mutable — потому что менять можно

    //запись страны по клику
    public void selectItem(String item) {
        selectedItem.setValue(item); // только из главного потока
    }

    // получить странуу, отдаёт LiveData — только для чтения/подписки
    public LiveData<String> getSelectedItem() {
        return selectedItem;
    }
}
