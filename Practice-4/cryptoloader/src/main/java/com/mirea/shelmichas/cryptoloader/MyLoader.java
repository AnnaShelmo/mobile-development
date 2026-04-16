package com.mirea.shelmichas.cryptoloader;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class MyLoader extends AsyncTaskLoader<String> {

    public static final String ARG_WORD = "word";
    private byte[] cryptText;
    private byte[] key;

    public MyLoader(@NonNull Context context, @Nullable Bundle args) {
        super(context);
        if (args != null) {
            cryptText = args.getByteArray(ARG_WORD);
            key = args.getByteArray("key");
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        javax.crypto.spec.SecretKeySpec originalKey = new javax.crypto.spec.SecretKeySpec(key, 0, key.length, "AES");
        return MainActivity.decryptMsg(cryptText, originalKey);
    }
}
