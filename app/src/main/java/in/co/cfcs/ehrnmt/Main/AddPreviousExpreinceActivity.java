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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.co.cfcs.ehrnmt.Model.MonthModel;
import in.co.cfcs.ehrnmt.Model.YearModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddPreviousExpreinceActivity extends AppCompatActivity {

    public TextView titleTxt,joiningDateBtn,joiningDateTxt,releavinfDateBtn,releavingDateTxt;
    public Spinner yearSpinner, monthSpinner;
    public ArrayList<YearModel> yearList = new ArrayList<>();
    public ArrayList<MonthModel> monthList = new ArrayList<>();
    public EditText   compNameTxt, designationTxt, jobDesTxt;
    public Button addBtn;
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeePreviousExperienceInsUpdt";
    public ConnectionDetector conn;
    public String authcode = "", userId = "", year = "", month = "", actionMode = "", recordId = "", compNameStr = ""
            , joiningDateStr = "" , relivingDateStr = "", designationStr = "", yearStrId = "", monthIdStr = ""
            ,jobDescStr = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_previous_expreince);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.previoustollbar);
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

        titleTxt.setText("Add Previous Experience");

        Intent intent = getIntent();
        if (intent != null)
        {
            actionMode = intent.getStringExtra("ActionMode");
            recordId = intent.getStringExtra("RecordId");
            compNameStr = intent.getStringExtra("CompanyName");
            joiningDateStr = intent.getStringExtra("JoiningDate");
            relivingDateStr = intent.getStringExtra("RelivingDate");
            designationStr = intent.getStringExtra("Designation");
            yearStrId = intent.getStringExtra("JobYearId");
            monthIdStr = intent.getStringExtra("JobMonthId");
            jobDescStr = intent.getStringExtra("JobDescription");
        }

        conn = new ConnectionDetector(AddPreviousExpreinceActivity.this);
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddPreviousExpreinceActivity.this)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddPreviousExpreinceActivity.this)));


        yearSpinner = (Spinner)findViewById(R.id.yearspinner);
        monthSpinner = (Spinner)findViewById(R.id.monthspinner);
        joiningDateBtn = (TextView) findViewById(R.id.joinongdatetxt);
        joiningDateTxt = (TextView) findViewById(R.id.joinongdatetxt);
        releavinfDateBtn = (TextView) findViewById(R.id.releavingdatetxt);
        releavingDateTxt = (TextView) findViewById(R.id.releavingdatetxt);
        compNameTxt = (EditText) findViewById(R.id.compName);
        designationTxt = (EditText) findViewById(R.id.designation);
        jobDesTxt = (EditText) findViewById(R.id.jobdesc);
        addBtn = (Button) findViewById(R.id.newrequestbtn);

        if (actionMode.equalsIgnoreCase("EditMode"))
        {
            compNameTxt.setText(compNameStr);
            joiningDateTxt.setText(joiningDateStr);
            releavingDateTxt.setText(relivingDateStr);
            designationTxt.setText(designationStr);
            jobDesTxt.setText(jobDescStr);

            addBtn.setText("Update Previous Experience");

            titleTxt.setText("Update Previous Experience");
        }


        //Year List Spinner
        if (yearList.size()>0)
        {
            yearList.clear();
        }
        yearList.add(new YearModel("Please Select Year", ""));
        yearList.add(new YearModel("0 Year","0"));
        yearList.add(new YearModel("1 Year","1"));
        yearList.add(new YearModel("2 Year's","2"));
        yearList.add(new YearModel("3 Year's","3"));
        yearList.add(new YearModel("4 Year's","4"));
        yearList.add(new YearModel("5 Year's","5"));
        yearList.add(new YearModel("6 Year's","6"));
        yearList.add(new YearModel("7 Year's","7"));
        yearList.add(new YearModel("8 Year's","8"));
        yearList.add(new YearModel("9 Year's","9"));
        yearList.add(new YearModel("10 Year's","10"));
        yearList.add(new YearModel("11 Year's","11"));
        yearList.add(new YearModel("12 Year's","12"));
        yearList.add(new YearModel("13 Year's","13"));
        yearList.add(new YearModel("14 Year's","14"));
        yearList.add(new YearModel("15 Year's","15"));
        yearList.add(new YearModel("16 Year's","16"));
        yearList.add(new YearModel("17 Year's","17"));
        yearList.add(new YearModel("18 Year's","18"));
        yearList.add(new YearModel("19 Year's","19"));
        yearList.add(new YearModel("20 Year's","20"));
        yearList.add(new YearModel("21 Year's","21"));
        yearList.add(new YearModel("22 Year's","22"));
        yearList.add(new YearModel("23 Year's","23"));
        yearList.add(new YearModel("24 Year's","24"));
        yearList.add(new YearModel("25 Year's","25"));
        yearList.add(new YearModel("26 Year's","26"));
        yearList.add(new YearModel("27 Year's","27"));
        yearList.add(new YearModel("28 Year's","28"));
        yearList.add(new YearModel("29 Year's","29"));
        yearList.add(new YearModel("30 Year's","30"));
        yearList.add(new YearModel("31 Year's","31"));
        yearList.add(new YearModel("32 Year's","32"));
        yearList.add(new YearModel("33 Year's","33"));
        yearList.add(new YearModel("34 Year's","34"));
        yearList.add(new YearModel("35 Year's","35"));


        //change spinner arrow color
        yearSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<YearModel> yearAdapter = new ArrayAdapter<YearModel>(AddPreviousExpreinceActivity.this, R.layout.customizespinner,
                yearList);
        yearAdapter.setDropDownViewResource(R.layout.customizespinner);
        yearSpinner.setAdapter(yearAdapter);

        //month list spinner
        if (monthList.size()>0)
        {
            monthList.clear();
        }
        monthList.add(new MonthModel(0, "0 Month"));
        monthList.add(new MonthModel(1, "1 Month"));
        monthList.add(new MonthModel(2, "2 Month"));
        monthList.add(new MonthModel(3, "3 Month"));
        monthList.add(new MonthModel(4, "4 Month"));
        monthList.add(new MonthModel(5, "5 Month"));
        monthList.add(new MonthModel(6, "6 Month"));
        monthList.add(new MonthModel(7, "7 Month"));
        monthList.add(new MonthModel(8, "8 Month"));
        monthList.add(new MonthModel(9, "9 Month"));
        monthList.add(new MonthModel(10, "10 Month"));
        monthList.add(new MonthModel(11, "11 Month"));
        monthList.add(new MonthModel(12, "12 Month"));

        //Edit Mode spinner
        if (actionMode.equalsIgnoreCase("EditMode"))
        {
            for (int i=0; i<yearList.size(); i++)
            {
                if (yearList.get(i).getYearId().equalsIgnoreCase(yearStrId))
                {
                    yearSpinner.setSelection(i);
                }
            }

            for (int i=0; i<monthList.size(); i++)
            {
                if (monthList.get(i).getMonthId()== Integer.parseInt(monthIdStr))
                {
                    monthSpinner.setSelection(i);
                }
            }
        }

        //change spinner arrow color
        monthSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<MonthModel> MonthAdater = new ArrayAdapter<MonthModel>(AddPreviousExpreinceActivity.this, R.layout.customizespinner,
                monthList);
        MonthAdater.setDropDownViewResource(R.layout.customizespinner);
        monthSpinner.setAdapter(MonthAdater);

        joiningDateBtn.setOnClickListener(new View.OnClickListener() {
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPreviousExpreinceActivity.this,
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
                                joiningDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        //relaving Date Picker
        releavinfDateBtn.setOnClickListener(new View.OnClickListener() {
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPreviousExpreinceActivity.this,
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
                                releavingDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //get year and month
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                year = yearList.get(i).getYearId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                month = String.valueOf(monthList.get(i).getMonthId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //add new previous expreince
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (compNameTxt.getText().toString().equalsIgnoreCase(""))
                {
                    compNameTxt.setError("Please enter company name");
                }else if (joiningDateTxt.getText().toString().equalsIgnoreCase(""))
                {
                    joiningDateTxt.setError("Please enter joining date");
                }else if (releavingDateTxt.getText().toString().equalsIgnoreCase(""))
                {
                    releavingDateTxt.setError("Please enter Reliving date");
                }else if (year.equalsIgnoreCase("Please Select Year"))
                {
                    Toast.makeText(AddPreviousExpreinceActivity.this, "Please select job periode", Toast.LENGTH_SHORT).show();
                }else if (jobDesTxt.getText().toString().equalsIgnoreCase(""))
                {
                    jobDesTxt.setError("Please enter job description");
                }else {
                    if (conn.getConnectivityStatus() > 0) {

                        addPreviousExpreinceDetails(userId, recordId, compNameTxt.getText().toString(), joiningDateTxt.getText().toString(),
                                designationTxt.getText().toString(), releavingDateTxt.getText().toString(), year, month, jobDesTxt.getText().toString(),
                                authcode);
                    } else {
                        conn.showNoInternetAlret();
                    }
                }
            }
        });
    }

    //Add Previous Expreince
    public void addPreviousExpreinceDetails(final String AdminID  , final String RecordID, final String CompanyName, final String JoiningDate,
                                            final String Designation, final String RelievingDate , final String Year , final String Month,
                                            final String Desc , final String AuthCode)  {

        final ProgressDialog pDialog = new ProgressDialog(AddPreviousExpreinceActivity.this,R.style.AppCompatAlertDialogStyle);
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
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
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
                params.put("CompanyName",CompanyName);
                params.put("Designation",Designation);
                params.put("JoiningDate",JoiningDate);
                params.put("RelievingDate",RelievingDate);
                params.put("Year",Year);
                params.put("Month",Month);
                params.put("Desc",Desc);

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
        startActivity(new Intent(AddPreviousExpreinceActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddPreviousExpreinceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddPreviousExpreinceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddPreviousExpreinceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddPreviousExpreinceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddPreviousExpreinceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddPreviousExpreinceActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddPreviousExpreinceActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddPreviousExpreinceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddPreviousExpreinceActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
