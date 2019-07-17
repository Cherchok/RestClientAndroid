package com.e.restclienttest;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SystemsActivity extends AppCompatActivity {

    // список имен доступных систем
    String[] systemsNames;

    // список всех систем
    LinkedHashMap<String, ArrayList<String>> systlist;

    // текст для выбора модуля
    TextView selection;

    // заполняем список модулей имеющимися на сервере
    public String[] getAllSystemsNames() {
        Intent intentConnection = getIntent();
        String[] systems = new String[intentConnection.getExtras().keySet().size() + 1];
        systems[0] = "_________________";
        int id = 1;
        for (String name : intentConnection.getExtras().keySet()) {
            systems[id] = name;
            id++;
        }
        return systems;
    }

    // получаем мапу всех систем для доступа к ним. Доступ осуществляется по имени путем выбора
    // имени в селекционном меню
    public LinkedHashMap<String, ArrayList<String>> getAllSystems() {
        LinkedHashMap<String, ArrayList<String>> systList = new LinkedHashMap<>();
        Intent intentConnection = getIntent();
        for (String name : intentConnection.getExtras().keySet()) {
            systList.put(name, intentConnection.getStringArrayListExtra(name));
        }
        return systList;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systems);
        Spinner systemSelector = findViewById(R.id.systems);
        selection = findViewById(R.id.selection_sys);
        systemsNames = getAllSystemsNames();
        systlist = getAllSystems();
        selection.setText("System: ");

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, systemsNames);

        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Применяем адаптер к элементу spinner
        systemSelector.setAdapter(adapter);

        AdapterView.OnItemSelectedListener systemSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранную систему
                String systName = (String) parent.getItemAtPosition(position);

                if ("_________________".equals(systName)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SystemsActivity.this);
                    builder.setMessage("Выберите систему")
                            .setNeutralButton("OK", null)
                            .create()
                            .show();
                } else {
                    final Intent intentForLogin = new Intent(SystemsActivity.this, LoginActivity.class);
                    intentForLogin.putExtra(systName, systlist.get(systName));
                    SystemsActivity.this.startActivity(intentForLogin);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        systemSelector.setOnItemSelectedListener(systemSelectedListener);


    }
}
