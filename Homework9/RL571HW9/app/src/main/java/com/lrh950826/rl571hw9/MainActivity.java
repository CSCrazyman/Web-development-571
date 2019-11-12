package com.lrh950826.rl571hw9;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements Form.OnFormFragmentInteractionListener {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabs;
    private Toolbar toolbar;
    private Toolbar toolbar_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // --------------- Splash Screen ---------------
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        tabs = findViewById(R.id.tabs);
        toolbar_total = findViewById(R.id.tool_bar_details);
        toolbar_total.setVisibility(View.GONE);

        // --------------- ViewPager ---------------
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.content);
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                if (position == 0) {
                    toolbar_total.setVisibility(View.GONE);
                }
                else if (position == 1) {
                    toolbar_total.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return Form.newInstance("Fragment 1", " form");
            }
            if (position == 1) {
                return WishList.newInstance("Fragment 2, ", "wishlist");
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
