package com.example.coutlootinventory.Stock;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.coutlootinventory.InventoryApplication;
import com.example.coutlootinventory.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScanQRCode extends AppCompatActivity {

    private Button button;
    String StockID;
    InventoryApplication application;
    String responseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        application = (InventoryApplication)getApplicationContext();

        initializeViews();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(ScanQRCode.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    public void initializeViews()
    {
        button = (Button)findViewById(R.id.zxing_barcode_scanner);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(ScanQRCode.this,"Scanning cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                StockID = result.getContents();
                Toast.makeText(ScanQRCode.this,StockID, Toast.LENGTH_LONG).show();
                getStockDetails(StockID);
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void getStockDetails(String id)
    {
        Log.e("Session",application.getSessionId());
        OkHttpClient client = new OkHttpClient();

        Log.i("id", String.valueOf(id));

        FormBody body = new FormBody.Builder()
                .add("productId", id)
                .build();


        Request request = new Request.Builder()
                .url("http://projectx38.com/NewApp/getStockDetails.php")
                .addHeader("Appapi-Id","FZUx9ZBqMLpJ18lcnaEfcA7rTJOVpERbRiEuLqp1lj8=")
                .addHeader("Device-Id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                .addHeader("User-Id", application.getUserId())
                .addHeader("Session-Id",application.getSessionId())
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("Error",e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                responseString = response.body().string();
                try {
                    final JSONObject object = new JSONObject(responseString);
                    if(!object.get("message").equals("Product Does not Exist"))
                    {
                        Log.e("Response",responseString);
                        Intent intent = new Intent(ScanQRCode.this, StockDetails.class);
                        intent.putExtra("Response",responseString);
                        startActivity(intent);
                    }
                    else
                    {
                        Log.e("Response",responseString);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}

