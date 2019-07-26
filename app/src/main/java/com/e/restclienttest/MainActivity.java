package com.e.restclienttest;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


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
import java.util.LinkedHashMap;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {

    // список запросов
    private LinkedHashMap<String, DataSet> dataSetList = new LinkedHashMap<>();


    // параметры для запроса в SAP
    String table; // = "T001";
    String fieldsQuan; // = "14";
    String language;
    String where; // "BUKRS >= '5555' AND BUKRS <= 'CZ01'";
    String order; // = " ";
    String group; // = " ";
    String fieldNames; //  = " ";
    String url;

    // параметры клиента
    String systemAddress;
    String login;
    String password;
    String number;
    String ip;


    //сюда будет приходить Sap ответ с наполненными данными
    ArrayList<Mapa> sapDataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // передаем введенные данные
        Intent intentMain = getIntent();

        systemAddress = intentMain.getStringExtra("systemAddress");
        login = intentMain.getStringExtra("userName");
        password = intentMain.getStringExtra("password");
        number = intentMain.getStringExtra("clientNumber");
        ip = intentMain.getStringExtra("ip");
        table = intentMain.getStringExtra("table");
        fieldsQuan = intentMain.getStringExtra("fieldsQuan");
        language = intentMain.getStringExtra("language");
        if(!intentMain.getStringExtra("where").equals(" ")){
            where = intentMain.getStringExtra("where").replaceAll(" ", "~~&");
        } else where = intentMain.getStringExtra("where");
        if(!intentMain.getStringExtra("order").equals(" ")){
            order = intentMain.getStringExtra("order").replaceAll(" ", "~~&");
        }else order = intentMain.getStringExtra("order");
        if(!intentMain.getStringExtra("group").equals(" ")){
            group = intentMain.getStringExtra("group").replaceAll(" ", "~~&");
        } else group = intentMain.getStringExtra("group");
        if(!intentMain.getStringExtra("fieldsNames").equals(" ")){
            fieldNames = intentMain.getStringExtra("fieldsNames").replaceAll(" ", "~~&");
        } else fieldNames = intentMain.getStringExtra("fieldsNames");
        createSys();

    }

    // кидает запрос серверу и получает ответ заполненный метод который заполняется данными из ответа
    public void getConnection(final String systemAddress, final String login, final String password, final String number,
                              final String table, final String fieldsQuan, final String language, final String where,
                              final String order, final String group, final String fieldNames) {


        // составляем url с параметрами идентификации(применим после настройки сервера)
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http://"+ip+"/rest/rest/wmap" + "/").append(systemAddress).append("/")
                .append(login).append("/").append(password).append("/").append(number).append("/").append(table);

        if (!fieldsQuan.equals(" ")) {
            urlSB.append("/").append(fieldsQuan);
        }
        if (!language.equals(" ")) {
            urlSB.append("/").append(language);
        }
        if (!where.equals(" ")) {
            urlSB.append("/").append(where);
        }
        if (!order.equals(" ")) {
            urlSB.append("/").append(order);
        }
        if (!group.equals(" ")) {
            urlSB.append("/").append(group);
        }
        if (!fieldNames.equals(" ")) {
            urlSB.append("/").append(fieldNames);
        }


        // получаем готвый url с внесенными параметрами
        url = urlSB.toString();

        // GET запрос к серверу
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        LinkedHashMap<String, LinkedList<String>> tempMap = new LinkedHashMap<>();
                        sapDataList = (new Gson()).fromJson(response.toString(),
                                new TypeToken<ArrayList<Mapa>>() {
                                }.getType());
                        for (int i = 0; i < sapDataList.size(); i++) {
                            tempMap.put(sapDataList.get(i).getName(), sapDataList.get(i).getValues());
                        }

                        visualisation(table, fieldsQuan, language, where, order, group, fieldNames, tempMap);
//                        dataSetList.get(table + fieldsQuan + language + where + order + group + fieldNames).keyVisualisation();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response", error.toString());
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }


    // будет создавать экземпляр класса dataSet и складываться в map где ключом будет идентификатор клиента
    public void createDataSet(String urlClient, String login, String password, String number, String table,
                              String fieldsQuan, String language, String where, String order,
                              String group, String fieldNames) {


        DataSet dataSet = new DataSet();
        dataSetList.put(table + fieldsQuan + language + where + order + group + fieldNames, dataSet);
        getConnection(urlClient, login, password, number, table, fieldsQuan, language, where, order,
                group, fieldNames);
    }


    // создает клиента
    public void createSys() {
        createDataSet(systemAddress, login, password, number, table, fieldsQuan, language, where, order, group, fieldNames);
    }


    // визуализирует dataSet
    public void visualisation(String table,
                              String fieldsQuan, String language, String where, String order,
                              String group, String fieldNames, LinkedHashMap<String, LinkedList<String>> tempMap) {
        LinearLayout linear = findViewById(R.id.linear);
        TableMainLayout tab = new TableMainLayout(this, tempMap);
        setContentView(tab);
//        for (String key : tempMap.keySet()) {
//            //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
//            View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);
//            Button deleteField = view.findViewById(R.id.button2);
//            EditText text = view.findViewById(R.id.editText);
//            String val = String.valueOf(tempMap.get(key));
//            text.setText((val));
//
//            //добавляем все что создаем в массив
//            linear.addView(view);
//        }
//        dataSetList.get(table + fieldsQuan + language + where + order + group + fieldNames).setLinear(linear);
    }


}
