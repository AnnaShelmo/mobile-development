package com.mirea.shelmichas.retrofitapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    // набор картинок из интернета (реальные фото) для загрузки через Picasso
    private static final String[] IMAGE_URLS = {
            "https://i.pinimg.com/1200x/dd/35/17/dd3517a25beed9890958b82b650fb9e2.jpg",
            "https://i.pinimg.com/736x/35/c1/60/35c160208ab0d44f22ccb82d70241513.jpg",
            "https://i.pinimg.com/736x/f9/97/63/f997632b87b8b2759d7bc2da86908b22.jpg",
            "https://i.pinimg.com/736x/da/70/92/da7092722debc85abbb710f1333d633e.jpg",
            "https://i.pinimg.com/736x/8e/14/30/8e143016fcf8f33e21949ec2357d3129.jpg"
    };

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Todo> todos;
    private final ApiService apiService;

    // настройки отображения изображений
    private boolean centerCrop = true;
    private boolean circle = false;
    private boolean resize = false;

    public TodoAdapter(Context context, List<Todo> todoList, ApiService apiService) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.todos = todoList;
        this.apiService = apiService;
    }

    // смена параметров отображения с перерисовкой списка
    public void setCenterCrop(boolean value) {
        this.centerCrop = value;
        notifyDataSetChanged();
    }

    public void setCircle(boolean value) {
        this.circle = value;
        notifyDataSetChanged();
    }

    public void setResize(boolean value) {
        this.resize = value;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);// берёт по позиции
        holder.textViewTitle.setText(todo.getTitle());// ставит title в TextView

        // убираем слушатель, чтобы он не сработал при установке значения из данных
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(Boolean.TRUE.equals(todo.getCompleted()));

        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setCompleted(isChecked);
            updateTodo(todo);
        });

        loadImage(holder, position);
    }

    // загрузка изображения из интернета с настройкой параметров отображения
    private void loadImage(TodoViewHolder holder, int position) {
        String url = IMAGE_URLS[position % IMAGE_URLS.length];

        com.squareup.picasso.RequestCreator request = Picasso.get()
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        // centerCrop требует заданных размеров, поэтому фиксируем их в этом случае
        if (resize || centerCrop) {
            request.resize(200, 200);
        }
        if (centerCrop) {
            request.centerCrop();
        }
        if (circle) {
            request.transform(circleTransformation());
        }
        request.into(holder.imageViewTodo);
    }

    // собственная трансформация «круг» без сторонних библиотек
    private Transformation circleTransformation() {
        return new Transformation() {
            @NonNull
            @Override
            public android.graphics.Bitmap transform(@NonNull android.graphics.Bitmap source) {
                int size = Math.min(source.getWidth(), source.getHeight());
                android.graphics.Bitmap output = android.graphics.Bitmap.createBitmap(
                        size, size, android.graphics.Bitmap.Config.ARGB_8888);
                android.graphics.Canvas canvas = new android.graphics.Canvas(output);
                android.graphics.BitmapShader shader = new android.graphics.BitmapShader(source,
                        android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
                android.graphics.Paint paint = new android.graphics.Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                float radius = size / 2f;
                canvas.drawCircle(radius, radius, radius, paint);
                // Picasso требует освободить исходный Bitmap после трансформации
                source.recycle();
                return output;
            }

            @NonNull
            @Override
            public String key() {
                return "circle";
            }
        };
    }

    // сколько в списке строк
    @Override
    public int getItemCount() {
        return todos.size();
    }

    // PUT-запрос на обновление тodo при обновлении состояния CheckBox
    private void updateTodo(Todo todo) {
        Call<Todo> call = apiService.updateTodo(todo.getId(), todo);
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(MainActivity.TAG, "Обновлено: id=" + response.body().getId()
                            + ", completed=" + response.body().getCompleted());
                } else {
                    Log.e(MainActivity.TAG, "onResponse: " + response.code());
                }
            }

            // метод для обработки ошибок, вывод в Toast
            @Override
            public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
                Toast.makeText(context.getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
