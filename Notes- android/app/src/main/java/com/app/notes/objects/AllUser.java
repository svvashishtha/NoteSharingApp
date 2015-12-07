package com.app.notes.objects;

import android.content.Context;

import com.android.volley.Request;
import com.app.notes.extras.RequestTags;
import com.app.notes.serverApi.AppRequestListener;
import com.app.notes.serverApi.LoginApi;
import com.app.notes.serverApi.SignUpApi;

/**
 * Created by Saurabh on 16-11-2015.
 */
public class AllUser extends BaseObject implements RequestTags {
    private static AllUser sInstance;

    public static AllUser getInstance() {
        if (sInstance == null)
            sInstance = new AllUser();
        return sInstance;
    }

    @Override
    public void clear(Context context) {

    }

    public void requestLogin(Context context, AppRequestListener appRequestListener,
                             String url, String userName, String password) {
        AppNetworkError error = new AppNetworkError();
        LoginApi request = new LoginApi(Request.Method.POST, url, error, LOGIN_APP, appRequestListener, userName, password);
        sendRequest(context, error, request, appRequestListener);
    }

    public void requestSignUp(Context context, AppRequestListener appRequestListener, String url,
                              String UserName, String password, String email, String firstName,
                              String LastName, String rollNo) {
        AppNetworkError error = new AppNetworkError();
        SignUpApi request = new SignUpApi(Request.Method.POST, url, error, SIGN_UP_APP, appRequestListener, UserName, password,
                email, firstName, LastName, rollNo);
        sendRequest(context, error, request, appRequestListener);
    }
}
