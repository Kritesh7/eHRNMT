package in.co.cfcs.ehrnmt.Main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.GPSTracker;

public class ShoweRouteLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double lat, log;
    public GPSTracker gpsTracker;
    public Context context;
    public LinearLayout toolBar;
    public ImageView backImg;
    private ArrayList<LatLng> points = new ArrayList<>();
    Polyline line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        context = ShoweRouteLocation.this;
        toolBar = (LinearLayout) findViewById(R.id.custome_bar);
        backImg = (ImageView) findViewById(R.id.backimg);

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        gpsTracker = new GPSTracker(context, ShoweRouteLocation.this);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        lat = gpsTracker.getLatitude();
        log = gpsTracker.getLongitude();

        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(lat, log);


        //googleMap.clear();  //clears all Markers and Polylines


        LatLng sydney1 = new LatLng(28.5244, 77.1855);
        LatLng sydney2 = new LatLng(28.5374, 77.2597);

        LatLng sydney3 = new LatLng(28.5670, 77.3418);

        LatLng sydney4 = new LatLng(28.5787, 77.3174);

        points.add(sydney1);
        points.add(sydney2);
        points.add(sydney3);
        points.add(sydney4);

        PolylineOptions options = new PolylineOptions().width(15).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        line = mMap.addPolyline(options); //add Polyline

        mMap.addMarker(new MarkerOptions().position(points.get(0)).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(points.get(points.size() - 1)).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(points.get(0)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }

    private void redrawLine() {

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }
}
