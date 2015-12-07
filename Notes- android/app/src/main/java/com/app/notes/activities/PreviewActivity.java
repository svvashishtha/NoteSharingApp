package com.app.notes.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.notes.R;
import com.app.notes.application.NApplication;
import com.app.notes.baseObjects.NotesObject;
import com.app.notes.extras.RequestTags;
import com.app.notes.objects.AllSubjects;
import com.app.notes.serverApi.AppRequestListener;
import com.app.notes.serverApi.BaseTask;
import com.app.notes.serverApi.RatingGetApi;
import com.app.notes.serverApi.RequestManager;

public class PreviewActivity extends AppCompatActivity implements AppRequestListener, RequestTags, View.OnClickListener {
    String url;
    ViewPager pager;
    private ProgressBar progress;
    private TextView nullcaseText;
    private LinearLayout retryLayout;
    Button rateNote;
    NotesObject notesObject;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        notesObject = (NotesObject) getIntent().getSerializableExtra("noteObject");
        if (notesObject != null)
            url = NApplication.getBaseUrl() + "preview/" + notesObject.getId();
        else
            finish();
        CustomPagerAdapter adapter = new CustomPagerAdapter(PreviewActivity.this);
        pager = (ViewPager) findViewById(R.id.image_preview_pager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(2);
        setLoadingVariables();
        String rateUrl = NApplication.getBaseUrl() + "userrating/" + NApplication.getInstance().getUserId(PreviewActivity.this)
                + "/" + notesObject.getId();
        AllSubjects.getInstance().getRating(PreviewActivity.this, PreviewActivity.this, rateUrl);
    }

    public void setLoadingVariables() {
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        nullcaseText = (TextView) findViewById(R.id.nullcase_text);
        retryLayout = (LinearLayout) findViewById(R.id.retryLayout);
    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(RATE_NOTES)) {
            showLoadingView();
        } else if (request.getRequestTag().equalsIgnoreCase(GET_RATING)) {
            //do nothing
        }
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(RATE_NOTES)) {
            showView();
            Toast.makeText(PreviewActivity.this, "Rating Posted", Toast.LENGTH_SHORT).show();
        } else if (request.getRequestTag().equalsIgnoreCase(GET_RATING)) {
            ratingBar.setRating(((RatingGetApi) request).getRating());
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(RATE_NOTES)) {
            showView();
            Toast.makeText(PreviewActivity.this, "An error occurred. Please try again..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rate_post:
                String url = NApplication.getBaseUrl() + "rate";
                AllSubjects.getInstance().rateNote(PreviewActivity.this, PreviewActivity.this, url,
                        notesObject.getId(), ratingBar.getRating());
        }
    }


    public void showView() {
        progress.setVisibility(View.GONE);
        /*nullcaseText.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);*/
        pager.setVisibility(View.VISIBLE);
    }

    public void showLoadingView() {
        progress.setVisibility(View.VISIBLE);
        /*nullcaseText.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);*/
    }


    public class CustomPagerAdapter extends PagerAdapter {
        Context context;

        public CustomPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position <= 1) {
                View view = LayoutInflater.from(context).inflate(R.layout.generic_imageview, container, false);
                String url1 = url + "/" + (position + 1);
                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                RequestManager.get(PreviewActivity.this).requestImage(PreviewActivity.this, imageView, url1);
                container.addView(view);
                return view;
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.prompt_rate_layout, container, false);
                container.addView(view);
                rateNote = (Button) view.findViewById(R.id.rate_post);
                rateNote.setOnClickListener(PreviewActivity.this);
                rateNote.setActivated(false);
                ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        rateNote.setActivated(true);
                    }
                });
                return view;
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
