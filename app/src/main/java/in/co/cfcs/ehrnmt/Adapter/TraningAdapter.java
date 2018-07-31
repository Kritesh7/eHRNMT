package in.co.cfcs.ehrnmt.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Model.TraningModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 18-09-2017.
 */

public class TraningAdapter extends  RecyclerView.Adapter<TraningAdapter.ViewHolder>
{

    public Context context;
    public ArrayList<TraningModel> list = new ArrayList<>();

    public TraningAdapter(Context context, ArrayList<TraningModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.traning_items_layout, parent, false);
        return new TraningAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TraningModel model = list.get(position);

        holder.domainTxt.setText(model.getDomain());
        holder.courseTxt.setText(model.getCourse());
        holder.estimatedStartDateTxt.setText(model.getEstimatedStartDate());
        holder.estimatedEndDateTxt.setText(model.getEstimatedEndDate());
        holder.statusTxt.setText(model.getStatus());

       /* if (position % 2 == 1) {
            holder.mainLay.setCardBackgroundColor(context.getResources().getColor(R.color.col1));
        }
        else{
            holder.mainLay.setCardBackgroundColor(context.getResources().getColor(R.color.col2));
        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView domainTxt,courseTxt,estimatedStartDateTxt,estimatedEndDateTxt, statusTxt;

        public CardView mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            domainTxt = (TextView)itemView.findViewById(R.id.domain);
            courseTxt = (TextView)itemView.findViewById(R.id.course);
            estimatedStartDateTxt = (TextView)itemView.findViewById(R.id.estimated_start_date);
            estimatedEndDateTxt = (TextView)itemView.findViewById(R.id.estimated_end_date);
            statusTxt = (TextView)itemView.findViewById(R.id.traning_status);

            mainLay = (CardView)itemView.findViewById(R.id.traning_main_lay);
        }
    }
}
