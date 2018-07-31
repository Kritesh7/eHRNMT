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

import in.co.cfcs.ehrnmt.Model.WarningModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.DownloadTask;
import in.co.cfcs.ehrnmt.Source.SettingConstant;


/**
 * Created by Admin on 25-09-2017.
 */

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.ViewHolder>
{
    public Context context;
    public ArrayList<WarningModel> list = new ArrayList<>();
    String[] permissions = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    public Activity activity;
    public String checkNavigateStr = "Warning";

    public WarningAdapter(Context context, ArrayList<WarningModel> list, Activity activity) {
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.warning_item_layout, parent, false);
        return new WarningAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        WarningModel model = list.get(position);

        holder.warningDteTxt.setText(model.getWarningDate());
        holder.warningDetailsTxt.setText(model.getWarningDetails());
        holder.warningTitileTxt.setText(model.getWarningTitle());

        if (model.getFileNameText().equalsIgnoreCase(""))
        {
            holder.downloadLay.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }else
        {
            holder.downloadLay.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
        }

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissions()) {
                    new DownloadTask(context, SettingConstant.DownloadUrl + model.getFileNameText(),checkNavigateStr);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView warningDteTxt,warningDetailsTxt, warningTitileTxt;
        public View view;
        public LinearLayout downloadLay;
        public ImageView downloadBtn;
        //public CardView mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            warningDteTxt = (TextView)itemView.findViewById(R.id.warning_date);
            warningDetailsTxt = (TextView)itemView.findViewById(R.id.warning_details);
            warningTitileTxt = (TextView)itemView.findViewById(R.id.warning_title);
            downloadLay = (LinearLayout) itemView.findViewById(R.id.downloadOptionLay);
            view = (View)itemView.findViewById(R.id.view);
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
