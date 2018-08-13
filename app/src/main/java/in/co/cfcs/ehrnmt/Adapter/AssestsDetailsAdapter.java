package in.co.cfcs.ehrnmt.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Model.AssestDetailsModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 18-09-2017.
 */

public class AssestsDetailsAdapter extends RecyclerView.Adapter<AssestsDetailsAdapter.ViewHolder> {
    public Context context;
    public ArrayList<AssestDetailsModel> list = new ArrayList<>();

    public AssestsDetailsAdapter(Context context, ArrayList<AssestDetailsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.assests_details_item_layout, parent, false);
        return new AssestsDetailsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        AssestDetailsModel model = list.get(position);

        holder.assestsNameTxt.setText(model.getAssetsName());
        holder.brandNameTxt.setText(model.getBrandName());
        holder.issuesDateTxt.setText(model.getIssuesDate());
        holder.estimatedReturnDateTxt.setText(model.getExpectedReturnDate());
        holder.issuesReasonsTxt.setText(model.getIssuesReason());
        holder.remarkTxt.setText(model.getRemark());
        holder.holderNameTxt.setText(model.getName());

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
        public TextView assestsNameTxt, brandNameTxt, issuesDateTxt, estimatedReturnDateTxt, issuesReasonsTxt, remarkTxt, holderNameTxt;

        public CardView mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            assestsNameTxt = (TextView) itemView.findViewById(R.id.assesname);
            brandNameTxt = (TextView) itemView.findViewById(R.id.brandname);
            issuesDateTxt = (TextView) itemView.findViewById(R.id.issuesdate);
            estimatedReturnDateTxt = (TextView) itemView.findViewById(R.id.estimated_return_date);
            issuesReasonsTxt = (TextView) itemView.findViewById(R.id.issuesreasons);
            remarkTxt = (TextView) itemView.findViewById(R.id.remark);
            holderNameTxt = (TextView) itemView.findViewById(R.id.username);

            mainLay = (CardView) itemView.findViewById(R.id.assets_main_lay);
        }
    }
}
