package com.example.coutlootinventory.Stock;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coutlootinventory.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by karan on 02/06/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<ProductData> data = Collections.emptyList();
    ProductData current;

    public ProductAdapter(Context context, List<ProductData> data){
        this.context=context;
        inflater= LayoutInflater.from(context);

        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.product_list_item, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        current=data.get(position);
        myHolder.productTitle.setText(current.productTitle);
        myHolder.productDetails.setText(current.productDetails);

        // load image into ImageView using glide
        Glide.with(context).load(current.productImage)
                .into(myHolder.productImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView productTitle;
        ImageView productImage;
        TextView productDetails;

        // create constructor to get widget reference
        public MyHolder(final View itemView) {
            super(itemView);
            productTitle = (TextView) itemView.findViewById(R.id.productTitle);
            productImage = (ImageView)itemView.findViewById(R.id.productImage);
            productDetails = (TextView)itemView.findViewById(R.id.productDetails);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StockDetails.class);
                    intent.putExtra("id", data.get(getAdapterPosition()).productId);
                    intent.putExtra("type", "1");
                    context.startActivity(intent);
                }
            });
        }

    }
}
