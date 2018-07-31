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

import in.co.cfcs.ehrnmt.Main.ViewLeavemangementActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerRequestTraningDetailsActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerModel.ManagerRequestTraningModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Admin on 21-11-2017.
 */

public class ManagerRequestTraningAdapter extends RecyclerView.Adapter<ManagerRequestTraningAdapter.ViewHolder>
{

    public Context context;
    public ArrayList<ManagerRequestTraningModel> list = new ArrayList<>();
    public Activity activity;
    public PopupWindow popupWindow;
    public String authcode = "", userid = "", statusId = "";
    public String approveRejectUrl = SettingConstant.BaseUrl + "AppManagerTrainingRequestApproveAndReject";

    public ManagerRequestTraningAdapter(Context context, ArrayList<ManagerRequestTraningModel> list, Activity activity, String statusId) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.statusId = statusId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.manager_request_traning_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ManagerRequestTraningModel model = list.get(position);

        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userid =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));



        holder.empNameTxt.setText(model.getEmpName());
        holder.doaminNameTxt.setText(model.getDomainName());
        holder.courseNameTxt.setText(model.getCourseName());
        holder.startDateTxt.setText(model.getStartDate());
        holder.endDateTxt.setText(model.getEndDate());
        holder.profiNameTxt.setText(model.getProficenacyName());
        holder.statusTxt.setText(model.getStatus());

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ManagerRequestTraningDetailsActivity.class);
                i.putExtra("LeaveApplication_Id",model.getEmpId());
                i.putExtra("CheckingNavigate",statusId);
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPopupWindow("Reject",authcode,userid, model.getEmpId(),position);
            }
        });

        holder.approvedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPopupWindow("Approve",authcode,userid, model.getEmpId(),position);
            }
        });

        //hide widget
        if (statusId.equalsIgnoreCase("2"))
        {
            holder.btnLay.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }else
            {
                holder.btnLay.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.VISIBLE);
            }





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView empNameTxt,startDateTxt,endDateTxt,doaminNameTxt, profiNameTxt,courseNameTxt, statusTxt;
        public ImageView delBtn;
        public LinearLayout mainLay, btnLay;
        public View view;
        public ImageView approvedBtn, rejectBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            empNameTxt = (TextView) itemView.findViewById(R.id.req_tran_name);
            startDateTxt = (TextView)itemView.findViewById(R.id.req_tran_start_date);
            endDateTxt = (TextView)itemView.findViewById(R.id.req_tran_end_date);
            doaminNameTxt = (TextView)itemView.findViewById(R.id.req_tran_domain_name);
            profiNameTxt = (TextView)itemView.findViewById(R.id.req_tran_proficeancy_name);
            courseNameTxt = (TextView)itemView.findViewById(R.id.req_tran_course_name);
            statusTxt = (TextView) itemView.findViewById(R.id.req_tran_status);
            rejectBtn = (ImageView)itemView.findViewById(R.id.rejectbtn);
            approvedBtn = (ImageView)itemView.findViewById(R.id.approedbtn);
            mainLay = (LinearLayout)itemView.findViewById(R.id.leave_management_main_lay);
            btnLay = (LinearLayout) itemView.findViewById(R.id.btnlay);
            view = (View) itemView.findViewById(R.id.view);

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

                            apiRejectToApprove(authCode, leaveId, userId, postion, remarkTxt.getText().toString(),
                                    "2");


                    }else
                    {
                        apiRejectToApprove(authCode, leaveId, userId, postion, remarkTxt.getText().toString(),
                                    "4");
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
    public void apiRejectToApprove(final String AuthCode ,final String ApplicationID, final String userId,
                                   final int postion, final String Remark, final String status) {

        final ProgressDialog pDialog = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, approveRejectUrl, new Response.Listener<String>() {
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
                params.put("AdminID",userId);
                params.put("ApplicationID",ApplicationID);
                params.put("Status",status);
                params.put("Remark",Remark);

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
