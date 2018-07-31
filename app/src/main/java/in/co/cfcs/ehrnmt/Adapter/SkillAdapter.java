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

import in.co.cfcs.ehrnmt.Main.AddNewSkilActivity;
import in.co.cfcs.ehrnmt.Model.SkillsModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 21-09-2017.
 */

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.ViewHolder>
{

    public Context context;
    public ArrayList<SkillsModel> list = new ArrayList<>();
    public Activity activity;
    public String checkNavigate;


    public SkillAdapter(Context context, ArrayList<SkillsModel> list, Activity activity, String checkNavigate) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.checkNavigate = checkNavigate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.skils_item_layout, parent, false);
        return new SkillAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SkillsModel model = list.get(position);

        holder.skillTxt.setText(model.getSkill());
        holder.proficencyTxt.setText(model.getProficency());
        holder.sourceTxt.setText(model.getSource());
        holder.lastUsedTxt.setText(model.getLastUsed());

        if (model.getCurrentUsed().equalsIgnoreCase("true")) {
            holder.currentUsedTxt.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
        }else
            {
                holder.currentUsedTxt.setVisibility(View.GONE);
                holder.view.setVisibility(View.GONE);
            }

        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkNavigate.equalsIgnoreCase("FirstOne")) {

                    Intent i = new Intent(context, AddNewSkilActivity.class);
                    i.putExtra("ActionMode", "EditMode");
                    i.putExtra("RecordId", model.getRecordID());
                    i.putExtra("SkillName", model.getSkill());
                    i.putExtra("ProficeiancyName", model.getProficency());
                    i.putExtra("SourceName", model.getSource());
                    i.putExtra("CurrentelyUsed", model.getCurrentUsed());
                    i.putExtra("LastUsedDate", model.getLastUsed());
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
        public TextView skillTxt,proficencyTxt,sourceTxt,lastUsedTxt;
        public View view;
        public LinearLayout currentUsedTxt,mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            skillTxt = (TextView)itemView.findViewById(R.id.skill);
            proficencyTxt = (TextView)itemView.findViewById(R.id.proficiency);
            sourceTxt = (TextView)itemView.findViewById(R.id.source);
            lastUsedTxt = (TextView)itemView.findViewById(R.id.lastused);
            currentUsedTxt = (LinearLayout)itemView.findViewById(R.id.currentused);
            view = (View) itemView.findViewById(R.id.view);

            mainLay = (LinearLayout)itemView.findViewById(R.id.main_lay);
        }
    }
}
