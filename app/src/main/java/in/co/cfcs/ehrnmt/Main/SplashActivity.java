package in.co.cfcs.ehrnmt.Main;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
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
import in.co.cfcs.ehrnmt.Source.GPSTracker;
import in.co.cfcs.ehrnmt.Source.NotificationBroadCast;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    public ConnectionDetector conn;
    public GPSTracker gps;
    public String loginStatus = "";
    private static final int REQUEST_WRITE_PERMISSION = 20;
    public String loginStatusUrl = SettingConstant.BASEURL_FOR_LOGIN + "AppLoginStatusCheck";
    public String logoutUrl = SettingConstant.BASEURL_FOR_LOGIN + "AppLoginLogOut";
    public String userid = "", authcode = "";
    //public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        conn = new ConnectionDetector(SplashActivity.this);
        userid = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(SplashActivity.this)));
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(SplashActivity.this)));
        gps = new GPSTracker(SplashActivity.this, SplashActivity.this);
        loginStatus = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getStatus(SplashActivity.this)));

        if (authcode.equalsIgnoreCase("null")) {
            authcode = "";
        }

        if (userid.equalsIgnoreCase("null")) {
            userid = "";
        }

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, REQUEST_WRITE_PERMISSION);
        }else
            {

                checkGPS();
            }*/


       /* progressDialog = ProgressDialog.show(SplashActivity.this,"Loading...",
                "Loading application View, please wait...", false, false);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, REQUEST_WRITE_PERMISSION);
        } else {

            checkGPS();
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(getApplicationContext(), SplashActivity.class);
        startActivity(i);
        finish();
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE
            }, REQUEST_WRITE_PERMISSION);
        }else
        {

            checkGPS();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkGPS() {

        if (conn.getConnectivityStatus() > 0) {
            if (gps.canGetLocation()) {

                if (userid.equalsIgnoreCase("") && authcode.equalsIgnoreCase("")) {

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);


                } else {

                    getLoginStatus(authcode, userid);
                }


            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();

            }
        } else {
            conn.showNoInternetAlret();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case REQUEST_WRITE_PERMISSION:
                checkGPS();
        }

    }


    //Status Check Count  API
    public void getLoginStatus(final String AuthCode, final String AdminID) {

       /* final ProgressDialog pDialog = new ProgressDialog(SplashActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();
*/
        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, loginStatusUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String LoginCount = jsonObject.getString("LoginCount");

                        if (LoginCount.equalsIgnoreCase("1") || LoginCount.equalsIgnoreCase("-1")) {

                            if (loginStatus.equalsIgnoreCase("1")) {

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                                        finish();
                                    }
                                }, SPLASH_TIME_OUT);


                            } else {


                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                                        finish();
                                    }
                                }, SPLASH_TIME_OUT);


                            }

                        } else {
                            singleButton(SplashActivity.this, AdminID, authcode);
                        }

                    }
                    //    pDialog.dismiss();

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

                Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode", AuthCode);
                params.put("AdminID", AdminID);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //alert dialoge signle Button
    public AlertDialog singleButton(Context context, String userid, String authcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("Alert")
                .setIcon(R.mipmap.logo)
                .setCancelable(false)
                .setMessage("Session Expired please login again!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                        getLogout(userid, authcode, dialog);

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    //Log Out API Work
    public void getLogout(final String AdminID, final String AuthCode, final DialogInterface dialog) {

      /*  final ProgressDialog pDialog = new ProgressDialog(SplashActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();*/

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, logoutUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("success")) {
                            // navigationItemIndex = 22;

                            Intent ik = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(ik);
                            overridePendingTransition(R.anim.push_left_in,
                                    R.anim.push_right_out);
                            finish();

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(SplashActivity.this,
                                    "")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(SplashActivity.this,
                                    "")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(SplashActivity.this,
                                    "")));

                            //cancel background services
                            cancelAlarm();

                            dialog.dismiss();


                        } else {

                            String MsgNotification = jsonObject.getString("MsgNotification");
                            Toast.makeText(SplashActivity.this, MsgNotification, Toast.LENGTH_SHORT).show();


                            Intent ik = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(ik);
                            overridePendingTransition(R.anim.push_left_in,
                                    R.anim.push_right_out);
                            finish();

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(SplashActivity.this,
                                    "")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(SplashActivity.this,
                                    "")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(SplashActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(SplashActivity.this,
                                    "")));
                            //cancel background services
                            cancelAlarm();
                        }

                    }

                    //   pDialog.dismiss();

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

                Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                //   pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AdminID", AdminID);
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

    //cancel Notification
    public void cancelAlarm() {
        Intent intent = new Intent(this, NotificationBroadCast.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        //Toast.makeText(this, "Cancelled alarm", Toast.LENGTH_SHORT).show();
    }
}
