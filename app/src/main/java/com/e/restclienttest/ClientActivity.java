package com.e.restclienttest;

import android.app.AlertDialog;

import android.content.DialogInterface;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

// при загрузке приложения запускается данный класс в котором идет загрузка доступных систем
public class ClientActivity extends AppCompatActivity {
    // IP сервера
    static String ipServer;
    //адресс SAP системы
    static String selectedSystem;
    // клиентский ID полученный от сервера
    static String clientID;
    // имя пользователя
    static String username;
    // пароль
    static String password;
    // язык выходных данных
    static String language;
    // список модулей
    static LinkedList<String> modulesList = new LinkedList<>();
    // модуль с которым производится работа
    static String selectedModule;
    // id модуля с которым производится работа
    static String selectedModuleID;
    // список id модулей
    static LinkedList<String> moduleIDlist = new LinkedList<>();
    // список доступных систем на сервере
    static LinkedHashMap<String, LinkedList> systemsList = new LinkedHashMap<>();
    // список запросов к серверу с данными
    static LinkedHashMap<String, DataSet> dataSetList = new LinkedHashMap<>();
    // id запроса к серверу с данными
    static String dataSetID;
    // requestUrl get-запроса в SAP через сервер
    static String requestUrl;
    // QR текст
    static String qrText;
    // QR url
    static String qrUrl;
    // хранилище для ip сервера
    MyPropertiesHolder propertiesHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        getConnection();
    }

    // удаление впереди идущих 0
    public static String replaceLeftLeadingZeros(String expression) {
        return expression.replaceFirst("^0+(?!$)", "");
    }

    // дессерилазция json response
    public static ArrayList<Mapa> deserialization(JSONArray response) {
        return (new Gson()).fromJson(response.toString(),
                new TypeToken<ArrayList<Mapa>>() {
                }.getType());
    }

    //считываем данные из syst
    public static void readSyst(ArrayList<Mapa> sapDataList) {
        //считываем список доступных приложений и номер клиента, передаваемый от сервера
        for (int i = 0; i < sapDataList.size(); i++) {
            if (sapDataList.get(i).getName().equals("REPI2")) {
                ClientActivity.modulesList = sapDataList.get(i).getValues();
                ClientActivity.modulesList = sapDataList.get(i).getValues();
            }
            if (sapDataList.get(i).getName().equals("clientNumber")) {
                ClientActivity.clientID = sapDataList.get(i).getValues().get(0);

            }
            if (sapDataList.get(i).getName().equals("CPROG")) {
                ClientActivity.moduleIDlist = sapDataList.get(i).getValues();
            }
            //TODO
        }
    }

    // вводим IP сервера через клиентский интерфейс
    private void setIp(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClientActivity.this);
        if (message.equals("Введите IP сервера")) {
            builder.setMessage(message)
                    .setNeutralButton("Ввести IP", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentSetIP = new Intent(ClientActivity.this, SetIP.class);
                            ClientActivity.this.startActivity(intentSetIP);
                        }
                    })
                    .create()
                    .show();

        } else {
            builder.setMessage(message)
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentSetIP = new Intent(ClientActivity.this, ClientActivity.class);
                            ClientActivity.this.startActivity(intentSetIP);
                        }
                    })
                    .setNeutralButton("Ввести IP", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentSetIP = new Intent(ClientActivity.this, SetIP.class);
                            ClientActivity.this.startActivity(intentSetIP);
                        }
                    })
                    .create()
                    .show();
        }
    }

    // подключаемся к серверу и получем список доступных систем
    private void getConnection() {
        try {
            propertiesHolder = new MyPropertiesHolder(this, "test.properties",
                    MyPropertiesHolder.MODE_UPDATE);


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (propertiesHolder != null) {
                ipServer = propertiesHolder.getProperty();
                try {
                    propertiesHolder.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                setIp("Введите IP сервера");
            }
        } catch (NullPointerException e) {
            ipServer = " ";
        }

        String urlConnection = "http://" + ipServer + "/rest/rest/wmap/connection";
        final RequestQueue connectionQueue = Volley.newRequestQueue(this);
        final Intent intentConnection = new Intent(ClientActivity.this, SystemsActivity.class);
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
                            systemsList.put(sapDataList.get(i).getName(), sapDataList.get(i).getValues());
                        }
                        ClientActivity.this.startActivity(intentConnection);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ClientActivity response", error.toString());
                        setIp("Сервер не отвечает");
                    }
                }
        );
        connectionQueue.add(jsonArrayRequestConnection);
    }

    // получем id выбранного модуля
    public static String getSelectedModuleID() {
        String selectedApp = "";
        int index = 999;
        for (int i = 0; i < ClientActivity.modulesList.size(); i++) {
            if (ClientActivity.modulesList.get(i).contains(ClientActivity.selectedModule)
                    && !ClientActivity.selectedModule.equals("")) {
                index = i;
            }
        }
        if (index != 999) {
            selectedApp = ClientActivity.moduleIDlist.get(index).trim();
        }
        return selectedApp;
    }

    // добавление данных в список запросов
    public static void putDataSet(LinkedHashMap<String, LinkedList<String>> map) {
        DataSet dataSet = new DataSet();
        dataSet.setMap(map);
        ClientActivity.dataSetList.put(ClientActivity.dataSetID, dataSet);
    }
}
