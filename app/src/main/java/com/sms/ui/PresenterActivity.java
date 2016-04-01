package com.sms.ui;

/*
 * sms
 * Created by A.Kolchev  9.3.2016
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.View;
import android.widget.Button;


import com.afollestad.materialdialogs.MaterialDialog;
import com.sms.App;
import com.sms.R;
import com.sms.data.model.Presenter;
import com.sms.service.AsyncTaskCompleteListener;
import com.sms.service.GetItemsTask;
import com.sms.ui.adapter.PresenterAdapter;


import java.util.ArrayList;
import java.util.List;


public class PresenterActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PresenterAdapter presenterAdapter;
    private List<Presenter> items = new ArrayList<>();
    private Button buttonGetItems;
    private GetItemsTask asyncTask;
    private SearchView searchView;
    private String mQuery = "";
    private MaterialDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presenter_activity);
        buttonGetItems = (Button) findViewById(R.id.buttonGetItems);
        recyclerView = (RecyclerView) findViewById(R.id.presRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        presenterAdapter = new PresenterAdapter(this, items);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(presenterAdapter);
        asyncTask = new GetItemsTask(this, new AsyncTaskCompleteListener() {
            @Override
            public void onTaskComplete() {
                //fillData();
            }
        });
        mProgressDialog = new MaterialDialog.Builder(this).title(R.string.loading).content(R.string.progress_message).progress(true, 0).build();
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                fillData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    protected void fillData() {
        new FillDataTask().execute();
    }

    protected void setVisibilityButton() {
        if (items.isEmpty()) {
            buttonGetItems.setVisibility(View.VISIBLE);
        } else {
            buttonGetItems.setVisibility(View.GONE);
        }
    }

    public void onClickGetItems(View view) {
        asyncTask.execute();
    }


    protected class FillDataTask extends AsyncTask<String, Void, Boolean> {

        private String errorMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                items.clear();
                if (mQuery.equals("")) {
                    items.addAll(App.getInstance().getDBHelper().getPresenterItems());
                } else {
                    items.addAll(App.getInstance().getDBHelper().getPresenterItems());
                }

                return true;
            } catch (Exception e) {
                errorMessage = getString(R.string.no_data);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                presenterAdapter.setItems(items);
                mProgressDialog.dismiss();
            } else {
                mProgressDialog.setContent(errorMessage);
            }
            setVisibilityButton();
        }
    }

}


