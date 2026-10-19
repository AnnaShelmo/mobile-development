package com.mirea.shelmichas.recyclerviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoricalEventRecyclerViewAdapter
        extends RecyclerView.Adapter<HistoricalEventViewHolder> {

    private final List<HistoricalEvent> events;
    private Context context;

    public HistoricalEventRecyclerViewAdapter(List<HistoricalEvent> events) {
        this.events = events;
    }

    // создание, на выходе пустая карточка с запомненными ссылками, без данных.
    @NonNull
    @Override
    public HistoricalEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View recyclerViewItem = LayoutInflater.from(context)
                .inflate(R.layout.event_item_view, parent, false); // превращаем XML карточки в реальный View
        return new HistoricalEventViewHolder(recyclerViewItem); // заворачиваем его во ViewHolder, который уже нашёл внутри карточки ImageView и 2 TextView
    }

    // заполняет данными
    @Override
    public void onBindViewHolder(@NonNull HistoricalEventViewHolder holder, int position) {
        HistoricalEvent event = this.events.get(position); // берём своё событие по номеру
        String pkgName = context.getPackageName();
        // ищем drawable по имени из модели
        int resID = context.getResources().getIdentifier(event.getImageName(), "drawable", pkgName);
        // через ViewHolder (ссылки уже готовы, без findViewById) кладём
        holder.getImageView().setImageResource(resID);
        holder.getEventNameView().setText(event.getEventName());
        holder.getDescriptionView().setText(event.getDescription());
    }

    // по этому числу RecyclerView понимает длину списка и рисует полосу прокрутки
    @Override
    public int getItemCount() {
        return this.events.size();
    }
}
