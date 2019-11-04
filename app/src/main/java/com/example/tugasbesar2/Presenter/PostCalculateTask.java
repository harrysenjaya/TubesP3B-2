package com.example.tugasbesar2.Presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasbesar2.IMainActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostCalculateTask {
    protected final String BASE_URL = "http://p3b.labftis.net/api.php";
    protected IMainActivity ui;
    protected Context context;
    protected Gson gson;

    public PostCalculateTask(Context context, IMainActivity ui){
        this.context = context;
        this.ui = ui;
        this.gson = new Gson();
    }

    public void executeGET(final int npm){
        Log.d("SUKSES","GET");
        RequestQueue queue = Volley.newRequestQueue(context);
        String url =BASE_URL+"?api_key="+npm;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE",response);
                processResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","");
            }
        });

        queue.add(stringRequest);
    }


    public void executePOST(final int npm, final int order, final int value){
        Log.d("SUKSES","POST");
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SUKSES","");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","");
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("api_key",npm+"");
                params.put("order", order+"");
                params.put("value", value+"");

                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void processResult(String response){
        Log.d("RESPONSE",response + "1");
        int idxHighscore = response.indexOf("value");
        int lastindex = idxHighscore+8;
        int ctr = 0;
        while(true){
            if((int)response.charAt(lastindex+ctr)-48<=9 && (int)response.charAt(lastindex+ctr)-48>=0 ){
                ctr++;
            }
            else{
                break;
            }
        }
        String highscore= response.substring(lastindex,lastindex+ctr);
        this.ui.sendResult(highscore);
    }
}
