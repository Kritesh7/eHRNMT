package in.co.cfcs.ehrnmt.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Map;

import in.co.cfcs.ehrnmt.Main.AddPreviousExpreinceActivity;
import in.co.cfcs.ehrnmt.Model.PreviousExpreinceModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * Created by Admin on 21-09-2017.
 */

public class PreviousExpreinceAdapter extends RecyclerView.Adapter<PreviousExpreinceAdapter.ViewHolder>
{

    public Context context;
    public ArrayList<PreviousExpreinceModel> list = new ArrayList<>();
    public Activity activity;
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeePreviousExperienceDelete";
    public String authCode = "";
    public String checkNavigate;

    public PreviousExpreinceAdapter(Context context, ArrayList<PreviousExpreinceModel> list, Activity activity, String checkNavigate) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.checkNavigate = checkNavigate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.previousexprience_item_layout, parent, false);
        return new PreviousExpreinceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        PreviousExpreinceModel model = list.get(position);

        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));


        holder.companyNametxt.setText(model.getCompanyName());
        holder.joingDateTxt.setText(model.getJoiningDate());
        holder.jobDescTxt.setText(model.getJobDescription());
        holder.jobPeriodeTxt.setText(model.getJobPeriode());
        holder.designationTxt.setText(model.getDesignation());
        holder.statusTxt.setText(model.getStatus());
        holder.releivingdate.setText(model.getRelievingDate());

        if (model.getComments().equalsIgnoreCase(""))
        {
            holder.commLay.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }else
            {
                holder.commTxt.setText(model.getComments());
                holder.commLay.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.VISIBLE);
            }



        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getEditable().equalsIgnoreCase("1"))
                {
                    Intent i = new Intent(context, AddPreviousExpreinceActivity.class);
                    i.putExtra("ActionMode", "EditMode");
                    i.putExtra("RecordId", model.getRecordID());
                    i.putExtra("CompanyName", model.getCompanyName());
                    i.putExtra("JoiningDate",model.getJoiningDate());
                    i.putExtra("RelivingDate",model.getRelievingDate());
                    i.putExtra("Designation",model.getDesignation());
                    i.putExtra("JobYearId",model.getJobYearId());
                    i.putExtra("JobMonthId",model.getJobMonthId());
                    i.putExtra("JobDescription",model.getJobDescription());
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }else
                    {
                        //Toast.makeText(context, "Your Previous request waiting for Hr approval.", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                        // Setting Dialog Title
                        //  alertDialog.setTitle("Alert");

                        // Setting Dialog Message
                        alertDialog.setMessage("Your Previous request waiting for Hr approval.");

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

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getDeleteable().equalsIgnoreCase("1"))
                {
                    showSettingsAlert(position,authCode,model.getRecordID());
                }else
                    {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                        // Setting Dialog Title
                        //  alertDialog.setTitle("Alert");

                        // Setting Dialog Message
                        alertDialog.setMessage("Hr is approve, this is not deletable .");

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

        if (checkNavigate.equalsIgnoreCase("FirstOne"))
        {
            holder.view2.setVisibility(View.VISIBLE);
            holder.btnLay.setVisibility(View.VISIBLE);
        }else
            {
                holder.view2.setVisibility(View.GONE);
                holder.btnLay.setVisibility(View.GONE);
            }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView companyNametxt,joingDateTxt,jobDescTxt,jobPeriodeTxt, designationTxt, statusTxt, commTxt,releivingdate;
        public ImageView editBtn, delBtn;
        public LinearLayout commLay, btnLay;
        public View view, view2;

        public ViewHolder(View itemView) {
            super(itemView);

            companyNametxt = (TextView)itemView.findViewById(R.id.companyname);
            joingDateTxt = (TextView)itemView.findViewById(R.id.joiningdate);
            jobDescTxt = (TextView)itemView.findViewById(R.id.job_description);
            jobPeriodeTxt = (TextView)itemView.findViewById(R.id.Job_period);
            designationTxt = (TextView)itemView.findViewById(R.id.designation);
            editBtn = (ImageView) itemView.findViewById(R.id.editBtn);
            delBtn = (ImageView) itemView.findViewById(R.id.delbtn);
            statusTxt = (TextView) itemView.findViewById(R.id.status);
            commLay = (LinearLayout) itemView.findViewById(R.id.commLay);
            commTxt = (TextView) itemView.findViewById(R.id.comment);
            view = (View) itemView.findViewById(R.id.view);
            releivingdate = (TextView) itemView.findViewById(R.id.relivingdatetxt);
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

    public void showSettingsAlert(final int postion, final String authcode, final String recordId) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        //  alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("Are You Sure You Want to Delete?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                deleteMethod(authcode,recordId, postion);
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
    public void deleteMethod(final String AuthCode , final String RecordID, final int  postion) {

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
