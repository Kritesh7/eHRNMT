package in.co.cfcs.ehrnmt.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.cfcs.ehrnmt.Main.AttendanceModule;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Main.NewAddLeaveMangementActivity;
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerRequestToApproveActivity;
import in.co.cfcs.ehrnmt.Model.LeaveSummarryModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


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
    public LinearLayout leaverequsLay, attendanceLay, stationaryLay,docsLay,cabLay,hotelLay,appreceationLay,warningLay,
            req_approve,holiday,log_report,attendence_list;

    public OnFragmentInteractionListenerForToolbar mListener;

    public ArrayList<LeaveSummarryModel> list ;

    PieChart pieChart,pieChart11 ;
    ArrayList entries ;
    ArrayList PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;

    BarChart barchart;
    BarData barData;
    BarDataSet barDataSet;
    List<String> BarEntryLable;
    List<BarEntry> entriesbar;


    float barWidth;
    float barSpace;
    float groupSpace;


    public String leaveSummeryUrl = SettingConstant.BaseUrl + "AppEmployeeLeaveSummaryList";
    public String attendanceSummeryUrl = SettingConstant.BaseUrl + "AppEmployeeAttendanceChart";

    public String userId = "", authCode = "";
    public ConnectionDetector conn;

    List<String> barvaluelistLeaveTaken;
    List<String> barvaluelistLeaveTotal;
    List<String> barvaluelistLeaveAvail;
    ArrayList<String> barColor;


    List<String> PieEntryLable;
    List<String> pievalue;


    String currentVersion = null;

    String month ="";
    String year ="";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;


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
    //    Log.e("checking count for dashboard fragment",strtext + " null");

        //transfer data fragment to other Fragment
        Bundle bundle = new Bundle();
        bundle.putString("Count", strtext);

        list = new ArrayList<>();

        mListener.onFragmentInteractionForToolbarMethod(0,"DashBoard",strtext);

        leaverequsLay = (LinearLayout)rootView.findViewById(R.id.leavereq);
        attendanceLay = (LinearLayout)rootView.findViewById(R.id.attendance_lay);
        attendence_list = (LinearLayout)rootView.findViewById(R.id.attendence_list);
        stationaryLay = (LinearLayout)rootView.findViewById(R.id.stationary_lay);
        docsLay = (LinearLayout)rootView.findViewById(R.id.docs_lay);
        cabLay = (LinearLayout)rootView.findViewById(R.id.cab_lay);
        hotelLay = (LinearLayout)rootView.findViewById(R.id.hotel_lay);
        appreceationLay = (LinearLayout)rootView.findViewById(R.id.appre_lay);
        warningLay = (LinearLayout)rootView.findViewById(R.id.warning_lay);
       // req_approve = (LinearLayout)rootView.findViewById(R.id.req_approve);
        holiday = (LinearLayout)rootView.findViewById(R.id.holiday);
        log_report = (LinearLayout)rootView.findViewById(R.id.log_report);

        conn = new ConnectionDetector(getActivity());

        try {
            currentVersion = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));


        barWidth = 0.2f;
        barSpace = 0f;
        groupSpace = 0.4f;


        if (conn.getConnectivityStatus()>0) {

          //  new ForceUpdateAsync(currentVersion).execute();

            leaveSummeryData(authCode, userId);

            AttendanceSummaryData(authCode,userId,userId,month,year);

        }else
        {
            conn.showNoInternetAlret();
        }

        attendanceLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), AttendanceModule.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

            }
        });

        attendence_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteractionForToolbarMethod(18,"Attendence Request",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new AttendaceListFragment();
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

//        req_approve.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent ik = new Intent(getActivity(),ManagerRequestToApproveActivity.class);
//                startActivity(ik);
//                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
//            }
//        });

        holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteractionForToolbarMethod(201, "Holiday List",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new HolidayListFragment();
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

        log_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFragmentInteractionForToolbarMethod(202, "Log List",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new AttendanceLogListFragment();
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

        warningLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteractionForToolbarMethod(221,"Warning",strtext);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment frag = new WarningFragment();
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

        leaverequsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(getActivity(),NewAddLeaveMangementActivity.class);
                startActivity(ik);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

            }
        });

        barchart = (BarChart)rootView.findViewById(R.id.chart2);
        barchart.setDescription(null);
        barchart.setPinchZoom(false);
        barchart.setScaleEnabled(false);
        barchart.setDrawBarShadow(false);
        barchart.setDrawGridBackground(false);
        barchart.animateY(3000);


        pieChart = (PieChart)rootView.findViewById(R.id.chart1);
        pieChart.setDescription(null);
        pieChart.animateY(3000);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setMaxAngle(180f); // HALF CHART
        pieChart.setRotationAngle(180f);
        pieChart.setCenterTextOffset(0, -00);
        pieChart.setExtraOffsets(0,5,0,-110);
        pieChart.setCenterText("Attendance");

        return rootView;
    }

    private void AttendanceSummaryData(String authCode, String userId, String logInid, String month, String year) {


        PieEntryLable = new ArrayList<>();
        pievalue = new ArrayList<>();





        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, attendanceSummeryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("status")) {
                            LoginStatus = jsonObject.getString("status");
                            msgstatus = jsonObject.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            String ValueText = jsonObject.getString("ValueText");
                            String ValueCount = jsonObject.getString("ValueCount");

                            PieEntryLable.add(i,ValueText);
                            pievalue.add(i, ValueCount);

                        }


                    }

                    checkMethodPie();

                    // adapter.notifyDataSetChanged();
                   // pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption" , e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(),
                            "Time Out Server Not Respond",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getActivity(),
                            "Server Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getActivity(),
                            "Network Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(getActivity(),
                            "Parse Error",
                            Toast.LENGTH_LONG).show();
                }
              //  pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",authCode);
                params.put("LoginAdminID",logInid);
                params.put("EmployeeID",userId);
                params.put("Month",month);
                params.put("Year",year);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");
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



    //Leave Summery List
    public void leaveSummeryData(final String AuthCode , final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        barvaluelistLeaveTaken = new ArrayList<>();
        barvaluelistLeaveTotal = new ArrayList<>();
        BarEntryLable = new ArrayList<String>();
        barvaluelistLeaveAvail = new ArrayList<String>();
        barColor = new ArrayList<String>();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, leaveSummeryUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("status")) {
                            LoginStatus = jsonObject.getString("status");
                            msgstatus = jsonObject.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            String LeaveTypeName = jsonObject.getString("LeaveTypeName");
                            String LeaveYear = jsonObject.getString("LeaveYear");
                            String EntitledFor = jsonObject.getString("LeaveAvailable");
                            String LeaveCarryOver = jsonObject.getString("LeaveCarryOver");
                            String LeaveTaken = jsonObject.getString("LeaveTaken");
                            String LeaveBalance = jsonObject.getString("LeaveBalance");
                            String LeaveAvail = jsonObject.getString("LeaveAvail");
                            String SPLeaveText = jsonObject.getString("SPLeaveText");
                            String Color = jsonObject.getString("Color");

                            float entitled = Float.parseFloat(EntitledFor);
                            float carryOver = Float.parseFloat(LeaveCarryOver);
                            float totalLeave = entitled+carryOver;

                            BarEntryLable.add(i,LeaveTypeName);
                            barvaluelistLeaveTotal.add(i, String.valueOf(totalLeave));
                            barvaluelistLeaveTaken.add(i,LeaveTaken);
                            barvaluelistLeaveAvail.add(i,LeaveAvail);
                            barColor.add(i, Color);

                        }


                    }

                    checkMethod();

                   // adapter.notifyDataSetChanged();
                    pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption" , e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(),
                            "Time Out Server Not Respond",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getActivity(),
                            "Server Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getActivity(),
                            "Network Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(getActivity(),
                            "Parse Error",
                            Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("LoginAdminID",AdminID);
                params.put("EmployeeID",AdminID);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    private void checkMethodPie() {

        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        for (int i = 0; i < PieEntryLable.size(); i++)
            yVals1.add(new PieEntry(Float.parseFloat(pievalue.get(i)),PieEntryLable.get(i)));


        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // instantiate pie data object now
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        // update pie chart
        pieChart.invalidate();

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

    }

    private void checkMethod() {

        int groupCount = 0;

        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();
        List<BarEntry> entriesGroup3 = new ArrayList<>();
        List<Integer>  barColourGroup = new ArrayList<>();


        // fill the lists
        for(int i = 0; i < barvaluelistLeaveTotal.size(); i++) {
            entriesGroup1.add(new BarEntry(i, Float.parseFloat(barvaluelistLeaveTotal.get(i))));
            entriesGroup2.add(new BarEntry(i, Float.parseFloat(barvaluelistLeaveTaken.get(i))));
            entriesGroup3.add(new BarEntry(i, Float.parseFloat(barvaluelistLeaveAvail.get(i))));
            groupCount += 1;
            int colour = Color.parseColor(barColor.get(i));
            barColourGroup.add(i,colour);

        }

        float barwidthActoGroup = (float) (groupCount / 20.0);

        BarDataSet set1, set2,set3;
        set1 = new BarDataSet(entriesGroup1, "Entitlements");
        set1.setColors(barColourGroup);
        set2 = new BarDataSet(entriesGroup2, "Taken");
        set2.setColor(Color.RED);
        set3 = new BarDataSet(entriesGroup3, "Month Avail");
        set3.setColor(Color.GREEN);
        BarData data = new BarData(set1, set2, set3);
        data.setValueFormatter(new LargeValueFormatter());
       // data.setBarWidth(0.5f);
        barchart.setData(data);
        barchart.getBarData().setBarWidth(barwidthActoGroup);
        barchart.getXAxis().setAxisMinimum(0);
        barchart.getXAxis().setAxisMaximum(0 + barchart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barchart.groupBars(0, groupSpace, barSpace);
        barchart.getData().setHighlightEnabled(false);
        barchart.invalidate();


        Legend l = barchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = barchart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(groupCount);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(BarEntryLable));
//Y-axis
        barchart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barchart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);




    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;

        public ForceUpdateAsync(String currentVersion) {
            this.currentVersion = currentVersion;

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null && !latestVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(latestVersion)) {
                    //show dialog
                    showForceUpdateDialog();
                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showForceUpdateDialog() {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(new ContextThemeWrapper(getActivity(),
                    R.style.AppTheme));

            alertDialogBuilder.setTitle(getActivity().getString(R.string.youAreNotUpdatedTitle));
            alertDialogBuilder.setMessage(getActivity().getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + getActivity().getString(R.string.youAreNotUpdatedMessage1));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.update1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                    dialog.cancel();
                }
            });
            alertDialogBuilder.show();
        }
    }

    private void Logout() {


        getActivity().finishAffinity();
        startActivity(new Intent(getContext(), LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(getContext(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(getContext(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(getContext(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(getContext(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(getContext(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(getContext(),
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(getContext(),
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(getContext(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(getContext(),
                "")));


    }

}
