package com.app.notes.fragments;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.notes.R;
import com.app.notes.activities.SubjectViewActivity;
import com.app.notes.adapter.SubjectAdapter;
import com.app.notes.application.NApplication;
import com.app.notes.baseObjects.SubjectObject;
import com.app.notes.extras.AppConstants;
import com.app.notes.extras.RequestTags;
import com.app.notes.objects.AllSubjects;
import com.app.notes.serverApi.AppRequestListener;
import com.app.notes.serverApi.BaseTask;
import com.app.notes.serverApi.SubjectApi;

import java.util.ArrayList;

public class SubjectFragment extends BaseFragment implements AppConstants, AppRequestListener, RequestTags, View.OnClickListener {
    private RecyclerView subGrid;
    private int width;
    private SubjectAdapter mAdapter;

    public static SubjectFragment newInstance() {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SubjectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_subject, container, false);
        subGrid = (RecyclerView) view.findViewById(R.id.notes_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        subGrid.setLayoutManager(gridLayoutManager);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        subGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.left = width / 15;
                    outRect.right = width / 15;
                    outRect.bottom = width / 30;
                } else if (parent.getChildAdapterPosition(view) % 2 == 1) {
                    outRect.left = width / 15;
                    outRect.right = width / 30;
                    outRect.bottom = width / 15;
                } else if (parent.getChildAdapterPosition(view) % 2 == 0) {
                    outRect.left = width / 30;
                    outRect.right = width / 15;
                    outRect.bottom = width / 15;
                }
                if (parent.getChildAdapterPosition(view) == 0 /*|| parent.getChildAdapterPosition(view) == 1*/) {
                    outRect.top = width / 15;
                    outRect.bottom = width / 15;
                }

            }
        });

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == 1)
                    return 2;
                else return 1;
            }
        });
        setLoadingVariables();
        retryLayout.setOnClickListener(this);
        mAdapter = new SubjectAdapter(getActivity(), width * 0.4);
        mAdapter.setOnSubjectListener(new SubjectAdapter.onSubjectClick() {
            @Override
            public void onSubjectClickListener(int position) {
                Intent intent = new Intent(getActivity(), SubjectViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(SUBJECT_OBJECT, subjects.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        loadData();
        /*subjects = new ArrayList<>();
        subjects.add(new SubjectObject("Advanced Computer Networks"));
        subjects.add(new SubjectObject("Requirement Estimation Techniques"));
        showView();
        changeViewVisibility(subGrid, 0);
        setUpAdapter();*/
        return view;

    }

    private void loadData() {
        NApplication.getInstance();
        String url = NApplication.getBaseUrl() + "courses/" + NApplication.getInstance().getSemNo(getActivity());
        AllSubjects.getInstance().getSubjects(getActivity(), this, url);
    }

    private void setUpAdapter() {

        mAdapter.setData(subjects);
        subGrid.setAdapter(mAdapter);
    }

    ArrayList<SubjectObject> subjects;

    @Override
    public <T> void onRequestStarted(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(SUBJECT_REQUEST)) {
            showLoadingView();
            changeViewVisibility(subGrid, 8);
        }
    }

    @Override
    public <T> void onRequestCompleted(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(SUBJECT_REQUEST)) {
            subjects = ((SubjectApi) request).getSubjects();
            showView();
            changeViewVisibility(subGrid, 0);
            setUpAdapter();
        }
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(SUBJECT_REQUEST)) {
            showRetryLayout();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retryLayout:
                loadData();
        }
    }
}
