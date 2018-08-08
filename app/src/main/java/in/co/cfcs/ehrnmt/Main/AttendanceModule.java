package in.co.cfcs.ehrnmt.Main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.co.cfcs.ehrnmt.Interface.GeofenceTrasitionService;
import in.co.cfcs.ehrnmt.Interface.LocationUpdateService;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.CameraView;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.GPSTracker;
import in.co.cfcs.ehrnmt.Source.GetLoctionAddress;
import in.co.cfcs.ehrnmt.Source.MockLocationDetector;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;



public class AttendanceModule extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status> {


    public TextView titleTxt;
    public SupportMapFragment mSupportMapFragment;
    public MarkerOptions options;
    public ArrayList<LatLng> MarkerPoints;
    public GPSTracker gpsTracker;
    public Context mContext;
    public ImageView locationImg;
    public EditText locationTxt;
    String locationAddress = "";
    public Button subBtn;
    private GoogleMap mMap;
    public ImageView profileImg;
    public Button cancelBtn;
    public ImageView profileSelectImg;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    public TextView timeTxt, dateTxt, addTxt;
    public ConnectionDetector conn;
    public String imageBase64 = "";
    public String addUrl = SettingConstant.BaseUrl + "AppEmployeeAttendanceLogInsUpdt";
    public String addGeoFencingUrl = SettingConstant.BaseGeoUrl + "GEOfencingDetail";
    public String authCode = "", userId = "";
    public EditText remarkTxt;
    public ProgressDialog pDialog;

    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    public LocationUpdateService myService;

    private MapFragment mapFragment;

//    double lati = 28.49606;
//    double longit = 77.40185;

    LatLng GeolatLng;

    int CheckGeoFence;

    FrameLayout camera_view;

    LatLngBounds.Builder builder;

    String msgstatus = "";

    RadioButton rd_in, rd_out;

    String EmployeeName;
    String addressName1;

    String InOutStatus ="";


    String LoginStatus;
    String invalid = "loginfailed";

    private static final String TAG = AttendanceModule.class.getSimpleName();
    String MSg;
    public static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent(context, AttendanceModule.class);
        intent.putExtra(NOTIFICATION_MSG, msg);
        return intent;
    }

    public static String id1 = "ehrms_channel_01";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_module);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.attendancetollbar);
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

        gpsTracker = new GPSTracker(AttendanceModule.this, AttendanceModule.this);
        conn = new ConnectionDetector(AttendanceModule.this);
        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(AttendanceModule.this)));
        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(AttendanceModule.this)));
        InOutStatus = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getInOutStatus(AttendanceModule.this)));


        titleTxt.setText("Mark Attendance");
        profileImg = (ImageView) findViewById(R.id.pro_image);
        subBtn = (Button) findViewById(R.id.submitbtn);
        timeTxt = (TextView) findViewById(R.id.time);
        dateTxt = (TextView) findViewById(R.id.date);
        addTxt = (TextView) findViewById(R.id.addreestxt);
        remarkTxt = (EditText) findViewById(R.id.remarktxt);
        cancelBtn = (Button) findViewById(R.id.cancelbtn);
        rd_in = findViewById(R.id.rd_in);
        rd_out = findViewById(R.id.rd_out);

        createchannel();  //needed for the persistent notification created in service.

        if (conn.getConnectivityStatus() > 0) {

            GeoFencingDetail(userId, authCode);
        } else {

            conn.showNoInternetAlret();
        }

        if(InOutStatus.compareTo("1") == 0){
            rd_in.setVisibility(View.GONE);
            rd_out.setChecked(true);

        }else {
            rd_in.setVisibility(View.VISIBLE);
            rd_out.setVisibility(View.GONE);
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiverDisable,
                new IntentFilter("custom-disable-button"));


        // subBtn.setVisibility(View.GONE);

        subBtn.setEnabled(false);
        subBtn.setText("Reach Your Location");


        // initialize GoogleMaps
        initGMaps();

        // create GoogleApiClient
        createGoogleApi();

        //current Time incresing
        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();

                timeTxt.setText(String.format("%02d:%02d:%02d", c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
            }

            public void onFinish() {

            }
        };
        newtimer.start();

        //set current date
        dateTxt.setText(getCurrentTime());

        if (gpsTracker.canGetLocation()) {

            GetLoctionAddress locationAddress = new GetLoctionAddress();
            locationAddress.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), getApplicationContext(),
                    new GeocoderHandler());

