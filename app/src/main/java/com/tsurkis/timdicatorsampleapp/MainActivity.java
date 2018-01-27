package com.tsurkis.timdicatorsampleapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.tsurkis.timdicator.Timdicator;
import com.tsurkis.timdicator.TimdicatorBinder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), getDataCollectionOfColors()));

        TimdicatorBinder.attachViewPagerDynamically((Timdicator) findViewById(R.id.timdicator), viewPager);
    }

    private List<String> getDataCollectionOfColors() {
        List<String> colors = new ArrayList<>();
        colors.add("#910505");
        colors.add("#74f441");
        colors.add("#f4bb41");
        return colors;
    }
}
