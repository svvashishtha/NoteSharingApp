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
import com.app.notes.adapter.BranchAdapter;
import com.app.notes.baseObjects.BranchObject;

import java.util.ArrayList;


public class BranchFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private View view;
    private RecyclerView branchGrid;
    private int width;
    private BranchAdapter mAdapter;
    ArrayList<BranchObject> mData;
    public static BranchFragment newInstance() {
        BranchFragment fragment = new BranchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BranchFragment() {
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
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_branch, container, false);
        branchGrid = (RecyclerView) view.findViewById(R.id.notes_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        branchGrid.setLayoutManager(gridLayoutManager);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        branchGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        mData = new ArrayList<>();
        mData.add(new BranchObject("CSE","some id"));
        mAdapter = new BranchAdapter(getActivity(), width * 0.4);
        mAdapter.setData(mData);
        mAdapter.setOnSubjectClickListener(new BranchAdapter.OnSubjectClickListener() {
            @Override
            public void subjectClicked(int position) {
                mListener.onFragmentInteraction(mData.get(position));
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
        branchGrid.setAdapter(mAdapter);
        return view;

    }

    public void setOnFragmentIterationListener(OnFragmentInteractionListener mListener)
    {
        this.mListener= mListener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(BranchObject branchObject);
    }

}
