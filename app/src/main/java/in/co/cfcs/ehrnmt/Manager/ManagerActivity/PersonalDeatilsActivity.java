package in.co.cfcs.ehrnmt.Manager.ManagerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Main.HomeActivity;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.EmergencyContactModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class PersonalDeatilsActivity extends AppCompatActivity {

    public TextView titleTxt, nameTxt, fatherNameTxt, dobTxt, genderTxt, prefferdNameTxt, materialStatusTxt, noOfChildTxt, nationloityTxt
            , emailtxt, alternativeEmailTxt, panNoTxt, passportNameTxt, passportNumberTxt, mobileNumberTxt, phoneNumberTxt, personalEmailTxt
            , coprativeEmailTxt, empIdTxt, compNameTxt, zoneNameTxt, designationTxt, departmentTxt, joingDateTxt, managerNameTxt
            , bloodGroupTxt, allergiesTxt, seriousIllnessTxt, familyDrNameTxt, famiilyDrNoTxt;
    public LinearLayout noOfChildLay;
    public String empId = "";
    public ConnectionDetector conn;
    public String personalInfoUrl = SettingConstant.BaseUrl + "AppEmployeeProfile";
    public de.hdodenhof.circleimageview.CircleImageView proImg;
    public String userid = "", authcode = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_deatils);

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

        titleTxt.setText("Personal Information");

        conn = new ConnectionDetector(PersonalDeatilsActivity.this);
        userid =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(PersonalDeatilsActivity.this)));
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(PersonalDeatilsActivity.this)));


        nameTxt = (TextView) findViewById(R.id.proname);
        fatherNameTxt = (TextView) findViewById(R.id.profathername);
        dobTxt = (TextView) findViewById(R.id.prodob);
        genderTxt = (TextView) findViewById(R.id.progender);
        prefferdNameTxt = (TextView) findViewById(R.id.proprefreddname);
        materialStatusTxt = (TextView) findViewById(R.id.promaterialstatus);
        noOfChildTxt = (TextView) findViewById(R.id.pronoofchildern);
        nationloityTxt = (TextView) findViewById(R.id.pronationality);
        emailtxt = (TextView) findViewById(R.id.proemail);
        alternativeEmailTxt = (TextView) findViewById(R.id.proalternativeemail);
        panNoTxt = (TextView) findViewById(R.id.propanno);
        passportNameTxt = (TextView) findViewById(R.id.propassportname);
        passportNumberTxt = (TextView) findViewById(R.id.propassportno);
        mobileNumberTxt = (TextView) findViewById(R.id.promobileno);
        phoneNumberTxt = (TextView) findViewById(R.id.prophoneno);
        personalEmailTxt = (TextView) findViewById(R.id.propersonalemail);
        coprativeEmailTxt = (TextView) findViewById(R.id.procoprateemail);
        compNameTxt = (TextView) findViewById(R.id.procompanyname);
        empIdTxt = (TextView) findViewById(R.id.proempid);
        zoneNameTxt = (TextView) findViewById(R.id.prozonename);
        designationTxt = (TextView) findViewById(R.id.prodesignation);
        departmentTxt = (TextView) findViewById(R.id.prodepartment);
        joingDateTxt = (TextView) findViewById(R.id.projoingdate);
        managerNameTxt = (TextView) findViewById(R.id.promanagername);
        bloodGroupTxt = (TextView) findViewById(R.id.probloodgroup);
        allergiesTxt = (TextView) findViewById(R.id.proallergies);
        seriousIllnessTxt = (TextView) findViewById(R.id.proseriousillness);
        familyDrNameTxt = (TextView) findViewById(R.id.profamilydoctorname);
        famiilyDrNoTxt = (TextView) findViewById(R.id.profamilydoctornumber);
        noOfChildLay = (LinearLayout) findViewById(R.id.noofchildernlay);
        proImg = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.pro_image);


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

       if (conn.getConnectivityStatus()>0)
       {
           personalInfoData(authcode,userid,empId);
       }else
           {
               conn.showNoInternetAlret();
           }
    }

    //Personal Information Data bind
    public void personalInfoData(final String AuthCode , final String AdminID, final String EmployeeID) {

        final ProgressDialog pDialog = new ProgressDialog(PersonalDeatilsActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalInfoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));


                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object.has("status")) {
                            LoginStatus = object.getString("status");
                            msgstatus = object.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                            }
                        }else{

                            String Title = object.getString("Title");
                            String EmployeeName = object.getString("EmployeeName");
                            String EmpID = object.getString("EmpID");
                            String Email = object.getString("Email");
                            String FatherName = object.getString("FatherName");
                            String CompName = object.getString("CompName");
                            String Children = object.getString("Children");
                            String ZoneName = object.getString("ZoneName");
                            String JoiningDate = object.getString("JoiningDate");
                            String DepartmentName = object.getString("DepartmentName");
                            String DesignationName = object.getString("DesignationName");
                            String GenderName = object.getString("GenderName");
                            String MartialStatusName = object.getString("MartialStatusName");
                            String ReportTo = object.getString("ReportTo");
                            String AlternateEmail = object.getString("AlternateEmail");
                            String PAN = object.getString("PAN");
                            String PassportName = object.getString("PassportName");
                            String PassportNo = object.getString("PassportNo");
                            String PreferredName = object.getString("PreferredName");
                            String NationalityName = object.getString("NationalityName");
                            String DOB = object.getString("DOB");
                            String CompPhoto = object.getString("CompPhoto");
                            String EmpPhoto = object.getString("EmpPhoto");
                            String BloodGroupName = object.getString("BloodGroupName");
                            String FamilyDoctorName = object.getString("FamilyDoctorName");
                            String FamilyDoctorNo = object.getString("FamilyDoctorNo");
                            String Allergies = object.getString("Allergies");
                            String Illness = object.getString("Illness");
                            String PhoneNo = object.getString("PhoneNo");
                            String MobileNo = object.getString("MobileNo");
                            String EmailPersonal = object.getString("EmailPersonal");
                            String EmailCorporate = object.getString("EmailCorporate");

                            if (Children.equalsIgnoreCase("0"))
                            {
                                noOfChildLay.setVisibility(View.GONE);
                            }else
                            {
                                noOfChildLay.setVisibility(View.VISIBLE);
                            }

                            nameTxt.setText(Title + " " +EmployeeName);
                            empIdTxt.setText(EmpID);
                            emailtxt.setText(Email);
                            fatherNameTxt.setText(FatherName);
                            compNameTxt.setText(CompName);
                            noOfChildTxt.setText(Children);
                            zoneNameTxt.setText(ZoneName);
                            joingDateTxt.setText(JoiningDate);
                            departmentTxt.setText(DepartmentName);
                            designationTxt.setText(DesignationName);
                            genderTxt.setText(GenderName);
                            materialStatusTxt.setText(MartialStatusName);
                            managerNameTxt.setText(ReportTo);
                            alternativeEmailTxt.setText(AlternateEmail);
                            panNoTxt.setText(PAN);
                            passportNameTxt.setText(PassportName);
                            passportNumberTxt.setText(PassportNo);
                            prefferdNameTxt.setText(PreferredName);
                            nationloityTxt.setText(NationalityName);
                            dobTxt.setText(DOB);
                            bloodGroupTxt.setText(BloodGroupName);
                            familyDrNameTxt.setText(FamilyDoctorName);
                            famiilyDrNoTxt.setText(FamilyDoctorNo);
                            allergiesTxt.setText(Allergies);
                            seriousIllnessTxt.setText(Illness);
                            phoneNumberTxt.setText(PhoneNo);
                            mobileNumberTxt.setText(MobileNo);
                            personalEmailTxt.setText(EmailPersonal);
                            coprativeEmailTxt.setText(EmailCorporate);

                            //set profile Image
                            Picasso pic = Picasso.with(PersonalDeatilsActivity.this);
                            pic.setIndicatorsEnabled(true);
                            pic.with(PersonalDeatilsActivity.this).cancelRequest(proImg);
                            pic.with(PersonalDeatilsActivity.this)
                                    .load(SettingConstant.DownloadUrl + EmpPhoto)
                                    .placeholder(R.drawable.prf)
                                    .error(R.drawable.prf)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .tag(PersonalDeatilsActivity.this)
                                    .into(proImg);



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

                Toast.makeText(PersonalDeatilsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(PersonalDeatilsActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(PersonalDeatilsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(PersonalDeatilsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(PersonalDeatilsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(PersonalDeatilsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(PersonalDeatilsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(PersonalDeatilsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(PersonalDeatilsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(PersonalDeatilsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(PersonalDeatilsActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }


}
