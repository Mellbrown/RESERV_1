package com.comma.cw01272.reservation.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cw012 on 2017-10-30.
 */

public class ReservationRequest extends StringRequest {
    final static private String URL = "http://cw01272.dothome.co.kr/Reservation.php";
    private Map<String, String> parameters;

    public ReservationRequest(String userID, String com_seq, String com_name, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("com_seq", com_seq);
        parameters.put("com_name", com_name);

    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
