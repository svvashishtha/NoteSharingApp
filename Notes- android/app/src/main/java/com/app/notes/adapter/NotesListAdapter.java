package com.app.notes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.notes.R;
import com.app.notes.baseObjects.NotesObject;

import java.util.ArrayList;

/**
 * Created by Saurabh on 11-11-2015.
 */
public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteItemHolder> {
    Context context;
    OnNoteClick mListener;
    ArrayList<NotesObject> notes;

    public NotesListAdapter(Context context) {
        this.context = context;
        notes = new ArrayList<>();
    }

    public void setOnNoteClickListener(OnNoteClick mListener) {
        this.mListener = mListener;
    }

    public void setData(ArrayList<NotesObject> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public NoteItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);

        return new NoteItemHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteItemHolder holder, final int position) {
        holder.name.setText(notes.get(position).getTeacher());
        holder.ratingBar.setRating(notes.get(position).getRating());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNoteDownloadClick(position);
            }
        });
        holder.preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setPreview(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteItemHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView download, preview;
        RatingBar ratingBar;

        public NoteItemHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            download = (ImageView) itemView.findViewById(R.id.download);
            preview = (ImageView) itemView.findViewById(R.id.preview);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rRatingReview);
        }

    }
    public interface OnNoteClick{
        public void onNoteDownloadClick(int position);
        void setPreview(int position);
    }
}
