package com.bbb.bbdev1.run;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navigationView;
    ViewPager pager;
    TabsFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigation_main);
        navigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);

        //pageAdapter = new TabsFragmentPagerAdapter(getSupportFragmentManager());
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new StartFragment());
        fragments.add(new HistoryFragment());
        pager = findViewById(R.id.viewpager);
        adapter = new TabsFragmentPagerAdapter(getSupportFragmentManager(), fragments, fragments.size());
        pager.setAdapter(adapter);


        if (savedInstanceState == null) {
            navigationView.setSelectedItemId(R.id.tab_start);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.tab_start:
                pager.setCurrentItem(0);
                Toast.makeText(MainActivity.this, "On Start tab", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tab_history:
                pager.setCurrentItem(1);
                Toast.makeText(MainActivity.this, "On History tab", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
