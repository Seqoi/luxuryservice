package com.example.luxuryservice;

import android.app.Application;

public class App extends Application {

    private MedicService medicService;
    private SharedPreferencesManager preferencesManager;
    private CartManager cartManager;

    @Override
    public void onCreate() {
        super.onCreate();

        cartManager = new CartManager();
        medicService = new MedicService();
        preferencesManager = new SharedPreferencesManager(getApplicationContext());
    }

    public MedicService getService() {
        return medicService;
    }

    public SharedPreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public CartManager getCartManager() {
        return cartManager;
    }

    public void setPreferencesManager(SharedPreferencesManager sharedPreferencesManager) {
        this.preferencesManager = sharedPreferencesManager;
    }
}
