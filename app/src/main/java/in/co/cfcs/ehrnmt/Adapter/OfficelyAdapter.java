package in.co.cfcs.ehrnmt.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.cfcs.ehrnmt.Model.OfficealyModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.DownloadTask;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * Created by Admin on 20-09-2017.
 */

public class OfficelyAdapter extends RecyclerView.Adapter<OfficelyAdapter.ViewHolder>
{

    public Context context;
    public ArrayList<OfficealyModel> list = new ArrayList<>();
    String[] permissions = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeeOfficeDocumentDelete";
    public String authCode = "", userId = "";
    public Activity activity;
    public String checkNavigateStr = "OfficealDocs";
    public String checkNavigate ;

    public OfficelyAdapter(Context context, ArrayList<OfficealyModel> list, Activity activity, String checkNavigate) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.checkNavigate = checkNavigate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.officely_item_layout, parent, false);
        return new OfficelyAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OfficealyModel model = list.get(position);


        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));


        holder.documentTypeTxt.setText(model.getDocumentType());
        holder.noOfDocumentTxt.setText(model.getNoOfDocuments());
        holder.issuesDateTxt.setText(model.getIssuesDate());
        holder.expiryDateTxt.setText(model.getExpiryDate());
        holder.placeOfIssuesTxt.setText(model.getPlaceOfIssues());

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!model.getFileNameText().equalsIgnoreCase(""))
                {
                    if (checkPermissions()) {

                        new DownloadTask(context, SettingConstant.DownloadUrl + model.getFileNameText(),checkNavigateStr);
                    }

                }
            }

        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getDeleteable().equalsIgnoreCase("1"))
                {
                    showSettingsAlert(position,authCode,model.getRecordID(),userId);
                }else
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    // Setting Dialog Title
                    //  alertDialog.setTitle("Alert");

                    // Setting Dialog Message
                    alertDialog.setMessage("We don't permission to delete this document");

                    // On pressing the Settings button.
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                            //deleteMethod(authcode,recordId, postion);
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            }
        });

       holder.navigateBtnDownload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (!model.getFileNameText().equalsIgnoreCase(""))
               {
                   if (checkPermissions()) {

                       new DownloadTask(context, SettingConstant.DownloadUrl + model.getFileNameText(),checkNavigateStr);
                   }

               }
           }
       });

       if (checkNavigate.equalsIgnoreCase("FirstOne"))
       {
           holder.attcahLay.setVisibility(View.GONE);
           holder.view.setVisibility(View.GONE);
           holder.withOutLay.setVisibility(View.VISIBLE);
           holder.view2.setVisibility(View.VISIBLE);
       }else
           {
               holder.attcahLay.setVisibility(View.VISIBLE);
               holder.view.setVisibility(View.VISIBLE);
               holder.withOutLay.setVisibility(View.GONE);
               holder.view2.setVisibility(View.GONE);
           }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView documentTypeTxt,noOfDocumentTxt,issuesDateTxt,expiryDateTxt, placeOfIssuesTxt;
        public ImageView downloadBtn,deleteBtn,navigateBtnDownload;
        public CardView mainLay;
        public LinearLayout attcahLay, withOutLay;
        public View view, view2;

        public ViewHolder(View itemView) {
            super(itemView);

            documentTypeTxt = (TextView)itemView.findViewById(R.id.document_type);
            noOfDocumentTxt = (TextView)itemView.findViewById(R.id.no_document);
            issuesDateTxt = (TextView)itemView.findViewById(R.id.officely_issue_date);
            expiryDateTxt = (TextView)itemView.findViewById(R.id.officely_expiry_date);
            placeOfIssuesTxt = (TextView)itemView.findViewById(R.id.placeof_issues);
            downloadBtn = (ImageView) itemView.findViewById(R.id.downloadOptionBtn);
            deleteBtn = (ImageView) itemView.findViewById(R.id.delbtn);
            navigateBtnDownload = (ImageView) itemView.findViewById(R.id.navigatebtn);
            attcahLay = (LinearLayout) itemView.findViewById(R.id.attchmentlay);
            view = (View)itemView.findViewById(R.id.attcahmnetview);
            withOutLay = (LinearLayout) itemView.findViewById(R.id.outattchalay);
            view2 = (View) itemView.findViewById(R.id.view3);


           // mainLay = (CardView)itemView.findViewById(R.id.leave_management_main_lay);

        }
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

    public void remove(int position) {
        if (position < 0 || position >= list.size()) {
            return;
        }
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void showSettingsAlert(final int postion, final String authcode, final String recordId, final String userid) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        //  alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("Are You Sure You Want to Delete?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                deleteMethod(authcode,recordId, postion,userid);
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
    public void deleteMethod(final String AuthCode , final String RecordID, final int  postion, final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, deleteUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    if (jsonObject.has("status"))
                    {
                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("success"))
                        {

                            remove(postion);
                            Toast.makeText(context, "Delete successfully", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("RecordID",RecordID);
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
}
