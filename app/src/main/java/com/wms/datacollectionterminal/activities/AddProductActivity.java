package com.wms.datacollectionterminal.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.wms.datacollectionterminal.R;
import com.wms.datacollectionterminal.helpers.CallBackHttpSender;
import com.wms.datacollectionterminal.helpers.HttpSender;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText productName;
    private EditText productBarCode;
    private EditText productLength;
    private EditText productWeight;
    private EditText productWidth;
    private EditText productHeight;
    private EditText productPrice;
    private Button save;
    private Button delete;

    private Long id;

    private Boolean isUpdate = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productName = findViewById(R.id.product_name);
        productBarCode = findViewById(R.id.product_bar_code);
        productLength = findViewById(R.id.product_length);
        productWeight = findViewById(R.id.product_weight);
        productWidth = findViewById(R.id.product_width);
        productHeight = findViewById(R.id.product_height);
        productPrice = findViewById(R.id.product_price);
        save = findViewById(R.id.btn_product_save);
        delete = findViewById(R.id.btn_product_delete);

        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productName.setText(extras.getString("name"));
            productBarCode.setText(extras.getString("bar_code"));
            productLength.setText("" + extras.getDouble("length"));
            productWeight.setText("" + extras.getDouble("weight"));
            productWidth.setText("" + extras.getDouble("width"));
            productHeight.setText("" + extras.getDouble("height"));
            productPrice.setText("" + extras.getDouble("price"));
            id = extras.getLong("id");
            isUpdate = true;
        } else {
            delete.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_product_save:
                save();
                break;
            case R.id.btn_product_delete:
                delete();
                break;
        }
    }

    private void save() {


        if (isUpdate) {
            JSONObject json = new JSONObject();

            try {
                json.put("date", "2021-12-07T16:09:24.319+0000");
                json.put("count_on_warehouse", "0");
                json.put("count_on_shipping", "0");
                json.put("count_expected", "0");
                json.put("unit", "thing");
                json.put("type_product", "fragile");
                json.put("product_name", productName.getText().toString());
                json.put("bar_code", productBarCode.getText().toString());
                json.put("length", productLength.getText().toString());
                json.put("weight", productWeight.getText().toString());
                json.put("width", productWidth.getText().toString());
                json.put("height", productHeight.getText().toString());
                json.put("price", productPrice.getText().toString());
                json.put("id", id.toString());
            } catch (Exception e) {
                Log.e("test", e.getMessage());
            }

            Log.i("test", "JSON = " + json.toString());

            HttpSender.postRequest(this, "/product/updateById", new CallBackHttpSender() {
                @Override
                public void responseResult(String s) {
                    Log.i("test", "Save product: " + s);
//                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }

                @Override
                public void responseResult(JSONObject json) {
                    Log.i("test", "Save product: " + json);
                    finish();
                }

                @Override
                public void error(VolleyError e) {
                    Log.e("test", "Save product: " + e.getMessage());
                    finish();
                }
            }, Collections.emptyMap(), json);

        } else {
            JSONObject json = new JSONObject();

            try {
                json.put("date", "2021-12-07T16:09:24.319+0000");
                json.put("count_on_warehouse", "0");
                json.put("count_on_shipping", "0");
                json.put("count_expected", "0");
                json.put("unit", "thing");
                json.put("type_product", "fragile");
                json.put("product_name", productName.getText().toString());
                json.put("bar_code", productBarCode.getText().toString());
                json.put("length", productLength.getText().toString());
                json.put("weight", productWeight.getText().toString());
                json.put("width", productWidth.getText().toString());
                json.put("height", productHeight.getText().toString());
                json.put("price", productPrice.getText().toString());
            } catch (Exception e) {
                Log.e("test", e.getMessage());
            }

            Log.i("test", "JSON = " + json.toString());

            HttpSender.postRequest(this, "/product/add", new CallBackHttpSender() {
                @Override
                public void responseResult(String s) {
                    Log.i("test", "Save product: " + s);
//                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                }

                @Override
                public void responseResult(JSONObject json) {
                    Log.i("test", "Save product: " + json);
                    finish();
                }

                @Override
                public void error(VolleyError e) {
                    Log.e("test", "Save product: " + e.getMessage());
                    finish();
                }
            }, Collections.emptyMap(), json);
        }
    }

    private void delete() {
        HttpSender.deleteRequest(this, "/product/deleteById?id=" + id.toString(), new CallBackHttpSender() {
            @Override
            public void responseResult(String s) {
                Log.i("test", "delete product: " + s);
                finish();
            }

            @Override
            public void responseResult(JSONObject json) {
                Log.i("test", "delete product: " + json);
                finish();
            }

            @Override
            public void error(VolleyError e) {
                Log.e("test", "delete product: " + e.getMessage());
                finish();
            }
        }, Collections.singletonMap("id", id.toString()));
    }
}