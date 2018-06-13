package com.comma.cw01272.reservation.request;


import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cw012 on 2017-10-30.
 */

public class UserModifyRequest extends StringRequest {
    final static private String URL = "http://cw01272.dothome.co.kr/UserModify.php";
    private Map<String, String> parameters;

    public UserModifyRequest(String userID, String userEMAIL, String userPASS, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userEMAIL", userEMAIL);
        parameters.put("userPASS", userPASS);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
