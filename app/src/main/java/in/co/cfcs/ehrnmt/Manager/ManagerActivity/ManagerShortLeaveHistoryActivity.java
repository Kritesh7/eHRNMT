package in.co.cfcs.ehrnmt.Manager.ManagerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Adapter.ShortLeaveHistoryAdapter;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.MonthModel;
import in.co.cfcs.ehrnmt.Model.ShortLeaveHistoryModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerShortLeaveHistoryActivity extends AppCompatActivity {

    public TextView titleTxt;
    public RecyclerView shortLeaveHistoryRecycler;
    public ShortLeaveHistoryAdapter adapter;
    public ArrayList<ShortLeaveHistoryModel> list = new ArrayList<>();
    public String userId = "", authCode = "";
    public ConnectionDetector conn;
    public String shortLeaveUrl = SettingConstant.BaseUrl + "AppEmployeeShortLeaveList";

    public Spinner monthSpinner, yearSpinner;
    public ArrayList<String> yearList = new ArrayList<>();
    public ArrayList<MonthModel> monthList = new ArrayList<>();
    public ArrayAdapter<MonthModel> monthAdapter;
    public ArrayAdapter<String> yearAdapter;
    public ImageView serchBtn;
    public String  yearString ="", empId = "";
    public int monthString;
    public TextView noRecordFoundTxt;
    public int cmonth,cyear,rearYear;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_short_leave_history);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.mgrtoolbar);
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

        titleTxt.setText("Short Leave History");

        Intent intent = getIntent();
        if (intent != null)
        {
            empId = intent.getStringExtra("empId");
        }

        shortLeaveHistoryRecycler = (RecyclerView)findViewById(R.id.short_leave_history_recycler);
        monthSpinner = (Spinner)findViewById(R.id.monthspinner);
        yearSpinner = (Spinner)findViewById(R.id.yearspinner);
        serchBtn = (ImageView)findViewById(R.id.serchresult);
        noRecordFoundTxt = (TextView)findViewById(R.id.norecordfound);

        conn = new ConnectionDetector(ManagerShortLeaveHistoryActivity.this);

        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerShortLeaveHistoryActivity.this)));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerShortLeaveHistoryActivity.this)));

        adapter = new ShortLeaveHistoryAdapter(ManagerShortLeaveHistoryActivity.this,list,ManagerShortLeaveHistoryActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManagerShortLeaveHistoryActivity.this);
        shortLeaveHistoryRecycler.setLayoutManager(mLayoutManager);
        shortLeaveHistoryRecycler.setItemAnimator(new DefaultItemAnimator());
        shortLeaveHistoryRecycler.setAdapter(adapter);

        shortLeaveHistoryRecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);

        //current month and year
        Calendar c= Calendar.getInstance();
        cyear = c.get(Calendar.YEAR);//calender year starts from 1900 so you must add 1900 to the value recevie.i.e., 1990+112 = 2012
        cmonth = c.get(Calendar.MONTH);//this is april so you will receive  3 instead of 4.
        rearYear = cyear-2;

        Log.e("current Year", rearYear + "");
        Log.e("current Month", cmonth + "");

        //Month spinner work
        if (monthList.size()>0)
        {
            monthList.clear();
        }

        monthList.add(new MonthModel(0,"All"));
        monthList.add(new MonthModel(1,"Jan"));
        monthList.add(new MonthModel(2,"Feb"));
        monthList.add(new MonthModel(3,"Mar"));
        monthList.add(new MonthModel(4,"Apr"));
        monthList.add(new MonthModel(5,"May"));
        monthList.add(new MonthModel(6,"Jun"));
        monthList.add(new MonthModel(7,"July"));
        monthList.add(new MonthModel(8,"Aug"));
        monthList.add(new MonthModel(9,"Sep"));
        monthList.add(new MonthModel(10,"Oct"));
        monthList.add(new MonthModel(11,"Nov"));
        monthList.add(new MonthModel(12,"Dec"));

        monthSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        monthAdapter = new ArrayAdapter<MonthModel>(ManagerShortLeaveHistoryActivity.this, R.layout.customizespinner,
                monthList);
        monthAdapter.setDropDownViewResource(R.layout.customizespinner);
        monthSpinner.setAdapter(monthAdapter);

        //select the current Month First Time
        for (int i=0; i<monthList.size(); i++)
        {
            if (cmonth+1 == monthList.get(i).getMonthId())
            {
                monthSpinner.setSelection(i);
            }
        }

        //year Spinner Work
        if (yearList.size()>0)
        {
            yearList.clear();
        }

        yearList.add(cyear+ "");
        yearList.add(cyear-1 + "");
        yearList.add(cyear-2 + "");

        yearSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        yearAdapter = new ArrayAdapter<String>(ManagerShortLeaveHistoryActivity.this, R.layout.customizespinner,
                yearList);
        yearAdapter.setDropDownViewResource(R.layout.customizespinner);
        yearSpinner.setAdapter(yearAdapter);



        //selected spinner Data then call API
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                monthString = monthList.get(i).getMonthId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                yearString = yearList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //search Result
        serchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shortLeaveHistoryList(authCode, userId, empId,monthString + "", yearString);
            }
        });


        //set list
        if (conn.getConnectivityStatus()>0)
        {
            shortLeaveHistoryList(authCode, userId, empId,cmonth+1 +"", cyear + "");
        }else
            {
                conn.showNoInternetAlret();
            }


    }

    //Short Leave History List
    public void shortLeaveHistoryList(final String AuthCode , final String AdminID, final String EmployeeID ,
                                      final String Month, final String year) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerShortLeaveHistoryActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, shortLeaveUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    if (list.size()>0)
                    {
                        list.clear();
                    }
                    for (int i=0 ; i<jsonArray.length();i++)
                    {
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
                            String LeaveApplication_Id = jsonObject.getString("LeaveApplication_Id");
                            String LeaveTypeName = jsonObject.getString("LeaveTypeName");
                            String StartDate = jsonObject.getString("StartDateText");
                            String TimeFrom = jsonObject.getString("TimeFrom");
                            String TimeTo = jsonObject.getString("TimeTo");
                            String AppliedDate = jsonObject.getString("AppliedDate");
                            String StatusText = jsonObject.getString("StatusText");
                            String CommentText = jsonObject.getString("CommentText");
                            //  String IsDeleteable = jsonObject.getString("IsDeleteable");
                            String UserName = jsonObject.getString("UserNameWithComp");

                            list.add(new ShortLeaveHistoryModel(UserName,LeaveApplication_Id,LeaveTypeName,StartDate,TimeFrom,TimeTo,AppliedDate,
                                    StatusText,CommentText,"0"));
                        }

                    }

                    if (list.size() == 0)
                    {
                        noRecordFoundTxt.setVisibility(View.VISIBLE);
                        shortLeaveHistoryRecycler.setVisibility(View.GONE);
                    }else
                    {
                        noRecordFoundTxt.setVisibility(View.GONE);
                        shortLeaveHistoryRecycler.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();
                    pDialog.dismiss();

                } catch (JSONException e) {

                    Log.e("checking json excption" , e.getMessage());
                    e.printStackTrace();

                }catch (StringIndexOutOfBoundsException ex)
                {
                    Toast.makeText(ManagerShortLeaveHistoryActivity.this,"Error in request processing", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    //  Log.e("checking exception", ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                Toast.makeText(ManagerShortLeaveHistoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("LoginAdminID",AdminID);
                params.put("EmployeeID",EmployeeID);
                params.put("Month",Month);
                params.put("Year",year);


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
        startActivity(new Intent(ManagerShortLeaveHistoryActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ManagerShortLeaveHistoryActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ManagerShortLeaveHistoryActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ManagerShortLeaveHistoryActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ManagerShortLeaveHistoryActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ManagerShortLeaveHistoryActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ManagerShortLeaveHistoryActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ManagerShortLeaveHistoryActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ManagerShortLeaveHistoryActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ManagerShortLeaveHistoryActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
