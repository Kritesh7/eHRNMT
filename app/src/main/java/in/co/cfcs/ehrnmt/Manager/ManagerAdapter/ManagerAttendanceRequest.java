package in.co.cfcs.ehrnmt.Manager.ManagerAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.AttendanceRequest;
import in.co.cfcs.ehrnmt.Manager.ManagerModel.ManagerAttendanceRequestModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ManagerAttendanceRequest extends RecyclerView.Adapter<ManagerAttendanceRequest.ViewHolder> {
    public Context context;
    public ArrayList<ManagerAttendanceRequestModel> list = new ArrayList<>();
    public Activity activity;
    public String checkStatus;
    public String deleteUrl = SettingConstant.BaseUrl + "AppPunchLogApproveReject";
    public String authCode = "", userId = "";

    public ManagerAttendanceRequest(Context context, ArrayList<ManagerAttendanceRequestModel> list, Activity activity,
                                    String checkStatus) {
        this.context = context;
        this.list = list;
        this.checkStatus = checkStatus;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.attendance_request_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ManagerAttendanceRequestModel model = list.get(position);

        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));

        holder.username.setText(model.getUserName());
        holder.employee_id.setText(model.getEmpID());
        holder.designation_name.setText(model.getDesignationName());
        holder.log_date.setText(model.getLogDateText());
        holder.log_time.setText(model.getLogTime());
        holder.log_by.setText(model.getLogTypeText());
        holder.status.setText(model.getApprovalStatusText());


        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPopupWindow("3", authCode, userId, model.getDeviceLogID(), position);
            }
        });

        holder.approvedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPopupWindow("2", authCode, userId, model.getDeviceLogID(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username, employee_id, designation_name, log_date, log_time, log_by, status;
        public ImageView delBtn;
        public LinearLayout mainLay, btnLay;
        public View view;
        public ImageView approvedBtn, rejectBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            employee_id = (TextView) itemView.findViewById(R.id.employee_id);
            designation_name = (TextView) itemView.findViewById(R.id.designation_name);
            log_date = (TextView) itemView.findViewById(R.id.log_date);
            log_time = (TextView) itemView.findViewById(R.id.log_time);
            log_by = (TextView) itemView.findViewById(R.id.log_by);
            status = (TextView) itemView.findViewById(R.id.status);
            rejectBtn = (ImageView) itemView.findViewById(R.id.rejectbtn);
            approvedBtn = (ImageView) itemView.findViewById(R.id.approedbtn);
            mainLay = (LinearLayout) itemView.findViewById(R.id.leave_management_main_lay);

        }
    }

    private void setPopupWindow(final String check, final String authCode, final String userId, final String deviceLogID,
                                final int postion) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        if (check.compareToIgnoreCase("2") == 0) {
            alertDialog.setMessage("Do you want to Approve?");
        } else {
            alertDialog.setMessage("Do you want to Reject?");
        }


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub


                apiRejectToApprove(check, authCode, userId, deviceLogID, postion);


                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(context, AttendanceRequest.class);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        alertDialog.show();


    }

    //approve reject API
    public void apiRejectToApprove(String check, String authCode, String userId, String deviceLogID, int postion) {

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
                        String MsgNotification = jsonObject.getString("MsgNotification");

                        if (status.equalsIgnoreCase("success")) {
                            remove(postion);
                            Toast.makeText(context, MsgNotification, Toast.LENGTH_SHORT).show();
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
                params.put("AuthCode", authCode);
                params.put("AdminID", userId);
                params.put("DeviceLogID", deviceLogID);
                params.put("ApprovalStatusID", check);

                Log.e("Parms", params.toString());
                return params;
            }
        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //remove list
    public void remove(int position) {
        if (position < 0 || position >= list.size()) {
            return;
        }
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
