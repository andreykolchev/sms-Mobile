package com.sms.ui;

/*
 * sms
 * Created by A.Kolchev  24.2.2016
 */

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.sms.App;
import com.sms.R;
import com.sms.data.model.Route;
import com.sms.service.AsyncTaskCompleteListener;
import com.sms.service.GetRoutsTask;
import com.sms.ui.adapter.RouteAdapter;

import java.util.ArrayList;
import java.util.List;


public class RoutsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RouteAdapter routeAdapter;
    private List<Route> items = new ArrayList<>();
    private Button buttonGetRouts;
    private GetRoutsTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routs_activity);
        buttonGetRouts = (Button) findViewById(R.id.buttonGetRouts);
        recyclerView = (RecyclerView) findViewById(R.id.routRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        routeAdapter = new RouteAdapter(this, items);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(routeAdapter);
        asyncTask = new GetRoutsTask(this, new AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete() {
                fillData();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    public void fillData(){
        items.clear();
        items.addAll(App.getInstance().getDBHelper().getRouts());
        routeAdapter.setItems(items);
        setVisibilityButtonGetRouts();
    }

    protected void setVisibilityButtonGetRouts() {
        if (items.isEmpty()) {
            buttonGetRouts.setVisibility(View.VISIBLE);
        }else{
            buttonGetRouts.setVisibility(View.GONE);
        }
    }

    public void onClickGetRouts(View view) {
        asyncTask.execute();
    }
}



