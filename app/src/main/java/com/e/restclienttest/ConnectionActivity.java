package com.e.restclienttest;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

// при закрузке приложения запускается данный класс в котором идет загрузка доступных систем
public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        String urlConnection = "http://192.168.0.21:8080/rest/rest/wmap/connection";
        final RequestQueue connectionQueue = Volley.newRequestQueue(this);
        final Intent intentConnection = new Intent(ConnectionActivity.this, SystemsActivity.class);

        final JsonArrayRequest jsonArrayRequestConnection = new JsonArrayRequest(
                Request.Method.GET,
                urlConnection,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Mapa> sapDataList = (new Gson()).fromJson(response.toString(),
                                new TypeToken<ArrayList<Mapa>>() {
                                }.getType());
                        for (int i = 0; i < sapDataList.size(); i++) {
                            intentConnection.putExtra(sapDataList.get(i).getName(), sapDataList.get(i).getValues());
                        }
                        ConnectionActivity.this.startActivity(intentConnection);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response", error.toString());
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConnectionActivity.this);
                        builder.setMessage("Сервер не отвечает")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                }
        );
        connectionQueue.add(jsonArrayRequestConnection);

    }
}
