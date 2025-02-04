package com.example.project3.Genre;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project3.Networking.ResponseInfo;
import com.example.project3.Networking.RetroCallback;
import com.example.project3.Networking.RetroClient;
import com.example.project3.Networking.VideoInfo;
import com.example.project3.R;
import com.example.project3.SplashActivity;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;
import java.util.List;


public class Genre extends AppCompatActivity {

    private ArrayList<VideoRecyclerItem> mMyData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerVideoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View view;
    SwipeRefreshLayout refreshLayout;


    String youtubeurl;
    private static Context context;
    String Token;
    String Username;
    private String[] images = {"HIPHOP", "POPPIN", "URBAN", "GIRLS", "WAACKING", "LOCKING"};

    RetroClient retroClient;

    private SlidrInterface slidr;

    public static Context getParentContext() {
        return context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        Token = sf.getString("Token", null);
        Username = sf.getString("Id", null);
        setContentView(R.layout.genre);
        view = findViewById(R.id.genre_rootview);

        slidr = Slidr.attach(this);
        slidr.unlock();

        Intent intent = getIntent();
        final int position = (int) intent.getIntExtra("position", 0);
        retroClient = RetroClient.getInstance(this).createBaseApi();

        //pull to request
        refreshLayout = findViewById(R.id.genre_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initVideoByGenre(position);
            }
        });

        //toolbar
        Toolbar toolbar = findViewById(R.id.genre_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //init
        initVideoByGenre(position);


    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("DEBUG","Restart");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("로그아웃");
                alertDialogBuilder.setMessage("로그아웃 하시겠습니까??").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = sf.edit();
                                editor.remove("Id");
                                editor.remove("Pw");
                                editor.remove("Token");
                                editor.clear();
                                editor.apply();
                                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initVideoByGenre(int genre) {
        refreshLayout.setRefreshing(true);
        retroClient.getVideoByGenre(genre, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                refreshLayout.setRefreshing(false);
                Log.d("DEBUG", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                refreshLayout.setRefreshing(false);
                mMyData.clear();
                List<VideoInfo> data = ((ResponseInfo) receivedData).getData();
                for (int i = 0; i < data.size(); i++) {
                    VideoRecyclerItem item = new VideoRecyclerItem();
                    item.setAuthor(data.get(i).getUsername());
                    item.setGenre(data.get(i).getGenre());
                    item.setVideoId(data.get(i).getVideoId());
                    item.setTitle(data.get(i).getTitle());
                    item.setDescription(data.get(i).getDescription());
                    item.setCreatedAt(data.get(i).getCreatedAt());
                    mMyData.add(item);
                }
                initRecyclerView();
            }

            @Override
            public void onFailure(int code) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerVideoAdapter(mMyData);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.video_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
