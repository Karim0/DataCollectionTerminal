package com.wms.datacollectionterminal.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.wms.datacollectionterminal.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_acceptance;
    Button btn_balances;
    Button btn_moving;
    Button btn_selection;
    Button btn_search;
    Button btn_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_acceptance = findViewById(R.id.btn_acceptance);
        btn_balances = findViewById(R.id.btn_balances);
        btn_moving = findViewById(R.id.btn_moving);
        btn_selection = findViewById(R.id.btn_selection);
        btn_search = findViewById(R.id.btn_search);
        btn_product = findViewById(R.id.btn_product);

        btn_acceptance.setOnClickListener(this);
        btn_balances.setOnClickListener(this);
        btn_moving.setOnClickListener(this);
        btn_selection.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_product.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("username", "").equals("")) {
            startActivity(new Intent(this, AuthenticationActivity.class));
        }
    }

    @SuppressLint("NonConstantResourceId")
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
            case R.id.btn_moving:
                startActivity(new Intent(this, MovingActivity.class));
                break;
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

            case R.id.btn_product:
                startActivity(new Intent(this, ProductsActivity.class));
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu_logout:
                SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("username");
                editor.remove("password");
                editor.apply();
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
