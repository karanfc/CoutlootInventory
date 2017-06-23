package com.example.coutlootinventory.Stock;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.coutlootinventory.InventoryApplication;
import com.example.coutlootinventory.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StockList extends AppCompatActivity {

    public String responseString;
    public String title;
    public String sellerId;
    public String type;
    RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    final List<ProductData> data=new ArrayList<>();
    InventoryApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details_by_title_or_user);
        Intent intent = getIntent();
        type = intent.getExtras().getString("type");
       // Log.e("title",title);
        application = (InventoryApplication) getApplicationContext();
        initializeViews();
        if(type.equals("1"))
        {
            sellerId = intent.getExtras().getString("sellerId");
            getStockDetailsFromSellerId(sellerId);
        }
        else {
            title = intent.getExtras().getString("title");
            getStockDetailsFromTitle(title);
        }
    }

    public void initializeViews()
    {
        application = (InventoryApplication) getApplicationContext();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mAdapter = new ProductAdapter(this, data);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getStockDetailsFromSellerId(String sellerId)
    {
        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("sellerId", sellerId)
                .build();

        Request request = new Request.Builder()
                .url("http://35.154.101.166/CoutlootOrders/sellerProducts.php")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Handle UI here
                        Log.e("Response", responseString);

                        try {
                            JSONObject object = new JSONObject(responseString);
                                JSONArray array = object.getJSONArray("subCatProducts");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject product = array.getJSONObject(i);
                                    ProductData productData = new ProductData();
                                    productData.productImage = product.getString("thumb1");
                                    productData.productTitle = product.getString("title");
                                    productData.productDetails = product.getString("description");
                                    productData.productId = product.getString("id");
                                    data.add(productData);
                                }
                                mAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public void getStockDetailsFromTitle(String title){

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("title", title)
                .build();

        Request request = new Request.Builder()
                .url("http://projectx38.com/NewApp/getStockDetailsFromTitle.php")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Handle UI here
                        Log.e("Response", responseString);
                        try {
                            JSONObject object = new JSONObject(responseString);
                            JSONArray array = object.getJSONArray("productDetail");
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject product = array.getJSONObject(i);
                                ProductData productData = new ProductData();
                                productData.productImage = product.getString("thumb1");
                                productData.productTitle = product.getString("title");
                                productData.productDetails = product.getString("description");
                                productData.productId = product.getString("id");
                                data.add(productData);
                            }
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
