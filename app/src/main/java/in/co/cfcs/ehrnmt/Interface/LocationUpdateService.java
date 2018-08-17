package in.co.cfcs.ehrnmt.Interface;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.co.cfcs.ehrnmt.Main.AttendanceModule;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;


public class LocationUpdateService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ILocationConstants {


    private static final String TAG = LocationUpdateService.class.getSimpleName();

    /**
     * Provides the entry point to Google Play services.
     */
    protected static GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    public ConnectionDetector conn;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    private Location oldLocation;

    private Location newLocation;

    protected String ANDROID_CHANNEL_ID = "100";

    /**
     * Total distance covered
     */
    private float distance;


    double currentLat;
    double currentLog;

    int count = 0;

    List<String> loglist;

    ArrayList<LocationDataModel> locationDataModels;


    int serviceID = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        oldLocation = new Location("Point A");
        newLocation = new Location("Point B");
        locationDataModels = new ArrayList<LocationDataModel>();

        Notification notification = getNotification("Please before leave the Office mark out");
        startForeground(1, notification);  //not sure what the ID needs to be.

        Log.d(TAG, "onCreate Distance: " + distance);
    }

    // build a persistent notification and return it.
    public Notification getNotification(String message) {

        return new NotificationCompat.Builder(getApplicationContext(), AttendanceModule.id1)
                .setSmallIcon(R.drawable.ic_logo_notification)
                .setOngoing(true)  //persistent notification!
                .setChannelId(AttendanceModule.id1)
                .setContentTitle("Attendance Successfully Mark")   //Title message top row.
                .setContentText(message)  //message when looking at the notification, second row
                .build();  //finally build and return a Notification.

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        conn = new ConnectionDetector(getBaseContext());

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        serviceID = startId;


        if (mGoogleApiClient.isConnected()) {

            startLocationUpdates();

        }

        return START_STICKY;

    }


    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */

    protected void startLocationUpdates() {

        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (SecurityException ex) {

        }
    }

    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */

    private void updateUI(Location location) {

        if (null != mCurrentLocation) {

            Date date1 = new Date();
            String stringDate = DateFormat.getDateInstance().format(date1);

            String DateTime = stringDate + " " + mLastUpdateTime;


            DecimalFormat numberFormat = new DecimalFormat("#.0000");

            double UpdateLocationLat = location.getLatitude();

            double UpdateLocationLong = location.getLongitude();

            double updateLatRound = Double.parseDouble(numberFormat.format(UpdateLocationLat));
            double updateLogRound = Double.parseDouble(numberFormat.format(UpdateLocationLong));

            double currentLatRound = Double.parseDouble(numberFormat.format(oldLocation.getLatitude()));
            double currentLogRound = Double.parseDouble(numberFormat.format(oldLocation.getLongitude()));


            if (currentLatRound == 0 && currentLogRound == 0) {

                if (conn.getConnectivityStatus() > 0) {

                    LocationDataModel locaDataModel = new LocationDataModel();
                    locaDataModel.setLat(String.valueOf(UpdateLocationLat));
                    locaDataModel.setLng(String.valueOf(UpdateLocationLong));
                    locaDataModel.setWaitCounter(String.valueOf(count));
                    locaDataModel.setReportTime(DateTime);
                    locationDataModels.add(locaDataModel);

                    if (locationDataModels.size() > 0) {

                        new UpdateLocation(locationDataModels, getBaseContext()).execute();

                        //     Toast.makeText(getBaseContext(),"currentLatRound == 0 && currentLogRound == 0 and internet find"+ locationDataModels.size(), Toast.LENGTH_LONG).show();

                        count = 0;
                        oldLocation.setLatitude(UpdateLocationLat);
                        oldLocation.setLongitude(UpdateLocationLong);

                    }

                } else {

                    LocationDataModel locaDataModel1 = new LocationDataModel();
                    locaDataModel1.setLat(String.valueOf(UpdateLocationLat));
                    locaDataModel1.setLng(String.valueOf(UpdateLocationLong));
                    locaDataModel1.setWaitCounter(String.valueOf(count));
                    locaDataModel1.setReportTime(DateTime);
                    locationDataModels.add(locaDataModel1);

                    count = 0;
                    oldLocation.setLatitude(UpdateLocationLat);
                    oldLocation.setLongitude(UpdateLocationLong);

                    //     Toast.makeText(getBaseContext(),"currentLatRound == 0 && currentLogRound == 0 and internet not find"+  "Array List" +locationDataModels.size() +"Count" + count, Toast.LENGTH_LONG).show();

                }


            } else if (currentLatRound == updateLatRound && currentLogRound == updateLogRound) {

                count += 1;
                //   Toast.makeText(getBaseContext(),"LatLng equals"+ "Array List" +locationDataModels.size() +"Count" + count, Toast.LENGTH_LONG).show();

            } else {

                if (conn.getConnectivityStatus() > 0) {

                    LocationDataModel locaDataModel2 = new LocationDataModel();
                    locaDataModel2.setLat(String.valueOf(UpdateLocationLat));
                    locaDataModel2.setLng(String.valueOf(UpdateLocationLong));
                    locaDataModel2.setWaitCounter(String.valueOf(count));
                    locaDataModel2.setReportTime(DateTime);
                    locationDataModels.add(locaDataModel2);

                    if (locationDataModels.size() > 0) {

                        new UpdateLocation(locationDataModels, getBaseContext()).execute();

                        //      Toast.makeText(getBaseContext(),"LatLng not 0.00 and interne find"+  "Array List" +locationDataModels.size() +"Count" + count, Toast.LENGTH_LONG).show();

                        count = 0;
                        oldLocation.setLatitude(UpdateLocationLat);
                        oldLocation.setLongitude(UpdateLocationLong);


                    }

                } else {

                    LocationDataModel locaDataModel3 = new LocationDataModel();
                    locaDataModel3.setLat(String.valueOf(UpdateLocationLat));
                    locaDataModel3.setLng(String.valueOf(UpdateLocationLong));
                    locaDataModel3.setWaitCounter(String.valueOf(count));
                    locaDataModel3.setReportTime(DateTime);
                    locationDataModels.add(locaDataModel3);

                    count = 0;

                    oldLocation.setLatitude(UpdateLocationLat);
                    oldLocation.setLongitude(UpdateLocationLong);

                    //      Toast.makeText(getBaseContext(),"LatLng not 0.00 and interne not find"+  "Array List" +locationDataModels.size() +"Count" + count, Toast.LENGTH_LONG).show();

                }

            }

        } else {

//            Toast.makeText(this, R.string.unable_to_find_location, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Send broadcast using LocalBroadcastManager to update UI in activity
     *
     * @param sbLocationData
     */
//    private void sendLocationBroadcast(String sbLocationData) {
//
//        Intent locationIntent = new Intent();
//        locationIntent.setAction(LOACTION_ACTION);
//        locationIntent.putExtra(LOCATION_MESSAGE, sbLocationData);
//
//        LocalBroadcastManager.getInstance(this).sendBroadcast(locationIntent);
//
//    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    public void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
     //   Toast.makeText(this, "Service Stop", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onDestroy() {

        stopLocationUpdates();

        mGoogleApiClient.disconnect();

        Log.d(TAG, "onDestroy Distance " + distance);

        //  Toast.makeText(this,"Service Stop", Toast.LENGTH_LONG).show();

        super.onDestroy();
    }


    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) throws SecurityException {
        Log.i(TAG, "Connected to GoogleApiClient");


        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            //   updateUI(oldLocation);
        }

        startLocationUpdates();

    }

    /**
     * Callback that fires when the location changes.
     */

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI(location);

    }

    @Override
    public void onConnectionSuspended(int cause) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.d(TAG, "TASK REMOVED");

        //  Toast.makeText(this,"Service Restart", Toast.LENGTH_LONG).show();

        PendingIntent service = PendingIntent.getService(
                getApplicationContext(),
                1001,
                new Intent(getApplicationContext(), LocationUpdateService.class),
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
    }


}

