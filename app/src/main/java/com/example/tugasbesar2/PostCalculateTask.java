package com.example.tugasbesar2;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class PostCalculateTask {
    final String BASE_URL = "http://p3b.labftis.net/api.php";
    IMainActivity ui;
    Context context;
    Gson gson;

    public PostCalculateTask(Context context, IMainActivity ui){
        this.context = context;
        this.ui = ui;
        this.gson = new Gson();
    }

    public void execute(String[] expr, int precision) throws JSONException{
        Input input = new Input(expr,precision);
        String inputJSON = this.gson.toJson(input);
        JSONObject json = new JSONObject(inputJSON);
        this.callVoley(json);
    }

    public void callVoley(JSONObject json){
        RequestQueue queue = Volley.newRequestQueue(this.context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, this.BASE_URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                processResult(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "");
            }
        });
    }

    public void processResult(String json){
        Result res = this.gson.fromJson(json, Result.class);
        this.ui.sendResult(res);
    }
}
