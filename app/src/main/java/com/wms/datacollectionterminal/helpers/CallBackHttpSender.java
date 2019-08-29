package com.wms.datacollectionterminal.helpers;

import com.android.volley.VolleyError;

public interface CallBackHttpSender {
    void responseResult(String s);
    void error(VolleyError e);
}
