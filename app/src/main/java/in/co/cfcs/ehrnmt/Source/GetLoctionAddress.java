package in.co.cfcs.ehrnmt.Source;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetLoctionAddress {

    public static List<Address> getFromLocation(double lat, double lng, final Context context, final Handler handler){

        String address = String.format(Locale.ENGLISH,"https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyBGYX4XPMyZg7D7LIcdCeWHrqXLL7MdTlc&latlng=%1$f,%2$f&sensor=true&language="+ Locale.getDefault().getCountry(), lat, lng);
      //  https://maps.googleapis.com/maps/api/geocode/json?key=AbCdEfGhIjKlMnOpQrStUvWxYz&latlng=%1$f,%2$f&address=Dallas&sensor=true&language=




        HttpGet httpGet = new HttpGet(address);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        List<Address> retList = null;

        String result = null;

        Address addr;

        String indiStr=null;

        try {

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                response = client.execute(httpGet);

                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }

            }


            JSONObject jsonObject = new JSONObject();
            jsonObject = new JSONObject(stringBuilder.toString());


            retList = new ArrayList<Address>();


            if("OK".equalsIgnoreCase(jsonObject.getString("status"))){
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i=0;i<1;i++ ) {
                    JSONObject result1 = results.getJSONObject(i);
                     indiStr = result1.getString("formatted_address");
                    addr = new Address(Locale.getDefault());
                    addr.setAddressLine(0, indiStr);
                    retList.add(addr);

                }
            }

        } catch (ClientProtocolException e) {
        //    Log.e(MyGeocoder.class.getName(), "Error calling Google geocode webservice.", e);
        } catch (IOException e) {
        //    Log.e(MyGeocoder.class.getName(), "Error calling Google geocode webservice.", e);
        } catch (JSONException e) {
        //    Log.e(MyGeocoder.class.getName(), "Error parsing Google geocode webservice response.", e);
        } finally {
            Message message = Message.obtain();
            message.setTarget(handler);
            if (indiStr != null) {
                message.what = 1;
                Bundle bundle = new Bundle();
                indiStr =  indiStr;
                bundle.putString("address", indiStr);
                message.setData(bundle);

            } else {
                message.what = 1;
                Bundle bundle = new Bundle();
                indiStr = "";
                bundle.putString("address", indiStr);
                message.setData(bundle);
            }
            message.sendToTarget();
        }

        return retList;
    }
}