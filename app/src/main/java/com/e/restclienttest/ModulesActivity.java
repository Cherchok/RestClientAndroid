package com.e.restclienttest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ModulesActivity extends AppCompatActivity {

    // список доступных модулей
    String[] modules;

    // тест для выбора модуля
    TextView selection;

    // заполняем список модулей доступных в SAP системе
    public String[] getModulesNames() {
        Intent intentLogin = getIntent();
        ArrayList<String> modulesList = new ArrayList<>();
        String[] modules;
        for (String name : intentLogin.getExtras().keySet()) {
            if (name.equals("REPI2")) {
                modulesList = intentLogin.getStringArrayListExtra(name);
            }
        }
        modules = new String[modulesList.size() - 1];
        int id = 0;
        for (int i = 0; i < modulesList.size(); i++) {
            if (!(i == 1)) {
                modules[id] = modulesList.get(i).trim();
                id++;
            } else selection.setText(modulesList.get(i).trim());
        }

        return modules;
    }

    // метод вызывется при создании(вызове) данного Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        Spinner spinner = findViewById(R.id.systems);
        selection = findViewById(R.id.selection_sys);
        modules = getModulesNames();

        // передаем введенные  логин, пароль и язык
        Intent intentLogin = getIntent();
        final String login = intentLogin.getStringExtra("userName");
        final String password = intentLogin.getStringExtra("password");
        final String language = intentLogin.getStringExtra("language");
        final String systemAddress = intentLogin.getStringArrayListExtra("systemAddress").get(0);
        final String clientNumber = intentLogin.getStringExtra("clientNumber");
        final String ip = intentLogin.getStringExtra("ip");

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

                // осуществляем вход в необходимый модуль в соответствии с выбранным по имени
                switch (item) {
                    case "QR коды": // A
                        final Intent intentQR = new Intent(ModulesActivity.this, QRscanActivity.class);

                        intentQR.putExtra("userName", login);
                        intentQR.putExtra("password", password);
                        intentQR.putExtra("language", language);
                        intentQR.putExtra("module", item);
                        intentQR.putExtra("systemAddress", systemAddress);
                        intentQR.putExtra("clientNumber", clientNumber);
                        intentQR.putExtra("ip", ip);
                        ModulesActivity.this.startActivity(intentQR);
                        break;

                    case "Просмотр таблиц": // Z
                        final Intent intentTab = new Intent(ModulesActivity.this, ParamsActivity.class);

                        intentTab.putExtra("userName", login);
                        intentTab.putExtra("password", password);
                        intentTab.putExtra("language", language);
                        intentTab.putExtra("module", item);
                        intentTab.putExtra("systemAddress", systemAddress);
                        intentTab.putExtra("clientNumber", clientNumber);
                        intentTab.putExtra("ip", ip);
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


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        System.out.println("destroyingggggggggggggg!!!!!!!!");
//    }

//    // метод вызывется при нажатии кнопки return на данном Activity на устройстве android
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//    }
}
