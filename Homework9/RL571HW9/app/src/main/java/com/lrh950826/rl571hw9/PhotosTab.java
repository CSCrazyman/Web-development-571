package com.lrh950826.rl571hw9;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotosTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private JSONObject thisJSONPhoto;
    private String photoURL;

    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;
    private ImageView photo5;
    private ImageView photo6;
    private ImageView photo7;
    private ImageView photo8;
    private TextView noRlt;
    private ConstraintLayout photosContainer;
    private ProgressBar progressPhotos;
    private TextView waitingPhotos;

    // Empty public constructor
    public PhotosTab() {
        // Just for requirement
    }

    // TODO: Rename and change types and number of parameters
    public static PhotosTab newInstance(String param1) {
        PhotosTab fragment = new PhotosTab();
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
            photoURL = mParam1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initializes the view using super
        super.onViewCreated(view, savedInstanceState);

        photo1 = view.findViewById(R.id.google_photo1);
        photo2 = view.findViewById(R.id.google_photo2);
        photo3 = view.findViewById(R.id.google_photo3);
        photo4 = view.findViewById(R.id.google_photo4);
        photo5 = view.findViewById(R.id.google_photo5);
        photo6 = view.findViewById(R.id.google_photo6);
        photo7 = view.findViewById(R.id.google_photo7);
        photo8 = view.findViewById(R.id.google_photo8);
        noRlt = view.findViewById(R.id.noRlt_photos_tab);
        photosContainer = view.findViewById(R.id.photos_container);
        progressPhotos = view.findViewById(R.id.progress_photos);
        waitingPhotos = view.findViewById(R.id.waiting_photos);

        photo1.setVisibility(View.GONE);
        photo2.setVisibility(View.GONE);
        photo3.setVisibility(View.GONE);
        photo4.setVisibility(View.GONE);
        photo5.setVisibility(View.GONE);
        photo6.setVisibility(View.GONE);
        photo7.setVisibility(View.GONE);
        photo8.setVisibility(View.GONE);
        noRlt.setVisibility(View.GONE);
        progressPhotos.setVisibility(View.VISIBLE);
        waitingPhotos.setVisibility(View.VISIBLE);

        try {
            RequestQueue queue2 = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, photoURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response2) {
                            try {
                                thisJSONPhoto = new JSONObject(response2);
                                if (thisJSONPhoto.has("items")) {
                                    JSONArray photoArr = thisJSONPhoto.getJSONArray("items");
                                    if (photoArr.length() != 0) {
                                        final int width = photosContainer.getWidth();
                                        progressPhotos.setVisibility(View.GONE);
                                        waitingPhotos.setVisibility(View.GONE);
                                        for (int i = 0; i < photoArr.length(); i++) {
                                            String photoLink = photoArr.getJSONObject(i).getString("link");
                                            switch (i) {
                                                case 0:
                                                    Picasso.with(getContext()).load(photoLink).resize(width, width).into(photo1, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            photo1.setImageResource(R.drawable.loading_failed);
                                                            photo1.getLayoutParams().height = width;
                                                        }
                                                    });
                                                    photo1.setVisibility(View.VISIBLE);
                                                    break;
                                                case 1:
                                                    Picasso.with(getContext()).load(photoLink).resize(width, width).into(photo2, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            photo2.setImageResource(R.drawable.loading_failed);
                                                            photo2.getLayoutParams().height = width;
                                                        }
                                                    });
                                                    photo2.setVisibility(View.VISIBLE);
                                                    break;
                                                case 2:
                                                    Picasso.with(getContext()).load(photoLink).resize(width, width).into(photo3, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            photo3.setImageResource(R.drawable.loading_failed);
                                                            photo3.getLayoutParams().height = width;
                                                        }
                                                    });
                                                    photo3.setVisibility(View.VISIBLE);
                                                    break;
                                                case 3:
                                                    Picasso.with(getContext()).load(photoLink).resize(width, width).into(photo4, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            photo4.setImageResource(R.drawable.loading_failed);
                                                            photo4.getLayoutParams().height = width;
                                                        }
                                                    });
                                                    photo4.setVisibility(View.VISIBLE);
                                                    break;
                                                case 4:
                                                    Picasso.with(getContext()).load(photoLink).resize(width, width).into(photo5, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            photo5.setImageResource(R.drawable.loading_failed);
                                                            photo5.getLayoutParams().height = width;
                                                        }
                                                    });
                                                    photo5.setVisibility(View.VISIBLE);
                                                    break;
                                                case 5:
                                                    Picasso.with(getContext()).load(photoLink).resize(width, width).into(photo6, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            photo6.setImageResource(R.drawable.loading_failed);
                                                            photo6.getLayoutParams().height = width;
                                                        }
                                                    });
                                                    photo6.setVisibility(View.VISIBLE);
                                                    break;
                                                case 6:
                                                    Picasso.with(getContext()).load(photoLink).resize(width, width).into(photo7, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            photo7.setImageResource(R.drawable.loading_failed);
                                                            photo7.getLayoutParams().height = width;
                                                        }
                                                    });
                                                    photo7.setVisibility(View.VISIBLE);
                                                    break;
                                                case 7:
                                                    Picasso.with(getContext()).load(photoLink).resize(width, width).into(photo8, new Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            photo8.setImageResource(R.drawable.loading_failed);
                                                            photo8.getLayoutParams().height = width;
                                                        }
                                                    });
                                                    photo8.setVisibility(View.VISIBLE);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    }
                                    else {
                                        progressPhotos.setVisibility(View.GONE);
                                        waitingPhotos.setVisibility(View.GONE);
                                       noRlt.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    progressPhotos.setVisibility(View.GONE);
                                    waitingPhotos.setVisibility(View.GONE);
                                    noRlt.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                progressPhotos.setVisibility(View.GONE);
                                waitingPhotos.setVisibility(View.GONE);
                                noRlt.setVisibility(View.VISIBLE);
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
            progressPhotos.setVisibility(View.GONE);
            waitingPhotos.setVisibility(View.GONE);
            noRlt.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }

    }

}
