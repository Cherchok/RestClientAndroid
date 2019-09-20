package com.e.restclienttest;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MenuActions {

    // выводит название системы в которой работает пользователь
    static void systemButton(Context context) {
        String systemName = "";
        for (String systName : ClientActivity.systemsList.keySet()) {
            if (ClientActivity.systemsList.get(systName).get(0).equals(ClientActivity.selectedSystem)) {
                systemName = systName;
            }
        }
        Toast.makeText(context, systemName, Toast.LENGTH_SHORT).show();
    }

    // переключает в меню настроек
    static void settings(Context context) {
        Intent intentSettings = new Intent(context, SetIP.class);
        context.startActivity(intentSettings);
    }
}
