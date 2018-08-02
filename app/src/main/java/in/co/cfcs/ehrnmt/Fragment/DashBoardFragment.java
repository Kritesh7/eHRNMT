package in.co.cfcs.ehrnmt.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.Main.AttendanceModule;
import in.co.cfcs.ehrnmt.Main.NewAddLeaveMangementActivity;
import in.co.cfcs.ehrnmt.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public LinearLayout leaverequsLay, attendanceLay, stationaryLay,docsLay,cabLay,hotelLay,appreceationLay,warningLay;

    public OnFragmentInteractionListenerForToolbar mListener;

    PieChart pieChart,pieChart11 ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;

    BarChart barchart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<String> BarEntryLable;
    ArrayList<BarEntry> entriesbar;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dash_board, container, false);


        String strtext = getArguments().getString("Count");
        Log.e("checking count for dashboard fragment",strtext + " null");

        //transfer data fragment to other Fragment
        Bundle bundle = new Bundle();
        bundle.putString("Count", strtext);

        mListener.onFragmentInteractionForToolbarMethod(0,"DashBoard",strtext);

        leaverequsLay = (LinearLayout)rootView.findViewById(R.id.leavereq);
        attendanceLay = (LinearLayout)rootView.findViewById(R.id.attendance_lay);
        stationaryLay = (LinearLayout)rootView.findViewById(R.id.stationary_lay);
        docsLay = (LinearLayout)rootView.findViewById(R.id.docs_lay);
        cabLay = (LinearLayout)rootView.findViewById(R.id.cab_lay);
        hotelLay = (LinearLayout)rootView.findViewById(R.id.hotel_lay);
        appreceationLay = (LinearLayout)rootView.findViewById(R.id.appre_lay);
//        warningLay = (LinearLayout)rootView.findViewById(R.id.warning_lay);

        attendanceLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), AttendanceModule.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

            }
        });

        stationaryLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteractionForToolbarMethod(18,"Stationary Request",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new StationaryRequestFragment();
                frag.setArguments(bundle);

                /*fragmentManager.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);*/
                // update the main content by replacing fragments
                fragmentManager.beginTransaction()
                        .replace(R.id.container, frag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();

            }
        });

        docsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteractionForToolbarMethod(19 , "Document List",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new DocumentListFragment();
                frag.setArguments(bundle);

                /*fragmentManager.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);*/
                // update the main content by replacing fragments
                fragmentManager.beginTransaction()
                        .replace(R.id.container, frag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();

            }
        });

        cabLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteractionForToolbarMethod(20, "Cab List",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new TaxiListFragment();
                frag.setArguments(bundle);

                /*fragmentManager.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);*/
                // update the main content by replacing fragments
                fragmentManager.beginTransaction()
                        .replace(R.id.container, frag)
                       // .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();

            }
        });

        hotelLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteractionForToolbarMethod(21,"Hotel Booking List",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new HotelBookingListFragment();
                frag.setArguments(bundle);

                /*fragmentManager.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);*/
                // update the main content by replacing fragments
                fragmentManager.beginTransaction()
                        .replace(R.id.container, frag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });

        appreceationLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteractionForToolbarMethod(220,"Appreciation",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new AppreceationFragment();
                frag.setArguments(bundle);

                /*fragmentManager.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);*/
                // update the main content by replacing fragments
                fragmentManager.beginTransaction()
                        .replace(R.id.container, frag)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            }
        });

//        warningLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mListener.onFragmentInteractionForToolbarMethod(221,"Warning",strtext);
//
//                FragmentManager fragmentManager = getFragmentManager();
//                Fragment frag = new WarningFragment();
//                frag.setArguments(bundle);
//
//                /*fragmentManager.setCustomAnimations(
//                        R.anim.push_right_in,
//                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);*/
//                // update the main content by replacing fragments
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, frag)
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

        leaverequsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(getActivity(),NewAddLeaveMangementActivity.class);
                startActivity(ik);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

            }
        });

        pieChart = (PieChart)rootView.findViewById(R.id.chart1);

        barchart = (BarChart) rootView.findViewById(R.id.chart2);

        pieChart11 = (PieChart)rootView.findViewById(R.id.chart11);

        entries = new ArrayList<>();

        entriesbar = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        BarEntryLable = new ArrayList<String>();

        AddValuesToPIEENTRY();

        AddValuesToBarENTRY();

        AddValuesToPieEntryLabels();

        AddValuesToBarEntryLabels();

        pieDataSet = new PieDataSet(entries, "");
        barDataSet = new BarDataSet(entriesbar,"");

        pieData = new PieData(PieEntryLabels, pieDataSet);
        barData = new BarData(BarEntryLable,barDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);
        pieChart11.setData(pieData);
        barchart.setData(barData);

        pieChart.animateY(3000);
        pieChart11.animateY(3000);
        barchart.animateY(3000);

        pieChart.setHighlightPerTapEnabled(true);
        pieChart11.setHighlightPerTapEnabled(true);
        barchart.setHighlightPerTapEnabled(true);

        pieChart.setCenterText("Machine Status");

        pieChart.setDescription(null);

        pieChart11.setDescription(null);

        barchart.setDescription(null);

        pieChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //fire up event

                //   Toast.makeText(Dashboard.this, "item Clicked "+dataSetIndex +" "+ e, Toast.LENGTH_SHORT).show();
                if(e.getXIndex() == 0){

                    //    Toast.makeText(DashboardActivity.this, "item Clicked 0 "+dataSetIndex +" "+ e, Toast.LENGTH_SHORT).show();

                } else if(e.getXIndex() == 1){

                    //   Toast.makeText(DashboardActivity.this, "item Clicked 1"+dataSetIndex +" "+ e, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected() {

            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerForToolbar) {
            mListener = (OnFragmentInteractionListenerForToolbar) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListenerForToolbar {
        // TODO: Update argument type and name
        void onFragmentInteractionForToolbarMethod(int navigationCount, String Titile, String count);
    }


    public void AddValuesToPIEENTRY(){

        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(4f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(8f, 3));

    }

    public void AddValuesToPieEntryLabels(){

        PieEntryLabels.add("AMC");
        PieEntryLabels.add("Warranty");
        PieEntryLabels.add("Paid");
        PieEntryLabels.add("FOC");


    }

    public void AddValuesToBarENTRY(){
        entriesbar.add(new BarEntry(7, 0));
        entriesbar.add(new BarEntry(5, 1));
        entriesbar.add(new BarEntry(0, 2));
        entriesbar.add(new BarEntry(0, 3));
        entriesbar.add(new BarEntry((float) 22.5, 4));
        entriesbar.add(new BarEntry(17, 5));
    }

    public void AddValuesToBarEntryLabels(){
        BarEntryLable.add("CL");
        BarEntryLable.add("CL_Remain");
        BarEntryLable.add("Comp.Off");
        BarEntryLable.add("Comp.Off_Remain");
        BarEntryLable.add("PL");
        BarEntryLable.add("PL_Remain");

    }
}
