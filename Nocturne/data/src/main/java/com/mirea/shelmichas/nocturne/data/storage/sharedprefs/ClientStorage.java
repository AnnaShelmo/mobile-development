package com.mirea.shelmichas.nocturne.data.storage.sharedprefs;

import com.mirea.shelmichas.nocturne.data.storage.sharedprefs.models.Client;

public interface ClientStorage {
    Client get();

    boolean save(Client client);
}