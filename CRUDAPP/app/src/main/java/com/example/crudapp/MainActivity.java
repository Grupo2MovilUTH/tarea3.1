package com.example.crudapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText etNombres, etPassword;
    private Button btnLogin, btnRegister;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        etNombres = findViewById(R.id.etNombres);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        recyclerView = findViewById(R.id.recyclerView);

        // Aplicar animaciones
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        etNombres.startAnimation(fadeIn);
        etPassword.startAnimation(fadeIn);
        btnLogin.startAnimation(slideInRight);
        btnRegister.startAnimation(slideInRight);
        recyclerView.startAnimation(fadeIn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);

        // Verificar si el usuario ya está logueado
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        if (token != null) {
            // Si el token está presente, iniciar UserListActivity
            Intent intent = new Intent(MainActivity.this, UserListActivity.class);
            startActivity(intent);
            finish(); // Cerrar MainActivity
            return;
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        String nombres = etNombres.getText().toString();
        String password = etPassword.getText().toString();

        ApiService apiService = ApiClient.getApiClient().create(ApiService.class);
        User user = new User();
        user.setNombres(nombres);
        user.setPassword(password);

        Call<LoginResponse> call = apiService.loginUser(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null && response.body().getStatus().equals("success")) {
                    Toast.makeText(MainActivity.this, "Ingreso exitoso", Toast.LENGTH_SHORT).show();

                    // Almacenar el token en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();

                    // Iniciar UserListActivity
                    Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                    startActivity(intent);
                    finish(); // Cerrar MainActivity
                } else {
                    Toast.makeText(MainActivity.this, "Ingreso Fallido: " + (response.body() != null ? response.body().getMessage() : "Error desconocido"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOGIN_ERROR", t.getMessage());
                Toast.makeText(MainActivity.this, "Login error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Realiza tareas de limpieza aquí
        Log.d("MainActivity", "onStop called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Realiza tareas de limpieza aquí
        Log.d("MainActivity", "onPause called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Realiza tareas de limpieza aquí
        Log.d("MainActivity", "onDestroy called");
    }
}
