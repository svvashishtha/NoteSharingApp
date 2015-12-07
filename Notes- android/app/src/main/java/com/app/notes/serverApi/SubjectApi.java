package com.app.notes.serverApi;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.app.notes.baseObjects.SubjectObject;
import com.app.notes.utilities.JSONUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saurabh on 05-11-2015.
 */
public class SubjectApi extends BaseTask<JSONArray> {
    JSONArray response;

    public SubjectApi(int method, String url, Response.ErrorListener listener, String requestTag,
                      AppRequestListener requestListener) throws JSONException {
        super(method, url, listener, requestTag, requestListener);
        headers = new HashMap<>();

    }

    ArrayList<SubjectObject> subjects;

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void processData() {
        if (subjects == null)
            subjects = new ArrayList<>();
        Gson gson = new Gson();
        Log.i("SubjectApi", response.toString());
        for (int i = 0; i < response.length(); i++)
            try {
                subjects.add(gson.fromJson(response.get(i).toString(),SubjectObject.class));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        sendMessage();
    }

    public ArrayList<SubjectObject> getSubjects() {
        return subjects;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
        return Response.success(
                JSONUtils.getJSONArray(new String(networkResponse.data)),
                getCacheEntry());

    }

    @Override
    protected void deliverResponse(JSONArray jsonArray) {
        this.response = jsonArray;
        RequestPoolManager.getInstance().executeRequest(this);
    }
}
