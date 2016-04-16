package com.catwoman.lifetodo.activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.HomePagerAdapter;
import com.catwoman.lifetodo.fragments.PlansFragment;
import com.catwoman.lifetodo.models.Plan;

public class HomeActivity extends AppCompatActivity {

    static final int ADD_PLAN_REQUEST = 1;

    private HomePagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        setListeners();
    }

    private void setListeners() {
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
                toolbar.setAlpha(1 - percentage);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animateFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddPlanActivity.class);
                startActivityForResult(intent, ADD_PLAN_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_PLAN_REQUEST) {
            if (resultCode == RESULT_OK) {
                Plan plan = data.getParcelableExtra("plan");
                PlansFragment fragment = (PlansFragment) getSupportFragmentManager()
                        .findFragmentByTag("android:switcher:" + R.id.container + ":0");
                if (null != fragment) {
                    fragment.addItem(plan);
                }
            }
        }
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                fab.show();
                break;
            default:
                fab.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
