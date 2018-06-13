package com.comma.cw01272.reservation.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cw012 on 2017-10-30.
 */

public class CheckReservationRequest extends StringRequest {
    final static private String URL = "http://cw01272.dothome.co.kr/MyReservation.php";
    private Map<String, String> parameters;

    public CheckReservationRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
