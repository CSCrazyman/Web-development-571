package com.lrh950826.rl571hw9;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.HorizontalScrollView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import android.support.v4.view.ViewPager;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout mGallery;
    private int[] mImgIds;
    private LayoutInflater mInflater;
    private HorizontalScrollView horizontalScrollView;

    private JSONObject thisObj;
    private JSONObject thisObjPro;
    private JSONArray thisArr;
    private ImageView singlePhoto;
    private TextView titleShow;
    private TextView priceShow;
    private TextView shippingCostShow;
    private ViewPager seperateLine1;
    private ViewPager seperateLine2;
    private ImageView highlightView;
    private ImageView specView;
    private TextView highlightText;
    private TextView specText;
    private TextView specList;
    private TextView subtitle;
    private TextView price;
    private TextView brand;
    private TextView subtitle2;
    private TextView price2;
    private TextView brand2;

    // Empty public constructor
    public ProductTab() {
        // Just for requirement
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * //     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductTab newInstance(String param1, String param2) {
        ProductTab fragment = new ProductTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        try {
            thisObj = new JSONObject(mParam1);
            thisObjPro = new JSONObject(mParam2);
            if (thisObj.has("PictureURL")) {
                thisArr = thisObj.getJSONArray("PictureURL");
            }
            else {
                thisArr = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initializes the view using super
        super.onViewCreated(view, savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        horizontalScrollView = view.findViewById(R.id.scroll_view);
        singlePhoto = view.findViewById(R.id.if_single_photo);
        titleShow = view.findViewById(R.id.product_tab_title);
        priceShow = view.findViewById(R.id.product_tab_price);
        shippingCostShow = view.findViewById(R.id.product_tab_spcost);
        seperateLine1 = view.findViewById(R.id.seperate_line1);
        seperateLine2 = view.findViewById(R.id.seperate_line2);
        highlightView = view.findViewById(R.id.product_HL);
        specView = view.findViewById(R.id.product_specifics);
        highlightText = view.findViewById(R.id.highlight_text);
        specText = view.findViewById(R.id.product_specifics_text);
        specList = view.findViewById(R.id.product_specifics_list);
        subtitle = view.findViewById(R.id.product_tab_subtitle);
        price = view.findViewById(R.id.product_tab_price_HL);
        brand = view.findViewById(R.id.product_tab_brand);
        subtitle2 = view.findViewById(R.id.product_subtitle_text);
        price2 = view.findViewById(R.id.product_price_text);
        brand2 = view.findViewById(R.id.product_brand_text);

        initView(view);
        initSectionOne();
        initSectionTwo();
    }


    private void initView(@NonNull View view) {
        mGallery = view.findViewById(R.id.id_gallery);
        try {
            if (thisArr == null) {
                horizontalScrollView.setVisibility(View.VISIBLE);
                mGallery.setVisibility(View.GONE);
                singlePhoto.setVisibility(View.VISIBLE);
                singlePhoto.setImageResource(R.drawable.loading_failed);
            }
            else if (thisArr.length() == 1) {
                horizontalScrollView.setVisibility(View.VISIBLE);
                mGallery.setVisibility(View.GONE);
                singlePhoto.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(thisArr.getString(0)).resize(270, 270).into(singlePhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        singlePhoto.setImageResource(R.drawable.loading_failed);
                    }
                });
            }
            else {
                horizontalScrollView.setVisibility(View.VISIBLE);
                singlePhoto.setVisibility(View.GONE);
                mGallery.setVisibility(View.VISIBLE);
                for (int i = 0; i < thisArr.length(); i++) {
                    View viewSub = mInflater.inflate(R.layout.activity_gallery_item, mGallery, false);
                    final ImageView img = viewSub.findViewById(R.id.id_index_gallery_item_image);
                    Picasso.with(getContext()).load(thisArr.getString(i)).resize(270, 270).into(img, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            img.setImageResource(R.drawable.loading_failed);
                        }
                    });
                    mGallery.addView(viewSub);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initSectionOne() {
        try {
            titleShow.setText(thisObj.getString("Title"));
            priceShow.setText(thisObj.has("CurrentPrice") ? "$" + thisObj.getJSONObject("CurrentPrice").getString("Value") : "N/A");
            if (thisObjPro.has("shippingInfo")) {
                JSONObject singleShipArrD = thisObjPro.getJSONArray("shippingInfo").getJSONObject(0);
                if (singleShipArrD.has("shippingServiceCost")) {
                    String shippingCostD = singleShipArrD.getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                    if (shippingCostD.equals("0.0")) {
                        shippingCostShow.setText("With Free Shipping");
                    }
                    else {
                        shippingCostShow.setText("With $" + shippingCostD + " Shipping");
                    }
                }
                else {
                    shippingCostShow.setText("N/A");
                }
            }
            else {
                shippingCostShow.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSectionTwo() {
        try {
            boolean hasFirst = false;
            boolean hasSecond = false;
            boolean hasBrand = false;

            if (thisObjPro.has("subtitle")) {
                hasFirst = true;
                subtitle.setVisibility(View.VISIBLE);
                subtitle2.setVisibility(View.VISIBLE);
                subtitle2.setText(thisObjPro.getJSONArray("subtitle").getString(0));
            }
            else {
                subtitle.setVisibility(View.GONE);
                subtitle2.setVisibility(View.GONE);
            }

            if (thisObj.has("CurrentPrice")) {
                hasFirst = true;
                price.setVisibility(View.VISIBLE);
                price2.setVisibility(View.VISIBLE);
                price2.setText("$" + thisObj.getJSONObject("CurrentPrice").getString("Value"));
            }
            else {
                price.setVisibility(View.GONE);
                price2.setVisibility(View.GONE);
            }

            if (thisObj.has("ItemSpecifics")) {
                JSONArray nameValPair = thisObj.getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
                hasSecond = true;
                String content = "";
                for (int i = 0 ; i < nameValPair.length() ; i ++) {
                    JSONArray values = nameValPair.getJSONObject(i).getJSONArray("Value");
                    if (nameValPair.getJSONObject(i).getString("Name").equals("Brand")) {
                        hasFirst = true;
                        hasBrand = true;
                        String subStr = "";
                        for (int j = 0 ; j < values.length() ; j ++) {
                            subStr += values.getString(j);
                            if (j != values.length() - 1) {
                                subStr += ", ";
                            }
                        }
                        brand.setVisibility(View.VISIBLE);
                        brand2.setVisibility(View.VISIBLE);
                        brand2.setText(subStr);
                        if (nameValPair.length() == 1) {
                            content = "· " + subStr;
                        }
                        else {
                            content =  "· " + subStr + "\n" + content;
                        }
                    }
                    else {
                        String subStr = "";
                        for (int j = 0 ; j < values.length() ; j ++) {
                            subStr += values.getString(j);
                            if (j != values.length() - 1) {
                                subStr += ", ";
                            }
                        }
                        content += "· " + subStr;
                        if (i != nameValPair.length() - 1) {
                            content += "\n";
                        }
                    }
                }
                specList.setText(content);
            }

            if (hasFirst) {
                seperateLine1.setVisibility(View.VISIBLE);
                highlightText.setVisibility(View.VISIBLE);
                highlightView.setVisibility(View.VISIBLE);
            }
            else {
                seperateLine1.setVisibility(View.GONE);
                highlightText.setVisibility(View.GONE);
                highlightView.setVisibility(View.GONE);
            }

            if (!hasBrand) {
                brand.setVisibility(View.GONE);
                brand2.setVisibility(View.GONE);
            }

            if (hasSecond) {
                specList.setVisibility(View.VISIBLE);
                specText.setVisibility(View.VISIBLE);
                specView.setVisibility(View.VISIBLE);
                seperateLine2.setVisibility(View.VISIBLE);
            }
            else {
                specList.setVisibility(View.GONE);
                specText.setVisibility(View.GONE);
                specView.setVisibility(View.GONE);
                seperateLine2.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
