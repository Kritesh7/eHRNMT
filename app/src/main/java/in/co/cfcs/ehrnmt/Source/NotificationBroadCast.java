package in.co.cfcs.ehrnmt.Source;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
//import android.support.v7.app.NotificationCompat;
import android.util.Log;

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

import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerRequestToApproveActivity;
import in.co.cfcs.ehrnmt.R;



public class NotificationBroadCast extends BroadcastReceiver {

    public String countUrl = SettingConstant.BaseUrl + "AppManagerRequestToApproveDashBoard";
    public String userId = "",authcode = "";
    public PendingIntent contentIntent;
    public int count ;
    public ConnectionDetector conn;

    @Override
    public void onReceive(Context context, Intent intent) {

        conn = new ConnectionDetector(context);
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));

        if (conn.getConnectivityStatus() > 0){

            getCount(authcode,userId, context);
        }
    }

    public void getCount(final String AuthCode , final String AdminID, final Context context) {

       /* final ProgressDialog pDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();*/

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, countUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String LeaveCount = jsonObject.getString("LeaveCount");
                        String CancelLeaveCount = jsonObject.getString("CancelLeaveCount");
                        String ShortLeaveCount = jsonObject.getString("ShortLeaveCount");
                        String ShortCancelLeaveCount = jsonObject.getString("ShortCancelLeaveCount");
                        String TrainingCount = jsonObject.getString("TrainingCount");


                        count = Integer.parseInt(LeaveCount) + Integer.parseInt(CancelLeaveCount) + Integer.parseInt(ShortLeaveCount) +
                                Integer.parseInt(ShortCancelLeaveCount) +  Integer.parseInt(TrainingCount);

                        Log.e("count is ", count + "");


                        if (count > 0)
                        {
                            generateNotification(context,count +" new request(s) pending in HRMS");
                        }else
                        {

                        }


                    }
                   // pDialog.dismiss();

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

              //  Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();

                //  pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("AdminID",AdminID);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    private void generateNotification(Context context, String message) {

        int icon = R.drawable.logowhite;
        int id = (int) System.currentTimeMillis();
        long when = System.currentTimeMillis();
        String appname = context.getResources().getString(R.string.app_name);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Notification notification;



            Intent intent=new Intent(context, ManagerRequestToApproveActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            contentIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        // To support 2.3 os, we use "Notification" class and 3.0+ os will use
        // "NotificationCompat.Builder" class.
        if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
            notification = new Notification(icon, message, 0);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notification);



            //sound light and viberation
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context);
            notification = builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.drawable.so).setTicker(appname).setWhen(0)
                    .setSound(alarmSound)
                    .setAutoCancel(true).setContentTitle(appname)
                    .setContentText(message).build();

            //sound light and viberation
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;

            notificationManager.notify(0, builder.build());

        }

    }



}
