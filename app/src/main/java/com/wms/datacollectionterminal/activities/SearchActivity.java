package com.wms.datacollectionterminal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.wms.datacollectionterminal.R;
import com.wms.datacollectionterminal.dialogs.ScanBarcodeDialog;
import com.wms.datacollectionterminal.dialogs.BarcodeScanResult;
import com.wms.datacollectionterminal.helpers.CallBackHttpSender;
import com.wms.datacollectionterminal.helpers.HttpSender;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,
        TextWatcher {

    EditText barcode;
    ImageButton scanBarcode;
    TextView infoBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        barcode = findViewById(R.id.se_et_barcode_product);
        scanBarcode = findViewById(R.id.se_btn_barcode);
        infoBorder = findViewById(R.id.se_infoBorder);

        scanBarcode.setOnClickListener(this);
        barcode.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        ScanBarcodeDialog scanBarcodeDialog = new ScanBarcodeDialog();
        scanBarcodeDialog.setBarcodeScanResult(new BarcodeScanResult() {
            @Override
            public void result(String s) {
                barcode.setText(s);
            }

            @Override
            public void error(Exception e) {

            }
        });

        scanBarcodeDialog.show(getFragmentManager(), "Search");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        infoBorder.setText("");
        Map<String, String> params = new HashMap<>();
        params.put("bar_code", barcode.getText().toString());

        HttpSender.postRequest(getApplicationContext(), "/store/findCellsByProduct", new CallBackHttpSender() {
            @Override
            public void responseResult(String s) {
                try {
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        JSONObject cell = jsonObject.getJSONObject("cellId");
                        infoBorder.setText(MessageFormat.format("{0}адрес ячейки {1}-{2}-{3}: {4} шт.\n", infoBorder.getText(), cell.getInt("stillage"), cell.getInt("shelf"), cell.getInt("cell"), jsonObject.getInt("amount")));
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
}
