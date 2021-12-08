package com.wms.datacollectionterminal.helpers;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface CallBackHttpSender {
    void responseResult(String s);
    default void responseResult(JSONObject json){}
    void error(VolleyError e);
}
