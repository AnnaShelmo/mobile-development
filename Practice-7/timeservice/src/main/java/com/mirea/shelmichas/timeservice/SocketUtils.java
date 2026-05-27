package com.mirea.shelmichas.timeservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

// Утилита для работы с сокетами
// getReader() - создаёт BufferedReader для чтения данных из сокета
public class SocketUtils {
    public static BufferedReader getReader(Socket s) throws IOException {
        return new BufferedReader(new InputStreamReader(s.getInputStream()));
    }
}
