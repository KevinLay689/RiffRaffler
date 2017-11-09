package com.example.kevinlay.riffraffler;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.kevinlay.riffraffler.Fragments.CompletedRafflesFragment;
import com.example.kevinlay.riffraffler.Fragments.MyRafflesFragment;
import com.example.kevinlay.riffraffler.Fragments.PageFragment;
import com.example.kevinlay.riffraffler.adapter.SampleFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        adapter.addFragment(new MyRafflesFragment());
        adapter.addFragment(new CompletedRafflesFragment());
        adapter.addFragment(new PageFragment());
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


}
