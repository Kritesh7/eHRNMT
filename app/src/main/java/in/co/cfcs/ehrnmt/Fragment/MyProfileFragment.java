package in.co.cfcs.ehrnmt.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TextView titleTxt, nameTxt, fatherNameTxt, dobTxt, genderTxt, prefferdNameTxt, materialStatusTxt, noOfChildTxt, nationloityTxt
            , emailtxt, alternativeEmailTxt, panNoTxt, passportNameTxt, passportNumberTxt, mobileNumberTxt, phoneNumberTxt, personalEmailTxt
            , coprativeEmailTxt, empIdTxt, compNameTxt, zoneNameTxt, designationTxt, departmentTxt, joingDateTxt, managerNameTxt
            , bloodGroupTxt, allergiesTxt, seriousIllnessTxt, familyDrNameTxt, famiilyDrNoTxt;
    public LinearLayout noOfChildLay;
    public String empId = "";
    public ConnectionDetector conn;
    public String personalInfoUrl = SettingConstant.BaseUrl + "AppEmployeeProfile";
    public de.hdodenhof.circleimageview.CircleImageView proImg;
    public String userid = "", authcode = "";

    private OnFragmentInteractionListener mListener;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        String strtext = getArguments().getString("Count");
        Log.e("checking count",strtext + " null");

        mListener.onFragmentInteraction(strtext);

        conn = new ConnectionDetector(getActivity());
        userid =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));


        nameTxt = (TextView)rootView. findViewById(R.id.proname);
        fatherNameTxt = (TextView) rootView.findViewById(R.id.profathername);
        dobTxt = (TextView) rootView.findViewById(R.id.prodob);
        genderTxt = (TextView) rootView.findViewById(R.id.progender);
        prefferdNameTxt = (TextView) rootView.findViewById(R.id.proprefreddname);
        materialStatusTxt = (TextView) rootView.findViewById(R.id.promaterialstatus);
        noOfChildTxt = (TextView) rootView.findViewById(R.id.pronoofchildern);
        nationloityTxt = (TextView) rootView.findViewById(R.id.pronationality);
        emailtxt = (TextView) rootView.findViewById(R.id.proemail);
        alternativeEmailTxt = (TextView) rootView.findViewById(R.id.proalternativeemail);
        panNoTxt = (TextView) rootView.findViewById(R.id.propanno);
        passportNameTxt = (TextView) rootView.findViewById(R.id.propassportname);
        passportNumberTxt = (TextView) rootView.findViewById(R.id.propassportno);
        mobileNumberTxt = (TextView) rootView.findViewById(R.id.promobileno);
        phoneNumberTxt = (TextView) rootView.findViewById(R.id.prophoneno);
        personalEmailTxt = (TextView) rootView.findViewById(R.id.propersonalemail);
        coprativeEmailTxt = (TextView) rootView.findViewById(R.id.procoprateemail);
        compNameTxt = (TextView) rootView.findViewById(R.id.procompanyname);
        empIdTxt = (TextView) rootView.findViewById(R.id.proempid);
        zoneNameTxt = (TextView) rootView.findViewById(R.id.prozonename);
        designationTxt = (TextView) rootView.findViewById(R.id.prodesignation);
        departmentTxt = (TextView) rootView.findViewById(R.id.prodepartment);
        joingDateTxt = (TextView) rootView.findViewById(R.id.projoingdate);
        managerNameTxt = (TextView) rootView.findViewById(R.id.promanagername);
        bloodGroupTxt = (TextView) rootView.findViewById(R.id.probloodgroup);
        allergiesTxt = (TextView) rootView.findViewById(R.id.proallergies);
        seriousIllnessTxt = (TextView) rootView.findViewById(R.id.proseriousillness);
        familyDrNameTxt = (TextView) rootView.findViewById(R.id.profamilydoctorname);
        famiilyDrNoTxt = (TextView) rootView.findViewById(R.id.profamilydoctornumber);
        noOfChildLay = (LinearLayout) rootView.findViewById(R.id.noofchildernlay);
        proImg = (de.hdodenhof.circleimageview.CircleImageView) rootView.findViewById(R.id.pro_image);


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (conn.getConnectivityStatus()>0)
        {
            personalInfoData(authcode,userid);
        }else
        {
            conn.showNoInternetAlret();
        }

        return rootView;
    }

    //Personal Information Data bind
    public void personalInfoData(final String AuthCode , final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalInfoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));


                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object.has("status")) {
                            LoginStatus = object.getString("status");
                            msgstatus = object.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            String Title = object.getString("Title");
                            String EmployeeName = object.getString("EmployeeName");
                            String EmpID = object.getString("EmpID");
                            String Email = object.getString("Email");
                            String FatherName = object.getString("FatherName");
                            String CompName = object.getString("CompName");
                            String Children = object.getString("Children");
                            String ZoneName = object.getString("ZoneName");
                            String JoiningDate = object.getString("JoiningDate");
                            String DepartmentName = object.getString("DepartmentName");
                            String DesignationName = object.getString("DesignationName");
                            String GenderName = object.getString("GenderName");
                            String MartialStatusName = object.getString("MartialStatusName");
                            String ReportTo = object.getString("ReportTo");
                            String AlternateEmail = object.getString("AlternateEmail");
                            String PAN = object.getString("PAN");
                            String PassportName = object.getString("PassportName");
                            String PassportNo = object.getString("PassportNo");
                            String PreferredName = object.getString("PreferredName");
                            String NationalityName = object.getString("NationalityName");
                            String DOB = object.getString("DOB");
                            String CompPhoto = object.getString("CompPhoto");
                            String EmpPhoto = object.getString("EmpPhoto");
                            String BloodGroupName = object.getString("BloodGroupName");
                            String FamilyDoctorName = object.getString("FamilyDoctorName");
                            String FamilyDoctorNo = object.getString("FamilyDoctorNo");
                            String Allergies = object.getString("Allergies");
                            String Illness = object.getString("Illness");
                            String PhoneNo = object.getString("PhoneNo");
                            String MobileNo = object.getString("MobileNo");
                            String EmailPersonal = object.getString("EmailPersonal");
                            String EmailCorporate = object.getString("EmailCorporate");

                            if (Children.equalsIgnoreCase("0"))
                            {
                                noOfChildLay.setVisibility(View.GONE);
                            }else
                            {
                                noOfChildLay.setVisibility(View.VISIBLE);
                            }

                            nameTxt.setText(Title + " " +EmployeeName);
                            empIdTxt.setText(EmpID);
                            emailtxt.setText(Email);
                            fatherNameTxt.setText(FatherName);
                            compNameTxt.setText(CompName);
                            noOfChildTxt.setText(Children);
                            zoneNameTxt.setText(ZoneName);
                            joingDateTxt.setText(JoiningDate);
                            departmentTxt.setText(DepartmentName);
                            designationTxt.setText(DesignationName);
                            genderTxt.setText(GenderName);
                            materialStatusTxt.setText(MartialStatusName);
                            managerNameTxt.setText(ReportTo);
                            alternativeEmailTxt.setText(AlternateEmail);
                            panNoTxt.setText(PAN);
                            passportNameTxt.setText(PassportName);
                            passportNumberTxt.setText(PassportNo);
                            prefferdNameTxt.setText(PreferredName);
                            nationloityTxt.setText(NationalityName);
                            dobTxt.setText(DOB);
                            bloodGroupTxt.setText(BloodGroupName);
                            familyDrNameTxt.setText(FamilyDoctorName);
                            famiilyDrNoTxt.setText(FamilyDoctorNo);
                            allergiesTxt.setText(Allergies);
                            seriousIllnessTxt.setText(Illness);
                            phoneNumberTxt.setText(PhoneNo);
                            mobileNumberTxt.setText(MobileNo);
                            personalEmailTxt.setText(EmailPersonal);
                            coprativeEmailTxt.setText(EmailCorporate);

                            //set profile Image
                            Picasso pic = Picasso.with(getActivity());
                            pic.setIndicatorsEnabled(true);
                            pic.with(getActivity()).cancelRequest(proImg);
                            pic.with(getActivity())
                                    .load(SettingConstant.DownloadUrl + EmpPhoto)
                                    .placeholder(R.drawable.prf)
                                    .error(R.drawable.prf)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .tag(getActivity())
                                    .into(proImg);


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
