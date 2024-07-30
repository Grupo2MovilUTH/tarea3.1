package com.example.crudapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private static final String URL = "http://192.168.1.33/CRUD/CRUD/Getregister.php";
    private static final String TAG = "UserListActivity";
    public static final int REQUEST_CODE_EDIT_USER = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.recyclerView);
        Button btnLogout = findViewById(R.id.btnLogout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList); // 'this' es el contexto de la actividad
        recyclerView.setAdapter(userAdapter);

        fetchUsers();

        // Configurar el botón de cerrar sesión
        btnLogout.setOnClickListener(v -> {
            // Implementa la lógica de cierre de sesión aquí
            logout();
        });
    }

    private void fetchUsers() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Response: " + response.toString());
                        userList.clear(); // Limpia la lista antes de agregar nuevos datos
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject userObject = response.getJSONObject(i);
                                User user = new User();
                                user.setId(userObject.getInt("id"));
                                user.setNombres(userObject.getString("nombres"));
                                user.setApellidos(userObject.getString("apellidos"));
                                user.setPassword(userObject.getString("password"));
                                user.setToken(userObject.getString("token"));

                                userList.add(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (userAdapter != null) {
                            userAdapter.notifyDataSetChanged(); // Notificar cambios al adaptador
                        } else {
                            Log.e(TAG, "UserAdapter is null");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error al obtener los datos: " + error.getMessage());
                        Toast.makeText(UserListActivity.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
        if (userList.isEmpty()) {
            Log.d(TAG, "La lista de usuarios está vacía");
        } else {
            Log.d(TAG, "La lista de usuarios tiene datos");
        }
    }

    private void logout() {
        // Borrar el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.apply();



        // Redirigir al usuario a MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();  // Cerrar esta actividad
    }

}
