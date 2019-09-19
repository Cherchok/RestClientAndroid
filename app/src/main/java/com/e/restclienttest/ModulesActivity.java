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
import android.widget.Spinner;
import android.widget.TextView;

public class ModulesActivity extends AppCompatActivity {

    // список доступных модулей
    String[] modules;
    // текст для выбора модуля
    TextView selectionText;
    // id выбранного приложения
    String selectedAppID;

    // метод вызывется при создании(вызове) данного Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        startSelectedModule();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModulesActivity.this);

        builder.setMessage("Хотите сменить пользователя ?")
                .setNegativeButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intentSetIP = new Intent(ModulesActivity.this, LoginActivity.class);
                        ModulesActivity.this.startActivity(intentSetIP);
                    }
                })
                .setNeutralButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intentSetIP = new Intent(ModulesActivity.this, ModulesActivity.class);
                        ModulesActivity.this.startActivity(intentSetIP);
                    }
                })
                .create()
                .show();

    }

    // заполняем список модулей доступных в SAP системе
    public void setModulesNames() {
        modules = new String[ClientActivity.modulesList.size() - 1];
        int id = 0;
        for (int i = 0; i < ClientActivity.modulesList.size(); i++) {
            if (!(i == 1)) {
                modules[id] = ClientActivity.replaceLeftLeadingZeros(ClientActivity.modulesList.get(i).trim());
                id++;
            } else selectionText.setText(ClientActivity.modulesList.get(i).trim());
        }
    }

    // запуск приложения
    private void startSelectedApp(AdapterView<?> parent, int position) {
        // Получаем выбранный объект
        ClientActivity.selectedModule = (String) parent.getItemAtPosition(position);
        selectedAppID = ClientActivity.getSelectedModuleID();
        // осуществляем вход в необходимый модуль в соответствии с выбранным по имени
        if (selectedAppID != null) {
            ClientActivity.selectedModuleID = selectedAppID;
            switch (selectedAppID) {
                case "A":
                    final Intent intentTab = new Intent(ModulesActivity.this, ParamsActivity.class);
                    ModulesActivity.this.startActivity(intentTab);
                    break;
                case "Z":
                    final Intent intentQR = new Intent(ModulesActivity.this, QRscanActivity.class);
                    ModulesActivity.this.startActivity(intentQR);
                    break;
            }
        }
    }

    // запуск выбранного модуля
    public void startSelectedModule() {
        Spinner spinner = findViewById(R.id.systems);
        selectionText = findViewById(R.id.selection_sys);
        setModulesNames();

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modules);

        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startSelectedApp(parent, position);
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
