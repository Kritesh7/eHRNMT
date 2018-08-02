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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Adapter.EmergencyContactAdapter;
import in.co.cfcs.ehrnmt.Main.AddNewEmergencyContactDetailsActivity;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.EmergencyContactModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmergencyContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmergencyContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmergencyContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecyclerView emergencyContactRecyler;
    public FloatingActionButton fab;
    public ArrayList<EmergencyContactModel> list = new ArrayList<>();
    public ConnectionDetector conn;
    public EmergencyContactAdapter adapter;
    public String userId = "", authCode = "";
    public String emergencyContactUrl = SettingConstant.BaseUrl + "AppEmployeeEmergencyContactList";
    public TextView noCust;



    private OnFragmentInteractionListener mListener;

    public EmergencyContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmergencyContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmergencyContactsFragment newInstance(String param1, String param2) {
        EmergencyContactsFragment fragment = new EmergencyContactsFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_emergency_contacts, container, false);

        String strtext = getArguments().getString("Count");
        Log.e("checking count",strtext + " null");

        mListener.onFragmentInteraction(strtext);

        emergencyContactRecyler = (RecyclerView) rootView.findViewById(R.id.emergency_contact_recycler);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        noCust = (TextView) rootView.findViewById(R.id.no_record_txt);

        conn = new ConnectionDetector(getActivity());
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));

        adapter = new EmergencyContactAdapter(getActivity(),list,getActivity(),"FirstOne");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        emergencyContactRecyler.setLayoutManager(mLayoutManager);
        emergencyContactRecyler.setItemAnimator(new DefaultItemAnimator());
        emergencyContactRecyler.setAdapter(adapter);

        emergencyContactRecyler.getRecycledViewPool().setMaxRecycledViews(0, 0);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), AddNewEmergencyContactDetailsActivity.class);
                i.putExtra("RecordId","0");
                i.putExtra("Mode","AddMode");
                i.putExtra("Title","");
                i.putExtra("Name","");
                i.putExtra("Type","");
                i.putExtra("RelationshipName","");
                i.putExtra("Address","");
                i.putExtra("City","");
                i.putExtra("State", "");
                i.putExtra("PostalCode", "");
                i.putExtra("CountryName","");
                i.putExtra("TelephoneNumber","");
                i.putExtra("MobileNumber","");
                i.putExtra("Email","");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (conn.getConnectivityStatus()>0) {

            emergencyContactDetailsList(authCode,userId);

        }else
        {
            conn.showNoInternetAlret();
        }
    }


    // dependent List
    public void emergencyContactDetailsList(final String AuthCode , final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, emergencyContactUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    if (list.size()>0)
                    {
                        list.clear();
                    }
                    JSONArray emergencyContactArray = jsonObject.getJSONArray("EmergencyContactList");
                    for (int i=0 ; i<emergencyContactArray.length();i++)
                    {
                        JSONObject object = emergencyContactArray.getJSONObject(i);

                        String Title = object.getString("Title");
                        String Name = object.getString("Name");
                        String Address = object.getString("Address");
                        String City = object.getString("City");
                        String State = object.getString("State");
                        String CountryName = object.getString("CountryName");
                        String PostCode = object.getString("PostCode");
                        String PhoneNo = object.getString("PhoneNo");
                        String MobileNo = object.getString("MobileNo");
                        String Email = object.getString("Email");
                        String RelationshipName = object.getString("RelationshipName");
                        String Type = object.getString("Type");
                        String LastUpdate = object.getString("LastUpdate");
                        String RecordID = object.getString("RecordID");



                        list.add(new EmergencyContactModel(Title , Name ,Address,City,State,PostCode,CountryName,PhoneNo,MobileNo,
                                Email,RelationshipName,LastUpdate,Type,RecordID));



                    }

                    JSONArray statusArray = jsonObject.getJSONArray("Status");
                    for (int k =0; k<statusArray.length(); k++)
                    {
                        JSONObject obj = statusArray.getJSONObject(k);

                        String IsVisibilityAdd = obj.getString("IsVisibilityAdd");
                        if (IsVisibilityAdd.equalsIgnoreCase("2"))
                        {
                            fab.setVisibility(View.GONE);
                        }else
                            {
                                fab.setVisibility(View.VISIBLE);
                            }
                    }

                    if (list.size() == 0)
                    {
                        noCust.setVisibility(View.VISIBLE);
                        emergencyContactRecyler.setVisibility(View.GONE);
                    }else
                    {
                        noCust.setVisibility(View.GONE);
                        emergencyContactRecyler.setVisibility(View.VISIBLE);
                    }


                    adapter.notifyDataSetChanged();
                    pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption" , e.getMessage());
                    e.printStackTrace();
                    Logout();
                    Toast.makeText(getActivity(), "Invalid Login", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());
                // Log.e("checking now ",error.getMessage());

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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