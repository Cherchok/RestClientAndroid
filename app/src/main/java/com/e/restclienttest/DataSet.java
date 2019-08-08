package com.e.restclienttest;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@SuppressWarnings("unused")
public class DataSet extends AppCompatActivity {
    private LinkedHashMap<String, LinkedList<String>> map;
    private LinearLayout linear;
    private View view;

    public LinkedHashMap<String, LinkedList<String>> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<String, LinkedList<String>> map) {
        this.map = map;
    }

    public LinearLayout getLinear() {
        return linear;
    }

    public void setLinear(LinearLayout linear) {
        this.linear = linear;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void keyVisualisation() {
        LinearLayout linear2 = linear;
        // управление наполнением внутри view
        for (int i = 0; i < linear2.getChildCount(); i++) {
            if (i == 2) {
                linear2.getChildAt(i).findViewById(R.id.button2).setVisibility(View.INVISIBLE);
            }

        }
    }
}
