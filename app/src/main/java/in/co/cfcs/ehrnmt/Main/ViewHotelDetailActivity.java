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
import android.widget.Button;
import android.widget.LinearLayout;
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

public class ViewHotelDetailActivity extends AppCompatActivity {

    public TextView titleTxt,employeNameTxt,hotelNameTxt,cityNameTxt,statusTxt,requestDateTxt,approvalDateTxt,hrCommTxt,empCommTxt,
                    checkInDateTxt,checkInTimeTxt, checkOutDateTxt, hotelTypeTxt;
    public String hotelDetailsUrl = SettingConstant.BaseUrl + "AppEmployeeHotelBookingDetail";
    public String bidString = "",authcode = "", userId = "",HotelType = "",CityName = "",HotelName = "",CheckInDateText = ""
            ,CheckInTime = "",CheckOutDateText = "",EmpComment = "",BIDStr = "";
    public ConnectionDetector conn;
    public Button editBtn;
    public LinearLayout followLay, hrCommentLay;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hotel_detail);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewhoteltoolbar);
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

        titleTxt.setText("Hotel Details");

        Intent intent = getIntent();
        if (intent != null)
        {
            bidString = intent.getStringExtra("BID");
        }

        conn = new ConnectionDetector(ViewHotelDetailActivity.this);
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ViewHotelDetailActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ViewHotelDetailActivity.this)));

        employeNameTxt = (TextView) findViewById(R.id.hotel_empname);
        hotelNameTxt = (TextView) findViewById(R.id.hotel_name);
        cityNameTxt = (TextView) findViewById(R.id.hotel_cityname);
        requestDateTxt = (TextView) findViewById(R.id.hotel_request_date);
        approvalDateTxt = (TextView) findViewById(R.id.hotel_approvaldate);
        statusTxt = (TextView) findViewById(R.id.hotel_status);
        checkInDateTxt = (TextView) findViewById(R.id.hotel_checkindate);
        checkInTimeTxt = (TextView) findViewById(R.id.hotel_checkintime);
        checkOutDateTxt = (TextView) findViewById(R.id.hotel_checkoutdate);
        empCommTxt = (TextView) findViewById(R.id.hotel_employcomment);
        hrCommTxt = (TextView) findViewById(R.id.hotel_hr_comment);
        editBtn = (Button) findViewById(R.id.edithotel);
        hotelTypeTxt = (TextView) findViewById(R.id.hotel_type);
        followLay = (LinearLayout) findViewById(R.id.followudateLay);
        hrCommentLay = (LinearLayout) findViewById(R.id.hrcommentLay);


       editBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent i = new Intent(ViewHotelDetailActivity.this, AddHotelActivity.class);
               i.putExtra("Mode", "Edit");
               i.putExtra("Hotel type",HotelType);
               i.putExtra("Booking City",CityName);
               i.putExtra("Guest House",HotelName);
               i.putExtra("Check In Date",CheckInDateText);
               i.putExtra("Check In Time",CheckInTime);
               i.putExtra("Check Out Time",CheckOutDateText);
               i.putExtra("Remark",EmpComment);
               i.putExtra("BID",BIDStr);

               startActivity(i);
               overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
           }
       });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (conn.getConnectivityStatus()>0)
        {
            viewHotelDetails(authcode,bidString, userId);

        }else
        {
            conn.showNoInternetAlret();
        }
    }

    //View Hotel Details
    public void viewHotelDetails(final String AuthCode , final String BID, final String userId) {

        final ProgressDialog pDialog = new ProgressDialog(ViewHotelDetailActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, hotelDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    for (int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.has("status")) {
                            LoginStatus = object.getString("status");
                            msgstatus = object.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                            }
                        }else {
                            String EmpName = object.getString("EmpName");
                            String requestDate = object.getString("AddDateText");
                            String approvedBy = object.getString("AppDateText");
                            String HrComment = object.getString("HrComment");
                            String AppStatusText = object.getString("AppStatusText");
                            CityName = object.getString("CityName");
                            CheckInDateText = object.getString("CheckInDateText");
                            EmpComment = object.getString("EmpRemark");
                            CheckOutDateText = object.getString("CheckOutDateText");
                            CheckInTime = object.getString("CheckInTime");
                            HotelName = object.getString("HotelName");
                            HotelType = object.getString("HotelTypeText");
                            BIDStr = object.getString("BID");

                            employeNameTxt.setText(EmpName);
                            empCommTxt.setText(EmpComment);
                            requestDateTxt.setText(requestDate);
                            approvalDateTxt.setText(approvedBy);
                            hrCommTxt.setText(HrComment);
                            statusTxt.setText(AppStatusText);
                            cityNameTxt.setText(CityName);
                            checkInTimeTxt.setText(CheckInTime);
                            checkInDateTxt.setText(CheckInDateText);
                            checkOutDateTxt.setText(CheckOutDateText);
                            hotelNameTxt.setText(HotelName);
                            hotelTypeTxt.setText(HotelType);

                            if (approvedBy.equalsIgnoreCase("") || approvedBy.equalsIgnoreCase("null"))
                            {
                                followLay.setVisibility(View.GONE);
                            }else
                            {
                                followLay.setVisibility(View.VISIBLE);
                            }

                            if (HrComment.equalsIgnoreCase("") || HrComment.equalsIgnoreCase("null"))
                            {
                                hrCommentLay.setVisibility(View.GONE);
                            }else
                            {
                                hrCommentLay.setVisibility(View.VISIBLE);
                            }

                        /*if (HrComment.equalsIgnoreCase("") ||  HrComment.equalsIgnoreCase("null") )
                        {
                            hrCommTxt.setVisibility(View.GONE);
                            hrcommentFontTxt.setVisibility(View.GONE);

                        }else if (approvedBy.equalsIgnoreCase("") || approvedBy.equalsIgnoreCase("null"))
                        {
                            approvalDateTxt.setVisibility(View.GONE);
                            followDateTxt.setVisibility(View.GONE);
                        }*/



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

                Toast.makeText(ViewHotelDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("BID",BID);
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
        startActivity(new Intent(ViewHotelDetailActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ViewHotelDetailActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ViewHotelDetailActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ViewHotelDetailActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ViewHotelDetailActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ViewHotelDetailActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ViewHotelDetailActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ViewHotelDetailActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ViewHotelDetailActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ViewHotelDetailActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
