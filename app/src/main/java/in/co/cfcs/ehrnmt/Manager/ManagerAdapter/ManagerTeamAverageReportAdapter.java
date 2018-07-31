package in.co.cfcs.ehrnmt.Manager.ManagerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Manager.ManagerModel.ManagerTeamAvearageModel;
import in.co.cfcs.ehrnmt.R;

/**
 * Created by Admin on 15-11-2017.
 */

public class ManagerTeamAverageReportAdapter extends RecyclerView.Adapter<ManagerTeamAverageReportAdapter.ViewHolder>
{
    public Context context;
    public ArrayList<ManagerTeamAvearageModel> list = new ArrayList<>();

    public ManagerTeamAverageReportAdapter(Context context, ArrayList<ManagerTeamAvearageModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.manager_team_average_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ManagerTeamAvearageModel model = list.get(position);

        holder.empNameTxt.setText(model.getEmpName());
        holder.empIdTxt.setText(model.getEmpId());
        holder.monthTxt.setText(model.getAvgMonth() + " " + model.getAvgYear());
        holder.aveargeTxt.setText(model.getAverage());
        holder.workingDaysTxt.setText(model.getTotalNoOfDays());
        holder.workingHourTxt.setText(model.getTotalNoOfHours());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView empNameTxt,empIdTxt,monthTxt,workingDaysTxt, workingHourTxt,aveargeTxt;



        public ViewHolder(View itemView) {
            super(itemView);

            empNameTxt = (TextView) itemView.findViewById(R.id.mgrname);
            empIdTxt = (TextView)itemView.findViewById(R.id.mgrid);
            monthTxt = (TextView)itemView.findViewById(R.id.mgrmonth);
            workingDaysTxt = (TextView)itemView.findViewById(R.id.mgrdays);
            workingHourTxt = (TextView)itemView.findViewById(R.id.mgrhour);
            aveargeTxt = (TextView)itemView.findViewById(R.id.mgravg);

        }
    }
}
