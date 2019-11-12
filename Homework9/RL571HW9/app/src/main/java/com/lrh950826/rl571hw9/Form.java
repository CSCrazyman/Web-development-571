package com.lrh950826.rl571hw9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Form.OnFormFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Form#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Form extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String REGEX = "^[0-9]{5}$";
    private String currentZip;

    // ----------- For two error messages -----------
    private TextView keywordError;
    private TextView zipcodeError;
    private TextView textFrom;
    private TextView textCurLoc;

    // ----------- For two regular inputs -----------
    private EditText keyword;
    private EditText miles;

    private AutoCompleteTextView zipcode;
    private List<String> categoryList;
    private ArrayAdapter categoryAdapter;
    private Spinner categorySpin;

    // ----------- For checkboxes and radios ----------
    private CheckBox conNew;
    private CheckBox conUsed;
    private CheckBox conUnsp;
    private CheckBox spLocal;
    private CheckBox spFree;
    private CheckBox enableNearby;
    private RadioGroup radios;
    private RadioButton current;
    private RadioButton other;

    // ----------- For two buttons -----------
    private Button searchButton;
    private Button clearButton;

    // ----------- For auto-complete zip code -----------
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;

    // ----------- For form interaction -----------
    private OnFormFragmentInteractionListener formListener;

    // Empty public constructor
    public Form() {
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
    public static Form newInstance(String param1, String param2) {
        Form fragment = new Form();
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
        getCurrentZip();
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Initializes the view using super
        super.onViewCreated(view, savedInstanceState);

        // All elements in the form we need to proceed
        keywordError = view.findViewById(R.id.keywordError);
        zipcodeError = view.findViewById(R.id.zipcodeError);
        keyword = view.findViewById(R.id.keywordText);
        categorySpin = view.findViewById(R.id.categoryId);
        conNew = view.findViewById(R.id.con_new);
        conUsed = view.findViewById(R.id.con_used);
        conUnsp = view.findViewById(R.id.con_unsp);
        spLocal = view.findViewById(R.id.shipping_opt1);
        spFree = view.findViewById(R.id.shipping_opt2);
        enableNearby = view.findViewById(R.id.enable_nearby);
        // Initially Hidden
        miles = view.findViewById(R.id.distanceText);
        textFrom = view.findViewById(R.id.nearby_text_2);
        radios = view.findViewById(R.id.radioGroup);
        current = view.findViewById(R.id.locationCurrent);
        other = view.findViewById(R.id.locationOther);
        textCurLoc = view.findViewById(R.id.location_text1);
        // Initially Hidden and Auto Complete
        zipcode = view.findViewById(R.id.location_text2);
        searchButton = view.findViewById(R.id.buttonSearch);
        clearButton = view.findViewById(R.id.buttonClear);
        // Validates the zip code
        final Pattern pattern = Pattern.compile(REGEX);

        // ----------------------------------------------------------
        // ------------------------ Category ------------------------
        // ----------------------------------------------------------
        categoryList = new ArrayList<String>();
        categoryList.add("All");
        categoryList.add("Art");
        categoryList.add("Baby");
        categoryList.add("Books");
        categoryList.add("Clothing, Shoes & Accessories");
        categoryList.add("Computers/Tablets & Networking");
        categoryList.add("Health & Beauty");
        categoryList.add("Music");
        categoryList.add("Video Games & Consoles");
        categoryAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpin.setAdapter(categoryAdapter);

        // ----------------------------------------------------------
        // --------- Setting up the adapter for AutoSuggest ---------
        // ----------------------------------------------------------
        final AppCompatAutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.location_text2);
        final TextView selectedText = view.findViewById(R.id.selected_item);
        autoSuggestAdapter = new AutoSuggestAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        selectedText.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        // ----------------------------------------------------------
        // ---------------------- Nearby Search ---------------------
        // ----------------------------------------------------------
        enableNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enableNearby.isChecked()) {
                    miles.setVisibility(View.VISIBLE);
                    textFrom.setVisibility(View.VISIBLE);
                    radios.setVisibility(View.VISIBLE);
                    textCurLoc.setVisibility(View.VISIBLE);
                    zipcode.setVisibility(View.VISIBLE);
                }
                else {
                    miles.setVisibility(View.GONE);
                    textFrom.setVisibility(View.GONE);
                    radios.setVisibility(View.GONE);
                    textCurLoc.setVisibility(View.GONE);
                    zipcode.setVisibility(View.GONE);
                }
            }
        });

        // ----------------------------------------------------------
        // ---------------------- Radio Change ----------------------
        // ----------------------------------------------------------
        radios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // If the radiobutton that has changed in check state is now checked...
                if (checkedRadioButton == current) {
                    zipcode.setEnabled(false);
                }
                else if (checkedRadioButton == other) {
                    zipcode.setEnabled(true);
                }
            }
        });

        // ----------------------------------------------------------
        // ----------------------- Search Btn -----------------------
        // ----------------------------------------------------------
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Matcher matcher = pattern.matcher(zipcode.getText().toString().trim());

                // Error conditions
                if (keyword == null || keyword.getText().toString().trim().length() == 0 ||
                        (enableNearby.isChecked() && other.isChecked() && !matcher.matches())) {
                    if (keyword == null || keyword.getText().toString().trim().length() == 0 ) {
                        keywordError.setVisibility(View.VISIBLE);
                    }
                    if (enableNearby.isChecked() && other.isChecked() &&
                            zipcode.getText().toString().trim().length() == 0) {
                        zipcodeError.setVisibility(view.VISIBLE);
                    }
                    if (enableNearby.isChecked() && other.isChecked() &&
                            zipcode.getText().toString().trim().length() != 0 && !matcher.matches()) {
                        zipcodeError.setVisibility(view.GONE);
                    }
                    Toast.makeText(getActivity().getBaseContext(), "Please fix all fields with errors",
                            Toast.LENGTH_SHORT).show();
                }
                else { // Normal conditions
                    Intent resultIntent = new Intent(getActivity(), ResultPageActivity.class);
                    keywordError.setVisibility(View.GONE);
                    zipcodeError.setVisibility(View.GONE);
                    String key = keyword.getText().toString().trim();
                    String categoryId = "";
                    String isNew = "";
                    String isUsed = "";
                    String isUnsp = "";
                    String isLocal = "";
                    String isFree = "";
                    String isMax = "";
                    String distance = "10";
                    String isZip = "";
                    String zip = "";

                    switch (categorySpin.getSelectedItem().toString()) {
                        case "All": categoryId = "-1"; break;
                        case "Art": categoryId = "550"; break;
                        case "Baby": categoryId = "2984"; break;
                        case "Books": categoryId = "267"; break;
                        case "Clothing, Shoes & Accessories": categoryId = "11450"; break;
                        case "Computers/Tablets & Networking": categoryId = "58058"; break;
                        case "Health & Beauty": categoryId = "26395"; break;
                        case "Music": categoryId = "11233"; break;
                        case "Video Games & Consoles": categoryId = "1249"; break;
                        default: categoryId = "-1"; break;
                    }

                    if (conNew.isChecked()) { isNew = "t"; }
                    else { isNew = "f"; }
                    if (conUsed.isChecked()) { isUsed = "t"; }
                    else { isUsed = "f"; }
                    if (conUnsp.isChecked()) { isUnsp = "t"; }
                    else { isUnsp = "f"; }
                    if (spFree.isChecked()) { isFree = "t"; }
                    else { isFree = "f"; }
                    if (spLocal.isChecked()) { isLocal = "t"; }
                    else { isLocal = "f"; }

                    if (enableNearby.isChecked()) {
                        isMax = isZip = "t";
                        if (!(miles == null || miles.getText().toString().trim().length() == 0)) {
                            distance = miles.getText().toString().trim();
                        }
                        if (other.isChecked() && matcher.matches()) {
                            zip = zipcode.getText().toString().trim();
                        }
                        else {
                            zip = currentZip;
                        }
                    }
                    else {
                        isMax = isZip = "f";
                        // Any values: it won't be used
                        zip = "00000";
                    }

                    String productsURL = "http://csci571-lurh950826-hw9.us-east-2.elasticbeanstalk.com/";
                    productsURL += key + "/" + isZip + "/" + zip + "/" + categoryId + "/" + isMax + "/" + distance;
                    productsURL += "/" + isFree + "/" + isLocal + "/t" + "/" + isNew + "/" + isUsed + "/" + isUnsp;
                    System.out.println(productsURL);
                    resultIntent.putExtra("productsUrl", productsURL);
                    resultIntent.putExtra("key", key);
                    startActivity(resultIntent);

                }
            }
        });

        // ----------------------------------------------------------
        // ----------------------- Clear Btn ------------------------
        // ----------------------------------------------------------
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword.setText("");
                keyword.requestFocus();
                categorySpin.setSelection(0);
                enableNearby.setChecked(false);
                miles.setText("");
                zipcode.setText("");
                ((RadioButton)radios.getChildAt(0)).setChecked(true);
                keywordError.setVisibility(View.GONE);
                zipcodeError.setVisibility(View.GONE);
                miles.setVisibility(View.GONE);
                textFrom.setVisibility(View.GONE);
                radios.setVisibility(View.GONE);
                textCurLoc.setVisibility(View.GONE);
                zipcode.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFormFragmentInteractionListener) {
            formListener = (OnFormFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        formListener = null;
    }

    public interface OnFormFragmentInteractionListener {
    }

    private void makeApiCall(String text) {
        ApiCall.make(getActivity(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    if (responseObject.has("postalCodes")) {
                        JSONArray postalCodes = responseObject.getJSONArray("postalCodes");
                        for (int i = 0; i < postalCodes.length(); i++) {
                            JSONObject single = postalCodes.getJSONObject(i);
                            stringList.add(single.getString("postalCode"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }


    private void getCurrentZip() {
        String url = "http://ip-api.com/json";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    currentZip = response.get("zip").toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjRequest);
    }

}
