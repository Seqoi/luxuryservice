package com.example.luxuryservice;

import com.example.luxuryservice.Responses.CodeResponse;
import com.example.luxuryservice.Responses.EmailResponse;
import com.example.luxuryservice.Responses.Sale;
import com.example.luxuryservice.Responses.Tests;
import com.example.luxuryservice.Responses.UpdateUser;
import com.example.luxuryservice.Responses.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface MedicApi {

    @POST("api/sendCode")
    Call<EmailResponse> sendCode(
            @Header("email") String email
    );

    @POST("api/signin")
    Call<CodeResponse> signIn(
            @Header("email") String email,
            @Header("code") String code
    );

    @POST("api/createProfile")
    Call<User> createProfile(
            @Body User user,
            @Header("Authorization") String auth_key
    );

    @GET("api/news")
    Call<List<Sale>> getNews(
    );

    @GET("api/catalog")
    Call<List<Tests>> getTests();

    @PUT("api/updateProfile")
    Call<User> updateProfile(
            @Body UpdateUser user,
            @Header("Authorization") String auth_key
    );

    @Multipart
    @POST("api/avatar")
    Call<EmailResponse> uploadAvatar(
            @Header("Authorization") String auth_key,
            @Part MultipartBody.Part file
    );

}
