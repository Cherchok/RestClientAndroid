package com.e.restclienttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;


public class DataActivity extends AppCompatActivity {

    // параметры для запроса в SAP
    String table;
    String fieldsQuan;
    String language;
    String where;
    String order;
    String group;
    String fieldNames;


    // метод вызывется при создании(вызове) данного Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startRequest();
        DataActivity.this.setTitle("Таблица : " + table);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // кидает запрос серверу и получает ответ заполненный метод который заполняется данными из ответа
    public void startRequest() {
        setParams();
        setRequestUrl();
        setDataSetID();
        if (!ClientActivity.dataSetList.containsKey(ClientActivity.dataSetID)) {
            // GET запрос к серверу
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    ClientActivity.requestUrl,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            LinkedHashMap<String, LinkedList<String>> tempMap = new LinkedHashMap<>();

                            ArrayList<Mapa> sapDataList = ClientActivity.deserialization(response);
                            for (int i = 0; i < sapDataList.size(); i++) {
                                tempMap.put(sapDataList.get(i).getName(), sapDataList.get(i).getValues());
                                if (sapDataList.get(i).getName().equals("clientNumber")) {
                                    ClientActivity.clientID = sapDataList.get(i).getValues().get(0);
                                }
                            }
                            ClientActivity.putDataSet(tempMap);
                            showTableThread();
                            // TODO
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
        } else {
            showTable();
        }
    }

    // вывод таблицы на экран
    private void showTable() {
        TableMainLayout tab = new TableMainLayout(DataActivity.this,
                ClientActivity.dataSetList.get(ClientActivity.dataSetID).getMap());
        setContentView(tab);
    }

    private void showTableThread() {
        new Thread() {
            public void run() {

                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showTable();
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    // метод передачи введенных параметров выводимой таблицы
    private void setParams() {

        // передаем введенные данные
        Intent intentMain = getIntent();
        if (!intentMain.getStringExtra("table").equals(" ")) {
            table = intentMain.getStringExtra("table");
        } else table = intentMain.getStringExtra("table").replaceAll(" ", "~~~");

        if (!intentMain.getStringExtra("fieldsQuan").equals(" ")) {
            fieldsQuan = intentMain.getStringExtra("fieldsQuan");
        } else fieldsQuan = intentMain.getStringExtra("fieldsQuan").replaceAll(" ", "~~~");
        language = ClientActivity.language;
        if (!intentMain.getStringExtra("where").equals(" ")) {
            where = intentMain.getStringExtra("where").replaceAll(" ", "~~&");
        } else where = intentMain.getStringExtra("where").replaceAll(" ", "~~~");

        if (!intentMain.getStringExtra("order").equals(" ")) {
            order = intentMain.getStringExtra("order").replaceAll(" ", "~~&");
        } else order = intentMain.getStringExtra("order").replaceAll(" ", "~~~");

        if (!intentMain.getStringExtra("group").equals(" ")) {
            group = intentMain.getStringExtra("group").replaceAll(" ", "~~&");
        } else group = intentMain.getStringExtra("group").replaceAll(" ", "~~~");

        if (!intentMain.getStringExtra("fieldsNames").equals(" ")) {
            fieldNames = intentMain.getStringExtra("fieldsNames").replaceAll(" ", "~~&");
        } else fieldNames = intentMain.getStringExtra("fieldsNames").replaceAll(" ", "~~~");

    }

    // составление url запроса
    private void setRequestUrl() {
        // получаем готвый requestUrl с внесенными параметрами
        ClientActivity.requestUrl = "https://" + ClientActivity.ipServer + "/RestTest/rest/wmap" + "/" +
                ClientActivity.selectedSystem + "/" + ClientActivity.username + "/" +
                ClientActivity.password + "/" + ClientActivity.clientID + "/" + table + "/" +
                fieldsQuan + "/" + language + "/" + where + "/" + order + "/" + group + "/" +
                fieldNames;
    }

    // составление ключа запроса
    private void setDataSetID() {
        // формирование ключа запрса
        ClientActivity.dataSetID = table + fieldsQuan + language + where + order + group + fieldNames;
    }

//    // визуализирует dataSet c динамически изменяемыми полями
//    public void visualisation(String table,
//                              String fieldsQuan, String language, String where, String order,
//                              String group, String fieldNames, LinkedHashMap<String, LinkedList<String>> tempMap) {
//        LinearLayout linear = findViewById(R.id.linear);
//        // динамический вывод строк с доп кнпками и т.д. в зависимсоти от условий
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
//    }
}
