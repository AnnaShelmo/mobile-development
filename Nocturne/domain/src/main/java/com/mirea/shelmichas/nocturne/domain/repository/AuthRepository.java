package com.mirea.shelmichas.nocturne.domain.repository;

public interface AuthRepository {
    void signIn(String email, String password, AuthCallback callback);

    void signUp(String email, String password, AuthCallback callback);

    void signOut();

    boolean isLoggedIn(); // «пользователь авторизован?»
}