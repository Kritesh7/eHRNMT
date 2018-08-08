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

import in.co.cfcs.ehrnmt.Adapter.EmergencyContactAdapter;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.EmergencyContactModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerEmergencyAddressActivity extends AppCompatActivity {

    public TextView titleTxt;
    public String empId = "";
    public RecyclerView emergencyContactRecyler;
    public ArrayList<EmergencyContactModel> list = new ArrayList<>();
    public ConnectionDetector conn;
    public EmergencyContactAdapter adapter;
    public String userId = "", authCode = "";
    public String emergencyContactUrl = SettingConstant.BaseUrl + "AppEmployeeEmergencyContactList";
    public TextView noCust;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_emergency_address);

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

        titleTxt.setText("Emergency Contact Address");

        emergencyContactRecyler = (RecyclerView) findViewById(R.id.emergency_contact_recycler);
        noCust = (TextView) findViewById(R.id.no_record_txt);

        conn = new ConnectionDetector(ManagerEmergencyAddressActivity.this);
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerEmergencyAddressActivity.this)));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerEmergencyAddressActivity.this)));

        adapter = new EmergencyContactAdapter(ManagerEmergencyAddressActivity.this,list,
                ManagerEmergencyAddressActivity.this,"SecondOne");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManagerEmergencyAddressActivity.this);
        emergencyContactRecyler.setLayoutManager(mLayoutManager);
        emergencyContactRecyler.setItemAnimator(new DefaultItemAnimator());
        emergencyContactRecyler.setAdapter(adapter);

        emergencyContactRecyler.getRecycledViewPool().setMaxRecycledViews(0, 0);


        if (conn.getConnectivityStatus()>0) {

            emergencyContactDetailsList(authCode,userId, empId);

        }else
        {
            conn.showNoInternetAlret();
        }
    }

    //emergency Contact  List
    public void emergencyContactDetailsList(final String AuthCode , final String AdminID, final String EmployeeID) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerEmergencyAddressActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, emergencyContactUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    if (list.size()>0)
                    {
                        list.clear();
                    }
                    JSONArray emergencyContactArray = jsonObject.getJSONArray("EmergencyContactList");
                    for (int i=0 ; i<emergencyContactArray.length();i++)
                    {
                        JSONObject object = emergencyContactArray.getJSONObject(i);
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

                            String Title = object.getString("Title");
                            String Name = object.getString("Name");
                            String Address = object.getString("Address");
                            String City = object.getString("City");
                            String State = object.getString("State");
                            String CountryName = object.getString("CountryName");
                            String PostCode = object.getString("PostCode");
                            String PhoneNo = object.getString("PhoneNo");
                            String MobileNo = object.getString("MobileNo");
                            String Email = object.getString("Email");
                            String RelationshipName = object.getString("RelationshipName");
                            String Type = object.getString("Type");
                            String LastUpdate = object.getString("LastUpdate");
                            String RecordID = object.getString("RecordID");



                            list.add(new EmergencyContactModel(Title , Name ,Address,City,State,PostCode,CountryName,PhoneNo,MobileNo,
                                    Email,RelationshipName,LastUpdate,Type,RecordID));



                        }


                    }

                    JSONArray statusArray = jsonObject.getJSONArray("Status");
                    for (int k =0; k<statusArray.length(); k++)
                    {
                        JSONObject obj = statusArray.getJSONObject(k);

                        String IsVisibilityAdd = obj.getString("IsVisibilityAdd");

                    }

                    if (list.size() == 0)
                    {
                        noCust.setVisibility(View.VISIBLE);
                        emergencyContactRecyler.setVisibility(View.GONE);
                    }else
                    {
                        noCust.setVisibility(View.GONE);
                        emergencyContactRecyler.setVisibility(View.VISIBLE);
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

                Toast.makeText(ManagerEmergencyAddressActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void Logout() {


        finishAffinity();
        startActivity(new Intent(ManagerEmergencyAddressActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ManagerEmergencyAddressActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ManagerEmergencyAddressActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ManagerEmergencyAddressActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ManagerEmergencyAddressActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ManagerEmergencyAddressActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ManagerEmergencyAddressActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ManagerEmergencyAddressActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ManagerEmergencyAddressActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ManagerEmergencyAddressActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
