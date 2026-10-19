package com.mirea.shelmichas.recyclerviewapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoricalEventViewHolder extends RecyclerView.ViewHolder {

    private final ImageView imageView;
    private final TextView eventNameView;
    private final TextView descriptionView;

    public HistoricalEventViewHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.imageView);
        this.eventNameView = itemView.findViewById(R.id.textViewEventName);
        this.descriptionView = itemView.findViewById(R.id.textViewDescription);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getEventNameView() {
        return eventNameView;
    }

    public TextView getDescriptionView() {
        return descriptionView;
    }
}
