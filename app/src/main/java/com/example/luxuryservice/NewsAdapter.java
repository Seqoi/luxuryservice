package com.example.luxuryservice;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.luxuryservice.Responses.Sale;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<Sale> salesList;

    private final int[] colors = {
            R.color.card1,
            R.color.card2,
            R.color.card3,
            R.color.card4,
            R.color.card5
    };

    public NewsAdapter(List<Sale> salesList) {
        this.salesList = salesList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.title.setText(salesList.get(position).getName());
        holder.description.setText(salesList.get(position).getDescription());
        holder.price.setText(salesList.get(position).getPrice() + "₽");
        holder.newsCard.setBackgroundTintList(ColorStateList.valueOf(holder.itemView.getContext().getColor(colors[position])));

        Glide.with(holder.itemView.getContext()).load(Uri.parse(salesList.get(position % 5).getImage())).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView description;
        private final TextView price;
        private final ImageView image;
        private final CardView newsCard;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            newsCard = itemView.findViewById(R.id.newsCard);
        }
    }
}
