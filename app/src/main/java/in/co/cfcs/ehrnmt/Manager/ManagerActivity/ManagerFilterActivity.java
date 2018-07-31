package in.co.cfcs.ehrnmt.Manager.ManagerActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Manager.ManagerModel.ManagerEmployeeSpinnerModel;
import in.co.cfcs.ehrnmt.Manager.ManagerModel.TeamFilterModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerFilterActivity extends AppCompatActivity {

    public TextView titleTxt;
    public Spinner teamSpinner, empSpinner;
    public ArrayList<TeamFilterModel> teamList = new ArrayList<>();
    public ArrayList<ManagerEmployeeSpinnerModel> empList = new ArrayList<>();
    public ArrayAdapter<TeamFilterModel> teamAdapter;
    public ArrayAdapter<ManagerEmployeeSpinnerModel> empAdapter;
    public String teamDdlBindTxt = SettingConstant.BaseUrl + "AppManagerTeamNameList";
    public String empDataBind = SettingConstant.BaseUrl + "AppManagerTeamList";
    public ConnectionDetector conn;
    public String authCode = "", userId = "", checkingActivity = "", empId = "" ;
    public Button showBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_filter);

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
            checkingActivity = intent.getStringExtra("CheckingTheActivity");
        }

        titleTxt.setText(checkingActivity);

        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerFilterActivity.this)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerFilterActivity.this)));



        conn = new ConnectionDetector(ManagerFilterActivity.this);

        teamSpinner = (Spinner) findViewById(R.id.teamspinner);
        empSpinner = (Spinner) findViewById(R.id.employeespinner);
        showBtn = (Button) findViewById(R.id.showbtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        //Team Spinner
        teamSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);
        teamAdapter = new ArrayAdapter<TeamFilterModel>(ManagerFilterActivity.this, R.layout.customizespinner,
                teamList);
        teamAdapter.setDropDownViewResource(R.layout.customizespinner);
        teamSpinner.setAdapter(teamAdapter);

        //Employee Spinner
        empSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);
        empAdapter = new ArrayAdapter<ManagerEmployeeSpinnerModel>(ManagerFilterActivity.this, R.layout.customizespinner,
                empList);
        empAdapter.setDropDownViewResource(R.layout.customizespinner);
        empSpinner.setAdapter(empAdapter);


        // bind the data of emp list spinner
        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                employeeData(authCode,userId,teamList.get(i).getAdminId(), i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // get emp id
        empSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                empId = empList.get(i).getEmpId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (conn.getConnectivityStatus()>0)
        {
            teamData(authCode, userId);
        }else
        {
            conn.showNoInternetAlret();
        }

        // cancel activity

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkingActivity.equalsIgnoreCase("Asset Details")) {

                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerAssetDetailsActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

                }else if (checkingActivity.equalsIgnoreCase("Leave Summery"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerLeaveSummeryActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

                }else if(checkingActivity.equalsIgnoreCase("Weak Off"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerWeakOffActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

                }else if (checkingActivity.equalsIgnoreCase("Leave History"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerTeamLeaveHistoryActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

                }else if (checkingActivity.equalsIgnoreCase("Short Leave History"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerShortLeaveHistoryActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Attendance Report"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerAttendanceReportActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Team Average Report"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerTeamAvearageReportActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Administrative Information"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerOfficealyDataActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else  if (checkingActivity.equalsIgnoreCase("Medical And Insurance"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerMedicalActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Address And Contact"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerAddressContactActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Emergency Contact Address"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerEmergencyAddressActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Personal Information"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, PersonalDeatilsActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Skills Details"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerSkillsActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Language Details"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerLangaugeActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Previous Experience Details"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerPreviousExprinceActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Education Details"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerEducationDetailsActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else if (checkingActivity.equalsIgnoreCase("Attendance Basic Log"))
                {
                    Intent ik = new Intent(ManagerFilterActivity.this, ManagerAttendanceLogDetailsActivity.class);
                    ik.putExtra("empId", empId);
                    startActivity(ik);
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }
            }
        });

    }

    //bind Team spinner
    public void teamData(final String AuthCode, final String userId) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerFilterActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, teamDdlBindTxt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Team data", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    //bind material List
                    if (teamList.size()>0)
                    {
                        teamList.clear();
                    }
                    for (int i =0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String AdminID = object.getString("AdminID");
                        String ManagerName = object.getString("ManagerName");

                        teamList.add(new TeamFilterModel(AdminID,ManagerName));


                    }

                    teamAdapter.notifyDataSetChanged();
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

                Toast.makeText(ManagerFilterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
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

    //employee spinner bind
    public void employeeData(final String AuthCode, final String userId, final String ManagerID, final int postion) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerFilterActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, empDataBind, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Employee data", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    //bind material List
                    if (empList.size()>0)
                    {
                        empList.clear();
                    }
                    for (int i =0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String AdminID = object.getString("AdminID");
                        String EmployeeName = object.getString("EmployeeName");

                        empList.add(new ManagerEmployeeSpinnerModel(AdminID,EmployeeName));


                    }

                    //first time Select
                    for (int k = 0; k<empList.size(); k++)
                    {
                        if (empList.get(k).getEmpId().equalsIgnoreCase(teamList.get(postion).getAdminId()))
                        {
                            empSpinner.setSelection(k);
                        }
                    }

                    empAdapter.notifyDataSetChanged();
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

                Toast.makeText(ManagerFilterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("AdminID",userId);
                params.put("ManagerID",ManagerID);


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
