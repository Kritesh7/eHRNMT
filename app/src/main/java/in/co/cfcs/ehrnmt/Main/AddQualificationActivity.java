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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import in.co.cfcs.ehrnmt.Model.DisciplineSpinnerModel;
import in.co.cfcs.ehrnmt.Model.QualificationSpinnerModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddQualificationActivity extends AppCompatActivity {

    public TextView titleTxt,passDateTxt,passDateBtn;
    public Spinner qualificationSpinner, courseTypeSpinner,deciplineSpinner;
    public ArrayList<QualificationSpinnerModel> qualificationList = new ArrayList<>();
    public ArrayList<String> courseTypeList = new ArrayList<>();
    public ArrayList<DisciplineSpinnerModel> deciplineList = new ArrayList<>();
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeeEducational";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeEducationInsUpdt";
    public ArrayAdapter<DisciplineSpinnerModel> deciplineAdapter;
    public ArrayAdapter<QualificationSpinnerModel> qualificationAdapter;
    public ConnectionDetector conn;
    public String authcode = "", userId = "";
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public EditText instituteTxt;
    public CheckBox highestDegreeCheck;
    public Button addBtn;
    public String courseType = "", deciplineID = "", qualifiucationId = "", highestDegreeTxt = "false", recordId = "", actionMode = ""
            ,qualificationNameStr = "",disciplineNameStr = "", passinDateStr = "", instituteStr = "", courseTypeNameStr = ""
            ,highestDegreeStr = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qualification);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.addqualificationtollbar);
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

        titleTxt.setText("Add Qualification Detail");


        Intent intent = getIntent();
        if (intent != null)
        {
            actionMode = intent.getStringExtra("Mode");
            recordId = intent.getStringExtra("RecordId");
            qualificationNameStr = intent.getStringExtra("QualificationName");
            disciplineNameStr = intent.getStringExtra("DeciplineName");
            passinDateStr = intent.getStringExtra("PassingDate");
            instituteStr = intent.getStringExtra("Institute");
            courseTypeNameStr = intent.getStringExtra("CourseTypeName");
            highestDegreeStr = intent.getStringExtra("HighestDegree");


            titleTxt.setText("Update Qualification Details");
        }

        conn = new ConnectionDetector(AddQualificationActivity.this);
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddQualificationActivity.this)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddQualificationActivity.this)));



        qualificationSpinner = (Spinner)findViewById(R.id.qualificationspinner);
        courseTypeSpinner = (Spinner)findViewById(R.id.coursetypespinner);
        deciplineSpinner = (Spinner)findViewById(R.id.deciplinespinner);
        passDateBtn = (TextView) findViewById(R.id.passdateTxt);
        passDateTxt = (TextView) findViewById(R.id.passdateTxt);
        instituteTxt = (EditText) findViewById(R.id.institutetxt);
        highestDegreeCheck = (CheckBox) findViewById(R.id.checkHighest);
        addBtn = (Button) findViewById(R.id.newrequestbtn);

        if (actionMode.equalsIgnoreCase("EditMode"))
        {
            passDateTxt.setText(passinDateStr);
            instituteTxt.setText(instituteStr);

            if (highestDegreeStr.equalsIgnoreCase("true"))
            {
                highestDegreeCheck.setChecked(true);
            }

            addBtn.setText("Update Qualification Details");

        }



        //Qualification List Spinner
        //change spinner arrow color
        qualificationSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        qualificationAdapter = new ArrayAdapter<QualificationSpinnerModel>(AddQualificationActivity.this, R.layout.customizespinner,
                qualificationList);
        qualificationAdapter.setDropDownViewResource(R.layout.customizespinner);
        qualificationSpinner.setAdapter(qualificationAdapter);

        //course Type Spinner
        if (courseTypeList.size()>0)
        {
            courseTypeList.clear();
        }
        courseTypeList.add("Please Select Course Type");
        courseTypeList.add("Part Time");
        courseTypeList.add("Full Time");
        courseTypeList.add("Correspondence");


        //change spinner arrow color
        courseTypeSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> CourseTypeAdapter = new ArrayAdapter<String>(AddQualificationActivity.this, R.layout.customizespinner,
                courseTypeList);
        CourseTypeAdapter.setDropDownViewResource(R.layout.customizespinner);
        courseTypeSpinner.setAdapter(CourseTypeAdapter);

        //eDIT mode
        for (int k =0; k<courseTypeList.size(); k++)
        {
            if (actionMode.equalsIgnoreCase("EditMode"))
            {
                if (courseTypeList.get(k).equalsIgnoreCase(courseTypeNameStr))
                {
                    courseTypeSpinner.setSelection(k);
                    courseType = courseTypeList.get(k);
                }
            }
        }


        //Decipline Spinner
        //change spinner arrow color
        deciplineSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        deciplineAdapter = new ArrayAdapter<DisciplineSpinnerModel>(AddQualificationActivity.this, R.layout.customizespinner,
                deciplineList);
        deciplineAdapter.setDropDownViewResource(R.layout.customizespinner);
        deciplineSpinner.setAdapter(deciplineAdapter);

        if (conn.getConnectivityStatus()>0)
        {
            personalDdlDetails(userId,authcode);
        }else
            {
                conn.showNoInternetAlret();
            }

        //Passing Date Picker
        passDateBtn.setOnClickListener(new View.OnClickListener() {
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddQualificationActivity.this,
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
                                passDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        //get course Type
        courseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                courseType = courseTypeList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //GET decipline Id
        deciplineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                deciplineID = deciplineList.get(i).getDesciplineId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //get qualification id
        qualificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                qualifiucationId = qualificationList.get(i).getQualificationId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //get Highest Degree
        highestDegreeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                {
                  highestDegreeTxt = "true";
                }else
                    {
                        highestDegreeTxt = "false";
                    }
            }
        });

        //Add new Education Details
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (qualifiucationId.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddQualificationActivity.this, "Please select qualification", Toast.LENGTH_SHORT).show();
                }else if (deciplineID.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddQualificationActivity.this, "Please select descipline", Toast.LENGTH_SHORT).show();
                }else if (passDateTxt.getText().toString().equalsIgnoreCase(""))
                {
                    passDateTxt.setError("Please enter passing date");
                }else if (instituteTxt.getText().toString().equalsIgnoreCase(""))
                {
                    instituteTxt.setError("Please enter institute");
                }else if (courseType.equalsIgnoreCase("Please Select Course Type"))
                {
                    Toast.makeText(AddQualificationActivity.this, "Please select course type", Toast.LENGTH_SHORT).show();
                }else {

                    if (conn.getConnectivityStatus()>0) {
                        addEducationDetails(userId, recordId, instituteTxt.getText().toString(), passDateTxt.getText().toString(),
                                courseType, deciplineID, qualifiucationId, highestDegreeTxt, authcode);
                    }else
                        {
                            conn.showNoInternetAlret();
                        }
                }
            }
        });

    }

    //bind all spiiner data
    public void personalDdlDetails(final String AdminID, final String AuthCode ) {


        final ProgressDialog pDialog = new ProgressDialog(AddQualificationActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalDdlDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    //bind Skills List
                    if (qualificationList.size()>0)
                    {
                        qualificationList.clear();
                    }
                    qualificationList.add(new QualificationSpinnerModel("Please Select Qualification",""));

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

                        JSONArray qualificationObj = jsonObject.getJSONArray("QualificationMaster");
                        for (int i =0; i<qualificationObj.length(); i++)
                        {
                            JSONObject object = qualificationObj.getJSONObject(i);

                            String QualificationID = object.getString("QualificationID");
                            String QualificationName = object.getString("QualificationName");

                            qualificationList.add(new QualificationSpinnerModel(QualificationName,QualificationID));

                        }

                        if (deciplineList.size()>0)
                        {
                            deciplineList.clear();
                        }
                        deciplineList.add(new DisciplineSpinnerModel("Please Select Discipline",""));

                        JSONArray descObj = jsonObject.getJSONArray("DisciplineMaster");
                        for (int i =0; i<descObj.length(); i++)
                        {
                            JSONObject object = descObj.getJSONObject(i);

                            String DisciplineID = object.getString("DisciplineID");
                            String DisciplineName = object.getString("DisciplineName");

                            deciplineList.add(new DisciplineSpinnerModel(DisciplineName,DisciplineID));

                        }

                        //Edit Mode
                        for (int k =0; k<qualificationList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (qualificationList.get(k).getQualification().equalsIgnoreCase(qualificationNameStr))
                                {
                                    qualificationSpinner.setSelection(k);
                                    qualifiucationId = qualificationList.get(k).getQualificationId();
                                }
                            }
                        }

                        for (int k =0; k<deciplineList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (deciplineList.get(k).getDescipline().equalsIgnoreCase(disciplineNameStr))
                                {
                                    deciplineSpinner.setSelection(k);
                                    deciplineID = deciplineList.get(k).getDesciplineId();
                                }
                            }
                        }
                    }


                    deciplineAdapter.notifyDataSetChanged();
                    qualificationAdapter.notifyDataSetChanged();
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



                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //Add EducationDeatils
    public void addEducationDetails(final String AdminID  , final String RecordID, final String Institute, final String PassingDate,
                                    final String CourseType, final String DisciplineID , final String QualificationID ,
                                    final String HighestDegree, final String AuthCode)  {

        final ProgressDialog pDialog = new ProgressDialog(AddQualificationActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
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
                        } else  if (LoginStatus.equalsIgnoreCase("success")){
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
                params.put("Institute",Institute);
                params.put("CourseType",CourseType);
                params.put("PassingDate",PassingDate);
                params.put("DisciplineID",DisciplineID);
                params.put("QualificationID",QualificationID);
                params.put("HighestDegree",HighestDegree);

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
        startActivity(new Intent(AddQualificationActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddQualificationActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddQualificationActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddQualificationActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddQualificationActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddQualificationActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddQualificationActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddQualificationActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddQualificationActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddQualificationActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
