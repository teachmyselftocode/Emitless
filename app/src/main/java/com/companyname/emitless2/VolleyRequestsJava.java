package com.companyname.emitless2;


import android.app.Fragment;
import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

// Contains callback method in order to
public class VolleyRequestsJava {

    //Just a constructor for the context to pass Main Activity as this class is on another file.
    private Context ctx;
    public VolleyRequestsJava(Context mContext) {
        this.ctx = mContext;
    }

    public void yourFunctionForVolleyRequest(final ServerCallbackJava callback, final String url) {

        RequestQueue queue = Volley.newRequestQueue(ctx);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response); // call call back function here
                        //TODO add flag here e.g. isCompleted = true
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("Volley error json object ", "Error: " + error.getMessage());
            }
        })

        {
            @Override
            public String getBodyContentType ()
            {
                return "application/json";
            }
        };
        // Adding request to request queue
        queue.add(jsonObjReq);
    }
}
