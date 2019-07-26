package com.e.restclienttest;

import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.LinkedHashMap;

public class ParamsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params);

        final EditText etTable = findViewById(R.id.etTable);
        final EditText etFieldsQuan = findViewById(R.id.etFields_Quantity);
        final EditText etLanguage = findViewById(R.id.etlng);
        final EditText etWhere = findViewById(R.id.where);
        final EditText etOrder = findViewById(R.id.order);
        final EditText etGroup = findViewById(R.id.group);
        final EditText etFieldsNames = findViewById(R.id.etFields_Names);
        final Button bEnter = findViewById(R.id.bEnter);


        bEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // заполняем переменные занесенные юзером
                Intent intentModules = getIntent();
                final String systemAddress = intentModules.getStringExtra("systemAddress");
                final String username = intentModules.getStringExtra("userName");
                final String password = intentModules.getStringExtra("password");
                final String table = etTable.getText().toString().toUpperCase().trim();
                final String fieldsQuan = etFieldsQuan.getText().toString().trim();
                final String language;
                if (etLanguage.getText().toString().equals("")) {
                    language = intentModules.getStringExtra("language");
                } else {
                    language = etLanguage.getText().toString().toUpperCase().trim();
                }
                final String where = etWhere.getText().toString().toUpperCase().trim();
                final String order = etOrder.getText().toString().toUpperCase().trim();
                final String group = etGroup.getText().toString().toUpperCase().trim();
                final String fieldsNames = etFieldsNames.getText().toString().toUpperCase().trim();
                final String clientNumber = intentModules.getStringExtra("clientNumber");
                final String ip = intentModules.getStringExtra("ip");

                // создал проверочную мапу для проверки введенных парметров
                LinkedHashMap<String, String> chekMap = new LinkedHashMap<>();
                chekMap.put("systemAddress", systemAddress );
                chekMap.put("userName", username);
                chekMap.put("password", password);
                chekMap.put("table", table);
                chekMap.put("fieldsQuan", fieldsQuan);
                chekMap.put("language", language);
                chekMap.put("where", where);
                chekMap.put("order", order);
                chekMap.put("group", group);
                chekMap.put("fieldsNames", fieldsNames);
                chekMap.put("clientNumber", clientNumber);
                chekMap.put("ip", ip);

                // заполняем параметрами мапу, которая передаст их основному классу исполнения
                Intent intentParams = new Intent(ParamsActivity.this, MainActivity.class);
                for (String name : chekMap.keySet()) {
                    if (!chekMap.get(name).equals("")) {
                        intentParams.putExtra(name, chekMap.get(name));
                    } else intentParams.putExtra(name, " ");
                }
                ParamsActivity.this.startActivity(intentParams);
            }
        });
    }
}
