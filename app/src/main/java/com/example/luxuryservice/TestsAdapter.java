package com.example.luxuryservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.TestsViewHolder> implements Filterable {

    private List<CartElement> testsList;
    private List<CartElement> unfilteredList;
    private CartListener cartListener;

    private CartManager cartManager;

    private BottomSheetDialog bottomSheet;

    private TextView sheetTitle;
    private TextView sheetDescription;
    private TextView sheetPreparations;
    private TextView sheetResults;
    private TextView sheetBiomaterial;
    private AppCompatButton sheetAdd;

    public TestsAdapter(List<CartElement> testsList, BottomSheetDialog bottomSheet, CartManager cartManager) {
        this.testsList = testsList;
        this.unfilteredList = new ArrayList<>(testsList);
        this.bottomSheet = bottomSheet;
        this.cartListener = null;
        this.cartManager = cartManager;
    }

    @NonNull
    @Override
    public TestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.tests_item, parent, false);
        return new TestsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TestsViewHolder holder, int position) {
        holder.title.setText(testsList.get(holder.getAdapterPosition()).getName());
        holder.days.setText(testsList.get(holder.getAdapterPosition()).getTime_result());
        holder.price.setText(testsList.get(holder.getAdapterPosition()).getPrice() + " ₽");

        if (cartManager.getList().contains(testsList.get(holder.getAdapterPosition()))) {
            disableButton(holder);
        } else {
            enableButton(holder);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.show();

                sheetTitle = bottomSheet.findViewById(R.id.title_sheet);
                sheetDescription = bottomSheet.findViewById(R.id.description);
                sheetPreparations = bottomSheet.findViewById(R.id.preparations);
                sheetResults = bottomSheet.findViewById(R.id.results);
                sheetBiomaterial = bottomSheet.findViewById(R.id.biomaterial);
                sheetAdd = bottomSheet.findViewById(R.id.addButton);

                sheetTitle.setText(testsList.get(holder.getAdapterPosition()).getName());
                sheetDescription.setText(testsList.get(holder.getAdapterPosition()).getDescription());
                sheetPreparations.setText(testsList.get(holder.getAdapterPosition()).getPreparation());
                sheetResults.setText(testsList.get(holder.getAdapterPosition()).getTime_result());
                sheetBiomaterial.setText(testsList.get(holder.getAdapterPosition()).getBio());

                if (!cartManager.getList().contains(testsList.get(holder.getAdapterPosition()))) {
                    sheetAdd.setText("Добавить за " + testsList.get(holder.getAdapterPosition()).getPrice() + " ₽");
                    sheetAdd.setBackgroundDrawable(v.getContext().getDrawable(R.drawable.button_bg_available));
                    sheetAdd.setEnabled(true);
                } else {
                    sheetAdd.setText("В корзине");
                    sheetAdd.setBackgroundDrawable(v.getContext().getDrawable(R.drawable.button_bg_unavailable));
                    sheetAdd.setEnabled(false);
                }

                sheetAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToCard(testsList.get(holder.getAdapterPosition()));
                        disableButton(holder);
                        bottomSheet.cancel();
                    }
                });
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cartManager.getList().contains(testsList.get(holder.getAdapterPosition()))) {
                    addToCard(testsList.get(holder.getAdapterPosition()));
                    disableButton(holder);
                } else {
                    removeFromCard(testsList.get(holder.getAdapterPosition()));
                    enableButton(holder);
                }
            }
        });
    }

    private void disableButton(TestsViewHolder holder) {
        holder.add.setTextColor(holder.itemView.getContext().getColor(R.color.gray));
        holder.add.setBackgroundDrawable(holder.itemView.getContext().getDrawable(R.drawable.button_bg_unavailable));
        holder.add.setText("Убрать");
    }
    private void enableButton(TestsViewHolder holder) {
        holder.add.setTextColor(holder.itemView.getContext().getColor(R.color.white));
        holder.add.setBackgroundDrawable(holder.itemView.getContext().getDrawable(R.drawable.button_bg_available));
        holder.add.setText("Добавить");
    }

    private void removeFromCard(CartElement cartElement) {
        cartManager.clearElement(cartElement);
    }

    private void addToCard(CartElement cartElement) {
        cartManager.addElement(cartElement);
    }

    @Override
    public int getItemCount() {
        return testsList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CartElement> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(unfilteredList);
            } else {
                String pattern = constraint.toString().toLowerCase().trim();
                for (CartElement test : unfilteredList) {
                    if (test.getCategory().toLowerCase().contains(pattern)) {
                        filteredList.add(test);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            testsList.clear();
            testsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class TestsViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView days;
        private TextView price;
        private AppCompatButton add;
        private CardView card;

        public TestsViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.test_card);
            title = itemView.findViewById(R.id.title);
            days = itemView.findViewById(R.id.days);
            price = itemView.findViewById(R.id.price);
            add = itemView.findViewById(R.id.addButton);
            add.setBackgroundDrawable(itemView.getContext().getDrawable(R.drawable.button_bg_available));
        }
    }

    public interface CartListener {
        void onElementChanged();
    }

    public void setOnElementChangedListener(CartListener cartListener) {
        this.cartListener = cartListener;
    }

}
