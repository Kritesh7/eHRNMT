package in.co.cfcs.ehrnmt.Source;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by Admin on 09-03-2017.
 */
public class UtilsMethods
{
    private static String imeiNo, mobNo;
    public static final String downloadDirectory = "HRMS Attachment";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static String getBlankIfStringNull(String toCheck)
    {
        if (toCheck != null)
            return toCheck;
        else
            return "";
    }
    public static boolean isStringNullOrBlank(String toCheck)
    {
        if (toCheck != null && toCheck.trim().length() > 0)
            return false;
        else
            return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
  /*  public static String getIMEINo(Context context)
    {
        if (getBlankIfStringNull(imeiNo).length() > 0)
            return imeiNo;
        else
        {
            imeiNo = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                    .getDeviceId();
            return imeiNo != null ? imeiNo : (imeiNo = Settings.Secure.getString(context
                    .getContentResolver(), Settings.Secure.ANDROID_ID)) != null ? imeiNo : "0";//
            // default 0 if not found
        }
    }*/
    /*public static String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date()); // Find todays date
            return currentTimeStamp;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }*/
   /* public static String getMobileNo(Context context)
    {
        if (getBlankIfStringNull(mobNo).length() > 0)
            return mobNo;
        else
        {
            mobNo = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                    .getLine1Number();
            ;
            return mobNo != null ? mobNo : "0";// default 0 if not found
        }
    }*/
   /* public static void showAlertMsg(Context context)
    {
        //getString(R.string.no_internet_title), getString(R.string.no_internet), context
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Oops!");
            builder.setCancelable(false);
            builder.setMessage("Internet connectivity Break, Try again!!");
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
            builder.create();
            builder.show();
        } catch (Exception e)
        {
            Log.e("Utilsmethods", " in alert show exception ");
            e.printStackTrace();
        }
    }*/
   /* public static void showAlertMsg1(Context context, String title, String msg)
    {
        //getString(R.string.no_internet_title), getString(R.string.no_internet), context
        try
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setCancelable(false);
            builder.setMessage(msg);
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
            builder.create();
            builder.show();
        } catch (Exception e)
        {
            Log.e("Utilsmethods", " in alert show exception ");
            e.printStackTrace();
        }
    }*/
}