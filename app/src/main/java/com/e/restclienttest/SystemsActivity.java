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

public class SystemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systems);
        SelectSystem();
    }

    // получение списка доступных имен систем SAP
    public String[] getAllSystemsNames() {
        String[] systemsNames = new String[ClientActivity.systemsList.size() + 1];
        systemsNames[0] = "_________________";
        int id = 1;
        for (String name : ClientActivity.systemsList.keySet()) {
            systemsNames[id] = name;
            id++;
        }
        return systemsNames;
    }

    // выбор системы SAP из списка
    public void SelectSystem() {
        Spinner systemSelector = findViewById(R.id.systems);
        // список имен доступных систем
        String[] systemsNames = getAllSystemsNames();

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                systemsNames);

        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Применяем адаптер к элементу spinner
        systemSelector.setAdapter(adapter);

        AdapterView.OnItemSelectedListener systemSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startSelectedSystem(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        systemSelector.setOnItemSelectedListener(systemSelectedListener);
    }

    // запуск выбранной системы
    private void startSelectedSystem(AdapterView<?> parent, int position) {
        // Получаем выбранную систему
        String selectedSystName = (String) parent.getItemAtPosition(position);

        if ("_________________".equals(selectedSystName)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SystemsActivity.this);
            builder.setMessage("Выберите систему")
                    .setNeutralButton("OK", null)
                    .create()
                    .show();
        } else {
            final Intent intentForLogin = new Intent(SystemsActivity.this, LoginActivity.class);
            ClientActivity.selectedSystem = (String) ClientActivity.systemsList.get(selectedSystName).get(0);
            SystemsActivity.this.startActivity(intentForLogin);
        }
    }
}
