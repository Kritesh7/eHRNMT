package in.co.cfcs.ehrnmt.Main;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.co.cfcs.ehrnmt.Fragment.AssestDetailsFragment;
import in.co.cfcs.ehrnmt.Fragment.AttendaceListFragment;
import in.co.cfcs.ehrnmt.Fragment.AttendanceLogListFragment;
import in.co.cfcs.ehrnmt.Fragment.ChnagePasswordFragment;
import in.co.cfcs.ehrnmt.Fragment.ContactPhoneFragment;
import in.co.cfcs.ehrnmt.Fragment.ContactsDetailsFragment;
import in.co.cfcs.ehrnmt.Fragment.DashBoardFragment;
import in.co.cfcs.ehrnmt.Fragment.DependnetsFragment;
import in.co.cfcs.ehrnmt.Fragment.DocumentListFragment;
import in.co.cfcs.ehrnmt.Fragment.EducationDetailsFragment;
import in.co.cfcs.ehrnmt.Fragment.EmergencyContactsFragment;
import in.co.cfcs.ehrnmt.Fragment.HolidayListFragment;
import in.co.cfcs.ehrnmt.Fragment.HotelBookingListFragment;
import in.co.cfcs.ehrnmt.Fragment.LanguagesFragment;
import in.co.cfcs.ehrnmt.Fragment.LeaveManagementFragment;
import in.co.cfcs.ehrnmt.Fragment.LeaveSummarryFragment;
import in.co.cfcs.ehrnmt.Fragment.ManagerDashBoardFragment;
import in.co.cfcs.ehrnmt.Fragment.MedicalAndEnsuranceFragment;
import in.co.cfcs.ehrnmt.Fragment.MedicalDetailsFragment;
import in.co.cfcs.ehrnmt.Fragment.MyProfileFragment;
import in.co.cfcs.ehrnmt.Fragment.OfficeallyDetailsFragment;
import in.co.cfcs.ehrnmt.Fragment.PersonalDetailsFragment;
import in.co.cfcs.ehrnmt.Fragment.PreviousExprienceFragment;
import in.co.cfcs.ehrnmt.Fragment.ShortLeaveHistoryFragment;
import in.co.cfcs.ehrnmt.Fragment.SkillsFragment;
import in.co.cfcs.ehrnmt.Fragment.StationaryRequestFragment;
import in.co.cfcs.ehrnmt.Fragment.TaxiListFragment;
import in.co.cfcs.ehrnmt.Fragment.WeekOfListFragment;
import in.co.cfcs.ehrnmt.Interface.LocationUpdateService;
import in.co.cfcs.ehrnmt.Manager.ManagerActivity.ManagerRequestToApproveActivity;
import in.co.cfcs.ehrnmt.R;
import in.co.cfcs.ehrnmt.Source.AppController;
import in.co.cfcs.ehrnmt.Source.ConnectionDetector;
import in.co.cfcs.ehrnmt.Source.NotificationBroadCast;
import in.co.cfcs.ehrnmt.Source.SettingConstant;
import in.co.cfcs.ehrnmt.Source.SharedPrefs;
import in.co.cfcs.ehrnmt.Source.UtilsMethods;
import uk.co.senab.photoview.PhotoViewAttacher;

