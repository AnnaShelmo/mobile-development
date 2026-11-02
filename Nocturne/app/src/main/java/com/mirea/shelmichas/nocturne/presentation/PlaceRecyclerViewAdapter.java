package com.mirea.shelmichas.nocturne.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.shelmichas.nocturne.R;
import com.mirea.shelmichas.nocturne.domain.models.Place;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlaceRecyclerViewAdapter
        extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.PlaceViewHolder> {

    // Нажатие на карточку обрабатывает экран, который показывает список.
    public interface OnPlaceClickListener {
        void onPlaceClick(Place place);
    }

    private List<Place> itemList = new ArrayList<>();
    private final OnPlaceClickListener listener;

    public PlaceRecyclerViewAdapter(OnPlaceClickListener listener) {
        this.listener = listener;
    }

    //получение карточки
    public void setItems(List<Place> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }

    // создание, на выходе пустая карточка с запомненными ссылками, без данных.
    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerViewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place_view, parent, false);
        return new PlaceViewHolder(recyclerViewItem);
    }

    // заполнение данными
    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView nameView;
        private final TextView cityView;
        private final TextView descriptionView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.nameView = itemView.findViewById(R.id.textViewPlaceName);
            this.cityView = itemView.findViewById(R.id.textViewPlaceCity);
            this.descriptionView = itemView.findViewById(R.id.textViewPlaceDescription);
        }

        public void bind(Place item) {
            this.nameView.setText(item.getName());
            this.cityView.setText(item.getCity() + ", " + item.getCountry() + " · " + item.getYear());
            this.descriptionView.setText(item.getDescription());

            // изображение загружается из сети через Picasso
            Picasso.get()
                    .load(item.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(this.imageView);

            // открытие страницы сущности
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlaceClick(item);
                }
            });
        }
    }
}
