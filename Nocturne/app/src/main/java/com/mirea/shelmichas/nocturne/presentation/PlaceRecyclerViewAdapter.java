package com.mirea.shelmichas.nocturne.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.shelmichas.nocturne.R;
import com.mirea.shelmichas.nocturne.domain.models.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceRecyclerViewAdapter
        extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.PlaceViewHolder> {

    private List<Place> itemList = new ArrayList<>();

    public void setItems(List<Place> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerViewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place_view, parent, false);
        return new PlaceViewHolder(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        private final ImageView imageView;
        private final TextView nameView;
        private final TextView cityView;
        private final TextView descriptionView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            this.imageView = itemView.findViewById(R.id.imageView);
            this.nameView = itemView.findViewById(R.id.textViewPlaceName);
            this.cityView = itemView.findViewById(R.id.textViewPlaceCity);
            this.descriptionView = itemView.findViewById(R.id.textViewPlaceDescription);
        }

        public void bind(Place item) {
            this.nameView.setText(item.getName());
            this.cityView.setText(item.getCity() + ", " + item.getCountry() + " · " + item.getYear());
            this.descriptionView.setText(item.getDescription());

            // картинка ищется в drawable по имени из модели (как flagName в примере)
            int resID = context.getResources()
                    .getIdentifier(item.getImageUrl(), "drawable", context.getPackageName());
            if (resID != 0) {
                this.imageView.setImageResource(resID);
            } else {
                this.imageView.setImageDrawable(null);
            }
        }
    }
}
