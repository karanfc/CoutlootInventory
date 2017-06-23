package com.example.coutlootinventory.Stock;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coutlootinventory.InventoryApplication;
import com.example.coutlootinventory.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StockDetails extends AppCompatActivity {

    String stockDetails;
    String stockId;
    String type;
    TextView title, description, featured, brand, category, size, originalPrice, mrp, percentage, liveOn, listedOn,
            listingType, likes, views, negotiationCount, sellerId, sellerName, sellerPhone, sellerEmail;
    InventoryApplication application;
    String photoUrl;
    String responseString;
    ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        application = (InventoryApplication) getApplication();
        Intent intent = getIntent();
        stockDetails = intent.getExtras().getString("Response");
        stockId = intent.getExtras().getString("id");
        type = intent.getExtras().getString("type");
        initializeViews();

       try {
            if(type.equals("0"))
            {
                displayDetails(stockDetails);

            }
            else if(type.equals("1"))
            {
                getDetails(stockId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getDetails(String id)
    {
        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("productId", id)
                .build();


        Request request = new Request.Builder()
                .url("http://35.154.101.166/CoutlootOrders/getStockDetails.php")
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
                                try {
                                    displayDetails(responseString);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });

    }


    private void displayDetails(String stockDetails) throws JSONException {
        JSONObject object = new JSONObject(stockDetails);
        JSONArray array;
        JSONArray array2;
        array = object.getJSONArray("Product Details");
        array2 = object.getJSONArray("Seller Details");
        Log.e("Array", array.toString());
        title.setText(array.getJSONObject(0).get("title").toString());
        description.setText(array.getJSONObject(0).get("description").toString());
        brand.setText(array.getJSONObject(0).get("brandName").toString());
        category.setText(array.getJSONObject(0).get("category").toString());
        size.setText(array.getJSONObject(0).get("size").toString());
        originalPrice.setText("Original price: " + " " + "₹" + array.getJSONObject(0).get("originalPrice").toString());
        mrp.setText("MRP: " + "\n" + "₹" + array.getJSONObject(0).get("mrp").toString());
        percentage.setText("Percent Off: " + "\n" + array.getJSONObject(0).get("percentOff") + "%");
        liveOn.setText("Live on: " + array.getJSONObject(0).get("liveOn").toString());
        listedOn.setText("Listed on: " + array.getJSONObject(0).get("listedOn").toString());
        listingType.setText("Listing type: " + array.getJSONObject(0).get("listedOn").toString());
        likes.setText(array.getJSONObject(0).get("likes").toString());
        views.setText(array.getJSONObject(0).get("views").toString());
        negotiationCount.setText(array.getJSONObject(0).get("negotiationCount").toString());
        sellerId.setText("User ID: " + array2.getJSONObject(0).get("userId").toString());
        sellerName.setText("Name: " + array2.getJSONObject(0).get("name").toString());
        sellerPhone.setText("Phone: " + array2.getJSONObject(0).get("mobile").toString() );
        sellerEmail.setText("Email Id: " + array2.getJSONObject(0).get("email").toString());
        photoUrl = array.getJSONObject(0).get("productImg1").toString();
        loadImages();
    }

    public void loadImages()
    {
        Glide.with(getApplicationContext()).load(photoUrl).into(productImage);
    }

    private void initializeViews() {
        title = (TextView)findViewById(R.id.stock_title);
        description = (TextView)findViewById(R.id.description);
        brand = (TextView)findViewById(R.id.brandName);
        category = (TextView)findViewById(R.id.category);
        size = (TextView)findViewById(R.id.size);
        originalPrice = (TextView)findViewById(R.id.originalPrice);
        mrp = (TextView)findViewById(R.id.mrp);
        percentage = (TextView)findViewById(R.id.percentageOff);
        liveOn = (TextView)findViewById(R.id.liveOn);
        listedOn = (TextView)findViewById(R.id.listedOn);
        listingType = (TextView)findViewById(R.id.listedType);
        likes = (TextView)findViewById(R.id.likes);
        views = (TextView)findViewById(R.id.views);
        negotiationCount = (TextView)findViewById(R.id.negotiationsCount);
        sellerId = (TextView)findViewById(R.id.sellerId);
        sellerName = (TextView)findViewById(R.id.sellerName);
        sellerEmail = (TextView)findViewById(R.id.sellerEmail);
        sellerPhone = (TextView)findViewById(R.id.sellerPhone);
        productImage = (ImageView)findViewById(R.id.productImage);
    }
}
