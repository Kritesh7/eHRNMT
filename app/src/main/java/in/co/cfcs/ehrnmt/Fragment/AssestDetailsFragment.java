package in.co.cfcs.ehrnmt.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import in.co.cfcs.ehrnmt.Adapter.AssestsDetailsAdapter;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.AssestDetailsModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssestDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssestDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssestDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RecyclerView assetsRecycler;
    public AssestsDetailsAdapter adapter;
    public ArrayList<AssestDetailsModel> list = new ArrayList<>();
    public String assestsUrl = SettingConstant.BaseUrl + "AppEmployeeAssetList";
    public TextView noCust;
    public ConnectionDetector conn;
    public String userId = "", authCode = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    public AssestDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssestDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssestDetailsFragment newInstance(String param1, String param2) {
        AssestDetailsFragment fragment = new AssestDetailsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_assest_details, container, false);

        String strtext = getArguments().getString("Count");
        Log.e("checking count", strtext + " null");

        mListener.onFragmentInteraction(strtext);

        assetsRecycler = (RecyclerView) rootView.findViewById(R.id.assets_recycler);
        noCust = (TextView) rootView.findViewById(R.id.no_record_txt);

        conn = new ConnectionDetector(getActivity());
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));


        adapter = new AssestsDetailsAdapter(getActivity(), list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        assetsRecycler.setLayoutManager(mLayoutManager);
        assetsRecycler.setItemAnimator(new DefaultItemAnimator());
        assetsRecycler.setAdapter(adapter);

        assetsRecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);

        //prepareInsDetails();

        return rootView;
    }

    /*private void prepareInsDetails() {

        AssestDetailsModel model = new AssestDetailsModel("Laptop","Lenovo","03-09-2017","02-01-2017","For Meeting","Comment to Employ");
        list.add(model);
        model = new AssestDetailsModel("Laptop","Lenovo","03-09-2017","02-01-2017","For Meeting","Comment to Employ");
        list.add(model);
        model = new AssestDetailsModel("Laptop","Lenovo","03-09-2017","02-01-2017","For Meeting","Comment to Employ");
        list.add(model);
        model = new AssestDetailsModel("Laptop","Lenovo","03-09-2017","02-01-2017","For Meeting","Comment to Employ");
        list.add(model);
        model = new AssestDetailsModel("Laptop","Lenovo","03-09-2017","02-01-2017","For Meeting","Comment to Employ");
        list.add(model);


        adapter.notifyDataSetChanged();

    }*/

    @Override
    public void onResume() {
        super.onResume();
        if (conn.getConnectivityStatus() > 0) {

            educationList(authCode, userId);

        } else {
            conn.showNoInternetAlret();
        }
    }


    //Assets list
    public void educationList(final String AuthCode, final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, assestsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    if (list.size() > 0) {
                        list.clear();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object.has("status")) {
                            LoginStatus = object.getString("status");
                            msgstatus = object.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getContext(), msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), msgstatus, Toast.LENGTH_LONG).show();
                            }
                        } else {


                            String AssetsHolder = object.getString("AssetsHolder");
                            String Assets = object.getString("AssetsName");
                            String IssueDate = object.getString("IssueDate");
                            String ExpReturnDate = object.getString("ExpReturnDate");
                            String BrandName = object.getString("BrandName");
                            String SerialNo = object.getString("SerialNo");
                            String Reasion = object.getString("Reasion");
                            String Remarks = object.getString("Remarks");


                            list.add(new AssestDetailsModel(AssetsHolder, Assets, BrandName + " " + SerialNo, IssueDate
                                    , ExpReturnDate, Reasion, Remarks));


                        }

                    }


                    if (list.size() == 0) {
                        noCust.setVisibility(View.VISIBLE);
                        assetsRecycler.setVisibility(View.GONE);
                    } else {
                        noCust.setVisibility(View.GONE);
                        assetsRecycler.setVisibility(View.VISIBLE);
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


    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
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
    }*/

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

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(getActivity(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(getActivity(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(getActivity(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(getActivity(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(getActivity(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(getActivity(),
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(getActivity(),
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(getActivity(),
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(getActivity(),
                "")));


//
//        Intent intent = new Intent(getContext(), LoginActivity.class);
//        startActivity(intent);


    }
}
