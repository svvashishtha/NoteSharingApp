package com.app.notes.serverApi;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.app.notes.baseObjects.NotesObject;
import com.app.notes.utilities.JSONUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saurabh on 12-11-2015.
 */
public class GetNotesApi extends BaseTask<JSONArray> {

    JSONArray response;

    public GetNotesApi(int method, String url, Response.ErrorListener listener,
                       String requestTag, AppRequestListener requestListener) {
        super(method, url, listener, requestTag, requestListener);
        headers = new HashMap<>();
    }

    ArrayList<NotesObject> notesObjectArrayList;

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    public void processData() {
        if (response != null) {
            if (notesObjectArrayList == null)
                notesObjectArrayList = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < response.length(); i++)
                try {
                    notesObjectArrayList.add(gson.fromJson(response.get(i).toString(), NotesObject.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        sendMessage();
    }

    public ArrayList<NotesObject> getNotesObjectList() {
        return notesObjectArrayList;
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
