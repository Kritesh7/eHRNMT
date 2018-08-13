package in.co.cfcs.ehrnmt.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Model.HolidayListModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 10-10-2017.
 */

public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.ViewHolder> {
    public Context context;
    public ArrayList<HolidayListModel> list = new ArrayList<>();

    public HolidayListAdapter(Context context, ArrayList<HolidayListModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holiday_list_item, parent, false);
        return new HolidayListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        HolidayListModel model = list.get(position);

        holder.holidayNameTxt.setText(model.getHolidayName());
        holder.holidayDateTxt.setText(model.getHolidayDate());
        holder.holidayTypeTxt.setText(model.getHolidayType());
        holder.descriptionTxt.setText(model.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView holidayNameTxt, holidayDateTxt, holidayTypeTxt, descriptionTxt;

        public LinearLayout mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            holidayNameTxt = (TextView) itemView.findViewById(R.id.holidayname);
            holidayDateTxt = (TextView) itemView.findViewById(R.id.holidaydate);
            holidayTypeTxt = (TextView) itemView.findViewById(R.id.holidaytype);
            descriptionTxt = (TextView) itemView.findViewById(R.id.description);

            mainLay = (LinearLayout) itemView.findViewById(R.id.leave_management_main_lay);

        }
    }
}
