package in.co.cfcs.ehrnmt.Manager.ManagerAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import in.co.cfcs.ehrnmt.Adapter.ShortLeaveHistoryAdapter;
import in.co.cfcs.ehrnmt.Main.ViewShortLeaveHistoryActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerModel.ManagerRequestToApprovedShortLeaveModel;
import in.co.cfcs.ehrnmt.Model.ShortLeaveHistoryModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Admin on 13-11-2017.
 */

public class ManagerRequestToApprovedShortLeaveAdapter extends RecyclerView.Adapter<ManagerRequestToApprovedShortLeaveAdapter.ViewHolder>
{
    public Context context;
    public ArrayList<ManagerRequestToApprovedShortLeaveModel> list = new ArrayList<>();
    public Activity activity;
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeeShortLeaveDelete";
    public String authCode = "", userId = "";
    public PopupWindow popupWindow;
    public String checkStatus = "";

    public ManagerRequestToApprovedShortLeaveAdapter(Context context, ArrayList<ManagerRequestToApprovedShortLeaveModel> list,
                                                     Activity activity, String checkStatus ) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.checkStatus = checkStatus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.manager_short_leave_approved_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final ManagerRequestToApprovedShortLeaveModel model = list.get(position);
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));



        holder.leaveTypeTxt.setText(model.getLeaveTypeName());
        holder.startDateTxt.setText(model.getStartDate());
        holder.timeFromTxt.setText(model.getTimeFrom());
        holder.timeToTxt.setText(model.getTimeTo());
        holder.AppliedDateTxt.setText(model.getAppliedDate());
        holder.statusTxt.setText(model.getStatusText());
        holder.commentTxt.setText(model.getCommentText());
        holder.empNameTxt.setText(model.getUserName());
        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ViewShortLeaveHistoryActivity.class);
                i.putExtra("LeaveApplication_Id",model.getLeaveApplication_Id());
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPopupWindow("Reject",authCode,userId,model.getLeaveApplication_Id(),position);
            }
        });

        holder.approvedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPopupWindow("Approve",authCode,userId,model.getLeaveApplication_Id(),position);
            }
        });


       /* //delete leave
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                setPopupWindow(position,authCode,model.getLeaveApplication_Id(),userId);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leaveTypeTxt,startDateTxt,timeFromTxt,timeToTxt, AppliedDateTxt,statusTxt,commentTxt, empNameTxt;
        public ImageView approvedBtn, rejectBtn;
        public LinearLayout mainLay,btnLay;
        public View view;;

        public ViewHolder(View itemView) {
            super(itemView);

            leaveTypeTxt = (TextView)itemView.findViewById(R.id.short_leave_type);
            startDateTxt = (TextView)itemView.findViewById(R.id.short_start_date);
            timeFromTxt = (TextView)itemView.findViewById(R.id.short_time_from);
            empNameTxt = (TextView) itemView.findViewById(R.id.short_leavename);
            timeToTxt = (TextView)itemView.findViewById(R.id.short_time_to);
            statusTxt = (TextView)itemView.findViewById(R.id.short_status);
            AppliedDateTxt = (TextView)itemView.findViewById(R.id.short_applied_date);
            commentTxt = (TextView)itemView.findViewById(R.id.short_comment);
            rejectBtn = (ImageView)itemView.findViewById(R.id.rejectbtn);
            approvedBtn = (ImageView)itemView.findViewById(R.id.approedbtn);
            btnLay = (LinearLayout) itemView.findViewById(R.id.btnLay);
            view = (View) itemView.findViewById(R.id.view2);
            mainLay = (LinearLayout)itemView.findViewById(R.id.short_leave_main_lay);





        }
    }

    private void setPopupWindow(final String check, final String authCode, final String userId, final String leaveId,
                                final int postion) {



        LayoutInflater layoutInflater = (LayoutInflater)activity.getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup_layout, null);
        Button cancel , backBtn;
        final EditText remarkTxt;
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.animationName);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

       //

        cancel = (Button) popupView.findViewById(R.id.cancel_leaverequest);
        remarkTxt = (EditText)popupView.findViewById(R.id.remarktxt);
        backBtn = (Button)popupView.findViewById(R.id.backbtn);

        if (check.equalsIgnoreCase("Approve"))
        {
            cancel.setText("Approve");


        }else {
            cancel.setText("Reject");

        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check.equalsIgnoreCase("Approve")) {

                    if (remarkTxt.getText().toString().equalsIgnoreCase("")) {
                        remarkTxt.setError("Please enter remark");
                        Toast.makeText(activity, "Plesae enter remark", Toast.LENGTH_SHORT).show();
                    } else {

                        if (remarkTxt.getText().toString().equalsIgnoreCase("")) {
                            remarkTxt.setError("Please enter remark");
                        } else {

                            if (checkStatus.equalsIgnoreCase("Short Leave Request")) {
                                apiRejectToApprove(authCode, leaveId, userId, postion, remarkTxt.getText().toString(),
                                        "2", "1");
                            }else
                                {
                                    apiRejectToApprove(authCode, leaveId, userId, postion, remarkTxt.getText().toString(),
                                            "2", "6");
                                }
                        }
                    }
                }else
                    {
                        if (remarkTxt.getText().toString().equalsIgnoreCase(""))
                        {
                            remarkTxt.setError("Please enter remark");
                        }else {

                            if (checkStatus.equalsIgnoreCase("Short Leave Request")) {
                                apiRejectToApprove(authCode, leaveId, userId, postion, remarkTxt.getText().toString(),
                                        "2", "3");
                            }else
                                {
                                    apiRejectToApprove(authCode, leaveId, userId, postion, remarkTxt.getText().toString(),
                                            "2", "8");
                                }
                        }
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

    //approve reject API
    public void apiRejectToApprove(final String AuthCode ,final String LeaveApplicationID, final String userId,
                             final int postion, final String Remark, final String type, final String status) {

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
                        String MsgNotification = jsonObject.getString("MsgNotification");

                        if (status.equalsIgnoreCase("success"))
                        {
                            remove(postion);
                            popupWindow.dismiss();
                            Toast.makeText(context, MsgNotification, Toast.LENGTH_SHORT).show();
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
                params.put("LeaveApplicationID",LeaveApplicationID);
                params.put("AdminID",userId);
                params.put("Remark",Remark);
                params.put("Type",type);
                params.put("Status",status);


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
