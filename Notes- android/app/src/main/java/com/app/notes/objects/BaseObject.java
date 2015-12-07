package com.app.notes.objects;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.app.notes.extras.RequestTags;
import com.app.notes.serverApi.AppRequestListener;
import com.app.notes.serverApi.BaseTask;
import com.app.notes.serverApi.RequestManager;
import com.app.notes.utilities.JSONUtils;

import org.json.JSONObject;

public abstract class BaseObject implements RequestTags {
	public static final String SELECTION = "selection";
	public static final String SELECTIONVALUE = "selectionargs";
	public static final String SORT_BY = "sortby";
	public static final String DESC_SORT = "DESC";
	public static final String ASC_SORT = "ASC";

	public static final String DEFAULT_LOADCOUNT = "20";
	public static final String NEXT_DATA = "1";
	public static final String OLD_DATA = "0";

	public abstract void clear(Context context);

	public boolean isIDExist(Context context, Uri uri, String coloum_ID,
			String coloum_value) {
		boolean isExistTrue;
		Cursor localCursor = context.getContentResolver().query(uri,
				new String[] { coloum_ID }, coloum_ID + " = ?",
				new String[] { coloum_value }, null);
		isExistTrue = isExist(localCursor);
		localCursor.close();
		return isExistTrue;

	}

	private boolean isExist(Cursor localCursor) {

		if (localCursor != null && localCursor.moveToFirst())
			return true;
		else
			return false;

	}

	public static String lastQueryTime(JSONObject response) {
		return JSONUtils.getStringfromJSON(response, "modification_datetime");
	}

	/*protected String enodeURL(String... argv) {

		String url = argv[0];
		List<NameValuePair> urlList = new LinkedList<NameValuePair>();
		int length = argv.length;
		for (int index = 1; index < length; index += 2) {
			if (argv[index] != null && argv[index + 1] != null)
				urlList.add(new BasicNameValuePair(argv[index], argv[index + 1]));
		}

		return (url += URLEncodedUtils.format(urlList, "utf-8"));

	}
*/
	public class AppNetworkError implements ErrorListener {

		BaseTask<?> task;
		AppRequestListener requestListener;

		public void setRequestListener(BaseTask<?> task,
				AppRequestListener requestListener) {
			this.requestListener = requestListener;
			this.task = task;
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			System.out.println("Request failed :: " + task.getStatusCode());
			requestListener.onRequestFailed(task);
		}

	}

	public void sendRequest(Context context, AppNetworkError error,
							BaseTask<?> request, AppRequestListener apprequestlistener) {
		RequestQueue manager = RequestManager.getInstance(context);
		manager.add(request);
		error.setRequestListener(request, apprequestlistener);
		apprequestlistener.onRequestStarted(request);
	}

	public void sendRequest(Context context, BaseTask<?> request, AppRequestListener appRequestListener){
		RequestQueue manager = RequestManager.getInstance(context);
		manager.add(request);
		AppNetworkError error = new AppNetworkError();
		error.setRequestListener(request, appRequestListener);
		appRequestListener.onRequestStarted(request);
	}
}
