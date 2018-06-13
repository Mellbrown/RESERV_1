package com.comma.cw01272.reservation.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cw012 on 2017-10-30.
 */

public class CancelReservationRequest extends StringRequest {
    final static private String URL = "http://cw01272.dothome.co.kr/CancelReservation.php";
    private Map<String, String> parameters;

    public CancelReservationRequest(String reservationSeq, String comSeq, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("reservationSeq", reservationSeq);
        parameters.put("comSeq", comSeq);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
