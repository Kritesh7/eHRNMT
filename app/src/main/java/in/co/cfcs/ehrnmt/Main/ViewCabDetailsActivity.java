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

import in.co.cfcs.ehrnmt.Adapter.CabItemsAdapter;
import in.co.cfcs.ehrnmt.Model.CabItemModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class ViewCabDetailsActivity extends AppCompatActivity {

    public TextView titleTxt, empNameTxt, requestDateTxt, approvedByTxt, bookDateTxt, cityNameTxt, statusTxt, empcommTxt, hrcommTxt,
            followDateTxt, hrcommentFontTxt;
    public String cabDetailsUrl = SettingConstant.BaseUrl + "AppEmployeeTaxiBookingRequestDetail";
    public ConnectionDetector conn;
    public String bidString = "", authCode = "", userId = "";
    public in.co.cfcs.ehrnmt.Source.MyListLayout cabItemList;
    public CabItemsAdapter adapter;
    public ArrayList<CabItemModel> list = new ArrayList<>();
    public Button editBtn;
    public String BookDateText = "", CityName = "", BookTime = "", SourceAdd = "", DestinationAdd = "", EmpComment = "", BIDStr = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cab_details);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewcabtoolbar);
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

        titleTxt.setText("Cab Details");

        Intent intent = getIntent();
        if (intent != null) {
            bidString = intent.getStringExtra("Bid");
        }

        conn = new ConnectionDetector(ViewCabDetailsActivity.this);
        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(ViewCabDetailsActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(ViewCabDetailsActivity.this)));

        empNameTxt = (TextView) findViewById(R.id.cab_empname);
        requestDateTxt = (TextView) findViewById(R.id.cab_request_date);
        approvedByTxt = (TextView) findViewById(R.id.cab_request);
        bookDateTxt = (TextView) findViewById(R.id.cab_bookdate);
        cityNameTxt = (TextView) findViewById(R.id.cab_cityname);
        statusTxt = (TextView) findViewById(R.id.cab_status);
        empcommTxt = (TextView) findViewById(R.id.cab_employcomment);
        hrcommTxt = (TextView) findViewById(R.id.cab_hr_comment);
        followDateTxt = (TextView) findViewById(R.id.followdatetxt);
        hrcommentFontTxt = (TextView) findViewById(R.id.hrcommenttxt);
        cabItemList = (in.co.cfcs.ehrnmt.Source.MyListLayout) findViewById(R.id.cab_item_list);
        editBtn = (Button) findViewById(R.id.editcab);

        adapter = new CabItemsAdapter(ViewCabDetailsActivity.this, list);

        cabItemList.setAdapter(adapter);


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ViewCabDetailsActivity.this, AddCabActivity.class);
                i.putExtra("Mode", "Edit");
                i.putExtra("Booking Date", BookDateText);
                i.putExtra("Booking City", CityName);
                i.putExtra("Booking Time", BookTime);
                i.putExtra("Source Address", SourceAdd);
                i.putExtra("Destination Address", DestinationAdd);
                i.putExtra("Booking Remark", EmpComment);
                i.putExtra("BID", BIDStr);

                startActivity(i);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (conn.getConnectivityStatus() > 0) {
            viewCabDetails(authCode, bidString, userId);

        } else {
            conn.showNoInternetAlret();
        }
    }

    //view cab details
    public void viewCabDetails(final String AuthCode, final String BID, final String userId) {

        final ProgressDialog pDialog = new ProgressDialog(ViewCabDetailsActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, cabDetailsUrl, new Response.Listener<String>() {
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
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }else {
                        JSONArray requestDetailsArray = jsonObject.getJSONArray("TaxiBookingMaster");
                        for (int i = 0; i < requestDetailsArray.length(); i++) {
                            JSONObject object = requestDetailsArray.getJSONObject(i);

                            String EmpName = object.getString("EmpName");
                            String requestDate = object.getString("AddDateText");
                            String approvedBy = object.getString("AppDateText");
                            String HrComment = object.getString("HrComment");
                            String AppStatusText = object.getString("AppStatusText");
                            BIDStr = object.getString("BID");
                            CityName = object.getString("CityName");
                            BookDateText = object.getString("BookDateText");
                            EmpComment = object.getString("EmpComment");
                            //String BookDateText = object.getString("BookDateText");

                            empNameTxt.setText(EmpName);
                            empcommTxt.setText(EmpComment);
                            requestDateTxt.setText(requestDate);
                            approvedByTxt.setText(approvedBy);
                            hrcommTxt.setText(HrComment);
                            statusTxt.setText(AppStatusText);
                            cityNameTxt.setText(CityName);
                            bookDateTxt.setText(BookDateText);


                            if (HrComment.equalsIgnoreCase("") || HrComment.equalsIgnoreCase("null")) {
                                hrcommTxt.setVisibility(View.GONE);
                                hrcommentFontTxt.setVisibility(View.GONE);

                            } else if (approvedBy.equalsIgnoreCase("") || approvedBy.equalsIgnoreCase("null")) {
                                approvedByTxt.setVisibility(View.GONE);
                                followDateTxt.setVisibility(View.GONE);
                            }


                        }

                        JSONArray itemdetaislArray = jsonObject.getJSONArray("TaxiBookingDetail");
                        if (list.size() > 0) {
                            list.clear();
                        }
                        for (int j = 0; j < itemdetaislArray.length(); j++) {
                            JSONObject object = itemdetaislArray.getJSONObject(j);

                            BookTime = object.getString("BookTime");
                            SourceAdd = object.getString("SourceAdd");
                            DestinationAdd = object.getString("DestinationAdd");


                            list.add(new CabItemModel(BookTime, SourceAdd, DestinationAdd));


                        }


                    }

                    adapter.notifyDataSetChanged();
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

                Toast.makeText(ViewCabDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode", AuthCode);
                params.put("BID", BID);
                params.put("AdminID", userId);


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
        startActivity(new Intent(ViewCabDetailsActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(ViewCabDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(ViewCabDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(ViewCabDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(ViewCabDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(ViewCabDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(ViewCabDetailsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(ViewCabDetailsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(ViewCabDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(ViewCabDetailsActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
