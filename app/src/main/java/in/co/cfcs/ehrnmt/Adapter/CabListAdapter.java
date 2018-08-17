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

import in.co.cfcs.ehrnmt.Main.AddCabActivity;
import in.co.cfcs.ehrnmt.Main.ViewCabDetailsActivity;
import in.co.cfcs.ehrnmt.Model.CabListModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * Created by Admin on 18-09-2017.
 */

public class CabListAdapter extends RecyclerView.Adapter<CabListAdapter.ViewHolder> {

    public Context context;
    public ArrayList<CabListModel> list = new ArrayList<>();
    public Activity activity;
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeeTaxiRequestDelete";
    public String authCode = "", userId = "";

    public CabListAdapter(Context context, ArrayList<CabListModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cab_item_layout, parent, false);
        return new CabListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CabListModel model = list.get(position);
        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));


        holder.empNameTxt.setText(model.getEmployName());
        holder.zoneNameTxt.setText(model.getZoneName());
        holder.cityNameTXT.setText(model.getCityName());
        holder.bookingDateTxt.setText(model.getBookingDate());
        holder.requestDateTxt.setText(model.getRequestDate());
        holder.followupDateTxt.setText(model.getFollowUpDate());
        holder.statusTxt.setText(model.getStatus());

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, AddCabActivity.class);
                i.putExtra("Mode", "Edit");
                i.putExtra("BID", model.getBID());
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSettingsAlert(position, authCode, model.getBID(), userId);
            }
        });

        if (model.getFollowUpDate().equalsIgnoreCase("null")) {
            holder.hotelFollowLay.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        } else {
            holder.hotelFollowLay.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
        }

        holder.mainLayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ViewCabDetailsActivity.class);
                i.putExtra("Bid", model.getBID());
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        if (model.getStatus().equalsIgnoreCase("In Process")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.orange_color));
        } else if (model.getStatus().equalsIgnoreCase("Approved")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.green_color));
        } else if (model.getStatus().equalsIgnoreCase("Rejected")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.red_color));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView empNameTxt, zoneNameTxt, cityNameTXT, requestDateTxt, bookingDateTxt, followupDateTxt, statusTxt;
        public LinearLayout hotelFollowLay, btnLay, mainLayView;
        public ImageView mainLay, delBtn;
        public View view, view2;

        public ViewHolder(View itemView) {
            super(itemView);

            empNameTxt = (TextView) itemView.findViewById(R.id.cab_employename);
            zoneNameTxt = (TextView) itemView.findViewById(R.id.cab_zonename);
            cityNameTXT = (TextView) itemView.findViewById(R.id.city);
            requestDateTxt = (TextView) itemView.findViewById(R.id.cab_requestdate);
            bookingDateTxt = (TextView) itemView.findViewById(R.id.bookingdate);
            followupDateTxt = (TextView) itemView.findViewById(R.id.cab_followupdate);
            statusTxt = (TextView) itemView.findViewById(R.id.cab_status);
            delBtn = (ImageView) itemView.findViewById(R.id.delbtn);
            mainLay = (ImageView) itemView.findViewById(R.id.cab_main_lay);
            hotelFollowLay = (LinearLayout) itemView.findViewById(R.id.hotel_follow_lay);
            btnLay = (LinearLayout) itemView.findViewById(R.id.btnlay);
            mainLayView = (LinearLayout) itemView.findViewById(R.id.mainlay);
            view2 = (View) itemView.findViewById(R.id.view2);
            view = (View) itemView.findViewById(R.id.view);
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

    public void showSettingsAlert(final int postion, final String authcode, final String recordId, final String userid) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        //  alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("Are You Sure You Want to Delete?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                deleteMethod(authcode, recordId, userid, postion);
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
    public void deleteMethod(final String AuthCode, final String BID, final String userId, final int postion) {

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
                params.put("BID", BID);
                params.put("AdminID", userId);

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
