package com.lrh950826.rl571hw9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.wssholmes.stark.circular_score.CircularScoreView;
import android.text.SpannableString;
import android.support.v4.view.ViewPager;
import org.json.JSONObject;

public class ShippingTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private JSONObject thisObj;
    private JSONObject thisObjPro;

    private ImageView shippingIcon1;
    private ImageView shippingIcon2;
    private ImageView shippingIcon3;
    private TextView shippingText1;
    private TextView shippingText2;
    private TextView shippingText3;
    private ViewPager seperateLine1;
    private ViewPager seperateLine2;
    private TextView storename;
    private TextView storename2;
    private TextView feedbackScore;
    private TextView feedbackScore2;
    private TextView popularity;
    private CircularScoreView popularity2;
    private TextView feedbackStar;
    private ImageView feedbackStar2;
    private TextView shippingCost;
    private TextView shippingCost2;
    private TextView globalShipping;
    private TextView globalShipping2;
    private TextView handlingTime;
    private TextView handlingTime2;
    private TextView conditionS;
    private TextView conditionS2;
    private TextView policy;
    private TextView policy2;
    private TextView returnIn;
    private TextView returnIn2;
    private TextView refundMode;
    private TextView refundMode2;
    private TextView shipBy;
    private TextView shipBy2;
    private TextView noRlts;


    // Empty public constructor
    public ShippingTab() {
        // Just for requirement
    }

    // TODO: Rename and change types and number of parameters
    public static ShippingTab newInstance(String param1, String param2) {
        ShippingTab fragment = new ShippingTab();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shipping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initializes the view using super
        super.onViewCreated(view, savedInstanceState);

        shippingIcon1 = view.findViewById(R.id.shipping_image1);
        shippingIcon2 = view.findViewById(R.id.shipping_image2);
        shippingIcon3 = view.findViewById(R.id.shipping_image3);
        shippingText1 = view.findViewById(R.id.shipping_text1);
        shippingText2 = view.findViewById(R.id.shipping_text2);
        shippingText3 = view.findViewById(R.id.shipping_text3);
        seperateLine1 = view.findViewById(R.id.shipping_seperate_line1);
        seperateLine2 = view.findViewById(R.id.shipping_seperate_line2);
        // First Section
        storename = view.findViewById(R.id.storename);
        storename2 = view.findViewById(R.id.storename_text);
        feedbackScore = view.findViewById(R.id.feedback);
        feedbackScore2 = view.findViewById(R.id.feedback_text);
        popularity = view.findViewById(R.id.popular);
        popularity2 = view.findViewById(R.id.circular_score);
        feedbackStar = view.findViewById(R.id.feedback_star);
        feedbackStar2 = view.findViewById(R.id.feedback_star_image);
        // Second Section
        shippingCost = view.findViewById(R.id.shipping_cost);
        shippingCost2 = view.findViewById(R.id.shipping_cost_text);
        globalShipping = view.findViewById(R.id.global_shipping);
        globalShipping2 = view.findViewById(R.id.global_shipping_text);
        handlingTime = view.findViewById(R.id.handling_time);
        handlingTime2 = view.findViewById(R.id.handling_time_text);
        conditionS = view.findViewById(R.id.condition_shipping);
        conditionS2 = view.findViewById(R.id.condition_shipping_text);
        // Third Section
        policy = view.findViewById(R.id.policy);
        policy2 = view.findViewById(R.id.policy_text);
        returnIn = view.findViewById(R.id.return_within);
        returnIn2 = view.findViewById(R.id.return_within_text);
        refundMode = view.findViewById(R.id.refund_mode);
        refundMode2 = view.findViewById(R.id.refund_mode_text);
        shipBy = view.findViewById(R.id.ship_by);
        shipBy2 = view.findViewById(R.id.ship_by_text);
        noRlts = view.findViewById(R.id.noRlt_shipping_tab);

        boolean has1 = initFirstSection();
        boolean has2 = initSecondSection();
        boolean has3 = initThirdSection();

        if (!has1 && !has2 && !has3) {
            noRlts.setVisibility(View.GONE);
            disappearAll();
        }

        if (has2 && !has3) {
            seperateLine2.setVisibility(View.GONE);
        }

        if (has1 && !has2 && !has3) {
            seperateLine1.setVisibility(View.GONE);
        }
    }

    private boolean initFirstSection() {
        boolean has = false;
        try {
            if (thisObjPro.has("storeInfo")) {
                JSONObject storeObj = thisObjPro.getJSONArray("storeInfo").getJSONObject(0);
                if (storeObj.has("storeName")) {
                    has = true;
                    storename.setVisibility(View.VISIBLE);
                    storename2.setVisibility(View.VISIBLE);
                    String name = storeObj.getJSONArray("storeName").getString(0);
                    SpannableString content = new SpannableString(name);
                    content.setSpan(new UnderlineSpan(), 0, name.length(), 0);
                    storename2.setText(content);
                    if (storeObj.has("storeURL")) {
                        final String storeUrl = storeObj.getJSONArray("storeURL").getString(0);
                        storename2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intentShare = new Intent(Intent.ACTION_VIEW);
                                intentShare.setData(Uri.parse(storeUrl));
                                startActivity(intentShare);
                            }
                        });
                    }
                }
                else {
                    storename.setVisibility(View.GONE);
                    storename2.setVisibility(View.GONE);
                }
            }
            else {
                storename.setVisibility(View.GONE);
                storename2.setVisibility(View.GONE);
            }

            if (thisObjPro.has("sellerInfo")) {
                JSONObject sellerObj = thisObjPro.getJSONArray("sellerInfo").getJSONObject(0);
                if (sellerObj.has("feedbackScore")) {
                    has = true;
                    feedbackScore.setVisibility(View.VISIBLE);
                    feedbackScore2.setVisibility(View.VISIBLE);
                    feedbackStar.setVisibility(View.VISIBLE);
                    feedbackStar2.setVisibility(View.VISIBLE);
                    String score = sellerObj.getJSONArray("feedbackScore").getString(0);
                    int scoreNum = Integer.parseInt(score);
                    feedbackScore2.setText(score);
                    if (scoreNum >= 10 && scoreNum < 49) { feedbackStar2.setImageResource(R.drawable.star_low_yellow); }
                    else if (scoreNum >= 50 && scoreNum < 100) { feedbackStar2.setImageResource(R.drawable.star_low_realblue); }
                    else if (scoreNum >= 100 && scoreNum < 500) { feedbackStar2.setImageResource(R.drawable.star_low_blue); }
                    else if (scoreNum >= 500 && scoreNum < 1000) { feedbackStar2.setImageResource(R.drawable.star_low_purple); }
                    else if (scoreNum >= 1000 && scoreNum < 5000) { feedbackStar2.setImageResource(R.drawable.star_low_red); }
                    else if (scoreNum >= 5000 && scoreNum < 10000) { feedbackStar2.setImageResource(R.drawable.star_low_green); }
                    else if (scoreNum >= 10000 && scoreNum < 25000) { feedbackStar2.setImageResource(R.drawable.star_high_yellow); }
                    else if (scoreNum >= 25000 && scoreNum < 50000) { feedbackStar2.setImageResource(R.drawable.star_high_blue); }
                    else if (scoreNum >= 50000 && scoreNum < 100000) { feedbackStar2.setImageResource(R.drawable.star_high_purple); }
                    else if (scoreNum >= 100000 && scoreNum < 500000) { feedbackStar2.setImageResource(R.drawable.star_high_red); }
                    else if (scoreNum >= 500000 && scoreNum < 1000000) { feedbackStar2.setImageResource(R.drawable.star_high_green); }
                    else if (scoreNum >= 1000000) { feedbackStar2.setImageResource(R.drawable.star_high_silver); }
                    else {
                        feedbackStar.setVisibility(View.GONE);
                        feedbackStar2.setVisibility(View.GONE);
                    }
                }
                else {
                    feedbackScore.setVisibility(View.GONE);
                    feedbackScore2.setVisibility(View.GONE);
                    feedbackStar.setVisibility(View.GONE);
                    feedbackStar2.setVisibility(View.GONE);
                }

                if (sellerObj.has("positiveFeedbackPercent")) {
                    has = true;
                    popularity.setVisibility(View.VISIBLE);
                    popularity2.setVisibility(View.VISIBLE);
                    popularity2.setScore((int) Math.round(Double.parseDouble(sellerObj.getJSONArray("positiveFeedbackPercent").getString(0))));
                }
                else {
                    popularity.setVisibility(View.GONE);
                    popularity2.setVisibility(View.GONE);
                }
            }
            else {
                feedbackScore.setVisibility(View.GONE);
                feedbackScore2.setVisibility(View.GONE);
                popularity.setVisibility(View.GONE);
                popularity2.setVisibility(View.GONE);
                feedbackStar.setVisibility(View.GONE);
                feedbackStar2.setVisibility(View.GONE);
            }

            if (has) {
                shippingIcon1.setVisibility(View.VISIBLE);
                shippingText1.setVisibility(View.VISIBLE);
                seperateLine1.setVisibility(View.VISIBLE);
            }
            else {
                shippingIcon1.setVisibility(View.GONE);
                shippingText1.setVisibility(View.GONE);
                seperateLine1.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return has;
    }

    private boolean initSecondSection() {
        boolean has = false;
        try {
            if (thisObjPro.has("shippingInfo")) {
                JSONObject shippingObj = thisObjPro.getJSONArray("shippingInfo").getJSONObject(0);
                if (shippingObj.has("shippingServiceCost")) {
                    has = true;
                    shippingCost.setVisibility(View.VISIBLE);
                    shippingCost2.setVisibility(View.VISIBLE);
                    String money = shippingObj.getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                    if (money.equals("0.0")) {
                        shippingCost2.setText("Free Shipping");
                    }
                    else {
                        shippingCost2.setText("$" + money);
                    }
                }
                else {
                    shippingCost.setVisibility(View.GONE);
                    shippingCost2.setVisibility(View.GONE);
                }
            }
            else {
                shippingCost.setVisibility(View.GONE);
                shippingCost2.setVisibility(View.GONE);
            }

            if (thisObj.has("GlobalShipping")) {
                has = true;
                globalShipping.setVisibility(View.VISIBLE);
                globalShipping2.setVisibility(View.VISIBLE);
                if (thisObj.getBoolean("GlobalShipping")) {
                    globalShipping2.setText("Yes");
                }
                else {
                    globalShipping2.setText("No");
                }
            }
            else {
                globalShipping.setVisibility(View.GONE);
                globalShipping2.setVisibility(View.GONE);
            }

            if (thisObj.has("HandlingTime")) {
                has = true;
                handlingTime.setVisibility(View.VISIBLE);
                handlingTime2.setVisibility(View.VISIBLE);
                if (thisObj.getInt("HandlingTime") == 0 || thisObj.getInt("HandlingTime") == 1) {
                    handlingTime2.setText(thisObj.getInt("HandlingTime") + " Day");
                }
                else {
                    handlingTime2.setText(thisObj.getInt("HandlingTime") + " Days");
                }
            }
            else {
                handlingTime.setVisibility(View.GONE);
                handlingTime2.setVisibility(View.GONE);
            }

            if (thisObj.has("ConditionDisplayName")) {
                has = true;
                conditionS.setVisibility(View.VISIBLE);
                conditionS2.setVisibility(View.VISIBLE);
                conditionS2.setText(thisObj.getString("ConditionDisplayName"));
            }
            else {
                conditionS2.setVisibility(View.GONE);
                conditionS.setVisibility(View.GONE);
            }

            if (has) {
                shippingIcon2.setVisibility(View.VISIBLE);
                shippingText2.setVisibility(View.VISIBLE);
                seperateLine2.setVisibility(View.VISIBLE);
            }
            else {
                shippingIcon2.setVisibility(View.GONE);
                shippingText2.setVisibility(View.GONE);
                seperateLine2.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return has;
    }

    private boolean initThirdSection() {
        boolean has = false;
        try {
            if (thisObj.has("ReturnPolicy")) {
                JSONObject returnObj = thisObj.getJSONObject("ReturnPolicy");
                if (returnObj.has("Refund")) {
                    has = true;
                    refundMode.setVisibility(View.VISIBLE);
                    refundMode2.setVisibility(View.VISIBLE);
                    refundMode2.setText(returnObj.getString("Refund"));
                }
                else {
                    refundMode.setVisibility(View.GONE);
                    refundMode2.setVisibility(View.GONE);
                }

                if (returnObj.has("ReturnsWithin")) {
                    has = true;
                    returnIn.setVisibility(View.VISIBLE);
                    returnIn2.setVisibility(View.VISIBLE);
                    returnIn2.setText(returnObj.getString("ReturnsWithin"));
                }
                else {
                    returnIn.setVisibility(View.GONE);
                    returnIn2.setVisibility(View.GONE);
                }

                if (returnObj.has("ReturnsAccepted")) {
                    has = true;
                    policy.setVisibility(View.VISIBLE);
                    policy2.setVisibility(View.VISIBLE);
                    policy2.setText(returnObj.getString("ReturnsAccepted"));
                }
                else {
                    policy.setVisibility(View.GONE);
                    policy2.setVisibility(View.GONE);
                }

                if (returnObj.has("ShippingCostPaidBy")) {
                    has = true;
                    shipBy.setVisibility(View.VISIBLE);
                    shipBy2.setVisibility(View.VISIBLE);
                    shipBy2.setText(returnObj.getString("ShippingCostPaidBy"));
                }
                else {
                    shipBy.setVisibility(View.GONE);
                    shipBy2.setVisibility(View.GONE);
                }

            }
            else {
                policy.setVisibility(View.GONE);
                policy2.setVisibility(View.GONE);
                returnIn.setVisibility(View.GONE);
                returnIn2.setVisibility(View.GONE);
                refundMode.setVisibility(View.GONE);
                refundMode2.setVisibility(View.GONE);
                shipBy.setVisibility(View.GONE);
                shipBy2.setVisibility(View.GONE);
            }

            if (has) {
                shippingIcon3.setVisibility(View.VISIBLE);
                shippingText3.setVisibility(View.VISIBLE);
            }
            else {
                shippingIcon3.setVisibility(View.GONE);
                shippingText3.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return has;
    }

    private void disappearAll() {
        shippingIcon1.setVisibility(View.GONE);
        shippingText1.setVisibility(View.GONE);
        seperateLine1.setVisibility(View.GONE);
        shippingIcon2.setVisibility(View.GONE);
        shippingText2.setVisibility(View.GONE);
        seperateLine2.setVisibility(View.GONE);
        shippingIcon3.setVisibility(View.GONE);
        shippingText3.setVisibility(View.GONE);
        // -----------------------------
        storename.setVisibility(View.GONE);
        storename2.setVisibility(View.GONE);
        feedbackScore.setVisibility(View.GONE);
        feedbackScore2.setVisibility(View.GONE);
        popularity.setVisibility(View.GONE);
        popularity2.setVisibility(View.GONE);
        feedbackStar.setVisibility(View.GONE);
        feedbackStar2.setVisibility(View.GONE);
        // -----------------------------
        shippingCost.setVisibility(View.GONE);
        shippingCost2.setVisibility(View.GONE);
        globalShipping.setVisibility(View.GONE);
        globalShipping2.setVisibility(View.GONE);
        handlingTime.setVisibility(View.GONE);
        handlingTime2.setVisibility(View.GONE);
        conditionS2.setVisibility(View.GONE);
        conditionS.setVisibility(View.GONE);
        // -----------------------------
        policy.setVisibility(View.GONE);
        policy2.setVisibility(View.GONE);
        returnIn.setVisibility(View.GONE);
        returnIn2.setVisibility(View.GONE);
        refundMode.setVisibility(View.GONE);
        refundMode2.setVisibility(View.GONE);
        shipBy.setVisibility(View.GONE);
        shipBy2.setVisibility(View.GONE);
    }

}
