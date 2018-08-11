package in.co.cfcs.ehrnmt.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ViewLeavemangementActivity extends AppCompatActivity {

    public TextView titleTxt;
    public String LeaveApplication_Id = "",authCode = "";
    public TextView leaveTypeTxt, startDateTxt,endDateTxt,numberofDaysTxt, appliedOnTxt, statusTxt,commentByMangerTxt, managerCommentedOn,
                    commentByHrTxt, hrCommentedOnTxt, empRemarkTxt, cancelationRemarkByEmp, cancelationRemarkByMng, cancelationRemarkByHr, text1, text2, text3;
    public String viewDetailsUrl = SettingConstant.BaseUrl + "AppEmployeeLeaveDetail";
    public ConnectionDetector conn;
    public String userId = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_leavemangement);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewleavtoolbar);
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

        titleTxt.setText("Leave Details");

        Intent intent = getIntent();
        if (intent != null)
        {
            LeaveApplication_Id = intent.getStringExtra("LeaveApplication_Id");
        }


        conn = new ConnectionDetector(ViewLeavemangementActivity.this);
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ViewLeavemangementActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ViewLeavemangementActivity.this)));
        //find id
        leaveTypeTxt = (TextView)findViewById(R.id.leavetype);
        startDateTxt = (TextView)findViewById(R.id.startdate);
        endDateTxt = (TextView)findViewById(R.id.enddate);
        numberofDaysTxt = (TextView)findViewById(R.id.numberofdays);
        appliedOnTxt = (TextView)findViewById(R.id.appliedon);
        statusTxt = (TextView)findViewById(R.id.status);
        commentByMangerTxt = (TextView)findViewById(R.id.commentbymanger);
        managerCommentedOn = (TextView)findViewById(R.id.managercommentedon);
        commentByHrTxt = (TextView)findViewById(R.id.commentedbyhr);
        hrCommentedOnTxt = (TextView)findViewById(R.id.hrcommentedon);
        empRemarkTxt = (TextView) findViewById(R.id.empremark);
        cancelationRemarkByEmp = (TextView) findViewById(R.id.short_cancelationremarkbyname);
        cancelationRemarkByMng = (TextView) findViewById(R.id.short_cancelationremarkbymanager);
        cancelationRemarkByHr = (TextView) findViewById(R.id.short_cancelationremarkbyhr);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);

        if (conn.getConnectivityStatus()>0)
        {
            viewDetails(authCode,LeaveApplication_Id, userId);
        }
        else
            {
                conn.showNoInternetAlret();
            }
    }

    //view Details Api
    public void viewDetails(final String AuthCode , final String LeaveApplicationID, final String userId ) {


        final ProgressDialog pDialog = new ProgressDialog(ViewLeavemangementActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, viewDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // String status = jsonObject.getString("status");
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

                            String LeaveTypeName = jsonObject.getString("LeaveTypeName");
                            String StartDateText = jsonObject.getString("StartDateText");
                            String EndDateText = jsonObject.getString("EndDateText");
                            String Noofdays = jsonObject.getString("Noofdays");
                            String AppliedDate = jsonObject.getString("AppliedDate");
                            String StatusText = jsonObject.getString("StatusText");
                            String ManagerComment = jsonObject.getString("ManagerComment");
                            String ManagerDateText = jsonObject.getString("ManagerDateText");
                            String HRComment = jsonObject.getString("HRComment");
                            String HRDateText = jsonObject.getString("HRDateText");
                            String Remark = jsonObject.getString("Comments");
                            String EmpRemark = jsonObject.getString("Remark");
                            String ManagerRemark = jsonObject.getString("ManagerRemark");
                            String HRRemark = jsonObject.getString("HRRemark");


                            if (EmpRemark.equalsIgnoreCase("null") || EmpRemark.equalsIgnoreCase(""))
                            {
                                cancelationRemarkByEmp.setVisibility(View.GONE);
                                text1.setVisibility(View.GONE);
                            }else
                            {
                                cancelationRemarkByEmp.setVisibility(View.VISIBLE);
                                text1.setVisibility(View.VISIBLE);
                                cancelationRemarkByEmp.setText(EmpRemark);
                            }


                            if (ManagerRemark.equalsIgnoreCase("null") || ManagerRemark.equalsIgnoreCase(""))
                            {
                                cancelationRemarkByMng.setVisibility(View.GONE);
                                text2.setVisibility(View.GONE);
                            }else
                            {
                                cancelationRemarkByMng.setVisibility(View.VISIBLE);
                                text2.setVisibility(View.VISIBLE);
                                cancelationRemarkByMng.setText(ManagerRemark);

                            }
                            if (HRRemark.equalsIgnoreCase("null") || HRRemark.equalsIgnoreCase(""))
                            {
                                cancelationRemarkByHr.setVisibility(View.GONE);
                                text3.setVisibility(View.GONE);
                            }else
                            {
                                cancelationRemarkByHr.setVisibility(View.VISIBLE);
                                text3.setVisibility(View.VISIBLE);
                                cancelationRemarkByHr.setText(HRRemark);
                            }




                            leaveTypeTxt.setText(LeaveTypeName);
                            startDateTxt.setText(StartDateText);
                            endDateTxt.setText(EndDateText);
                            numberofDaysTxt.setText(Noofdays);
                            appliedOnTxt.setText(AppliedDate);
                            statusTxt.setText(StatusText);
                            commentByMangerTxt.setText(ManagerComment);
                            managerCommentedOn.setText(ManagerDateText);
                            commentByHrTxt.setText(HRComment);
                            hrCommentedOnTxt.setText(HRDateText);
                            empRemarkTxt.setText(Remark);


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

                params.put("AuthCode",AuthCode);
                params.put("LeaveApplicationID",LeaveApplicationID);
                params.put("AdminID",userId);


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
        startActivity(new Intent(ViewLeavemangementActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ViewLeavemangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ViewLeavemangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ViewLeavemangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ViewLeavemangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ViewLeavemangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ViewLeavemangementActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ViewLeavemangementActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ViewLeavemangementActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ViewLeavemangementActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }


}
