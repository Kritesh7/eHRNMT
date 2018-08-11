package in.co.cfcs.ehrnmt.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import in.co.cfcs.ehrnmt.Model.RelationShipeTypeModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddNewEmergencyContactDetailsActivity extends AppCompatActivity {

    public TextView titleTxt;
    public RadioGroup emergencyGroup;
    public RadioButton primoryBtn, secondaryBtn;
    public Spinner relationShipSpinner,titileSpinner, counterySpinner;
    public EditText nameTxt, addTxt, cityTxt, stateTxt, postalCodeTxt, telNoTxt, mobileNoTxt, emailTxt;
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeePersonalData";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeEmergencyContactInsUpdt";
    public ArrayAdapter<RelationShipeTypeModel> relationshipAdapter ;
    public ArrayAdapter<CountryModel> countryAdapter ;
    public ArrayList<RelationShipeTypeModel> relationshipList = new ArrayList<>();
    public ArrayList<String> titleList = new ArrayList<>();
    public ArrayList<CountryModel> countryList = new ArrayList<>();
    public ArrayAdapter<String> titleAdapter;
    public ConnectionDetector conn;
    public LinearLayout radioBtnLay;
    public Button addBtn;
    public TextView emgTxt;
    public String authcode = "", userId = "", type = "0", titleStr = "", relationshipIdStr = "", countryIdStr = "",recordIdStr = ""
            , titlePassStr = "",namePassStr = "",relationshipPassNameStr = "",addressPassStr = "", cityPassStr = "",statePassStr =""
            , countryNamePassStr = "", postalCodePassStr = "",telPassStr = "",mobPassStr = "",emailPassStr = "", typePassStr = ""
            , actionMode = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_emergency_contact_details);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.emergencytoolbar);
        setSupportActionBar(toolbar);
        titleTxt = (TextView)toolbar.findViewById(R.id.titletxt);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getSupportActionBar() != null){
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

        titleTxt.setText("Add New Emergency Contact");

        Intent intent = getIntent();
        if (intent != null)
        {
            recordIdStr = intent.getStringExtra("RecordId");
            titlePassStr = intent.getStringExtra("Title");;
            namePassStr = intent.getStringExtra("Name");
            relationshipPassNameStr = intent.getStringExtra("RelationshipName");
            actionMode = intent.getStringExtra("Mode");
            addressPassStr = intent.getStringExtra("Address");
            cityPassStr = intent.getStringExtra("City");
            statePassStr = intent.getStringExtra("State");
            countryNamePassStr = intent.getStringExtra("CountryName");
            postalCodePassStr = intent.getStringExtra("PostalCode");
            telPassStr = intent.getStringExtra("TelephoneNumber");
            mobPassStr = intent.getStringExtra("MobileNumber");
            emailPassStr = intent.getStringExtra("Email");
            typePassStr = intent.getStringExtra("Type");

        }




        conn = new ConnectionDetector(AddNewEmergencyContactDetailsActivity.this);
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddNewEmergencyContactDetailsActivity.this)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddNewEmergencyContactDetailsActivity.this)));


        primoryBtn = (RadioButton)findViewById(R.id.primory_radiobtn);
        secondaryBtn = (RadioButton)findViewById(R.id.secondary_radiobtn);

        emergencyGroup = (RadioGroup)findViewById(R.id.emergency_radiogroup);

        relationShipSpinner = (Spinner)findViewById(R.id.relationshipspinner);
        titileSpinner = (Spinner)findViewById(R.id.titilespinner);
        counterySpinner = (Spinner) findViewById(R.id.counterySpiiner);
        nameTxt = (EditText) findViewById(R.id.emergency_contact_name);
        addTxt = (EditText) findViewById(R.id.emg_add);
        cityTxt = (EditText) findViewById(R.id.emg_city);
        stateTxt = (EditText) findViewById(R.id.emg_state);
        postalCodeTxt = (EditText) findViewById(R.id.emg_postalcode);
        telNoTxt = (EditText) findViewById(R.id.emg_telno);
        mobileNoTxt = (EditText) findViewById(R.id.emg_mobile_no);
        emailTxt = (EditText) findViewById(R.id.emg_email);
        addBtn = (Button) findViewById(R.id.newrequestbtn);
        radioBtnLay = (LinearLayout) findViewById(R.id.radiobtnlay);
        emgTxt = (TextView) findViewById(R.id.emgtxt);


        //check action mode
        if (actionMode.equalsIgnoreCase("EditMode"))
        {
            addBtn.setText("Update Emergency Contact");
            titleTxt.setText("Update Emergency Contact");
            nameTxt.setText(namePassStr);
            addTxt.setText(addressPassStr);
            cityTxt.setText(cityPassStr);
            stateTxt.setText(statePassStr);
            postalCodeTxt.setText(postalCodePassStr);
            telNoTxt.setText(telPassStr);
            mobileNoTxt.setText(mobPassStr);
            emailTxt.setText(emailPassStr);

            //check radio button
            if (typePassStr.equalsIgnoreCase("0"))
            {
                primoryBtn.setChecked(true);
                type = "0";
                emgTxt.setText("Primary Contact");
            }else
            {
                secondaryBtn.setChecked(true);
                type = "1";

                emgTxt.setText("Secondary Contact");
            }

            radioBtnLay.setVisibility(View.GONE);
            emgTxt.setVisibility(View.VISIBLE);
        }else
        {
            primoryBtn.setChecked(true);
            type = "0";

            radioBtnLay.setVisibility(View.VISIBLE);
            emgTxt.setVisibility(View.GONE);
        }



        //change spinner arrow color

        relationShipSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        relationshipAdapter = new ArrayAdapter<RelationShipeTypeModel>(AddNewEmergencyContactDetailsActivity.this, R.layout.customizespinner,
                relationshipList);
        relationshipAdapter.setDropDownViewResource(R.layout.customizespinner);
        relationShipSpinner.setAdapter(relationshipAdapter);



        //Title Spinner
        //change spinner arrow color
        titileSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);
        titleAdapter = new ArrayAdapter<String>(AddNewEmergencyContactDetailsActivity.this, R.layout.customizespinner,
                titleList);
        titleAdapter.setDropDownViewResource(R.layout.customizespinner);
        titileSpinner.setAdapter(titleAdapter);


        // country Spinner
        counterySpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);
        countryAdapter = new ArrayAdapter<CountryModel>(AddNewEmergencyContactDetailsActivity.this, R.layout.customizespinner,
                countryList);
        countryAdapter.setDropDownViewResource(R.layout.customizespinner);
        counterySpinner.setAdapter(countryAdapter);



        emergencyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


                if (checkedId == R.id.primory_radiobtn)
                {

                    type = "0";
                }
                else if (checkedId == R.id.secondary_radiobtn)
                {

                    type = "1";
                }
            }
        });

        //get tItle
        titileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                titleStr = titleList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // get relationship id
        relationShipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                relationshipIdStr = relationshipList.get(i).getRelationshipId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //get country id
        counterySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                countryIdStr = countryList.get(i).getCountryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //add Button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (titleStr.equalsIgnoreCase("Please select Title"))
                {
                    Toast.makeText(AddNewEmergencyContactDetailsActivity.this, "Please select name title", Toast.LENGTH_SHORT).show();
                }else if (nameTxt.getText().toString().equalsIgnoreCase(""))
                {
                    nameTxt.setError("Please enter name");
                    nameTxt.requestFocus();
                }else if (relationshipIdStr.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddNewEmergencyContactDetailsActivity.this, "Please select Relationship", Toast.LENGTH_SHORT).show();
                }else if (addTxt.getText().toString().equalsIgnoreCase(""))
                {
                    addTxt.setError("Please enter address");
                    addTxt.requestFocus();
                }else if (cityTxt.getText().toString().equalsIgnoreCase(""))
                {
                    cityTxt.setError("Please enter city");
                    cityTxt.requestFocus();
                }else if (stateTxt.getText().toString().equalsIgnoreCase(""))
                {
                    stateTxt.setError("Please enter state");
                    stateTxt.requestFocus();
                }else if (countryIdStr.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddNewEmergencyContactDetailsActivity.this, "Please select country", Toast.LENGTH_SHORT).show();
                }else if (postalCodeTxt.getText().toString().equalsIgnoreCase(""))
                {
                    postalCodeTxt.setError("Please enter postal code");
                    postalCodeTxt.requestFocus();
                }else {
                    if (conn.getConnectivityStatus() > 0) {
                        addEmergencyContactDetails(userId, recordIdStr, type, titleStr, authcode, nameTxt.getText().toString(), relationshipIdStr,
                                addTxt.getText().toString(), cityTxt.getText().toString(), stateTxt.getText().toString(), countryIdStr, postalCodeTxt.getText().toString(),
                                telNoTxt.getText().toString(), mobileNoTxt.getText().toString(), emailTxt.getText().toString());
                    } else {
                        conn.showNoInternetAlret();
                    }
                }
            }
        });

        //bind Data in spinner
        if (conn.getConnectivityStatus()>0)
        {
            personalDdlDetails();

        }else
            {
                conn.showNoInternetAlret();
            }



    }

    //add emergency contact
    public void addEmergencyContactDetails(final String AdminID  , final String RecordID, final String Type, final String Title,
                                           final String AuthCode, final String Name, final String RelationshipID, final String Address,
                                           final String City, final String State, final String CountryID, final String PostCode,
                                           final String PhoneNo, final String MobileNo, final String Email)  {

        final ProgressDialog pDialog = new ProgressDialog(AddNewEmergencyContactDetailsActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, addUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));
                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else  if (LoginStatus.equalsIgnoreCase("success")) {
                            onBackPressed();
                        }else {
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }


                    pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption" , e.getMessage());
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
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AdminID",AdminID);
                params.put("AuthCode",AuthCode);
                params.put("RecordID",RecordID);
                params.put("Type",Type);
                params.put("Title",Title);
                params.put("Name",Name);
                params.put("RelationshipID",RelationshipID);
                params.put("Address",Address);
                params.put("City",City);
                params.put("State",State);
                params.put("CountryID",CountryID);
                params.put("PostCode",PostCode);
                params.put("PhoneNo",PhoneNo);
                params.put("MobileNo",MobileNo);
                params.put("Email",Email);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //bind all spiiner data
    public void personalDdlDetails() {


        final ProgressDialog pDialog = new ProgressDialog(AddNewEmergencyContactDetailsActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalDdlDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    //bind Relation  List
                    if (relationshipList.size()>0)
                    {
                        relationshipList.clear();
                    }
                    relationshipList.add(new RelationShipeTypeModel("Please Select Relationship",""));
                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }else {
                        JSONArray relationshipObj = jsonObject.getJSONArray("RelationshipMaster");
                        for (int i =0; i<relationshipObj.length(); i++)
                        {
                            JSONObject object = relationshipObj.getJSONObject(i);

                            String RelationshipID = object.getString("RelationshipID");
                            String RelationshipName = object.getString("RelationshipName");

                            relationshipList.add(new RelationShipeTypeModel(RelationshipName,RelationshipID));

                        }

                        //bind Title List
                        if (titleList.size()>0)
                        {
                            titleList.clear();
                        }
                        titleList.add("Please select Title");
                        JSONArray TitleObj = jsonObject.getJSONArray("TitleMaster");
                        for (int i =0; i<TitleObj.length(); i++)
                        {
                            JSONObject object = TitleObj.getJSONObject(i);

                            String TitleName = object.getString("TitleName");
                            titleList.add(TitleName);

                        }

                        //bind Country Spinner
                        if (countryList.size()>0)
                        {
                            countryList.clear();
                        }

                        countryList.add(new CountryModel("","Please select Country"));
                        JSONArray countryObj = jsonObject.getJSONArray("CountryMaster");
                        for (int i =0; i<countryObj.length(); i++)
                        {
                            JSONObject object = countryObj.getJSONObject(i);

                            String CountryID = object.getString("CountryID");
                            String CountryName = object.getString("CountryName");
                            countryList.add(new CountryModel(CountryID,CountryName));

                        }

                        //Edit Option
                        for (int k =0; k<relationshipList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (relationshipList.get(k).getRelationshipName().equalsIgnoreCase(relationshipPassNameStr))
                                {
                                    relationshipIdStr = relationshipList.get(k).getRelationshipId();
                                    relationShipSpinner.setSelection(k);
                                }
                            }
                        }

                        for (int k =0; k<titleList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (titleList.get(k).equalsIgnoreCase(titlePassStr))
                                {
                                    titleStr = titleList.get(k);
                                    titileSpinner.setSelection(k);
                                }
                            }
                        }

                        for (int k =0; k<countryList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (countryList.get(k).getCountryName().equalsIgnoreCase(countryNamePassStr))
                                {
                                    countryIdStr = countryList.get(k).getCountryId();
                                    counterySpinner.setSelection(k);
                                }
                            }
                        }
                    }


                    countryAdapter.notifyDataSetChanged();
                    titleAdapter.notifyDataSetChanged();
                    relationshipAdapter.notifyDataSetChanged();
                    pDialog.dismiss();


                } catch (JSONException e) {
                    Log.e("checking json excption" , e.getMessage());
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }

    private void Logout() {


        finishAffinity();
        startActivity(new Intent(AddNewEmergencyContactDetailsActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddNewEmergencyContactDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddNewEmergencyContactDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddNewEmergencyContactDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddNewEmergencyContactDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddNewEmergencyContactDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddNewEmergencyContactDetailsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddNewEmergencyContactDetailsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddNewEmergencyContactDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddNewEmergencyContactDetailsActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
