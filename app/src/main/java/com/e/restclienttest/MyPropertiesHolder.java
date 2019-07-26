package com.e.restclienttest;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class MyPropertiesHolder {
    public static int MODE_CREATE = 0;
    public static int MODE_UPDATE = 1;
    private Context context;
    private String filename;
    private Properties properties;
    public MyPropertiesHolder(Context context, String filename, int mode) throws IOException {
        this.context = context;
        this.filename = filename;
        this.properties = new Properties();
        if(mode != MODE_CREATE)  {
            FileInputStream inputStream = context.openFileInput(filename);
            properties.load(inputStream);
            inputStream.close();
        }
    }
    public String getProperty(String key){
        return (String) properties.get(key);
    }
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    public void commit() throws IOException {
        FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        properties.store(outputStream, "");
        outputStream.close();
    }
}
