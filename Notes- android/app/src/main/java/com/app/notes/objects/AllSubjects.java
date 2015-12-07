package com.app.notes.objects;

import android.content.Context;

import com.android.volley.Request;
import com.app.notes.application.NApplication;
import com.app.notes.extras.RequestTags;
import com.app.notes.serverApi.AppRequestListener;
import com.app.notes.serverApi.GetNotesApi;
import com.app.notes.serverApi.RatingApi;
import com.app.notes.serverApi.RatingGetApi;
import com.app.notes.serverApi.SubjectApi;
import com.app.notes.serverApi.UploadFileApi;

import org.json.JSONException;

import java.io.File;

/**
 * Created by Saurabh on 05-11-2015.
 */
public class AllSubjects extends BaseObject implements RequestTags {
    private static AllSubjects sInstance;

    public static AllSubjects getInstance() {
        if (sInstance == null)
            sInstance = new AllSubjects();
        return sInstance;
    }

    @Override
    public void clear(Context context) {

    }

    public void getSubjects(Context context, AppRequestListener appRequestListener, String url) {
        AppNetworkError appNetworkError = new AppNetworkError();
        SubjectApi request = null;
        try {
            request = new SubjectApi(Request.Method.GET, url, appNetworkError, SUBJECT_REQUEST, appRequestListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendRequest(context, appNetworkError, request, appRequestListener);
    }

    public void getNotes(Context context, AppRequestListener appRequestListener, String url) {
        AppNetworkError error = new AppNetworkError();
        GetNotesApi request = new GetNotesApi(Request.Method.GET, url, error, NOTES_REQUEST, appRequestListener);
        sendRequest(context, error, request, appRequestListener);
    }

    public void uploadNotes(Context context, AppRequestListener appRequestListener, String url, File file) {
        AppNetworkError error = new AppNetworkError();
        UploadFileApi request = new UploadFileApi(Request.Method.POST, url, error, UPLOAD_NOTES, appRequestListener, file);
        sendRequest(context, error, request, appRequestListener);
    }

    public void rateNote(Context context, AppRequestListener appRequestListener, String url, String pdfId, float rating) {
        AppNetworkError error = new AppNetworkError();
        RatingApi request = new RatingApi(Request.Method.POST, url, error, RATE_NOTES, appRequestListener,
                NApplication.getInstance().getUserId(context), pdfId, rating);
        sendRequest(context, error, request, appRequestListener);
    }

    public void getRating(Context context,AppRequestListener appRequestListener,String url)
    {
        AppNetworkError error = new AppNetworkError();
        RatingGetApi request = new RatingGetApi(Request.Method.GET,url,error,GET_RATING,appRequestListener);
        sendRequest(context, error, request, appRequestListener);
    }
}
