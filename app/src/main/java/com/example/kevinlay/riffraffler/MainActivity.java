package com.example.kevinlay.riffraffler;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.kevinlay.riffraffler.fragments.CompletedRafflesFragment;
import com.example.kevinlay.riffraffler.fragments.MyRafflesFragment;
import com.example.kevinlay.riffraffler.fragments.BrowerRafflesPageFragment;
import com.example.kevinlay.riffraffler.adapter.RaffleFragmentPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int[] tabIcons = {
            R.drawable.ic_account_box,
            R.drawable.ic_ticket_confirmation,
            R.drawable.ic_message_text
    };

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        this.setTitle(this.getString(R.string.activity_title_user_id) + mAuth.getUid());

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        RaffleFragmentPagerAdapter adapter = new RaffleFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        MyRafflesFragment myRafflesFragment = new MyRafflesFragment();
        CompletedRafflesFragment completedRafflesFragment = new CompletedRafflesFragment();
        BrowerRafflesPageFragment browerRafflesPageFragment = new BrowerRafflesPageFragment();

        adapter.addFragment(myRafflesFragment);
        adapter.addFragment(completedRafflesFragment);
        adapter.addFragment(browerRafflesPageFragment);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_menu2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_action:
                mAuth.signOut();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
        }
        return true;
    }
}
