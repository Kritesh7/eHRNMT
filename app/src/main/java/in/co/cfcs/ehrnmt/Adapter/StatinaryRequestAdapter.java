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

import in.co.cfcs.ehrnmt.Main.ViewRequestDetailsActivity;
import in.co.cfcs.ehrnmt.Model.StationaryRequestModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * Created by Admin on 18-09-2017.
 */

public class StatinaryRequestAdapter extends RecyclerView.Adapter<StatinaryRequestAdapter.ViewHolder>
{

    public Context context;
    public ArrayList<StationaryRequestModel> list = new ArrayList<>();
    public Activity activity;
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeeStationaryRequestDelete";
    public String authCode = "", userId = "";

    public StatinaryRequestAdapter(Context context, ArrayList<StationaryRequestModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.staionary_request_item_layout, parent, false);
        return new StatinaryRequestAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final StationaryRequestModel model = list.get(position);
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));



        holder.empNameTxt.setText(model.getEmployName());
        holder.zoneNameTxt.setText(model.getZoneName());
        holder.qunatityTxt.setText(model.getQuantity());
        holder.idleclouserDateTxt.setText(model.getIdleClousersDate());
        holder.requestDateTxt.setText(model.getRequestDate());
        holder.followupDateTxt.setText(model.getFollowUpDate());
        holder.statusTxt.setText(model.getStatus());

        //check app status
        if (model.getAppStatus().equalsIgnoreCase("1"))
        {
            holder.delBtn.setVisibility(View.VISIBLE);
            holder.mainLay.setVisibility(View.VISIBLE);
            holder.viewSecond.setVisibility(View.VISIBLE);


        }else
        {
            holder.delBtn.setVisibility(View.GONE);
            holder.mainLay.setVisibility(View.GONE);
            holder.viewSecond.setVisibility(View.GONE);
        }

        if (model.getAppStatus().equalsIgnoreCase("1"))
        {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.orange_color));
        }
        else if (model.getAppStatus().equalsIgnoreCase("2"))
        {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.green_color));
        }else if (model.getAppStatus().equalsIgnoreCase("3"))
        {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.red_color));
        }

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ViewRequestDetailsActivity.class);
                i.putExtra("Rid",model.getRID());
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        //follow Layout hide and visibile
        if (model.getFollowUpDate().equalsIgnoreCase("null"))
        {
            holder.followupLay.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);

        }


        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showSettingsAlert(position,authCode,model.getRID(),userId);
            }
        });







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

                deleteMethod(authcode,recordId,userid,postion);
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
    public void deleteMethod(final String AuthCode , final String RID, final String userId, final int postion) {

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
                params.put("RID",RID);
                params.put("AdminID",userId);

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
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView empNameTxt,zoneNameTxt,qunatityTxt,requestDateTxt, idleclouserDateTxt,followupDateTxt,statusTxt;
        public View view, viewSecond;
        public LinearLayout followupLay;
        public ImageView mainLay,delBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            empNameTxt = (TextView)itemView.findViewById(R.id.employename);
            zoneNameTxt = (TextView)itemView.findViewById(R.id.zonename);
            qunatityTxt = (TextView)itemView.findViewById(R.id.quantity);
            requestDateTxt = (TextView)itemView.findViewById(R.id.requestdate);
            idleclouserDateTxt = (TextView)itemView.findViewById(R.id.idleclouserdate);
            followupDateTxt = (TextView)itemView.findViewById(R.id.followupdate);
            statusTxt = (TextView)itemView.findViewById(R.id.statinarystatus);
            view = (View)itemView.findViewById(R.id.view);
            delBtn = (ImageView)itemView.findViewById(R.id.delbtn);
            mainLay = (ImageView) itemView.findViewById(R.id.stationary_request_main_lay);
            followupLay = (LinearLayout)itemView.findViewById(R.id.followuplay);
            viewSecond = (View)itemView.findViewById(R.id.view2);
        }
    }
}
