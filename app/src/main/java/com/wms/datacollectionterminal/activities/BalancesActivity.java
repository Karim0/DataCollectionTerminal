package com.wms.datacollectionterminal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

public class BalancesActivity extends AppCompatActivity implements View.OnClickListener,
        TextWatcher {

    Spinner dropDownMenu;
    EditText etBarcode;
    ImageButton btnScanBarcode;
    TextView infoBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balances);

        dropDownMenu = findViewById(R.id.bl_dropdown_list);
        String[] items = new String[]{"Pallet", "Product", "Cell"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropDownMenu.setAdapter(adapter);

        etBarcode = findViewById(R.id.bl_barcode);

        btnScanBarcode = findViewById(R.id.bl_btn_barcode);
        infoBorder = findViewById(R.id.bl_infoBorder);

        etBarcode.addTextChangedListener(this);
        btnScanBarcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ScanBarcodeDialog scanBarcodeDialog = new ScanBarcodeDialog();
        scanBarcodeDialog.setBarcodeScanResult(new BarcodeScanResult() {
            @Override
            public void result(String s) {
                etBarcode.setText(s);
            }

            @Override
            public void error(Exception e) {

            }
        });

        scanBarcodeDialog.show(getFragmentManager(), "Balance");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Map<String, String> params = new HashMap<>();
        params.put("barcode", etBarcode.getText().toString());

        String url = "";

        switch (dropDownMenu.getSelectedItemPosition()) {
            case 0:
                url = "/container/getContainerByBarcode";
                break;
            case 1:
                url = "/product/getProductByBarcode";
                break;
            case 2:
                url = "/store/getCellBalance";
                break;
            default:
                break;
        }

        HttpSender.postRequest(this, url, new CallBackHttpSender() {
            @Override
            public void responseResult(String s) {
                if (s.equals("null")) {
                    infoBorder.setText("null");
                    return;
                }
                try {
                    switch (dropDownMenu.getSelectedItemPosition()) {
                        case 0:
                            JSONObject container = new JSONObject(s);
                            infoBorder.setText("Product: " + container.getJSONObject("product").getString("product_name") + "\n" +
                                    "amount: " + container.getDouble("amount") + " шт.\n");
                            break;
                        case 1:
                            JSONObject product = new JSONObject(s);
                            infoBorder.setText("Product: " + product.getString("product_name") + "\n" +
                                    "amount: " + product.getDouble("count_on_warehouse") + " шт.\n");
                            break;
                        case 2:
                            JSONArray cell = new JSONArray(s);
                            infoBorder.setText("");

                            for (int i = 0; i < cell.length(); i++) {
                                JSONObject object = cell.getJSONObject(i);

                                infoBorder.setText(infoBorder.getText().toString() +
                                        "Pallet № " + object.getLong("id") + "\n" +
                                        "Product: " + object.getJSONObject("product").getString("product_name") + "\n" +
                                        "amount: " + object.getDouble("amount") + " шт.\n");
                            }
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    Log.e("myLogs", e.getMessage());
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
