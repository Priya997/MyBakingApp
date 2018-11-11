package com.priyankaj.mybakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.priyankaj.mybakingapp.R;
import com.priyankaj.mybakingapp.adapters.MainAdapter;
import com.priyankaj.mybakingapp.models.MainModel;
import com.priyankaj.mybakingapp.utils.Connection;
import com.priyankaj.mybakingapp.utils.OnItemTouchListener;
import com.priyankaj.mybakingapp.utils.Urls;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String LIST_CONSTANT = "list";
    public static ArrayList<MainModel> list = new ArrayList<>();
    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerview;
    private LinearLayoutManager layoutManager;
    private MainAdapter adapter;
    private boolean isTablet;
    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AndroidNetworking.initialize(getApplicationContext());

        isTablet = getResources().getBoolean(R.bool.is_tablet);

        if (isTablet) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 1);
        }
        recyclerview.setLayoutManager(layoutManager);
        adapter = new MainAdapter(list, this);
        recyclerview.setAdapter(adapter);

        recyclerview.addOnItemTouchListener(new OnItemTouchListener(this, new OnItemTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                MainModel recipe = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", recipe);
                bundle.putInt("position", position);
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));
        Connection connection = new Connection(getApplicationContext());
        if (connection.isInternet()) {
            getData();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {

        AndroidNetworking.get(Urls.url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(MainModel.class, new ParsedRequestListener<ArrayList<MainModel>>() {
                    @Override
                    public void onResponse(ArrayList<MainModel> response) {
                        list = response;
                        Log.d("response", "" + response);
                        adapter = new MainAdapter(list, MainActivity.this);
                        recyclerview.setAdapter(adapter);
                        layoutManager.onRestoreInstanceState(listState);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "" + anError);
                    }
                });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listState = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_CONSTANT, listState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_CONSTANT);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
    }


}
