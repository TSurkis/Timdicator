package com.tsurkis.timdicatorsampleapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tsurkis.timdicator.Timdicator;
import com.tsurkis.timdicator.TimdicatorBinder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> colors;

    private Button button;

    private ViewPager viewPager;

    private RecyclerView recyclerView;

    private Timdicator timdicator;

    private PagerSnapHelper snapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.switch_layouts_button);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), getDataCollectionOfColors()));

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.GONE);
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        timdicator = findViewById(R.id.timdicator);

        TimdicatorBinder.attachRecyclerView(timdicator, recyclerView, snapHelper);

        recyclerView.scrollToPosition(3);

        button.setText("switch to RecyclerView");
        setViewPagerVisible();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getVisibility() == View.VISIBLE) {
                    button.setText("switch to ViewPager");
                    viewPager.setVisibility(View.GONE);
                    setRecyclerViewVisible();
                } else {
                    button.setText("switch to RecyclerView");
                    recyclerView.setVisibility(View.GONE);
                    setViewPagerVisible();
                }
            }
        });
    }

    private void setViewPagerVisible() {
        viewPager.setVisibility(View.VISIBLE);
        TimdicatorBinder.attachViewPagerDynamically(timdicator, viewPager);
    }

    private void setRecyclerViewVisible() {
        recyclerView.setVisibility(View.VISIBLE);
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new Adapter(getDataCollectionOfColors()));
        }
    }

    private List<String> getDataCollectionOfColors() {
        if (colors == null) {
            colors = new ArrayList<>();
            colors.add("#910505");
            colors.add("#74f441");
            colors.add("#f4bb41");
            colors.add("#f5b890");
            colors.add("#807f3f");
        }
        return colors;
    }
}
