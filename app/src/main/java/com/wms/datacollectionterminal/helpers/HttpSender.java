package com.wms.datacollectionterminal.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class HttpSender {

    private static String baseURl = "http://23.111.206.237:8090";

    private static String name = "settings";

    public static void getRequest(Context context, String url, final CallBackHttpSender callBackHttpSender) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        final SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseURl + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        callBackHttpSender.responseResult(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callBackHttpSender.error(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String username = sharedPreferences.getString("username", ""),
                        password = sharedPreferences.getString("password", "");

                if(!username.equals("") && !password.equals("")){
                    String key = "Authorization";
                    String encodedString = Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
                    String value = String.format("Basic %s", encodedString);
                    map.put(key, value);
                }
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void postRequest(Context context, String url,
                                   final CallBackHttpSender callBackHttpSender,
                                   final Map<String, String> params) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        final SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseURl + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("test", response);
                        callBackHttpSender.responseResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.i("test", error.getMessage());
                callBackHttpSender.error(error);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String username = sharedPreferences.getString("username", ""),
                        password = sharedPreferences.getString("password", "");

                if(!username.equals("") && !password.equals("")){
                    String key = "Authorization";
                    String encodedString = Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), Base64.NO_WRAP);
                    String value = String.format("Basic %s", encodedString);
                    map.put(key, value);
                }
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
