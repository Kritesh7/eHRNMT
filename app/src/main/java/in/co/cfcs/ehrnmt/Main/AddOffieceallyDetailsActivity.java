package in.co.cfcs.ehrnmt.Main;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Base64OutputStream;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import in.co.cfcs.ehrnmt.Model.DocumentTypeModel;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.FilePath;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;

public class AddOffieceallyDetailsActivity extends AppCompatActivity {

    public TextView titleTxt, issueDateTxt, issueDateBtn, expiryDateBtn, expiryDateTxt;
    public Spinner documentTypeSpinner;
    public ArrayList<DocumentTypeModel> documentTypeList = new ArrayList<>();
    public Button uploadBtn;
    public StringTokenizer tokens;
    public String uploadedFileName = "";
    public File file1;
    private ProgressDialog mDialog;
    String[] permissions = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };
    public EditText noTxt, issuesOfPlaceTxt;
    private int yy, mm, dd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    public String personalDdlDetailsUrl = SettingConstant.BaseUrl + "AppddlEmployeePersonalData";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeOfficeDocumentInsUpdt";
    public ArrayAdapter<DocumentTypeModel> documentAdapter;
    public ConnectionDetector conn;
    public Button addBtn;
    public String userId = "", authcode = "", documentId = "", imageBase64 = "", imageExtenstion = "", userNameStr = "", compId = "", documentTxt = "";
    public LinearLayout fileSelectTxt;
    public ImageView crossBtn;
    private static final int FILE_SELECT_CODE = 0;

    String LoginStatus;
    String invalid = "loginfailed";
    String msgstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offieceally_details);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.offecalytollbar);
        setSupportActionBar(toolbar);

        titleTxt = (TextView) toolbar.findViewById(R.id.titletxt);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getSupportActionBar() != null) {
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

        titleTxt.setText("Add Officially Detail");

        conn = new ConnectionDetector(AddOffieceallyDetailsActivity.this);
        authcode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AddOffieceallyDetailsActivity.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AddOffieceallyDetailsActivity.this)));
        userNameStr = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserName(AddOffieceallyDetailsActivity.this)));
        compId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getCompanyId(AddOffieceallyDetailsActivity.this)));

        documentTypeSpinner = (Spinner) findViewById(R.id.documenttypespinner);
        uploadBtn = (Button) findViewById(R.id.uploaddocsbtn);
        issueDateBtn = (TextView) findViewById(R.id.issuedatetxt);
        expiryDateBtn = (TextView) findViewById(R.id.expirydatetxt);
        issueDateTxt = (TextView) findViewById(R.id.issuedatetxt);
        expiryDateTxt = (TextView) findViewById(R.id.expirydatetxt);
        noTxt = (EditText) findViewById(R.id.number);
        issuesOfPlaceTxt = (EditText) findViewById(R.id.issueplace);
        addBtn = (Button) findViewById(R.id.newrequestbtn);
        fileSelectTxt = (LinearLayout) findViewById(R.id.file_selecttxt);
        crossBtn = (ImageView) findViewById(R.id.crossbtn);

        //DOcument Type List Spinner
        //change spinner arrow color
        documentTypeSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        documentAdapter = new ArrayAdapter<DocumentTypeModel>(AddOffieceallyDetailsActivity.this, R.layout.customizespinner,
                documentTypeList);
        documentAdapter.setDropDownViewResource(R.layout.customizespinner);
        documentTypeSpinner.setAdapter(documentAdapter);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (checkPermissions()) {
                    showFileChooser();

                }


            }
        });

        // issue date Picker
        issueDateBtn.setOnClickListener(new View.OnClickListener() {
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddOffieceallyDetailsActivity.this,
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
                                issueDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        expiryDateBtn.setOnClickListener(new View.OnClickListener() {
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddOffieceallyDetailsActivity.this,
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
                                expiryDateTxt.setText(dayOfMonth + "-" + sdf + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //get spiiner data
        if (conn.getConnectivityStatus() > 0) {
            personalDdlDetails();
        } else {
            conn.showNoInternetAlret();
        }


        //get document id
        documentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                documentId = documentTypeList.get(i).getDocTypeId();
                documentTxt = documentTypeList.get(i).getDocTypeName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //add officealy docs
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (documentId.equalsIgnoreCase("")) {
                    Toast.makeText(AddOffieceallyDetailsActivity.this, "Please select Document type", Toast.LENGTH_SHORT).show();
                } else if (imageExtenstion.equalsIgnoreCase("")) {
                    Toast.makeText(AddOffieceallyDetailsActivity.this, "Please choose Documents", Toast.LENGTH_SHORT).show();
                } else {
                    if (conn.getConnectivityStatus() > 0) {

                        if (!imageBase64.equalsIgnoreCase("")) {
                            addOfficealyDocs(userId, "0", documentId, noTxt.getText().toString(), authcode, issuesOfPlaceTxt.getText().toString(),
                                    expiryDateTxt.getText().toString(), issueDateTxt.getText().toString(), imageBase64, imageExtenstion,
                                    compId, userNameStr, documentTxt);
                        } else {
                            Toast.makeText(AddOffieceallyDetailsActivity.this, "This file is not supported", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        conn.showNoInternetAlret();
                    }

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
    }

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

        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("*/*");
        // startActivityForResult(intent, 7);

       /* try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddOffieceallyDetailsActivity.this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }*/
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            if (resultCode == Activity.RESULT_OK) {



                if (data.getData() != null) {

                    //Log.e("Checking Null", selectedFileURI + "");
                    Bitmap bitmap = null;
                    try {

                        Uri selectedFileURI = data.getData();
                        String path = getRealPathFromURI(selectedFileURI);

                        Log.e("checking path name", path + " null");

                        if (path == null)
                            path = selectedFileURI.getPath(); // From File Manager

                        if (path != null)
                            bitmap  = BitmapFactory.decodeFile(path);

                        //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFileURI);

                        File file = new File(selectedFileURI.getPath());
                        Log.d("", "File : " + file.getName());
                        uploadedFileName = file.getName().toString();

                        imageExtenstion = path.substring(selectedFileURI.getPath().lastIndexOf("."));
                        Log.e("File Name", imageExtenstion);
                       // first = tokens.nextToken();

                        mDialog = new ProgressDialog(AddOffieceallyDetailsActivity.this);
                        mDialog.setMessage("Uploading " + file.getName());
                        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                       // mDialog.show();

                        new MyTask(mDialog);


                        if (imageExtenstion.equalsIgnoreCase(".jpg")) {

                           imageBase64 = getEncoded64ImageStringFromBitmap(bitmap);
                            Log.e("checking the frount 64", getEncoded64ImageStringFromBitmap(bitmap) + "Null");
                        }else
                            {
                                imageBase64 = convertFileToByteArray(file);
                                Log.e("checking the frount 64", convertFileToByteArray(file) + "Null");
                            }

                        //File select Successfully Text Visibile
                        fileSelectTxt.setVisibility(View.VISIBLE);
                        uploadBtn.setVisibility(View.GONE);

                    } catch (StringIndexOutOfBoundsException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Error", e.getMessage());
                    }


                } else {
                    Log.e("Checking Null", "Null");
                }


                // input Stream


            }
        }*/

        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    //File directory = null,photoDirectory = null;


                    //if there is no SD card, create new directory objects to make directory on device
                   /* if (Environment.getExternalStorageState() == null) {
                        //create new file directory object


                        directory = new File(Environment.getDataDirectory()
                                + "/RobotiumTestLog/");
                        photoDirectory = new File(Environment.getDataDirectory()
                                + "/Robotium-Screenshots/");
            *//*
                     * this checks to see if there are any previous test photo files
                     * if there are any photos, they are deleted for the sake of
                     * memory
                     *//*
                        if (photoDirectory.exists()) {
                            File[] dirFiles = photoDirectory.listFiles();
                            if (dirFiles.length != 0) {
                                for (int ii = 0; ii <= dirFiles.length; ii++) {
                                    dirFiles[ii].delete();
                                }
                            }
                        }
                        // if no directory exists, create new directory
                        if (!directory.exists()) {
                            directory.mkdir();
                        }

                        // if phone DOES have sd card
                    } else if (Environment.getExternalStorageState() != null) {
                        // search for directory on SD card
                        directory = new File(Environment.getExternalStorageDirectory()
                                + "/RobotiumTestLog/");
                        photoDirectory = new File(
                                Environment.getExternalStorageDirectory()
                                        + "/Robotium-Screenshots/");


                        Bitmap bitmap = null;
                       // Log.d("Checking One", "File Uri: " + uri.toString());
                        // Get the path
                        String path = null;

                        path = directory.getPath();
                        Log.e("checking the Path", path+ "Null");

                        if (path != null)
                            bitmap = BitmapFactory.decodeFile(path);



                        mDialog = new ProgressDialog(AddOffieceallyDetailsActivity.this);
                        mDialog.setMessage("Uploading " + directory.getName());
                        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        // mDialog.show();

                        new MyTask(mDialog);


                        if (imageExtenstion.equalsIgnoreCase(".jpg")) {

                            imageBase64 = getEncoded64ImageStringFromBitmap(bitmap);
                            Log.e("checking the frount 64", getEncoded64ImageStringFromBitmap(bitmap) + "Null");
                        } else {
                            imageBase64 = convertFileToByteArray(directory);
                            Log.e("checking the frount 64", convertFileToByteArray(directory) + "Null");
                        }

                        //File select Successfully Text Visibile
                        fileSelectTxt.setVisibility(View.VISIBLE);
                        uploadBtn.setVisibility(View.GONE);
                       *//* if (photoDirectory.exists()) {
                            File[] dirFiles = photoDirectory.listFiles();
                            if (dirFiles.length > 0) {
                                for (int ii = 0; ii < dirFiles.length; ii++) {
                                    dirFiles[ii].delete();
                                }
                                dirFiles = null;
                            }
                        }*//*
                        // if no directory exists, create new directory to store test
                        // results
                        if (!directory.exists()) {
                            directory.mkdir();
                        }
                    } else {*/

                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Bitmap bitmap = null;
                    Log.d("Checking One", "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;

                    path = FilePath.getPath(AddOffieceallyDetailsActivity.this, uri);

                    if (path == null)
                        path = FilePath.getPath(AddOffieceallyDetailsActivity.this, uri);// From File Manager

                    if (path != null)
                        bitmap = BitmapFactory.decodeFile(path);


                    Log.d("Checking", "File Path: " + path);
                    File file = new File(FilePath.getPath(AddOffieceallyDetailsActivity.this, uri));
                    Log.d("", "File : " + file.getName());
                    uploadedFileName = file.getName().toString();

                    imageExtenstion = path.substring(path.lastIndexOf("."));
                    Log.e("File Name", imageExtenstion);
                    // first = tokens.nextToken();

                    mDialog = new ProgressDialog(AddOffieceallyDetailsActivity.this);
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


    public static String getPath(final Context context, final Uri uri) {
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }

            //DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    /* public static String getPath(Context context, Uri uri) throws URISyntaxException {
         if ("content".equalsIgnoreCase(uri.getScheme())) {
             String[] projection = { "_data" };
             Cursor cursor = null;

             try {
                 cursor = context.getContentResolver().query(uri, projection, null, null, null);
                 int column_index = cursor.getColumnIndexOrThrow("_data");
                 if (cursor.moveToFirst()) {
                     return cursor.getString(column_index);
                 }
             } catch (Exception e) {
                 // Eat it
             }
         }
         else if ("file".equalsIgnoreCase(uri.getScheme())) {
             return uri.getPath();
         }

         return null;
     }
 */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        if (cursor == null)
            return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    public class MyTask extends AsyncTask<String, Integer, String> {

        private ProgressDialog progressBar;

        public MyTask(ProgressDialog progressBar) {

            this.progressBar = progressBar;
        }

        @Override
        protected String doInBackground(String... params) {
            progressBar.show();
            //do your work
            return "OK";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.dismiss();
        }

        /* @Override
        protected void onPostExecute( ArrayList<Comment> result ) {
            progressBar.setVisibility( View.GONE );
        }*/
    }

    ;

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

    public String getBase64FromFile(String path) {
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        byte[] baat = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            baat = baos.toByteArray();
            encodeString = Base64.encodeToString(baat, Base64.DEFAULT);

            //  pDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodeString;
    }

    public String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

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


        final ProgressDialog pDialog = new ProgressDialog(AddOffieceallyDetailsActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, personalDdlDetailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONObject jsonObject = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));

                    //bind material List
                    if (documentTypeList.size() > 0) {
                        documentTypeList.clear();
                    }
                    documentTypeList.add(new DocumentTypeModel("Please Select Policy Type", ""));
                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {
                            Logout();
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        JSONArray documentTypeObj = jsonObject.getJSONArray("DocumentTypeMaster");
                        for (int i = 0; i < documentTypeObj.length(); i++) {
                            JSONObject object = documentTypeObj.getJSONObject(i);

                            String DocumentTypeID = object.getString("DocumentTypeID");
                            String DocumentTypeName = object.getString("DocumentTypeName");

                            documentTypeList.add(new DocumentTypeModel(DocumentTypeName, DocumentTypeID));

                        }

                    }



                   /* for (int k =0; k<policyTypeList.size(); k++)
                    {
                        if (actionMode.equalsIgnoreCase("EditMode"))
                        {
                            if (policyTypeList.get(k).getPolicyType().equalsIgnoreCase(policyTypeStr))
                            {
                                policyTypeSpinner.setSelection(k);
                            }
                        }
                    }*/

                    documentAdapter.notifyDataSetChanged();
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

    // add the officeally document
    public void addOfficealyDocs(final String AdminID, final String RecordID, final String DocumentID, final String Number,
                                 final String AuthCode, final String IssuePlace, final String ExpiryDate, final String IssueDate,
                                 final String FileJson, final String FileExtension, final String CompID, final String UserName,
                                 final String Document) {

        final ProgressDialog pDialog = new ProgressDialog(AddOffieceallyDetailsActivity.this, R.style.AppCompatAlertDialogStyle);
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
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        } else if (LoginStatus.equalsIgnoreCase("success")) {
                            onBackPressed();
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }


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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AdminID", AdminID);
                params.put("AuthCode", AuthCode);
                params.put("RecordID", RecordID);
                params.put("DocumentID", DocumentID);
                params.put("Number", Number);
                params.put("IssuePlace", IssuePlace);
                params.put("ExpiryDate", ExpiryDate);
                params.put("IssueDate", IssueDate);
                params.put("FileJson", FileJson);
                params.put("FileExtension", FileExtension);
                params.put("CompID", CompID);
                params.put("UserName", UserName);
                params.put("Document", Document);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    private void Logout() {


        finishAffinity();
        startActivity(new Intent(AddOffieceallyDetailsActivity.this, LoginActivity.class));

//        Intent ik = new Intent(ManagerRequestToApproveActivity.this, LoginActivity.class);
//        startActivity(ik);


        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AddOffieceallyDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AddOffieceallyDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AddOffieceallyDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AddOffieceallyDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AddOffieceallyDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AddOffieceallyDetailsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AddOffieceallyDetailsActivity.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AddOffieceallyDetailsActivity.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AddOffieceallyDetailsActivity.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }


}
