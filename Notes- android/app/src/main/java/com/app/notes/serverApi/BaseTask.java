package com.app.notes.serverApi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.app.notes.extras.RequestTags;

import org.apache.http.Consts;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("HandlerLeak")
public abstract class BaseTask<T> extends Request<T> implements RequestTags {

	protected MultipartEntityBuilder entity;

	protected Map<String, String> mBundle;

	protected String requestTag;
    protected String BOUNDARY;

	protected AppRequestListener requestlistener;

	protected JSONObject jsonResponse;

	protected String response;

	protected Handler mHandler;

	protected Map<String, String> headers;

	protected int statusCode;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setHeader(String title, String content) {
		headers.put(title, content);
	}

	public BaseTask(int method, String url, ErrorListener listener,
			String requestTag, AppRequestListener requestListener) {
		super(method, url, listener);
		setShouldCache(false);

		this.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 2));

		this.requestTag = requestTag;
		this.requestlistener = requestListener;
		setTag(requestTag);

	}

	public static String parseStatusCode(int code){
		switch (code){
			case 410:
				return "Invalid combination of email and password";
            case 405:
                return "Form validation failed";
			case 500:
				return "Internel server error: returned response 500";
            case 401:
                return "Data not received";
            case 402:
                return "Must send POST request";
            case 403:
                return "Token authentication failed";
            case 404:
                return "Page not found";
			default:
				return "Unknown status code";
		}
	}

	public BaseTask(int method, String url, ErrorListener listener) {
		super(method, url, listener);
		setShouldCache(false);
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mBundle;
	}

	public void setRequestlistener(AppRequestListener requestlistener) {
		this.requestlistener = requestlistener;
	}

	public AppRequestListener getRequestlistener() {
		return requestlistener;
	}

	public void setRequestTag(String requestTag) {
		this.requestTag = requestTag;
	}

	public String getRequestTag() {
		return this.requestTag;
	}

	protected Map<String, String> getmBundle() {
		return mBundle;
	}

	protected void setmBundle(Map<String, String> mBundle) {
		this.mBundle = mBundle;
	}

	public JSONObject getResponse() {
		return jsonResponse;
	}

	public void setResponse(JSONObject jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public void setStringResponse(String response) {
		this.response = response;
	}

	public String getStringResponse() {
		return this.response;
	}

	public void addStringBody(String... args)
			throws UnsupportedEncodingException {

		if(entity == null) {
			entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.setCharset(Consts.UTF_8);
            BOUNDARY = "----WebKitFormBoundary"+System.currentTimeMillis();
            entity.setBoundary(BOUNDARY);
		}
		int length = args.length;
		for (int index = 0; index < length; index += 2) {

			if (args[index] != null && args[index + 1] != null && !args[index+1].isEmpty()) {

					Log.i("addStringBody:"+args[index], args[index+1]);
				entity.addPart(args[index], new StringBody(args[index + 1]));
			}

		}

	}
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		if(headers == null)
			headers = new HashMap<>();
		headers.put("Content-Type","multipart/form-data; boundary=" + BOUNDARY + "; charset=utf-8");
		return headers;
	}

	public void addHeader(String key, String val){
		if(headers == null)
			headers = new HashMap<>();
		Log.i("Headder:"+key, val);
		headers.put(key, val);
	}
	public void addFileBody(String key, File file){
		if(entity == null) {
			entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.setCharset(Consts.UTF_8);
            BOUNDARY = "----WebKitFormBoundary"+System.currentTimeMillis();
            entity.setBoundary(BOUNDARY);
		}

			Log.i("addFileBody:"+key, file.getAbsolutePath());
		entity.addPart(key, new FileBody(file));
	}

	public void addFileBodyArray(String key, File[] files){
		if(entity == null){
			entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.setCharset(Consts.UTF_8);
			BOUNDARY = "----WebKitFormBoundary"+System.currentTimeMillis();
			entity.setBoundary(BOUNDARY);
		}

			Log.i("addFileBOdyArray:"+key, files[0].getAbsolutePath());
	}

	protected void addStringBody(String name, byte[]... args)
			throws UnsupportedEncodingException {
		if(entity == null) {
			entity = MultipartEntityBuilder.create();
			entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		}

		int length = args.length;
		for (int index = 0; index < length; index += 1) {

			if (args[index] != null)
				entity.addPart(name + index, new ByteArrayBody(args[index],
						"image/jpeg", System.currentTimeMillis() + ".jpg"));

		}

	}

	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {

		NetworkResponse res = volleyError.networkResponse;
		try {
			System.out.println(" Error : " + res);
			if (res != null) {
				System.out.println(" Code " + res.statusCode);
				setStatusCode(res.statusCode);
				System.out.println(" Error : " + new String(res.data));
			}

			if (res != null && res.data != null) {
				JSONObject jsonResponse = new JSONObject(new String(res.data));
				setResponse(jsonResponse);
				setStatusCode(res.statusCode);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	/*	this.deliverError(volleyError);
        if(requestlistener != null)
            requestlistener.onRequestFailed(this);*/
		return super.parseNetworkError(volleyError);
	}

	protected void sendMessage() {

		mHandler = new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				android.os.Process
						.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
				deliverresponse();
			}

		};
		mHandler.sendEmptyMessage(0);

	}

	protected void deliverresponse() {
		if (requestlistener != null)
			requestlistener.onRequestCompleted(this);
	}

	public void finish() {

	}

	public abstract void processData();

	public void send(Context context) {
		RequestQueue manager = RequestManager.getInstance(context);
		manager.add(this);
		this.requestlistener.onRequestStarted(this);
	}

}