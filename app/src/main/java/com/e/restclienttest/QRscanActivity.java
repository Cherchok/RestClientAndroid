package com.e.restclienttest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TimerTask;

public class QRscanActivity extends AppCompatActivity {
    SurfaceView cameraPreview;
    TextView textResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;

    String where;
    String table = "QR";
    String fieldsQuan = "1";
    String language;
    String order = " ";
    String group = " ";
    String fieldNames = " ";
    String url;
    // параметры клиента
    String systemAddress;
    String login;
    String password;
    String number;
    ArrayList<Mapa> sapDataList;

    final int RequestCamerPermissinID = 1001;

    public void sendQR() {

        // составляем url с параметрами идентификации(применим после настройки сервера)
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http://192.168.0.21:8080/rest/rest/wmap" + "/").append(systemAddress).append("/")
                .append(login).append("/").append(password).append("/").append(number).append("/").append(table);

        if (!fieldsQuan.equals(" ")) {
            urlSB.append("/").append(fieldsQuan);
        }
        if (!language.equals(" ")) {
            urlSB.append("/").append(language);
        }
        if (!where.equals(" ")) {
            String st = where;
            try {
                where = URLEncoder.encode(st, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            urlSB.append("/").append(where);
        }
        if (!order.equals(" ")) {
            urlSB.append("/").append(order);
        }
        if (!group.equals(" ")) {
            urlSB.append("/").append(group);
        }
        if (!fieldNames.equals(" ")) {
            urlSB.append("/").append(fieldNames);
        }


        // получаем готвый url с внесенными параметрами
        url = urlSB.toString();

        // GET запрос к серверу
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // TODO
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response", error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
        if (!(jsonArrayRequest.getUrl() == null)) {
            textResult.setText("QR передан успешно");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCamerPermissinID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_qr);

        Intent intentMain = getIntent();
        login = intentMain.getStringExtra("userName");
        password = intentMain.getStringExtra("password");
        language = intentMain.getStringExtra("language");
        systemAddress = intentMain.getStringExtra("systemAddress");
        number = intentMain.getStringExtra("clientNumber");

        cameraPreview = findViewById(R.id.cameraPreview);
        textResult = findViewById(R.id.txtResult);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        // add event
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission
                    ActivityCompat.requestPermissions(QRscanActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCamerPermissinID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0) {
                    textResult.post(new Runnable() {
                        @Override
                        public void run() {
                            //Create vibrate
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(300);
                            textResult.setText(qrcodes.valueAt(0).displayValue);
                            where = qrcodes.valueAt(0).displayValue
                                    .replaceAll("\\r", "~~&")
                                    .replaceAll("\\n", "~~&")
                                    .replaceAll(" ", "~~&");
                            sendQR();
                        }
                    });
                }
            }
        });
    }
}
