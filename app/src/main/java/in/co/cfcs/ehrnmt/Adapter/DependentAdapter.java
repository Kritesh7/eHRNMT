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

import in.co.cfcs.ehrnmt.Main.AddDependentActivity;
import in.co.cfcs.ehrnmt.Model.DependentModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * Created by Admin on 20-09-2017.
 */

public class DependentAdapter extends RecyclerView.Adapter<DependentAdapter.ViewHolder> {
    public Context context;
    public ArrayList<DependentModel> list = new ArrayList<>();
    public Activity activity;
    public String deleteUrl = SettingConstant.BaseUrl + "AppEmployeeDependentDelete";
    public String authCode = "", userid = "";

    public DependentAdapter(Context context, ArrayList<DependentModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.dependent_item_layout, parent, false);
        return new DependentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DependentModel model = list.get(position);

        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(context)));
        userid = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(context)));

        holder.nameTxt.setText(model.getFirstName() + " " + model.getLastName());
        holder.dobTxt.setText(model.getDob());
        holder.genderTxt.setText(model.getGender());
        holder.relationshipTxt.setText(model.getRelationship());

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, AddDependentActivity.class);
                i.putExtra("RecordId", model.getRecordId());
                i.putExtra("Mode", "EditMode");
                i.putExtra("FirstName", model.getFirstName());
                i.putExtra("LastName", model.getLastName());
                i.putExtra("GenderName", model.getGender());
                i.putExtra("RelationshipName", model.getRelationship());
                i.putExtra("DOB", model.getDob());
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTxt, dobTxt, genderTxt, relationshipTxt;
        public ImageView delBtn;
        public ImageView mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTxt = (TextView) itemView.findViewById(R.id.dependent_name);
            dobTxt = (TextView) itemView.findViewById(R.id.dependent_dob);
            genderTxt = (TextView) itemView.findViewById(R.id.dependent_gender);
            relationshipTxt = (TextView) itemView.findViewById(R.id.dependent_relationship);
            delBtn = (ImageView) itemView.findViewById(R.id.delbtn);
            mainLay = (ImageView) itemView.findViewById(R.id.main_lay);
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
                params.put("RecordID", RecordID);
                params.put("AdminID", AdminID);

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
