package com.app.notes.serverApi;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.app.notes.baseObjects.UserObject;
import com.app.notes.extras.AppConstants;
import com.app.notes.utilities.JSONUtils;
import com.google.gson.Gson;

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
 * Created by Saurabh on 16-11-2015.
 */
public class SignUpApi extends BaseTask<JSONObject> implements AppConstants {
    HttpEntity entity;
    String statusString = null;
    private UserObject userObject;

    public SignUpApi(int method, String url, Response.ErrorListener listener,
                     String requestTag, AppRequestListener requestListener, String userName, String password, String email,
                     String firstName, String lastName, String rollNo) {
        super(method, url, listener, requestTag, requestListener);
        headers = new HashMap<String, String>();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserName", userName);
            jsonObject.put("Password", password);
            jsonObject.put("RollNo", rollNo);
            jsonObject.put("FirstName", firstName);
            jsonObject.put("LastName", lastName);
            jsonObject.put("Email", email);
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
        if (jsonResponse != null) {
            try {
                if (jsonResponse.getInt("status") == 0) {
                    statusString = EMAIL_EXISTS;

                } else if (jsonResponse.getInt("status") == 1) {
                    statusString = USERNAME_EXISTS;
                } else if (jsonResponse.getInt("status") == 2)
                    userObject = new Gson().fromJson(jsonResponse.toString(), UserObject.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sendMessage();
    }

    public String getStatusString() {
        return statusString;
    }

    public UserObject getUserObject() {
        return userObject;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        statusCode = networkResponse.statusCode;
        String responseString = new String(networkResponse.data);
        Log.i("SignUpApi", "response:" + responseString);
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
