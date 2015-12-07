package com.app.notes.serverApi;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.app.notes.extras.AppConstants;
import com.app.notes.utilities.JSONUtils;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saurabh on 21-11-2015.
 */
public class RatingApi extends BaseTask<JSONObject> implements AppConstants {
    HttpEntity entity;
    boolean status;

    public RatingApi(int method, String url, Response.ErrorListener listener, String requestTag,
                     AppRequestListener requestListener, String userid, String pdfId, float rating) {
        super(method, url, listener, requestTag, requestListener);
        headers = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", userid);
            jsonObject.put("PdfId", pdfId);
            jsonObject.put("Rating", rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            entity = new StringEntity(jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processData() {
        try {
            if (jsonResponse.getBoolean("status")) {
                status = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage();
    }

    public boolean getStatus() {
        return status;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        statusCode = networkResponse.statusCode;
        String responseString = new String(networkResponse.data);
        Log.i("RatingApi", "response:" + responseString);
        return Response.success(
                JSONUtils.getJSONObject(responseString),
                getCacheEntry());
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        this.setResponse(jsonObject);
        RequestPoolManager.getInstance().executeRequest(this);
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();

    }
}
