package com.app.notes.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.notes.R;

import java.io.File;

/**
 * Created by Saurabh on 1/30/2015.
 */
public class fileAdapter extends BaseAdapter{
    Context context;
    File files[];
    public fileAdapter(Context context, File files[])
    {
        this.context = context;
        this.files = files;
    }


    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int position) {
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View_Holder view_holder;

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, parent, false);

            view_holder = new View_Holder();
            view_holder.textViewItem = (TextView) convertView.findViewById(R.id.row);

            convertView.setTag(view_holder);
        }
        else
        {
            view_holder = (View_Holder)convertView.getTag();
        }
        view_holder.textViewItem.setText(files[position].getName());
        view_holder.textViewItem.setTag(files[position].getName());
        return convertView;
    }


    static class View_Holder
    {
        TextView textViewItem;
    }
}