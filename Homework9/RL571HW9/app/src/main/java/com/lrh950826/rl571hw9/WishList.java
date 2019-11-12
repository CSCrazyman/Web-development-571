package com.lrh950826.rl571hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;

public class WishList extends Fragment implements LinearAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerWish;
    private List<Product> wishList = new ArrayList<>();
    private TextView noWishes;
    private TextView wish_items;
    private TextView wish_price;
    private GridLayout wishlistLayout;

    private OnFragmentInteractionListener listener;

    // Empty public constructor
    public WishList() {
        // Just for requirement
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment form.
     */
    // TODO: Rename and change types and number of parameters
    public static WishList newInstance(String param1, String param2) {
        WishList fragment = new WishList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        recyclerWish = view.findViewById(R.id.recycler_wishlist);
        recyclerWish.setLayoutManager(new LinearLayoutManager(getActivity()));
        noWishes = view.findViewById(R.id.noRlt_wishlist);
        wishlistLayout = view.findViewById(R.id.wishlistLayout);
        wish_items = getActivity().findViewById(R.id.wish_total_text);
        wish_price = getActivity().findViewById(R.id.wish_total_price);

        if (wishList.size() == 0) {
            wish_items.setText("Wishlist total(0 item):");
            wish_price.setText("$0.00");
            noWishes.setVisibility(View.VISIBLE);
            wishlistLayout.setVisibility(View.GONE);
        }
        else {
            Double money = 0.0;
            DecimalFormat df = new DecimalFormat("#########.##");
            for (int x = 0 ; x < wishList.size() ; x ++) {
                if (wishList.get(x).getPrice() != null) {
                    money += Double.parseDouble(wishList.get(x).getPrice());
                }
            }
            if (wishList.size() == 1) {
                wish_items.setText("Wishlist total(" + wishList.size() + " item):");
            }
            else {
                wish_items.setText("Wishlist total(" + wishList.size() + " items):");
            }
            wish_price.setText("$" + df.format(money));
            noWishes.setVisibility(View.GONE);
            wishlistLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        wishList.clear();
        SharedPreferences shared = this.getActivity().getSharedPreferences("wishlist", getActivity().MODE_PRIVATE);
        Map<String, ?> storageMap = shared.getAll();

        for (Map.Entry<String, ?> entry : storageMap.entrySet()) {
            String str = shared.getString(entry.getKey(), " ");
            try {
                String title;
                String itemId;
                String pictureUrl = null;
                String zipcode = null;
                String shippingCost = null;
                String condition = null;
                String price = null;
                boolean isWanted = true;
                JSONObject singleObj = new JSONObject(str);

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
                Product one = new Product(title, itemId, pictureUrl, zipcode,
                        shippingCost, condition, price, isWanted);

                wishList.add(one);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerWish.setAdapter(new LinearAdapter(getActivity(), wishList, this));

        if (wishList.size() == 0) {
            wish_items.setText("Wishlist total(0 item):");
            wish_price.setText("$0.00");
            noWishes.setVisibility(View.VISIBLE);
            wishlistLayout.setVisibility(View.GONE);
        }
        else {
            Double money = 0.0;
            DecimalFormat df = new DecimalFormat("#########.##");
            for (int x = 0 ; x < wishList.size() ; x ++) {
                if (wishList.get(x).getPrice() != null) {
                    money += Double.parseDouble(wishList.get(x).getPrice());
                }
            }
            if (wishList.size() == 1) {
                wish_items.setText("Wishlist total(" + wishList.size() + " item):");
            }
            else {
                wish_items.setText("Wishlist total(" + wishList.size() + " items):");
            }
            wish_price.setText("$" + df.format(money));
            noWishes.setVisibility(View.GONE);
            wishlistLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void clickToDetail(int index) {
            String itemIdSub = wishList.get(index).getItemId();
            SharedPreferences shared = this.getActivity().getSharedPreferences("wishlist", Context.MODE_PRIVATE);
            Map<String, ?> storageMap = shared.getAll();
            Intent intentD = new Intent(getActivity(), DetailPageActivity.class);
            intentD.putExtra("singleUrl", "http://csci571-lurh950826-hw9.us-east-2.elasticbeanstalk.com/itemF/" + itemIdSub);
            intentD.putExtra("similarUrl", "http://csci571-lurh950826-hw9.us-east-2.elasticbeanstalk.com/s1/s2/s3/" + itemIdSub);
            intentD.putExtra("thisProduct", wishList.get(index));
            intentD.putExtra("thisJSON", storageMap.get(itemIdSub).toString());
            startActivity(intentD);
    }

    @Override
    public void changeWish(int index, ImageView view) {

        String msg = wishList.get(index).getTitleS() + " was removed from wish list";
        String itemId = wishList.get(index).getItemId();
        SharedPreferences sharedPre = this.getActivity().getSharedPreferences("wishlist", Context.MODE_PRIVATE);
        Map<String, ?> storageMappp = sharedPre.getAll();
        SharedPreferences.Editor edt = sharedPre.edit();

        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        wishList.get(index).change();
        edt.remove(itemId);
        edt.commit();
        wishList.remove(index);

        if (wishList.size() == 0) {
            wish_items.setText("Wishlist total(0 item):");
            wish_price.setText("$0.00");
            noWishes.setVisibility(View.VISIBLE);
            wishlistLayout.setVisibility(View.GONE);
        }
        else {
            Double money = 0.0;
            DecimalFormat df = new DecimalFormat("#########.##");
            for (int x = 0 ; x < wishList.size() ; x ++) {
                if (wishList.get(x).getPrice() != null) {
                    money += Double.parseDouble(wishList.get(x).getPrice());
                }
            }
            if (wishList.size() == 1) {
                wish_items.setText("Wishlist total(" + wishList.size() + " item):");
            }
            else {
                wish_items.setText("Wishlist total(" + wishList.size() + " items):");
            }
            wish_price.setText("$" + df.format(money));
            noWishes.setVisibility(View.GONE);
            wishlistLayout.setVisibility(View.VISIBLE);
        }
        recyclerWish.setAdapter(new LinearAdapter(getActivity(), wishList, this));

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }

}
