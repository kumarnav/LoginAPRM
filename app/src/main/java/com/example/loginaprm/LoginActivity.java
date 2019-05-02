package com.example.loginaprm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private Button logout;
    private TextView ShowUsername;
    private Button Contracts;
    static private int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logout = findViewById(R.id.logout);
        ShowUsername = findViewById(R.id.ShowUser);
        Contracts = findViewById(R.id.Contracts);
        Intent intent = getIntent();
        String Username = intent.getStringExtra("email");
        final String Token = intent.getStringExtra("Token");
        ShowUsername.append(Username);
        Contracts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ContractListIntent = new Intent(LoginActivity.this, ContractList.class);
                ContractListIntent.putExtra("Token", Token);
                ContractListIntent.putExtras(getIntent());
                startActivity(ContractListIntent);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}