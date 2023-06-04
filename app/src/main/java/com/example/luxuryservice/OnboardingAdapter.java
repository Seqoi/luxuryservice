package com.example.luxuryservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_onboarding, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnBoardingData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    public static class OnboardingViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView description;
        private final ImageView image;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.onboardingTitle);
            this.description = itemView.findViewById(R.id.onboardingDescription);
            this.image = itemView.findViewById(R.id.onboardingImage);
        }

        private void setOnBoardingData(OnboardingItem onBoardingItem){
            title.setText(onBoardingItem.getTitle());
            description.setText(onBoardingItem.getDescription());
            image.setImageResource(onBoardingItem.getImage());
        }
    }
}
