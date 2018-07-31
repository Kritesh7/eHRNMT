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

import in.co.cfcs.ehrnmt.Main.AddNewLnaguageActivity;
import in.co.cfcs.ehrnmt.Model.LanguageModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 21-09-2017.
 */

public class LangaugeAdapter extends RecyclerView.Adapter<LangaugeAdapter.ViewHolder>
{

    public Context context;
    public ArrayList<LanguageModel> list = new ArrayList<>();
    public Activity activity;
    public String checkNavigate;

    public LangaugeAdapter(Context context, ArrayList<LanguageModel> list, Activity activity, String checkNavigate) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.checkNavigate = checkNavigate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.langayge_item_layout, parent, false);
        return new LangaugeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LanguageModel model = list.get(position);

        holder.languageTxt.setText(model.getLangaugae());

        if (model.getRead().equalsIgnoreCase("true"))
        {
            holder.readTxt.setText("Yes");
        }else
            {
                holder.readTxt.setText("No");
            }

        if (model.getWrite().equalsIgnoreCase("true"))
        {
            holder.writeTxt.setText("Yes");
        }else
        {
            holder.writeTxt.setText("No");
        }

        if (model.getSpeak().equalsIgnoreCase("true"))
        {
            holder.speakTxt.setText("Yes");
        }else
        {
            holder.speakTxt.setText("No");
        }


        holder.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkNavigate.equalsIgnoreCase("FirstOne")) {

                    Intent i = new Intent(context, AddNewLnaguageActivity.class);
                    i.putExtra("ActionMode", "EditMode");
                    i.putExtra("RecordId", model.getRecordID());
                    i.putExtra("LangageName", model.getLangaugae());
                    i.putExtra("Read", model.getRead());
                    i.putExtra("Write", model.getWrite());
                    i.putExtra("Speak", model.getSpeak());
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
        public TextView languageTxt,readTxt,writeTxt,speakTxt;

        public LinearLayout mainLay;

        public ViewHolder(View itemView) {
            super(itemView);

            languageTxt = (TextView)itemView.findViewById(R.id.langauge);
            readTxt = (TextView)itemView.findViewById(R.id.read);
            writeTxt = (TextView)itemView.findViewById(R.id.write);
            speakTxt = (TextView)itemView.findViewById(R.id.speak);
            mainLay = (LinearLayout) itemView.findViewById(R.id.main_lay);

        }
    }
}
