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

import in.co.cfcs.ehrnmt.Adapter.DocumentListAdapter;
import in.co.cfcs.ehrnmt.Main.AddDocumentActivity;
import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.BookMeaPrevisionModel;
import in.co.cfcs.ehrnmt.Model.DocumentListModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DocumentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DocumentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecyclerView documentRecycler;
    public DocumentListAdapter adapter;
    public ConnectionDetector conn;
    public String userId = "",authCode = "";
    public ArrayList<DocumentListModel> list = new ArrayList<>();
    public String documentListUrl = SettingConstant.BaseUrl + "AppEmployeeStationaryRequestList";
    public FloatingActionButton fab;
    private OnFragmentInteractionListener mListener;
    public ArrayList<BookMeaPrevisionModel> itemBindList = new ArrayList<>();
    public TextView noCust ;
    public String strtext = "";

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;


    public DocumentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentListFragment newInstance(String param1, String param2) {
        DocumentListFragment fragment = new DocumentListFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_document_list, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            strtext = bundle.getString("Count");
        }else {
            strtext = getArguments().getString("Count");
        }
        Log.e("checking count",strtext + " null");
        mListener.onFragmentInteraction(strtext);

        documentRecycler = (RecyclerView)rootView.findViewById(R.id.document_recycler);
        fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        noCust = (TextView) rootView.findViewById(R.id.no_record_txt);

        conn = new ConnectionDetector(getActivity());
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        authCode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));

        adapter = new DocumentListAdapter(getActivity(),list,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        documentRecycler.setLayoutManager(mLayoutManager);
        documentRecycler.setItemAnimator(new DefaultItemAnimator());
        documentRecycler.setAdapter(adapter);

        documentRecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), AddDocumentActivity.class);
                i.putExtra("Mode", "Add");
                i.putExtra("mylist", itemBindList);
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

            documentListData(authCode,userId,"0","2");

        }else
        {
            conn.showNoInternetAlret();
        }
    }

   /* private void prepareInsDetails() {

        DocumentListModel model = new DocumentListModel("Raman Kumar","East","2","03-09-2017","02-01-2017","10-01-2017",
                "Approved");
        list.add(model);
        model = new DocumentListModel("Raman Kumar","East","2","03-09-2017","02-01-2017","10-01-2017",
                "Approved");
        list.add(model);
        model = new DocumentListModel("Raman Kumar","East","2","03-09-2017","02-01-2017","10-01-2017",
                "Approved");
        list.add(model);
        model = new DocumentListModel("Raman Kumar","East","2","03-09-2017","02-01-2017","10-01-2017",
                "Approved");
        list.add(model);
        model = new DocumentListModel("Raman Kumar","East","2","03-09-2017","02-01-2017","10-01-2017",
                "Approved");
        list.add(model);


        adapter.notifyDataSetChanged();

    }*/

    //Document List Data
    public void documentListData(final String AuthCode , final String AdminID, final String AppStatus, final String ItemCatID) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, documentListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    if (list.size()>0)
                    {
                        list.clear();
                    }
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

                            String EmployeeName = jsonObject.getString("EmployeeName");
                            String ZoneName = jsonObject.getString("ZoneName");
                            String Quantity = jsonObject.getString("Items");
                            String requestDate = jsonObject.getString("AddDateText");
                            String IdealClosureDateText = jsonObject.getString("IdealClosureDateText");
                            String followDate = jsonObject.getString("AppDateText");
                            String AppStatusText = jsonObject.getString("AppStatusText");
                            String RID = jsonObject.getString("RID");
                            String ItemCatID = jsonObject.getString("ItemCatID");
                            String AppStatus = jsonObject.getString("AppStatus");

                            list.add(new DocumentListModel(EmployeeName,ZoneName,Quantity,requestDate,IdealClosureDateText
                                    ,followDate,AppStatusText,RID,ItemCatID,AppStatus));
                        }

                    }

                    if (list.size() == 0)
                    {
                        noCust.setVisibility(View.VISIBLE);
                        documentRecycler.setVisibility(View.GONE);
                    }else
                    {
                        noCust.setVisibility(View.GONE);
                        documentRecycler.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();
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

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode",AuthCode);
                params.put("AdminID",AdminID);
                params.put("AppStatus",AppStatus);
                params.put("ItemCatID",ItemCatID);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }
   /* // TODO: Rename method, update argument and hook method into UI event
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
