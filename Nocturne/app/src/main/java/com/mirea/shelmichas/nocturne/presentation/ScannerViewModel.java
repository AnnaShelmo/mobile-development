package com.mirea.shelmichas.nocturne.presentation;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Здесь вся "тяжёлая" часть сканера: чтение фото, загрузка модели и распознавание.
// Fragment только передаёт Uri и показывает то, что приходит через LiveData.
public class ScannerViewModel extends AndroidViewModel {

    // Файл модели лежит в app/src/main/assets.
    private static final String MODEL_FILE = "efficientnet_lite0.tflite";
    // Максимальная сторона фото при декодировании, чтобы не занять всю память.
    private static final int MAX_IMAGE_SIDE = 1024;

    // Распознавание нельзя делать в главном потоке, поэтому используем один фоновый поток.
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Состояние экрана. Fragment подписывается на эти LiveData и обновляет UI.
    private final MutableLiveData<Bitmap> image = new MutableLiveData<>(); // выбранное фото
    private final MutableLiveData<List<Prediction>> predictions = new MutableLiveData<>(); // топ-3
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false); // идёт распознавание
    private final MutableLiveData<String> error = new MutableLiveData<>(); // текст ошибки

    // Модель создаём один раз и переиспользуем при следующих фото.
    // ByteBuffer должен жить, пока жив classifier, поэтому храним его в поле.
    private ByteBuffer modelBuffer;
    private ImageClassifier classifier;

    public ScannerViewModel(Application application) {
        super(application);
    }

    // Геттеры нужны Fragment'у для подписки на состояние.
    public LiveData<Bitmap> getImage() {
        return image;
    }

    public LiveData<List<Prediction>> getPredictions() {
        return predictions;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    // Главный метод: Fragment вызывает его после выбора фото в галерее.
    public void recognize(Uri uri) {
        if (uri == null) {
            return;
        }

        // Сразу переводим экран в состояние "загрузка" и убираем старый результат.
        loading.setValue(true);
        error.setValue(null);
        predictions.setValue(null);

        // Всё тяжёлое выполняем в фоне
        executor.execute(() -> {
            try {
                Bitmap bitmap = decodeBitmap(uri); // 1. читаем и уменьшаем фото
                if (bitmap == null) {
                    throw new IOException("Не удалось прочитать изображение");
                }
                image.postValue(bitmap);                 // 2. показываем предпросмотр
                ensureClassifier();                      // 3. грузим модель при первом запуске
                predictions.postValue(classify(bitmap)); // 4. распознаём
            } catch (Exception e) {
                error.postValue("Не удалось распознать изображение");
            } finally {
                // В любом случае выключаем ProgressBar.
                loading.postValue(false);
            }
        });
    }

    // Читаем картинку из галереи и сразу уменьшаем её.
    private Bitmap decodeBitmap(Uri uri) throws IOException {
        // На Android 9+ ImageDecoder сам учитывает поворот из EXIF,умеет уменьшать фото и приводить цвета к sRGB.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.Source source = ImageDecoder.createSource(
                    getApplication().getContentResolver(), uri);
            return ImageDecoder.decodeBitmap(source, (decoder, info, src) -> {
                int width = info.getSize().getWidth();
                int height = info.getSize().getHeight();
                int maxSide = Math.max(width, height);
                if (maxSide > MAX_IMAGE_SIDE) {
                    float scale = (float) MAX_IMAGE_SIDE / maxSide;
                    decoder.setTargetSize(Math.max(1, Math.round(width * scale)),
                            Math.max(1, Math.round(height * scale)));
                }
                decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE);
                decoder.setTargetColorSpace(ColorSpace.get(ColorSpace.Named.SRGB));
            });
        }

        // Запасной путь для Android 8.0–8.1:
        // сначала узнаём размеры картинки, потом читаем её с уменьшением.
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        try (InputStream input = getApplication().getContentResolver().openInputStream(uri)) {
            BitmapFactory.decodeStream(input, null, bounds);
        }
        int sampleSize = 1;
        while (Math.max(bounds.outWidth, bounds.outHeight) / sampleSize > MAX_IMAGE_SIDE) {
            sampleSize *= 2;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        try (InputStream input = getApplication().getContentResolver().openInputStream(uri)) {
            return BitmapFactory.decodeStream(input, null, options);
        }
    }

    // Ленивая загрузка модели: при первом распознавании читаем .tflite из assets.
    // При следующих вызовах просто возвращаем уже созданный classifier.
    private void ensureClassifier() throws IOException {
        if (classifier != null) {
            return;
        }

        // Читаем файл модели в массив байтов.
        byte[] modelBytes;
        try (InputStream input = getApplication().getAssets().open(MODEL_FILE)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            modelBytes = output.toByteArray();
        }

        // Task Library требует прямой ByteBuffer (не массив).
        // Буфер нельзя освобождать, пока используется classifier.
        modelBuffer = ByteBuffer.allocateDirect(modelBytes.length);
        modelBuffer.put(modelBytes);
        modelBuffer.rewind();

        // 4 потока для CPU-инференса, maxResults = 3 — нужен только топ-3.
        BaseOptions baseOptions = BaseOptions.builder()
                .setNumThreads(4)
                .build();
        ImageClassifier.ImageClassifierOptions options =
                ImageClassifier.ImageClassifierOptions.builder()
                        .setBaseOptions(baseOptions)
                        .setMaxResults(3)
                        .build();
        classifier = ImageClassifier.createFromBufferAndOptions(modelBuffer, options);
    }

    // Само распознавание. Превращаем Bitmap в TensorImage и запускаем модель.
    private List<Prediction> classify(Bitmap bitmap) {
        List<Prediction> result = new ArrayList<>();
        if (classifier == null) {
            return result;
        }

        // TensorImage — это обёртка над Bitmap для TFLite. classify() запускает модель.
        TensorImage tensorImage = TensorImage.fromBitmap(bitmap);
        List<Classifications> classifications = classifier.classify(tensorImage);
        if (classifications.isEmpty()) {
            return result;
        }

        // У модели одна "голова" классификации, поэтому берём первый элемент.
        // Внутри — категории, уже отсортированные по убыванию вероятности.
        for (Category category : classifications.get(0).getCategories()) {
            // Название класса берём из метаданных модели.
            String label = category.getDisplayName();
            if (label == null || label.isEmpty()) {
                label = category.getLabel();
            }
            result.add(new Prediction(toRussian(label), category.getScore()));//переводит известные классы через словарь RU_LABELS, неизвестные оставляет на английском
        }
        return result;
    }

    // Переводим известные классы на русский, остальные оставляем как в ImageNet.
    private String toRussian(String label) {
        if (label == null) {
            return "";
        }
        String translated = RU_LABELS.get(label.toLowerCase(Locale.ROOT));
        return translated != null ? translated : label;
    }

    // очистка
    @Override
    protected void onCleared() {
        super.onCleared();
        // ViewModel уничтожается — освобождаем нативные ресурсы модели.
        // Закрытие ставим в очередь executor, чтобы не оборвать текущее распознавание.
        executor.execute(() -> {
            if (classifier != null) {
                classifier.close();
                classifier = null;
            }
            modelBuffer = null;
        });
        executor.shutdown();
    }

    // Одна строка результата: название класса и уверенность модели.
    public static class Prediction {

        private final String label;
        private final float confidence;

        public Prediction(String label, float confidence) {
            this.label = label;
            this.confidence = confidence;
        }

        public String getLabel() {
            return label;
        }

        public float getConfidence() {
            return confidence;
        }

        // Переводим уверенность из 0..1 в проценты для экрана.
        public int getPercent() {
            return Math.round(confidence * 100);
        }
    }

    // Словарь: английское название класса ImageNet -> русское название.
    private static final Map<String, String> RU_LABELS = new HashMap<>();

    static {
        RU_LABELS.put("church", "Собор / церковь");
        RU_LABELS.put("cathedral", "Собор");
        RU_LABELS.put("basilica", "Базилика");
        RU_LABELS.put("mosque", "Мечеть");
        RU_LABELS.put("monastery", "Монастырь");
        RU_LABELS.put("temple", "Храм");
        RU_LABELS.put("castle", "Замок");
        RU_LABELS.put("fortress", "Крепость");
        RU_LABELS.put("palace", "Дворец");
        RU_LABELS.put("dome", "Купол");
        RU_LABELS.put("bell cote", "Звонница");
        RU_LABELS.put("belfry", "Колокольня");
        RU_LABELS.put("altar", "Алтарь");
        RU_LABELS.put("lighthouse", "Маяк");
        RU_LABELS.put("beacon", "Маяк / бакен");
        RU_LABELS.put("stupa", "Ступа");
        RU_LABELS.put("fountain", "Фонтан");
        RU_LABELS.put("viaduct", "Виадук");
        RU_LABELS.put("aqueduct", "Акведук");
        RU_LABELS.put("triumphal arch", "Триумфальная арка");
        RU_LABELS.put("arch", "Арка");
        RU_LABELS.put("suspension bridge", "Подвесной мост");
        RU_LABELS.put("steel arch bridge", "Арочный мост");
        RU_LABELS.put("pier", "Пирс");
        RU_LABELS.put("dock", "Причал");
        RU_LABELS.put("boathouse", "Лодочный ангар");
        RU_LABELS.put("barn", "Амбар");
        RU_LABELS.put("greenhouse", "Теплица");
        RU_LABELS.put("library", "Библиотека");
        RU_LABELS.put("bookshop", "Книжный магазин");
        RU_LABELS.put("butcher shop", "Мясная лавка");
        RU_LABELS.put("bakery", "Пекарня");
        RU_LABELS.put("barbershop", "Парикмахерская");
        RU_LABELS.put("cinema", "Кинотеатр");
        RU_LABELS.put("confectionery", "Кондитерская");
        RU_LABELS.put("restaurant", "Ресторан");
        RU_LABELS.put("grocery store", "Продуктовый магазин");
        RU_LABELS.put("shoe shop", "Обувной магазин");
        RU_LABELS.put("tobacco shop", "Табачная лавка");
        RU_LABELS.put("toy shop", "Магазин игрушек");
        RU_LABELS.put("pharmacy", "Аптека");
        RU_LABELS.put("prison", "Тюрьма");
        RU_LABELS.put("home theater", "Домашний кинотеатр");
        RU_LABELS.put("mobile home", "Передвижной дом");
        RU_LABELS.put("house", "Дом");
        RU_LABELS.put("log cabin", "Бревенчатая хижина");
        RU_LABELS.put("yurt", "Юрта");
        RU_LABELS.put("igloo", "Иглу");
        RU_LABELS.put("thatch", "Соломенная крыша");
        RU_LABELS.put("tile roof", "Черепичная крыша");
        RU_LABELS.put("birdhouse", "Скворечник");
        RU_LABELS.put("water tower", "Водонапорная башня");
        RU_LABELS.put("clock tower", "Часовая башня");
        RU_LABELS.put("street sign", "Дорожный знак");
        RU_LABELS.put("traffic light", "Светофор");
        RU_LABELS.put("obelisk", "Обелиск");
        RU_LABELS.put("totem pole", "Тотемный столб");
        RU_LABELS.put("monument", "Памятник");
        RU_LABELS.put("statue", "Статуя");
        RU_LABELS.put("stone wall", "Каменная стена");
        RU_LABELS.put("picket fence", "Забор");
        RU_LABELS.put("worm fence", "Изгородь");
        RU_LABELS.put("chainlink fence", "Сетчатый забор");
        RU_LABELS.put("patio", "Патио");
        RU_LABELS.put("swimming pool", "Бассейн");
        RU_LABELS.put("park bench", "Скамейка в парке");
        RU_LABELS.put("balloon", "Воздушный шар");
        RU_LABELS.put("parachute", "Парашют");
        RU_LABELS.put("flagpole", "Флагшток");
        RU_LABELS.put("pedestal", "Пьедестал");
        RU_LABELS.put("vault", "Свод");
        RU_LABELS.put("ruin", "Руины");
        RU_LABELS.put("amphitheater", "Амфитеатр");
        RU_LABELS.put("colosseum", "Колизей");
        RU_LABELS.put("alp", "Гора");
        RU_LABELS.put("cliff", "Скала");
        RU_LABELS.put("coral reef", "Коралловый риф");
        RU_LABELS.put("geyser", "Гейзер");
        RU_LABELS.put("lakeside", "Берег озера");
        RU_LABELS.put("promontory", "Мыс");
        RU_LABELS.put("sandbar", "Песчаная коса");
        RU_LABELS.put("seashore", "Морской берег");
        RU_LABELS.put("valley", "Долина");
        RU_LABELS.put("volcano", "Вулкан");
    }
}
