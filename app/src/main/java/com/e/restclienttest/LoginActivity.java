package com.e.restclienttest;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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

    // в случае если логин или пароль введены неверно, появляется сообщение об этом продолжительностью
    // 3 секунды
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

    @Override
    public void onBackPressed() {
        Intent intentSystems = new Intent(LoginActivity.this, SystemsActivity.class);
        LoginActivity.this.startActivity(intentSystems);
    }

    // добавление кнопки меню на activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // обработка команд кнопки меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_system) {
            MenuActions.systemButton(LoginActivity.this);
        }
        if (id == R.id.menu_settings) {
            MenuActions.settings(LoginActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    // входим в систему
    private void getAuthentification() {
        final EditText etUsername = findViewById(R.id.etUsername);
        final EditText etPassword = findViewById(R.id.etPassword);
        final EditText lang = findViewById(R.id.language);
        final Button bLogin = findViewById(R.id.bEnter);
        final RequestQueue loginQueue = Volley.newRequestQueue(this);
        final ProgressBar loginPB = findViewById(R.id.progressBarLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPB.setVisibility(View.VISIBLE);
                ClientActivity.username = etUsername.getText().toString().toUpperCase().trim();
                ClientActivity.password = etPassword.getText().toString().trim();
                ClientActivity.language = lang.getText().toString().toUpperCase().trim();
                final Intent intentLogin = new Intent(LoginActivity.this, ModulesActivity.class);
                String urlAuth = "https://" + ClientActivity.ipServer + "/rest/rest/wmap" + "/" + ClientActivity.selectedSystem + "/"
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
                                loginPB.setVisibility(View.INVISIBLE);
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
