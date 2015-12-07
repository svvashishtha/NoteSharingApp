package com.app.notes.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.notes.R;
import com.app.notes.adapter.NotesListAdapter;
import com.app.notes.application.NApplication;
import com.app.notes.baseObjects.NotesObject;
import com.app.notes.baseObjects.SubjectObject;
import com.app.notes.extras.AppConstants;
import com.app.notes.extras.RequestTags;
import com.app.notes.objects.AllSubjects;
import com.app.notes.serverApi.AppRequestListener;
import com.app.notes.serverApi.BaseTask;
import com.app.notes.serverApi.GetNotesApi;
import com.coolerfall.download.DownloadListener;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;

import java.io.File;
import java.util.ArrayList;

public class SubjectViewActivity extends AppCompatActivity implements RequestTags, AppConstants, AppRequestListener, View.OnClickListener {

    RecyclerView notesList;
    LinearLayoutManager llm;
    SubjectObject subjectObject;
    private ProgressBar progress;
    private TextView nullcaseText;
    private LinearLayout retryLayout;
    FloatingActionButton fab;
    NotesListAdapter mAdapter;

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder, mUploadBuilder;
    private String path_scan;
    Dialog uploadDialog;
    TextView fileName;
    File fileTobeUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(NApplication.getInstance().getSemNo(SubjectViewActivity.this) + " Semester");
        subjectObject = (SubjectObject) getIntent().getExtras().getSerializable(SUBJECT_OBJECT);
        setLoadingVariables();
        retryLayout.setOnClickListener(this);
        mAdapter = new NotesListAdapter(this);
        notesList = (RecyclerView) findViewById(R.id.notes_list);
        llm = new LinearLayoutManager(this);
        notesList.setLayoutManager(llm);
        mAdapter.setOnNoteClickListener(new NotesListAdapter.OnNoteClick() {
            @Override
            public void onNoteDownloadClick(int position) {
                setupDownloading(notesObjectArrayList.get(position).getId(), notesObjectArrayList.get(position).getTeacher() + "_" +
                        notesObjectArrayList.get(position).getCourse_id());
            }

            @Override
            public void setPreview(int position) {
                Intent intent = new Intent(SubjectViewActivity.this, PreviewActivity.class);

                intent.putExtra("noteObject", notesObjectArrayList.get(position));
                startActivity(intent);
            }
        });
        setUpFab();
        loadData();
        mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /*downloadManager = DownloadManagerBuilder.from(this)
                .build();*/
    }

    private void loadData() {
        String url = NApplication.getBaseUrl() + "courses/id/" + subjectObject.getCode()
                /*+ "/" + NApplication.getInstance().getUserId(SubjectViewActivity.this)*/;
        AllSubjects.getInstance().getNotes(SubjectViewActivity.this, this, url);
    }

    public void setUpFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        uploadDialog = new Dialog(SubjectViewActivity.this);
        View dialogView = LayoutInflater.from(SubjectViewActivity.this).inflate(R.layout.upload_layout_file, null);
        uploadDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        uploadDialog.setContentView(dialogView);
        final EditText teacherName = (EditText) uploadDialog.findViewById(R.id.teacher_name);
        fileName = (TextView) uploadDialog.findViewById(R.id.filename);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = uploadDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        uploadDialog.setCanceledOnTouchOutside(true);

        uploadDialog.findViewById(R.id.open_browser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubjectViewActivity.this, FolderOpenActivity.class);
                intent.putExtra("path_default", "/storage");
                startActivityForResult(intent, 0);
            }
        });
        uploadDialog.findViewById(R.id.upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = NApplication.getBaseUrl() + "upload/1/ETMA_101/silica";
                if (teacherName.getText().toString().trim().length() > 0) {
                    String url = NApplication.getBaseUrl() + "upload/" +
                            NApplication.getInstance().getSemNo(SubjectViewActivity.this) + "/" +
                            subjectObject.getCode() + "/" + teacherName.getText().toString();
                    if (fileTobeUploaded != null)
                        AllSubjects.getInstance().uploadNotes(SubjectViewActivity.this, SubjectViewActivity.this, url, fileTobeUploaded);

                    uploadDialog.dismiss();
                } else {
                    teacherName.setError("This field is important.");
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                uploadDialog.show();
            }
        });


    }

    @Override
    public <T> void onRequestStarted(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(NOTES_REQUEST)) {
            showLoadingView();

        } else if (request.getRequestTag().equalsIgnoreCase(UPLOAD_NOTES)) {

            Log.i("SubjectViewActivity", "upload started");
            setUpUploading();
        }
    }

    ArrayList<NotesObject> notesObjectArrayList;

    @Override
    public <T> void onRequestCompleted(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(NOTES_REQUEST)) {
            if (((GetNotesApi) request).getNotesObjectList() != null) {
                notesObjectArrayList = ((GetNotesApi) request).getNotesObjectList();
                if (notesObjectArrayList.size() > 0) {
                    setAdapterData();
                    showView();
                } else {
                    showNullCaseView("No Notes found");
                }
            }
        } else if (request.getRequestTag().equalsIgnoreCase(UPLOAD_NOTES)) {
            Log.i("SubjectViewActivity", "uploaded");
            mUploadBuilder.setContentText("File uploaded");
            Toast.makeText(SubjectViewActivity.this,"Upload Successful.",Toast.LENGTH_SHORT).show();
            mNotifyManager.notify(uploadId, mUploadBuilder.build());

        }
    }

    private void setAdapterData() {
        mAdapter.setData(notesObjectArrayList);
        notesList.setAdapter(mAdapter);
    }

    @Override
    public <T> void onRequestFailed(BaseTask<T> request) {
        if (request.getRequestTag().equalsIgnoreCase(NOTES_REQUEST)) {
            showRetryLayout();
        } else if (request.getRequestTag().equalsIgnoreCase(UPLOAD_NOTES)) {
            Log.i("SubjectViewActivity", "upload failed");
            mUploadBuilder.setContentText("File upload failed");
            Toast.makeText(SubjectViewActivity.this,"Upload Failed.",Toast.LENGTH_SHORT).show();
            mNotifyManager.notify(uploadId, mUploadBuilder.build());
        }
    }

    public void setLoadingVariables() {
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        nullcaseText = (TextView) findViewById(R.id.nullcase_text);
        retryLayout = (LinearLayout) findViewById(R.id.retryLayout);
    }

    public void showRetryLayout() {
        progress.setVisibility(View.GONE);
        nullcaseText.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.VISIBLE);
        notesList.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
    }

    public void showView() {
        progress.setVisibility(View.GONE);
        nullcaseText.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        notesList.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    public void showLoadingView() {
        progress.setVisibility(View.VISIBLE);
        nullcaseText.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        notesList.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
    }

    public void showNullCaseView(String text) {
        showRetryLayout();
        nullcaseText.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retryLayout:
                loadData();
        }
    }

    int id = 1, uploadId = 100;

    private void setupDownloading(String url, String filename) {
        NApplication.getInstance();
        url = NApplication.getBaseUrl() + url;
        DownloadManager manager = new DownloadManager();
        String destPath = Environment.getExternalStorageDirectory() +
                File.separator + filename + ".pdf";


        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Downloading Notes")
                .setContentText("Downloading")
                .setSmallIcon(R.drawable.ic_file_download_black_48dp);
        id = (int) (Math.random() * 100);
        DownloadRequest request = new DownloadRequest()
                .setDownloadId(id)
                .setUrl(url)
                .setDestFilePath(destPath)
                .setDownloadListener(new DownloadListener() {
                                         @Override
                                         public void onStart(int downloadId, long totalBytes) {
                                             Log.i("SubjectViewActivity ", "started " + totalBytes + "id" + id);

                                             mBuilder.setProgress(100, 0, false);
                                             mBuilder.mNumber = 0;
                                             mNotifyManager.notify(id, mBuilder.build());
                                             Toast.makeText(SubjectViewActivity.this, "Download Started", Toast.LENGTH_SHORT).show();
                                         }

                                         @Override
                                         public void onRetry(int downloadId) {
                                         }

                                         @Override
                                         public void onProgress(int downloadId, long bytesWritten, long totalBytes) {

                                             int progress = (int) (((float) bytesWritten / (float) totalBytes) * 100);
                                             if (progress % 5 == 0) {
                                                 mBuilder.setProgress(100, progress, false);
                                                 if (progress <= 100)
                                                     mBuilder.setContentText(progress + "% Complete");
                                                 Log.i("SubjectViewActivity ", "progress" + progress + "id" + id);
                                             }
                                             mNotifyManager.notify(id, mBuilder.build());
                                         }

                                         @Override
                                         public void onSuccess(int downloadId, String filePath) {
                                             Log.i("SubjectViewActivity ", "saved " + filePath);
                                             mBuilder.setProgress(100, 100, false);
                                             mBuilder.setContentText("Download Complete");
                                             mNotifyManager.notify(id, mBuilder.build());
                                         }

                                         @Override
                                         public void onFailure(int downloadId, int statusCode, String errMsg) {
                                             Log.i("SubjectViewActivity", "failed" + errMsg);
                                             mBuilder.setProgress(0, 100, false);
                                             mBuilder.setContentText("Download Failed");
                                             mNotifyManager.notify(id, mBuilder.build());
                                         }
                                     }
                );


        manager.add(request);

    }

    public void setUpUploading() {
        uploadId = (int) (Math.random() * 100);
        mUploadBuilder = new NotificationCompat.Builder(this);
        mUploadBuilder.setContentTitle("Uploading file")
                .setContentText("Uploading")
                .setSmallIcon(R.drawable.ic_file_upload_black_24dp);
        mNotifyManager.notify(uploadId, mUploadBuilder.build());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            path_scan = data.getStringExtra("path");
            Log.i("path", path_scan);
            fileTobeUploaded = new File(path_scan);
            fileName.setText(fileTobeUploaded.getName());
        }
    }
}
