package com.lrh950826.rl571hw9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.text.SpannableStringBuilder;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ResultPageActivity extends AppCompatActivity implements LinearAdapter.OnItemClickListener {

    private JSONArray items;
    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView waiting;
    private TextView proTotalMsg;
    private TextView noRlts;
    private TextView errorRlt;
    private ImageView backBtn;
    private GridLayout productsLayout;
    private List<Product> productsList;
    private LinearAdapter linear;
    private LinearAdapter linearSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        progress = findViewById(R.id.progress);
        waiting = findViewById(R.id.waiting);
        noRlts = findViewById(R.id.noRlt_result);
        errorRlt = findViewById(R.id.error_result);
        proTotalMsg = findViewById(R.id.products_num_text);
        backBtn = findViewById(R.id.back);
        recycler = findViewById(R.id.recycler);
        productsLayout = findViewById(R.id.productsLayout);

        progress.setVisibility(View.VISIBLE);
        waiting.setVisibility(View.VISIBLE);
        noRlts.setVisibility(View.GONE);
        errorRlt.setVisibility(View.GONE);
        proTotalMsg.setVisibility(View.GONE);
        productsLayout.setVisibility(View.GONE);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        productsList = new ArrayList<>();

        Intent intent = getIntent();
        final String productsURL = intent.getStringExtra("productsUrl");
        final String keyword = intent.getStringExtra("key");
        final SharedPreferences sharedMain = this.getSharedPreferences("wishlist", this.MODE_PRIVATE);

        Log.d("result table", productsURL);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToSearch();
            }
        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, productsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        noRlts.setVisibility(View.GONE);
                        errorRlt.setVisibility(View.GONE);
                        proTotalMsg.setVisibility(View.GONE);
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            Map<String, ?> sharedMap = sharedMain.getAll();
                            if (responseObject.has("findItemsAdvancedResponse")) {
                                JSONObject jsonObjAll = responseObject.getJSONArray("findItemsAdvancedResponse").getJSONObject(0);
                                if (jsonObjAll.getJSONArray("ack").getString(0).equals("Success")) {
                                    JSONObject jsonObjSR = jsonObjAll.getJSONArray("searchResult").getJSONObject(0);
                                    String number = jsonObjSR.getString("@count");
                                    if (!number.equals("0")) {
                                        JSONArray itemsArray = jsonObjSR.getJSONArray("item");
                                        items = jsonObjSR.getJSONArray("item");

                                        String title;
                                        String itemId;
                                        String pictureUrl = null;
                                        String zipcode = null;
                                        String shippingCost = null;
                                        String condition = null;
                                        String price = null;
                                        boolean isWanted;

                                        for (int i = 0 ; i < itemsArray.length() ; i ++) {
                                            JSONObject singleObj = itemsArray.getJSONObject(i);
                                            itemId = singleObj.getJSONArray("itemId").getString(0);
                                            title = singleObj.getJSONArray("title").getString(0);
                                            if (singleObj.has("galleryURL")) {
                                                pictureUrl = singleObj.getJSONArray("galleryURL").getString(0);
                                            }
                                            if (singleObj.has("shippingInfo")) {
                                                JSONObject singleShipArr = singleObj.getJSONArray("shippingInfo").getJSONObject(0);
                                                if (singleShipArr.has("shippingServiceCost")) {
                                                    shippingCost = singleShipArr.getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                                                }
                                            }
                                            if (singleObj.has("postalCode")) {
                                                zipcode = singleObj.getJSONArray("postalCode").getString(0);
                                            }
                                            if (singleObj.has("condition")) {
                                                JSONObject singleConArr = singleObj.getJSONArray("condition").getJSONObject(0);
                                                if (singleConArr.has("conditionDisplayName")) {
                                                    condition = singleConArr.getJSONArray("conditionDisplayName").getString(0);
                                                }
                                            }
                                            if (singleObj.has("sellingStatus")) {
                                                JSONObject singlePriceArr = singleObj.getJSONArray("sellingStatus").getJSONObject(0);
                                                if (singlePriceArr.has("currentPrice")) {
                                                    price = singlePriceArr.getJSONArray("currentPrice").getJSONObject(0).getString("__value__");
                                                }
                                            }
                                            if (sharedMap.containsKey(itemId)) {
                                                isWanted = true;
                                            } else {
                                                isWanted = false;
                                            }
                                            Product one = new Product(title, itemId, pictureUrl, zipcode,
                                                    shippingCost, condition, price, isWanted);
                                            productsList.add(one);
                                        }
                                        waitingDone();
                                        showTotalText(number, keyword);
                                        linear = new LinearAdapter(ResultPageActivity.this, productsList, ResultPageActivity.this);
                                        recycler.setAdapter(linear);
                                        productsLayout.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        throw new Exception();
                                    }
                                }
                                else {
                                    throw new Exception();
                                }
                            }
                            else if (responseObject == null) {
                                throw new Exception();
                            }
                            else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            errorRlt.setVisibility(View.GONE);
                            proTotalMsg.setVisibility(View.GONE);
                            productsLayout.setVisibility(View.GONE);
                            waitingDone();
                            noRlts.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noRlts.setVisibility(View.GONE);
                proTotalMsg.setVisibility(View.GONE);
                productsLayout.setVisibility(View.GONE);
                System.out.println("Error!");
                Toast.makeText(getBaseContext(), "Please go back and check network...",
                        Toast.LENGTH_SHORT).show();
                waitingDone();
                errorRlt.setVisibility(View.VISIBLE);
            }
        });
        queue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ListIterator<Product> listIterator = productsList.listIterator();
        SharedPreferences sharedSubT = this.getSharedPreferences("wishlist", this.MODE_PRIVATE);
        Map<String, ?> sharedMap = sharedSubT.getAll();
        while (listIterator.hasNext()) {
            Product oneSub = listIterator.next();
            boolean has = oneSub.getWanted();
            if ((sharedMap.containsKey(oneSub.getItemId()) && has == false)
                    || (!sharedMap.containsKey(oneSub.getItemId()) && has == true)) {
                oneSub.change();
                linearSub = new LinearAdapter(ResultPageActivity.this, productsList, ResultPageActivity.this);
                recycler.setAdapter(linearSub);
            }
        }

    }

    @Override
    public void clickToDetail(int index) {
        try {
            Intent intentD = new Intent(ResultPageActivity.this, DetailPageActivity.class);
            intentD.putExtra("singleUrl", "http://csci571-lurh950826-hw9.us-east-2.elasticbeanstalk.com/itemF/" + productsList.get(index).getItemId());
            intentD.putExtra("similarUrl", "http://csci571-lurh950826-hw9.us-east-2.elasticbeanstalk.com/s1/s2/s3/" + productsList.get(index).getItemId());
            intentD.putExtra("thisProduct", productsList.get(index));
            intentD.putExtra("thisJSON", items.getJSONObject(index).toString());
            startActivity(intentD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeWish(int index, ImageView view) {
        try {
            if (productsList.get(index).getWanted()) {
                String msg = productsList.get(index).getTitleS() + " was removed from wish list";
                SharedPreferences wanted = this.getSharedPreferences("wishlist", this.MODE_PRIVATE);
                SharedPreferences.Editor edt = wanted.edit();

                view.setImageResource(R.drawable.add_to_wishlist);
                Toast.makeText(ResultPageActivity.this, msg, Toast.LENGTH_SHORT).show();
                edt.remove(productsList.get(index).getItemId());
                edt.apply();
                productsList.get(index).change();
            }
            else {
                String msg = productsList.get(index).getTitleS() + " was added to wish list";
                JSONObject item = items.getJSONObject(index);
                SharedPreferences wanted = this.getSharedPreferences("wishlist", this.MODE_PRIVATE);
                SharedPreferences.Editor edt = wanted.edit();

                view.setImageResource(R.drawable.remove_from_wishlist);
                Toast.makeText(ResultPageActivity.this, msg, Toast.LENGTH_SHORT).show();
                edt.putString(productsList.get(index).getItemId(), item.toString());
                edt.apply();
                productsList.get(index).change();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void waitingDone() {
        progress = findViewById(R.id.progress);
        waiting = findViewById(R.id.waiting);
        progress.setVisibility(View.GONE);
        waiting.setVisibility(View.GONE);
    }


    private void showTotalText(String num, final String key) {
        proTotalMsg = findViewById(R.id.products_num_text);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1= new SpannableString("Showing ");
        SpannableString str2= new SpannableString(num);
        SpannableString str3= new SpannableString(" results for ");
        SpannableString str4= new SpannableString(key);
        str2.setSpan(new ForegroundColorSpan(Color.rgb(255, 87, 34)), 0, str2.length(), 0);
        str4.setSpan(new ForegroundColorSpan(Color.rgb(255, 87, 34)), 0, str4.length(), 0);
        builder.append(str1);
        builder.append(str2);
        builder.append(str3);
        builder.append(str4);
        proTotalMsg.setText(builder, TextView.BufferType.SPANNABLE);
        proTotalMsg.setVisibility(View.VISIBLE);
    }

    private void goBackToSearch() {
        finish();
    }

}
