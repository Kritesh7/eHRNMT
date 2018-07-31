package in.co.cfcs.ehrnmt.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Main.AddNewContactActivity;
import in.co.cfcs.ehrnmt.Model.ContactModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 02-11-2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
{
    public ArrayList<ContactModel> list = new ArrayList<>();
    public Context context;
    public Activity activity;
    public String checkNavigate;

    public ContactAdapter(ArrayList<ContactModel> list, Context context, Activity activity, String checkNavigate) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.checkNavigate = checkNavigate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.contact_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ContactModel model = list.get(position);

        if (model.getAddressType().equalsIgnoreCase("1"))
        {
            holder.addressTypeTxt.setText("Permanent Address");

        }else if (model.getAddressType().equalsIgnoreCase("2"))
        {
            holder.addressTypeTxt.setText("Current Address");

        }else if (model.getAddressType().equalsIgnoreCase("3"))
        {
            holder.addressTypeTxt.setText("Correspond Address");
        }

        holder.addressTxt.setText(model.getAddress());
        holder.cityTxt.setText(model.getCity());
        holder.stateTxt.setText(model.getState());
        holder.postCodeTxt.setText(model.getPostalcode());
        holder.countryNameTxt.setText(model.getCounteryName());
        holder.lastUpdateTxt.setText(model.getLastUpdate());

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkNavigate.equalsIgnoreCase("FirstOne")) {

                    Intent i = new Intent(context, AddNewContactActivity.class);
                    i.putExtra("RecordId", model.getRecordId());
                    i.putExtra("Mode", "EditMode");
                    i.putExtra("AddressType", model.getAddressType());
                    i.putExtra("Address", model.getAddress());
                    i.putExtra("City", model.getCity());
                    i.putExtra("State", model.getState());
                    i.putExtra("PostalCode", model.getPostalcode());
                    i.putExtra("CountryName", model.getCounteryName());
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
        public TextView addressTypeTxt,addressTxt, cityTxt, stateTxt, postCodeTxt, countryNameTxt, lastUpdateTxt;
        public View view;
        public LinearLayout mainLay;
        public ImageView downloadBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            addressTypeTxt = (TextView)itemView.findViewById(R.id.address_type);
            addressTxt = (TextView)itemView.findViewById(R.id.address);
            cityTxt = (TextView)itemView.findViewById(R.id.city);
            stateTxt = (TextView)itemView.findViewById(R.id.state);
            postCodeTxt = (TextView)itemView.findViewById(R.id.postalcode);
            countryNameTxt = (TextView)itemView.findViewById(R.id.countrycode);
            lastUpdateTxt = (TextView)itemView.findViewById(R.id.lastupdate);

            mainLay = (LinearLayout) itemView.findViewById(R.id.main_lay);



        }
    }

}
