package com.app.notes.fragments;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.notes.R;
import com.app.notes.adapter.SemListAdapter;
import com.app.notes.application.NApplication;

public class SemFragment extends Fragment {

    private OnSemSelected mListener;
    RecyclerView semGrid;
    GridLayoutManager gridLayoutManager;
    SemListAdapter mAdapter;
    View view;

    public static SemFragment newInstance() {
        SemFragment fragment = new SemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    int width;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sem, container, false);
        semGrid = (RecyclerView) view.findViewById(R.id.notes_list);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        semGrid.setLayoutManager(gridLayoutManager);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        semGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        mAdapter = new SemListAdapter(getActivity(), width * 0.4);
        mAdapter.setonItemClickListener(new SemListAdapter.onItemClick() {
            @Override
            public void onClick(int semNo) {
                NApplication.getInstance().setSemNo(getActivity(), semNo);
                mListener.onSemClicked(semNo);
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
        semGrid.setAdapter(mAdapter);
        return view;

    }

    public void setOnSemSelectedListener(OnSemSelected mListener) {
        this.mListener = mListener;
    }

    public interface OnSemSelected {
        void onSemClicked(int semNo);
    }

}
