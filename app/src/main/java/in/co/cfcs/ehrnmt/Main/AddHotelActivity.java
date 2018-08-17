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
import in.co.cfcs.ehrnmt.Model.HotelNameModel;
import in.co.cfcs.ehrnmt.Model.HotelTypeModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddHotelActivity extends AppCompatActivity {

    public TextView titleTxt, checkInDateBtn, checkInDateTxt, checkinTimeTxt, checkInTimeBtn, checkOutDateBtn, checkOutDateTxt;
    public Spinner hotelTypeSpinner, cityofBookingSpinner, hotelSpinner;
    public ArrayList<CabCityModel> cityList = new ArrayList<>();
    public ArrayList<HotelTypeModel> hotelTypeList = new ArrayList<>();
    public ArrayList<HotelNameModel> hotelList = new ArrayList<>();
    public String ddlBindTxt = SettingConstant.BaseUrl + "AppddlBookMeAProvision";
    public String ddlBindNameAndCityTxt = SettingConstant.BaseUrl + "AppddlHotelList";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeHotelBookingInsUpdt";
    public ArrayAdapter<CabCityModel> cityAdapter;
    public ArrayAdapter<HotelTypeModel> hotelTypeAdapter;
    public ArrayAdapter<HotelNameModel> hotelAdapter;
    public ConnectionDetector conn;
    public String hotelTypeID = "", hotelCityId = "", authcode = "", userId = "", hotelId = "";
    public EditText remarkTxt;
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public Button addBtn;
    public String hotelTypeStr = "", cityOfNameStr = "", hotelNameStr = "", checkInDateStr = "", checkInTimeStr = "", checkOutDateStr = "", reamrkStr = "", actionMode = "", bidStr = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tolbarofhotel);
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

        titleTxt.setText("Add New Hotel Booking");

        Intent intent = getIntent();
        if (intent != null) {
            actionMode = intent.getStringExtra("Mode");
            hotelTypeStr = intent.getStringExtra("Hotel type");
            cityOfNameStr = intent.getStringExtra("Booking City");
            hotelNameStr = intent.getStringExtra("Guest House");
            checkInDateStr = intent.getStringExtra("Check In Date");
            checkInTimeStr = intent.getStringExtra("Check In Time");
            checkOutDateStr = intent.getStringExtra("Check Out Time");
            reamrkStr = intent.getStringExtra("Remark");
            bidStr = intent.getStringExtra("BID");
        }

        conn = new ConnectionDetector(AddHotelActivity.this);
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddHotelActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddHotelActivity.this)));


        hotelTypeSpinner = (Spinner) findViewById(R.id.hoteltypespinner);
        cityofBookingSpinner = (Spinner) findViewById(R.id.cityofbookingspinner);
        hotelSpinner = (Spinner) findViewById(R.id.hotelspinner);
        checkInDateBtn = (TextView) findViewById(R.id.hotel_checkindatetxt);
        checkInTimeBtn = (TextView) findViewById(R.id.hotel_checkintimetxt);
        checkOutDateBtn = (TextView) findViewById(R.id.hotel_checkoutdatetxt);
        checkInDateTxt = (TextView) findViewById(R.id.hotel_checkindatetxt);
        checkinTimeTxt = (TextView) findViewById(R.id.hotel_checkintimetxt);
        checkOutDateTxt = (TextView) findViewById(R.id.hotel_checkoutdatetxt);
        remarkTxt = (EditText) findViewById(R.id.hotel_emp_remark);
        addBtn = (Button) findViewById(R.id.newrequestbtn);

        //edit mode
        if (actionMode.equalsIgnoreCase("Edit")) {
            titleTxt.setText("Update Hotel Booking");
            addBtn.setText("Update Hotel Booking");
            checkInDateTxt.setText(checkInDateStr);
            checkOutDateTxt.setText(checkOutDateStr);
            checkinTimeTxt.setText(checkInTimeStr);
            remarkTxt.setText(reamrkStr);
        } else {
            //select current date
            checkOutDateTxt.setText(getCurrentTime());

        }

        //City List Spinner
        //change spinner arrow color
        cityofBookingSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        cityAdapter = new ArrayAdapter<CabCityModel>(AddHotelActivity.this, R.layout.customizespinner,
                cityList);
        cityAdapter.setDropDownViewResource(R.layout.customizespinner);
        cityofBookingSpinner.setAdapter(cityAdapter);

        //hotel Type Spinner
        //change spinner arrow color
        hotelTypeSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        hotelTypeAdapter = new ArrayAdapter<HotelTypeModel>(AddHotelActivity.this, R.layout.customizespinner,
                hotelTypeList);
        hotelTypeAdapter.setDropDownViewResource(R.layout.customizespinner);
        hotelTypeSpinner.setAdapter(hotelTypeAdapter);

        //hotel Spinner
        //change spinner arrow color
        hotelSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        hotelAdapter = new ArrayAdapter<HotelNameModel>(AddHotelActivity.this, R.layout.customizespinner,
                hotelList);
        hotelAdapter.setDropDownViewResource(R.layout.customizespinner);
        hotelSpinner.setAdapter(hotelAdapter);


        //select hotel type
        hotelTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                hotelTypeID = hotelTypeList.get(i).getHotelTypeId();
                hotelNameANDCity(hotelTypeID, "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cityofBookingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hotelCityId = cityList.get(i).getCityId();

                hotelNameANDCity(hotelTypeID, hotelCityId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //get hotel Id
        hotelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hotelId = hotelList.get(i).getHotelId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //checkInDate DatePicker
        checkInDateBtn.setOnClickListener(new View.OnClickListener() {
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddHotelActivity.this,
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
                                checkInDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        });

        //checkOut Date Picker
        checkOutDateBtn.setOnClickListener(new View.OnClickListener() {
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddHotelActivity.this,
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
                                checkOutDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        });

        //check In Time Picker
        checkInTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddHotelActivity.this,
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

        //bind data
        if (conn.getConnectivityStatus() > 0) {
            personalDdlDetails();
        } else {
            conn.showNoInternetAlret();
        }

        // add Hotel Reequest
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (hotelTypeID.equalsIgnoreCase("")) {
                    Toast.makeText(AddHotelActivity.this, "Please Select Hotel Type", Toast.LENGTH_SHORT).show();
                } else if (hotelCityId.equalsIgnoreCase("")) {
                    Toast.makeText(AddHotelActivity.this, "Please Select City", Toast.LENGTH_SHORT).show();
                } else if (hotelId.equalsIgnoreCase("")) {
                    Toast.makeText(AddHotelActivity.this, "Please Select Hotel", Toast.LENGTH_SHORT).show();
                } else if (checkOutDateTxt.getText().toString().equalsIgnoreCase("")) {
                    checkOutDateTxt.setError("Please enter check in date");
                } else if (checkinTimeTxt.getText().toString().equalsIgnoreCase("")) {
                    checkinTimeTxt.setError("Please enter check in time");
                } else if (checkOutDateTxt.getText().toString().equalsIgnoreCase("")) {
                    checkOutDateTxt.setError("Please enter check out date");
                } else {

                    if (conn.getConnectivityStatus() > 0) {

                        addHotelRequest(userId, bidStr, hotelId, hotelCityId, checkInDateTxt.getText().toString(), checkinTimeTxt.getText().toString(),
                                checkOutDateTxt.getText().toString(), authcode, remarkTxt.getText().toString());
                    } else {
                        conn.showNoInternetAlret();
                    }

                }

            }
        });
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

        checkinTimeTxt.setText(aTime);
    }


    //add Hotel Booking
    public void addHotelRequest(final String AdminID, final String BID, final String HID, final String CityID,
                                final String CheckInDate, final String CheckInTime, final String CheckOutDate,
                                final String AuthCode, final String EmpRemark) {

        final ProgressDialog pDialog = new ProgressDialog(AddHotelActivity.this, R.style.AppCompatAlertDialogStyle);
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
                params.put("HID", HID);
                params.put("CityID", CityID);
                params.put("CheckInDate", CheckInDate);
                params.put("CheckInTime", CheckInTime);
                params.put("CheckOutDate", CheckOutDate);
                params.put("EmpRemark", EmpRemark);
                params.put("AuthCode", AuthCode);

                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //bind the hotel Type
    public void personalDdlDetails() {

        final ProgressDialog pDialog = new ProgressDialog(AddHotelActivity.this, R.style.AppCompatAlertDialogStyle);
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

                    if (hotelTypeList.size() > 0) {
                        hotelTypeList.clear();
                    }
                    hotelTypeList.add(new HotelTypeModel("Please Select Hotel Type", ""));

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
                        JSONArray hotelObj = jsonObject.getJSONArray("HotelType");
                        for (int k = 0; k < hotelObj.length(); k++) {
                            JSONObject object = hotelObj.getJSONObject(k);

                            String HotelType = object.getString("HotelType");
                            String HotelTypeID = object.getString("HotelTypeID");

                            hotelTypeList.add(new HotelTypeModel(HotelType, HotelTypeID));
                        }

                        //Edit case
                        for (int k = 0; k < hotelTypeList.size(); k++) {
                            if (actionMode.equalsIgnoreCase("Edit")) {
                                if (hotelTypeList.get(k).getHotelTypeId().equalsIgnoreCase(hotelTypeStr)) {
                                    hotelTypeSpinner.setSelection(k);
                                    hotelTypeID = hotelTypeList.get(k).getHotelTypeId();
                                    //hotelNameANDCity(hotelTypeID,"");
                                }
                            }
                        }

                    }

                    hotelTypeAdapter.notifyDataSetChanged();
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


    //bind the city and hotel name
    public void hotelNameANDCity(final String HotelType, final String HotelCityID) {

        final ProgressDialog pDialog = new ProgressDialog(AddHotelActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, ddlBindNameAndCityTxt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    //bind material List
                    if (cityList.size() > 0) {
                        cityList.clear();
                    }
                    cityList.add(new CabCityModel("Please Select City", ""));
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

                        JSONArray cityObj = jsonObject.getJSONArray("HotelCityName");
                        for (int i = 0; i < cityObj.length(); i++) {
                            JSONObject object = cityObj.getJSONObject(i);

                            String CityName = object.getString("CityName");
                            String CityID = object.getString("CityID");

                            cityList.add(new CabCityModel(CityName, CityID));

                        }

                        if (hotelList.size() > 0) {
                            hotelList.clear();
                        }
                        hotelList.add(new HotelNameModel("Please Select Hotel", ""));
                        JSONArray hotelObj = jsonObject.getJSONArray("HotelName");
                        for (int k = 0; k < hotelObj.length(); k++) {
                            JSONObject object = hotelObj.getJSONObject(k);

                            String HotelType = object.getString("HotelName");
                            String HotelTypeID = object.getString("HotelID");

                            hotelList.add(new HotelNameModel(HotelType, HotelTypeID));
                        }

                        //Edit case
                        for (int k = 0; k < hotelList.size(); k++) {
                            if (actionMode.equalsIgnoreCase("Edit")) {
                                if (hotelList.get(k).getHotelName().equalsIgnoreCase(hotelNameStr)) {
                                    hotelSpinner.setSelection(k);
                                    hotelId = hotelList.get(k).getHotelId();
                                    actionMode = "add";
                                }
                            }
                        }

                        //Hotel Name

                        for (int k = 0; k < cityList.size(); k++) {
                            if (actionMode.equalsIgnoreCase("Edit")) {
                                if (cityList.get(k).getCityName().equalsIgnoreCase(cityOfNameStr)) {
                                    cityofBookingSpinner.setSelection(k);
                                    hotelCityId = cityList.get(k).getCityId();
                                    // hotelNameANDCity(hotelCityId,"");
                                }
                            }
                        }
                    }

                    hotelAdapter.notifyDataSetChanged();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("HotelType", HotelType);
                params.put("HotelCityID", HotelCityID);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }

    private void Logout() {


        finishAffinity();
        startActivity(new Intent(AddHotelActivity.this, LoginActivity.class));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddHotelActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddHotelActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddHotelActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddHotelActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddHotelActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddHotelActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddHotelActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddHotelActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddHotelActivity.this,
                "")));

    }

}
