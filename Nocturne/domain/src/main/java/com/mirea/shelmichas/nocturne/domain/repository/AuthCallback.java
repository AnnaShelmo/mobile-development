package com.mirea.shelmichas.nocturne.domain.repository;

public interface AuthCallback {
    void onSuccess();//успех входа

    void onFailure(String message);//ошибка
}