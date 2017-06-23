package com.example.coutlootinventory;

import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coutlootinventory.MainPackages.Home;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText ePhoneNumber, ePassword;
    Button blogin;
    String mobile, password1;
    TextInputLayout tPhoneNumber, tPassword;
    String responseString;
    InventoryApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void initializeViews()
    {
        application = (InventoryApplication) getApplicationContext();
        tPhoneNumber = (TextInputLayout)findViewById(R.id.text_input_layout_phone);
        tPassword = (TextInputLayout)findViewById(R.id.text_input_layout_password);
        ePhoneNumber = (EditText) findViewById(R.id.input_phone_number);
        ePassword = (EditText) findViewById(R.id.input_password);
        blogin = (Button)findViewById(R.id.btn_login);
    }

    public void login()
    {
        mobile = ePhoneNumber.getText().toString();
        password1 = ePassword.getText().toString();

        if(mobile.length()!=10)
        {
            tPhoneNumber.setError("Please enter a valid phone number:");
        }
        else
        {
            tPhoneNumber.setError("");
            OkHttpClient client = new OkHttpClient();

            FormBody body = new FormBody.Builder()
                    .add("mobile", mobile)
                    .add("password1", password1)
                    .build();


            Request request = new Request.Builder()
                    .url("http://projectx38.com/NewApp/inventoryUserDetails.php")
                    .addHeader("Appapi-Id","FZUx9ZBqMLpJ18lcnaEfcA7rTJOVpERbRiEuLqp1lj8=")
                    .addHeader("Device-Id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                    .post(body)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseString = response.body().string();
                                Log.e("Response",responseString);
                                JSONObject jsonObject = new JSONObject(responseString);
                                if(jsonObject.get("success").toString().equals("1"))
                                {
                                    String sessionId = jsonObject.getString("session");
                                    String userId = jsonObject.getJSONArray("details").getString(0);
                                    application.setSessionId(sessionId);
                                    application.setUserId(userId);
                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


        }
    }
}