package com.e.restclienttest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class SetIP extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip);

        final EditText etIP = findViewById(R.id.etIP);
        Button connectBtn = findViewById(R.id.bConnect);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = etIP.getText().toString().trim();
                Intent intentConnection = new Intent(SetIP.this, ConnectionActivity.class);

                MyPropertiesHolder propHolder = null;
                try {
                    propHolder = new MyPropertiesHolder(SetIP.this, "test.properties", MyPropertiesHolder.MODE_CREATE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                propHolder.setProperty("ip1", ip);

                try {
                    propHolder.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SetIP.this.startActivity(intentConnection);

            }
        });


    }
}