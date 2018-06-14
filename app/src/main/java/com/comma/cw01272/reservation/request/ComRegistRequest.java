package com.comma.cw01272.reservation.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cw012 on 2018-06-12.
 */

public class ComRegistRequest extends StringRequest {
    final static private String URL = "http://cw01272.dothome.co.kr/ComRegisterRequest.php";
    private Map<String, String> parameters = new HashMap<>();

    public ComRegistRequest(String name, String info, int vailableNum, int totalNum, String file_name, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters.put("name", name);
        parameters.put("info", info);
        parameters.put("vailableNum", vailableNum+"");
        parameters.put("totalNum", totalNum+"");
        parameters.put("file_name", file_name);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
