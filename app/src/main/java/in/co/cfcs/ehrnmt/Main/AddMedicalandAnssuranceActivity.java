package in.co.cfcs.ehrnmt.Main;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import in.co.cfcs.ehrnmt.Model.PolicyTypeModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.DownloadTask;
import in.co.cfcs.ehrnmt.Source.FilePath;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddMedicalandAnssuranceActivity extends AppCompatActivity {

    public TextView titleTxt,startDateTxt,startDateBtn,endDateBtn,endDateTxt;
    public Spinner policyTypeSpinner;
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeePersonalData";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeMedicalPolicyInsUpdt";
    public ArrayList<PolicyTypeModel> policyTypeList = new ArrayList<>();
    public ArrayAdapter<PolicyTypeModel> policyTypeAdapter;
    public ConnectionDetector conn;
    public EditText   policyNumberTxt, policyNameTxt, policyDurationTxt, policyByTxt, insuranceCompTxt,
                    amountInsuredTxt;
    public Button addBtn, uploadBtn;
    public StringTokenizer tokens;
    private int yy, mm, dd;
    private static final int FILE_SELECT_CODE = 0;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public String policyTypeIdStr = "", authcode = "", userId = "", actionMode = "", recordidStr = "",policyTypeStr = ""
            ,policyNameStr = "", policyNumberStr = "", policyDurationStr = "", policyByStr = "", insuranceCompStr = ""
            ,amountStr = "", startDateStr = "", endDateStr = "",policyType = "",imageBase64 = "", imageExtenstion = ""
            ,uploadedFileName = "", first = "", fileStr = "";
    String[] permissions = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    private ProgressDialog mDialog;
    public LinearLayout fileSelectTxt;
    public TextView editTxt;
    public ImageView crossBtn, downloadBtn;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicaland_anssurance);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.medicaltollbar);
        setSupportActionBar(toolbar);

        titleTxt = (TextView)toolbar.findViewById(R.id.titletxt);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                onBackPressed();

            }
        });

        titleTxt.setText("Add New Medical&Insurance");

        Intent intent = getIntent();
        if (intent != null)
        {
            actionMode = intent.getStringExtra("Mode");
            recordidStr = intent.getStringExtra("RecordId");
            policyTypeStr = intent.getStringExtra("PolicyType");
            policyNameStr = intent.getStringExtra("PolicyName");
            policyNumberStr = intent.getStringExtra("PolicyNumber");
            policyDurationStr = intent.getStringExtra("PolicyDuration");
            policyByStr = intent.getStringExtra("PolicyBy");
            insuranceCompStr = intent.getStringExtra("InsuranceCompany");
            amountStr = intent.getStringExtra("AmountInsured");
            startDateStr = intent.getStringExtra("StartDate");
            endDateStr = intent.getStringExtra("EndDate");
            fileStr = intent.getStringExtra("File");
        }

        conn = new ConnectionDetector(AddMedicalandAnssuranceActivity.this);
        authcode =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddMedicalandAnssuranceActivity.this)));
        userId =  UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddMedicalandAnssuranceActivity.this)));


        //   policyCompanySpinner = (Spinner)findViewById(R.id.policycompanynamespinner);
        policyTypeSpinner = (Spinner)findViewById(R.id.policytypespinner);
        startDateBtn = (TextView) findViewById(R.id.startdatetxt);
        endDateBtn = (TextView) findViewById(R.id.enddatetxt);
        startDateTxt = (TextView) findViewById(R.id.startdatetxt);
        endDateTxt = (TextView) findViewById(R.id.enddatetxt);
        policyNameTxt = (EditText) findViewById(R.id.policy_name);
        policyNumberTxt = (EditText) findViewById(R.id.policy_number);
        policyDurationTxt = (EditText) findViewById(R.id.policy_duration);
        policyByTxt = (EditText) findViewById(R.id.policy_by);
        insuranceCompTxt = (EditText) findViewById(R.id.policy_insurance);
        amountInsuredTxt = (EditText) findViewById(R.id.policy_amount);
        addBtn = (Button) findViewById(R.id.newrequestbtn);
        uploadBtn = (Button) findViewById(R.id.uploaddocsbtn);
        fileSelectTxt = (LinearLayout) findViewById(R.id.file_selecttxt);
        crossBtn = (ImageView) findViewById(R.id.crossbtn);
        editTxt = (TextView) findViewById(R.id.edittxt);
        downloadBtn = (ImageView) findViewById(R.id.downloadbtn);

        //change button name
        if (actionMode.equalsIgnoreCase("EditMode"))
        {
            addBtn.setText("Update Medical AND Insurance");
            titleTxt.setText("Update Medical&Insurance");

            policyNameTxt.setText(policyNameStr);
            policyNumberTxt.setText(policyNumberStr);
            policyDurationTxt.setText(policyDurationStr);
            policyByTxt.setText(policyByStr);
            insuranceCompTxt.setText(insuranceCompStr);
            amountInsuredTxt.setText(amountStr);
            startDateTxt.setText(startDateStr);
            endDateTxt.setText(endDateStr);

            if (fileStr.equalsIgnoreCase(""))
            {
                uploadBtn.setVisibility(View.VISIBLE);
                fileSelectTxt.setVisibility(View.GONE);
            }else {

                uploadBtn.setVisibility(View.GONE);
                fileSelectTxt.setVisibility(View.VISIBLE);

            }
            //editTxt.setText("Edit Your File");

            imageBase64 = "";
            imageExtenstion = "";

            downloadBtn.setVisibility(View.VISIBLE);
            editTxt.setVisibility(View.GONE);

        }

        //Policy Type List Spinner
        //change spinner arrow color
        policyTypeSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        policyTypeAdapter = new ArrayAdapter<PolicyTypeModel>(AddMedicalandAnssuranceActivity.this, R.layout.customizespinner,
                policyTypeList);
        policyTypeAdapter.setDropDownViewResource(R.layout.customizespinner);
        policyTypeSpinner.setAdapter(policyTypeAdapter);

        //start date Picker
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddMedicalandAnssuranceActivity.this,
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
                                startDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        //end date Picker
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddMedicalandAnssuranceActivity.this,
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
                                endDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // find policy type id
        policyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                policyTypeIdStr = policyTypeList.get(i).getPolicyId();
                policyType = policyTypeList.get(i).getPolicyType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // add new records
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (policyTypeIdStr.equalsIgnoreCase(""))
                {
                    Toast.makeText(AddMedicalandAnssuranceActivity.this, "Please select Policy Type", Toast.LENGTH_SHORT).show();
                }else if (policyNumberTxt.getText().toString().equalsIgnoreCase(""))
                {
                    policyNumberTxt.setError("Please enter policy number");
                }else if (policyNameTxt.getText().toString().equalsIgnoreCase(""))
                {
                    policyNameTxt.setError("Please enter policy name");
                }else if (policyDurationTxt.getText().toString().equalsIgnoreCase(""))
                {
                    policyDurationTxt.setError("please enter policy duration");
                }else if (policyByTxt.getText().toString().equalsIgnoreCase(""))
                {
                    policyByTxt.setError("Please enter policy by");
                }else if (insuranceCompTxt.getText().toString().equalsIgnoreCase(""))
                {
                    insuranceCompTxt.setError("Please enter insurance company ");
                }else if (amountInsuredTxt.getText().toString().equalsIgnoreCase(""))
                {
                    amountInsuredTxt.setError("Please enter amount insured");
                }else {

                    if (conn.getConnectivityStatus()>0) {

                        if (!imageBase64.equalsIgnoreCase("")) {

                            addMedicalAnssuranceData(userId, recordidStr, policyTypeIdStr, policyNameTxt.getText().toString(), authcode,
                                    policyNumberTxt.getText().toString(), startDateTxt.getText().toString(), endDateTxt.getText().toString(),
                                    policyDurationTxt.getText().toString(), policyByTxt.getText().toString(), insuranceCompTxt.getText().toString(),
                                    amountInsuredTxt.getText().toString(), imageBase64, imageExtenstion);
                        }else
                            {
                                Toast.makeText(AddMedicalandAnssuranceActivity.this, "This file is not supported", Toast.LENGTH_SHORT).show();
                            }
                    }else
                        {
                            conn.showNoInternetAlret();
                        }
                }

            }
        });
        // bind spinner data
        if (conn.getConnectivityStatus()>0)
        {
            personalDdlDetails();
        }else
            {
                conn.showNoInternetAlret();
            }

        //Upload Documents
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (checkPermissions()){
                    showFileChooser();

                }


            }
        });

        //cross uploadede docs
        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadBtn.setVisibility(View.VISIBLE);
                fileSelectTxt.setVisibility(View.GONE);

                imageBase64 = "";
                imageExtenstion = "";
            }
        });


        //download view
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissions()) {


                    new DownloadTask(AddMedicalandAnssuranceActivity.this, SettingConstant.DownloadUrl + fileStr,"MedialAnssurance");
                }
            }
        });
    }

    public void DownloadFromUrl() {
        try {

            URL url = new URL(SettingConstant.DownloadUrl + fileStr);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String Path = Environment.getExternalStorageDirectory() + "/download/";
            Log.v("PortfolioManger", "PATH: " + Path);
            File file = new File(Path);
            file.mkdirs();
            FileOutputStream fos = new FileOutputStream("site.html");

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[702];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            Log.d("PortfolioManger", "Error: " + e);
        }
        Log.v("PortfolioManger", "Check: ");
    }




    //choose attcahment
    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

       /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file*//*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddMedicalandAnssuranceActivity.this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {


                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Bitmap bitmap = null;
                    Log.d("Checking One", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;

                    path = FilePath.getPath(AddMedicalandAnssuranceActivity.this, uri);

                    if (path == null)
                        path = FilePath.getPath(AddMedicalandAnssuranceActivity.this, uri);// From File Manager

                    if (path != null)
                        bitmap = BitmapFactory.decodeFile(path);


                    Log.d("Checking", "File Path: " + path);
                    File file = new File(FilePath.getPath(AddMedicalandAnssuranceActivity.this, uri));
                    Log.d("", "File : " + file.getName());
                    uploadedFileName = file.getName().toString();

                    imageExtenstion = path.substring(path.lastIndexOf("."));
                    Log.e("File Name", imageExtenstion);
                    // first = tokens.nextToken();

                    mDialog = new ProgressDialog(AddMedicalandAnssuranceActivity.this);
                    mDialog.setMessage("Uploading " + file.getName());
                    mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    // mDialog.show();

                    new MyTask(mDialog);


                    if (imageExtenstion.equalsIgnoreCase(".jpg")) {

                        imageBase64 = getEncoded64ImageStringFromBitmap(bitmap);
                        Log.e("checking the frount 64", getEncoded64ImageStringFromBitmap(bitmap) + "Null");
                    } else {
                        imageBase64 = convertFileToByteArray(file);
                        Log.e("checking the frount 64", convertFileToByteArray(file) + "Null");
                    }

                    //File select Successfully Text Visibile
                    fileSelectTxt.setVisibility(View.VISIBLE);
                    uploadBtn.setVisibility(View.GONE);
                    //  }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public class MyTask extends AsyncTask<String, Integer, String> {

        private ProgressDialog progressBar;

        public MyTask( ProgressDialog progressBar ) {

            this.progressBar= progressBar;
        }

        @Override
        protected String doInBackground(String... params ) {
            progressBar.show();
            //do your work
            return "OK";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.dismiss( );
        }

        /* @Override
        protected void onPostExecute( ArrayList<Comment> result ) {
            progressBar.setVisibility( View.GONE );
        }*/
    };
    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }


    //for files
    public String convertFileToByteArray(File f) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 11];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();

            Log.e("Byte array", ">" + byteArray);

            // mDialog.dismiss();

        } catch (IOException e) {
            e.printStackTrace();
        }

        mDialog.dismiss();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    //convert bitmap to base64
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        mDialog.dismiss();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    //check storage permission
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showFileChooser();
            }
            return;
        }
    }

    //bind all spiiner data
    public void personalDdlDetails() {


        final ProgressDialog pDialog = new ProgressDialog(AddMedicalandAnssuranceActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
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
                    if (policyTypeList.size()>0)
                    {
                        policyTypeList.clear();
                    }
                    policyTypeList.add(new PolicyTypeModel("Please Select Policy Type",""));

                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }else {

                        JSONArray policyObj = jsonObject.getJSONArray("PolicyTypeMaster");
                        for (int i =0; i<policyObj.length(); i++)
                        {
                            JSONObject object = policyObj.getJSONObject(i);

                            String PolicyTypeID = object.getString("PolicyTypeID");
                            String PolicyTypeName = object.getString("PolicyTypeName");

                            policyTypeList.add(new PolicyTypeModel(PolicyTypeName,PolicyTypeID));

                        }


                        for (int k =0; k<policyTypeList.size(); k++)
                        {
                            if (actionMode.equalsIgnoreCase("EditMode"))
                            {
                                if (policyTypeList.get(k).getPolicyType().equalsIgnoreCase(policyTypeStr))
                                {
                                    policyTypeSpinner.setSelection(k);
                                }
                            }
                        }


                    }


                    policyTypeAdapter.notifyDataSetChanged();
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
                    Toast.makeText(getBaseContext(),
                            "Time Out Server Not Respond",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getBaseContext(),
                            "Server Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getBaseContext(),
                            "Network Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(getBaseContext(),
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

    //add new medical anssurance
    public void addMedicalAnssuranceData(final String AdminID  , final String RecordID, final String PolicyID, final String Name,
                                         final String AuthCode, final String Number, final String StartDate, final String EndDate,
                                         final String Duration, final String PolicyBy, final String InsuranceCompany,
                                         final String AmountInsured, final String ImgJson, final String ImageExtension  )  {

        final ProgressDialog pDialog = new ProgressDialog(AddMedicalandAnssuranceActivity.this,R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, addUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}") +1 ));

                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else  if (LoginStatus.equalsIgnoreCase("success")){
                            onBackPressed();

                        }else {
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getBaseContext(),
                            "Time Out Server Not Respond",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO

                    Toast.makeText(getBaseContext(),
                            "Server Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //TODO
                    Toast.makeText(getBaseContext(),
                            "Network Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    Toast.makeText(getBaseContext(),
                            "Parse Error",
                            Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();


            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AdminID",AdminID);
                params.put("AuthCode",AuthCode);
                params.put("RecordID",RecordID);
                params.put("PolicyID",PolicyID);
                params.put("Name",Name);
                params.put("Number",Number);
                params.put("StartDate",StartDate);
                params.put("EndDate",EndDate);
                params.put("Duration",Duration);
                params.put("PolicyBy",PolicyBy);
                params.put("InsuranceCompany",InsuranceCompany);
                params.put("AmountInsured",AmountInsured);
                params.put("FileExtension",ImageExtension);
                params.put("FileJson",ImgJson);



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
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }
    private void Logout() {


        finishAffinity();
        startActivity(new Intent(AddMedicalandAnssuranceActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddMedicalandAnssuranceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddMedicalandAnssuranceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddMedicalandAnssuranceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddMedicalandAnssuranceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddMedicalandAnssuranceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddMedicalandAnssuranceActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddMedicalandAnssuranceActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddMedicalandAnssuranceActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddMedicalandAnssuranceActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }


}
