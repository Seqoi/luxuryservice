package com.example.luxuryservice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.luxuryservice.App;
import com.example.luxuryservice.R;
import com.example.luxuryservice.Responses.CodeResponse;
import com.example.luxuryservice.SharedPreferencesManager;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MailCodeActivity extends AppCompatActivity {

    private Intent intent;

    private EditText emailCode;
    private String email;
    private String code;

    private TextView timerText;

    private App app;
    private SharedPreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_code);

        intent = getIntent();
        email = intent.getStringExtra("email");
        code = "";

        emailCode = findViewById(R.id.emailCode);
        timerText = findViewById(R.id.timerText);

        app = (App) getApplication();
        preferencesManager = app.getPreferencesManager();

        emailCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code = emailCode.getText().toString();
                validateCode(code);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        startTimer();

    }

    private void validateCode(String code) {
        if (code.length() == 4) {
            sendCode(code, email);
        }
    }

    private void startTimer() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText("Отправить код повторно можно\nбудет через " + millisUntilFinished / 1000 + " секунд");
            }

            @Override
            public void onFinish() {
                requestCode();
                startTimer();
            }
        }.start();
    }

    private void requestCode() {
        app.getService().getApi().sendCode(email);
    }

    private void sendCode(String code, String email) {
        Call<CodeResponse> call = app.getService().getApi().signIn(email, code);
        call.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if (response.code() == 200) {
                    createAccount(Objects.requireNonNull(response.body().getToken()));
                } else if (response.code() == 422) {
                    emailCode.setText("");
                    Toast.makeText(getApplicationContext(), "Неверный код", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {

            }
        });
    }

    private void createAccount(String token) {
        preferencesManager.saveToken(token);
        intent = new Intent(this, PasswordCreationActivity.class);
        startActivity(intent);
    }
}