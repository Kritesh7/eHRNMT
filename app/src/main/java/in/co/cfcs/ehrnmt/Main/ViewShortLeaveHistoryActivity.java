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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
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

public class ViewShortLeaveHistoryActivity extends AppCompatActivity {

    public TextView titleTxt, leaveTypeTxt, empNameTxt,empIdTxt, timeFromTxt, timeToTxt, appliedDateTxt,noOfHoursTxt,statusTxt,
                    empCommentTxt, managerNameTxt, managerApprovedDateTxt, managerCommentTxt, hrApprovedDateTxt, hrCommentTxt
            ,shortLeaveDateTxt, cancelationRemarkByEmp, cancelationRemarkByMng, cancelationRemarkByHr, text1, text2, text3;
    public ConnectionDetector conn;
    public String LeaveApplication_Id = "",authCode = "", userId = "";
    public String viewDetailsUrl = SettingConstant.BaseUrl + "AppEmployeeShortLeaveDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_short_leave_history);

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

        titleTxt.setText("Short Leave Details");

        Intent intent = getIntent();
        if (intent != null)
        {
            LeaveApplication_Id = intent.getStringExtra("LeaveApplication_Id");
        }

        conn = new ConnectionDetector(ViewShortLeaveHistoryActivity.this);
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ViewShortLeaveHistoryActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ViewShortLeaveHistoryActivity.this)));

        leaveTypeTxt = (TextView)findViewById(R.id.short_leavetype);
        empNameTxt = (TextView)findViewById(R.id.short_emplyname);
        empIdTxt = (TextView)findViewById(R.id.short_emp_id);
        timeFromTxt = (TextView)findViewById(R.id.short_timefrom);
        timeToTxt = (TextView)findViewById(R.id.short_timeto);
        appliedDateTxt = (TextView)findViewById(R.id.short_applieddate);
        noOfHoursTxt = (TextView)findViewById(R.id.short_no_of_hour);
        statusTxt = (TextView)findViewById(R.id.short_status);
        empCommentTxt = (TextView)findViewById(R.id.short_employcomment);
        managerNameTxt = (TextView)findViewById(R.id.short_ManagerName);
        managerApprovedDateTxt = (TextView)findViewById(R.id.short_manager_approved_date);
        managerCommentTxt = (TextView)findViewById(R.id.short_managercomment);
        hrApprovedDateTxt = (TextView)findViewById(R.id.short_hrapproved_date);
        hrCommentTxt = (TextView)findViewById(R.id.short_hr_comment);
        shortLeaveDateTxt = (TextView) findViewById(R.id.short_leavedate);
        cancelationRemarkByEmp = (TextView) findViewById(R.id.short_cancelationremarkbyname);
        cancelationRemarkByMng = (TextView) findViewById(R.id.short_cancelationremarkbymanager);
        cancelationRemarkByHr = (TextView) findViewById(R.id.short_cancelationremarkbyhr);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);


        if (conn.getConnectivityStatus()>0)
        {
            viewDetails(authCode,LeaveApplication_Id,userId);
        }else
        {
            conn.showNoInternetAlret();
        }
    }

    //view Details Api
    public void viewDetails(final String AuthCode , final String LeaveApplicationID, final String userId ) {


        final ProgressDialog pDialog = new ProgressDialog(ViewShortLeaveHistoryActivity.this,R.style.AppCompatAlertDialogStyle);
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
                        if (jsonObject.has("MsgNotification"))
                        {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            Toast.makeText(ViewShortLeaveHistoryActivity.this, MsgNotification, Toast.LENGTH_SHORT).show();

                        }

                        String LeaveTypeName = jsonObject.getString("LeaveTypeName");
                        String FullName = jsonObject.getString("FullName");
                        String EmpID = jsonObject.getString("EmpID");
                        String TimeFrom = jsonObject.getString("TimeFrom");
                        String TimeTo = jsonObject.getString("TimeTo");
                        String AppliedDateText = jsonObject.getString("AppliedDateText");
                        String StatusText = jsonObject.getString("StatusText");
                        String Comment = jsonObject.getString("Comment");
                        String TotalMinute = jsonObject.getString("TotalMinute");
                        String FullNameManager = jsonObject.getString("FullNameManager");
                        String ManagerDateText = jsonObject.getString("ManagerDateText");
                        String ManagerComment = jsonObject.getString("ManagerComment");
                        String HRDateText = jsonObject.getString("HRDateText");
                        String HRComment = jsonObject.getString("HRComment");
                        String leaveDate = jsonObject.getString("StartDateText");
                        String Remark = jsonObject.getString("Remark");
                        String ManagerRemark = jsonObject.getString("ManagerRemark");
                        String HRRemark = jsonObject.getString("HRRemark");

                        int min = Integer.parseInt(TotalMinute);

                        int hours = min / 60; //since both are ints, you get an int
                        int minutes = min % 60;

                        if (Remark.equalsIgnoreCase("null") || Remark.equalsIgnoreCase(""))
                        {
                            cancelationRemarkByEmp.setVisibility(View.GONE);
                            text1.setVisibility(View.GONE);
                        }else
                            {
                                cancelationRemarkByEmp.setVisibility(View.VISIBLE);
                                text1.setVisibility(View.VISIBLE);
                                cancelationRemarkByEmp.setText(Remark);
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
                        empNameTxt.setText(FullName);
                        empIdTxt.setText(EmpID);
                        timeFromTxt.setText(TimeFrom);
                        timeToTxt.setText(TimeTo);
                        statusTxt.setText(StatusText);
                        appliedDateTxt.setText(AppliedDateText);
                        managerApprovedDateTxt.setText(ManagerDateText);
                        hrCommentTxt.setText(HRComment);
                        hrApprovedDateTxt.setText(HRDateText);
                        empCommentTxt.setText(Comment);
                        noOfHoursTxt.setText(hours + " h " + minutes + " m" );
                        managerNameTxt.setText(FullNameManager);
                        managerCommentTxt.setText(ManagerComment);
                        shortLeaveDateTxt.setText(leaveDate);


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

                Toast.makeText(ViewShortLeaveHistoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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


}
