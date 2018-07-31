package in.co.cfcs.ehrnmt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Model.BookMeaPrevisionModel;
import in.co.cfcs.ehrnmt.R;


/**
 * Created by Admin on 12-10-2017.
 */

public class RequestedItemAdapter extends BaseAdapter
{

    public ArrayList<BookMeaPrevisionModel> list = new ArrayList<>();
    public Context context;
    LayoutInflater inflater;

    public RequestedItemAdapter(ArrayList<BookMeaPrevisionModel> list, Context context) {
        this.list = list;
        this.context = context;
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
            view = inflater.inflate(R.layout.requested_item_layout, null);

            holder.sNoTxt = (TextView) view.findViewById(R.id.srno);
            holder.itemNameTxt = (TextView) view.findViewById(R.id.itemname);
            holder.quantityTxt = (TextView) view.findViewById(R.id.quantity);
            holder.remarkTxt = (TextView) view.findViewById(R.id.remark);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        int pos = i+1;
        holder.sNoTxt.setText("("+pos + ")");
        holder.itemNameTxt.setText(list.get(i).getItemName());
        holder.remarkTxt.setText(list.get(i).getRemark());
        holder.quantityTxt.setText(list.get(i).getMaxQuantity());

        if (list.get(i).getRemark().equalsIgnoreCase(""))
        {
            holder.remarkTxt.setVisibility(View.GONE);
        }


        return view;
    }

    private class ViewHolder {

        TextView sNoTxt, itemNameTxt, quantityTxt, remarkTxt;
        //LinearLayout mainLay,primory_layout;
    }
}
