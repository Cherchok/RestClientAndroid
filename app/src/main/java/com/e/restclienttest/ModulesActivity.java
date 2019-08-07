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
    String[] modules;

    // текст для выбора модуля
    TextView selectionText;

    String selectedApp;

    // заполняем список модулей доступных в SAP системе
    public void setModulesNames() {
        modules = new String[ClientActivity.modulesList.size() - 1];
        int id = 0;
        for (int i = 0; i < ClientActivity.modulesList.size(); i++) {
            if (!(i == 1)) {
                modules[id] = ClientActivity.modulesList.get(i).trim();
                id++;
            } else selectionText.setText(ClientActivity.modulesList.get(i).trim());
        }
    }

    //
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

                // Получаем выбранный объект
                ClientActivity.selectedModule = (String) parent.getItemAtPosition(position);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < ClientActivity.selectedModule.length(); i++) {
                    if (ClientActivity.selectedModule.charAt(i) != '.') {
                        sb.append(ClientActivity.selectedModule.charAt(i));
                    } else break;
                }

                // удаление впереди идущих '0'
                String moduleToStart = sb.toString().replaceFirst("^0+(?!$)", "");


                int index = 999;
                for (int i = 0; i < ClientActivity.modulesList.size(); i++) {
                    if (ClientActivity.modulesList.get(i).contains(ClientActivity.selectedModule)
                            && !ClientActivity.selectedModule.equals("")) {
                        index = i;
                    }
                }
                if (index != 999) {
                    selectedApp = ClientActivity.moduleIDlist.get(index).trim();
                }


                // осуществляем вход в необходимый модуль в соответствии с выбранным по имени
                if (selectedApp != null) {
                    switch (selectedApp) {
                        case "A":
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
                        case "Z":
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
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    // метод вызывется при создании(вызове) данного Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        startSelectedModule();
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
