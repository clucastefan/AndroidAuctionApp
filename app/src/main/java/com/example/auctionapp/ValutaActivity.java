package com.example.auctionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.JarInputStream;

public class ValutaActivity extends AppCompatActivity {

    private TextView textView;
    private RequestQueue requestQueue;
    private Button btnParse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuta);
        textView = findViewById(R.id.ios_tv_valuta);
        Button btnParse = findViewById(R.id.ios_btn_parse);
        requestQueue = Volley.newRequestQueue(this);
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseJson();
            }
        });
    }

    private void parseJson() {
        String url = "https://jsonkeeper.com/b/LCNB";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("exchange");
                    for( int i = 0; i<jsonArray.length(); i++){
                        parse(jsonArray, i);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    private void parse(JSONArray jsonArray, int i) throws JSONException {
        JSONObject exchange = jsonArray.getJSONObject(i);
        String valuta = exchange.getString("valuta");
        double valoare = exchange.getDouble("valoare");
        String moneda = exchange.getString("moneda");

        textView.append("1 "+valuta + " = "+String.valueOf(valoare)+" "+moneda + "\n\n");
    }
}