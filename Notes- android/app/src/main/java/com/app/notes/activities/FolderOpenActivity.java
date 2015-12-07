package com.app.notes.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.app.notes.R;
import com.app.notes.adapter.FileBrowserAdapter;

import java.io.File;


public class FolderOpenActivity extends AppCompatActivity {
    String path;
    File file, files[];
    RecyclerView listView;
    //fileAdapter adapter;
    FileBrowserAdapter mAdapter;
    Context context;
    // Button path_Butt;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_open);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // path_Butt = (Button)findViewById(R.id.temptext);
        Intent intent = getIntent();
        path = intent.getStringExtra("path_default");
        // path_Butt.setText(path);
        toolbar.setTitle(path);
        file = new File(path);
        files = file.listFiles();
        llm = new LinearLayoutManager(FolderOpenActivity.this);
        listView = (RecyclerView) findViewById(R.id.second_List);
        listView.setLayoutManager(llm);
        context = getApplicationContext();
        //adapter = new fileAdapter(context, files);
        mAdapter = new FileBrowserAdapter(context, files);
        mAdapter.setOnItemClickListener(new FileBrowserAdapter.OnItemClick() {
            @Override
            public void OnFileClickListener(int position) {
                if (files[position].isDirectory()) {
                    Intent intent = new Intent(FolderOpenActivity.this, FolderOpenActivity.class);
                    Log.i("path", files[position].getAbsolutePath());
                    intent.putExtra("path_default", files[position].getAbsolutePath());
                    startActivityForResult(intent, 0);
                } else {
                    Intent intent1 = new Intent();
                    intent1.putExtra("path", files[position].getAbsolutePath());
                    setResult(RESULT_OK, intent1);
                    finish();
                }
            }

            @Override
            public void OnContextMenuCreate(final int position) {
                final Dialog contextMenuDialog = new Dialog(FolderOpenActivity.this);
                View dialogView = LayoutInflater.from(FolderOpenActivity.this).inflate(R.layout.dialog_context_menu_file, null);
                contextMenuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                contextMenuDialog.setContentView(dialogView);
                if (!files[position].isDirectory()) {
                    contextMenuDialog.findViewById(R.id.open).setVisibility(View.GONE);
                }

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = contextMenuDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                contextMenuDialog.setCanceledOnTouchOutside(true);
                contextMenuDialog.findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent();
                        intent1.putExtra("path", files[position].getAbsolutePath());
                        setResult(RESULT_OK, intent1);
                        contextMenuDialog.dismiss();
                        finish();
                    }
                });
                contextMenuDialog.findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FolderOpenActivity.this, FolderOpenActivity.class);
                        Log.i("path", files[position].getAbsolutePath());
                        intent.putExtra("path_default", files[position].getAbsolutePath());
                        contextMenuDialog.dismiss();
                        startActivityForResult(intent, 0);

                    }
                });
                contextMenuDialog.show();
            }

        });
        listView.setAdapter(mAdapter);
        /*listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (files[position].isDirectory()) {
                    Intent intent = new Intent(FolderOpenActivity.this, FolderOpenActivity.class);
                    Log.i("path", files[position].getAbsolutePath());
                    intent.putExtra("path_default", files[position].getAbsolutePath());
                    startActivityForResult(intent, 0);
                } else {
                    Intent intent1 = new Intent();
                    intent1.putExtra("path", files[position].getAbsolutePath());
                    setResult(RESULT_OK, intent1);
                    finish();
                }
            }

        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent1 = new Intent();
            String return_path = data.getStringExtra("path");
            intent1.putExtra("path", return_path);
            setResult(RESULT_OK, intent1);
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    /* @Override
     public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
         if (v.getId() == R.id.second_List) {
             AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
             menu.setHeaderTitle(files[info.position].getName());
             if (files[info.position].isDirectory()) {
                 menu.add("Open");
                 menu.add("select");
             } else {
                 menu.add("select");
             }
         }
     }
 */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String item_name = item.toString();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Log.i("on create options", "" + item_name);
        if (item_name == "Open") {

            Intent intent = new Intent(FolderOpenActivity.this, FolderOpenActivity.class);
            Log.i("path", files[info.position].getAbsolutePath());
            intent.putExtra("path_default", files[info.position].getAbsolutePath());
            startActivityForResult(intent, 0);

        } else {
            Intent intent1 = new Intent();
            intent1.putExtra("path", files[info.position].getAbsolutePath());
            setResult(RESULT_OK, intent1);
            finish();
        }
        return true;
    }
}