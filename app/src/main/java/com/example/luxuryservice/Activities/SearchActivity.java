package com.example.luxuryservice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxuryservice.App;
import com.example.luxuryservice.CartElement;
import com.example.luxuryservice.CartManager;
import com.example.luxuryservice.R;
import com.example.luxuryservice.SearchAdapter;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TextView exit;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private SearchAdapter searchAdapter;
    private Filter filter;

    private App app;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        app = (App) getApplication();
        cartManager = app.getCartManager();

        recyclerView = findViewById(R.id.search_view);
        searchView = findViewById(R.id.search);

        exit = findViewById(R.id.cancel_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        setupAdapter((List) intent.getSerializableExtra("list"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter.filter(newText);
                return false;
            }
        });
    }

    private void setupAdapter(List<CartElement> testsList) {
        searchAdapter = new SearchAdapter(testsList, cartManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(searchAdapter);
        filter = searchAdapter.getFilter();
    }
}