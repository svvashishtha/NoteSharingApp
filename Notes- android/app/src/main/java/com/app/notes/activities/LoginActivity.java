package com.app.notes.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.notes.R;
import com.app.notes.application.NApplication;
import com.app.notes.baseObjects.UserObject;
import com.app.notes.extras.AppConstants;
import com.app.notes.extras.RequestTags;
import com.app.notes.objects.AllUser;
import com.app.notes.serverApi.AppRequestListener;
import com.app.notes.serverApi.BaseTask;
import com.app.notes.serverApi.LoginApi;
import com.app.notes.serverApi.SignUpApi;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,
        AppRequestListener, AppConstants, RequestTags {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    View signUpView, loginView;
    // UI references.
    private AutoCompleteTextView mEmailLoginView, mEmailSignupView, mUserName, mRollNo, mFirstName, mLastName;
    private EditText mPasswordLoginView, mPasswordSignUpView;
    private View mProgressLoginView, mProgressSignUpView;
    private View mLoginFormLoginView, mLoginFormSignUpView;
    private ViewPager pager;
    CustomPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NApplication.getInstance().isLogin(this)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            this.finish();
            startActivity(intent);
        }
        setContentView(R.layout.activity_login);
        pager = (ViewPager) findViewById(R.id.login_signup_pager);
        mAdapter = new CustomPagerAdapter(this);
        mProgressLoginView = findViewById(R.id.login_progress);
        pager.setAdapter(mAdapter);
    }

    private void setUpLoginView(View view) {
        mEmailLoginView = (AutoCompleteTextView) view.findViewById(R.id.email);
        populateAutoComplete();

        mPasswordLoginView = (EditText) view.findViewById(R.id.password);
        mPasswordLoginView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mEmailSignUpButton = (Button) view.findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });
        mLoginFormLoginView = view.findViewById(R.id.login_form);
    }

    public void setSignUpView(View view) {
        mEmailSignupView = (AutoCompleteTextView) view.findViewById(R.id.email);
        populateAutoComplete();

        mPasswordSignUpView = (EditText) view.findViewById(R.id.password);
        mPasswordSignUpView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                pager.setCurrentItem(0);
            }
        });
        Button mEmailSignUpButton = (Button) view.findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
        mLoginFormLoginView = view.findViewById(R.id.login_form);
        mFirstName = (AutoCompleteTextView) view.findViewById(R.id.firstname);
        mLastName = (AutoCompleteTextView) view.findViewById(R.id.lastname);
        mUserName = (AutoCompleteTextView) view.findViewById(R.id.username);
        mRollNo = (AutoCompleteTextView) view.findViewById(R.id.roll_no);

    }

    private void attemptSignUp() {
        mEmailSignupView.setError(null);
        mPasswordSignUpView.setError(null);
        String email = mEmailSignupView.getText().toString();
        String password = mPasswordSignUpView.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String userName = mUserName.getText().toString();
        String roll = mRollNo.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordSignUpView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordSignUpView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailLoginView.setError(getString(R.string.error_field_required));
            focusView = mEmailLoginView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailLoginView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailLoginView;
            cancel = true;
        }
        if (TextUtils.isEmpty(roll)) {
            mRollNo.setError(getString(R.string.error_field_required));
            focusView = mRollNo;
            cancel = true;
        }
        if (TextUtils.isEmpty(firstName)) {
            mFirstName.setError(getString(R.string.error_field_required));
            focusView = mFirstName;
            cancel = true;
        }
        if (TextUtils.isEmpty(lastName)) {
            mLastName.setError(getString(R.string.error_field_required));
            focusView = mLastName;
            cancel = true;
        }
        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

           /* mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
            String url = NApplication.getBaseUrl() + "register";
            AllUser.getInstance().requestSignUp(LoginActivity.this, LoginActivity.this, url,
                    userName, password, email, firstName, lastName, roll);
        }
    }

    private void populateAutoComplete() {


        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailLoginView.setError(null);
        mPasswordLoginView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailLoginView.getText().toString();
        String password = mPasswordLoginView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordLoginView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordLoginView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailLoginView.setError(getString(R.string.error_field_required));
            focusView = mEmailLoginView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailLoginView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailLoginView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

           /* mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
            String url = NApplication.getBaseUrl() + "login";
            AllUser.getInstance().requestLogin(LoginActivity.this, LoginActivity.this, url, email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.trim().length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormLoginView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormLoginView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormLoginView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressLoginView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressLoginView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressLoginView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressLoginView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormLoginView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(LOGIN_APP)) {
            showProgress(true);
        } else if (request.getRequestTag().equalsIgnoreCase(SIGN_UP_APP)) {
            showProgress(true);
        }
    }

    UserObject userObject;

    @Override
    public <T> void onRequestCompleted(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(LOGIN_APP)) {
            showProgress(false);
            if (((LoginApi) request).getStatus()) {
                userObject = ((LoginApi) request).getUserObject();
                NApplication.getInstance().login(LoginActivity.this, userObject);
                /*NApplication.getInstance().setUserId(LoginActivity.this, userObject.get_id());
                NApplication.getInstance().setLoggedIn(LoginActivity.this, true);
                */
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                LoginActivity.this.finish();
                startActivity(intent);
            }
        } else if (((SignUpApi) request).getRequestTag().equalsIgnoreCase(SIGN_UP_APP)) {
            showProgress(false);
            if (((SignUpApi) request).getStatusString() == null) {
                userObject = ((SignUpApi) request).getUserObject();
                NApplication.getInstance().login(LoginActivity.this, userObject);
                /*NApplication.getInstance().setUserId(LoginActivity.this, userObject.get_id());
                NApplication.getInstance().setLoggedIn(LoginActivity.this, true);
                */
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                LoginActivity.this.finish();
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, ((SignUpApi) request).getStatusString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> request) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailLoginView.setAdapter(adapter);
    }

    public class CustomPagerAdapter extends PagerAdapter {
        Context context;

        public CustomPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 0) {
                loginView = LayoutInflater.from(context).inflate(R.layout.login_view, container, false);
                // Set up the login form.
                setUpLoginView(loginView);
                container.addView(loginView);
                return loginView;
            } else {
                signUpView = LayoutInflater.from(context).inflate(R.layout.signup_layout, container, false);
                setSignUpView(signUpView);
                container.addView(signUpView);
                return signUpView;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }


}

