package com.mirea.shelmichas.retrofitapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("todos") // https://jsonplaceholder.typicode.com/todos
    Call<List<Todo>> getTodos(); // тип ответа: список объектов Todo

    // обновление записи при изменении состояния CheckBox
    @PUT("todos/{id}")
    Call<Todo> updateTodo(@Path("id") int id, @Body Todo todo);
}
