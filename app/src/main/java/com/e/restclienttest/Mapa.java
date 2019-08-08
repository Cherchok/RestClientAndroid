package com.e.restclienttest;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

@SuppressWarnings("unused")
// класс для десерелизации Map из сервера
public class Mapa {

    @SerializedName("name")
    private String name;

    @SerializedName("values")
    private LinkedList<String> values;

    public Mapa() {
    }

    public Mapa(String name, LinkedList<String> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String toString() {
        return name + ": " + values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    LinkedList<String> getValues() {
        return values;
    }

    public void setValues(LinkedList<String> values) {
        this.values = values;
    }
}
