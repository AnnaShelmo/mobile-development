package com.mirea.shelmichas.mireaproject;

// =========================================================
// FOREGROUND SERVICE - МУЗЫКАЛЬНЫЙ ПЛЕЕР
// =========================================================
// Foreground Service - это сервис, который работает в фоне
// и показывает уведомление пользователю.
// Музыка продолжает играть даже когда приложение закрыто.
// =========================================================

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PlayerService extends Service {
    
    // Медиаплеер для воспроизведения музыки
    private MediaPlayer mediaPlayer;
    
    // ID канала уведомлений (для Android 8+)
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    // Метод вызывается при привязке сервиса (не используем)
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Метод вызывается при запуске сервиса
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Начинаем воспроизведение
        mediaPlayer.start();
        
        // Когда музыка закончится - убираем уведомление
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                stopForeground(true);  // Убираем уведомление
            }
        });
        
        return super.onStartCommand(intent, flags, startId);
    }

    // Метод вызывается при создании сервиса (один раз)
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Загружаем картинку для уведомления
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ph);
        if (bitmap != null) {
            // Масштабируем до нужного размера (уведомления требуют небольшие картинки)
            bitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, true);
        }
        
        // =====================================
        // СОЗДАЁМ УВЕДОМЛЕНИЕ
        // =====================================
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Playing...")                              // Текст под заголовком
                .setSmallIcon(R.mipmap.ic_launcher)                       // Иконка в уведомлении
                .setLargeIcon(bitmap)                                      // Большая картинка (справа)
                .setPriority(NotificationCompat.PRIORITY_HIGH)            // Высокий приоритет
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()  // Стиль медиа
                        .setShowActionsInCompactView(0))                   // Показывать кнопку play
                .setContentTitle("Music Player")                          // Заголовок уведомления
                .setContentText("MireaProject");                           // Описание
        
        // =====================================
        // СОЗДАЁМ КАНАЛ УВЕДОМЛЕНИЙ (Android 8+)
        // =====================================
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Student FIO Notification", importance);
        channel.setDescription("MIREA Channel");
        
        // Регистрируем канал в системе
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);
        
        // =====================================
        // ЗАПУСКАЕМ СЕРВИС КАК FOREGROUND
        // =====================================
        // Параметр 1 - ID уведомления
        // Параметр 2 - само уведомление
        startForeground(1, builder.build());
        
        // =====================================
        // СОЗДАЁМ И НАСТРАИВАЕМ МЕДИАПЛЕЕР
        // =====================================
        mediaPlayer = MediaPlayer.create(this, R.raw.true_widow);  // Загружаем трек из ресурсов
        mediaPlayer.setLooping(false);                                // Не повторять
    }

    // Метод вызывается при уничтожении сервиса
    @Override
    public void onDestroy() {
        stopForeground(true);  // Убираем уведомление
        mediaPlayer.stop();     // Останавливаем музыку
    }
}