package com.e.restclienttest;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import org.json.JSONArray;

import java.util.ArrayList;

// класс проверки логина и пароля перед входом
public class LoginActivity extends AppCompatActivity {

    CountDownTimer timer = new CountDownTimer(3000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            try {
                LoginActivity.this.setTitle("Авторизация");
            } catch (Exception e) {
                Log.e("Error", "Error: " + e.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getAuthentification();
    }

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
                 String urlAuth = "http://" + ClientActivity.ipServer + "/rest/rest/wmap" + "/" + ClientActivity.selectedSystem + "/"
                        + ClientActivity.username + "/" + ClientActivity.password + "/" + ClientActivity.language;

                // GET запрос к серверу для авторизации
                final JsonArrayRequest jsonArrayRequestLogin = new JsonArrayRequest(
                        Request.Method.GET,
                        urlAuth,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ArrayList<Mapa> sapDataList = ClientActivity.deserialization(response);
                                ClientActivity.readSyst(sapDataList);
                                LoginActivity.this.startActivity(intentLogin);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                LoginActivity.this.setTitle("Неверный логин или пароль!!!");
                                timer.start();
                            }
                        }
                );
                loginQueue.add(jsonArrayRequestLogin);
            }
        });
    }


}
