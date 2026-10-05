package com.mirea.shelmichas.nocturne.domain.repository;

public interface AuthCallback {
    void onSuccess();

    void onFailure(String message);
}