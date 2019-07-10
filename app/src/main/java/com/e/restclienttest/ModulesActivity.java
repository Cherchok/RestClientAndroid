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

    String[] modules = new String[]{"_________________", "A", "Z"};
    TextView selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        Spinner spinner = findViewById(R.id.modules);
        selection = findViewById(R.id.selection);

        Intent intent = getIntent();
        final String login = intent.getStringExtra("userName");
        final String password = intent.getStringExtra("password");
        final String language = intent.getStringExtra("language");

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

                switch (item) {
                    case "Z":
                        final Intent intentQR = new Intent(ModulesActivity.this, QRscanActivity.class);

                        intentQR.putExtra("userName", login);
                        intentQR.putExtra("password", password);
                        intentQR.putExtra("language", language);
                        intentQR.putExtra("module", item);
                        ModulesActivity.this.startActivity(intentQR);
                        break;

                    case "A":
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
