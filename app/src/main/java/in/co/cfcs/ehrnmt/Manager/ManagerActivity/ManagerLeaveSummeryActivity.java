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

import in.co.cfcs.ehrnmt.Adapter.LeaveSummarryAdapter;
import in.co.cfcs.ehrnmt.Model.LeaveSummarryModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ManagerLeaveSummeryActivity extends AppCompatActivity {

    public TextView titleTxt,noCust;
    public String empId = "";

    public LeaveSummarryAdapter adapter;
    public ArrayList<LeaveSummarryModel> list = new ArrayList<>();
    public RecyclerView leaveSummrryRecy;
    public String leaveSummeryUrl = SettingConstant.BaseUrl + "AppEmployeeLeaveSummaryList";

    public String userId = "", authCode = "";
    public ConnectionDetector conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_leave_summery);

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

        titleTxt.setText("Leave Summery");

        conn = new ConnectionDetector(ManagerLeaveSummeryActivity.this);

        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ManagerLeaveSummeryActivity.this)));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ManagerLeaveSummeryActivity.this)));

        leaveSummrryRecy =(RecyclerView)findViewById(R.id.leave_summerry_recycler);
        noCust = (TextView) findViewById(R.id.no_record_txt);

        adapter = new LeaveSummarryAdapter(ManagerLeaveSummeryActivity.this,list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManagerLeaveSummeryActivity.this);
        leaveSummrryRecy.setLayoutManager(mLayoutManager);
        leaveSummrryRecy.setItemAnimator(new DefaultItemAnimator());
        leaveSummrryRecy.setAdapter(adapter);

        leaveSummrryRecy.getRecycledViewPool().setMaxRecycledViews(0, 0);


        //set data
        if (conn.getConnectivityStatus()>0) {

            leaveSummeryData(authCode, userId, empId);

        }else
        {
            conn.showNoInternetAlret();
        }

    }

  /*  @Override
    protected void onResume() {
        super.onResume();


    }*/

    //Leave Summery List
    public void leaveSummeryData(final String AuthCode , final String AdminID, final String EmployeeID) {

        final ProgressDialog pDialog = new ProgressDialog(ManagerLeaveSummeryActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, leaveSummeryUrl, new Response.Listener<String>() {
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
                        String LeaveTypeName = jsonObject.getString("LeaveTypeName");
                        String LeaveYear = jsonObject.getString("LeaveYear");
                        String EntitledFor = jsonObject.getString("LeaveAvailable");
                        String LeaveCarryOver = jsonObject.getString("LeaveCarryOver");
                        String LeaveTaken = jsonObject.getString("LeaveTaken");
                        String LeaveBalance = jsonObject.getString("LeaveBalance");
                        String LeaveAvail = jsonObject.getString("LeaveAvail");
                        String SPLeaveText = jsonObject.getString("SPLeaveText");

                        list.add(new LeaveSummarryModel(LeaveTypeName,LeaveYear,EntitledFor,LeaveCarryOver,LeaveTaken,
                                LeaveBalance,LeaveAvail,SPLeaveText));



                    }


                    if (list.size() == 0)
                    {
                        noCust.setVisibility(View.VISIBLE);
                        leaveSummrryRecy.setVisibility(View.GONE);
                    }else
                    {
                        noCust.setVisibility(View.GONE);
                        leaveSummrryRecy.setVisibility(View.VISIBLE);
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

                Toast.makeText(ManagerLeaveSummeryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
