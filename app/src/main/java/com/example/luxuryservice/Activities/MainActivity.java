package com.example.luxuryservice.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.luxuryservice.MainActivityFragments.ResultsFragment;
import com.example.luxuryservice.MainActivityFragments.SupportFragment;
import com.example.luxuryservice.MainActivityFragments.TestsFragment;
import com.example.luxuryservice.MainActivityFragments.UserFragment;
import com.example.luxuryservice.R;
import com.google.android.material.tabs.TabLayout;

public class   MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setIcon(getDrawable(R.drawable.tab_layout_tests)).setText("Анализы"));
        tabLayout.addTab(tabLayout.newTab().setIcon(getDrawable(R.drawable.tab_layout_results)).setText("Результаты"));
        tabLayout.addTab(tabLayout.newTab().setIcon(getDrawable(R.drawable.tab_layout_support)).setText("Поддержка"));
        tabLayout.addTab(tabLayout.newTab().setIcon(getDrawable(R.drawable.tab_layout_user)).setText("Пользователь"));

        fragmentManager = getSupportFragmentManager();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setFragment(tab.getPosition());
//                tabLayout.getTabAt(tab.getPosition()).select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setFragment(0);
    }

    private void setFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new TestsFragment();
                break;
            case 1:
                fragment = new ResultsFragment();
                break;
            case 2:
                fragment = new SupportFragment();
                break;
            case 3:
                fragment = new UserFragment();
                break;
        }
        beginTransaction(fragment);
    }

    private void beginTransaction(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();
    }
}