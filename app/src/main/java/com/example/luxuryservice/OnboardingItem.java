package com.example.luxuryservice;

public class OnboardingItem {
    private final int image;
    private final int title;
    private final int description;

    public OnboardingItem(int image, int title, int description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public int getTitle() {
        return title;
    }

    public int getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }
}
