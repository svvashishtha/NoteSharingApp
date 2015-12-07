package com.app.notes.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.notes.R;
import com.app.notes.baseObjects.SubjectObject;

import java.util.ArrayList;

/**
 * Created by Saurabh on 01-11-2015.
 */
public class SubjectAdapter extends RecyclerView.Adapter {

    Context context;
    double height;
    private int ITEM_TYPE_TITLE = 1, ITEM_TYPE_GRID = 2;
    ArrayList<SubjectObject> mData;
    onSubjectClick mListener;

    public SubjectAdapter(Context context, double height) {
        this.context = context;
        this.height = height;
        mData = new ArrayList<>();
    }

    public void setData(ArrayList<SubjectObject> mData) {
        this.mData = mData;
    }

    public void setOnSubjectListener(onSubjectClick mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ITEM_TYPE_TITLE;
        else
            return ITEM_TYPE_GRID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_GRID) {
            ItemHolder holder = new ItemHolder(LayoutInflater.from(context).inflate(R.layout.generic_grid_view, parent, false));
            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            lp.height = (int) height;
            return holder;
        } else
            return new TitleHolder(LayoutInflater.from(context).inflate(R.layout.generic_textview, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            ((TitleHolder) holder).title.setText("Subjects");
            ((TitleHolder) holder).title.setTypeface(null, Typeface.BOLD);
            ((TitleHolder) holder).title.setTextSize(context.getResources().getDimension(R.dimen.font_medium));
        } else {
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.subjectName.setText(mData.get(position - 1).getName());
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSubjectClickListener(position-1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        TextView title;

        public TitleHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView subjectName;

        public ItemHolder(View itemView) {
            super(itemView);
            subjectName = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    public interface onSubjectClick {
        void onSubjectClickListener(int position);
    }
}
