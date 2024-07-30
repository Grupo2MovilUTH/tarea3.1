package com.example.crudapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etNombres, etApellidos, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String nombres = etNombres.getText().toString();
        String apellidos = etApellidos.getText().toString();
        String password = etPassword.getText().toString();

        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        User user = new User();
        user.setNombres(nombres);
        user.setApellidos(apellidos);
        user.setPassword(password);

        Call<RegisterResponse> call = apiService.registerUser(user);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.body() != null && response.body().getStatus().equals("success")) {
                    Toast.makeText(RegisterActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registro Fallido: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("REGISTER_ERROR", t.getMessage());
                Toast.makeText(RegisterActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
