package com.example.loginaprm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Password;
    private TextView LoginAttempt;
    private Button Login;
    private int counter = 2;
    private RequestQueue mQueue;
    private static final String TAG = "MainActivity";
    String Token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Username = findViewById(R.id.Username);
        Password = findViewById(R.id.Password);
        LoginAttempt = findViewById(R.id.LoginAttempt);
        Login = findViewById(R.id.Login);
        LoginAttempt.setText("No Of Attempts Remaining: 2");
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueue = Volley.newRequestQueue(MainActivity.this);
                jsonParse(Username.getText().toString(),Password.getText().toString());
            }
        });
    }
    private void jsonParse(String email, String password) {
        final String url = " http://10.232.119.132:51360/aprm-portal/Login";
        final HashMap<String, String> KeyVal = new HashMap<String, String>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("user", email);
        params.put("password", password);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("value of token is ", response.toString());
                        VolleyLog.wtf(response.toString(), "utf-8");
                        Intent i = new Intent(MainActivity.this,LoginActivity.class);
                        i.putExtra("email",Username.getText().toString());
                        i.putExtra("password",Password.getText().toString());
                        i.putExtra("Token",Token);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "The username or password is incorrect", Toast.LENGTH_SHORT).show();
                counter--;
                LoginAttempt.setText("No Of Attempts Remaining: " + String.valueOf(counter));
                if(counter == 0){
                    Login.setEnabled(false);
                }
                Log.e("Password is missing","Some Error Naveen Babu");
                Log.e("onErrorResponse", error.toString());
                error.printStackTrace();
                try {
                    String response = new String(
                            error.networkResponse.data,
                            HttpHeaderParser.parseCharset(error.networkResponse.headers));

                    Log.e(TAG,error.networkResponse.allHeaders.toString());
                    Log.e(TAG,response);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG,e.getMessage());
                }
            }
        }) {
            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                try {
                    List<Header> headers = response.allHeaders;
                    for(Header header: headers){
                        Log.e(TAG,"Name " + header.getName() + " Value " + header.getValue());
                        if(header.getName().equals("uxfauthorization"))
                        {
                            Token = header.getValue();
                            Log.e(TAG,"Name " + header.getName() + " Value " + header.getValue());
                        }
                    }
                    String json = new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(
                            new JSONObject(json),
                            HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        mQueue.add(request);
    }
}