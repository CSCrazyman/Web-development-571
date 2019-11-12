package com.lrh950826.rl571hw9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SimilarViewHolder> {

    private List<SimilarProduct> similars;
    private Context context;
    private SimilarAdapter.OnItemClickListener listener;

    public SimilarAdapter(Context context, List<SimilarProduct> similars, OnItemClickListener listener) {
        this.context = context;
        this.similars = similars;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarAdapter.SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SimilarAdapter.SimilarViewHolder(LayoutInflater.from(context).inflate(R.layout.single_similar_product, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SimilarAdapter.SimilarViewHolder viewHolder, final int i) {

        // Set Image
        String imageUrl = similars.get(i).getPicture();
        if (imageUrl != null) {
            Picasso.with(context).load(imageUrl).resize(170, 170).into(viewHolder.similarImage, new Callback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onError() {
                    viewHolder.similarImage.setImageResource(R.drawable.loading_failed);
                }
            });
        }
        else {
            viewHolder.similarImage.setImageResource(R.drawable.loading_failed);
        }

        // Set title
        viewHolder.similarTitle.setText(similars.get(i).getTitle());

        // Set Shipping
        String shipCost = "";
        if (similars.get(i).getShippingCost() == null) {
            shipCost = "N/A";
        } else if (similars.get(i).getShippingCost().equals("0.00")) {
            shipCost = "Free Shipping";
        } else {
            shipCost = "$" + similars.get(i).getShippingCost();
        }
        viewHolder.similarShipping.setText(shipCost);

        // Set Days Left
        String days = "";
        if (similars.get(i).getDaysLeft() == null) {
            days = "N/A";
        }
        else {
            String tempStr = similars.get(i).getDaysLeft();
            int daysNum = Integer.parseInt(tempStr.substring(tempStr.indexOf('P') + 1, tempStr.indexOf('D')));
            if (daysNum == 0 || daysNum == 1) {
                days = daysNum + " Day Left";
            }
            else {
                days = daysNum + " Days Left";
            }
        }
        viewHolder.similarDays.setText(days);

        // Set Price
        String price = "";
        if (similars.get(i).getPrice() == null) {
            price = "N/A";
        } else if (similars.get(i).getPrice().equals("0.00")) {
            price = "Free";
        } else {
            price = "$" + similars.get(i).getPrice();
        }
        viewHolder.similarPrice.setText(price);

        // Set click link
        viewHolder.contentHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickSimilar(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (similars != null) {
            return similars.size();
        }
        else {
            return 0;
        }
    }

    class SimilarViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout contentHolder;
        private ImageView similarImage;
        private TextView similarTitle;
        private TextView similarShipping;
        private TextView similarDays;
        private TextView similarPrice;

        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);
            contentHolder = itemView.findViewById(R.id.adjust_layout_similar);
            similarImage = itemView.findViewById(R.id.similar_image);
            similarTitle = itemView.findViewById(R.id.similar_title);
            similarShipping = itemView.findViewById(R.id.similar_shipping_cost);
            similarDays = itemView.findViewById(R.id.similar_days_left);
            similarPrice = itemView.findViewById(R.id.similar_price);
        }
    }

    public interface OnItemClickListener{
        void clickSimilar(int index);
    }
}
