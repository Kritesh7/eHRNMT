package in.co.cfcs.ehrnmt.Source;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class ConnectionDetector extends Activity
{
    public final static int TYPE_WIFI = 1;
    public final static int TYPE_MOBILE = 2;
    public final static int TYPE_NOT_CONNECTED = 0;
    private Context _context;
    public ConnectionDetector(Context context){
        this._context = context;
    }
    public int getConnectivityStatus()
    {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork && activeNetwork.isConnected())
        {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }
    public String getConnectivityStatusString()
    {
        int conn = getConnectivityStatus();
        String status = null;
        if (conn == ConnectionDetector.TYPE_WIFI)
        {
            status = "Wifi enabled";
        } else if (conn == ConnectionDetector.TYPE_MOBILE)
        {
            status = "Mobile data enabled";
        } else if (conn == ConnectionDetector.TYPE_NOT_CONNECTED)
        {
            status = "Not connected to Internet";
        }
        return status;
    }
    public void showNoInternetAlret(){
        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();
        // Setting Dialog Title
        internet_dialog.setTitle("No Internet");
        // Setting Dialog Message
        internet_dialog.setMessage("Active Internet Connection Required.");
        // Setting OK Button
        internet_dialog.setButton("Setting", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                internet_dialog.dismiss();
                _context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        // Showing Alert Message
        internet_dialog.show();
    }
    public void showCustomDialog(String title, String msg){
        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();
        // Setting Dialog Title
        internet_dialog.setTitle(title);
        // Setting Dialog Message
        internet_dialog.setMessage(msg);
        internet_dialog.setCancelable(false);
        // Setting OK Button
        internet_dialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               /* Intent i=new Intent(_context,Login_Activity.class);
             _context.startActivity(i);*/
                // internet_dialog.dismiss();
            }
        });
        // Showing Alert Message
        internet_dialog.show();
    }
    public void showCustomrenew_diloge(String title, String msg){
        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();
        // Setting Dialog Title
        internet_dialog.setTitle(title);
        // Setting Dialog Message
        internet_dialog.setMessage(msg);
        internet_dialog.setCancelable(false);
        // Setting OK Button
        internet_dialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               /* Intent i=new Intent(_context,MyProfile.class);
                _context.startActivity(i);*/
                // internet_dialog.dismiss();
            }
        });
        // Showing Alert Message
        internet_dialog.show();
    }
    public void showCustomDialogForParsingAndServerError(){
        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();
        // Setting Dialog Title
        internet_dialog.setTitle("Server Error!");
        // Setting Dialog Message
        internet_dialog.setMessage("Please try later");
        // Setting Icon to Dialog
        //internet_dialog.setIcon(R.drawable.tick);
        // Setting OK Button
        internet_dialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                Intent i=new Intent(_context,Login_Activity.class);
//                _context.startActivity(i);
                // internet_dialog.dismiss();
            }
        });
        // Showing Alert Message
        internet_dialog.show();
    }
    public void showAPISuccessDialog(String msg){
        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();
        // Setting Dialog Title
        // internet_dialog.setTitle("Congratulation");
        // Setting Dialog Message
        internet_dialog.setMessage(msg);
        // Setting Icon to Dialog
        //internet_dialog.setIcon(R.drawable.tick);
        // Setting OK Button
        internet_dialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                internet_dialog.dismiss();
            }
        });
        // Showing Alert Message
        internet_dialog.show();
    }
    public void loginotherdevice(String msg){
        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();
        // Setting Dialog Title
        // internet_dialog.setTitle("Congratulation");
        // Setting Dialog Message
        internet_dialog.setMessage(msg);
        // Setting Icon to Dialog
        //internet_dialog.setIcon(R.drawable.tick);
        // Setting OK Button
        internet_dialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
           /* Intent i=new Intent(getApplicationContext(),Login_Activity.class);
                startActivity(i);*/
              /*  Intent i=new Intent(_context,Login_Activity.class);
                _context.startActivity(i);*/
                //internet_dialog.dismiss();
            }
        });
        // Showing Alert Message
        internet_dialog.show();
    }
    public void showAPIFailureDialog(String msg){
        final AlertDialog internet_dialog = new AlertDialog.Builder(
                _context).create();
        // Setting Dialog Title
        internet_dialog.setTitle("Request Failed");
        // Setting Dialog Message
        internet_dialog.setMessage(msg);
        // Setting Icon to Dialog
        //internet_dialog.setIcon(R.drawable.tick);
        // Setting OK Button
        internet_dialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                internet_dialog.dismiss();
            }
        });
        // Showing Alert Message
        internet_dialog.show();
    }
}

