package in.co.cfcs.ehrnmt.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactPhoneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactPhoneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public EditText phoneNoTxt, emailTxt, altPhoneTxt, altEmailTxt;
    public Button editBtn;
    public String contactUrl = SettingConstant.BaseUrl + "AppEmployeeAddressList";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeTelephoneInsUpdt";
    public ConnectionDetector conn;
    public String authcode = "", userid = "", RecordID = "";
    private AwesomeValidation awesomeValidation;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    public ContactPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactPhoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactPhoneFragment newInstance(String param1, String param2) {
        ContactPhoneFragment fragment = new ContactPhoneFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_contact_phone, container, false);

        String strtext = getArguments().getString("Count");
        Log.e("checking count", strtext + " null");

        mListener.onFragmentInteraction(strtext);

        conn = new ConnectionDetector(getActivity());
        userid = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        phoneNoTxt = (EditText) rootView.findViewById(R.id.phoneno);
        altPhoneTxt = (EditText) rootView.findViewById(R.id.altphoneno);
        emailTxt = (EditText) rootView.findViewById(R.id.email);
        altEmailTxt = (EditText) rootView.findViewById(R.id.altemail);
        editBtn = (Button) rootView.findViewById(R.id.editphonebtn);

        awesomeValidation.addValidation(getActivity(), R.id.phoneno, Patterns.PHONE, R.string.phone_txt);
        awesomeValidation.addValidation(getActivity(), R.id.email, Patterns.EMAIL_ADDRESS, R.string.email_txt);


        if (conn.getConnectivityStatus() > 0) {
            contactDetailsList(authcode, userid);

        } else {
            conn.showNoInternetAlret();
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editBtn.getText().toString().equalsIgnoreCase("Edit Contact")) {
                    editBtn.setText("Update Contact");
                }
                // if (awesomeValidation.validate()) {
                if (phoneNoTxt.getText().toString().equalsIgnoreCase("")) {
                    phoneNoTxt.setError("Please enter mobile number");
                } else if (emailTxt.getText().toString().equalsIgnoreCase("")) {
                    emailTxt.setError("Please enter email id");
                } else {
                    if (conn.getConnectivityStatus() > 0) {

                        if (editBtn.getText().toString().equalsIgnoreCase("Update Contact")) {

                            addContact(userid, RecordID, phoneNoTxt.getText().toString(), altPhoneTxt.getText().toString(),
                                    authcode, emailTxt.getText().toString(), altEmailTxt.getText().toString());
                        } else {
                            addContact(userid, "", phoneNoTxt.getText().toString(), altPhoneTxt.getText().toString(),
                                    authcode, emailTxt.getText().toString(), altEmailTxt.getText().toString());
                        }
                    } else {
                        conn.showNoInternetAlret();
                    }
                }
                // }

            }
        });


        return rootView;
    }

    //Contact Number
    public void contactDetailsList(final String AuthCode, final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, contactUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

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

                        JSONArray jsonArray = jsonObject.getJSONArray("EmpTelephone");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String PhoneNo = object.getString("PhoneNo");
                            String MobileNo = object.getString("MobileNo");
                            String EmailPersonal = object.getString("EmailPersonal");
                            String EmailCorporate = object.getString("EmailCorporate");
                            RecordID = object.getString("RecordID");

                            //set text in edit text
                            phoneNoTxt.setText(PhoneNo);
                            altPhoneTxt.setText(MobileNo);
                            emailTxt.setText(EmailPersonal);
                            altEmailTxt.setText(EmailCorporate);


                        }

                        Log.e("checking lenth of array", jsonArray.length() + "");
                        if (jsonArray.length() == 0) {
                            editBtn.setText("Add Contact");
                        } else {
                            editBtn.setText("Edit Contact");
                        }


                        JSONArray statusArray = jsonObject.getJSONArray("Status");
                        for (int k = 0; k < statusArray.length(); k++) {
                            JSONObject obj = statusArray.getJSONObject(k);

                            String IsAddAddressDetail = obj.getString("IsAddAddressDetail");

                            if (!IsAddAddressDetail.equalsIgnoreCase("0")) {
                                editBtn.setBackgroundColor(getActivity().getResources().getColor(R.color.disbale_color));
                                editBtn.setEnabled(false);
                                Toast.makeText(getActivity(), "Your Previous request waiting for Hr approval.", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }


                    pDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("checking json excption", e.getMessage());
                    e.printStackTrace();
                    // Logout();
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

    //add contact
    public void addContact(final String AdminID, final String RecordID, final String PhoneNo, final String MobileNo,
                           final String AuthCode, final String EmailPersonal, final String EmailCorporate) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, addUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));


                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getContext(), msgstatus, Toast.LENGTH_LONG).show();
                        } else if (LoginStatus.equalsIgnoreCase("success")) {
                            contactDetailsList(AuthCode, AdminID);
                            // Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(getContext(), msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }

//                    if (jsonObject.has("status"))
//                    {
//                        String status = jsonObject.getString("status");
//
//                        if (jsonObject.has("MsgNotification")) {
//                            String MsgNotification = jsonObject.getString("MsgNotification");
//
//                            Toast.makeText(getActivity(), MsgNotification, Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        if (status.equalsIgnoreCase("success"))
//                        {
//                            //onBackPressed();
//                            contactDetailsList(AuthCode,AdminID);
//
//                        }
//                    }


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
                params.put("AdminID", AdminID);
                params.put("AuthCode", AuthCode);
                params.put("RecordID", RecordID);
                params.put("PhoneNo", PhoneNo);
                params.put("MobileNo", MobileNo);
                params.put("EmailPersonal", EmailPersonal);
                params.put("EmailCorporate", EmailCorporate);

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
