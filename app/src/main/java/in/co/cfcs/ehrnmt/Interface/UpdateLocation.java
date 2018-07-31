package in.co.cfcs.ehrnmt.Interface;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class UpdateLocation extends AsyncTask<String,String,String> {

    public String addGeoFencingUrl = SettingConstant.BaseGeoUrl + "GEOTrackingInsUpdt";


    double lat;
    double longi;

    String AdminID;
    String AuthCode;

    Context mContext;

    int Counter = 0;

    ArrayList<LocationDataModel> loclist = new ArrayList<LocationDataModel>();

    String LatLngJson;

    public UpdateLocation(ArrayList<LocationDataModel> loglist, Context baseContext) {
        this.mContext = baseContext;
        this.loclist = loglist;

    }

    @Override
    protected String doInBackground(String... strings) {


        AdminID = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(mContext)));
        AuthCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(mContext)));


        if(loclist.size() >0){

            makeJsonLocation();
        }

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, addGeoFencingUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    if (jsonObject.has("status")) {
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("success")) {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                        }
                    }

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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("AdminID", AdminID);
                params.put("AuthCode", AuthCode);
                params.put("LocationJson", LatLngJson);
                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

        return null;
    }


    private void makeJsonLocation() {


        try {
            Gson gson = new Gson();
            JSONObject jsonObj = new JSONObject();
            JSONArray array = new JSONArray();
            for (int i = 0; i < loclist.size(); i++) {
                String Lat = loclist.get(i).getLat();
                String Lng = loclist.get(i).getLng();
                String WaitCounter = loclist.get(i).getWaitCounter();
                String ReportTime = loclist.get(i).getReportTime();
                LocationdataModelTemp diary = getImageObjectFilled(Lat,Lng,WaitCounter,ReportTime);
                String case_json1 = gson.toJson(loclist);
                String case_json = gson.toJson(diary);
                JSONObject objImg = new JSONObject(case_json);
                array.put(objImg);
//                jsonObj.put("members", array);
                //Log.e("make json size is ", array+" null");

            }
            Log.e("make json size is ", " cfcs " + jsonObj.toString());
            LatLngJson = array.toString();
            loclist.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private LocationdataModelTemp getImageObjectFilled(String lat, String lng, String waitCounter, String ReportTime) {
        LocationdataModelTemp bean = new LocationdataModelTemp();
        bean.setLat(lat);
        bean.setLng(lng);
        bean.setWaitCounter(waitCounter);
        bean.setReportTime(ReportTime);
        return bean;

    }

}
