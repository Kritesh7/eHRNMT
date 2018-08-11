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

import in.co.cfcs.ehrnmt.Adapter.LangaugeAdapter;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.LanguageModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerLangaugeActivity extends AppCompatActivity {

    public TextView titleTxt;
    public String empId = "";
    public LangaugeAdapter adapter;
    public ArrayList<LanguageModel> list = new ArrayList<>();
    public RecyclerView langauageRecy;
    public FloatingActionButton fab;
    public String langaugeUrl = SettingConstant.BaseUrl + "AppEmployeeLanguageList";
    public ConnectionDetector conn;
    public String userId = "",authCode = "";
    public TextView noCust ;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_langauge);

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

        titleTxt.setText("Language Details");

        langauageRecy = (RecyclerView)findViewById(R.id.lanagauage_recycler);
        noCust = (TextView) findViewById(R.id.no_record_txt);

        conn = new ConnectionDetector(ManagerLangaugeActivity.this);
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerLangaugeActivity.this)));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerLangaugeActivity.this)));


        adapter = new LangaugeAdapter(ManagerLangaugeActivity.this,list, ManagerLangaugeActivity.this,"SecondOne");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManagerLangaugeActivity.this);
        langauageRecy.setLayoutManager(mLayoutManager);
        langauageRecy.setItemAnimator(new DefaultItemAnimator());
        langauageRecy.setAdapter(adapter);

        langauageRecy.getRecycledViewPool().setMaxRecycledViews(0, 0);

        if (conn.getConnectivityStatus()>0) {

            langauageList(authCode,userId,empId);

        }else
        {
            conn.showNoInternetAlret();
        }


    }

    //Skills list
    public void langauageList(final String AuthCode , final String AdminID, final String EmployeeID) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerLangaugeActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, langaugeUrl, new Response.Listener<String>() {
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
                            String LanguageName = jsonObject.getString("LanguageName");
                            String Read = jsonObject.getString("Read");
                            String Write = jsonObject.getString("Write");
                            String Speak = jsonObject.getString("Speak");
                            String RecordID = jsonObject.getString("RecordID");

                            list.add(new LanguageModel(LanguageName,Read,Write,Speak,RecordID));

                        }

                    }

                    if (list.size() == 0)
                    {
                        noCust.setVisibility(View.VISIBLE);
                        langauageRecy.setVisibility(View.GONE);
                    }else
                    {
                        noCust.setVisibility(View.GONE);
                        langauageRecy.setVisibility(View.VISIBLE);
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
        startActivity(new Intent(ManagerLangaugeActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ManagerLangaugeActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ManagerLangaugeActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ManagerLangaugeActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ManagerLangaugeActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ManagerLangaugeActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ManagerLangaugeActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ManagerLangaugeActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ManagerLangaugeActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ManagerLangaugeActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
