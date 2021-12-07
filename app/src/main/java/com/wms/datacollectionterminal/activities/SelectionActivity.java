package com.wms.datacollectionterminal.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wms.datacollectionterminal.R;
import com.wms.datacollectionterminal.dialogs.BarcodeScanResult;
import com.wms.datacollectionterminal.dialogs.ScanBarcodeDialog;
import com.wms.datacollectionterminal.helpers.CallBackHttpSender;
import com.wms.datacollectionterminal.helpers.HttpSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener {

    //    EditText etOrderNumber;
    EditText etPalletBarcode;
    EditText etAmount;
    //    ImageButton scanOrderNumber;
    ImageButton scanBarcodePallet;
    TextView amountProduct;
    Button take;
    int amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

//        etOrderNumber = findViewById(R.id.sel_ed_code_order);
        etPalletBarcode = findViewById(R.id.sel_ed_code_container);
        etAmount = findViewById(R.id.sel_ed_amount);
        amountProduct = findViewById(R.id.sel_amount_product);

//        scanOrderNumber = findViewById(R.id.sel_btn_code_order);
        scanBarcodePallet = findViewById(R.id.sel_btn_code_container);
        take = findViewById(R.id.sel_btn_take);

//        scanOrderNumber.setOnClickListener(this);
        scanBarcodePallet.setOnClickListener(this);
        take.setOnClickListener(this);

        etPalletBarcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Map<String, String> params = new HashMap<>();
                params.put("barcode", etPalletBarcode.getText().toString());

                HttpSender.postRequest(getApplicationContext(), "/container/getContainerByBarcode", new CallBackHttpSender() {
                    @Override
                    public void responseResult(String s) {
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONObject cell = object.getJSONObject("cellId");

                            amount = object.getInt("amount");

                            amountProduct.setText(MessageFormat.format("{0}: {1}шт.", object.getJSONObject("product").getString("product_name"), amount));
                            if (object.getString("lifeCycle").equals("receipt")) {
                                amountProduct.setText(MessageFormat.format("Переместить паллет в ячейку {0}-{1}-{2}", cell.getString("stillage"), cell.getString("shelf"), cell.getString("cell")));
                            } else if (object.getString("lifeCycle").equals("store")) {
                                amountProduct.setText("Паллет уже находится в своей ячейке");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error(VolleyError e) {

                    }
                }, params);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sel_btn_code_container:
                ScanBarcodeDialog scanBarcodeDialog = new ScanBarcodeDialog();
                scanBarcodeDialog.setBarcodeScanResult(new BarcodeScanResult() {
                    @Override
                    public void result(String s) {
                        etPalletBarcode.setText(s);
                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
                scanBarcodeDialog.show(getFragmentManager(), "Scan barcode");
                break;
//            case R.id.sel_btn_code_order:
//                ScanBarcodeDialog scanBarcodeDialog2 = new ScanBarcodeDialog();
//                scanBarcodeDialog2.setBarcodeScanResult(new BarcodeScanResult() {
//                    @Override
//                    public void result(String s) {
//                        etOrderNumber.setText(s);
//                    }
//
//                    @Override
//                    public void error(Exception e) {
//
//                    }
//                });
//                scanBarcodeDialog2.show(getFragmentManager(), "Scan barcode");
//                break;
            case R.id.sel_btn_take:
                if (etPalletBarcode.getText().toString().isEmpty() &&
                        etAmount.getText().toString().isEmpty()) {
                    Toast.makeText(this, etPalletBarcode.getText().toString().isEmpty() + " " +
                            etAmount.getText().toString().isEmpty(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show();
                } else {
                    if (Integer.parseInt(etAmount.getText().toString()) > amount) {
                        Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show();
                    } else {
                        Map<String, String> params = new HashMap<>();
                        params.put("barcode", etPalletBarcode.getText().toString());
                        params.put("amount", ""+Integer.parseInt(etAmount.getText().toString()));

                        HttpSender.postRequest(this, "/container/takeProduct", new CallBackHttpSender() {
                            @Override
                            public void responseResult(String s) {
                                Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void error(VolleyError e) {

                            }
                        }, params);
                    }
                }
                break;
        }
    }
}
