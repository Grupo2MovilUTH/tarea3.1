package com.example.crudapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditUserActivity extends AppCompatActivity {
    private EditText editTextNombres, editTextApellidos;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        editTextNombres = findViewById(R.id.editTextNombres);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        btnSave = findViewById(R.id.btnSave);

        // Obtener datos del Intent
        int userId = getIntent().getIntExtra("userId", -1);
        String nombres = getIntent().getStringExtra("nombres");
        String apellidos = getIntent().getStringExtra("apellidos");

        // Llenar campos con datos actuales
        editTextNombres.setText(nombres);
        editTextApellidos.setText(apellidos);

        btnSave.setOnClickListener(v -> {
            // Lógica para guardar los cambios
            String nuevosNombres = editTextNombres.getText().toString();
            String nuevosApellidos = editTextApellidos.getText().toString();

            // Ejecutar la tarea asíncrona para actualizar el usuario en el servidor
            new UpdateUserTask(userId, nuevosNombres, nuevosApellidos).execute("http://192.168.1.33/CRUD/CRUD/UpdateUsers.php");
        });
    }

    private class UpdateUserTask extends AsyncTask<String, Void, String> {
        private int userId;
        private String nuevosNombres;
        private String nuevosApellidos;

        public UpdateUserTask(int userId, String nuevosNombres, String nuevosApellidos) {
            this.userId = userId;
            this.nuevosNombres = nuevosNombres;
            this.nuevosApellidos = nuevosApellidos;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // Crear el JSON
                JSONObject json = new JSONObject();
                json.put("id", userId);
                json.put("nombres", nuevosNombres);
                json.put("apellidos", nuevosApellidos);
                json.put("password", ""); // Agregar lógica para la contraseña si es necesario
                json.put("token", ""); // Agregar lógica para el token si es necesario

                // Enviar el JSON
                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes("UTF-8"));
                os.close();

                // Leer la respuesta del servidor
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return "Usuario actualizado";
                } else {
                    return "No se puede actualizar el usuario";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(EditUserActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }
}
