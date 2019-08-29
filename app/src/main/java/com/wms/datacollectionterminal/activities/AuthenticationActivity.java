package com.wms.datacollectionterminal.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wms.datacollectionterminal.R;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText login;
    EditText password;
    Button sing_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        sing_in = findViewById(R.id.btn_sing_in);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sing_in:
                Toast.makeText(this, login.getText() + " " + password.getText(), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
