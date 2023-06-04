package com.example.luxuryservice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.luxuryservice.App;
import com.example.luxuryservice.OnboardingAdapter;
import com.example.luxuryservice.OnboardingItem;
import com.example.luxuryservice.R;
import com.example.luxuryservice.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    public ViewPager2 viewPager;
    private TextView skip;
    private LinearLayout onboardingIndicator;
    public OnboardingAdapter onboardingAdapter;

    public App app;
    public SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (App) getApplication();
        sharedPreferencesManager = app.getPreferencesManager();

        checkForState();

        setContentView(R.layout.activity_onboarding);

        skip = findViewById(R.id.skip);
        onboardingIndicator = findViewById(R.id.onboardingIndicator);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });

        onboardingAdapter = setOnboardingAdapter();

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(onboardingAdapter);

        setOnboardingIndicator();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

    }

    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void toMain() {
        Intent intent = new Intent(this, PasswordCreationActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void setOnboardingIndicator() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(6, 0, 6, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(this.getDrawable(R.drawable.tab_inactive));
            indicators[i].setLayoutParams(layoutParams);
            onboardingIndicator.addView(indicators[i]);
        }

        setCurrentIndicator(0);

    }

    private void setCurrentIndicator(int index) {
        int count = onboardingIndicator.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView indicator = (ImageView) onboardingIndicator.getChildAt(i);
            if (i == index) {
                indicator.setImageDrawable(this.getDrawable(R.drawable.tab_active));
            } else {
                indicator.setImageDrawable(this.getDrawable(R.drawable.tab_inactive));
            }
        }
        if (index == onboardingAdapter.getItemCount() -1) {
            skip.setText("Продолжить");
        } else {
            skip.setText("Пропустить");
        }
    }

    private OnboardingAdapter setOnboardingAdapter() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();
        OnboardingItem tests = new OnboardingItem(R.drawable.onboarding1, R.string.onboarding1title, R.string.onboarding1description);
        OnboardingItem notifications = new OnboardingItem(R.drawable.onboarding2, R.string.onboarding2title, R.string.onboarding2description);
        OnboardingItem monitoring = new OnboardingItem(R.drawable.onboarding3, R.string.onboarding3title, R.string.onboarding3description);

        onboardingItems.add(tests);
        onboardingItems.add(notifications);
        onboardingItems.add(monitoring);

        return new OnboardingAdapter(onboardingItems);
    }

    public void checkForState() {
        if (sharedPreferencesManager.isLoggedIn()) {
            toMain();
        }
    }

}

