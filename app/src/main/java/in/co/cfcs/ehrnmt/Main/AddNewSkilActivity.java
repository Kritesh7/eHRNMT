package in.co.cfcs.ehrnmt.Main;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.transition.TransitionManager;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.co.cfcs.ehrnmt.Model.ProficiencySpiinerModel;
import in.co.cfcs.ehrnmt.Model.SkillsSpinnerModel;
import in.co.cfcs.ehrnmt.Model.SourceSpinnerModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddNewSkilActivity extends AppCompatActivity {

    public TextView titleTxt;
    public Spinner skillSpinner, proficenacySpinner, sourceSpinner;
    public ArrayList<SkillsSpinnerModel> skillList = new ArrayList<>();
    public ArrayList<ProficiencySpiinerModel> profyList = new ArrayList<>();
    public ArrayList<SourceSpinnerModel> sourceList = new ArrayList<>();
    public RadioGroup radioGroup;
    public RadioButton lastUsedBtn, currentUsedBtn;
    public LinearLayout mainLay, lastUsedLay;
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeeSkillANDLanguage";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeSkillInsUpdt";
    public ArrayAdapter<SkillsSpinnerModel> skillAdapter;
    public ArrayAdapter<ProficiencySpiinerModel> profyAdapter;
    public ArrayAdapter<SourceSpinnerModel> sourceAdapter;
    public ConnectionDetector conn;
    public ImageView lastUsedCalBtn;
    public EditText lastUsedTxt;
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public Button addBtn;
    public String authcode = "", userId = "", skillId = "", proficieancyId = "", sourceId = "", checkUsed = "true"
            ,actionMode = "", skillNameStr = "", proficeiancyNameStr, sourceNameStr = "", useSkillStr = "", lastDateStr = ""
            , recordId = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_skil);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.newskilltollbar);
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

        titleTxt.setText("Add New Skill");

        Intent intent = getIntent();
        if (intent != null)
        {
            actionMode = intent.getStringExtra("ActionMode");
            skillNameStr = intent.getStringExtra("SkillName");
            recordId = intent.getStringExtra("RecordId");
            proficeiancyNameStr = intent.getStringExtra("ProficeiancyName");
            sourceNameStr = intent.getStringExtra("SourceName");
            useSkillStr = intent.getStringExtra("CurrentelyUsed");
            lastDateStr = intent.getStringExtra("LastUsedDate");

        }

        conn = new ConnectionDetector(AddNewSkilActivity.this);
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddNewSkilActivity.this)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddNewSkilActivity.this)));


        skillSpinner = (Spinner)findViewById(R.id.skillspinner);
        proficenacySpinner = (Spinner)findViewById(R.id.proficenacySpinner);
        sourceSpinner = (Spinner)findViewById(R.id.sourceSpinner);
        radioGroup = (RadioGroup) findViewById(R.id.skillsusedradiogroup);
        lastUsedBtn = (RadioButton)findViewById(R.id.lastused);
        currentUsedBtn = (RadioButton)findViewById(R.id.currentused);
        lastUsedLay = (LinearLayout)findViewById(R.id.lastusedlay);
        mainLay = (LinearLayout)findViewById(R.id.mainlay);
        lastUsedCalBtn = (ImageView) findViewById(R.id.lastusedbtn);
        lastUsedTxt = (EditText) findViewById(R.id.lastusedtxt);
        addBtn = (Button) findViewById(R.id.newrequestbtn);


        if (actionMode.equalsIgnoreCase("EditMode"))
        {
            if (useSkillStr.equalsIgnoreCase("true"))
            {
                currentUsedBtn.setChecked(true);
                checkUsed = "true";

                TransitionManager.beginDelayedTransition(mainLay);
                lastUsedLay.setVisibility(View.GONE);


            }else
                {
                    lastUsedBtn.setChecked(true);
                    checkUsed = "false";

                    TransitionManager.beginDelayedTransition(mainLay);
                    lastUsedLay.setVisibility(View.VISIBLE);

                }

                lastUsedTxt.setText(lastDateStr);

            addBtn.setText("Update Skill");
            titleTxt.setText("Update Skill");
        }else
        {
            currentUsedBtn.setChecked(true);
        }

        //Skill Spinner List Spinner
        // change spinner arrow color
        skillSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);
        skillAdapter = new ArrayAdapter<SkillsSpinnerModel>(AddNewSkilActivity.this, R.layout.customizespinner,
                skillList);
        skillAdapter.setDropDownViewResource(R.layout.customizespinner);
        skillSpinner.setAdapter(skillAdapter);

        //Proficeancy Spinner
        //change spinner arrow color
        proficenacySpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);
        profyAdapter = new ArrayAdapter<ProficiencySpiinerModel>(AddNewSkilActivity.this, R.layout.customizespinner,
                profyList);
        profyAdapter.setDropDownViewResource(R.layout.customizespinner);
        proficenacySpinner.setAdapter(profyAdapter);

        //Source Spinner
        //change spinner arrow color
        sourceSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);
        sourceAdapter = new ArrayAdapter<SourceSpinnerModel>(AddNewSkilActivity.this, R.layout.customizespinner,
                sourceList);
        sourceAdapter.setDropDownViewResource(R.layout.customizespinner);
        sourceSpinner.setAdapter(sourceAdapter);



        //Radio Group Work
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                if (i == R.id.lastused)
                {
                    TransitionManager.beginDelayedTransition(mainLay);
                    lastUsedLay.setVisibility(View.VISIBLE);

                    checkUsed = "false";

                }
                else if (i == R.id.currentused)
                {
                    TransitionManager.beginDelayedTransition(mainLay);
                    lastUsedLay.setVisibility(View.GONE);

                    checkUsed = "true";

                }
            }
        });


        //Last Used Date Picker
        lastUsedCalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

               /* inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);*/
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewSkilActivity.this,
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
                                lastUsedTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        // spinner Bind
        if (conn.getConnectivityStatus()>0)
        {
            personalDdlDetails(userId,authcode);
        }else
            {
                conn.showNoInternetAlret();
            }


        //get skill Id
        skillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                skillId = skillList.get(i).getSkillsId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //get proficieancy Id
        proficenacySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                proficieancyId = profyList.get(i).getProficiencyId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //get source Id
        sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sourceId = sourceList.get(i).getSourceId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Add New Skills
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (skillId.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddNewSkilActivity.this, "Please select skills", Toast.LENGTH_SHORT).show();
                }else if (proficieancyId.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddNewSkilActivity.this, "Please select proficiency", Toast.LENGTH_SHORT).show();
                }else if (sourceId.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddNewSkilActivity.this, "Please select source", Toast.LENGTH_SHORT).show();
                }else {

                    if (conn.getConnectivityStatus() > 0) {

                        if (checkUsed.equalsIgnoreCase("true")) {

                            addSkillsDetails(userId, recordId, skillId, proficieancyId, sourceId, getCurrentTime(), authcode, checkUsed);

                        } else {
                            addSkillsDetails(userId, recordId, skillId, proficieancyId, sourceId, lastUsedTxt.getText().toString()
                                    , authcode, checkUsed);
                        }

                    } else {
                        conn.showNoInternetAlret();
                    }
                }
            }
        });

    }

    //bind all spiiner data
    public void personalDdlDetails(final String AdminID, final String AuthCode ) {


        final ProgressDialog pDialog = new ProgressDialog(AddNewSkilActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalDdlDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    //bind Skills List
                    if (skillList.size()>0)
                    {
                        skillList.clear();
                    }
                    skillList.add(new SkillsSpinnerModel("Please Select Skills",""));
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
                        JSONArray skillsObj = jsonObject.getJSONArray("SkillMaster");
                        for (int i =0; i<skillsObj.length(); i++)
                        {
                            JSONObject object = skillsObj.getJSONObject(i);

                            String SkillID = object.getString("SkillID");
                            String SkillName = object.getString("SkillName");

                            skillList.add(new SkillsSpinnerModel(SkillName,SkillID));

                        }

                        //bind Proficeancy
                        if (profyList.size()>0)
                        {
                            profyList.clear();
                        }
                        profyList.add(new ProficiencySpiinerModel("Please Select Proficiency",""));

                        JSONArray proficiencyObj = jsonObject.getJSONArray("ProficiencyMaste");
                        for (int i =0; i<proficiencyObj.length(); i++)
                        {
                            JSONObject object = proficiencyObj.getJSONObject(i);

                            String ProficiencyID = object.getString("ProficiencyID");
                            String ProficiencyName = object.getString("ProficiencyName");

                            profyList.add(new ProficiencySpiinerModel(ProficiencyName,ProficiencyID));

                        }

                        //bind source
                        if (sourceList.size()>0)
                        {
                            sourceList.clear();
                        }
                        sourceList.add(new SourceSpinnerModel("Please Select Proficiency",""));

                        JSONArray sourceObj = jsonObject.getJSONArray("SkillSourceMaster");
                        for (int i =0; i<sourceObj.length(); i++)
                        {
                            JSONObject object = sourceObj.getJSONObject(i);

                            String SkillSourceID = object.getString("SkillSourceID");
                            String SkillSourceName = object.getString("SkillSourceName");

                            sourceList.add(new SourceSpinnerModel(SkillSourceName,SkillSourceID));

                        }


                        for (int k =0; k<skillList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (skillList.get(k).getSkillsName().equalsIgnoreCase(skillNameStr))
                                {
                                    skillSpinner.setSelection(k);

                                    skillId = skillList.get(k).getSkillsId();
                                }
                            }
                        }

                        for (int k =0; k<profyList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (profyList.get(k).getProficiencyName().equalsIgnoreCase(proficeiancyNameStr))
                                {
                                    proficenacySpinner.setSelection(k);

                                    proficieancyId = profyList.get(k).getProficiencyId();
                                }
                            }
                        }

                        for (int k =0; k<sourceList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (sourceList.get(k).getSourceName().equalsIgnoreCase(sourceNameStr))
                                {
                                    sourceSpinner.setSelection(k);
                                    sourceId = sourceList.get(k).getSourceId();
                                }
                            }
                        }


                    }

                   sourceAdapter.notifyDataSetChanged();
                   profyAdapter.notifyDataSetChanged();
                    skillAdapter.notifyDataSetChanged();
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

                params.put("AdminID",AdminID);
                params.put("AuthCode",AuthCode);



                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //Add skill
    public void addSkillsDetails(final String AdminID  , final String RecordID, final String SkillID, final String ProficiencyID,
                                 final String SourceID, final String LastUsed , final String AuthCode, final String CurrentlyUsed)  {

        final ProgressDialog pDialog = new ProgressDialog(AddNewSkilActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, addUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else if (LoginStatus.equalsIgnoreCase("success")) {
                            onBackPressed();
                        }else {
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        }
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

                params.put("AdminID",AdminID);
                params.put("AuthCode",AuthCode);
                params.put("RecordID",RecordID);
                params.put("SkillID",SkillID);
                params.put("ProficiencyID",ProficiencyID);
                params.put("SourceID",SourceID);
                params.put("LastUsed",LastUsed);
                params.put("CurrentlyUsed",CurrentlyUsed);


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
        startActivity(new Intent(AddNewSkilActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddNewSkilActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddNewSkilActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddNewSkilActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddNewSkilActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddNewSkilActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddNewSkilActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddNewSkilActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddNewSkilActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddNewSkilActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
