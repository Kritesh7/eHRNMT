package in.co.cfcs.ehrnmt.Manager.ManagerActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.co.cfcs.ehrnmt.Adapter.AttendanceLogListAdapter;
import in.co.cfcs.ehrnmt.Main.AddHotelActivity;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.AttendanceLogDetailsModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerAttendanceLogDetailsActivity extends AppCompatActivity {

    public TextView titleTxt;
    public String empId = "";
    public AttendanceLogListAdapter adapter;
    public ArrayList<AttendanceLogDetailsModel> list = new ArrayList<>();
    public RecyclerView attendanceLogRecy;
    public TextView noRecordFoundTxt;
    public ConnectionDetector conn;
    public String userId = "", authCode = "";
    public ImageView calBtn;
    public TextView calTxt;
    public String attendnaceListUrl = SettingConstant.BaseUrl + "AppEmployeeAttendanceLogList";
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_attendance_log_details);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.mgrtoolbar);
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

        Intent intent = getIntent();
        if (intent != null) {
            empId = intent.getStringExtra("empId");
        }

        titleTxt.setText("Attendance Basic Log");

        attendanceLogRecy = (RecyclerView) findViewById(R.id.attendace_log_list_recycler);
        noRecordFoundTxt = (TextView) findViewById(R.id.norecordfound);
        calBtn = (ImageView) findViewById(R.id.calBtn);
        calTxt = (TextView) findViewById(R.id.caldatetxt);


        conn = new ConnectionDetector(ManagerAttendanceLogDetailsActivity.this);

        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerAttendanceLogDetailsActivity.this)));
        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerAttendanceLogDetailsActivity.this)));

        adapter = new AttendanceLogListAdapter(ManagerAttendanceLogDetailsActivity.this, list, ManagerAttendanceLogDetailsActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManagerAttendanceLogDetailsActivity.this);
        attendanceLogRecy.setLayoutManager(mLayoutManager);
        attendanceLogRecy.setItemAnimator(new DefaultItemAnimator());
        attendanceLogRecy.setAdapter(adapter);

        attendanceLogRecy.getRecycledViewPool().setMaxRecycledViews(0, 0);

        calTxt.setText(getCurrentTime());

        calBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

               /* inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);*/
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ManagerAttendanceLogDetailsActivity.this,
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
                                calTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                                attendaceList(authCode, userId, empId, dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        //List Bind
        if (conn.getConnectivityStatus() > 0) {
            attendaceList(authCode, userId, empId, getCurrentTime());
        } else {
            conn.showNoInternetAlret();
        }


    }

    //get current time
    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();
        String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(cal.getTime());
        //sdf = new DateFormatSymbols().getShortMonths()[month];

        return dateFormat.format(cal.getTime());
    }

    //Attendace List
    public void attendaceList(final String AuthCode, final String AdminID, final String EmployeeID, final String LogDate) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerAttendanceLogDetailsActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, attendnaceListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    if (list.size() > 0) {
                        list.clear();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("status")) {
                            LoginStatus = jsonObject.getString("status");
                            msgstatus = jsonObject.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                            }
                        }else{

                            String UserName = jsonObject.getString("UserName");
                            String EmpID = jsonObject.getString("EmpID");
                            String DesignationName = jsonObject.getString("DesignationName");
                            String ZoneName = jsonObject.getString("ZoneName");
                            String LogDateText = jsonObject.getString("LogDateText");
                            String LogTime = jsonObject.getString("LogTime");
                            String LocationAddress = jsonObject.getString("LocationAddress");
                            String LocationPhoto = jsonObject.getString("FileNameText");
                            String Remark = jsonObject.getString("Remark");
                            String LogTypeText = jsonObject.getString("LogTypeText");
                            String ApprovalStatusText = jsonObject.getString("ApprovalStatusText");
                            String ApprovalDateText = jsonObject.getString("ApprovalDateText");
                            String ApprovedBy = jsonObject.getString("ApprovedBy");


                            list.add(new AttendanceLogDetailsModel(UserName, EmpID, DesignationName, LogTime, LogDateText, LogTypeText
                                    , LocationAddress, Remark, LocationPhoto, ZoneName, ApprovalStatusText, ApprovalDateText, ApprovedBy));


                        }

                    }

                    if (list.size() == 0) {
                        noRecordFoundTxt.setVisibility(View.VISIBLE);
                        attendanceLogRecy.setVisibility(View.GONE);
                    } else {
                        noRecordFoundTxt.setVisibility(View.GONE);
                        attendanceLogRecy.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();
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
                params.put("AuthCode", AuthCode);
                params.put("LoginAdminID", AdminID);
                params.put("EmployeeID", EmployeeID);
                params.put("LogDate", LogDate);


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
        startActivity(new Intent(ManagerAttendanceLogDetailsActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ManagerAttendanceLogDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ManagerAttendanceLogDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ManagerAttendanceLogDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ManagerAttendanceLogDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ManagerAttendanceLogDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ManagerAttendanceLogDetailsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ManagerAttendanceLogDetailsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ManagerAttendanceLogDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ManagerAttendanceLogDetailsActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }


}