public class HomeActivity extends AppCompatActivity implements DashBoardFragment.OnFragmentInteractionListenerForToolbar,
        TaxiListFragment.OnFragmentInteractionListener, ManagerDashBoardFragment.OnFragmentInteractionListener,
        MyProfileFragment.OnFragmentInteractionListener, WeekOfListFragment.OnFragmentInteractionListener,
        HolidayListFragment.OnFragmentInteractionListener, ContactPhoneFragment.OnFragmentInteractionListener,
        AttendanceLogListFragment.OnFragmentInteractionListener, ShortLeaveHistoryFragment.OnFragmentInteractionListener,
        SkillsFragment.OnFragmentInteractionListener, LanguagesFragment.OnFragmentInteractionListener,
        DependnetsFragment.OnFragmentInteractionListener, DocumentListFragment.OnFragmentInteractionListener,
        AssestDetailsFragment.OnFragmentInteractionListener, AttendaceListFragment.OnFragmentInteractionListener,
        LeaveSummarryFragment.OnFragmentInteractionListener, ChnagePasswordFragment.OnFragmentInteractionListener,
        MedicalDetailsFragment.OnFragmentInteractionListener, ContactsDetailsFragment.OnFragmentInteractionListener,
        PersonalDetailsFragment.OnFragmentInteractionListener, PreviousExprienceFragment.OnFragmentInteractionListener,
        StationaryRequestFragment.OnFragmentInteractionListener, OfficeallyDetailsFragment.OnFragmentInteractionListener,
        HotelBookingListFragment.OnFragmentInteractionListener, LeaveManagementFragment.OnFragmentInteractionListener,
        EmergencyContactsFragment.OnFragmentInteractionListener, MedicalAndEnsuranceFragment.OnFragmentInteractionListener,
        EducationDetailsFragment.OnFragmentInteractionListener {

    private Handler mHandeler;
    public NavigationView navigationView;
    public DrawerLayout drawerLayout;
    public View navHeader;
    private static final String TAG_Dashboard = "Dashboard";
    private static final String TAG_Manager = "Manager";
    private static final String TAG_Attendnace = "Attendance";
    private static final String TAG_Leave_Management = "LeaveMangement";
    private static final String TAG_Traning = "Traning";
    private static final String TAG_Asset_Details = "AssestDeatils";
    private static final String TAG_Employ_UpdateProfile = "UpdateProfile";
    private static final String TAG_Employ_PersonalDetails = "PersonalDetails";
    private static final String TAG_Employ_MedicalDetails = "MedicalDetails";
    private static final String TAG_Employ_OfficeallyDetails = "OfficeallyDetails";
    private static final String TAG_Employ_ContactsDetails = "ContactsDetails";
    private static final String TAG_Employ_Emergency_CntactDetails = "EmergencyContactsDetails";
    private static final String TAG_Employ_Dependents = "Dependents ";
    private static final String TAG_Employ_MedicalAndAnsurense = "MedicalAndAnsurense ";
    private static final String TAG_Employ_EducationDetails = "EducationDetails";
    private static final String TAG_Employ_PreviousExpreince = "PreviousExpreince";
    private static final String TAG_Employ_Languages = "Languages";
    private static final String TAG_Employ_Skills = "Skills";
    private static final String TAG_Employ_StationaryRequest = "StationaryRequest";
    private static final String TAG_Employ_DocumentList = "DocumentList";
    private static final String TAG_Employ_CabList = "CabList";
    private static final String TAG_Employ_HotelBooking = "HotelBooking";
    private static final String TAG_Employ_Logout = "Logout";
    private static final String TAG_Leave_Summrry = "Leave Summary";
    private static final String TAG_Short_Leave_History = "Short Leave History";
    private static final String TAG_WeekOf = "Week Of";
    private static final String TAG_Holiday_List = "Holiday List";
    private static final String TAG_Contact_Phone = "Contact Phone";
    private static final String TAG_AttendanceLogList = "AttendanceLogList";
    private static final String TAG_MYProfile = "MY PROFILE";
    public String userNameStr = "", photoStr = "", empIdStr = "", designationStr = "", companLogoStr = "";
    public static int navigationItemIndex = 0;
    public Toolbar toolbar;
    public static String CURRENT_TAG = TAG_Dashboard;
    private boolean shouldLoadHomeFragOnBackPress = true;
    public TextView titleTxt;
    public de.hdodenhof.circleimageview.CircleImageView proImg;
    public TextView nameTxt, designationTxt, empIdTxt;
    public ImageView backImg;
    public PhotoViewAttacher mAttacher;
    public String countUrl = SettingConstant.BaseUrl + "AppManagerRequestToApproveDashBoard";
    public String logoutUrl = SettingConstant.BASEURL_FOR_LOGIN + "AppLoginLogOut";
    public int count;
    public String userId = "", authCode = "";
    public TextView itemMessagesBadgeTextView;
    public PendingIntent pendingIntent;
    public AlarmManager manager;
    public ConnectionDetector conn;
    public Bundle bundle;
    // public String countinOne = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        userId = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAdminId(HomeActivity.this)));
        authCode = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getAuthCode(HomeActivity.this)));
        conn = new ConnectionDetector(HomeActivity.this);


        toolbar = (Toolbar) findViewById(R.id.hometollbar);
        setSupportActionBar(toolbar);
        titleTxt = (TextView) toolbar.findViewById(R.id.titletxt);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mHandeler = new Handler();

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.home_navigation);


        //     headeLay = (RelativeLayout) navigationView.findViewById(R.id.view_container) ;

        bundle = new Bundle();


        navHeader = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        proImg = (de.hdodenhof.circleimageview.CircleImageView) navHeader.findViewById(R.id.pro_image);
        nameTxt = (TextView) navHeader.findViewById(R.id.name);
        empIdTxt = (TextView) navHeader.findViewById(R.id.empid);
        designationTxt = (TextView) navHeader.findViewById(R.id.designation);
        backImg = (ImageView) navHeader.findViewById(R.id.backimage);

        userNameStr = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getUserName(HomeActivity.this)));
        photoStr = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getEmpPhoto(HomeActivity.this)));
        empIdStr = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getEmpId(HomeActivity.this)));
        designationStr = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getDesignation(HomeActivity.this)));
        companLogoStr = UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.getCompanyLogo(HomeActivity.this)));

        nameTxt.setText(userNameStr);
        empIdTxt.setText(empIdStr);
        designationTxt.setText(designationStr);

        //set profile Image
        Picasso pic = Picasso.with(HomeActivity.this);
        pic.setIndicatorsEnabled(true);
        pic.with(HomeActivity.this).cancelRequest(proImg);
        pic.with(HomeActivity.this)
                .load(SettingConstant.DownloadUrl + photoStr)
                .placeholder(R.drawable.prf)
                .error(R.drawable.prf)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .tag(HomeActivity.this)
                .into(proImg);

        //set Back Image
        Picasso backImgLoad = Picasso.with(HomeActivity.this);
        backImgLoad.setIndicatorsEnabled(true);
        backImgLoad.with(HomeActivity.this).cancelRequest(backImg);
        backImgLoad.with(HomeActivity.this)
                .load(SettingConstant.DownloadUrl + companLogoStr)
                .placeholder(R.drawable.logomain)
                .error(R.drawable.logomain)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .tag(HomeActivity.this)
                .into(backImg);


        proImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                int height = display.getHeight();
                loadPhoto(proImg, width, height);
            }
        });


        titleTxt.setText("Dashboard");


        //Notification BroadCast Recicver
        if (conn.getConnectivityStatus() > 0) {

            Intent alarm = new Intent(HomeActivity.this, NotificationBroadCast.class);
            pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, alarm, 0);
            startalarmreciver(pendingIntent);

        } else {
            conn.showNoInternetAlret();
        }


        //count the notification
        //call API
        getCount(authCode, userId);


    }

    //Start Notification
    public void startalarmreciver(PendingIntent intent) {
        manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1800000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, intent);
        //Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    //cancel Notification
    public void cancelAlarm() {
        Intent intent = new Intent(this, NotificationBroadCast.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        //Toast.makeText(this, "Cancelled alarm", Toast.LENGTH_SHORT).show();
    }

   /* @Override
    protected void onResume() {
        super.onResume();

        //count the notification
        //call API
        getCount(authCode,userId);
    }*/

    private void loadPhoto(ImageView imageView, int width, int height) {

        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.setContentView(R.layout.custom_fullimage_dialog);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_fullimage_dialoge,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        ImageView croosImg = (ImageView) layout.findViewById(R.id.imgClose);

        croosImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        image.setImageDrawable(imageView.getDrawable());
        image.getLayoutParams().height = height;
        image.getLayoutParams().width = width;
        mAttacher = new PhotoViewAttacher(image);
        image.requestLayout();
        dialog.setContentView(layout);
        dialog.show();

    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_dashboard:

                        navigationItemIndex = 0;
                        CURRENT_TAG = TAG_Dashboard;
                        titleTxt.setText("Dashboard");


                        break;

                    case R.id.nav_mger:

                        navigationItemIndex = 27;
                        CURRENT_TAG = TAG_Manager;
                        titleTxt.setText("Manager Dashboard");


                        break;


                    case R.id.nav_attendance:


                        navigationItemIndex = 1;
                        CURRENT_TAG = TAG_Attendnace;
                        titleTxt.setText("Attendance List");

                        break;

                    case R.id.nav_leavemanagment:

                        navigationItemIndex = 2;
                        CURRENT_TAG = TAG_Leave_Management;
                        titleTxt.setText("Leave History");

                        break;



                  /*  case R.id.nav_Traning:

                        navigationItemIndex = 3;
                        CURRENT_TAG = TAG_Traning;
                        titleTxt.setText("Training");

                        break;*/

                    case R.id.nav_Asset_details:

                        navigationItemIndex = 4;
                        CURRENT_TAG = TAG_Asset_Details;
                        titleTxt.setText("Assets Details");

                        break;


                    case R.id.nav_change_password:

                        navigationItemIndex = 5;
                        CURRENT_TAG = TAG_Employ_UpdateProfile;
                        titleTxt.setText("Update Profile");


                        break;

                    case R.id.nav_personaldetails:

                        navigationItemIndex = 6;
                        CURRENT_TAG = TAG_Employ_PersonalDetails;
                        titleTxt.setText("Personal Details");

                        break;

                    case R.id.nav_medical_detials:

                        navigationItemIndex = 7;
                        CURRENT_TAG = TAG_Employ_MedicalDetails;
                        titleTxt.setText("Medical Details");

                        break;

                    case R.id.nav_officeally_details:

                        navigationItemIndex = 8;
                        CURRENT_TAG = TAG_Employ_OfficeallyDetails;
                        titleTxt.setText("Office Details");

                        break;

                    case R.id.nav_contact_details:

                        navigationItemIndex = 9;
                        CURRENT_TAG = TAG_Employ_ContactsDetails;
                        titleTxt.setText("Contact Address");

                        break;

                    case R.id.nav_emergency_contact_details:

                        navigationItemIndex = 10;
                        CURRENT_TAG = TAG_Employ_Emergency_CntactDetails;
                        titleTxt.setText("Emergency Contact Address");

                        break;

                    case R.id.nav_dependents:

                        navigationItemIndex = 11;
                        CURRENT_TAG = TAG_Employ_Dependents;
                        titleTxt.setText("Dependents");

                        break;

                    case R.id.nav_medical_and_anssurence:

                        navigationItemIndex = 12;
                        CURRENT_TAG = TAG_Employ_MedicalAndAnsurense;
                        titleTxt.setText("Medical and Insurance");

                        break;

                    case R.id.nav_education_details:

                        navigationItemIndex = 13;
                        CURRENT_TAG = TAG_Employ_EducationDetails;
                        titleTxt.setText("Education Details");

                        break;

                    case R.id.nav_previous_expreince:

                        navigationItemIndex = 14;
                        CURRENT_TAG = TAG_Employ_PreviousExpreince;
                        titleTxt.setText("Previous Experience");

                        break;

                    case R.id.nav_languages:

                        navigationItemIndex = 15;
                        CURRENT_TAG = TAG_Employ_Languages;
                        titleTxt.setText("Language");

                        break;

                    case R.id.nav_skills:

                        navigationItemIndex = 16;
                        CURRENT_TAG = TAG_Employ_Skills;
                        titleTxt.setText("Skills");

                        break;

                    case R.id.nav_leavesummary:

                        navigationItemIndex = 17;
                        CURRENT_TAG = TAG_Leave_Summrry;
                        titleTxt.setText("Leave Summary");

                        break;

                    case R.id.nav_stationary_request:

                        navigationItemIndex = 18;
                        CURRENT_TAG = TAG_Employ_StationaryRequest;
                        titleTxt.setText("Stationary Request");

                        break;

                    case R.id.nav_document_list:

                        navigationItemIndex = 19;
                        CURRENT_TAG = TAG_Employ_DocumentList;
                        titleTxt.setText("Document List");

                        break;

                    case R.id.nav_cab_list:

                        navigationItemIndex = 20;
                        CURRENT_TAG = TAG_Employ_CabList;
                        titleTxt.setText("Cab List");

                        break;

                    case R.id.nav_hotel_booking_list:

                        navigationItemIndex = 21;
                        CURRENT_TAG = TAG_Employ_HotelBooking;
                        titleTxt.setText("Hotel Booking List");

                        break;

                    case R.id.nav_short_leave_history:

                        navigationItemIndex = 23;
                        CURRENT_TAG = TAG_Short_Leave_History;
                        titleTxt.setText("Short Leave History");

                        break;

                    case R.id.nav_weekof:

                        navigationItemIndex = 24;
                        CURRENT_TAG = TAG_WeekOf;
                        titleTxt.setText("Week Off");

                        break;

                    case R.id.nav_holidayList:

                        navigationItemIndex = 25;
                        CURRENT_TAG = TAG_Holiday_List;
                        titleTxt.setText("Holiday List");

                        break;

                    case R.id.nav_contact_phone:

                        navigationItemIndex = 26;
                        CURRENT_TAG = TAG_Contact_Phone;
                        titleTxt.setText("Contact Phone");
                        break;

                    case R.id.nav_log_attendance:

                        navigationItemIndex = 28;
                        CURRENT_TAG = TAG_AttendanceLogList;
                        titleTxt.setText("Attendance Log List");
                        break;

                    case R.id.nav_pro:

                        navigationItemIndex = 29;
                        CURRENT_TAG = TAG_MYProfile;
                        titleTxt.setText("My Profile");
                        break;

                    case R.id.nav_logout:

                        //Logout API
                        getLogout(userId, authCode);
                        break;


                    default:
                        navigationItemIndex = 0;
                }
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        //   selectNavMenu();


        Log.e("check current tag", CURRENT_TAG + " null");
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {


            drawerLayout.closeDrawers();
            // show or hide the fab button
            //    toggleFab();
            return;
        }

        //Closing drawer on item click
        drawerLayout.closeDrawers();
        // refresh toolbar menu
        invalidateOptionsMenu();

        getHomeFragment();
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navigationItemIndex).setChecked(true);
    }

    private Fragment getHomeFragment() {

        Fragment newFragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Log.e("checked postion", navigationItemIndex + " null");
        switch (navigationItemIndex) {
            case 0:
                // home
              /*  DashBoardFragment dashboard = new DashBoardFragment();
                return dashboard;*/

             /*   ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                        super.onDrawerClosed(drawerView);
                    }
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                        super.onDrawerOpened(drawerView);
                    }
                };
                //Setting the actionbarToggle to drawer layout
                drawerLayout.setDrawerListener(actionBarDrawerToggle);
*/
                newFragment = new DashBoardFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

               /* DashBoardFragment dashboard = new DashBoardFragment();
                return dashboard;*/

            case 1:
                //drawer.closeDrawers();
                // movies fragment
                /*  PayoutsListFragment payoutsListFragment = new PayoutsListFragment();*/

               /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);*/

                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);


                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new AttendaceListFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 2:

              /*  if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new LeaveManagementFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

               /* FollowUpFragment followUpFragment = new FollowUpFragment();
                return followUpFragment;*/

           /* case 3:

                newFragment = new TrainingFragment();
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment ;*/


           /* ChatFragment chatFragment = new ChatFragment();
                return chatFragment;*/

            case 4:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new AssestDetailsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 5:
