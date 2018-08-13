package in.co.cfcs.ehrnmt.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Adapter.MedicalAnssuredAdapter;
import in.co.cfcs.ehrnmt.Main.AddMedicalandAnssuranceActivity;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.MedicalAnssuranceModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicalAndEnsuranceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicalAndEnsuranceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicalAndEnsuranceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MedicalAnssuredAdapter adapter;
    public ArrayList<MedicalAnssuranceModel> list = new ArrayList<>();
    public RecyclerView medicalAnssuredRecy;
    public FloatingActionButton fab;
    public String policyUrl = SettingConstant.BaseUrl + "AppEmployeeMedicalPolicy";
    public ConnectionDetector conn;
    public String userId = "", authCode = "";
    public TextView noCust;

    private OnFragmentInteractionListener mListener;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    public MedicalAndEnsuranceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicalAndEnsuranceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicalAndEnsuranceFragment newInstance(String param1, String param2) {
        MedicalAndEnsuranceFragment fragment = new MedicalAndEnsuranceFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_medical_and_ensurance, container, false);

        String strtext = getArguments().getString("Count");
        Log.e("checking count", strtext + " null");

        mListener.onFragmentInteraction(strtext);

        medicalAnssuredRecy = (RecyclerView) rootView.findViewById(R.id.medical_anssured_recycler);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        noCust = (TextView) rootView.findViewById(R.id.no_record_txt);


        conn = new ConnectionDetector(getActivity());
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));


        adapter = new MedicalAnssuredAdapter(getActivity(), list, getActivity(), "FirstOne");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        medicalAnssuredRecy.setLayoutManager(mLayoutManager);
        medicalAnssuredRecy.setItemAnimator(new DefaultItemAnimator());
        medicalAnssuredRecy.setAdapter(adapter);

        medicalAnssuredRecy.getRecycledViewPool().setMaxRecycledViews(0, 0);

        //prepareInsDetails();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), AddMedicalandAnssuranceActivity.class);
                i.putExtra("RecordId", "");
                i.putExtra("Mode", "AddMode");
                i.putExtra("PolicyType", "");
                i.putExtra("PolicyName", "");
                i.putExtra("PolicyNumber", "");
                i.putExtra("PolicyDuration", "");
                i.putExtra("PolicyBy", "");
                i.putExtra("InsuranceCompany", "");
                i.putExtra("AmountInsured", "");
                i.putExtra("StartDate", "");
                i.putExtra("EndDate", "");
                i.putExtra("File", "");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (conn.getConnectivityStatus() > 0) {

            medicalAndAnssuranceList(authCode, userId);

        } else {
            conn.showNoInternetAlret();
        }
    }
   /* private void prepareInsDetails() {

        MedicalAnssuranceModel model = new MedicalAnssuranceModel("Full Time","28032017","6 Months","Adahr Policy","5000 Rs.");
        list.add(model);
        model = new MedicalAnssuranceModel("Full Time","28032017","6 Months","Adahr Policy","5000 Rs.");
        list.add(model);
        model = new MedicalAnssuranceModel("Full Time","28032017","6 Months","Adahr Policy","5000 Rs.");
        list.add(model);
        model = new MedicalAnssuranceModel("Full Time","28032017","6 Months","Adahr Policy","5000 Rs.");
        list.add(model);
        model = new MedicalAnssuranceModel("Full Time","28032017","6 Months","Adahr Policy","5000 Rs.");
        list.add(model);

        adapter.notifyDataSetChanged();

    }*/

    // Medical and Anssurance data
    public void medicalAndAnssuranceList(final String AuthCode, final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, policyUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    if (list.size() > 0) {
                        list.clear();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("status")) {
                            LoginStatus = jsonObject.getString("status");
                            msgstatus = jsonObject.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getContext(), msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), msgstatus, Toast.LENGTH_LONG).show();
                            }
                        } else {

                            String Name = jsonObject.getString("Name");
                            String Number = jsonObject.getString("Number");
                            String Duration = jsonObject.getString("Duration");
                            String AmountInsured = jsonObject.getString("AmountInsured");
                            String PolicyTypeName = jsonObject.getString("PolicyTypeName");
                            String PolicyBy = jsonObject.getString("PolicyBy");
                            String RecordID = jsonObject.getString("RecordID");
                            String InsuranceCompany = jsonObject.getString("InsuranceCompany");
                            String StartDate = jsonObject.getString("StartDate");
                            String EndDate = jsonObject.getString("EndDate");
                            String FileNameText = jsonObject.getString("FileNameText");


                            list.add(new MedicalAnssuranceModel(PolicyTypeName, Number, Duration, Name, AmountInsured, PolicyBy, RecordID,
                                    InsuranceCompany, StartDate, EndDate, FileNameText));

                        }


                    }

                    if (list.size() == 0) {
                        noCust.setVisibility(View.VISIBLE);
                        medicalAnssuredRecy.setVisibility(View.GONE);
                    } else {
                        noCust.setVisibility(View.GONE);
                        medicalAnssuredRecy.setVisibility(View.VISIBLE);
                    }


                    adapter.notifyDataSetChanged();
                    pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption", e.getMessage());
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode", AuthCode);
                params.put("LoginAdminID", AdminID);
                params.put("EmployeeID", AdminID);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String count);
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
