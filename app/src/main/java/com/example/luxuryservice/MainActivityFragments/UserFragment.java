package com.example.luxuryservice.MainActivityFragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.luxuryservice.App;
import com.example.luxuryservice.R;
import com.example.luxuryservice.Responses.EmailResponse;
import com.example.luxuryservice.Responses.UpdateUser;
import com.example.luxuryservice.Responses.User;
import com.example.luxuryservice.SharedPreferencesManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private Spinner gender;
    private EditText name;
    private EditText surname;
    private EditText thirdName;
    private EditText textDate;
    private String selectedGender;
    private ImageView avatar;

    private ArrayAdapter<String> adapter;

    private TextWatcher textWatcher;

    private AppCompatButton createUser;

    private App app;
    private SharedPreferencesManager sharedPreferencesManager;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = (App) getActivity().getApplication();
        sharedPreferencesManager = app.getPreferencesManager();

        gender = requireView().findViewById(R.id.Gender);
        name = requireView().findViewById(R.id.textName);
        surname = requireView().findViewById(R.id.textSurname);
        thirdName = requireView().findViewById(R.id.textThirdName);
        textDate = requireView().findViewById(R.id.textBirth);

        avatar = requireView().findViewById(R.id.avatar);

        selectedGender = "";

        Calendar calendar = Calendar.getInstance();

        final int YEAR = calendar.get(Calendar.YEAR);
        final int MONTH = calendar.get(Calendar.MONTH);
        final int DAY = calendar.get(Calendar.DAY_OF_MONTH);

        createUser = requireView().findViewById(R.id.createUser);
        createUser.setEnabled(false);

        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
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
        getUser();

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

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Загрузить аватар"), 1);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri avatarUri = data.getData();
            postImage(avatarUri);
        }
    }

    private void postImage(Uri avatarUri) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), avatarUri)
                    .compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        byte[] imageBytes = outputStream.toByteArray();

        Call<EmailResponse> imagePost = app.getService().getApi().uploadAvatar("Bearer " + sharedPreferencesManager.getToken(), MultipartBody.Part.createFormData(
                "file", "name", RequestBody.create(MediaType.parse("image/*"), imageBytes)
        ));
        imagePost.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    updateImageDrawable(avatarUri);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void getUser() {
        if (sharedPreferencesManager.getUserStatus()) {
            name.setText(sharedPreferencesManager.getUser().getFirstname());
            surname.setText(sharedPreferencesManager.getUser().getMiddlename());
            thirdName.setText(sharedPreferencesManager.getUser().getLastname());
            textDate.setText(sharedPreferencesManager.getUser().getBirth());
            gender.setSelection(adapter.getPosition(sharedPreferencesManager.getUser().getPol()));
        }
        if (sharedPreferencesManager.getAvatarStatus()) {
            updateImageDrawable(sharedPreferencesManager.getAvatarUri());
        }
    }

    private void updateImageDrawable(Uri uri) {
        Glide.with(requireContext()).load(uri).into(avatar);
        avatar.setForeground(null);
        sharedPreferencesManager.setAvatarUri(uri);
    }

    private void setupSpinner() {
        String[] items = new String[]{
                "Пол",
                "Мужской",
                "Женский"
        };

        adapter = new ArrayAdapter(requireContext(), R.layout.spinner_item, items) {
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

    private void validateInput() {
        if (name.getText().toString().length() > 2 &&
                surname.getText().toString().length() > 2 &&
                thirdName.getText().toString().length() > 2 &&
                textDate.getText().toString().length() > 0 &&
                selectedGender != "Пол" &&
                selectedGender != "") {
            createUser.setEnabled(true);
            createUser.setBackgroundDrawable(requireContext().getDrawable(R.drawable.button_bg_available));
        } else {
            createUser.setEnabled(false);
            createUser.setBackgroundDrawable(requireContext().getDrawable(R.drawable.button_bg_unavailable));
        }
    }

    private void updateUser() {
        UpdateUser user = new UpdateUser(name.getText().toString(),
                thirdName.getText().toString(),
                surname.getText().toString(),
                textDate.getText().toString(),
                selectedGender);

        createUser.setEnabled(false);
        createUser.setBackgroundDrawable(requireContext().getDrawable(R.drawable.button_bg_unavailable));

        Call<User> call = app.getService().getApi().updateProfile(user, "Bearer " + sharedPreferencesManager.getToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    sharedPreferencesManager.saveUser(response.body());
                    Toast.makeText(requireContext(), "Успешно сохранено", Toast.LENGTH_SHORT).show();
                    createUser.setEnabled(true);
                    createUser.setBackgroundDrawable(requireContext().getDrawable(R.drawable.button_bg_available));
                } else {
                    Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    createUser.setEnabled(true);
                    createUser.setBackgroundDrawable(requireContext().getDrawable(R.drawable.button_bg_available));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}