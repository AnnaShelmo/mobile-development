package com.mirea.shelmichas.nocturne.data.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mirea.shelmichas.nocturne.domain.repository.AuthCallback;
import com.mirea.shelmichas.nocturne.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {

    private final FirebaseAuth mAuth;

    public AuthRepositoryImpl() {
        mAuth = FirebaseAuth.getInstance();
    }

    //вход
    @Override
    public void signIn(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            final String message = task.getException() != null
                                    ? task.getException().getMessage() : "Authentication failed.";
                            callback.onFailure(message);
                        }
                    }
                });
    }

    // регистрация
    @Override
    public void signUp(String email, String password, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            final String message = task.getException() != null
                                    ? task.getException().getMessage() : "Registration failed.";
                            callback.onFailure(message);
                        }
                    }
                });
    }

    @Override
    public void signOut() {
        mAuth.signOut();
    }

    @Override
    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }
}