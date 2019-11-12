package com.lrh950826.rl571hw9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.net.URLEncoder;
import android.support.design.widget.FloatingActionButton;

public class DetailPageActivity extends AppCompatActivity {

    private ImageView backBtnD;
    private ImageView facebookShare;
    private ProgressBar progressD;
    private TextView waitingD;
    private Toolbar toolbarD;
    private TabLayout tabs;
    private ViewPager viewPagerD;
    private Product thisProduct;
    private String thisJSON;
    private String thisJSONSec;
    private FloatingActionButton floatingBtn;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TextView titleDetail;
    private String googlePhotos;
    private String similarURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        backBtnD = findViewById(R.id.back_detail);
        progressD = findViewById(R.id.progress_detail);
        waitingD = findViewById(R.id.waiting_detail);
        facebookShare = findViewById(R.id.shareFB);
        toolbarD = findViewById(R.id.tool_bar_details);
        viewPagerD = findViewById(R.id.content_detail);
        tabs = findViewById(R.id.tabs_detail);
        floatingBtn = findViewById(R.id.detail_wishlist);
        titleDetail = findViewById(R.id.title_detail);

        progressD.setVisibility(View.VISIBLE);
        waitingD.setVisibility(View.VISIBLE);
        viewPagerD.setVisibility(View.GONE);
        tabs.getTabAt(0).setIcon(R.drawable.product_icon);
        tabs.getTabAt(1).setIcon(R.drawable.shipping_icon_un);
        tabs.getTabAt(2).setIcon(R.drawable.photos_icon_un);
        tabs.getTabAt(3).setIcon(R.drawable.similars_icon_un);

        Intent intent = getIntent();
        final String singleURL = intent.getStringExtra("singleUrl");
        similarURL = intent.getStringExtra("similarUrl");
        thisProduct = (Product) intent.getSerializableExtra("thisProduct");
        thisJSON = intent.getStringExtra("thisJSON");
        titleDetail.setText(thisProduct.getTitle().substring(0, 26) + "...");

        setSupportActionBar(toolbarD);
        if (thisProduct.getWanted()) {
            floatingBtn.setImageResource(R.drawable.remove_from_wishlist2);
        }
        else {
            floatingBtn.setImageResource(R.drawable.add_to_wishlist2);
        }

        backBtnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToItems();
            }
        });

        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisProduct.getWanted()) {
                    String msg = thisProduct.getTitleS() + " was removed from wish list";
                    SharedPreferences wanted = DetailPageActivity.this.getSharedPreferences("wishlist", DetailPageActivity.this.MODE_PRIVATE);
                    SharedPreferences.Editor edt = wanted.edit();

                    floatingBtn.setImageResource(R.drawable.add_to_wishlist2);
                    Toast.makeText(DetailPageActivity.this, msg, Toast.LENGTH_SHORT).show();
                    edt.remove(thisProduct.getItemId());
                    edt.apply();
                    thisProduct.change();
                }
                else {
                    String msg = thisProduct.getTitleS() + " was added to wish list";
                    SharedPreferences wanted = DetailPageActivity.this.getSharedPreferences("wishlist", DetailPageActivity.this.MODE_PRIVATE);
                    SharedPreferences.Editor edt = wanted.edit();

                    floatingBtn.setImageResource(R.drawable.remove_from_wishlist2);
                    Toast.makeText(DetailPageActivity.this, msg, Toast.LENGTH_SHORT).show();
                    edt.putString(thisProduct.getItemId(), thisJSON);
                    edt.apply();
                    thisProduct.change();
                }
            }
        });

        // --------------- ViewPager ---------------
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPagerD.setAdapter(sectionsPagerAdapter);

        viewPagerD.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPagerD));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                tabs.getTabAt(0).setIcon((position == 0 ? R.drawable.product_icon : R.drawable.product_icon_un));
                tabs.getTabAt(1).setIcon((position == 1 ? R.drawable.shipping_icon : R.drawable.shipping_icon_un));
                tabs.getTabAt(2).setIcon((position == 2 ? R.drawable.photos_icon : R.drawable.photos_icon_un));
                tabs.getTabAt(3).setIcon((position == 3 ? R.drawable.similars_icon : R.drawable.similars_icon_un));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab){
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, singleURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            if (responseObject.getString("Ack").equals("Success")) {
                                if (responseObject.has("Item")) {
                                    JSONObject singleObj = responseObject.getJSONObject("Item");
                                    thisJSONSec = singleObj.toString();
                                    // --------------- Facebook Share ---------------
                                    String link = "";
                                    String title = "";
                                    String currentPrice;
                                    if (singleObj.has("ViewItemURLForNaturalSearch")) {
                                        link = singleObj.getString("ViewItemURLForNaturalSearch");
                                        link = URLEncoder.encode(link, "UTF-8");
                                    }
                                    if (singleObj.has("Title")) {
                                        title = singleObj.getString("Title");
                                        googlePhotos = "http://csci571-lurh950826-hw9.us-east-2.elasticbeanstalk.com/g1/g2/" +
                                                URLEncoder.encode(title, "UTF-8");
                                    }
                                    if (singleObj.has("CurrentPrice")) {
                                        currentPrice = singleObj.getJSONObject("CurrentPrice").getString("Value");
                                    } else {
                                        currentPrice = "N/A";
                                    }
                                    String quoteText = "Buy " + title + " for $" + currentPrice + " from Ebay!";
                                    quoteText = URLEncoder.encode(quoteText, "UTF-8");
                                    final String facebookURL = "https://www.facebook.com/dialog/share?app_id=2264019113814264&display=popup&href=" +
                                            link + "&quote=" + quoteText + "&hashtag=" + URLEncoder.encode("#CSCI571Spring2019Ebay", "UTF-8");
                                    facebookShare.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intentShare = new Intent(Intent.ACTION_VIEW);
                                            intentShare.setData(Uri.parse(facebookURL));
                                            startActivity(intentShare);
                                        }
                                    });
                                }
                                progressD.setVisibility(View.GONE);
                                waitingD.setVisibility(View.GONE);
                                viewPagerD.setVisibility(View.VISIBLE);
                            }
                            else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), "Please go back and check network...",
                        Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void goBackToItems() {
        finish();
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
            switch (position) {
                case 0: return ProductTab.newInstance(thisJSONSec, thisJSON);
                case 1: return ShippingTab.newInstance(thisJSONSec, thisJSON);
                case 2: return PhotosTab.newInstance(googlePhotos);
                case 3: return SimilarTab.newInstance(similarURL);
            }
            return ProductTab.newInstance(thisJSONSec, thisJSON);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


}
