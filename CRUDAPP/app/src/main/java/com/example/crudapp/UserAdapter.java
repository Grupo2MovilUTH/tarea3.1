package com.example.crudapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private Context context;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nombres.setText(user.getNombres());
        holder.apellidos.setText(user.getApellidos());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("userId", user.getId());
            intent.putExtra("nombres", user.getNombres());
            intent.putExtra("apellidos", user.getApellidos());
            ((UserListActivity) context).startActivityForResult(intent, UserListActivity.REQUEST_CODE_EDIT_USER);
        });

        holder.btnDelete.setOnClickListener(v -> {
            deleteUser(user.getId(), position);
        });

        Log.d("UserAdapter", "User: " + user.getNombres() + " " + user.getApellidos());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void deleteUser(int userId, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.33/CRUD/CRUD/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        UserDeleteRequest request = new UserDeleteRequest(userId);

        Call<ApiResponse> call = apiService.deleteUser(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                    userList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Toast.makeText(context, "No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nombres, apellidos;
        Button btnEdit, btnDelete;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nombres = itemView.findViewById(R.id.nombres);
            apellidos = itemView.findViewById(R.id.apellidos);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}