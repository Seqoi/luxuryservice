package com.example.luxuryservice.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.luxuryservice.App;
import com.example.luxuryservice.MedicService;
import com.example.luxuryservice.R;
import com.example.luxuryservice.Responses.User;
import com.example.luxuryservice.SharedPreferencesManager;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCardActivity extends AppCompatActivity {

    private Spinner gender;
    private EditText name;
    private EditText surname;
    private EditText thirdName;
    private EditText textDate;
    private String selectedGender;

    private TextWatcher textWatcher;

    private AppCompatButton createUser;
    private TextView skip;

    private App app;
    private MedicService medicService;
    private SharedPreferencesManager sharedPreferencesManager;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (App) getApplication();
        sharedPreferencesManager = app.getPreferencesManager();

        if (sharedPreferencesManager.getUserStatus()) {
            toMain();
        }

        setContentView(R.layout.activity_user_card);

        medicService = app.getService();

        gender = findViewById(R.id.Gender);
        name = findViewById(R.id.textName);
        surname = findViewById(R.id.textSurname);
        thirdName = findViewById(R.id.textThirdName);
        textDate = findViewById(R.id.textBirth);

        skip = findViewById(R.id.skip);

        selectedGender = "";

        Calendar calendar = Calendar.getInstance();

        final int YEAR = calendar.get(Calendar.YEAR);
        final int MONTH = calendar.get(Calendar.MONTH);
        final int DAY = calendar.get(Calendar.DAY_OF_MONTH);

        createUser = findViewById(R.id.createUser);
        createUser.setEnabled(false);

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UserCardActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "." + month + "." + year;
                        textDate.setText(date);
                    }
                }, YEAR, MONTH, DAY);
                datePickerDialog.show();
            }
        });

        setupSpinner();

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateInput();
            }
        };

        name.addTextChangedListener(textWatcher);
        surname.addTextChangedListener(textWatcher);
        thirdName.addTextChangedListener(textWatcher);
        textDate.addTextChangedListener(textWatcher);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMain();
            }
        });

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createResponse();
            }
        });

    }

    private void createResponse() {
        user = new User(0, name.getText().toString(),
                thirdName.getText().toString(),
                surname.getText().toString(),
                textDate.getText().toString(),
                selectedGender,
                null);

        createUser.setEnabled(false);
        createUser.setBackgroundDrawable(getDrawable(R.drawable.button_bg_unavailable));

        Call<User> call = medicService.getApi().createProfile(user, "Bearer " + sharedPreferencesManager.getToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    sharedPreferencesManager.saveUser(response.body());
                    toMain();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void validateInput() {
        if (name.getText().toString().length() > 2 &&
                surname.getText().toString().length() > 2 &&
                thirdName.getText().toString().length() > 2 &&
                textDate.getText().toString().length() > 0 &&
                selectedGender != "Пол" &&
                selectedGender != "") {
            createUser.setEnabled(true);
            createUser.setBackgroundDrawable(getDrawable(R.drawable.button_bg_available));
        } else {
            createUser.setEnabled(false);
            createUser.setBackgroundDrawable(getDrawable(R.drawable.button_bg_unavailable));
        }
    }

    private void setupSpinner() {
        String[] items = new String[]{
                "Пол",
                "Мужской",
                "Женский"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_item, items) {
            @Override
            public boolean isEnabled(int position) {
                return position !=0;
            }
        };

        gender.setAdapter(adapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = gender.getSelectedItem().toString();
                validateInput();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                validateInput();
            }
        });

    }

    private void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}