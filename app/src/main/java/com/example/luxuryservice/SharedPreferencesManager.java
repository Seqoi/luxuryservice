package com.example.luxuryservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.luxuryservice.Responses.UpdateUser;
import com.example.luxuryservice.Responses.User;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "user";
    private final SharedPreferences sharedPreferences;
    private final Context context;
    private final SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public void saveUser(User user){
        editor.putBoolean("dataEntered", true);
        editor.putString("firstname", user.getFirstname());
        editor.putString("lastname", user.getLastname());
        editor.putString("middlename", user.getMiddlename());
        editor.putString("birth", user.getBirth());
        editor.putString("pol", user.getPol());
        editor.putString("image", user.getImage());
        editor.apply();
    }

    public void saveToken(String token) {
        editor.putString("token", token);
        editor.putBoolean("logged", true);
        editor.apply();
    }

    public void savePin(String pin) {
        editor.putString("pin", pin);
        editor.putBoolean("isPin", true);
        editor.apply();
    }

    public String getPin() {
        return sharedPreferences.getString("pin", null);
    }

    public Uri getAvatarUri() {
        return Uri.parse(sharedPreferences.getString("avatar", null));
    }

    public boolean getAvatarStatus() {
        return sharedPreferences.getBoolean("avatarStatus", false);
    }

    public void setAvatarUri(Uri uri) {
        editor.putString("avatar", uri.toString());
        editor.putBoolean("avatarStatus", true);
        editor.apply();
    }

    public boolean pinCodeIsCreated() {
        return sharedPreferences.getBoolean("isPin", false);
    }

    public String getToken() {
        return sharedPreferences.getString("token", null);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("logged", false);
    }

    public boolean getUserStatus() {
        return sharedPreferences.getBoolean("dataEntered", false);
    }

    public UpdateUser getUser() {
        return new UpdateUser(sharedPreferences.getString("firstname", ""),
                sharedPreferences.getString("lastname", ""),
                sharedPreferences.getString("middlename", ""),
                sharedPreferences.getString("birth", ""),
                sharedPreferences.getString("pol", ""));
    }

    public void logOut() {
        editor.clear();
        editor.apply();
    }
}
