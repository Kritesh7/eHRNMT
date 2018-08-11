package in.co.cfcs.ehrnmt.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.co.cfcs.ehrnmt.Main.LoginActivity;
import in.co.cfcs.ehrnmt.Model.NationnalityModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String personalDetailsUrl = SettingConstant.BaseUrl + "AppEmployeePersonalData";
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeePersonalData";
    public String editPersonalDetailsUrl = SettingConstant.BaseUrl + "AppEmpPersonalDataLogInsUpdt";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public  TextView dobTxt,dobImg,joiningdateTxt,joingDateImg;
    public Button editBtn;
    public EditText nameTxt, fathernameTxt, designatiinTxt,childernTxt, emailTxt,  emplId, reporttoTxt,
                    prefrednameTxt, passportNameTxt, alternativeTxt, panNoTxt,passportnumberTxt,firstNameTxt,middleNameTxt;
    public Spinner materialStatusSpinner, natinalitySpinner, zoneSpinner,departmentSpinner,titleSpinner;
    public RadioGroup genderGroup;
    public RadioButton mailBtn, femailBtn;
    public String userId = "", authcode = "",userTypeString = "";

    public ArrayList<String> materialList = new ArrayList<>();
    public ArrayList<NationnalityModel> nationalityList = new ArrayList<>();
    public ArrayList<String> zoneList = new ArrayList<>();
    public ArrayList<String> departmentList = new ArrayList<>();
    public ArrayList<String> titleList = new ArrayList<>();
    public ProgressDialog pDialog;
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public ArrayAdapter<String> materialAdapter,titleAdapter;
    public ArrayAdapter<NationnalityModel> nationaolityAdapter;
    public String GenderName = "",MartialStatusName = "", selectedItemForNationality = "", genderId = "", materialStatusId = "";

    private OnFragmentInteractionListener mListener;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalDetailsFragment newInstance(String param1, String param2) {
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_personal_details, container, false);

        String strtext = getArguments().getString("Count");
        Log.e("checking count",strtext + " null");

        mListener.onFragmentInteraction(strtext);

        nameTxt = (EditText)rootView.findViewById(R.id.name);
        firstNameTxt = (EditText)rootView.findViewById(R.id.firstname);
        middleNameTxt = (EditText)rootView.findViewById(R.id.middlename);
        fathernameTxt = (EditText)rootView.findViewById(R.id.fathername);
        dobTxt = (TextView)rootView.findViewById(R.id.dob);
        dobImg = (TextView)rootView.findViewById(R.id.dob);
        designatiinTxt = (EditText)rootView.findViewById(R.id.designation);
        childernTxt = (EditText)rootView.findViewById(R.id.childern);
        emailTxt = (EditText)rootView.findViewById(R.id.email);
        joiningdateTxt = (TextView)rootView.findViewById(R.id.joiningdate);
        joingDateImg = (TextView)rootView.findViewById(R.id.joiningdate);
        emplId = (EditText)rootView.findViewById(R.id.employeid);
        reporttoTxt = (EditText)rootView.findViewById(R.id.reportto);
        prefrednameTxt = (EditText)rootView.findViewById(R.id.prefredname);
        passportNameTxt = (EditText)rootView.findViewById(R.id.passportname);
        passportnumberTxt = (EditText)rootView.findViewById(R.id.passportnumber);
        alternativeTxt = (EditText)rootView.findViewById(R.id.alternativeemail);
        panNoTxt = (EditText)rootView.findViewById(R.id.panno);
        genderGroup = (RadioGroup)rootView.findViewById(R.id.genderradiogroup);
        mailBtn = (RadioButton)rootView.findViewById(R.id.mailradiobtn);
        femailBtn = (RadioButton)rootView.findViewById(R.id.femailradiobtn);
        materialStatusSpinner = (Spinner)rootView.findViewById(R.id.materialstatus);
        natinalitySpinner = (Spinner)rootView.findViewById(R.id.nationalityspinner);
        departmentSpinner = (Spinner)rootView.findViewById(R.id.departmentspinner);
        zoneSpinner = (Spinner)rootView.findViewById(R.id.zonespinner);
        titleSpinner = (Spinner)rootView.findViewById(R.id.titilespinner);
        editBtn = (Button)rootView.findViewById(R.id.editprofilebtn);

        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(getActivity())));
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(getActivity())));
        userTypeString = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserType(getActivity())));

        pDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);


        //material Spinner
        materialStatusSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        materialAdapter = new ArrayAdapter<String>(getActivity(), R.layout.customizespinner,
                materialList);
        materialAdapter.setDropDownViewResource(R.layout.customizespinner);
        materialStatusSpinner.setAdapter(materialAdapter);

        //Title Spinner
        titleSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        titleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.customizespinner,
                titleList);
        titleAdapter.setDropDownViewResource(R.layout.customizespinner);
        titleSpinner.setAdapter(titleAdapter);


        //nationality Spiiner
        natinalitySpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        nationaolityAdapter = new ArrayAdapter<NationnalityModel>(getActivity(), R.layout.customizespinner,
                nationalityList);
        nationaolityAdapter.setDropDownViewResource(R.layout.customizespinner);
        natinalitySpinner.setAdapter(nationaolityAdapter);


        //zone sPINNER
        if (zoneList.size()>0)
        {
            zoneList.clear();
        }
        zoneList.add("Please Select Zone");
        zoneList.add("IT-Noida");
        zoneList.add("IT-Delhi");


        //change spinner arrow color
        zoneSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> zoneAdapter = new ArrayAdapter<String>(getActivity(), R.layout.customizespinner,
                zoneList);
        zoneAdapter.setDropDownViewResource(R.layout.customizespinner);
        zoneSpinner.setAdapter(zoneAdapter);

        //Department Spinner
        if (departmentList.size()>0)
        {
            departmentList.clear();
        }
        departmentList.add("Please Select Department");
        departmentList.add("Software Development");
        departmentList.add("Software Management");


        //change spinner arrow color
        departmentSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.customizespinner,
                departmentList);
        departmentAdapter.setDropDownViewResource(R.layout.customizespinner);
        departmentSpinner.setAdapter(departmentAdapter);

        //dob calendar
        dobImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                InputMethodManager inputManager = (InputMethodManager)getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                yy = year;
                                mm = monthOfYear;
                                dd = dayOfMonth;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.MONTH, monthOfYear);
                                String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(calendar.getTime());
                                sdf = new DateFormatSymbols().getShortMonths()[monthOfYear];

                                Log.e("checking,............", sdf + " null");
                                dobTxt.setText(dayOfMonth + " " + sdf + " " + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            }
        });

        //nationality spiiner select item
        natinalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedItemForNationality = nationalityList.get(i).getNationlityId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        //disable all widget
        firstNameTxt.setEnabled(false);
        middleNameTxt.setEnabled(false);
        nameTxt.setEnabled(false);
        fathernameTxt.setEnabled(false);
        dobImg.setEnabled(false);
        dobTxt.setEnabled(false);
        designatiinTxt.setEnabled(false);
       // mailBtn.setEnabled(false);
        genderGroup.setEnabled(false);
       // femailBtn.setEnabled(false);
        materialStatusSpinner.setEnabled(false);
        childernTxt.setEnabled(false);
        emailTxt.setEnabled(false);
        natinalitySpinner.setEnabled(false);
        joingDateImg.setEnabled(false);
        joiningdateTxt.setEnabled(false);
        zoneSpinner.setEnabled(false);
        departmentSpinner.setEnabled(false);
        emplId.setEnabled(false);
        reporttoTxt.setEnabled(false);
        prefrednameTxt.setEnabled(false);
        passportNameTxt.setEnabled(false);
        passportnumberTxt.setEnabled(false);
        alternativeTxt.setEnabled(false);
        panNoTxt.setEnabled(false);
        titleSpinner.setEnabled(false);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editBtn.getText().toString().equalsIgnoreCase("Edit Personal Details"))
                {

                    editBtn.setText("Save Personal Detail");

                    //enable all widget
                    titleSpinner.setEnabled(true);
                    firstNameTxt.setEnabled(true);
                    middleNameTxt.setEnabled(true);
                    nameTxt.setEnabled(true);
                    prefrednameTxt.setEnabled(true);
                    dobTxt.setEnabled(true);
                    dobImg.setEnabled(true);
                    alternativeTxt.setEnabled(true);
                    passportNameTxt.setEnabled(true);
                    passportnumberTxt.setEnabled(true);
                    panNoTxt.setEnabled(true);
                    natinalitySpinner.setEnabled(true);

                     mailBtn.setEnabled(false);
                   // genderGroup.setEnabled(false);
                     femailBtn.setEnabled(false);
                }else
                    {

                        editPersonalDetails(userId,dobTxt.getText().toString(),"",genderId,materialStatusId,
                                childernTxt.getText().toString(),selectedItemForNationality,alternativeTxt.getText().toString(),
                                prefrednameTxt.getText().toString(),passportNameTxt.getText().toString(),passportnumberTxt.getText().toString(),
                                panNoTxt.getText().toString(),titleSpinner.getSelectedItem().toString(),firstNameTxt.getText().toString(),
                                middleNameTxt.getText().toString(),nameTxt.getText().toString(),userTypeString,authcode);

                    }

              //  customeDialoge();
            }
        });






        //bind Details Api
        personalDdlDetails();




        return rootView;
    }

    //custome dialoge
    public void customeDialoge()
    {
        final Dialog openDialog = new Dialog(getActivity());
        openDialog.setContentView(R.layout.custome_diloge);
        openDialog.setTitle("Custom Dialog Box");
        TextView dialogTextContent = (TextView)openDialog.findViewById(R.id.dialog_text);
     //   ImageView dialogImage = (ImageView)openDialog.findViewById(R.id.dialog_image);
        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialog_button);
        dialogCloseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }


    public void personalDetails(final String AdminID  , final String AuthCode) {

       /* final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();*/

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["),response.lastIndexOf("]") +1 ));

                    for (int i=0 ; i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // String status = jsonObject.getString("status");
                        if (jsonObject.has("status")) {
                            LoginStatus = jsonObject.getString("status");
                            msgstatus = jsonObject.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {
                                Logout();
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            }
                        }else {

                            String Title = jsonObject.getString("Title");
                            String FirstName = jsonObject.getString("FirstName");
                            String MiddleName = jsonObject.getString("MiddleName");
                            String LastName = jsonObject.getString("LastName");
                            String FatherName = jsonObject.getString("FatherName");
                            String EmpID = jsonObject.getString("EmpID");
                            String Email = jsonObject.getString("Email");
                            GenderName = jsonObject.getString("GenderName");
                            String NationalityName = jsonObject.getString("NationalityName");
                            MartialStatusName = jsonObject.getString("MartialStatusName");
                            String ZoneName = jsonObject.getString("ZoneName");
                            String DepartmentName = jsonObject.getString("DepartmentName");
                            String DesignationName = jsonObject.getString("DesignationName");
                            String JoiningDate = jsonObject.getString("JoiningDate");
                            String ReportTo = jsonObject.getString("ReportTo");
                            String PassportNo = jsonObject.getString("PassportNo");
                            String PAN = jsonObject.getString("PAN");
                            String PassportName = jsonObject.getString("PassportName");
                            String PreferredName = jsonObject.getString("PreferredName");
                            String AlternateEmail = jsonObject.getString("AlternateEmail");
                            String DOB = jsonObject.getString("DOB");
                            String Children = jsonObject.getString("Children");
                            String EditVisibility = jsonObject.getString("EditVisibility");

                            //feed data in widget
                            nameTxt.setText(LastName);
                            firstNameTxt.setText(FirstName);
                            middleNameTxt.setText(MiddleName);
                            fathernameTxt.setText(FatherName);
                            dobTxt.setText(DOB);
                            designatiinTxt.setText(DesignationName);
                            childernTxt.setText(Children);
                            emailTxt.setText(Email);
                            joiningdateTxt.setText(JoiningDate);
                            emplId.setText(EmpID);
                            reporttoTxt.setText(ReportTo);
                            prefrednameTxt.setText(PreferredName);
                            passportNameTxt.setText(PassportName);
                            passportnumberTxt.setText(PassportNo);
                            alternativeTxt.setText(AlternateEmail);
                            panNoTxt.setText(PAN);
                            
                            //edit button visibility check
                            if (EditVisibility.equalsIgnoreCase("1"))
                            {
                                editBtn.setVisibility(View.VISIBLE);
                            }else
                                {
                                    editBtn.setText("Previous request Pending");
                                    editBtn.setBackgroundColor(getActivity().getResources().getColor(R.color.disbale_color));
                                    editBtn.setEnabled(false);
                                   // editBtn.setVisibility(View.GONE);
                                    //Toast.makeText(getActivity(), "Your Previous request waiting for Hr approval", Toast.LENGTH_SHORT).show();
                                }

                            //material selected Spinner
                            for (int j = 0; j<materialList.size(); j++)
                            {
                                if (materialList.get(j).equalsIgnoreCase(MartialStatusName))
                                {
                                    materialStatusSpinner.setSelection(j);

                                    if (MartialStatusName.equalsIgnoreCase("Single"))
                                    {
                                        materialStatusId = "1";
                                    }else
                                        {
                                            materialStatusId = "2";
                                        }
                                }
                            }

                            //nationality selected Spiiner
                            for (int k=0; k<nationalityList.size(); k++)
                            {
                                if (nationalityList.get(k).getNameNationlaity().equalsIgnoreCase(NationalityName))
                                {
                                    natinalitySpinner.setSelection(k);
                                }
                            }

                            //Zone selected Spinner
                            for (int l=0; l<zoneList.size(); l++)
                            {
                                if (zoneList.get(l).equalsIgnoreCase(ZoneName))
                                {
                                    zoneSpinner.setSelection(l);
                                }
                            }

                            //Department Selected Spinner
                            for (int m = 0; m<departmentList.size(); m++)
                            {
                                if (departmentList.get(m).equalsIgnoreCase(DepartmentName))
                                {
                                    departmentSpinner.setSelection(m);
                                }
                            }

                            //title Spinner
                            for (int n =0; n<titleList.size(); n++)
                            {
                                if (titleList.get(n).equalsIgnoreCase(Title))
                                {
                                    titleSpinner.setSelection(n);
                                }
                            }

                            //gender selected
                            if (GenderName.equalsIgnoreCase("Male"))
                            {
                                mailBtn.setChecked(true);
                                genderId = "1";
                            }else
                                {
                                    femailBtn.setChecked(true);
                                    genderId = "2";

                                }
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
                params.put("AdminID", AdminID);
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

    //bind all spiiner data
    public void personalDdlDetails() {


        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalDdlDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    //bind material List
                    if (materialList.size()>0)
                    {
                        materialList.clear();
                    }
                    materialList.add("Please Select Material Status");

                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }else {

                        JSONArray materialObj = jsonObject.getJSONArray("MartialStatus");
                        for (int i =0; i<materialObj.length(); i++)
                        {
                            JSONObject object = materialObj.getJSONObject(i);

                            String MartialStatusName = object.getString("MartialStatusName");
                            materialList.add(MartialStatusName);

                        }

                        //bind Nationality List
                        if (nationalityList.size()>0)
                        {
                            nationalityList.clear();
                        }
                        nationalityList.add(new NationnalityModel("Please Select Nationality",""));
                        JSONArray nationalityObj = jsonObject.getJSONArray("NationalityMaster");
                        for (int j=0; j<nationalityObj.length(); j++)
                        {
                            JSONObject object = nationalityObj.getJSONObject(j);

                            String NationalityName = object.getString("NationalityName");
                            String NationalityID = object.getString("NationalityID");
                            nationalityList.add(new NationnalityModel(NationalityName,NationalityID));
                        }

                        //bind Title Spinner Data
                        if (titleList.size()>0)
                        {
                            titleList.clear();
                        }
                        titleList.add("Please Select Title");
                        JSONArray titleObj = jsonObject.getJSONArray("TitleMaster");
                        for (int k=0; k<titleObj.length(); k++)
                        {
                            JSONObject object = titleObj.getJSONObject(k);

                            String TitleName = object.getString("TitleName");
                            titleList.add(TitleName);
                        }

                        materialAdapter.notifyDataSetChanged();
                        nationaolityAdapter.notifyDataSetChanged();
                        titleAdapter.notifyDataSetChanged();


                        personalDetails(userId,authcode);
                    }



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
                pDialog.dismiss();


            }
        });
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //Edit all details
    public void editPersonalDetails(final String AdminID  , final String DOB, final String MarrigeDate ,
                                    final String Gender , final String MartialStatus, final String Children, final String Nationality,
                                    final String Email, final String PreferredName, final String PassportName, final String PassportNo,
                                    final String PAN, final String Title, final String FirstName, final String MiddleName,
                                    final String LastName, final String UserType, final String AuthCode) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, editPersonalDetailsUrl, new Response.Listener<String>() {
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
                            } else if (LoginStatus.equalsIgnoreCase("success")) {
                                //change button name
                                editBtn.setText("Edit Personal Details");

                                //disable the widget
                                titleSpinner.setEnabled(false);
                                firstNameTxt.setEnabled(false);
                                middleNameTxt.setEnabled(false);
                                nameTxt.setEnabled(false);
                                prefrednameTxt.setEnabled(false);
                                dobTxt.setEnabled(false);
                                dobImg.setEnabled(false);
                                alternativeTxt.setEnabled(false);
                                passportNameTxt.setEnabled(false);
                                passportnumberTxt.setEnabled(false);
                                panNoTxt.setEnabled(false);
                                natinalitySpinner.setEnabled(false);

                                //bind Details Api
                                personalDdlDetails();

                            }else {
                                Toast.makeText(getContext(),msgstatus, Toast.LENGTH_LONG).show();
                            }
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
                params.put("AdminID", AdminID);
                params.put("DOB",DOB);
                params.put("MarrigeDate",MarrigeDate);
                params.put("Gender",Gender);
                params.put("MartialStatus",MartialStatus);
                params.put("Children",Children);
                params.put("Nationality",Nationality);
                params.put("Email",Email);
                params.put("PreferredName",PreferredName);
                params.put("PassportName",PassportName);
                params.put("PassportNo",PassportNo);
                params.put("PAN",PAN);
                params.put("Title",Title);
                params.put("FirstName",FirstName);
                params.put("MiddleName",MiddleName);
                params.put("LastName",LastName);
                params.put("UserType",UserType);
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
