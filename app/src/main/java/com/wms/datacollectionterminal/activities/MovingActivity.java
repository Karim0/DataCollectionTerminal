package com.wms.datacollectionterminal.activities;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.common.api.internal.TaskUtil;
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

public class MovingActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    EditText etBarcodePallet;
    EditText etBarcodeCell;
    TextView infoBorder;
    TextView prPallet;
    TextView prCell;
    ImageButton scanPallet;
    ImageButton scanCell;
    Button move;
    ContentLoadingProgressBar progressBar;
    ContentLoadingProgressBar progressBar2;

    String barcodeCell = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving);

        etBarcodePallet = findViewById(R.id.mv_ed_code_pallet);
        etBarcodePallet.addTextChangedListener(this);
        etBarcodeCell = findViewById(R.id.mv_ed_code_cell);

        infoBorder = findViewById(R.id.mv_infoBorder);
        prPallet = findViewById(R.id.mv_name_product);
        prCell = findViewById(R.id.mv_name_cell);
        scanPallet = findViewById(R.id.mv_btn_code_pallet);
        scanCell = findViewById(R.id.mv_btn_code_cell);
        progressBar = findViewById(R.id.mv_pr_pallet);
        progressBar2 = findViewById(R.id.mv_pr_cell);
        move = findViewById(R.id.mv_btn_move);

        scanPallet.setOnClickListener(this);
        scanCell.setOnClickListener(this);
        move.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mv_btn_code_pallet:
                ScanBarcodeDialog scanBarcodeDialog = new ScanBarcodeDialog();
                scanBarcodeDialog.setBarcodeScanResult(new BarcodeScanResult() {
                    @Override
                    public void result(String s) {
                        etBarcodePallet.setText(s);
                    }

                    @Override
                    public void error(Exception e) {
                    }
                });
                scanBarcodeDialog.show(getFragmentManager(), "Scan pallet");
                break;
            case R.id.mv_btn_code_cell:
                ScanBarcodeDialog scanBarcodeDialog1 = new ScanBarcodeDialog();
                scanBarcodeDialog1.setBarcodeScanResult(new BarcodeScanResult() {
                    @Override
                    public void result(String s) {
                        etBarcodeCell.setText(s);
                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
                scanBarcodeDialog1.show(getFragmentManager(), "Scan cell");
                break;
            case R.id.mv_btn_move:
                if (!TextUtils.isEmpty(etBarcodeCell.getText()) && !TextUtils.isEmpty(etBarcodePallet.getText())) {
                    if (!etBarcodeCell.getText().toString().equals(barcodeCell)) {
                        Toast.makeText(this, "Адрес ячейки не совпадает", Toast.LENGTH_LONG).show();
                    } else {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_container", etBarcodePallet.getText().toString());
                        params.put("id_stillage", etBarcodeCell.getText().toString());

                        HttpSender.postRequest(this, "/store/putOnShelf", new CallBackHttpSender() {
                            @Override
                            public void responseResult(String s) {
                                infoBorder.setText(s);
                            }

                            @Override
                            public void error(VolleyError e) {

                            }
                        }, params);
                    }
                } else {
                    Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        progressBar.show();
        Map<String, String> params = new HashMap<>();
        params.put("barcode", etBarcodePallet.getText().toString());

        HttpSender.postRequest(this, "/container/getContainerByBarcode", new CallBackHttpSender() {
            @Override
            public void responseResult(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject cell = object.getJSONObject("cellId");

                    barcodeCell = cell.getString("bar_code");
                    prPallet.setText(MessageFormat.format("{0}: {1}шт.", object.getJSONObject("product").getString("product_name"), object.getInt("amount")));
                    if (object.getString("lifeCycle").equals("receipt")) {
                        infoBorder.setText(MessageFormat.format("Переместить паллет в ячейку {0}-{1}-{2}", cell.getString("stillage"), cell.getString("shelf"), cell.getString("cell")));
                    } else if (object.getString("lifeCycle").equals("store")) {
                        infoBorder.setText("Паллет уже находится в своей ячейке");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressBar.hide();
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
