package in.co.cfcs.ehrnmt.Manager.ManagerActivity;

import android.app.ProgressDialog;
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

import in.co.cfcs.ehrnmt.Adapter.MedicalAnssuredAdapter;
import in.co.cfcs.ehrnmt.Model.MedicalAnssuranceModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerMedicalActivity extends AppCompatActivity {

    public TextView titleTxt;
    public String empId = "";
    public MedicalAnssuredAdapter adapter;
    public ArrayList<MedicalAnssuranceModel> list = new ArrayList<>();
    public RecyclerView medicalAnssuredRecy;
    public FloatingActionButton fab;
    public String policyUrl = SettingConstant.BaseUrl + "AppEmployeeMedicalPolicy";
    public ConnectionDetector conn;
    public String userId = "",authCode = "";
    public TextView noCust ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_medical);

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
            empId = intent.getStringExtra("empId");
        }

        titleTxt.setText("Medical And Insurance");

        medicalAnssuredRecy = (RecyclerView)findViewById(R.id.medical_anssured_recycler);
        noCust = (TextView) findViewById(R.id.no_record_txt);


        conn = new ConnectionDetector(ManagerMedicalActivity.this);
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerMedicalActivity.this)));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerMedicalActivity.this)));


        adapter = new MedicalAnssuredAdapter(ManagerMedicalActivity.this,list, ManagerMedicalActivity.this, "SecondOne");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManagerMedicalActivity.this);
        medicalAnssuredRecy.setLayoutManager(mLayoutManager);
        medicalAnssuredRecy.setItemAnimator(new DefaultItemAnimator());
        medicalAnssuredRecy.setAdapter(adapter);

        medicalAnssuredRecy.getRecycledViewPool().setMaxRecycledViews(0, 0);



     if (conn.getConnectivityStatus()>0)
     {
         medicalAndAnssuranceList(authCode,userId,empId);
     }else
         {
             conn.showNoInternetAlret();
         }
    }

    // Medical and Anssurance data
    public void medicalAndAnssuranceList(final String AuthCode , final String AdminID, final String EmployeeID) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerMedicalActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, policyUrl, new Response.Listener<String>() {
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
                        String Name = jsonObject.getString("Name");
                        String Number = jsonObject.getString("Number");
                        String Duration = jsonObject.getString("Duration");
                        String AmountInsured = jsonObject.getString("AmountInsured");
                        String PolicyTypeName = jsonObject.getString("PolicyTypeName");
                        String PolicyBy = jsonObject.getString("PolicyBy");
                        String RecordID = jsonObject.getString("RecordID");
                        String InsuranceCompany = jsonObject.getString("InsuranceCompany");
                        String StartDate = jsonObject.getString("StartDate");
                        String EndDate = jsonObject.getString("EndDate");
                        String FileNameText = jsonObject.getString("FileNameText");


                        list.add(new MedicalAnssuranceModel(PolicyTypeName,Number,Duration,Name,AmountInsured,PolicyBy,RecordID,
                                InsuranceCompany,StartDate,EndDate,FileNameText));



                    }

                    if (list.size() == 0)
                    {
                        noCust.setVisibility(View.VISIBLE);
                        medicalAnssuredRecy.setVisibility(View.GONE);
                    }else
                    {
                        noCust.setVisibility(View.GONE);
                        medicalAnssuredRecy.setVisibility(View.VISIBLE);
                    }


                    adapter.notifyDataSetChanged();
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

                Toast.makeText(ManagerMedicalActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("LoginAdminID",AdminID);
                params.put("EmployeeID",EmployeeID);


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
