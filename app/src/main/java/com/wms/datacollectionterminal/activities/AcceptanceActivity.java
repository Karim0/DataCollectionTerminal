package com.wms.datacollectionterminal.activities;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
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
import com.wms.datacollectionterminal.dialogs.ScanBarcodeDialog;
import com.wms.datacollectionterminal.dialogs.BarcodeScanResult;
import com.wms.datacollectionterminal.helpers.CallBackHttpSender;
import com.wms.datacollectionterminal.R;
import com.wms.datacollectionterminal.helpers.HttpSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AcceptanceActivity extends AppCompatActivity implements View.OnClickListener,
        CallBackHttpSender {

    EditText barcodePallet;
    EditText barcodeProduct;
    EditText amountProduct;
    ImageButton btnScanPallet;
    ImageButton btnScanProduct;
    Button btnAdd;
    TextView infoBorder;
    TextView tvProductName;
    ContentLoadingProgressBar progressBar;


    JSONObject product;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance);

        barcodePallet = findViewById(R.id.ac_ed_code_pallet);
        barcodeProduct = findViewById(R.id.ac_ed_code_product);
        barcodeProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changedText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        amountProduct = findViewById(R.id.ac_ed_amount_product);
        amountProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateInformation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnScanPallet = findViewById(R.id.ac_btn_code_pallet);
        btnScanProduct = findViewById(R.id.ac_btn_code_product);

        btnAdd = findViewById(R.id.ac_btn_add);

        infoBorder = findViewById(R.id.ac_infoBorder);
        tvProductName = findViewById(R.id.ac_name_product);
        progressBar = findViewById(R.id.ac_pr_product);

        btnScanPallet.setOnClickListener(this);
        btnScanProduct.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac_btn_add:
                if (TextUtils.isEmpty(barcodePallet.getText()) &&
                        TextUtils.isEmpty(barcodeProduct.getText()) &&
                        TextUtils.isEmpty(amountProduct.getText())) {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, String> params = new HashMap<>();
                params.put("amount", amountProduct.getText().toString());
                params.put("bar_code_pallet", barcodePallet.getText().toString());
                params.put("bar_code_product", barcodeProduct.getText().toString());
                HttpSender.postRequest(this, "/receipt/addOneProductForPalletByCode",
                        new CallBackHttpSender() {
                            @Override
                            public void responseResult(String s) {
                                Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void error(VolleyError e) {
                                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }, params);
                break;
            case R.id.ac_btn_code_pallet:
                ScanBarcodeDialog scanBarcodeDialog = new ScanBarcodeDialog();
                scanBarcodeDialog.setBarcodeScanResult(new BarcodeScanResult() {
                    @Override
                    public void result(String s) {
                        barcodePallet.setText(s);
                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
                scanBarcodeDialog.show(getFragmentManager(), "ac_btn_code_pallet");
                break;
            case R.id.ac_btn_code_product:
                ScanBarcodeDialog dialog_code_product = new ScanBarcodeDialog();
                dialog_code_product.setBarcodeScanResult(new BarcodeScanResult() {
                    @Override
                    public void result(String s) {
                        barcodeProduct.setText(s);
                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
                dialog_code_product.show(getFragmentManager(), "ac_btn_code_product");
                break;
        }
    }

    private void changedText() {
        progressBar.show();

        Map<String, String> params = new HashMap<>();
        params.put("barcode", barcodeProduct.getText().toString());
        HttpSender.postRequest(this, "/product/getProductByBarcode", new CallBackHttpSender() {
            @Override
            public void responseResult(String s) {
                progressBar.hide();
                if (s.equals("null")) {
                    tvProductName.setText("null");
                    return;
                }
                try {
                    product = new JSONObject(s);
                    tvProductName.setText(product.getString("product_name"));
                    calculateInformation();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(VolleyError e) {
                progressBar.hide();
                tvProductName.setText(e.toString());
            }
        }, params);
    }

    private void calculateInformation() {
        if (product == null || amountProduct.getText().toString().isEmpty()) return;

        int amount = Integer.parseInt(amountProduct.getText().toString());
        try {
            infoBorder.setText("Вес паллета " + (product.getDouble("weight") + amount) + "кг\n" +
                    "Объем паллета " + (product.getDouble("width") *
                    product.getDouble("length") * product.getDouble("height") * amount) + " м3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void responseResult(String s) {
        infoBorder.setText(s);
    }

    @Override
    public void error(VolleyError e) {
        infoBorder.setText(e.toString());
    }
}
