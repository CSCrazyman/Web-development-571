package com.lrh950826.rl571hw9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {

    private Context context;
    private List<Product> products;
    private OnItemClickListener listener;

    public LinearAdapter(Context context, List<Product> products, OnItemClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.single_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LinearAdapter.LinearViewHolder viewHolder, final int i) {

        if (i < (getItemCount() / 2.0)) {
            // Sets Image
            String imageUrl = products.get(i * 2).getPictureUrl();
            if (imageUrl != null) {
                Picasso.with(context).load(imageUrl).resize(170, 170).into(viewHolder.productImage, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        viewHolder.productImage.setImageResource(R.drawable.loading_failed);
                    }
                });
            }
            else {
                viewHolder.productImage.setImageResource(R.drawable.loading_failed);
            }
            // Set Title
            String titleOrg = products.get(i * 2).getTitle();
            viewHolder.productTitle.setText(titleOrg);
            viewHolder.productTitle.post(new Runnable() {
                @Override
                public void run() {
                   if (viewHolder.productTitle.getLineCount() > 3) {
                        String tempStr = (String) viewHolder.productTitle.getText();
                        tempStr = tempStr.substring(0, tempStr.length() / 3 * 2);
                        int lastIndex = tempStr.lastIndexOf(' ');
                        tempStr = tempStr.substring(0, lastIndex + 1);
                        tempStr += "...";
                        viewHolder.productTitle.setText(tempStr);
                        products.get(i * 2).setTitleS(tempStr);
                   }
                }
            });
            viewHolder.productTitle.setTooltipText(titleOrg);

            // Set Shipping Cost and Zip
            String zipcode = (products.get(i * 2).getZipcode() == null) ? "N/A" : products.get(i * 2).getZipcode();
            String shipCost = "";
            if (products.get(i * 2).getShippingCost() == null) {
                shipCost = "N/A";
            } else if (products.get(i * 2).getShippingCost().equals("0.0")) {
                shipCost = "Free Shipping";
            } else {
                shipCost = "$" + products.get(i * 2).getShippingCost();
            }
            viewHolder.productZip.setText(zipcode);
            viewHolder.productShipCost.setText(shipCost);

            // Set condition and price
            String price = "";
            String condition = "";
            if (products.get(i * 2).getPrice() == null) {
                price = "N/A";
            } else if (products.get(i * 2).getPrice().equals("0.0")) {
                price = "Free";
            } else {
                price = "$" + products.get(i * 2).getPrice();
            }
            if (products.get(i * 2).getCondition() == null) {
                condition = "N/A";
            } else {
                condition = products.get(i * 2).getCondition();
                if (condition.contains("refurbished")) {
                    condition = "Refurbished";
                }
                else if (condition.contains("New")) {
                    condition = "New";
                }
            }
            viewHolder.productPrice.setText(price);
            viewHolder.productCondition.setText(condition);

            // Set Wish List Icons
            if (products.get(i * 2).getWanted()) {
                viewHolder.wishlist.setImageResource(R.drawable.remove_from_wishlist);
            } else {
                viewHolder.wishlist.setImageResource(R.drawable.add_to_wishlist);
            }

            viewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.clickToDetail(i * 2);
                }
            });

            viewHolder.wishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.changeWish(i * 2, (ImageView) view);
                }
            });

            if ((i * 2 + 1) != getItemCount()) {
                // Sets Image
                String imageUrl2 = products.get(i * 2 + 1).getPictureUrl();
                if (imageUrl2 != null) {
                    Picasso.with(context).load(imageUrl2).resize(170, 170).into(viewHolder.productImage2, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            viewHolder.productImage2.setImageResource(R.drawable.loading_failed);
                        }
                    });
                }
                else {
                    viewHolder.productImage2.setImageResource(R.drawable.loading_failed);
                }
                // Set Title
                String titleOrg2 = products.get(i * 2 + 1).getTitle();
                viewHolder.productTitle2.setText(titleOrg2);
                viewHolder.productTitle2.post(new Runnable() {
                    @Override
                    public void run() {
                        if (viewHolder.productTitle2.getLineCount() > 3) {
                            String tempStr = (String) viewHolder.productTitle2.getText();
                            tempStr = tempStr.substring(0, tempStr.length() / 3 * 2);
                            int lastIndex = tempStr.lastIndexOf(' ');
                            tempStr = tempStr.substring(0, lastIndex + 1);
                            tempStr += "...";
                            viewHolder.productTitle2.setText(tempStr);
                            products.get(i * 2 + 1).setTitleS(tempStr);
                        }
                    }
                });
                viewHolder.productTitle2.setTooltipText(titleOrg2);
                // Set Shipping Cost and Zip
                String zipcode2 = (products.get(i * 2 + 1).getZipcode() == null) ? "N/A" : products.get(i * 2 + 1).getZipcode();
                String shipCost2 = "";
                if (products.get(i * 2 + 1).getShippingCost() == null) {
                    shipCost2 = "N/A";
                } else if (products.get(i * 2 + 1).getShippingCost().equals("0.0")) {
                    shipCost2 = "Free Shipping";
                } else {
                    shipCost2 = "$" + products.get(i * 2 + 1).getShippingCost();
                }
                viewHolder.productZip2.setText(zipcode2);
                viewHolder.productShipCost2.setText(shipCost2);

                // Set condition and price
                String price2 = "";
                String condition2 = "";
                if (products.get(i * 2 + 1).getPrice() == null) {
                    price2 = "N/A";
                } else if (products.get(i * 2 + 1).getPrice().equals("0.0")) {
                    price2 = "Free";
                } else {
                    price2 = "$" + products.get(i * 2 + 1).getPrice();
                }
                if (products.get(i * 2 + 1).getCondition() == null) {
                    condition2 = "N/A";
                } else {
                    condition2 = products.get(i * 2 + 1).getCondition();
                    if (condition2.contains("refurbished")) {
                        condition2 = "Refurbished";
                    }
                    else if (condition2.contains("New")) {
                        condition2 = "New";
                    }
                }
                viewHolder.productPrice2.setText(price2);
                viewHolder.productCondition2.setText(condition2);

                // Set Wish List Icons
                if (products.get(i * 2 + 1).getWanted()) {
                    viewHolder.wishlist2.setImageResource(R.drawable.remove_from_wishlist);
                } else {
                    viewHolder.wishlist2.setImageResource(R.drawable.add_to_wishlist);
                }

                viewHolder.card2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.clickToDetail(i * 2 + 1);
                    }
                });

                viewHolder.wishlist2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       listener.changeWish(i * 2 + 1, (ImageView) view);
                    }
                });
            }
            else {
                viewHolder.card2.setVisibility(View.GONE);
            }
        }
        else {
            viewHolder.card.setVisibility(View.GONE);
            viewHolder.card2.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() { return products.size(); }

    class LinearViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private ImageView productImage;
        private TextView productTitle;
        private TextView productShipCost;
        private TextView productZip;
        private ImageView wishlist;
        private TextView productCondition;
        private TextView productPrice;
        private CardView card2;
        private ImageView productImage2;
        private TextView productTitle2;
        private TextView productShipCost2;
        private TextView productZip2;
        private ImageView wishlist2;
        private TextView productCondition2;
        private TextView productPrice2;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_view);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productZip = itemView.findViewById(R.id.product_zip);
            productShipCost = itemView.findViewById(R.id.product_ship);
            wishlist = itemView.findViewById(R.id.wishlist_image);
            productCondition = itemView.findViewById(R.id.product_status);
            productPrice = itemView.findViewById(R.id.product_price);

            card2 = itemView.findViewById(R.id.card_view2);
            productImage2 = itemView.findViewById(R.id.product_image2);
            productTitle2 = itemView.findViewById(R.id.product_title2);
            productZip2 = itemView.findViewById(R.id.product_zip2);
            productShipCost2 = itemView.findViewById(R.id.product_ship2);
            wishlist2 = itemView.findViewById(R.id.wishlist_image2);
            productCondition2 = itemView.findViewById(R.id.product_status2);
            productPrice2 = itemView.findViewById(R.id.product_price2);
        }
    }

    public interface OnItemClickListener{
        void clickToDetail(int index);
        void changeWish(int index, ImageView view);
    }

}
