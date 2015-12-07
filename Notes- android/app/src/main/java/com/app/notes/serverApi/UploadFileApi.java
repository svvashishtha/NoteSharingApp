package com.app.notes.serverApi;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.app.notes.extras.AppConstants;
import com.app.notes.utilities.JSONUtils;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saurabh on 18-11-2015.
 */
public class UploadFileApi extends BaseTask<JSONObject> implements AppConstants {
    private MultipartEntity entity;

    public UploadFileApi(int method, String url, Response.ErrorListener listener, String requestTag,
                         AppRequestListener requestListener, File file) {
        super(method, url, listener, requestTag, requestListener);
        headers = new HashMap<String, String>();
        entity = new MultipartEntity();
        buildMultipartEntity(file);
    }

    private void buildMultipartEntity(File file) {
        entity.addPart("pdf", new FileBody(file));
        /*for adding text part to the entity
        try
        {
			entity.addPart(STRING_PART_NAME, new StringBody(mStringPart));
		}
		catch (UnsupportedEncodingException e)
		{
			VolleyLog.e("UnsupportedEncodingException");
		}*/
    }

    @Override
    public void processData() {
        sendMessage();
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        statusCode = networkResponse.statusCode;
        String responseString = new String(networkResponse.data);
        Log.i("UploadFileApi", "response:" + responseString);
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
        return entity.getContentType().getValue();
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
