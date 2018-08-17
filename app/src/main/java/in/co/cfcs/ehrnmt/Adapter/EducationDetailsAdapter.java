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

import in.co.cfcs.ehrnmt.Main.AddQualificationActivity;
import in.co.cfcs.ehrnmt.Model.EducationModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * Created by Admin on 21-09-2017.
 */

public class EducationDetailsAdapter extends RecyclerView.Adapter<EducationDetailsAdapter.ViewHolder> {

    public Context context;
    public ArrayList<EducationModel> list = new ArrayList<>();
    public Activity activity;
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeeEducationDelete";
    public String userId = "", authCode = "";
    public String checkNavigate;

    public EducationDetailsAdapter(Context context, ArrayList<EducationModel> list, Activity activity, String checkNavigate) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.checkNavigate = checkNavigate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.education_detials_item_layout, parent, false);
        return new EducationDetailsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        EducationModel model = list.get(position);

        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));


        holder.qualificationTxt.setText(model.getQualification());
        holder.desciplineTxt.setText(model.getDescipline());
        holder.passingDateTxt.setText(model.getPassingDate());
        holder.instituteTxt.setText(model.getInstitute());
        holder.courseTypeTxt.setText(model.getCourseType());
        holder.statusTxt.setText(model.getStatusTxt());

        if (model.getHighestDegree().equalsIgnoreCase("true")) {
            holder.highestDegreeTxt.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);

        } else {
            holder.highestDegreeTxt.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }

        //Comment lay visibile or not
        if (model.getComment().equalsIgnoreCase("")) {
            holder.commLay.setVisibility(View.GONE);
            holder.commentView.setVisibility(View.GONE);
        } else {
            holder.commLay.setVisibility(View.VISIBLE);
            holder.commentView.setVisibility(View.VISIBLE);

            holder.commentTxt.setText(model.getComment());
        }

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getEditable().equalsIgnoreCase("1")) {
                    Intent i = new Intent(context, AddQualificationActivity.class);
                    i.putExtra("RecordId", model.getRecordId());
                    i.putExtra("Mode", "EditMode");
                    i.putExtra("QualificationName", model.getQualification());
                    i.putExtra("DeciplineName", model.getDescipline());
                    i.putExtra("PassingDate", model.getPassingDate());
                    i.putExtra("Institute", model.getInstitute());
                    i.putExtra("CourseTypeName", model.getCourseType());
                    i.putExtra("HighestDegree", model.getHighestDegree());
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                } else {
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

                if (model.getDeletable().equalsIgnoreCase("1")) {
                    showSettingsAlert(position, authCode, model.getRecordId(), userId);
                } else {
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

        if (checkNavigate.equalsIgnoreCase("FirstOne")) {
            holder.view2.setVisibility(View.VISIBLE);
            holder.btnLay.setVisibility(View.VISIBLE);
        } else {
            holder.view2.setVisibility(View.GONE);
            holder.btnLay.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView qualificationTxt, desciplineTxt, passingDateTxt, instituteTxt, courseTypeTxt, commentTxt, statusTxt;
        public View view, commentView, view2;
        public ImageView editBtn, delBtn;
        public LinearLayout mainLay, highestDegreeTxt, commLay, btnLay;

        public ViewHolder(View itemView) {
            super(itemView);

            qualificationTxt = (TextView) itemView.findViewById(R.id.qualification);
            desciplineTxt = (TextView) itemView.findViewById(R.id.decipline);
            passingDateTxt = (TextView) itemView.findViewById(R.id.passingdate);
            instituteTxt = (TextView) itemView.findViewById(R.id.institute);
            courseTypeTxt = (TextView) itemView.findViewById(R.id.coursetype);
            highestDegreeTxt = (LinearLayout) itemView.findViewById(R.id.highestdegree);
            view = (View) itemView.findViewById(R.id.view);
            mainLay = (LinearLayout) itemView.findViewById(R.id.main_lay);
            commentTxt = (TextView) itemView.findViewById(R.id.commenttxt);
            statusTxt = (TextView) itemView.findViewById(R.id.status);
            commentView = (View) itemView.findViewById(R.id.commView);
            commLay = (LinearLayout) itemView.findViewById(R.id.commlay);
            editBtn = (ImageView) itemView.findViewById(R.id.editBtn);
            delBtn = (ImageView) itemView.findViewById(R.id.delbtn);
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

    public void showSettingsAlert(final int postion, final String authcode, final String recordId, final String userid) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        //  alertDialog.setTitle("Alert");

        // Setting Dialog Message
        alertDialog.setMessage("Are You Sure You Want to Delete?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                deleteMethod(authcode, recordId, postion, userid);
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
    public void deleteMethod(final String AuthCode, final String RecordID, final int postion, final String userid) {

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
                params.put("AdminID", userid);
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
}
