package in.co.cfcs.ehrnmt.Main;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.co.cfcs.ehrnmt.Model.CabCityModel;
import in.co.cfcs.ehrnmt.Model.StaticModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddCabActivity extends AppCompatActivity {

    public TextView titleTxt, timeBtn, timeTxt, timeBtn1, timeTxt1, timeBtn2, timeTxt2;
    public Spinner cityOfBookingSpinner;
    public ArrayList<CabCityModel> listOfBooking = new ArrayList<>();
    public ArrayList<StaticModel> staticList = new ArrayList<>();
    public Button addBtn;
    public ImageView dateBtn1, dateBtn2;
    public EditText dateTxt, dateTxt1, dateTxt2, sourceAddTxt, destinationDDtXT, bookingRemarkTxt,
            sourceAddTxt1, destinationDDtXT1, sourceAddTxt2, destinationDDtXT2, dateBtn;
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public String ddlBindTxt = SettingConstant.BaseUrl + "AppddlBookMeAProvision";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeTaxiBookingRequestInsUpdt";
    public String cabDetailsUrl = SettingConstant.BaseUrl + "AppEmployeeTaxiBookingRequestDetail";
    public ArrayAdapter<CabCityModel> cityAdapter;
    public ConnectionDetector conn;
    public String authCode = "", userId = "", cityId = "", cityName = "", actionMode = "", bookingDateStr = "", cityOfNameStr = "", bookingTimeStr = "", sourceAddressStr = "", destinationAddressStr = "", bookingRemarkStr = "", BidStr = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cab);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.addcabtollbar);
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

        titleTxt.setText("Add New Cab Details");

        Intent intent = getIntent();
        if (intent != null) {
            actionMode = intent.getStringExtra("Mode");
          /*  bookingDateStr = intent.getStringExtra("Booking Date");
            bookingTimeStr = intent.getStringExtra("Booking Time");
            sourceAddressStr = intent.getStringExtra("Source Address");
            destinationAddressStr = intent.getStringExtra("Destination Address");
            bookingRemarkStr = intent.getStringExtra("Booking Remark");
            cityOfNameStr = intent.getStringExtra("Booking City");*/
            BidStr = intent.getStringExtra("BID");
        }

        cityOfBookingSpinner = (Spinner) findViewById(R.id.cityofbokkinglist);
        addBtn = (Button) findViewById(R.id.newrequestbtn);
        dateBtn = (EditText) findViewById(R.id.cab_booking_date);
        timeBtn = (TextView) findViewById(R.id.cab_bokkint_time);
        timeBtn1 = (TextView) findViewById(R.id.cab_bokkint_time1);
        timeBtn2 = (TextView) findViewById(R.id.cab_bokkint_time2);
        dateTxt = (EditText) findViewById(R.id.cab_booking_date);
        timeTxt = (TextView) findViewById(R.id.cab_bokkint_time);
        timeTxt1 = (TextView) findViewById(R.id.cab_bokkint_time1);
        timeTxt2 = (TextView) findViewById(R.id.cab_bokkint_time2);
        sourceAddTxt = (EditText) findViewById(R.id.cab_source_add);
        destinationDDtXT = (EditText) findViewById(R.id.cab_destination_add);
        bookingRemarkTxt = (EditText) findViewById(R.id.cab_booking_remark);
        sourceAddTxt1 = (EditText) findViewById(R.id.cab_source_add1);
        destinationDDtXT1 = (EditText) findViewById(R.id.cab_destination_add1);
        sourceAddTxt2 = (EditText) findViewById(R.id.cab_source_add2);
        destinationDDtXT2 = (EditText) findViewById(R.id.cab_destination_add2);


        conn = new ConnectionDetector(AddCabActivity.this);
        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddCabActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddCabActivity.this)));

        //Edit case
        if (actionMode.equalsIgnoreCase("Edit")) {
            titleTxt.setText("Update Cab Details");
            addBtn.setText("Update Cab Details");
            viewCabDetails(authCode, BidStr, userId);

        } else {
            //set current date
            dateTxt.setText(getCurrentTime());

            if (conn.getConnectivityStatus() > 0) {
                personalDdlDetails();

            } else {
                conn.showNoInternetAlret();
            }

        }


        // Date Picker Work
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCabActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                yy = year;
                                mm = monthOfYear;
                                dd = dayOfMonth;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.MONTH, monthOfYear);
                                String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(calendar.getTime());
                                sdf = new DateFormatSymbols().getShortMonths()[monthOfYear];

                                Log.e("checking,............", sdf + " null");
                                dateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        });

        //time piccker
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddCabActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                              /*  hh = hourOfDay;
                                m = minute;*/
                                // ro = checking + hourOfDay  + minute;

                                updateTime(hourOfDay, minute);

                                //timeTxt.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        //time1 picker
        timeBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddCabActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                              /*  hh = hourOfDay;
                                m = minute;*/
                                // ro = checking + hourOfDay  + minute;

                                updateTime1(hourOfDay, minute);

                                //timeTxt.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        //time2 piccker
        timeBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddCabActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                              /*  hh = hourOfDay;
                                m = minute;*/
                                // ro = checking + hourOfDay  + minute;

                                updateTime2(hourOfDay, minute);

                                //timeTxt.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        //City List Spinner
        //change spinner arrow color

        cityOfBookingSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        cityAdapter = new ArrayAdapter<CabCityModel>(AddCabActivity.this, R.layout.customizespinner,
                listOfBooking);
        cityAdapter.setDropDownViewResource(R.layout.customizespinner);
        cityOfBookingSpinner.setAdapter(cityAdapter);


        cityOfBookingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cityId = listOfBooking.get(i).getCityId();
                cityName = listOfBooking.get(i).getCityName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // clear list
                if (staticList.size() > 0) {
                    staticList.clear();
                }

                // add data static
                // data bind first block
                if (!timeTxt.getText().toString().equalsIgnoreCase("") &&
                        !sourceAddTxt.getText().toString().equalsIgnoreCase("") &&
                        !destinationDDtXT.getText().toString().equalsIgnoreCase("")) {
                    staticList.add(new StaticModel(timeTxt.getText().toString(),
                            sourceAddTxt.getText().toString(), destinationDDtXT.getText().toString()));
                } else {
                    Toast.makeText(AddCabActivity.this, "Please enter Booking Details", Toast.LENGTH_SHORT).show();
                }

                //data bind second Block
                if (!timeTxt1.getText().toString().equalsIgnoreCase("") &&
                        !sourceAddTxt1.getText().toString().equalsIgnoreCase("") &&
                        !destinationDDtXT1.getText().toString().equalsIgnoreCase("")) {
                    staticList.add(new StaticModel(timeTxt1.getText().toString(),
                            sourceAddTxt1.getText().toString(), destinationDDtXT1.getText().toString()));
                } else {
                    //Toast.makeText(AddCabActivity.this, "Please enter all feild", Toast.LENGTH_SHORT).show();
                }

                // data bind Third block
                if (!timeTxt2.getText().toString().equalsIgnoreCase("") &&
                        !sourceAddTxt2.getText().toString().equalsIgnoreCase("") &&
                        !destinationDDtXT2.getText().toString().equalsIgnoreCase("")) {
                    staticList.add(new StaticModel(timeTxt2.getText().toString(),
                            sourceAddTxt2.getText().toString(), destinationDDtXT2.getText().toString()));
                } else {
                    // Toast.makeText(AddCabActivity.this, "Please enter all field", Toast.LENGTH_SHORT).show();
                }


                //Make json format
                JSONArray mainArray = new JSONArray();
                JSONObject object = new JSONObject();
                try {


                    for (int i = 0; i < staticList.size(); i++) {

                        JSONObject filterJson = new JSONObject();
                        filterJson.put("Time", staticList.get(i).getTime());
                        filterJson.put("Source", staticList.get(i).getSource());
                        filterJson.put("Destination", staticList.get(i).getDestination());

                        mainArray.put(filterJson);
                    }

                    object.put("members", mainArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // Log.e("cheking json format",object.toString());

                if (cityId.equalsIgnoreCase("")) {
                    Toast.makeText(AddCabActivity.this, "Please Select city name", Toast.LENGTH_SHORT).show();
                } else if (timeTxt.getText().toString().equalsIgnoreCase("")) {
                    timeTxt.setError("Please enter valid time");
                    Toast.makeText(AddCabActivity.this, "Please enter atelest one cab booking details", Toast.LENGTH_SHORT).show();
                } else if (sourceAddTxt.getText().toString().equalsIgnoreCase("")) {
                    sourceAddTxt.setError("Please enter source address");
                    Toast.makeText(AddCabActivity.this, "Please enter atelest one cab booking details", Toast.LENGTH_SHORT).show();

                } else if (destinationDDtXT.getText().toString().equalsIgnoreCase("")) {
                    destinationDDtXT.setError("Please enter destination address");
                    Toast.makeText(AddCabActivity.this, "Please enter atelest one cab booking details", Toast.LENGTH_SHORT).show();


                } else {

                    if (conn.getConnectivityStatus() > 0) {

                        if (actionMode.equalsIgnoreCase("Edit")) {

                            addCabRequest(userId, BidStr, cityId, cityName, dateTxt.getText().toString(), bookingRemarkTxt.getText().toString(),
                                    authCode, object);
                        } else {
                            addCabRequest(userId, "", cityId, cityName, dateTxt.getText().toString(), bookingRemarkTxt.getText().toString(),
                                    authCode, object);
                        }

                    } else {
                        conn.showNoInternetAlret();
                    }

                }

            }
        });


    }

    //get current time
    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();
        String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(cal.getTime());
        //sdf = new DateFormatSymbols().getShortMonths()[month];

        return dateFormat.format(cal.getTime());
    }

    //bind Spinner data
    public void personalDdlDetails() {

        final ProgressDialog pDialog = new ProgressDialog(AddCabActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, ddlBindTxt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    //bind material List
                    if (listOfBooking.size() > 0) {
                        listOfBooking.clear();
                    }
                    listOfBooking.add(new CabCityModel("Please Select City", ""));
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

                        JSONArray cityObj = jsonObject.getJSONArray("TaxiCityName");
                        for (int i = 0; i < cityObj.length(); i++) {
                            JSONObject object = cityObj.getJSONObject(i);

                            String CityName = object.getString("CityName");
                            String CityID = object.getString("CityID");

                            listOfBooking.add(new CabCityModel(CityName, CityID));


                        }

                        //Edit case
                        for (int k = 0; k < listOfBooking.size(); k++) {
                            if (actionMode.equalsIgnoreCase("Edit")) {
                                if (listOfBooking.get(k).getCityName().equalsIgnoreCase(cityOfNameStr)) {
                                    cityOfBookingSpinner.setSelection(k);
                                }
                            }
                        }
                    }

                    cityAdapter.notifyDataSetChanged();
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
        });
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //add New cab Request
    public void addCabRequest(final String AdminID, final String BID, final String BookCityID, final String BookCityName,
                              final String BookDate, final String EmpComment, final String AuthCode, final JSONObject mainArray) {

        final ProgressDialog pDialog = new ProgressDialog(AddCabActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, addUrl, new Response.Listener<String>() {
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
                            onBackPressed();

                        } else {
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();

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

                params.put("AdminID", AdminID);
                params.put("BID", BID);
                params.put("BookCityID", BookCityID);
                params.put("BookCityName", BookCityName);
                params.put("BookDate", BookDate);
                params.put("EmpComment", EmpComment);
                params.put("AuthCode", AuthCode);
                params.put("ItemDetailJson", mainArray.toString());
                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        timeTxt.setText(aTime);
    }

    private void updateTime1(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        timeTxt1.setText(aTime);
    }

    private void updateTime2(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        timeTxt2.setText(aTime);
    }

    //view cab details
    public void viewCabDetails(final String AuthCode, final String BID, final String userId) {

        final ProgressDialog pDialog = new ProgressDialog(AddCabActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
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
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        }
                    } else {

                        JSONArray requestDetailsArray = jsonObject.getJSONArray("TaxiBookingMaster");
                        for (int i = 0; i < requestDetailsArray.length(); i++) {
                            JSONObject object = requestDetailsArray.getJSONObject(i);

                            String EmpName = object.getString("EmpName");
                            String requestDate = object.getString("AddDateText");
                            String approvedBy = object.getString("AppDateText");
                            String HrComment = object.getString("HrComment");
                            String AppStatusText = object.getString("AppStatusText");
                            String BIDStr = object.getString("BID");
                            String CityName = object.getString("CityName");
                            String BookDateText = object.getString("BookDateText");
                            String EmpComment = object.getString("EmpComment");

                            dateTxt.setText(BookDateText);
                            bookingRemarkTxt.setText(EmpComment);

                            cityOfNameStr = CityName;


                        }

                        JSONArray itemdetaislArray = jsonObject.getJSONArray("TaxiBookingDetail");
                        if (staticList.size() > 0) {
                            staticList.clear();
                        }
                        for (int j = 0; j < itemdetaislArray.length(); j++) {
                            JSONObject object = itemdetaislArray.getJSONObject(j);

                            String BookTime = object.getString("BookTime");
                            String SourceAdd = object.getString("SourceAdd");
                            String DestinationAdd = object.getString("DestinationAdd");
                            staticList.add(new StaticModel(BookTime, SourceAdd, DestinationAdd));

                        }

                        try {
                            timeTxt.setText(staticList.get(0).getTime());
                            sourceAddTxt.setText(staticList.get(0).getSource());
                            destinationDDtXT.setText(staticList.get(0).getDestination());

                            timeTxt1.setText(staticList.get(1).getTime());
                            sourceAddTxt1.setText(staticList.get(1).getSource());
                            destinationDDtXT1.setText(staticList.get(1).getDestination());

                            timeTxt2.setText(staticList.get(2).getTime());
                            sourceAddTxt2.setText(staticList.get(2).getSource());
                            destinationDDtXT2.setText(staticList.get(2).getDestination());

                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }

                        personalDdlDetails();


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

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_right_out);
        } else {
            getFragmentManager().popBackStack();
        }

    }

    private void Logout() {


        finishAffinity();
        startActivity(new Intent(AddCabActivity.this, LoginActivity.class));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddCabActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddCabActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddCabActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddCabActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddCabActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddCabActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddCabActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddCabActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddCabActivity.this,
                "")));

    }

}
