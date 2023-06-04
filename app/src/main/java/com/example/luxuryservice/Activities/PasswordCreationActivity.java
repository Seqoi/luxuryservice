package com.example.luxuryservice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luxuryservice.App;
import com.example.luxuryservice.R;
import com.example.luxuryservice.SharedPreferencesManager;

public class PasswordCreationActivity extends AppCompatActivity {

    private LinearLayout pinCodeIndicator;
    private String pinCode = "";

    private App app;
    private SharedPreferencesManager preferencesManager;

    private TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (App) getApplication();
        preferencesManager = app.getPreferencesManager();

        if (preferencesManager.pinCodeIsCreated()) {
            toCreateUser();
        }

        setContentView(R.layout.activity_password_creation);

        pinCodeIndicator = findViewById(R.id.pinCodeDisplay);
        skip = findViewById(R.id.skip);

        setPinIndicators();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCreateUser();
            }
        });
    }

    private void setPinIndicators() {
        ImageView[] indicators = new ImageView[4];
        LinearLayout.LayoutParams indicatorParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        indicatorParams.setMargins(12, 0, 12, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(this.getDrawable(R.drawable.tab_inactive));
            indicators[i].setLayoutParams(indicatorParams);
            pinCodeIndicator.addView(indicators[i]);
        }
    }

    public void enterNumber(View view) {
        if (pinCode.length() < 4) {
            Button key = (Button) view;
            pinCode += key.getText().toString();
            addPinIndicator(pinCode.length() - 1);
            System.out.println(pinCode.length());
            if (pinCode.length() == 4) {
                toCreateUser();
            }
        }
    }

    public void removeNumber(View view) {
        if (pinCode.length() > 0) {
            removePinIndicator(pinCode.length()-1);
            pinCode = pinCode.substring(0, pinCode.length() - 1);
        }
    }

    private void addPinIndicator(int index) {
        ImageView indicator = (ImageView) pinCodeIndicator.getChildAt(index);
        indicator.setImageDrawable(this.getDrawable(R.drawable.tab_active));
    }

    private void removePinIndicator(int index) {
        ImageView indicator = (ImageView) pinCodeIndicator.getChildAt(index);
        indicator.setImageDrawable(this.getDrawable(R.drawable.tab_inactive));
    }

    private void toCreateUser() {
        preferencesManager.savePin(pinCode);
        Intent intent = new Intent(this, UserCardActivity.class);
        startActivity(intent);
        this.finish();
    }
}