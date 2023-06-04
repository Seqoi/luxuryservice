package com.example.luxuryservice.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luxuryservice.App;
import com.example.luxuryservice.BottomSheetUsersAdapter;
import com.example.luxuryservice.CartManager;
import com.example.luxuryservice.R;
import com.example.luxuryservice.Responses.OrderUser;
import com.example.luxuryservice.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private EditText address;
    private EditText datetime;
    private TextView user_name;
    private TextView user_image;
    private EditText phone;
    private AppCompatButton addUser;
    private TextView back;
    private LinearLayout user;

    private AppCompatButton sheet_back;
    private AppCompatButton sheet_confirm;
    private AppCompatButton sheet_addUser;
    private RecyclerView sheet_usersView;

    private App app;
    private SharedPreferencesManager sharedPreferencesManager;
    private CartManager cartManager;
    private List<OrderUser> usersList;

    private Calendar calendar;
    private BottomSheetDialog bottomSheetDialog;

    private OrderUser selectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        app = (App) getApplication();
        sharedPreferencesManager = app.getPreferencesManager();
        cartManager = app.getCartManager();

        initUsersList();

        address = findViewById(R.id.address_input);
        datetime = findViewById(R.id.date_time_input);
        user_name = findViewById(R.id.user_name);
        user_image = findViewById(R.id.user_image);
        phone = findViewById(R.id.phone_input);
        back = findViewById(R.id.back);
        addUser = findViewById(R.id.add_user);
        user = findViewById(R.id.user);
        calendar = Calendar.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        initBottomSheetDialog();

        datetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month += 1;
                        datetime.setText(dayOfMonth + "." + month + "." + year);
                    }
                },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        selectUser(usersList.get(0));
        selectedUser = usersList.get(0);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

    }

    private void selectUser(OrderUser orderUser) {
        user_name.setText(orderUser.getName());
        switch (orderUser.getGender()) {
            case "Мужской":
                user_image.setText("\uD83D\uDC66\uD83C\uDFFC");
                break;
            case "Женский":
                user_image.setText("\uD83D\uDC67\uD83C\uDFFC");
                break;
        }
    }

    private void initUsersList() {
        usersList = new ArrayList<>();
        usersList.add(new OrderUser(sharedPreferencesManager.getUser().getFirstname() + " " + sharedPreferencesManager.getUser().getLastname(), sharedPreferencesManager.getUser().getPol(), cartManager.getList()));
        usersList.add(new OrderUser("мужчина " + sharedPreferencesManager.getUser().getLastname(), "Мужской", cartManager.getList()));
    }

    private void initBottomSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(OrderActivity.this);
        bottomSheetDialog.setContentView(R.layout.user_selector_bottom_sheet);

        sheet_back = bottomSheetDialog.findViewById(R.id.close_sheet);
        sheet_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });
        sheet_addUser = bottomSheetDialog.findViewById(R.id.add_user);
        sheet_usersView = bottomSheetDialog.findViewById(R.id.patients_view);
        sheet_confirm = bottomSheetDialog.findViewById(R.id.confirm_button);
        sheet_usersView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        BottomSheetUsersAdapter sheet_usersAdapter = new BottomSheetUsersAdapter(usersList, this);
        sheet_usersView.setAdapter(sheet_usersAdapter);

        sheet_usersAdapter.setOnUserSelectedListener(new BottomSheetUsersAdapter.UserSelectedListener() {
            @Override
            public void onUserSelected(OrderUser user) {
                sheet_confirm.setEnabled(true);
                sheet_confirm.setBackgroundResource(R.drawable.button_bg_available);
                selectedUser = user;
            }
        });

        sheet_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        sheet_confirm.setEnabled(false);
        sheet_confirm.setBackgroundResource(R.drawable.button_bg_unavailable);

        sheet_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUser(selectedUser);
                bottomSheetDialog.cancel();
            }
        });



    }

    public OrderUser getSelectedUser() {
        return selectedUser;
    }

    private void back() {
        this.finish();
    }


}