package in.co.cfcs.ehrnmt.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.co.cfcs.ehrnmt.Model.AppreceationModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.DownloadTask;
import in.co.cfcs.ehrnmt.Source.SettingConstant;


/**
 * Created by Admin on 25-09-2017.
 */

public class AppreceationAdapter extends RecyclerView.Adapter<AppreceationAdapter.ViewHolder> {
    public Context context;
    public ArrayList<AppreceationModel> list = new ArrayList<>();
    String[] permissions = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    public String checkNavigateStr = "Appreciation";
    public Activity activity;

    public AppreceationAdapter(Context context, ArrayList<AppreceationModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.appreceation_item_layout, parent, false);
        return new AppreceationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AppreceationModel model = list.get(position);

        holder.appreceaionDateTxt.setText(model.getAppreceationDate());
        holder.appreceationDetailsTxt.setText(model.getAppreceationDetails());
        holder.appreceationTitleTxt.setText(model.getAppreceationTitle());

        if (model.getFileNameText().equalsIgnoreCase("")) {
            holder.downloadLay.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        } else {
            holder.downloadLay.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
        }

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissions()) {
                    new DownloadTask(context, SettingConstant.DownloadUrl + model.getFileNameText(), checkNavigateStr);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView appreceaionDateTxt, appreceationDetailsTxt, appreceationTitleTxt;
        public View view;
        public LinearLayout downloadLay;
        public ImageView downloadBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            appreceaionDateTxt = (TextView) itemView.findViewById(R.id.appreceation_date);
            appreceationDetailsTxt = (TextView) itemView.findViewById(R.id.appreceation_details);
            appreceationTitleTxt = (TextView) itemView.findViewById(R.id.appreceation_title);
            downloadLay = (LinearLayout) itemView.findViewById(R.id.downloadOptionLay);
            view = (View) itemView.findViewById(R.id.view);
            downloadBtn = (ImageView) itemView.findViewById(R.id.downloadOptionBtn);

        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

}
