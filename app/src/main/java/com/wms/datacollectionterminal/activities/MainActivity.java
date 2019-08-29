package com.wms.datacollectionterminal.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wms.datacollectionterminal.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_acceptance;
    Button btn_balances;
//    Button btn_inventory;
//    Button btn_moving;
    Button btn_selection;
//    Button btn_сhoose_product;
    Button btn_search;
//    Button btn_returns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_acceptance = findViewById(R.id.btn_acceptance);
        btn_balances = findViewById(R.id.btn_balances);
//        btn_inventory = findViewById(R.id.btn_inventory);
//        btn_moving = findViewById(R.id.btn_moving);
//        btn_returns = findViewById(R.id.btn_returns);
        btn_selection = findViewById(R.id.btn_selection);
//        btn_сhoose_product = findViewById(R.id.btn_сhoose_product);
        btn_search = findViewById(R.id.btn_search);

        btn_acceptance.setOnClickListener(this);
        btn_balances.setOnClickListener(this);
//        btn_inventory.setOnClickListener(this);
//        btn_moving.setOnClickListener(this);
//        btn_returns.setOnClickListener(this);
        btn_selection.setOnClickListener(this);
//        btn_сhoose_product.setOnClickListener(this);
        btn_search.setOnClickListener(this);

//        btn_inventory.setVisibility(View.INVISIBLE);
//        btn_moving.setVisibility(View.INVISIBLE);
//        btn_returns.setVisibility(View.INVISIBLE);
//        btn_сhoose_product.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_acceptance:
                startActivity(new Intent(this, AcceptanceActivity.class));
                break;
            case R.id.btn_balances:
                startActivity(new Intent(this, BalancesActivity.class));
                break;
//            case R.id.btn_inventory:
//                startActivity(new Intent(this, InventoryActivity.class));
//                break;
//            case R.id.btn_moving:
//                startActivity(new Intent(this, MovingActivity.class));
//                break;
//            case R.id.btn_returns:
//                startActivity(new Intent(this, ReturnsActivity.class));
//                break;
            case R.id.btn_selection:
                startActivity(new Intent(this, SelectionActivity.class));
                break;
//            case R.id.btn_сhoose_product:
//                startActivity(new Intent(this, ChooseProductActivity.class));
//                break;
            case R.id.btn_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;

        }
    }
}