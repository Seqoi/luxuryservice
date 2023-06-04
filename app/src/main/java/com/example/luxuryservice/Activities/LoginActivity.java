package com.example.luxuryservice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.luxuryservice.App;
import com.example.luxuryservice.R;
import com.example.luxuryservice.Responses.EmailResponse;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton login;
    private EditText email;

    private CompositeDisposable disposable;

    private String emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        disposable = new CompositeDisposable();
        emailText = "";

        final App app = (App) getApplicationContext();

        login = findViewById(R.id.loginNext);
        email = findViewById(R.id.loginEmail);

        login.setEnabled(false);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailText = email.getText().toString();
                validateMail(emailText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<EmailResponse> call = app.getService().getApi().sendCode(emailText);
                login.setEnabled(false);
                login.setBackgroundDrawable(getDrawable(R.drawable.button_bg_unavailable));
                call.enqueue(new Callback<EmailResponse>() {
                    @Override
                    public void onResponse(Call<EmailResponse> call, Response<EmailResponse> response) {
                        EmailResponse emailResponse = response.body();
                        System.out.println(emailResponse.getMessage());
                        if (response.isSuccessful()) {
                            toCodeEntering();
                        }
                    }

                    @Override
                    public void onFailure(Call<EmailResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void validateMail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login.setEnabled(true);
            login.setBackgroundDrawable(getDrawable(R.drawable.button_bg_available));
        } else {
            login.setEnabled(false);
            login.setBackgroundDrawable(getDrawable(R.drawable.button_bg_unavailable));
        }
    }

    private void toCodeEntering() {
        Intent intent = new Intent(this, MailCodeActivity.class);
        intent.putExtra("email", emailText);
        startActivity(intent);
    }

}