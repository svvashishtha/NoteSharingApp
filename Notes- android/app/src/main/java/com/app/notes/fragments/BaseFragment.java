package com.app.notes.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.notes.R;


public class BaseFragment extends Fragment {


    private Toast toast;
    public ProgressBar progress;
    public View view;
    public LinearLayout retryLayout;
    public TextView nullcaseText;

    public static BaseFragment newInstance(String param1, String param2) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BaseFragment() {
        // Required empty public constructor
    }

    public void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }

    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics;
    }

    public void setLoadingVariables() {
        progress = (ProgressBar) view.findViewById(R.id.progress_bar);
        nullcaseText = (TextView) view.findViewById(R.id.nullcase_text);
        retryLayout = (LinearLayout) view.findViewById(R.id.retryLayout);
    }

    public void showRetryLayout() {
        progress.setVisibility(View.GONE);
        nullcaseText.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.VISIBLE);
    }

    public void showView() {
        progress.setVisibility(View.GONE);
        nullcaseText.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
    }

    public void showLoadingView() {
        progress.setVisibility(View.VISIBLE);
        nullcaseText.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
    }

    //gone = 8, visible = 0 invisible = 4
    public void changeViewVisibility(View v, int visibility) {
        v.setVisibility(visibility);
    }

}
