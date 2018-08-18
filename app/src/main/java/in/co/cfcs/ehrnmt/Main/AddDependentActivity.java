package in.co.cfcs.ehrnmt.Main;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.co.cfcs.ehrnmt.Model.RelationShipeTypeModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddDependentActivity extends AppCompatActivity {

    public TextView titleTxt, dobBtn, dobTxt;
    public Spinner relationshipSpinner;
    public ArrayList<RelationShipeTypeModel> relationshipList = new ArrayList<>();
    public ArrayAdapter<RelationShipeTypeModel> relationShipAdapter;
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeePersonalData";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeDependentInsUpdt";
    public ConnectionDetector conn;

    public EditText firstNameTxt, lastNameTxt;
    public RadioGroup genderRadioGroup;
    public RadioButton maleBtn, femailBtn;
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public Button addBtn;
    public String authcode = "", userId = "", genderIdString = "", relationshipidStr = "", actionMode = "", firstNameStr = "", lastNameStr = "", genderNameStr = "", relationshipNameStr = "", dobStr = "", recordIdStr = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dependent);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.adddependenttollbar);
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

        titleTxt.setText("Add New Dependent");

        Intent intent = getIntent();
        if (intent != null) {
            recordIdStr = intent.getStringExtra("RecordId");
            actionMode = intent.getStringExtra("Mode");
            firstNameStr = intent.getStringExtra("FirstName");
            lastNameStr = intent.getStringExtra("LastName");
            genderNameStr = intent.getStringExtra("GenderName");
            relationshipNameStr = intent.getStringExtra("RelationshipName");
            dobStr = intent.getStringExtra("DOB");
        }

        conn = new ConnectionDetector(AddDependentActivity.this);
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddDependentActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddDependentActivity.this)));


        relationshipSpinner = (Spinner) findViewById(R.id.relationshipespinner);
        dobBtn = (TextView) findViewById(R.id.dobtxt);
        dobTxt = (TextView) findViewById(R.id.dobtxt);
        firstNameTxt = (EditText) findViewById(R.id.firstnametxt);
        lastNameTxt = (EditText) findViewById(R.id.lastnametxt);
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderradiogroup);
        maleBtn = (RadioButton) findViewById(R.id.malebtn);
        femailBtn = (RadioButton) findViewById(R.id.femailbtn);
        addBtn = (Button) findViewById(R.id.newrequestbtn);


        //check action
        if (actionMode.equalsIgnoreCase("EditMode")) {
            addBtn.setText("Update Dependent");
            titleTxt.setText("Update Dependent");

            firstNameTxt.setText(firstNameStr);
            lastNameTxt.setText(lastNameStr);
            dobTxt.setText(dobStr);


            if (genderNameStr.equalsIgnoreCase(maleBtn.getText().toString())) {
                maleBtn.setChecked(true);
                genderIdString = "1";
            } else {
                femailBtn.setChecked(true);
                genderIdString = "2";
            }
        }

        //Relationship Type Spinner
        //change spinner arrow color
        relationshipSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        relationShipAdapter = new ArrayAdapter<RelationShipeTypeModel>(AddDependentActivity.this, R.layout.customizespinner,
                relationshipList);
        relationShipAdapter.setDropDownViewResource(R.layout.customizespinner);
        relationshipSpinner.setAdapter(relationShipAdapter);

        //dob date picker
        dobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddDependentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                yy = year;
                                mm = monthOfYear;
                                dd = dayOfMonth;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.MONTH, monthOfYear);
                                String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(calendar.getTime());
                                sdf = new DateFormatSymbols().getShortMonths()[monthOfYear];

                                Log.e("checking,............", sdf + " null");
                                dobTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        //find genderID
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (R.id.malebtn == i) {
                    genderIdString = "1";

                } else {
                    genderIdString = "2";

                }
            }
        });

        // get relationship id
        relationshipSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                relationshipidStr = relationshipList.get(i).getRelationshipId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // add new Dependent
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firstNameTxt.getText().toString().equalsIgnoreCase("")) {
                    firstNameTxt.setError("Please enter first name");
                } else if (lastNameTxt.getText().toString().equalsIgnoreCase("")) {
                    lastNameTxt.setError("Please enter last name");
                } else if (dobTxt.getText().toString().equalsIgnoreCase("")) {
                    dobTxt.setError("Please enter dob");
                } else if (genderIdString.equalsIgnoreCase("")) {
                    Toast.makeText(AddDependentActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                } else if (relationshipidStr.equalsIgnoreCase("")) {
                    Toast.makeText(AddDependentActivity.this, "Please select relationship", Toast.LENGTH_SHORT).show();
                } else {

                    if (conn.getConnectivityStatus() > 0) {

                        addDepenedntData(userId, recordIdStr, firstNameTxt.getText().toString(), lastNameTxt.getText().toString(), authcode,
                                dobTxt.getText().toString(), genderIdString, relationshipidStr);
                    } else {
                        conn.showNoInternetAlret();
                    }
                }
            }
        });
        // bind spinner data
        if (conn.getConnectivityStatus() > 0) {
            personalDdlDetails();
        } else {
            conn.showNoInternetAlret();
        }

    }

    // add new Depenednt
    public void addDepenedntData(final String AdminID, final String RecordID, final String FirstName, final String LastName,
                                 final String AuthCode, final String DOB, final String GenderID, final String RelationshipID) {

        final ProgressDialog pDialog = new ProgressDialog(AddDependentActivity.this, R.style.AppCompatAlertDialogStyle);
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
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();

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
                params.put("FirstName", FirstName);
                params.put("LastName", LastName);
                params.put("DOB", DOB);
                params.put("GenderID", GenderID);
                params.put("RelationshipID", RelationshipID);


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


        final ProgressDialog pDialog = new ProgressDialog(AddDependentActivity.this, R.style.AppCompatAlertDialogStyle);
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

                    //bind material List
                    if (relationshipList.size() > 0) {
                        relationshipList.clear();
                    }
                    relationshipList.add(new RelationShipeTypeModel("Please Select Relationship", ""));
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

                        JSONArray relationshipObj = jsonObject.getJSONArray("RelationshipMaster");
                        for (int i = 0; i < relationshipObj.length(); i++) {
                            JSONObject object = relationshipObj.getJSONObject(i);

                            String RelationshipID = object.getString("RelationshipID");
                            String RelationshipName = object.getString("RelationshipName");
                            relationshipList.add(new RelationShipeTypeModel(RelationshipName, RelationshipID));
                        }
                        for (int k = 0; k < relationshipList.size(); k++) {
                            if (actionMode.equalsIgnoreCase("EditMode")) {
                                if (relationshipList.get(k).getRelationshipName().equalsIgnoreCase(relationshipNameStr)) {
                                    relationshipidStr = relationshipList.get(k).getRelationshipId();
                                    relationshipSpinner.setSelection(k);
                                }
                            }
                        }
                    }


                    relationShipAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }

    private void Logout() {

        finishAffinity();
        startActivity(new Intent(AddDependentActivity.this, LoginActivity.class));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddDependentActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddDependentActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddDependentActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddDependentActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddDependentActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddDependentActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddDependentActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddDependentActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddDependentActivity.this,
                "")));

    }

}
