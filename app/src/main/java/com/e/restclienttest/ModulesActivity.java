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

    // текст для выбора модуля
    TextView selection;

    // заполняем список модулей доступных в SAP системе
    public void getModulesNames() {
        modules = new String[ClientActivity.modulesList.size() - 1];
        int id = 0;
        for (int i = 0; i < ClientActivity.modulesList.size(); i++) {
            if (!(i == 1)) {
                modules[id] = ClientActivity.modulesList.get(i).trim();
                id++;
            } else selection.setText(ClientActivity.modulesList.get(i).trim());
        }
    }

    // метод вызывется при создании(вызове) данного Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        Spinner spinner = findViewById(R.id.systems);
        selection = findViewById(R.id.selection_sys);
        getModulesNames();

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
                ClientActivity.selectedModule = (String) parent.getItemAtPosition(position);
                String item = (String) parent.getItemAtPosition(position);

                // осуществляем вход в необходимый модуль в соответствии с выбранным по имени
                switch (item) {
                    case "QR коды": // A
                        final Intent intentQR = new Intent(ModulesActivity.this, QRscanActivity.class);

                        intentQR.putExtra("userName", ClientActivity.username);
                        intentQR.putExtra("password", ClientActivity.password);
                        intentQR.putExtra("language", ClientActivity.language);
                        intentQR.putExtra("module", ClientActivity.selectedModule);
                        intentQR.putExtra("systemAddress", ClientActivity.selectedSystem);
                        intentQR.putExtra("clientNumber", ClientActivity.clientID);
                        intentQR.putExtra("ipServer", ClientActivity.ipServer);
                        ModulesActivity.this.startActivity(intentQR);
                        break;

                    case "Просмотр таблиц": // Z
                        final Intent intentTab = new Intent(ModulesActivity.this, ParamsActivity.class);

                        intentTab.putExtra("userName", ClientActivity.username);
                        intentTab.putExtra("password", ClientActivity.password);
                        intentTab.putExtra("language", ClientActivity.language);
                        intentTab.putExtra("module", ClientActivity.selectedModule);
                        intentTab.putExtra("systemAddress", ClientActivity.selectedSystem);
                        intentTab.putExtra("clientNumber", ClientActivity.clientID);
                        intentTab.putExtra("ipServer", ClientActivity.ipServer);
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
