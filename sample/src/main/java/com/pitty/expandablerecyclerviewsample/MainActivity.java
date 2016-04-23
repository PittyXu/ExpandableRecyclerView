package com.pitty.expandablerecyclerviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.pitty.expandablerecyclerviewsample.horizontal.HorizontalLinearRecyclerViewSampleActivity;
import com.pitty.expandablerecyclerviewsample.vertical.VerticalLinearRecyclerViewSampleActivity;

public class MainActivity extends AppCompatActivity {

    private Button mVerticalSampleButton;
    private Button mHorizontalSampleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVerticalSampleButton = (Button) findViewById(R.id.activity_main_vertical_linear_sample_button);
        mVerticalSampleButton.setOnClickListener(mVerticalSampleButtonClickListener);

        mHorizontalSampleButton = (Button) findViewById(R.id.activity_main_horizontal_linear_sample_button);
        mHorizontalSampleButton.setOnClickListener(mHorizontalSampleButtonClickListener);

    }

    private View.OnClickListener mVerticalSampleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(VerticalLinearRecyclerViewSampleActivity.newIntent(v.getContext()));
        }
    };

    private View.OnClickListener mHorizontalSampleButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(HorizontalLinearRecyclerViewSampleActivity.newIntent(v.getContext()));
        }
    };
}
