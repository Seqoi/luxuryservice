package com.example.luxuryservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchElement> {

    private final List<CartElement> unfilteredList;
    private final List<CartElement> testsList;

    private final CartManager cartManager;

    public SearchAdapter(List<CartElement> testsList, CartManager cartManager) {
        this.testsList = testsList;
        this.unfilteredList = new ArrayList<>(testsList);
        this.cartManager = cartManager;
    }

    @NonNull
    @Override
    public SearchElement onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchElement(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchElement holder, int position) {
        holder.name.setText(testsList.get(position).getName());
        holder.price.setText(testsList.get(position).getPrice() + " Р");
        holder.days.setText(testsList.get(position).getTime_result());

        holder.searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.addElement(testsList.get(holder.getAdapterPosition()));
                Toast.makeText(v.getContext(), "Добавлено в корзину", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CartElement> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(unfilteredList);
            } else {
                String pattern = constraint.toString().toLowerCase();
                for (CartElement test : unfilteredList) {
                    if (test.getName().toLowerCase().contains(pattern)) {
                        filteredList.add(test);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            testsList.clear();
            testsList.addAll( (List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return testsList.size();
    }

    public Filter getFilter() {
        return filter;
    }

    public class SearchElement extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView price;
        private final TextView days;
        private final LinearLayout searchItem;

        public SearchElement(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.title);
            this.price = itemView.findViewById(R.id.price);
            this.days = itemView.findViewById(R.id.days);
            this.searchItem = itemView.findViewById(R.id.search_item);
        }
    }
}
