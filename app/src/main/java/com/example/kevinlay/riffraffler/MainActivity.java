package com.example.kevinlay.riffraffler;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kevinlay.riffraffler.fragments.CompletedRafflesFragment;
import com.example.kevinlay.riffraffler.fragments.MyRafflesFragment;
import com.example.kevinlay.riffraffler.fragments.MessagePageFragment;
import com.example.kevinlay.riffraffler.adapter.RaffleFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String s = getIntent().getStringExtra("idKey");
        Log.i(TAG, "onCreate: "+ s );

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        RaffleFragmentPagerAdapter adapter = new RaffleFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        adapter.addFragment(new MyRafflesFragment());
        adapter.addFragment(new CompletedRafflesFragment());
        adapter.addFragment(new MessagePageFragment());
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


}
