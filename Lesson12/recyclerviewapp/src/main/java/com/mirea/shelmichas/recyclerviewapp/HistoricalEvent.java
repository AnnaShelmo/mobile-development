package com.mirea.shelmichas.recyclerviewapp;

public class HistoricalEvent {

    private final String eventName;
    private final String imageName;
    private final String description;

    public HistoricalEvent(String eventName, String imageName, String description) {
        this.eventName = eventName;
        this.imageName = imageName;
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.eventName + " (" + this.description + ")";
    }
}
