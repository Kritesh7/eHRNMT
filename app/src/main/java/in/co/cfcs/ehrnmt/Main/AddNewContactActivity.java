package in.co.cfcs.ehrnmt.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Model.CountryModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddNewContactActivity extends AppCompatActivity {

    public TextView titleTxt;
    public Spinner countrySpinner;
    public ArrayList<CountryModel> countryList = new ArrayList<>();
    public ArrayAdapter<CountryModel> countryAdapter;
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeePersonalData";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeAddressInsUpdt";
    public ConnectionDetector conn;
    public RadioGroup radioGroup;
    public RadioButton parmanentBtn, currentBtn, corspondentBtn;
    public EditText addressTxt, cityTxt, stateTxt, postalCodeTxt;
    public Button addBtn;
    public TextView emgTxt;
    public LinearLayout innerLay;
    public String typeStr = "", authcode = "", userId = "", countryId = "", recordId = "", addressStr = "", cityStr = "", state = "", countryNameStr = "", postalCodeStr = "", actionMode = "", addressTypeStr = "", stateStr = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.contacttollbar);
        setSupportActionBar(toolbar);

        titleTxt = (TextView) toolbar.findViewById(R.id.titletxt);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                onBackPressed();

            }
        });

        titleTxt.setText("Add New Contact Details");


        Intent intent = getIntent();
        if (intent != null) {
            recordId = intent.getStringExtra("RecordId");
            actionMode = intent.getStringExtra("Mode");
            addressStr = intent.getStringExtra("Address");
            addressTypeStr = intent.getStringExtra("AddressType");
            cityStr = intent.getStringExtra("City");
            stateStr = intent.getStringExtra("State");
            postalCodeStr = intent.getStringExtra("PostalCode");
            countryNameStr = intent.getStringExtra("CountryName");

        }

        conn = new ConnectionDetector(AddNewContactActivity.this);
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddNewContactActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddNewContactActivity.this)));

        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        radioGroup = (RadioGroup) findViewById(R.id.contact_radiogroup);
        parmanentBtn = (RadioButton) findViewById(R.id.parmanent_radiobtn);
        currentBtn = (RadioButton) findViewById(R.id.current_radiobtn);
        corspondentBtn = (RadioButton) findViewById(R.id.corspond_radiobtn);
        addressTxt = (EditText) findViewById(R.id.contact_address);
        cityTxt = (EditText) findViewById(R.id.contact_city);
        stateTxt = (EditText) findViewById(R.id.contact_state);
        postalCodeTxt = (EditText) findViewById(R.id.contact_postalcode);
        addBtn = (Button) findViewById(R.id.newrequestbtn);
        emgTxt = (TextView) findViewById(R.id.emgtxt);
        innerLay = (LinearLayout) findViewById(R.id.radiobtnlay);

        if (actionMode.equalsIgnoreCase("EditMode")) {
            addressTxt.setText(addressStr);
            cityTxt.setText(cityStr);
            stateTxt.setText(stateStr);
            postalCodeTxt.setText(postalCodeStr);

            innerLay.setVisibility(View.GONE);
            emgTxt.setVisibility(View.VISIBLE);


            if (addressTypeStr.equalsIgnoreCase("1")) {
                typeStr = "1";
                parmanentBtn.setChecked(true);
                emgTxt.setText("Permanent Address");

            } else if (addressTypeStr.equalsIgnoreCase("2")) {
                typeStr = "2";
                currentBtn.setChecked(true);
                emgTxt.setText("Current Address");

            } else if (addressTypeStr.equalsIgnoreCase("3")) {
                typeStr = "3";
                corspondentBtn.setChecked(true);
                emgTxt.setText("Correspond Address");
            }

            addBtn.setText("Update Contact details");
            titleTxt.setText("Update Contact  Details");


        }


        // country Spinner
        countrySpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);
        countryAdapter = new ArrayAdapter<CountryModel>(AddNewContactActivity.this, R.layout.customizespinner,
                countryList);
        countryAdapter.setDropDownViewResource(R.layout.customizespinner);
        countrySpinner.setAdapter(countryAdapter);

        //bind Data in spinner
        if (conn.getConnectivityStatus() > 0) {
            personalDdlDetails();

        } else {
            conn.showNoInternetAlret();
        }

        // get type
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.parmanent_radiobtn) {
                    typeStr = "1";

                } else if (i == R.id.current_radiobtn) {
                    typeStr = "2";

                } else if (i == R.id.corspond_radiobtn) {
                    typeStr = "3";
                }
            }
        });

        //get country id
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countryId = countryList.get(i).getCountryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // add new contact details
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typeStr.equalsIgnoreCase("")) {
                    Toast.makeText(AddNewContactActivity.this, "Please select address type", Toast.LENGTH_SHORT).show();
                } else if (addressTxt.getText().toString().equalsIgnoreCase("")) {
                    addressTxt.setError("Please enter address");
                    addressTxt.requestFocus();
                } else if (cityTxt.getText().toString().equalsIgnoreCase("")) {
                    cityTxt.setError("Please enter city");
                    cityTxt.requestFocus();
                } else if (stateTxt.getText().toString().equalsIgnoreCase("")) {
                    stateTxt.setError("Please enter state");
                    stateTxt.requestFocus();

                } else if (countryId.equalsIgnoreCase("")) {
                    Toast.makeText(AddNewContactActivity.this, "Please enter Country", Toast.LENGTH_SHORT).show();

                } else if (postalCodeTxt.getText().toString().equalsIgnoreCase("")) {
                    postalCodeTxt.setError("Please enter postal Code");
                    postalCodeTxt.requestFocus();

                } else {

                    if (conn.getConnectivityStatus() > 0) {

                        addContactDetails(userId, recordId, typeStr, authcode, addressTxt.getText().toString(), cityTxt.getText().toString(),
                                stateTxt.getText().toString(), countryId, postalCodeTxt.getText().toString());
                    } else {
                        conn.showNoInternetAlret();
                    }

                }
            }
        });


    }

    //bind all spiiner data
    public void personalDdlDetails() {


        final ProgressDialog pDialog = new ProgressDialog(AddNewContactActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalDdlDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));


                    //bind Country Spinner
                    if (countryList.size() > 0) {
                        countryList.clear();
                    }

                    countryList.add(new CountryModel("", "Please select Country"));

                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        }
                    } else {

                        JSONArray countryObj = jsonObject.getJSONArray("CountryMaster");
                        for (int i = 0; i < countryObj.length(); i++) {
                            JSONObject object = countryObj.getJSONObject(i);

                            String CountryID = object.getString("CountryID");
                            String CountryName = object.getString("CountryName");
                            countryList.add(new CountryModel(CountryID, CountryName));

                        }

                        //Edit Option
                        for (int k = 0; k < countryList.size(); k++) {
                            if (actionMode.equalsIgnoreCase("EditMode")) {
                                if (countryList.get(k).getCountryName().equalsIgnoreCase(countryNameStr)) {
                                    countryId = countryList.get(k).getCountryId();
                                    countrySpinner.setSelection(k);
                                }
                            }
                        }
                    }

                    countryAdapter.notifyDataSetChanged();
                    pDialog.dismiss();


                } catch (JSONException e) {
                    Log.e("checking json excption", e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getBaseContext(),
                            "Time Out Server Not Respond",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getBaseContext(),
                            "Server Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getBaseContext(),
                            "Network Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(getBaseContext(),
                            "Parse Error",
                            Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();


            }
        });
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    // add contact Details
    public void addContactDetails(final String AdminID, final String RecordID, final String Type, final String AuthCode, final String Address,
                                  final String City, final String State, final String CountryID, final String PostCode) {

        final ProgressDialog pDialog = new ProgressDialog(AddNewContactActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, addUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        } else if (LoginStatus.equalsIgnoreCase("success")) {
                            onBackPressed();

                        } else {
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }

                    pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption", e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getBaseContext(),
                            "Time Out Server Not Respond",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getBaseContext(),
                            "Server Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getBaseContext(),
                            "Network Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(getBaseContext(),
                            "Parse Error",
                            Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AdminID", AdminID);
                params.put("AuthCode", AuthCode);
                params.put("RecordID", RecordID);
                params.put("Type", Type);
                params.put("Address", Address);
                params.put("City", City);
                params.put("State", State);
                params.put("CountryID", CountryID);
                params.put("PostCode", PostCode);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }

    private void Logout() {


        finishAffinity();
        startActivity(new Intent(AddNewContactActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddNewContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddNewContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddNewContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddNewContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddNewContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddNewContactActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddNewContactActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddNewContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddNewContactActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }


}
