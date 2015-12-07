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

/**
 * Created by Saurabh on 27-10-2015.
 */
public class SemListAdapter extends RecyclerView.Adapter {
    Context context;
    double height;
    private int ITEM_TYPE_TITLE = 1, ITEM_TYPE_GRID = 2;
    onItemClick mListener;

    public SemListAdapter(Context context, double height) {
        this.context = context;
        this.height = height;
    }

    public void setonItemClickListener(onItemClick mListener) {
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
            ((TitleHolder) holder).title.setText("Semester");
            ((TitleHolder) holder).title.setTypeface(null, Typeface.BOLD);
            ((TitleHolder) holder).title.setTextSize(context.getResources().getDimension(R.dimen.font_medium));
        } else {
            ItemHolder itemHolder = (ItemHolder) holder;
            switch (position) {
                case 1:
                    itemHolder.semNo.setText("I");
                    break;
                case 2:
                    itemHolder.semNo.setText("II");
                    break;
                case 3:
                    itemHolder.semNo.setText("III");
                    break;
                case 4:
                    itemHolder.semNo.setText("IV");
                    break;
                case 5:
                    itemHolder.semNo.setText("V");
                    break;
                case 6:
                    itemHolder.semNo.setText("VI");
                    break;
                case 7:
                    itemHolder.semNo.setText("VII");
                    break;
                case 8:
                    itemHolder.semNo.setText("VIII");
                    break;
            }
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(position );
                }
            });
        }
//todo set data here
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        TextView title;

        public TitleHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView semNo;

        public ItemHolder(View itemView) {
            super(itemView);
            semNo = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    public interface onItemClick {
        void onClick(int semNo);
    }
}
