package com.e.restclienttest;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;


// класс проверки логина и пароля перед входом
public class LoginActivity extends AppCompatActivity {
    static String urlAuth;

    // входим в систему
    private void getAuthentification() {
        final EditText etUsername = findViewById(R.id.etUsername);
        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText lang = findViewById(R.id.language);
        final Button bLogin = findViewById(R.id.bEnter);
        final RequestQueue loginQueue = Volley.newRequestQueue(this);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientActivity.username = etUsername.getText().toString().toUpperCase().trim();
                ClientActivity.password = etPassword.getText().toString().trim();
                ClientActivity.language = lang.getText().toString().toUpperCase().trim();

                final Intent intentLogin = new Intent(LoginActivity.this, ModulesActivity.class);
                urlAuth = "http://" + ClientActivity.ipServer + "/rest/rest/wmap" + "/" + ClientActivity.selectedSystem + "/"
                        + ClientActivity.username + "/" + ClientActivity.password;

                // GET запрос к серверу для авторизации
                final JsonArrayRequest jsonArrayRequestLogin = new JsonArrayRequest(
                        Request.Method.GET,
                        urlAuth,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<Mapa> sapDataList = (new Gson()).fromJson(response.toString(),
                                        new TypeToken<ArrayList<Mapa>>() {
                                        }.getType());
                                for (int i = 0; i < sapDataList.size(); i++) {
                                    if (sapDataList.get(i).getName().equals("REPI2")) {
                                        ClientActivity.modulesList = sapDataList.get(i).getValues();
                                    }
                                    if (sapDataList.get(i).getName().equals("clientNumber")) {
                                        ClientActivity.clientID = sapDataList.get(i).getValues().get(0);
                                    }
                                }
                                LoginActivity.this.startActivity(intentLogin);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Rest response", error.toString());
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Неверный логин или пароль пользователя")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        }
                );
                loginQueue.add(jsonArrayRequestLogin);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getAuthentification();
    }
}
