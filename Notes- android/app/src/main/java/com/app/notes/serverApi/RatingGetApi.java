package com.app.notes.serverApi;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.app.notes.extras.AppConstants;
import com.app.notes.utilities.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saurabh on 23-11-2015.
 */
public class RatingGetApi extends BaseTask<JSONObject> implements AppConstants {
    public RatingGetApi(int method, String url, Response.ErrorListener listener,
                        String requestTag, AppRequestListener requestListener) {
        super(method, url, listener, requestTag, requestListener);
        headers = new HashMap<>();
    }

    float rating;

    @Override
    public void processData() {
        if (jsonResponse != null) {
            try {
                rating = (float) jsonResponse.getDouble("Rating");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        sendMessage();
    }

    public float getRating() {
        return rating;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(
                JSONUtils.getJSONObject(new String(networkResponse.data)),
                getCacheEntry());
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        this.setResponse(jsonObject);
        RequestPoolManager.getInstance().executeRequest(this);
    }
}
