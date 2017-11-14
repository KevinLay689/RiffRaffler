package com.example.kevinlay.riffraffler;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kevinlay.riffraffler.adapter.MessagesAdapter;
import com.example.kevinlay.riffraffler.fragments.CompletedRafflesFragment;
import com.example.kevinlay.riffraffler.fragments.MyRafflesFragment;
import com.example.kevinlay.riffraffler.fragments.MessagePageFragment;
import com.example.kevinlay.riffraffler.adapter.RaffleFragmentPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        String id = getIntent().getStringExtra("idKey");
        String key = "key";
        Bundle bundle = new Bundle();
        bundle.putString(key, id);
        Log.i(TAG, "onCreate: "+ id );

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        RaffleFragmentPagerAdapter adapter = new RaffleFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this);

        MyRafflesFragment myRafflesFragment = new MyRafflesFragment();
        myRafflesFragment.setArguments(bundle);

        CompletedRafflesFragment completedRafflesFragment = new CompletedRafflesFragment();
        completedRafflesFragment.setArguments(bundle);

        MessagePageFragment messagePageFragment = new MessagePageFragment();
        messagePageFragment.setArguments(bundle);

        adapter.addFragment(myRafflesFragment);
        adapter.addFragment(completedRafflesFragment);
        adapter.addFragment(messagePageFragment);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


}
