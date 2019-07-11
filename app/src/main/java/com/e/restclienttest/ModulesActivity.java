package com.e.restclienttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ModulesActivity extends AppCompatActivity {

    // список доступных модулей
    String[] modules; // = new String[]{"_________________", "A", "Z"};

    // тест для выбора модуля
    TextView selection;

    // заполняем список модулей имеющимися на сервере
    public String[] fillModules() {
        Intent intentLogin = getIntent();
        String[] modules = new String[intentLogin.getExtras().keySet().size() - 2];
        modules[0] = "_________________";
        int id = 1;
        for (String name : intentLogin.getExtras().keySet()) {
            boolean flag = false;
            if (name.equals("userName")) {
                flag = true;
            }
            if (name.equals("password")) {
                flag = true;
            }
            if (name.equals("language")) {
                flag = true;
            }
            if (!flag) {
                modules[id] = name;
                id++;
            }
        }
        return modules;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        modules = fillModules();
        Spinner spinner = findViewById(R.id.modules);
        selection = findViewById(R.id.selection);

        // передаем введенные  логин, пароль и язык
        Intent intentLogin = getIntent();
        final String login = intentLogin.getStringExtra("userName");
        final String password = intentLogin.getStringExtra("password");
        final String language = intentLogin.getStringExtra("language");

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modules);

        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String) parent.getItemAtPosition(position);
//                selection.setText("Select module :");

                // осуществляем вход в необходимый модуль в соответствии с выбранным по имени
                switch (item) {
                    case "QRread": // A
                        final Intent intentQR = new Intent(ModulesActivity.this, QRscanActivity.class);

                        intentQR.putExtra("userName", login);
                        intentQR.putExtra("password", password);
                        intentQR.putExtra("language", language);
                        intentQR.putExtra("module", item);
                        ModulesActivity.this.startActivity(intentQR);
                        break;

                    case "ZTABLEREAD": // Z
                        final Intent intentTab = new Intent(ModulesActivity.this, MainActivity.class);

                        intentTab.putExtra("userName", login);
                        intentTab.putExtra("password", password);
                        intentTab.putExtra("language", language);
                        intentTab.putExtra("module", item);
                        ModulesActivity.this.startActivity(intentTab);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

}
