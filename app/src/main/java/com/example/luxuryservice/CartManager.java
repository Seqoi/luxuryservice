package com.example.luxuryservice;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private final List<CartElement> cartList;
    private UpdateListener updateListener;

    public CartManager() {
        updateListener = null;
        cartList = new ArrayList<>();
    }

    public void addElement(CartElement cartElement) {
        if (!cartList.contains(cartElement)) {
            cartList.add(cartElement);
        } else {
            cartList.get(cartList.indexOf(cartElement)).setAmount(cartElement.getAmount() + 1);
        }
        if (updateListener != null) {
            updateListener.onUpdate();
        }
    }

    public void removeElement(CartElement cartElement) {
        if (cartList.get(cartList.indexOf(cartElement)).getAmount() == 1) {
            cartList.remove(cartElement);
        } else {
            cartList.get(cartList.indexOf(cartElement)).setAmount(cartElement.getAmount() - 1);
        }
        if (updateListener != null) {
            updateListener.onUpdate();
        }
    }

    public void clearElement(CartElement cartElement) {
        cartList.remove(cartElement);
        if (updateListener != null) {
            updateListener.onUpdate();
        }
    }

    public List<CartElement> getList() {
        return cartList;
    }

    public void setOnUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public interface UpdateListener {
        void onUpdate();
    }
}
