package in.co.cfcs.ehrnmt.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Main.AddNewEmergencyContactDetailsActivity;
import in.co.cfcs.ehrnmt.Model.EmergencyContactModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 27-10-2017.
 */

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder>
{
    public Context context;
    public ArrayList<EmergencyContactModel> list = new ArrayList<>();
    public Activity activity;
    public String checkNavigate;

    public EmergencyContactAdapter(Context context, ArrayList<EmergencyContactModel> list, Activity activity, String checkNavigate) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.checkNavigate =  checkNavigate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.emergency_contact_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        EmergencyContactModel model = list.get(position);

        holder.nameTxt.setText(model.getTitle() +" " + model.getName());
        holder.addressTxt.setText(model.getAddress() +","+ model.getCity() +","+ model.getState() +"," +model.getPostalcode());
        holder.telNumberTxt.setText(model.getTelephoneNumber());
        holder.mobileNumberTxt.setText(model.getMobileNumber());
        holder.emailTxt.setText(model.getEmail());
        holder.relationshipnameTxt.setText(model.getRelationshipname());
        holder.lastUpdateTxt.setText(model.getLastUpdate());
        holder.countryNameTxt.setText(model.getCounteryName());

        if (model.getType().equalsIgnoreCase("0")) {

            holder.emergencyContactTypeTxt.setText("Primary Contact");

        }else
            {
                holder.emergencyContactTypeTxt.setText("Secondary Contact");
            }

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkNavigate.equalsIgnoreCase("FirstOne")) {

                    Intent i = new Intent(context, AddNewEmergencyContactDetailsActivity.class);
                    i.putExtra("RecordId", model.getRecordId());
                    i.putExtra("Mode", "EditMode");
                    i.putExtra("Title", model.getTitle());
                    i.putExtra("Name", model.getName());
                    i.putExtra("Type", model.getType());
                    i.putExtra("RelationshipName", model.getRelationshipname());
                    i.putExtra("Address", model.getAddress());
                    i.putExtra("City", model.getCity());
                    i.putExtra("State", model.getState());
                    i.putExtra("PostalCode", model.getPostalcode());
                    i.putExtra("CountryName", model.getCounteryName());
                    i.putExtra("TelephoneNumber", model.getTelephoneNumber());
                    i.putExtra("MobileNumber", model.getMobileNumber());
                    i.putExtra("Email", model.getEmail());
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTxt,addressTxt,countryNameTxt,telNumberTxt, mobileNumberTxt, emailTxt, relationshipnameTxt, lastUpdateTxt
                , emergencyContactTypeTxt;
        public LinearLayout mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTxt = (TextView)itemView.findViewById(R.id.emergency_contact_name);
            addressTxt = (TextView)itemView.findViewById(R.id.emergency_contact_address);
            countryNameTxt = (TextView)itemView.findViewById(R.id.emergency_contact_countryname);
            telNumberTxt = (TextView)itemView.findViewById(R.id.emergency_contact_telno);
            mobileNumberTxt = (TextView)itemView.findViewById(R.id.emergency_contact_mobileno);
            emailTxt = (TextView)itemView.findViewById(R.id.emergency_contact_email);
            relationshipnameTxt = (TextView)itemView.findViewById(R.id.emergecncy_contact_relationshipname);
            lastUpdateTxt = (TextView)itemView.findViewById(R.id.emergency_contact_lastupdate);
            emergencyContactTypeTxt = (TextView) itemView.findViewById(R.id.emergency_contact_type);


            mainLay = (LinearLayout)itemView.findViewById(R.id.main_lay);
        }
    }
}
