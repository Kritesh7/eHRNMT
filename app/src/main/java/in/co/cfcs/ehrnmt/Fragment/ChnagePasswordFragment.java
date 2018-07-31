package in.co.cfcs.ehrnmt.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChnagePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChnagePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChnagePasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public EditText nameTxt,emailidTxt,oldPass,newPass,confirmPass;
    public Button chngBtn;
    public String authCodeString = "",nameTxtString = "",emailIdTxtString = "",userIdString = "";
    public ConnectionDetector conn;
    public String changePassUrl = SettingConstant.BASEURL_FOR_LOGIN + "AppUserChangePassword";

    private OnFragmentInteractionListener mListener;

    public ChnagePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChnagePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChnagePasswordFragment newInstance(String param1, String param2) {
        ChnagePasswordFragment fragment = new ChnagePasswordFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_employ_data, container, false);

        String strtext = getArguments().getString("Count");
        Log.e("checking count",strtext + " null");

        mListener.onFragmentInteraction(strtext);

        nameTxt = (EditText)rootView.findViewById(R.id.name_txt);
        emailidTxt = (EditText) rootView.findViewById(R.id.emailid);
        oldPass = (EditText) rootView.findViewById(R.id.oldpass);
        newPass = (EditText) rootView.findViewById(R.id.newpass);
        confirmPass = (EditText)rootView.findViewById(R.id.confirmpass);
        chngBtn = (Button)rootView.findViewById(R.id.changepass);

        nameTxt.setFocusable(false);
        nameTxt.setEnabled(false);

        emailidTxt.setFocusable(false);
        emailidTxt.setEnabled(false);

        authCodeString =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));
        userIdString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        nameTxtString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserName(getActivity())));
        emailIdTxtString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getEmailId(getActivity())));
        conn = new ConnectionDetector(getActivity());

        emailidTxt.setText(emailIdTxtString);
        nameTxt.setText(nameTxtString);

        chngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (oldPass.getText().toString().equalsIgnoreCase(""))
                {
                    oldPass.setError("Please enter valid old password");
                }else if (newPass.getText().toString().equalsIgnoreCase(""))
                {
                    newPass.setError("Please enter valid new password");
                }
                else if (!confirmPass.getText().toString().equalsIgnoreCase(newPass.getText().toString()))
                {
                    confirmPass.setError("New password and confirm password is not match");
                }else
                    {
                        if (conn.getConnectivityStatus()>0) {

                            changePasswordApi(userIdString, oldPass.getText().toString(), newPass.getText().toString(), authCodeString);
                        }else
                            {
                                conn.showNoInternetAlret();
                            }
                    }

            }
        });

        return rootView;
    }


    public void changePasswordApi(final String UserID  , final String OldPassword, final String NewPassword ,
                                  final String AuthCode ) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, changePassUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // String status = jsonObject.getString("status");
                        if (jsonObject.has("MsgNotification"))
                        {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            Toast.makeText(getActivity(), MsgNotification, Toast.LENGTH_SHORT).show();
                        }

                    }

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
                params.put("AdminID", UserID);
                params.put("OldPassword",OldPassword);
                params.put("NewPassword",NewPassword);
                params.put("AuthCode",AuthCode);

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
}
