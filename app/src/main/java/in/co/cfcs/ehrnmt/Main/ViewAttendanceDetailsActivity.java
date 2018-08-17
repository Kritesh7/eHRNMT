package in.co.cfcs.ehrnmt.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

public class ViewAttendanceDetailsActivity extends AppCompatActivity {

    public TextView titleTxt, empNmaeTxt, inTimeDateTxt, empIdTxt, inTimeTxt, outTimeTxt, durationTxt, halfDayTxt,
            lateArivalTxt, earlyLeavingTxt, statusTxt;
    public Button updateBtn;
    public String viewDetailsUrl = SettingConstant.BaseUrl + "AppEmployeeAttendanceDetail";
    public String updateRequestUrl = SettingConstant.BaseUrl + "AppEmployeeAttendanceUpdateRequest";
    public ConnectionDetector conn;
    public PopupWindow popupWindow;
    public String authcode = "", isRequest = "", attendaceLogId = "", userId = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance_details);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.viewleavtoolbar);
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

        titleTxt.setText("Attendance Details");

        Intent intent = getIntent();
        if (intent != null) {

            attendaceLogId = intent.getStringExtra("AttendnaceLogId");
            // isRequest = intent.getStringExtra("Visibile");


        }

        conn = new ConnectionDetector(ViewAttendanceDetailsActivity.this);
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ViewAttendanceDetailsActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ViewAttendanceDetailsActivity.this)));
        //find widget
        updateBtn = (Button) findViewById(R.id.updatedetail);
        empNmaeTxt = (TextView) findViewById(R.id.empname);
        inTimeDateTxt = (TextView) findViewById(R.id.intimedate);
        // outTimeEditTxt = (TextView)findViewById(R.id.outtimedate);
        inTimeTxt = (TextView) findViewById(R.id.intime);
        outTimeTxt = (TextView) findViewById(R.id.outtime);
        durationTxt = (TextView) findViewById(R.id.duration);
        halfDayTxt = (TextView) findViewById(R.id.halfday);
        lateArivalTxt = (TextView) findViewById(R.id.latearival);
        earlyLeavingTxt = (TextView) findViewById(R.id.earlylearning);
        statusTxt = (TextView) findViewById(R.id.status);
        empIdTxt = (TextView) findViewById(R.id.empid);


        if (conn.getConnectivityStatus() > 0) {
            viewDetails(authcode, userId, attendaceLogId);
        } else {
            conn.showNoInternetAlret();
        }


        //click on button and update request
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (conn.getConnectivityStatus() > 0) {

                    setPopupWindow(authcode, userId, attendaceLogId);

                } else {
                    conn.showNoInternetAlret();
                }
            }
        });


    }

    private void setPopupWindow(final String authCode, final String userId, final String leaveId) {


        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_layout, null);
        Button cancel, backBtn;
        final EditText remarkTxt;
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.animationName);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        //

        cancel = (Button) popupView.findViewById(R.id.cancel_leaverequest);
        remarkTxt = (EditText) popupView.findViewById(R.id.remarktxt);
        backBtn = (Button) popupView.findViewById(R.id.backbtn);

        cancel.setText("Update");
        //  backBtn.setText("Update Request");


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (remarkTxt.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ViewAttendanceDetailsActivity.this, "Please fill Remark", Toast.LENGTH_SHORT).show();
                } else {
                    updateRequest(authCode, userId, leaveId, remarkTxt.getText().toString());
                }


            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();

            }
        });

    }

    //update Request
    public void updateRequest(final String AuthCode, final String AdminID, final String LeaveApplicationID, final String Remark) {


        final ProgressDialog pDialog = new ProgressDialog(ViewAttendanceDetailsActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, updateRequestUrl, new Response.Listener<String>() {
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
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                            popupWindow.dismiss();
                        }
                    }
//                    if (jsonObject.has("status")) {
//                        String status = jsonObject.getString("status");
//
//                        if (status.equalsIgnoreCase("success")) {
//
//                            String MsgNotification = jsonObject.getString("MsgNotification");
//
//                            Toast.makeText(ViewAttendanceDetailsActivity.this, MsgNotification, Toast.LENGTH_SHORT).show();
//                            popupWindow.dismiss();
//                        }
//                    }
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
                params.put("AdminID", AdminID);
                params.put("AttendanceLogID", LeaveApplicationID);
                params.put("Remark", Remark);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }


    //View Attendance Details
    public void viewDetails(final String AuthCode, final String AdminID, final String LeaveApplicationID) {


        final ProgressDialog pDialog = new ProgressDialog(ViewAttendanceDetailsActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, viewDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // String status = jsonObject.getString("status");

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
                            String Name = jsonObject.getString("Name");
                            String AttendanceDateText = jsonObject.getString("AttendanceDateText");
                            String InTime = jsonObject.getString("InTime");
                            String OutTime = jsonObject.getString("OutTime");
                            String InOutDuration = jsonObject.getString("InOutDuration");
                            String Halfday = jsonObject.getString("Halfday");
                            String LateArrival = jsonObject.getString("LateArrival");
                            String EarlyLeaving = jsonObject.getString("EarlyLeaving");
                            String Status = jsonObject.getString("Status");
                            String isRequest = jsonObject.getString("IsRequest");
                            String EmpID = jsonObject.getString("EmpID");


                            empNmaeTxt.setText(Name);
                            inTimeDateTxt.setText(AttendanceDateText);
                            inTimeTxt.setText(InTime);
                            outTimeTxt.setText(OutTime);
                            durationTxt.setText(InOutDuration);
                            statusTxt.setText(Status);
                            halfDayTxt.setText(Halfday);
                            lateArivalTxt.setText(LateArrival);
                            earlyLeavingTxt.setText(EarlyLeaving);
                            empIdTxt.setText(EmpID);

                            //hide button
                            if (isRequest.equalsIgnoreCase("0")) {
                                updateBtn.setVisibility(View.GONE);
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
                params.put("AdminID", AdminID);
                params.put("AttendanceLogID", LeaveApplicationID);


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
        startActivity(new Intent(ViewAttendanceDetailsActivity.this, LoginActivity.class));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ViewAttendanceDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ViewAttendanceDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ViewAttendanceDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ViewAttendanceDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ViewAttendanceDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ViewAttendanceDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ViewAttendanceDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ViewAttendanceDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ViewAttendanceDetailsActivity.this,
                "")));
    }

}