/*
                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new ChnagePasswordFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 6:

              /*  if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new PersonalDetailsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 7:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new MedicalDetailsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 8:

                /*if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new OfficeallyDetailsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 9:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new ContactsDetailsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 10:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new EmergencyContactsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 11:

              /*  if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new DependnetsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 12:

              /*  if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new MedicalAndEnsuranceFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 13:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new EducationDetailsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 14:

              /*  if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new PreviousExprienceFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 15:

             /*   if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new LanguagesFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 16:
/*
                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new SkillsFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 17:


               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new LeaveSummarryFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 18:


              /*  if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });

                newFragment = new StationaryRequestFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 19:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new DocumentListFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 20:

              /*  if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new TaxiListFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 21:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new HotelBookingListFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 23:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new ShortLeaveHistoryFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 24:

              /*  if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new WeekOfListFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 25:
/*
                if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new HolidayListFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 26:

                /*if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new ContactPhoneFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 27:

                /*if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new ManagerDashBoardFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 28:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });

                newFragment = new AttendanceLogListFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;

            case 29:

               /* if (getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }*/
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                toolbar.setNavigationIcon(R.drawable.backimg);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // onBackPressed();
                        onBackPressed();

                    }
                });


                newFragment = new MyProfileFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;


            default:

                newFragment = new DashBoardFragment();
                newFragment.setArguments(bundle);
                transaction.replace(R.id.home_navigation_framelayout, newFragment);
                transaction.setCustomAnimations(
                        R.anim.push_right_in,
                        R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out);
                transaction.addToBackStack(null);
                transaction.commit();
                return newFragment;
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navigationItemIndex != 0) {

                getCount(authCode, userId);
                titleTxt.setText("Dashboard");

                //   Log.e("cheking the onbackpressed count", countinOne + " null");

              /*  if (!countinOne.equalsIgnoreCase("" )) {
                    int co = Integer.parseInt(countinOne);
                    if (co > 0) {
                        itemMessagesBadgeTextView.setVisibility(View.VISIBLE);
                        itemMessagesBadgeTextView.setText(countinOne + "");
                    } else {
                        itemMessagesBadgeTextView.setVisibility(View.GONE);
                    }

                }*/


            /*    Log.e("cheking the onbackpressed count", countinOne + " null");
                bundle.putString("Count", countinOne);
*/
                return;
            }
        }

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.count_menu, menu);

        MenuItem itemMessages = menu.findItem(R.id.menu_messages);
        MenuItemCompat.setActionView(itemMessages, R.layout.badge_layout);
        View view = MenuItemCompat.getActionView(itemMessages);
        itemMessagesBadgeTextView = (TextView) view.findViewById(R.id.badge_textView);


        ImageView iconButtonMessages = (ImageView) view.findViewById(R.id.badge_icon_button);
        iconButtonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(HomeActivity.this, ManagerRequestToApproveActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        return super.onCreateOptionsMenu(menu);

        //getMenuInflater().inflate(R.menu.count_menu, menu);

        // MenuItem menuItem = menu.findItem(R.id.menu_messages);
        //menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_menu_gallery));

        //return true;
    }

    //show  count api
    public void getCount(final String AuthCode, final String AdminID) {

        final ProgressDialog pDialog = new ProgressDialog(HomeActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, countUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.has("MsgNotification")) {
                            String MsgNotification = jsonObject.getString("MsgNotification");
                            Toast.makeText(HomeActivity.this,MsgNotification, Toast.LENGTH_LONG).show();
                            getLogout(userId, authCode);
                        }else {

                            String LeaveCount = jsonObject.getString("LeaveCount");
                            String CancelLeaveCount = jsonObject.getString("CancelLeaveCount");
                            String ShortLeaveCount = jsonObject.getString("ShortLeaveCount");
                            String ShortCancelLeaveCount = jsonObject.getString("ShortCancelLeaveCount");
                            String TrainingCount = jsonObject.getString("TrainingCount");


                            count = Integer.parseInt(LeaveCount) + Integer.parseInt(CancelLeaveCount) + Integer.parseInt(ShortLeaveCount) +
                                    Integer.parseInt(ShortCancelLeaveCount) + Integer.parseInt(TrainingCount);

                            Log.e("count is ", count + "");
                            //     countinOne = String.valueOf(count);


                            //to save on bundel pass the count activity to fragment
                            bundle.putString("Count", count + "");


                            setUpNavigationView();

                            navigationItemIndex = 0;
                            CURRENT_TAG = TAG_Dashboard;
                            loadHomeFragment();

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
                params.put("AuthCode", AuthCode);
                params.put("AdminID", AdminID);


                Log.e("Parms", params.toString());
                return params;
            }

        };
        historyInquiry.setRetryPolicy(new DefaultRetryPolicy(SettingConstant.Retry_Time,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(historyInquiry, "Login");

    }

    //Log Out API Work
    public void getLogout(final String AdminID, final String AuthCode) {

        final ProgressDialog pDialog = new ProgressDialog(HomeActivity.this, R.style.AppCompatAlertDialogStyle);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest historyInquiry = new StringRequest(
                Request.Method.POST, logoutUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.e("Login", response);
                    JSONArray jsonArray = new JSONArray(response.substring(response.indexOf("["), response.lastIndexOf("]") + 1));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String status = jsonObject.getString("status");

                        if (status.equalsIgnoreCase("success")) {

                            navigationItemIndex = 22;

                            Intent ik = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(ik);
                            overridePendingTransition(R.anim.push_left_in,
                                    R.anim.push_right_out);
                            finish();

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(HomeActivity.this,
                                    "")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(HomeActivity.this,
                                    "")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(HomeActivity.this,
                                    "")));

                            //cancel background services
                            cancelAlarm();

                            getBaseContext().stopService(new Intent(HomeActivity.this, LocationUpdateService.class));

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in,
                                    R.anim.push_right_out);
                            finish();

                        } else {

                            String MsgNotification = jsonObject.getString("MsgNotification");
                            Toast.makeText(HomeActivity.this, MsgNotification, Toast.LENGTH_SHORT).show();

                            navigationItemIndex = 22;

                            Intent ik = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(ik);
                            overridePendingTransition(R.anim.push_left_in,
                                    R.anim.push_right_out);
                            finish();

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setStatus(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAdminId(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setAuthCode(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmailId(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setUserName(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpId(HomeActivity.this,
                                    "")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setEmpPhoto(HomeActivity.this,
                                    "")));

                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setDesignation(HomeActivity.this,
                                    "")));
                            UtilsMethods.getBlankIfStringNull(String.valueOf(SharedPrefs.setCompanyLogo(HomeActivity.this,
                                    "")));

                            //cancel background services
                            cancelAlarm();


                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in,
                                    R.anim.push_right_out);
                            finish();
                        }


                        CURRENT_TAG = TAG_Employ_Logout;
                        titleTxt.setText("Log Out");

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
    public void onFragmentInteractionForToolbarMethod(int navigationCount, String Title, String countstr) {

        navigationItemIndex = navigationCount;
        titleTxt.setText(Title);

        if (navigationItemIndex != 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolbar.setNavigationIcon(R.drawable.backimg);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // onBackPressed();
                    onBackPressed();

                }
            });
        }

        //check notification count
        // if (!countstr.equalsIgnoreCase("" ) || !countstr.equalsIgnoreCase("null")) {
        int co = Integer.parseInt(countstr);
        if (co > 0) {
            itemMessagesBadgeTextView.setVisibility(View.VISIBLE);
            itemMessagesBadgeTextView.setText(countstr + "");
        } else {
            itemMessagesBadgeTextView.setVisibility(View.GONE);
        }

        //  }

    }

    @Override
    public void onFragmentInteraction(String count) {

        //  countinOne = count;


        if (!count.equalsIgnoreCase("") || !count.equalsIgnoreCase("null")) {
            int co = Integer.parseInt(count);
            if (co > 0) {
                itemMessagesBadgeTextView.setVisibility(View.VISIBLE);
                itemMessagesBadgeTextView.setText(count + "");
            } else {
                itemMessagesBadgeTextView.setVisibility(View.GONE);
            }

        }

    }
}
