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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Adapter.ContactAdapter;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.ContactModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerAddressContactActivity extends AppCompatActivity {

    public TextView titleTxt;
    public String empId = "";
    public RecyclerView contactRecyler;
    public ContactAdapter adapter;
    public ArrayList<ContactModel> list = new ArrayList<>();
    public ConnectionDetector conn;
    public String userId = "",authCode = "", IsAddAddressDetail = "", IsVisibilityAdd = "";
    public TextView noCust ;
    public String contactUrl = SettingConstant.BaseUrl + "AppEmployeeAddressList";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_address_contact);

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

        titleTxt.setText("Address And Contact");

        contactRecyler = (RecyclerView) findViewById(R.id.contact_recycler);
        noCust = (TextView) findViewById(R.id.no_record_txt);

        conn = new ConnectionDetector(ManagerAddressContactActivity.this);
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerAddressContactActivity.this)));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerAddressContactActivity.this)));

        adapter = new ContactAdapter(list,ManagerAddressContactActivity.this, ManagerAddressContactActivity.this, "SecondOne");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManagerAddressContactActivity.this);
        contactRecyler.setLayoutManager(mLayoutManager);
        contactRecyler.setItemAnimator(new DefaultItemAnimator());
        contactRecyler.setAdapter(adapter);

        contactRecyler.getRecycledViewPool().setMaxRecycledViews(0, 0);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (conn.getConnectivityStatus()>0) {

            contactDetailsList(authCode,userId,empId);

        }else
        {
            conn.showNoInternetAlret();
        }

    }

    // Address List
    public void contactDetailsList(final String AuthCode , final String AdminID, final String EmployeeID) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerAddressContactActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, contactUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    if (list.size()>0)
                    {
                        list.clear();
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("AddressList");
                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
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

                            String Type = object.getString("Type");
                            String Address = object.getString("Address");
                            String City = object.getString("City");
                            String State = object.getString("State");
                            String PostCode = object.getString("PostCode");
                            String CountryName = object.getString("CountryName");
                            String LastUpdated = object.getString("LastUpdated");
                            String RecordID = object.getString("RecordID");

                            list.add(new ContactModel(Type , Address ,City,State,PostCode,CountryName,LastUpdated,RecordID));
                        }


                    }

                    JSONArray statusArray = jsonObject.getJSONArray("Status");
                    for (int k =0; k<statusArray.length(); k++)
                    {
                        JSONObject obj = statusArray.getJSONObject(k);

                        IsAddAddressDetail = obj.getString("IsAddAddressDetail");
                        IsVisibilityAdd = obj.getString("IsVisibilityAdd");

                    }

                    if (list.size() == 0)
                    {
                        noCust.setVisibility(View.VISIBLE);
                        contactRecyler.setVisibility(View.GONE);
                    }else
                    {
                        noCust.setVisibility(View.GONE);
                        contactRecyler.setVisibility(View.VISIBLE);
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
        startActivity(new Intent(ManagerAddressContactActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ManagerAddressContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ManagerAddressContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ManagerAddressContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ManagerAddressContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ManagerAddressContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ManagerAddressContactActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ManagerAddressContactActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ManagerAddressContactActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ManagerAddressContactActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
