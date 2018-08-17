package in.co.cfcs.ehrnmt.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.cfcs.ehrnmt.Main.AddMedicalandAnssuranceActivity;
import in.co.cfcs.ehrnmt.Model.MedicalAnssuranceModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.CheckForSDCard;
import in.co.cfcs.ehrnmt.Source.DownloadTask;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * Created by Admin on 20-09-2017.
 */

public class MedicalAnssuredAdapter extends RecyclerView.Adapter<MedicalAnssuredAdapter.ViewHolder> {
    public Context context;
    public ArrayList<MedicalAnssuranceModel> list = new ArrayList<>();
    public Activity activity;
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeeMedicalPolicyDelete";
    public String authCode = "";
    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val;
    String[] permissions = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    public String checkNavigateStr = "MedialAnssurance";
    public String userid = "";
    public String checkNaviagte;

    public MedicalAnssuredAdapter(Context context, ArrayList<MedicalAnssuranceModel> list, Activity activity, String checkNaviagte) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.checkNaviagte = checkNaviagte;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.medicalanssurance_item_layout, parent, false);
        return new MedicalAnssuredAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MedicalAnssuranceModel model = list.get(position);

        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userid = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));


        holder.policyTypeTxt.setText(model.getPolicyType());
        holder.policyNumberTxt.setText(model.getPolicyNumber());
        holder.policyDurationTxt.setText(model.getPolicyDuration());
        holder.policyNameTxt.setText(model.getPolicyName());
        holder.amountInsuredTxt.setText(model.getPolicyInsured());
        holder.policyByTxt.setText(model.getPolicyBy());

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, AddMedicalandAnssuranceActivity.class);
                i.putExtra("RecordId", model.getRecordId());
                i.putExtra("Mode", "EditMode");
                i.putExtra("PolicyType", model.getPolicyType());
                i.putExtra("PolicyName", model.getPolicyName());
                i.putExtra("PolicyNumber", model.getPolicyNumber());
                i.putExtra("PolicyDuration", model.getPolicyDuration());
                i.putExtra("PolicyBy", model.getPolicyBy());
                i.putExtra("InsuranceCompany", model.getInsuranceComp());
                i.putExtra("AmountInsured", model.getPolicyInsured());
                i.putExtra("StartDate", model.getStartDate());
                i.putExtra("EndDate", model.getEndDate());
                i.putExtra("File", model.getFileNameText());
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        //delete the list
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSettingsAlert(position, authCode, model.getRecordId(), userid);
            }
        });

        if (model.getFileNameText().equalsIgnoreCase("")) {
            holder.downloadLay.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        } else {
            holder.downloadLay.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
        }

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissions()) {

                    //startDownload(SettingConstant.DownloadUrl + model.getFileNameText());



                  /*  showProgress(SettingConstant.DownloadUrl + model.getFileNameText());

                    new Thread(new Runnable() {
                        public void run() {
                            downloadFile(SettingConstant.DownloadUrl + model.getFileNameText(),model.getFileNameText());
                        }
                    }).start();*/


                    //show online
                  /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SettingConstant.DownloadUrl + model.getFileNameText()));
                    activity.startActivity(browserIntent);
*/
                    new DownloadTask(context, SettingConstant.DownloadUrl + model.getFileNameText(), checkNavigateStr);
                }
            }
        });

        if (checkNaviagte.equalsIgnoreCase("FirstOne")) {
            holder.btnLay.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.VISIBLE);

        } else {
            holder.btnLay.setVisibility(View.GONE);
            holder.view2.setVisibility(View.GONE);
        }
    }


    void downloadFile(String UrlStr, String fileName) {

        Log.e("checking the url is", UrlStr);

        try {
            URL url = new URL(UrlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.e("code:", urlConnection.getResponseCode() + " f");
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            // getting file length
            int lenghtOfFile = urlConnection.getContentLength();

            Log.e("check file lenth", lenghtOfFile + "");

            //If Connection response is not OK then show Logs
          /*  if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("Checking one", "Server returned HTTP " + urlConnection.getResponseCode()
                        + " " + urlConnection.getResponseMessage());

            }*/

            File apkStorage = null;

            //Get File if SD card is present
            if (new CheckForSDCard().isSDCardPresent()) {

                apkStorage = new File(
                        Environment.getExternalStorageDirectory() + "/"
                                + UtilsMethods.downloadDirectory);
            } else
                Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

            //If File is not present create directory
            if (!apkStorage.exists()) {
                apkStorage.mkdir();
                Log.e("Checking one", "Directory Created.");
            }

            //   outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

            //Create New File if not present
          /*  if (!outputFile.exists()) {
                outputFile.createNewFile();
                Log.e(TAG, "File Created");
            }
*/

            /*//set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //create a new file, to save the downloaded file
            File file = new File(SDCardRoot,"HRMS_Docs");

            FileOutputStream fileOutput = new FileOutputStream(file);*/

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();


            activity.runOnUiThread(new Runnable() {
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                // fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float) downloadedSize / totalSize) * 100;
                        cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + (int) per + "%)");
                    }
                });
            }
            //close the output stream when complete //
            //  fileOutput.close();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    // pb.dismiss(); // if you want close it..
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        } catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    void showError(final String err) {
        activity.runOnUiThread(new Runnable() {
            public void run() {

                Toast.makeText(activity, err, Toast.LENGTH_SHORT).show();

                Log.e("Cheking the error", err);
                //Toast.makeText(context, Toast.LENGTH_LONG).show();
            }
        });
    }

    void showProgress(String file_path) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file from ... " + file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();

        pb = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(activity.getResources().getDrawable(R.drawable.green_progress));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView policyTypeTxt, policyNumberTxt, policyDurationTxt, policyNameTxt, amountInsuredTxt, policyByTxt;
        public ImageView delBtn;
        public ImageView mainLay;
        public View view, view2;
        public LinearLayout downloadLay, btnLay;
        public ImageView downloadBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            policyTypeTxt = (TextView) itemView.findViewById(R.id.policytype);
            policyNumberTxt = (TextView) itemView.findViewById(R.id.policynumber);
            policyDurationTxt = (TextView) itemView.findViewById(R.id.policy_duration);
            policyNameTxt = (TextView) itemView.findViewById(R.id.policyname);
            amountInsuredTxt = (TextView) itemView.findViewById(R.id.amountinsured);
            policyByTxt = (TextView) itemView.findViewById(R.id.policyby);
            delBtn = (ImageView) itemView.findViewById(R.id.delbtn);
            mainLay = (ImageView) itemView.findViewById(R.id.main_lay);
            downloadLay = (LinearLayout) itemView.findViewById(R.id.downloadOptionLay);
            view = (View) itemView.findViewById(R.id.view);
            downloadBtn = (ImageView) itemView.findViewById(R.id.downloadOptionBtn);
            btnLay = (LinearLayout) itemView.findViewById(R.id.btnlay);
            view2 = (View) itemView.findViewById(R.id.view2);

        }
    }


    public void remove(int position) {
        if (position < 0 || position >= list.size()) {
            return;
        }
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void showSettingsAlert(final int postion, final String authcode, final String recordId, final String userId) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        //  alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("Are You Sure You Want to Delete?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                deleteMethod(authcode, recordId, postion, userId);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    //delete the Details
    public void deleteMethod(final String AuthCode, final String RecordID, final int postion, final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    if (jsonObject.has("status")) {
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("success")) {

                            remove(postion);
                            Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode", AuthCode);
                params.put("AdminID", AdminID);
                params.put("RecordID", RecordID);

                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

}
