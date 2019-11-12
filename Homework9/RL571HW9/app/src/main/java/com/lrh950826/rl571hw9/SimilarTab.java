package com.lrh950826.rl571hw9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimilarTab extends Fragment implements SimilarAdapter.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private ProgressBar progressSm;
    private TextView waitingSm;
    private TextView noRlt;
    private RecyclerView recycler;
    private List<SimilarProduct> similarsList;
    private List<SimilarProduct> sortedList;
    private SimilarAdapter smAdapter;

    private Spinner spinner1;
    private Spinner spinner2;
    private List<String> spinnerList1;
    private List<String> spinnerList2;
    private ArrayAdapter adapter1;
    private ArrayAdapter adapter2;

    // Empty public constructor
    public SimilarTab() {
        // Just for requirement
    }

    // TODO: Rename and change types and number of parameters
    public static SimilarTab newInstance(String param1) {
        SimilarTab fragment = new SimilarTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            System.out.println(mParam1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_similar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initializes the view using super
        super.onViewCreated(view, savedInstanceState);

        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);

        similarsList = new ArrayList<>();

        spinnerList1 = new ArrayList<String>();
        spinnerList1.add("Default");
        spinnerList1.add("Name");
        spinnerList1.add("Price");
        spinnerList1.add("Days");
        adapter1 = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, spinnerList1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinnerList2 = new ArrayList<String>();
        spinnerList2.add("Ascending");
        spinnerList2.add("Descending");
        adapter2 = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, spinnerList2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setEnabled(false);

        progressSm = view.findViewById(R.id.progress_similar);
        waitingSm = view.findViewById(R.id.waiting_similar);
        noRlt = view.findViewById(R.id.noRlt_similar_tab);
        recycler = view.findViewById(R.id.recycler_similar);

        progressSm.setVisibility(View.VISIBLE);
        waitingSm.setVisibility(View.VISIBLE);
        noRlt.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            RequestQueue queue2 = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, mParam1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response2) {
                            try {
                                JSONObject thisJSONSimilar = new JSONObject(response2);
                                if (thisJSONSimilar.has("getSimilarItemsResponse")) {
                                    JSONObject jsonObjAll = thisJSONSimilar.getJSONObject("getSimilarItemsResponse");
                                    if (jsonObjAll.getString("ack").equals("Success")) {
                                        JSONArray similarArr = jsonObjAll.getJSONObject("itemRecommendations").getJSONArray("item");
                                        if (similarArr != null && similarArr.length() != 0) {
                                            for (int i = 0 ; i < similarArr.length() ; i ++) {
                                                JSONObject singleObj = similarArr.getJSONObject(i);
                                                String title;
                                                String productUrl = null;
                                                String pictureUrl = null;
                                                String shippingCost = null;
                                                String price = null;
                                                String daysLeft = null;

                                                title = singleObj.getString("title").trim();
                                                if (singleObj.has("viewItemURL")) {
                                                    productUrl = singleObj.getString("viewItemURL");
                                                }
                                                if (singleObj.has("imageURL")) {
                                                    pictureUrl = singleObj.getString("imageURL");
                                                }
                                                if (singleObj.has("buyItNowPrice")) {
                                                    price = singleObj.getJSONObject("buyItNowPrice").getString("__value__");
                                                }
                                                if (singleObj.has("shippingCost")) {
                                                    shippingCost = singleObj.getJSONObject("shippingCost").getString("__value__");
                                                }
                                                if (singleObj.has("timeLeft")) {
                                                    daysLeft = singleObj.getString("timeLeft");
                                                }

                                                SimilarProduct one = new SimilarProduct(title, pictureUrl, shippingCost,
                                                        daysLeft, price, productUrl);

                                                similarsList.add(one);
                                            }

                                            noRlt.setVisibility(View.GONE);
                                            progressSm.setVisibility(View.GONE);
                                            waitingSm.setVisibility(View.GONE);
                                            smAdapter = new SimilarAdapter(getContext(), similarsList, SimilarTab.this);
                                            recycler.setAdapter(smAdapter);
                                            recycler.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            throw new Exception();
                                        }
                                    }
                                    else {
                                        throw new Exception();
                                    }
                                }
                                else if (thisJSONSimilar == null) {
                                    throw new Exception();
                                }
                                else {
                                    throw new Exception();
                                }
                            } catch (Exception e) {
                                progressSm.setVisibility(View.GONE);
                                waitingSm.setVisibility(View.GONE);
                                noRlt.setVisibility(View.VISIBLE);
                                spinner1.setEnabled(false);
                                spinner2.setEnabled(false);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Please go back and check network...",
                            Toast.LENGTH_SHORT).show();
                }
            });

            queue2.add(stringRequest2);

        } catch (Exception e) {
            progressSm.setVisibility(View.GONE);
            waitingSm.setVisibility(View.GONE);
            noRlt.setVisibility(View.VISIBLE);
            spinner1.setEnabled(false);
            spinner2.setEnabled(false);
            e.printStackTrace();
        }

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sortedList = new ArrayList<>();

                for (int j = 0 ; j < similarsList.size() ; j ++) {
                    sortedList.add(similarsList.get(j));
                }

                if (adapter1.getItem(i).toString().contains("Default")) {
                    spinner2.setEnabled(false);
                }
                else {
                    spinner2.setEnabled(true);
                }

                if (adapter1.getItem(i).toString().contains("Name")) {
                    if (adapter2.getItem(spinner2.getSelectedItemPosition()).toString().contains("Ascending")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                return a.getTitle().compareTo(b.getTitle());
                            }
                        });
                    }
                    else {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                return b.getTitle().compareTo(a.getTitle());
                            }
                        });
                    }
                }

                if (adapter1.getItem(i).toString().contains("Price")) {
                    if (adapter2.getItem(spinner2.getSelectedItemPosition()).toString().contains("Ascending")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                Double a_price = Double.parseDouble(a.getPrice());
                                Double b_price = Double.parseDouble(b.getPrice());
                                return a_price.compareTo(b_price);
                            }
                        });
                    }
                    else {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                Double a_price = Double.parseDouble(a.getPrice());
                                Double b_price = Double.parseDouble(b.getPrice());
                                return b_price.compareTo(a_price);
                            }
                        });
                    }
                }

                if (adapter1.getItem(i).toString().contains("Days")) {
                    if (adapter2.getItem(spinner2.getSelectedItemPosition()).toString().contains("Ascending")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                String a_tempStr = a.getDaysLeft();
                                String b_tempStr = b.getDaysLeft();
                                int a_day = Integer.parseInt(a_tempStr.substring(a_tempStr.indexOf('P') + 1, a_tempStr.indexOf('D')));
                                int b_day = Integer.parseInt(b_tempStr.substring(b_tempStr.indexOf('P') + 1, b_tempStr.indexOf('D')));
                                return a_day - b_day;
                            }
                        });
                    }
                    else {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                String a_tempStr = a.getDaysLeft();
                                String b_tempStr = b.getDaysLeft();
                                int a_day = Integer.parseInt(a_tempStr.substring(a_tempStr.indexOf('P') + 1, a_tempStr.indexOf('D')));
                                int b_day = Integer.parseInt(b_tempStr.substring(b_tempStr.indexOf('P') + 1, b_tempStr.indexOf('D')));
                                return b_day - a_day;
                            }
                        });
                    }
                }

                smAdapter = new SimilarAdapter(getContext(), sortedList, new SimilarAdapter.OnItemClickListener() {
                    @Override
                    public void clickSimilar(int index) {
                        String productURL = sortedList.get(index).getProductUrl();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
                        startActivity(browserIntent);
                    }
                });

                recycler.setAdapter(smAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sortedList = new ArrayList<>();

                for (int j = 0 ; j < similarsList.size() ; j ++) {
                    sortedList.add(similarsList.get(j));
                }

                if (adapter2.getItem(i).toString().contains("Ascending")) {

                    if (adapter1.getItem(i).toString().contains("Name")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                return a.getTitle().compareTo(b.getTitle());
                            }
                        });
                    }

                    if (adapter1.getItem(i).toString().contains("Price")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                Double a_price = Double.parseDouble(a.getPrice());
                                Double b_price = Double.parseDouble(b.getPrice());
                                return a_price.compareTo(b_price);
                            }
                        });
                    }

                    if (adapter1.getItem(i).toString().contains("Days")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                String a_tempStr = a.getDaysLeft();
                                String b_tempStr = b.getDaysLeft();
                                int a_day = Integer.parseInt(a_tempStr.substring(a_tempStr.indexOf('P') + 1, a_tempStr.indexOf('D')));
                                int b_day = Integer.parseInt(b_tempStr.substring(b_tempStr.indexOf('P') + 1, b_tempStr.indexOf('D')));
                                return a_day - b_day;
                            }
                        });
                    }
                }

                if (adapter2.getItem(i).toString().contains("Descending")) {

                    if (adapter1.getItem(i).toString().contains("Name")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                return b.getTitle().compareTo(a.getTitle());
                            }
                        });
                    }

                    if (adapter1.getItem(i).toString().contains("Price")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                Double a_price = Double.parseDouble(a.getPrice());
                                Double b_price = Double.parseDouble(b.getPrice());
                                return b_price.compareTo(a_price);
                            }
                        });
                    }

                    if (adapter1.getItem(i).toString().contains("Days")) {
                        Collections.sort(sortedList, new Comparator<SimilarProduct>() {
                            public int compare(SimilarProduct a, SimilarProduct b) {
                                String a_tempStr = a.getDaysLeft();
                                String b_tempStr = b.getDaysLeft();
                                int a_day = Integer.parseInt(a_tempStr.substring(a_tempStr.indexOf('P') + 1, a_tempStr.indexOf('D')));
                                int b_day = Integer.parseInt(b_tempStr.substring(b_tempStr.indexOf('P') + 1, b_tempStr.indexOf('D')));
                                return b_day - a_day;
                            }
                        });
                    }
                }

                smAdapter = new SimilarAdapter(getContext(), sortedList, new SimilarAdapter.OnItemClickListener() {
                    @Override
                    public void clickSimilar(int index) {
                        String productURL = sortedList.get(index).getProductUrl();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
                        startActivity(browserIntent);
                    }
                });

                recycler.setAdapter(smAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }

    @Override
    public void clickSimilar(int index) {
        String productURL = similarsList.get(index).getProductUrl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productURL));
        startActivity(browserIntent);
    }

}
