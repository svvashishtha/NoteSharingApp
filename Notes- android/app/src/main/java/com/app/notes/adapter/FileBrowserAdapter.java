package com.app.notes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.notes.R;

import java.io.File;

/**
 * Created by Saurabh on 18-11-2015.
 */
public class FileBrowserAdapter extends RecyclerView.Adapter<FileBrowserAdapter.FileNameHolder> {
    Context context;
    File files[];
    OnItemClick mListener;

    public FileBrowserAdapter(Context context, File[] files) {
        this.context = context;
        this.files = files;
    }

    public void setOnItemClickListener(OnItemClick mListener) {
        this.mListener = mListener;
    }

    @Override
    public FileBrowserAdapter.FileNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        return new FileNameHolder(v);
    }

    @Override
    public void onBindViewHolder(FileBrowserAdapter.FileNameHolder holder, int position) {
        holder.nameOfFile.setText(files[position].getName());

    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public interface OnItemClick {
        void OnFileClickListener(int position);

        void OnContextMenuCreate(int position);
    }

    public class FileNameHolder extends RecyclerView.ViewHolder {
        TextView nameOfFile;

        public FileNameHolder(final View itemView) {
            super(itemView);
            nameOfFile = (TextView) itemView.findViewById(R.id.row);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnFileClickListener(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.OnContextMenuCreate(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
