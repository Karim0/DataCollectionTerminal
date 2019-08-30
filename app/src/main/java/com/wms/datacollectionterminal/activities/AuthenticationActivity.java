package com.wms.datacollectionterminal.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.wms.datacollectionterminal.R;
import com.wms.datacollectionterminal.helpers.CallBackHttpSender;
import com.wms.datacollectionterminal.helpers.HttpSender;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText login;
    EditText password;
    Button sing_in;
    boolean isRegistration = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        sing_in = findViewById(R.id.btn_sing_in);

        sing_in.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sing_in:
                sing_in.setEnabled(false);
                Map<String, String> params = new HashMap<>();
                params.put("username", login.getText().toString());
                params.put("password", password.getText().toString());

                HttpSender.postRequest(getBaseContext(), "/registration/isUserPresent", new CallBackHttpSender() {
                    @Override
                    public void responseResult(String s) {
                        sing_in.setEnabled(true);
                        if (s.equals("true")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", login.getText().toString());
                            editor.putString("password", password.getText().toString());
                            editor.apply();
                            finish();
                            isRegistration = true;
//                            Toast.makeText(getBaseContext(), "Sing in", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getBaseContext(), "Incorrect username or password", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void error(VolleyError e) {

                    }
                }, params);

//                Toast.makeText(this, login.getText() + " " + password.getText(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(!isRegistration){
            System.exit(1);
        }
        super.onDestroy();
    }
}
