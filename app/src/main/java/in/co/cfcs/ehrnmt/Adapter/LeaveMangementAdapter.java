package in.co.cfcs.ehrnmt.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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


import in.co.cfcs.ehrnmt.Main.ViewLeavemangementActivity;
import in.co.cfcs.ehrnmt.Model.LeaveManagementModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Admin on 18-09-2017.
 */

public class LeaveMangementAdapter extends RecyclerView.Adapter<LeaveMangementAdapter.ViewHolder> {

    public Context context;
    public ArrayList<LeaveManagementModel> list = new ArrayList<>();
    Activity activity;
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeeLeaveDelete";
    public String authCode = "", userId = "";
    public PopupWindow popupWindow;

    public LeaveMangementAdapter(Context context, ArrayList<LeaveManagementModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.leavemangaemnet_layout, parent, false);
        return new LeaveMangementAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final LeaveManagementModel model = list.get(position);

        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));


        holder.leaveTypeTxt.setText(model.getLeaveType());
        holder.startDateTxt.setText(model.getStartDate());
        holder.endDateTxt.setText(model.getEndDate());
        holder.appliedOnTxt.setText(model.getAppliedOn());
        holder.statusTxt.setText(model.getStatus());
        holder.empNameTxt.setText(model.getUserName());

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ViewLeavemangementActivity.class);
                i.putExtra("LeaveApplication_Id", model.getLeaveApplication_Id());
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        holder.noOfDaysTxt.setText(model.getNoofdays());

        if (model.getIsDeleteable().equalsIgnoreCase("1")) {
            holder.btnLay.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.btnLay.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }

        //delete leave
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPopupWindow(position, authCode, model.getLeaveApplication_Id(), userId);
            }
        });

        Log.e("chcking status id", model.getStatusId());
        Log.e("checking cancelStatusId", model.getCancelStatus());
        if (model.getStatusId().equalsIgnoreCase("3") || model.getStatusId().equalsIgnoreCase("4") ||
                model.getStatusId().equalsIgnoreCase("5") || model.getStatusId().equalsIgnoreCase("8") ||
                model.getStatusId().equalsIgnoreCase("9") || model.getCancelStatus().equalsIgnoreCase("3") ||
                model.getCancelStatus().equalsIgnoreCase("4") || model.getCancelStatus().equalsIgnoreCase("5") ||
                model.getCancelStatus().equalsIgnoreCase("8") || model.getCancelStatus().equalsIgnoreCase("9")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.red_color));
        } else if (model.getStatusId().equalsIgnoreCase("0") || model.getCancelStatus().equalsIgnoreCase("0")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.orange_color));
        } else {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.green_color));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leaveTypeTxt, startDateTxt, endDateTxt, appliedOnTxt, statusTxt, noOfDaysTxt, empNameTxt;
        public ImageView delBtn;
        public LinearLayout mainLay, btnLay;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);

            leaveTypeTxt = (TextView) itemView.findViewById(R.id.leave_type);
            startDateTxt = (TextView) itemView.findViewById(R.id.start_date);
            endDateTxt = (TextView) itemView.findViewById(R.id.end_date);
            appliedOnTxt = (TextView) itemView.findViewById(R.id.appliedon);
            statusTxt = (TextView) itemView.findViewById(R.id.status);
            noOfDaysTxt = (TextView) itemView.findViewById(R.id.noofdaystxt);
            delBtn = (ImageView) itemView.findViewById(R.id.delbtn);
            btnLay = (LinearLayout) itemView.findViewById(R.id.btnLay);
            view = (View) itemView.findViewById(R.id.view2);
            empNameTxt = (TextView) itemView.findViewById(R.id.leave_user);
            mainLay = (LinearLayout) itemView.findViewById(R.id.leave_management_main_lay);


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

    public void showSettingsAlert(final int postion, final String authcode, final String recordId, final String userid,
                                  final String remark) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        //  alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("Are You Sure You Want to Delete?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                deleteMethod(authcode, recordId, userid, postion, remark, "1", "5");

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
    public void deleteMethod(final String AuthCode, final String LeaveApplicationID, final String userId,
                             final int postion, final String Remark, final String Type, final String status) {

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
                            notifyItemChanged(postion);
                            popupWindow.dismiss();
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
                params.put("LeaveApplicationID", LeaveApplicationID);
                params.put("AdminID", userId);
                params.put("Remark", Remark);
                params.put("Type", Type);
                params.put("Status", status);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    private void setPopupWindow(final int position, final String authCode, final String recordId, final String userId) {


        LayoutInflater layoutInflater = (LayoutInflater) activity.getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_layout, null);
        Button cancel, backBtn;
        final EditText remarkTxt;
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.animationName);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


        cancel = (Button) popupView.findViewById(R.id.cancel_leaverequest);
        remarkTxt = (EditText) popupView.findViewById(R.id.remarktxt);
        backBtn = (Button) popupView.findViewById(R.id.backbtn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (remarkTxt.getText().toString().equalsIgnoreCase("")) {
                    remarkTxt.setHint("Please Enter Remark");
                    remarkTxt.setHintTextColor(Color.parseColor("#FF0000"));
                    Toast.makeText(activity, "Plesae enter remark", Toast.LENGTH_SHORT).show();
                } else {

                    showSettingsAlert(position, authCode, recordId, userId, remarkTxt.getText().toString());
                }


            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();
            }
        });

    }

}