//            LocationAddress locationAddress = new LocationAddress();
//            locationAddress.getAddressFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), AttendanceModule.this,
//                    new GeocoderHandler());


        } else {
            gpsTracker.showSettingsAlert();
        }


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        try {

            if (checkCameraRear()) {

                if (checkCameraFront(AttendanceModule.this)) {
                    mCamera = Camera.open(1);
                } else {
                    mCamera = Camera.open();
                }
            } else {
                Toast.makeText(this, "Camera is Not support your device", Toast.LENGTH_SHORT).show();
            }

            //you can use open(int) to use different cameras

        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (mCamera != null) {
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }

        //btn to close the application
        ImageButton imgClose = (ImageButton) findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });


        //click on button and take pic


        final Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                Bitmap bm = null;

                if (data != null) {
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    int screenHeight = getResources().getDisplayMetrics().heightPixels;
                    bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        // Notice that width and height are reversed
                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
                        int w = scaled.getWidth();
                        int h = scaled.getHeight();
                        // Setting post rotate to 90
                        Matrix mtx = new Matrix();

                        int CameraEyeValue = setPhotoOrientation(AttendanceModule.this, checkCameraFront(AttendanceModule.this) == true ? 1 : 0); // CameraID = 1 : front 0:back
                        if (checkCameraFront(AttendanceModule.this)) { // As Front camera is Mirrored so Fliping the Orientation
                            if (CameraEyeValue == 270) {
                                mtx.postRotate(90);
                            } else if (CameraEyeValue == 90) {
                                mtx.postRotate(270);
                            }
                        } else {
                            mtx.postRotate(CameraEyeValue); // CameraEyeValue is default to Display Rotation
                        }

                        bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                    } else {// LANDSCAPE MODE
                        //No need to reverse width and height
                        Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                        bm = scaled;
                    }
                }


                imageBase64 = getEncoded64ImageStringFromBitmap(bm);

                // profileImg.setImageBitmap(bm);

                camera.startPreview();
            }
        };

        builder = new LatLngBounds.Builder();

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCamera.takePicture(null, null, jpegCallback);

                pDialog = new ProgressDialog(AttendanceModule.this, R.style.AppCompatAlertDialogStyle);
                pDialog.setTitle("Mark Attendance");
                pDialog.setMessage("Please Wait...");
                pDialog.show();

                boolean isMock = MockLocationDetector.isLocationFromMockProvider(AttendanceModule.this, lastLocation);

                boolean mockLocationAppsPresent = MockLocationDetector.checkForAllowMockLocationsApps(AttendanceModule.this);

                boolean isAllowMockLocationsON = MockLocationDetector.isAllowMockLocationsOn(AttendanceModule.this);

                new Handler().postDelayed(new Runnable() {


                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        if (imageBase64.equalsIgnoreCase("")) {
                            Toast.makeText(AttendanceModule.this, "Please Click the pic properely", Toast.LENGTH_SHORT).show();
                        } else if (addTxt.getText().toString().equalsIgnoreCase("")) {
                            addTxt.setError("Please get address");
                            pDialog.dismiss();
                        } else if (isMock) {
                            Toast.makeText(AttendanceModule.this, "Mock Location On", Toast.LENGTH_LONG).show();
                            pDialog.dismiss();
                        } else if (gpsTracker.getLongitude() == 0 || gpsTracker.getLongitude() == 0) {
                            Toast.makeText(AttendanceModule.this, "Please get location", Toast.LENGTH_SHORT).show();
                        } else {

                            if (rd_in.isChecked()) {

                                if (conn.getConnectivityStatus() > 0) {

                                    TrackService();

                                    attendaceDetails(userId, gpsTracker.getLongitude() + "", gpsTracker.getLatitude() + "", addTxt.getText().toString(),
                                            remarkTxt.getText().toString(), imageBase64, authCode, ".jpeg");
                                } else {

                                    conn.showNoInternetAlret();
                                }

                            } else if (rd_out.isChecked()) {

                                if (conn.getConnectivityStatus() > 0) {

                                    StopTrackService();

                                    attendaceDetails(userId, gpsTracker.getLongitude() + "", gpsTracker.getLatitude() + "", addTxt.getText().toString(),
                                            remarkTxt.getText().toString(), imageBase64, authCode, ".jpeg");
                                } else {

                                    conn.showNoInternetAlret();
                                }
                            }


                        }
                    }
                }, 3000);
            }
        });
    }


    /*
     * for API 26+ create notification channels
     */
    private void createchannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel mChannel = new NotificationChannel(id1,
                    getString(R.string.channel_name),  //name of the channel
                    NotificationManager.IMPORTANCE_HIGH);   //importance level
            //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
            // Configure the notification channel.
            mChannel.setDescription(getString(R.string.channel_description));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setShowBadge(true);
            nm.createNotificationChannel(mChannel);
        }
    }


    public void StopTrackService() {

        getBaseContext().stopService(new Intent(this, LocationUpdateService.class));

    }

    @SuppressLint("NewApi")
    private void TrackService() {

        double innerlat = gpsTracker.getLatitude();
        double inerlog = gpsTracker.getLongitude();
        subBtn.setVisibility(View.GONE);

//        Intent serviceIntent = new Intent(getBaseContext(), LocationUpdateService.class);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            getBaseContext().startForegroundService(serviceIntent);
//        } else {
//            getBaseContext().startService(serviceIntent);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {

                Intent serviceIntent = new Intent(getBaseContext(), LocationUpdateService.class);
                serviceIntent.putExtra("times", 5);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                } else {
                    //lower then Oreo, just start the service.
                    startService(serviceIntent);
                }
                //  finish();
            }
        }

    }

    private void GeoFencingDetail(String userId, String authCode) {

        String UnableGeo = "NotConfigured";

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, addGeoFencingUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    // msgstatus = jsonObject.getString("MsgNotification");
                    if (jsonObject.has("status")) {

                        msgstatus = jsonObject.getString("MsgNotification");
                        if (msgstatus.equals(UnableGeo)) {

                            CheckGeoFence = 0;

                            //  subBtn.setVisibility(View.VISIBLE);
                            subBtn.setEnabled(true);
                            subBtn.setText("Submit");
                        }
                    } else {
                        CheckGeoFence = 1;
                        String EmployeeName = null;
                        String AddressName = null;
                        String Latt = null;
                        String Lang = null;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                            if (jsonObject.has("MsgNotification")) {
                                String MsgNotification = jsonObject.getString("MsgNotification");
                                Toast.makeText(AttendanceModule.this,MsgNotification, Toast.LENGTH_LONG).show();
                                Logout();
                            }else{

                                EmployeeName = jsonObject1.getString("EmployeeName").toString();
                                AddressName = jsonObject1.getString("AddressName").toString();
                                Latt = jsonObject1.getString("Latt").toString();
                                Lang = jsonObject1.getString("Lang").toString();
                            }



                        }

                        geofencingcheckdata(EmployeeName, AddressName, Latt, Lang);

                    }


                    // pDialog.dismiss();

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

                Toast.makeText(AttendanceModule.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("AuthCode", authCode);
                params.put("AdminID", userId);

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
        startActivity(new Intent(AttendanceModule.this, LoginActivity.class));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(AttendanceModule.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(AttendanceModule.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(AttendanceModule.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(AttendanceModule.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(AttendanceModule.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(AttendanceModule.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(AttendanceModule.this,
                "")));

        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(AttendanceModule.this,
                "")));
        UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(AttendanceModule.this,
                "")));

//        Intent intent = new Intent(NewAddLeaveMangementActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();


    }

    private void geofencingcheckdata(String employeeName, String addressName, String latt, String lang) {

        double LattGeo = Double.parseDouble(latt);
        double LangGeo = Double.parseDouble(lang);
        EmployeeName = employeeName;
        addressName1 = addressName;
        GeolatLng = new LatLng(LattGeo, LangGeo);


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCameraView.getHolder().removeCallback(mCameraView);
            mCamera.release();
            mCamera = null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {

            mCamera = Camera.open(1);
            // Add to Framelayout
            mCameraView = new CameraView(this, mCamera);//create a SurfaceView to show camera data
            camera_view.removeAllViews();
            camera_view.addView(mCameraView);

        } catch (RuntimeException ex) {

        }
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            if (message.compareTo("Enable Submit Button") == 0) {
                subBtn.setEnabled(true);
                subBtn.setText("Submit");
            } else {
                subBtn.setEnabled(false);
                subBtn.setText("Reach Your Location");
            }

        }
    };

    private BroadcastReceiver mMessageReceiverDisable = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            if (message.compareTo("Disable Submit Button") == 0) {
                subBtn.setEnabled(false);
                subBtn.setText("Reach Your Location");
            } else {
                subBtn.setEnabled(true);
                subBtn.setText("Submit");
            }

        }
    };


    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiverDisable);
        super.onDestroy();
    }


    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }


    private final int REQ_PERMISSION = 999;

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }


    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    // Initialize GoogleMaps
    private void initGMaps() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick(" + latLng + ")");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClickListener: " + marker.getPosition());
        return false;
    }

    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;

    // Start location Updates
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;

        writeActualLocation(location);

    }

    // GoogleApiClient.ConnectionCallbacks connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
        if (CheckGeoFence == 1) {
            recoverGeofenceMarker();
        }
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    private void writeActualLocation(Location location) {

        markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        if (CheckGeoFence == 1) {
            markerForGeofence(GeolatLng);
            startGeofence();
        }

    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    private Marker locationMarker;

    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation(" + latLng + ")");

        String title = EmployeeName;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        if (mMap != null) {
            if (locationMarker != null)
                locationMarker.remove();
            locationMarker = mMap.addMarker(markerOptions);
            float zoom = 14f;

            builder.include(locationMarker.getPosition());

            if (CheckGeoFence != 1) {

                LatLngBounds bounds = builder.build();
                int padding = 0;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50, 50, padding);
                mMap.animateCamera(cameraUpdate);
            }

        }

    }


    private Marker geoFenceMarker;

    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence(" + latLng + ")");
        String title = addressName1;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if (mMap != null) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();
            geoFenceMarker = mMap.addMarker(markerOptions);

            builder.include(geoFenceMarker.getPosition());
            LatLngBounds bounds = builder.build();
            int padding = 20;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cameraUpdate);

        }
    }

    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        if (geoFenceMarker != null) {
            Geofence geofence = createGeofence(geoFenceMarker.getPosition(), GEOFENCE_RADIUS);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(geofenceRequest);
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "CFCS Office";
    private static final float GEOFENCE_RADIUS = 40.0f; // in meters

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(this, GeofenceTrasitionService.class);
        return PendingIntent.getService(this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(googleApiClient, request, createGeofencePendingIntent()).setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
            saveGeofence();
            drawGeofence();
        } else {
            // inform about fail
        }
    }


    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;

    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(GEOFENCE_RADIUS);
        geoFenceLimits = mMap.addCircle(circleOptions);
    }

    private final String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    private final String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";

    // Saving GeoFence marker with prefs mng
    private void saveGeofence() {
        Log.d(TAG, "saveGeofence()");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(KEY_GEOFENCE_LAT, Double.doubleToRawLongBits(geoFenceMarker.getPosition().latitude));
        editor.putLong(KEY_GEOFENCE_LON, Double.doubleToRawLongBits(geoFenceMarker.getPosition().longitude));
        editor.apply();
    }

    // Recovering last Geofence marker
    private void recoverGeofenceMarker() {
        Log.d(TAG, "recoverGeofenceMarker");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        if (sharedPref.contains(KEY_GEOFENCE_LAT) && sharedPref.contains(KEY_GEOFENCE_LON)) {
            double lat = Double.longBitsToDouble(sharedPref.getLong(KEY_GEOFENCE_LAT, -1));
            double lon = Double.longBitsToDouble(sharedPref.getLong(KEY_GEOFENCE_LON, -1));
            LatLng latLng = new LatLng(lat, lon);
            markerForGeofence(latLng);
            drawGeofence();
        }
    }

    // Clear Geofence
    private void clearGeofence() {
        Log.d(TAG, "clearGeofence()");
        LocationServices.GeofencingApi.removeGeofences(
                googleApiClient,
                createGeofencePendingIntent()
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    // remove drawing
                    removeGeofenceDraw();
                }
            }
        });
    }

    private void removeGeofenceDraw() {
        Log.d(TAG, "removeGeofenceDraw()");
        if (geoFenceMarker != null)
            geoFenceMarker.remove();
        if (geoFenceLimits != null)
            geoFenceLimits.remove();
    }


    //add Attendnace Request
    public void attendaceDetails(final String AdminID, final String Lang, final String Lat, final String LocationAddress,
                                 final String Remark, final String FileJson, final String AuthCode, final String FileExtension) {

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
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        } else  if (LoginStatus.equalsIgnoreCase("success")) {
                           // String MsgNotification = jsonObject.getString("MsgNotification");
                            //Toast.makeText(AttendanceModule.this, MsgNotification, Toast.LENGTH_SHORT).show();
                            if(InOutStatus.compareTo("1") != 0){
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setInOutStatus(AttendanceModule.this,
                                        "1")));
                            }else {
                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setInOutStatus(AttendanceModule.this,
                                        "")));
                            }

                            onBackPressed();
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getBaseContext(),msgstatus, Toast.LENGTH_LONG).show();
                        }
                    }
