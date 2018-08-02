package in.co.cfcs.ehrnmt.Main;

import android.app.DatePickerDialog;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
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

import in.co.cfcs.ehrnmt.Model.LeaveTypeModel;
import in.co.cfcs.ehrnmt.Model.LeaveYearTypeModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class NewAddLeaveMangementActivity extends AppCompatActivity {

    public Spinner leaveTypeSpinner, leaveYearSpinner;
    public ArrayList<LeaveTypeModel> leaveTypeList = new ArrayList<>();
    public ArrayList<LeaveYearTypeModel> leaveYearList = new ArrayList<>();
    public ImageView startCal, endCal;
    public int month, year, day;
    public EditText startTxt, endTxt;
    public TextView titleTxt;
    public String leaveYearUrl = SettingConstant.BaseUrl + "AppEmployeeLeaveYearList";
    public String leaveTypeUrl = SettingConstant.BaseUrl + "AppEmployeeLeaveTypeList";
    public ArrayAdapter<LeaveYearTypeModel> leaveYearAdapter;
    public ArrayAdapter<LeaveTypeModel> leaveTypeAdapter;
    public String userId = "", authcode = "", mgrId = "", leaveId = "", firstHalfString = "false", secondHalfString = "false",
            yearString = "", userName = "", compId = "", leaveTypeStr = "";
    public ConnectionDetector conn;
    public String applyUrl = SettingConstant.BaseUrl + "AppEmployeeLeaveApply";
    public Button applyBtn;
    public EditText commentTxt;
    public CheckBox firstHalfCheck, secondHalfCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_leave_mangement);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.newaddtollbar);
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

        titleTxt.setText("Apply For Leave");

        leaveTypeSpinner = (Spinner) findViewById(R.id.leavetypespinner);
        leaveYearSpinner = (Spinner) findViewById(R.id.leaveyearspinner);

        startCal = (ImageView) findViewById(R.id.cal);
        endCal = (ImageView) findViewById(R.id.end_cal);
        startTxt = (EditText) findViewById(R.id.startdate);
        endTxt = (EditText) findViewById(R.id.enddate);
        applyBtn = (Button) findViewById(R.id.applyleave);
        firstHalfCheck = (CheckBox) findViewById(R.id.firsthalfcheck);
        secondHalfCheck = (CheckBox) findViewById(R.id.secondhalfcheck);
        commentTxt = (EditText) findViewById(R.id.leave_comment);

        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(NewAddLeaveMangementActivity.this)));
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(NewAddLeaveMangementActivity.this)));
        mgrId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getMgrDir(NewAddLeaveMangementActivity.this)));
        userName = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserName(NewAddLeaveMangementActivity.this)));
        compId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getCompanyId(NewAddLeaveMangementActivity.this)));
        conn = new ConnectionDetector(NewAddLeaveMangementActivity.this);


        startCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(new View(NewAddLeaveMangementActivity.this).getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                Calendar cal = Calendar.getInstance();

                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewAddLeaveMangementActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                               /* mm = month;
                                yy = year;
                                dd = dayOfMonth;*/

                                Calendar calendar = Calendar.getInstance();
                                String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(calendar.getTime());
                                sdf = new DateFormatSymbols().getShortMonths()[month];


                                startTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        endCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(new View(NewAddLeaveMangementActivity.this).getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                Calendar cal = Calendar.getInstance();

                day = cal.get(Calendar.DAY_OF_MONTH);
                month = cal.get(Calendar.MONTH);
                year = cal.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewAddLeaveMangementActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                               /* mm = month;
                                yy = year;
                                dd = dayOfMonth;*/
                                Calendar calendar = Calendar.getInstance();
                                String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(calendar.getTime());
                                sdf = new DateFormatSymbols().getShortMonths()[month];

                                endTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        //Leave Type Spinner Work
        leaveTypeSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        leaveTypeAdapter = new ArrayAdapter<LeaveTypeModel>(NewAddLeaveMangementActivity.this, R.layout.customizespinner,
                leaveTypeList);
        leaveTypeAdapter.setDropDownViewResource(R.layout.customizespinner);
        leaveTypeSpinner.setAdapter(leaveTypeAdapter);


        //leaveYearType Spinner
        leaveYearSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        leaveYearAdapter = new ArrayAdapter<LeaveYearTypeModel>(NewAddLeaveMangementActivity.this, R.layout.customizespinner,
                leaveYearList);
        leaveYearAdapter.setDropDownViewResource(R.layout.customizespinner);
        leaveYearSpinner.setAdapter(leaveYearAdapter);


        //get leave year type Data
        if (conn.getConnectivityStatus() > 0) {

            getLeaveYearType(authcode, userId);

        } else {
            conn.showNoInternetAlret();
        }


        //selectbale Year Type Spinner
        leaveYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //     String year = ;

                yearString = leaveYearList.get(i).getLeaveYear();

                try {

                    if(loginStatus != 0){
                        getLeaveType(authcode, userId, leaveYearList.get(i + 1).getLeaveYear());
                    }


                } catch (IndexOutOfBoundsException e) {

                    if(loginStatus != 0){
                        getLeaveType(authcode, userId, leaveYearList.get(i).getLeaveYear());
                    }


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //get leave id to select leave type
        leaveTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                leaveId = leaveTypeList.get(i).getLeaveID();
                leaveTypeStr = leaveTypeList.get(i).getLeaveTypeName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //first half check
        firstHalfCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    firstHalfString = "true";
                } else {
                    firstHalfString = "false";
                }
            }
        });

        secondHalfCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    secondHalfString = "true";
                } else {
                    secondHalfString = "false";
                }
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (yearString.equalsIgnoreCase("")) {
                    Toast.makeText(NewAddLeaveMangementActivity.this, "Please Select Year", Toast.LENGTH_SHORT).show();
                } else if (leaveId.equalsIgnoreCase("")) {
                    Toast.makeText(NewAddLeaveMangementActivity.this, "Please Select Leave Type", Toast.LENGTH_SHORT).show();
                } else if (startTxt.getText().toString().equalsIgnoreCase("")) {
                    startTxt.setError("Please Enter valid Start date");
                } else if (endTxt.getText().toString().equalsIgnoreCase("")) {
                    endTxt.setError("Please Enter valid End date");
                } else {
                    if (conn.getConnectivityStatus() > 0) {

                        applyLeave(userId, mgrId, leaveId, startTxt.getText().toString(), firstHalfString, endTxt.getText().toString(),
                                secondHalfString, commentTxt.getText().toString(), yearString, authcode, compId, userName,
                                leaveTypeStr);
                    } else {
                        conn.showNoInternetAlret();
                    }
                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent i = new Intent(NewAddLeaveMangementActivity.this,HomeActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }


    //leave Year spinner bind Data
    public void getLeaveYearType(final String AuthCode, final String AdminID) {


        final ProgressDialog pDialog = new ProgressDialog(NewAddLeaveMangementActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, leaveYearUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    if (leaveYearList.size() > 0) {
                        leaveYearList.clear();
                    }

                    leaveYearList.add(new LeaveYearTypeModel("", "Please Select Leave Year"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.has("MsgNotification")) {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            Toast.makeText(NewAddLeaveMangementActivity.this,MsgNotification, Toast.LENGTH_LONG).show();
                            Logout();
                        }else{
                            String LeaveYear = jsonObject.getString("LeaveYear");
                            String LeaveYearText = jsonObject.getString("LeaveYearText");

                            leaveYearList.add(new LeaveYearTypeModel(LeaveYear, LeaveYearText));

                        }




                    }

                    //getLeaveType(authcode,userId,leaveYearList.get(1).getLeaveYear());

                    leaveYearAdapter.notifyDataSetChanged();
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

                Toast.makeText(NewAddLeaveMangementActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode", AuthCode);
                params.put("AdminID", AdminID);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //Leave Type Spinner bind
    public void getLeaveType(final String AuthCode, final String AdminID, final String Year) {


        final ProgressDialog pDialog = new ProgressDialog(NewAddLeaveMangementActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, leaveTypeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    if (leaveTypeList.size() > 0) {
                        leaveTypeList.clear();
                    }

                    leaveTypeList.add(new LeaveTypeModel("", "Please Select Leave Type"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String LeaveID = jsonObject.getString("LeaveID");
                        String LeaveTypeName = jsonObject.getString("LeaveTypeName");

                        leaveTypeList.add(new LeaveTypeModel(LeaveID, LeaveTypeName));


                    }

                    leaveTypeAdapter.notifyDataSetChanged();
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

                Toast.makeText(NewAddLeaveMangementActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode", AuthCode);
                params.put("AdminID", AdminID);
                params.put("Year", Year);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //Apply  Leave
    public void applyLeave(final String AdminID, final String MgrID, final String LeaveID, final String FromDate,
                           final String FirstHalf, final String ToDate, final String SecondHalf,
                           final String Comments, final String Year, final String AuthCode, final String compaId,
                           final String username, final String leavetype) {

        final ProgressDialog pDialog = new ProgressDialog(NewAddLeaveMangementActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, applyUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Apply Short Leave", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.has("MsgNotification")) {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            Toast.makeText(NewAddLeaveMangementActivity.this, MsgNotification, Toast.LENGTH_SHORT).show();

                        }

                        if (jsonObject.has("status")) {
                            String status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("success")) {
                                onBackPressed();

                            }
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

                Toast.makeText(NewAddLeaveMangementActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AdminID", AdminID);
                params.put("MgrID", MgrID);
                params.put("LeaveID", LeaveID);
                params.put("FromDate", FromDate);
                params.put("FirstHalf", FirstHalf);
                params.put("ToDate", ToDate);
                params.put("SecondHalf", SecondHalf);
                params.put("Comments", Comments);
                params.put("Year", Year);
                params.put("AuthCode", AuthCode);
                params.put("UserName", username);
                params.put("CompID", compaId);
                params.put("LeaveType", leavetype);

                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    int loginStatus = 1;
    private void Logout() {

        loginStatus = 0;
        finishAffinity();
        startActivity(new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class));


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(NewAddLeaveMangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(NewAddLeaveMangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(NewAddLeaveMangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(NewAddLeaveMangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(NewAddLeaveMangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(NewAddLeaveMangementActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(NewAddLeaveMangementActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(NewAddLeaveMangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(NewAddLeaveMangementActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }




}
