package in.co.cfcs.ehrnmt.Manager.ManagerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.co.cfcs.ehrnmt.R;

public class ManagerDashboardActivity extends AppCompatActivity {

    public TextView titleTxt;
    public LinearLayout requaestApprovedLay, proceedLay, sixthTilesLay, thirdTilesLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_dashboard);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.mgrtoolbar);
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

        titleTxt.setText("Manager Dashboard");

        requaestApprovedLay = (LinearLayout) findViewById(R.id.requesttoapprovetxt);
        proceedLay = (LinearLayout) findViewById(R.id.proceedlay);
        sixthTilesLay = (LinearLayout) findViewById(R.id.sixthtiles);
        thirdTilesLay = (LinearLayout) findViewById(R.id.thirdTiles);

        requaestApprovedLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerDashboardActivity.this, ManagerRequestToApproveActivity.class);
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        proceedLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerDashboardActivity.this, ManagerProceedRequestActivity.class);
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

            }
        });

        sixthTilesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ik = new Intent(ManagerDashboardActivity.this, ManagerFilterActivity.class);
                ik.putExtra("CheckingTheActivity", "Asset Details");
                startActivity(ik);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });

        thirdTilesLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent ik = new Intent(ManagerDashboardActivity.this, ManagerLeaveMangementActivity.class);
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
