package com.example.luxuryservice;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MedicService {

    private final MedicApi api;

    public MedicService() {
        Retrofit retrofit = createRetrofit();
        api = retrofit.create(MedicApi.class);
    }

    public MedicApi getApi() {
        return api;
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://medic.madskill.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHTTPClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient createOkHTTPClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                final Request orig = chain.request();
                final HttpUrl origHttpUrl = orig.url();
                final HttpUrl url = origHttpUrl.newBuilder().build();
                final Request.Builder requestBuilder = orig.newBuilder()
                        .url(url);
                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        return httpClient.build();
    }
}
