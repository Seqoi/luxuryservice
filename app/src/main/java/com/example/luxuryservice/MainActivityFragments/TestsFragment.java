package com.example.luxuryservice.MainActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxuryservice.Activities.CartActivity;
import com.example.luxuryservice.Activities.SearchActivity;
import com.example.luxuryservice.App;
import com.example.luxuryservice.CartElement;
import com.example.luxuryservice.CartManager;
import com.example.luxuryservice.MedicService;
import com.example.luxuryservice.NewsAdapter;
import com.example.luxuryservice.R;
import com.example.luxuryservice.Responses.Sale;
import com.example.luxuryservice.Responses.Tests;
import com.example.luxuryservice.TestsAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestsFragment extends Fragment {

    private BottomSheetDialog bottomSheetDialog;

    private RecyclerView salesView;
    private RecyclerView testsView;
    private RecyclerView.LayoutManager horisontalLayoutManager;
    private RecyclerView.LayoutManager verticalLayoutManager;
    private TestsAdapter testsAdapter;

    private List<Sale> sales;
    private List<CartElement> tests;

    private App app;
    private MedicService service;
    private CartManager cartManager;

    private SearchView searchBar;

    private LinearLayout toCart;

    private AppCompatButton[] filters;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = (App) requireActivity().getApplication();
        service = app.getService();
        cartManager = app.getCartManager();

        salesView = requireView().findViewById(R.id.news_view);
        testsView = requireView().findViewById(R.id.tests_view);

        searchBar = requireView().findViewById(R.id.search);

        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        horisontalLayoutManager = new LinearLayoutManager(requireView().getContext(), LinearLayoutManager.HORIZONTAL, false);
        verticalLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,  false);

        salesView.setLayoutManager(horisontalLayoutManager);
        testsView.setLayoutManager(verticalLayoutManager);

        Call<List<Sale>> callSales = service.getApi().getNews();

        toCart = requireView().findViewById(R.id.to_cart_button);

        callSales.enqueue(new Callback<List<Sale>>() {
            @Override
            public void onResponse(Call<List<Sale>> call, Response<List<Sale>> response) {
                if (response.code() == 200) {
                    sales = response.body();
                    initSalesView();
                }
            }

            @Override
            public void onFailure(Call<List<Sale>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        Call<List<Tests>> callTests = service.getApi().getTests();

        callTests.enqueue(new Callback<List<Tests>>() {
            @Override
            public void onResponse(Call<List<Tests>> call, Response<List<Tests>> response) {
                if (response.code() == 200) {
                    tests = new ArrayList<>();
                    List<Tests> bodyList = response.body();
                    for(Tests body : bodyList) {
                        tests.add(new CartElement(
                                body.getId(),
                                body.getName(),
                                body.getDescription(),
                                body.getPrice(),
                                body.getCategory(),
                                body.getTime_result(),
                                body.getPreparation(),
                                body.getBio()
                        ));
                    }

                    initTestsView();
                }
            }

            @Override
            public void onFailure(Call<List<Tests>> call, Throwable t) {

            }
        });

        setupFilters();

        searchBar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    toSearchActivity();
                    searchBar.clearFocus();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (testsAdapter != null) {
            testsAdapter.notifyDataSetChanged();
            countPrice();
        }
    }

    private void initSalesView() {
        salesView.setAdapter(new NewsAdapter(sales));
    }

    private void initTestsView() {
        testsAdapter = new TestsAdapter(tests, bottomSheetDialog, cartManager);
        testsView.setAdapter(testsAdapter);

        cartManager.setOnUpdateListener(new CartManager.UpdateListener() {
            @Override
            public void onUpdate() {
                countPrice();
            }
        });

    }

    private void countPrice() {
        if (!cartManager.getList().isEmpty()) {
            toCart.setVisibility(View.VISIBLE);
            TextView price = toCart.findViewById(R.id.cart_price);
            price.setText(calcPrice() + " ₽");
        } else {
            toCart.setVisibility(View.GONE);
        }

        toCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCart();
            }
        });
    }

    private void toCart() {
        Intent intent = new Intent(requireContext(), CartActivity.class);
        startActivity(intent);
    }

    private int calcPrice() {
        int price = 0;
        for (int i = 0; i < cartManager.getList().size(); i++) {
            price += Integer.parseInt(cartManager.getList().get(i).getPrice()) * cartManager.getList().get(i).getAmount();
        }
        return price;
    }

    public TestsFragment() {
    }

    private void setupFilters() {
        filters = new AppCompatButton[4];
        boolean[] state = new boolean[4];
        for (int i = 0; i < filters.length; i++) {
            state[i] = false;
            filters[i] = requireView().findViewById(getResources().getIdentifier("filter" + (i + 1), "id", getContext().getPackageName()));
            filters[i].setTextColor(getContext().getColor(R.color.gray));
            filters[i].setBackgroundDrawable(getContext().getDrawable(R.drawable.text_input_bg));
            final int[] n = {i};
            filters[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < filters.length; i++) {
                        if (i == n[0] && !state[i]) {
                            state[i] = true;
                            filters[i].setTextColor(getContext().getColor(R.color.white));
                            filters[i].setBackgroundDrawable(getContext().getDrawable(R.drawable.button_bg_available));
                            changeFilter(i);
                        } else if (i == n[0] && state[i]) {
                            filters[i].setTextColor(getContext().getColor(R.color.gray));
                            filters[i].setBackgroundDrawable(getContext().getDrawable(R.drawable.text_input_bg));
                            changeFilter(4);
                            state[i] = false;
                        } else {
                            filters[i].setTextColor(getContext().getColor(R.color.gray));
                            filters[i].setBackgroundDrawable(getContext().getDrawable(R.drawable.text_input_bg));
                            state[i] = false;
                        }
                    }
                }
            });
        }
    }

    private void changeFilter(int index) {
        if (testsAdapter != null) {
            String filter;
            switch (index) {
                case 0:
                    filter = "Популярные";
                    break;
                case 1:
                    filter = "COVID";
                    break;
                case 2:
                    filter = "Онкогенетические";
                    break;
                case 3:
                    filter = "ЗОЖ";
                    break;
                default:
                    filter = null;
                    break;
            }
            testsAdapter.getFilter().filter(filter);
        }
    }

    private void toSearchActivity() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("list", (Serializable) tests);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tests, container, false);
    }
}