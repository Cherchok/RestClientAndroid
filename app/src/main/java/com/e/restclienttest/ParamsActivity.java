package com.e.restclienttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.LinkedHashMap;

public class ParamsActivity extends AppCompatActivity {
    // параметры поиска таблиц
    EditText etTable;
    EditText etFieldsQuan;
    EditText etLanguage;
    EditText etWhere;
    EditText etOrder;
    EditText etGroup;
    EditText etFieldsNames;
    Button bEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params);
        startResponse();
    }

    @Override
    public void onBackPressed() {
       Intent intentModules = new Intent(ParamsActivity.this, ModulesActivity.class);
       ParamsActivity.this.startActivity(intentModules);
    }

    // метод получения запроса
    private void startResponse() {
        etTable = findViewById(R.id.etTable);
        etFieldsQuan = findViewById(R.id.etFields_Quantity);
        etLanguage = findViewById(R.id.etlng);
        etWhere = findViewById(R.id.where);
        etOrder = findViewById(R.id.order);
        etGroup = findViewById(R.id.group);
        etFieldsNames = findViewById(R.id.etFields_Names);
        bEnter = findViewById(R.id.bEnter);

        bEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillParams();
            }
        });
    }

    // заполнение параметров
    private void fillParams() {
        // заполняем переменные занесенные юзером
        final String table = etTable.getText().toString().toUpperCase().trim();
        final String fieldsQuan = etFieldsQuan.getText().toString().trim();
        if (!etLanguage.getText().toString().equals("")) {
            ClientActivity.language = etLanguage.getText().toString().toUpperCase().trim();
            if (ClientActivity.language.equals("")) {
                ClientActivity.language = " ";
            }
        }

        final String where = etWhere.getText().toString().toUpperCase().trim();
        final String order = etOrder.getText().toString().toUpperCase().trim();
        final String group = etGroup.getText().toString().toUpperCase().trim();
        final String fieldsNames = etFieldsNames.getText().toString().toUpperCase().trim();


        // создал проверочную мапу для проверки введенных парметров
        LinkedHashMap<String, String> chekMap = new LinkedHashMap<>();
        chekMap.put("table", table);
        chekMap.put("fieldsQuan", fieldsQuan);
        chekMap.put("where", where);
        chekMap.put("order", order);
        chekMap.put("group", group);
        chekMap.put("fieldsNames", fieldsNames);


        // заполняем параметрами мапу, которая передаст их основному классу исполнения
        Intent intentParams = new Intent(ParamsActivity.this, DataActivity.class);
        for (String name : chekMap.keySet()) {
            if (!chekMap.get(name).equals("")) {
                intentParams.putExtra(name, chekMap.get(name));
            } else intentParams.putExtra(name, " ");
        }
        ParamsActivity.this.startActivity(intentParams);
    }
}
