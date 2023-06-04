package com.example.luxuryservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartView> {

    private CartManager cartManager;
    private List<CartElement> cartList;
    private OnPriceChangeListener onPriceChangeListener;
    private int total;

    public CartAdapter(CartManager cartManager) {
        this.onPriceChangeListener = null;
        this.total = 0;
        this.cartManager = cartManager;
        this.cartList = cartManager.getList();
    }

    @NonNull
    @Override
    public CartView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartView holder, int position) {
        calcTotal();

        holder.title.setText(cartList.get(holder.getAdapterPosition()).getName());
        holder.price.setText(cartList.get(holder.getAdapterPosition()).getPrice() + " ₽");
        holder.amount.setText("Пациенты: " + cartList.get(holder.getAdapterPosition()).getAmount());

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.removeElement(cartList.get(holder.getAdapterPosition()));
                calcTotal();
                notifyDataSetChanged();
//                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartManager.addElement(cartList.get(holder.getAdapterPosition()));
                calcTotal();
                notifyDataSetChanged();
//                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    public void calcTotal() {
        int total = 0;
        for (int i = 0; i < cartList.size(); i++) {
            total += Integer.parseInt(cartList.get(i).getPrice()) * cartList.get(i).getAmount();
        }
        if (onPriceChangeListener != null) {
            onPriceChangeListener.onPriceChange(total);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartView extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView amount;
        private TextView price;
        private AppCompatButton minus;
        private AppCompatButton plus;

        public CartView(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.title);
            this.amount = itemView.findViewById(R.id.amount);
            this.price = itemView.findViewById(R.id.price);
            this.minus = itemView.findViewById(R.id.minusButton);
            this.plus = itemView.findViewById(R.id.plusButton);
        }
    }

    public interface OnPriceChangeListener {
        void onPriceChange(int price);
    }

    public void setOnPriceChangeListener(OnPriceChangeListener onPriceChangeListener) {
        this.onPriceChangeListener = onPriceChangeListener;
    }
}