//                    if (jsonObject.has("status")) {
//                        String status = jsonObject.getString("status");
//
//                        if (status.equalsIgnoreCase("success")) {
//                            String MsgNotification = jsonObject.getString("MsgNotification");
//                            Toast.makeText(AttendanceModule.this, MsgNotification, Toast.LENGTH_SHORT).show();
//                            if(InOutStatus.compareTo("1") != 0){
//                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setInOutStatus(AttendanceModule.this,
//                                        "1")));
//                            }else {
//                                UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setInOutStatus(AttendanceModule.this,
//                                        "")));
//                            }
//
//                            onBackPressed();
//                        }else {
//                            String MsgNotification = jsonObject.getString("MsgNotification");
//                            Toast.makeText(AttendanceModule.this, MsgNotification, Toast.LENGTH_SHORT).show();
//                            Logout();
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

                Toast.makeText(AttendanceModule.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("AdminID", AdminID);
                params.put("AuthCode", AuthCode);
                params.put("Lang", Lang);
                params.put("Lat", Lat);
                params.put("LocationAddress", LocationAddress);
                params.put("Remark", Remark);
                params.put("FileExtension", FileExtension);
                params.put("FileJson", FileJson);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");
    }

    //get current time
    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

        Calendar cal = Calendar.getInstance();
        String sdf = new SimpleDateFormat("LLL", Locale.getDefault()).format(cal.getTime());
        //sdf = new DateFormatSymbols().getShortMonths()[month];

        return dateFormat.format(cal.getTime());
    }


    //check if you have a front Camera
    public static boolean checkCameraFront(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            return true;
        } else {
            return false;
        }
    }

    //Check if you have Camera in your device
    public static boolean checkCameraRear() {
        int numCamera = Camera.getNumberOfCameras();
        if (numCamera > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int setPhotoOrientation(Activity activity, int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            selectImage();
        }
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }

    }

    int getFrontCameraId() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1; // No front-facing camera found
    }

    //uploadImage work
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceModule.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //saveToInternalStorage(photo);
                profileSelectImg.setImageBitmap(photo);
            } else if (requestCode == 2) {

                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    profileSelectImg.setImageBitmap(bitmap);
                    //   uploadImage(imageBase64,userId);

                    //    getCustomerDetail(userId);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        double innerlat = gpsTracker.getLatitude();
        double inerlog = gpsTracker.getLongitude();


    }

    // get address with the help of lat log
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {


            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");

                    break;
                default:
                    locationAddress = null;
            }

            Log.e("checking address", locationAddress);
            //set Current Address
            addTxt.setText(locationAddress);

        }
    }

    //convert bitmap to base64
    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent i = new Intent(AttendanceModule.this,HomeActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }
}

