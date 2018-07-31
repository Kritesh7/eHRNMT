package in.co.cfcs.ehrnmt.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class LoginActivity extends AppCompatActivity {

    public Button loginBtn;
    public Context mContext;
    public ConnectionDetector conn;
    public String loginUrl = SettingConstant.BASEURL_FOR_LOGIN + "AppUserLogin";
    public EditText userNameTxt, passwordTxt;
    public TextView forgotBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        loginBtn = (Button) findViewById(R.id.loginbtn);
        userNameTxt = (EditText) findViewById(R.id.usernmaetxt);
        passwordTxt = (EditText) findViewById(R.id.passwordtxt);
        forgotBtn = (TextView) findViewById(R.id.forgetpass);
        mContext = LoginActivity.this;
        conn = new ConnectionDetector(LoginActivity.this);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String AuthCode = "";
                // Device model
                String PhoneModel = android.os.Build.MODEL;

                // Android version
                String AndroidVersion = android.os.Build.VERSION.RELEASE;

                long randomNumber = (long) ((Math.random() * 9000000) + 1000000);
                AuthCode = String.valueOf(randomNumber);


                if (userNameTxt.getText().toString().equalsIgnoreCase("")) {
                    userNameTxt.setError("Please enter valid user name");
                    userNameTxt.requestFocus();

                } else if (passwordTxt.getText().toString().equalsIgnoreCase("")) {

                    passwordTxt.setError("Please enter valid password");
                    passwordTxt.requestFocus();

                } else {

                    if (conn.getConnectivityStatus() > 0) {

                        Login_Api(userNameTxt.getText().toString(), passwordTxt.getText().toString(), AuthCode, PhoneModel, AndroidVersion);

                    } else {
                        conn.showNoInternetAlret();
                    }
                }

                // checkGPS();
            }
        });

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(getApplicationContext(), ForGotPasswordActivity.class);
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);


            }
        });

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

    }


    public void Login_Api(final String emailId, final String Password, final String AuthCode,
                          final String ClientName, final String ClientVersion) {


        final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // String status = jsonObject.getString("status");
                        if (jsonObject.has("MsgNotification")) {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            Toast.makeText(LoginActivity.this, MsgNotification, Toast.LENGTH_SHORT).show();

                        } else {

                            String AdminID = jsonObject.getString("AdminID");
                            String UserName = jsonObject.getString("UserName");
                            String Type = jsonObject.getString("Type");
                            String MgrID = jsonObject.getString("MgrID");
                            String CompID = jsonObject.getString("CompID");
                            String EmpID = jsonObject.getString("EmpID");
                            String EmployeePhoto = jsonObject.getString("EmployeePhoto");
                            String DesignationName = jsonObject.getString("DesignationName");
                            String CompanyLogo = jsonObject.getString("CompanyLogo");


                            Intent ik = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(ik);
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                            finish();


                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(LoginActivity.this,
                                    "1")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(LoginActivity.this,
                                    AdminID)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(LoginActivity.this,
                                    AuthCode)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(LoginActivity.this,
                                    emailId)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(LoginActivity.this,
                                    UserName)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserType(LoginActivity.this,
                                    Type)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setMgrDir(LoginActivity.this,
                                    MgrID)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyId(LoginActivity.this,
                                    CompID)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(LoginActivity.this,
                                    EmpID)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(LoginActivity.this,
                                    EmployeePhoto)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(LoginActivity.this,
                                    DesignationName)));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(LoginActivity.this,
                                    CompanyLogo)));
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

            //    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", emailId);
                params.put("Password", Password);
                params.put("AuthCode", AuthCode);
                params.put("BrandName", ClientName);
                params.put("ClientVersion", ClientVersion);

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
        finish();
        super.onBackPressed();

    }
}
