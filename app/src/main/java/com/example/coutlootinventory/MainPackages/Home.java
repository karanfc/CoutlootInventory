package com.example.coutlootinventory.MainPackages;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.coutlootinventory.InventoryApplication;
import com.example.coutlootinventory.R;
import com.example.coutlootinventory.Stock.ScanQRCode;
import com.example.coutlootinventory.Stock.StockDetails;
import com.example.coutlootinventory.Stock.StockList;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {

    ImageView img;
    EditText input;
    String stockID;
    InventoryApplication application;
    String responseString;
    String productTitle;
    String sellerID;
    String sellerNumber;
    String sellerEmail;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        application = (InventoryApplication) getApplicationContext();
        img = (ImageView)findViewById(R.id.img);
        search = (Button)findViewById(R.id.button);
        setupDrawer();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Home.this, search);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.context_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_qrcode:
                                Intent intent1 = new Intent(getApplicationContext(), ScanQRCode.class);
                                startActivity(intent1);
                                break;

                            case R.id.item_stockid:
                                showInputDialog("Search By Stock Id", 1);
                                input.setHint("Enter Stock ID:");
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                break;

                            case R.id.item_sellerid:
                                showInputDialog("Search By Seller Id", 2);
                                input.setHint("Enter Seller ID:");
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                break;

                            case R.id.item_title:
                                showInputDialog("Search By Title", 5);
                                input.setHint("Enter Title:");
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                break;

                            case R.id.item_sellernumber:
                                showInputDialog("Search by Seller Phone", 3);
                                input.setHint("Enter Seller Phone:");
                                input.setInputType(InputType.TYPE_CLASS_PHONE);
                                break;

                            case R.id.item_selleremail:
                                showInputDialog("Search by Seller Email", 4);
                                input.setHint("Enter Seller Email:");
                                input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }

    void setupDrawer()
    {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Home");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Item two");

        new DrawerBuilder().withActivity(this).withToolbar((Toolbar)findViewById(R.id.toolbar)).addDrawerItems(item1, new DividerDrawerItem(), item2, new DividerDrawerItem()).build();

    }

    void showInputDialog(String title, final int type) {
        //0,1 - stock id , 2 - seller_id, 3 - seller_no, 4 - seller_email, 5 - title

        View layout = getLayoutInflater().inflate(R.layout.layout_input_edittext, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setView(layout);
        input = (EditText) layout.findViewById(R.id.input);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(type == 0 | type == 1)
                {
                    stockID = input.getText().toString();
                    getStockDetailsFromStockID(stockID, type);
                }
                else if(type == 5)
                {
                    productTitle = input.getText().toString();
                    if(productTitle.length()!=0)
                    {
                        Intent intent = new Intent(Home.this, StockList.class);
                        intent.putExtra("title", productTitle);
                        intent.putExtra("type","5");
                        startActivity(intent);
                    }
                }
                else if(type == 2)
                {
                    sellerID = input.getText().toString();
                    Log.e("Stuck",sellerID);
                    Intent intent = new Intent(Home.this, StockList.class);
                    intent.putExtra("sellerId", sellerID);
                    intent.putExtra("type","2");
                    startActivity(intent);
                }

                else if(type == 3)
                {
                    sellerNumber = input.getText().toString();
                    Intent intent = new Intent(Home.this, StockList.class);
                    intent.putExtra("phone", sellerNumber);
                    intent.putExtra("type","3");
                    startActivity(intent);
                }
                else if(type == 4)
                {
                    sellerEmail = input.getText().toString();
                    Intent intent = new Intent(Home.this, StockList.class);
                    intent.putExtra("email", sellerEmail);
                    intent.putExtra("type","4");
                    startActivity(intent);
                }
            }
        });
        builder.show();

    }


    void getStockDetailsFromStockID(final String id, int type)
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
                Log.d("stuck:", responseString );
             //   Log.e("Response", responseString);
                Intent intent = new Intent(Home.this, StockDetails.class);
                intent.putExtra("Response", responseString);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });

    }
}


