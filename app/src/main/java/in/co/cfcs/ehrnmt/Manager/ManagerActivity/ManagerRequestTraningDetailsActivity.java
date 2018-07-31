package in.co.cfcs.ehrnmt.Manager.ManagerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import in.co.cfcs.ehrnmt.Main.ViewRequestDetailsActivity;
import in.co.cfcs.ehrnmt.Main.ViewShortLeaveHistoryActivity;
import in.co.cfcs.ehrnmt.Model.BookMeaPrevisionModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerRequestTraningDetailsActivity extends AppCompatActivity {

    public TextView titleTxt, empNameTxt, empIdTxt, zoneNameTxt, domainNameTxt, courseNameTxt, startDateTxt, endDateTxt, venueTxt, durationTxt
            , descTxt, empCommTxt, mgrCommTxt, profyTxt, statusTxt;
    public String applicationId = "", authcode = "", userid = "", chckNavigate;
    public String viewDetailsUrl = SettingConstant.BaseUrl + "AppManagerTrainingRequestDetail";
    public ConnectionDetector conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_request_traning_details);

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



        Intent intent = getIntent();
        if (intent != null)
        {
            applicationId = intent.getStringExtra("LeaveApplication_Id");
            chckNavigate = intent.getStringExtra("CheckingNavigate");
        }

        if (chckNavigate.equalsIgnoreCase("1"))
        {
            titleTxt.setText("Training Request Details");
        }else
            {
                titleTxt.setText("Training Proceed Details");
            }

        conn = new ConnectionDetector(ManagerRequestTraningDetailsActivity.this);
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerRequestTraningDetailsActivity.this)));
        userid = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerRequestTraningDetailsActivity.this)));


        empNameTxt = (TextView) findViewById(R.id.req_empname);
        empIdTxt = (TextView) findViewById(R.id.req_empid);
        zoneNameTxt = (TextView) findViewById(R.id.req_zonename);
        domainNameTxt = (TextView) findViewById(R.id.req_domainname);
        courseNameTxt = (TextView) findViewById(R.id.req_coursename);
        startDateTxt = (TextView) findViewById(R.id.req_startdate);
        endDateTxt = (TextView) findViewById(R.id.req_enddate);
        venueTxt = (TextView) findViewById(R.id.req_veneue);
        durationTxt = (TextView) findViewById(R.id.req_duration);
        descTxt = (TextView) findViewById(R.id.req_desc);
        empCommTxt = (TextView) findViewById(R.id.req_empcomment);
        mgrCommTxt = (TextView) findViewById(R.id.req_mgrcomm);
        profyTxt = (TextView) findViewById(R.id.rew_proname);
        statusTxt = (TextView) findViewById(R.id.req_status);

        if (conn.getConnectivityStatus()>0)
        {
            viewTraningRequestDeatils(authcode,userid,applicationId);

        }else
            {
                conn.showNoInternetAlret();
            }


    }

    //View Details of Traning Request
    public void viewTraningRequestDeatils(final String AuthCode , final String userId, final String ApplicationID) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerRequestTraningDetailsActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, viewDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));


                    for (int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String DomainName = object.getString("DomainName");
                        String CourseName = object.getString("CourseName");
                        String StartDate = object.getString("StartDate");
                        String EndDate = object.getString("EndDate");
                        String Venue = object.getString("Venue");
                        String Duration = object.getString("Duration");
                        String DescriptionText = object.getString("DescriptionText");
                        String EmployeeComment = object.getString("EmployeeComment");
                        String ProficiencyName = object.getString("ProficiencyName");
                        String EmployeeName = object.getString("EmployeeName");
                        String EmpID = object.getString("EmpID");
                        String ZoneName = object.getString("ZoneName");
                        String StatusText = object.getString("StatusText");
                        String CommentManager = object.getString("CommentManager");


                        domainNameTxt.setText(DomainName);
                        empNameTxt.setText(EmployeeName);
                        courseNameTxt.setText(CourseName);
                        startDateTxt.setText(StartDate);
                        endDateTxt.setText(EndDate);
                        statusTxt.setText(StatusText);
                        venueTxt.setText(Venue);
                        durationTxt.setText(Duration);
                        descTxt.setText(DescriptionText);
                        empCommTxt.setText(EmployeeComment);
                        profyTxt.setText(ProficiencyName);
                        empIdTxt.setText(EmpID);
                        zoneNameTxt.setText(ZoneName);
                        mgrCommTxt.setText(CommentManager);

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

                Toast.makeText(ManagerRequestTraningDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("AdminID",userId);
                params.put("ApplicationID",ApplicationID);


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


