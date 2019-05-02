package com.example.loginaprm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContractList extends AppCompatActivity {
    private TextView ContractList;
    private RequestQueue mQueue;
    private Button Back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_list);
        mQueue = Volley.newRequestQueue(this);
        ArrayAdapter<String> ArralyAdpt;
        ContractList = findViewById(R.id.ContractLists);
        Intent intent = getIntent();
        String Token  = intent.getStringExtra("Token");
        GetContractListJsonParse(Token);
        Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ContractList.this, LoginActivity.class);
                intent1.putExtras(getIntent());
                startActivity(intent1);
                finish();
            }
        });
    }
    private void GetContractListJsonParse(final String Token){
        String url = "http://10.232.119.132:51360/aprm-portal/9/contracts/";
        final HashMap<String, String> KeyVal = new HashMap<String, String>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("authorization", Token);
        Log.d("Token Pssed is: ", Token);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("list");
                            for (int i = 0 ; i < jsonArray.length(); i++) {
                                JSONObject list = jsonArray.getJSONObject(i);
                                String code = list.getString("code");
                                Log.d("Code is: ", code);
                                String name = list.getString("name");
                                String startDate = list.getString("startDate");
                                String customerCode = list.getString("customerCode");
                                String customerName = list.getString("customerName");
                                String contractTmplCode = list.getString("contractTmplCode");
                                String businessUnitCode = list.getString("businessUnitCode");
                                String type = list.getString("type");
                                String statusInd = list.getString("statusInd");
                                String statusName = list.getString("statusName");
                                String reasonCode = list.getString("reasonCode");
                                String reasonDesc = list.getString("reasonDesc");
                                ContractList.append(code +  ", " + name + ", " + startDate + "\n\n" );
                            }
                            int count = response.getInt("totalRecords");
                            Log.d("count Value ", Integer.toString(count));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", Token);
                return headers;
            };
        };
        mQueue.add(request);
    }

}