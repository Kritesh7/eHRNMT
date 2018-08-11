package in.co.cfcs.ehrnmt.Main;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Model.LangaugaeSpinnerModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddNewLnaguageActivity extends AppCompatActivity {

    public TextView titleTxt;
    public Spinner langageSpinner;
    public ArrayList<LangaugaeSpinnerModel> langageList = new ArrayList<>();
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeeSkillANDLanguage";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeLanguageInsUpdt";
    public ArrayAdapter<LangaugaeSpinnerModel> langageTypeAdapter;
    public ConnectionDetector conn;
    public String authcode = "", userId = "";
    public Button addBtn;
    public CheckBox readBtn, writeBtn, speakBtn;
    public String readLang = "false", writeLang = "false" , spealLang = "false", langaigeId = "", actionMode = "", langageNameStr = ""
            ,readStr = "", writeStr = "", speakStr = "", recordId = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_lnaguage);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.newlangaugaetollbar);
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

        titleTxt.setText("Add New Language");

        Intent intent = getIntent();
        if (intent != null)
        {
            actionMode = intent.getStringExtra("ActionMode");
            recordId = intent.getStringExtra("RecordId");
            langageNameStr = intent.getStringExtra("LangageName");
            readStr = intent.getStringExtra("Read");
            writeStr = intent.getStringExtra("Write");
            speakStr = intent.getStringExtra("Speak");
        }

        conn = new ConnectionDetector(AddNewLnaguageActivity.this);
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddNewLnaguageActivity.this)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddNewLnaguageActivity.this)));


        langageSpinner = (Spinner)findViewById(R.id.langaugaespinner);
        //langaugeRadioGroup = (RadioGroup) findViewById(R.id.langaugeRadioGroup);
        readBtn = (CheckBox) findViewById(R.id.readbtn);
        writeBtn = (CheckBox) findViewById(R.id.writebtn);
        speakBtn = (CheckBox) findViewById(R.id.speakbtn);
        addBtn = (Button) findViewById(R.id.newrequestbtn);

        if (actionMode.equalsIgnoreCase("EditMode"))
        {
            if (readStr.equalsIgnoreCase("true"))
            {
                readBtn.setChecked(true);
                readLang = "true";
            }

            if (writeStr.equalsIgnoreCase("true"))
            {
                writeBtn.setChecked(true);
                writeLang = "true";
            }

            if (speakStr.equalsIgnoreCase("true"))
            {
                speakBtn.setChecked(true);
                spealLang = "true";
            }

            addBtn.setText("Update Language");
            titleTxt.setText("Update Language");
        }


        //Langage Spinner List Spinner
        //change spinner arrow color
        langageSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        langageTypeAdapter = new ArrayAdapter<LangaugaeSpinnerModel>(AddNewLnaguageActivity.this, R.layout.customizespinner,
                langageList);
        langageTypeAdapter.setDropDownViewResource(R.layout.customizespinner);
        langageSpinner.setAdapter(langageTypeAdapter);

        //bind spinner
        if (conn.getConnectivityStatus()>0)
        {
            personalDdlDetails(userId,authcode);
        }else
            {
                conn.showNoInternetAlret();
            }



        //get langage Type
        readBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                {
                    readLang = "true";
                }else
                    {
                        readLang = "false";
                    }
            }
        });

        writeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                {
                    writeLang = "true";
                }else
                    {
                        writeLang = "false";
                    }
            }
        });

        speakBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                {
                    spealLang = "true";
                }else
                    {
                        spealLang = "false";
                    }
            }
        });

        //get lanage id
        langageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                langaigeId = langageList.get(i).getLangaugeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (langaigeId.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddNewLnaguageActivity.this, "Please select Language", Toast.LENGTH_SHORT).show();
                }else if (readLang.equalsIgnoreCase("false") && writeLang.equalsIgnoreCase("false") &&
                          spealLang.equalsIgnoreCase("false"))
                {
                    Toast.makeText(AddNewLnaguageActivity.this, "Please select Language Type", Toast.LENGTH_SHORT).show();
                }else {

                    if (conn.getConnectivityStatus()>0) {
                        addSkillsDetails(userId, recordId, langaigeId, writeLang, readLang, spealLang, authcode);
                    }else
                        {
                            conn.showNoInternetAlret();
                        }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }

    //bind all spiiner data
    public void personalDdlDetails(final String AdminID, final String AuthCode ) {


        final ProgressDialog pDialog = new ProgressDialog(AddNewLnaguageActivity.this,R.style.AppCompatAlertDialogStyle);
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
                    if (langageList.size()>0)
                    {
                        langageList.clear();
                    }
                    langageList.add(new LangaugaeSpinnerModel("Please Select Language",""));

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
                        JSONArray languageObj = jsonObject.getJSONArray("LanguageMaster");
                        for (int i =0; i<languageObj.length(); i++)
                        {
                            JSONObject object = languageObj.getJSONObject(i);

                            String LanguageID = object.getString("LanguageID");
                            String LanguageName = object.getString("LanguageName");

                            langageList.add(new LangaugaeSpinnerModel(LanguageName,LanguageID));

                        }




                        //Edit Mode
                        for (int k =0; k<langageList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (langageList.get(k).getLangaugeName().equalsIgnoreCase(langageNameStr))
                                {
                                    langageSpinner.setSelection(k);
                                    langaigeId = langageList.get(k).getLangaugeId();
                                }
                            }
                        }

                    }


                    langageTypeAdapter.notifyDataSetChanged();
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
    public void addSkillsDetails(final String AdminID  , final String RecordID, final String LanguageID, final String Write,
                                 final String Read, final String Speak , final String AuthCode)  {

        final ProgressDialog pDialog = new ProgressDialog(AddNewLnaguageActivity.this,R.style.AppCompatAlertDialogStyle);
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
                        } else  if (LoginStatus.equalsIgnoreCase("success")){
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
                params.put("LanguageID",LanguageID);
                params.put("Read",Read);
                params.put("Write",Write);
                params.put("Speak",Speak);

                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }
    private void Logout() {


        finishAffinity();
        startActivity(new Intent(AddNewLnaguageActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddNewLnaguageActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddNewLnaguageActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddNewLnaguageActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddNewLnaguageActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddNewLnaguageActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddNewLnaguageActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddNewLnaguageActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddNewLnaguageActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddNewLnaguageActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

}
