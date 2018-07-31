package in.co.cfcs.ehrnmt.Main;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.cfcs.ehrnmt.R;

public class AddandEditTraningActivity extends AppCompatActivity {

    public TextView titleTxt;
    public Spinner domainSpinner,courseSpinner, profeceancySpinner,prioritySpinner;
    public ArrayList<String> domainList = new ArrayList<>();
    public ArrayList<String> courseList = new ArrayList<>();
    public ArrayList<String> proficeancyList = new ArrayList<>();
    public ArrayList<String> priorityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addand_edit_traning);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.status_color));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.traningtoolbar);
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

        titleTxt.setText("Add New Training");

        domainSpinner = (Spinner)findViewById(R.id.domainspinner);
        courseSpinner = (Spinner)findViewById(R.id.coursespinner);
        profeceancySpinner = (Spinner)findViewById(R.id.proficiencyspinner);
        prioritySpinner = (Spinner)findViewById(R.id.priorityspinner);


        //domain Spinner
        if (domainList.size()>0)
        {
            domainList.clear();
        }
        domainList.add("Please Select Domain");
        domainList.add(".Net");
        domainList.add("Android");
        domainList.add("Other");


        //change spinner arrow color

        domainSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> DomainAdapter = new ArrayAdapter<String>(AddandEditTraningActivity.this, R.layout.customizespinner,
                domainList);
        DomainAdapter.setDropDownViewResource(R.layout.customizespinner);
        domainSpinner.setAdapter(DomainAdapter);

        // course Spinner
        if (courseList.size()>0)
        {
            courseList.clear();
        }
        courseList.add("Please Select Course");
        courseList.add("ASP.Net");
        courseList.add("Mvc");
        courseList.add("LINQ");


        //change spinner arrow color

        courseSpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(AddandEditTraningActivity.this, R.layout.customizespinner,
                courseList);
        courseAdapter.setDropDownViewResource(R.layout.customizespinner);
        courseSpinner.setAdapter(courseAdapter);

        // profeceancy Spinner
        if (proficeancyList.size()>0)
        {
            proficeancyList.clear();
        }
        proficeancyList.add("Please Select Proficiency");
        proficeancyList.add("Beginner");



        //change spinner arrow color

        profeceancySpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> profieceancyAdapter = new ArrayAdapter<String>(AddandEditTraningActivity.this, R.layout.customizespinner,
                proficeancyList);
        profieceancyAdapter.setDropDownViewResource(R.layout.customizespinner);
        profeceancySpinner.setAdapter(profieceancyAdapter);

        //prioirty Spinner

        if (priorityList.size()>0)
        {
            priorityList.clear();
        }
        priorityList.add("Please Select Priority");
        priorityList.add("Higher");


        //change spinner arrow color

        prioritySpinner.getBackground().setColorFilter(getResources().getColor(R.color.status_color), PorterDuff.Mode.SRC_ATOP);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(AddandEditTraningActivity.this, R.layout.customizespinner,
                priorityList);
        priorityAdapter.setDropDownViewResource(R.layout.customizespinner);
        prioritySpinner.setAdapter(priorityAdapter);


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_right_out);

    }

}
