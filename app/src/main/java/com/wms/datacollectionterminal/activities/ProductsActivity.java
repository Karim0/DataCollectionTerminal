package com.wms.datacollectionterminal.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.wms.datacollectionterminal.R;
import com.wms.datacollectionterminal.adapters.CustomAdapter;
import com.wms.datacollectionterminal.adapters.RecyclerItemClickListener;
import com.wms.datacollectionterminal.entities.ProductEntity;
import com.wms.datacollectionterminal.helpers.CallBackHttpSender;
import com.wms.datacollectionterminal.helpers.HttpSender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class ProductsActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Button addProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        customAdapter = new CustomAdapter();

        this.recyclerView = findViewById(R.id.recyclerView);
        this.addProduct = findViewById(R.id.btn_product_add);
        this.addProduct.setOnClickListener(this);

        this.recyclerView.setAdapter(customAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(ProductsActivity.this, AddProductActivity.class);

                        ProductEntity productEntity = customAdapter.getData().get(position);
                        i.putExtra("name", productEntity.getProductName());
                        i.putExtra("bar_code", productEntity.getBarCode());
                        i.putExtra("length", productEntity.getLength());
                        i.putExtra("weight", productEntity.getWeight());
                        i.putExtra("width", productEntity.getWidth());
                        i.putExtra("height", productEntity.getHeight());
                        i.putExtra("price", productEntity.getPrice());
                        i.putExtra("id", productEntity.getId());
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadProduct();
    }

    private void loadProduct() {
        customAdapter.clear();
        Log.i("test", "Load product list");
        HttpSender.postRequest(this, "/product/all", new CallBackHttpSender() {
            @Override
            public void responseResult(String s) {
                try {
                    JSONArray arr = new JSONArray(s);

                    for (int i = 0; i < arr.length(); i++) {
                        ProductEntity p = new ProductEntity(arr.getJSONObject(i));
                        customAdapter.addRow(p);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(VolleyError e) {

            }
        }, Collections.emptyMap());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_product_add:
                Intent i = new Intent(ProductsActivity.this, AddProductActivity.class);
                startActivity(i);
                break;
        }
    }
}