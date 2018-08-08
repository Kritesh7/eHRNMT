package in.co.cfcs.ehrnmt.Manager.ManagerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.co.cfcs.ehrnmt.R;

public class ManagerEmployeeDataDashboardActivity extends AppCompatActivity {

    public TextView titleTxt;
    public LinearLayout firstLay, secondLay, thirdTilesLay, fourthTileLay, fivthLay, sixthLay;



     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_employee_data_dashboard);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.mgrtoolbar);
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

        titleTxt.setText("Employee Data");

        firstLay = (LinearLayout) findViewById(R.id.firsttile);
        fivthLay = (LinearLayout) findViewById(R.id.firsttile);
        secondLay = (LinearLayout) findViewById(R.id.secondtiles);
        thirdTilesLay = (LinearLayout) findViewById(R.id.thirdtiles);
        fourthTileLay = (LinearLayout) findViewById(R.id.fourthtile);
        fivthLay = (LinearLayout) findViewById(R.id.fivthlay);
        sixthLay = (LinearLayout) findViewById(R.id.sixthtiles);

        secondLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerEmployeeDataDashboardActivity.this,ManagerFilterActivity.class);
                ik.putExtra("CheckingTheActivity","Administrative Information");
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        thirdTilesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerEmployeeDataDashboardActivity.this,ManagerFilterActivity.class);
                ik.putExtra("CheckingTheActivity","Medical And Insurance");
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        fourthTileLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerEmployeeDataDashboardActivity.this,ManagerFilterActivity.class);
                ik.putExtra("CheckingTheActivity","Address And Contact");
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        fivthLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerEmployeeDataDashboardActivity.this,ManagerFilterActivity.class);
                ik.putExtra("CheckingTheActivity","Emergency Contact Address");
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        firstLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerEmployeeDataDashboardActivity.this,ManagerFilterActivity.class);
                ik.putExtra("CheckingTheActivity","Personal Information");
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

            }
        });

        sixthLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerEmployeeDataDashboardActivity.this,ManagerSkillsAndCareerDashboard.class);
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }

}
