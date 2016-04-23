package com.pitty.expandablerecyclerviewsample.vertical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pitty.expandablerecyclerviewsample.R;

public class VerticalLinearRecyclerViewSampleActivity extends AppCompatActivity{

    private ExpandableAdapter mAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, VerticalLinearRecyclerViewSampleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_sample);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new ExpandableAdapter(this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
