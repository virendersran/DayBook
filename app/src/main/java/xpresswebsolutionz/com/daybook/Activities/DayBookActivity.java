package xpresswebsolutionz.com.daybook.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import xpresswebsolutionz.com.daybook.Adapters.Pager;
import xpresswebsolutionz.com.daybook.R;

public class DayBookActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date,date1;
    TextView textViewStartdate=null;
    TextView textViewEndDate=null;
    ArrayList<String> freqList,statusList;
    ArrayAdapter<String> sAdapterFreq=null,sAdapterStatus=null;
    Spinner spinnerFre=null,spinnerStatus=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_book);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=preferences.edit();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Pay"));
        tabLayout.addTab(tabLayout.newTab().setText("Receive"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount(),this);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        myCalendar = Calendar.getInstance();

        freqList=new ArrayList<>();
        statusList=new ArrayList<>();
        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }
        };

        freqList.add("Select Frequency");
        freqList.add("Routine");
        freqList.add("Monthly");
        freqList.add("once In a while");
        freqList.add("Yearly");

        statusList.add("Select Status");
        statusList.add("Officially");
        statusList.add("Unofficially");

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        Log.e("Selected","0");
        editor.putString("Pay","0");
        editor.commit();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.e("UnSelected","1");
        editor.putString("Pay","1");
        editor.commit();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.e("Reselected",tab.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_daybook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                finish();
                return true;
            case R.id.menu_item_filter:
                openDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    void openDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.filter_diaog);

        Button btnSet=null,btnReset=null;
        dialog.setTitle("Filter DayBook");
        textViewStartdate=dialog.findViewById(R.id.startDate);
        textViewEndDate=dialog.findViewById(R.id.endDate);
        spinnerFre=dialog.findViewById(R.id.frequency);
        spinnerStatus=dialog.findViewById(R.id.status);
        btnSet = dialog.findViewById(R.id.setFilter);
        btnReset = dialog.findViewById(R.id.resetFilter);
        textViewStartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        sAdapterFreq = new ArrayAdapter<>(dialog.getContext(),R.layout.spinner_item,freqList);
        sAdapterStatus = new ArrayAdapter<>(dialog.getContext(),R.layout.spinner_item,statusList);
        spinnerFre.setAdapter(sAdapterFreq);
        spinnerStatus.setAdapter(sAdapterStatus);



        dialog.create();
        dialog.show();

    }
    public void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textViewStartdate.setText(sdf.format(myCalendar.getTime()));
    }
    public void updateLabel1() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textViewEndDate.setText(sdf.format(myCalendar.getTime()));
    }


}
