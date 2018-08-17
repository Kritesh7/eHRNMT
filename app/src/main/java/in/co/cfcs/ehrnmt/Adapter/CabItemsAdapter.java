package in.co.cfcs.ehrnmt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Model.CabItemModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 25-10-2017.
 */

public class CabItemsAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<CabItemModel> list = new ArrayList<>();
    LayoutInflater inflater;

    public CabItemsAdapter(Context context, ArrayList<CabItemModel> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.cabdetails_item_layout, null);

            holder.sNoTxt = (TextView) view.findViewById(R.id.srno);
            holder.sourceAddTxt = (TextView) view.findViewById(R.id.sourceadd);
            holder.destAddTxt = (TextView) view.findViewById(R.id.destadd);
            holder.bookingTimeTxt = (TextView) view.findViewById(R.id.booktime);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        int pos = i + 1;
        holder.sNoTxt.setText("(" + pos + ")");
        holder.sourceAddTxt.setText(list.get(i).getSourceAdd());
        holder.destAddTxt.setText(list.get(i).getDestinationAdd());
        holder.bookingTimeTxt.setText(list.get(i).getBookTime());


        return view;
    }

    private class ViewHolder {
        TextView sNoTxt, sourceAddTxt, destAddTxt, bookingTimeTxt;
    }
}
