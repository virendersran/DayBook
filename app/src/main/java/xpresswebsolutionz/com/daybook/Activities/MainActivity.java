package xpresswebsolutionz.com.daybook.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import xpresswebsolutionz.com.daybook.Fragments.AddPersonsFragment;
import xpresswebsolutionz.com.daybook.Fragments.DepartmentsFragment;
import xpresswebsolutionz.com.daybook.Fragments.DisplayContactsFragment;
import xpresswebsolutionz.com.daybook.Fragments.DisplayDayBookFragment;
import xpresswebsolutionz.com.daybook.Fragments.HomeFragment;
import xpresswebsolutionz.com.daybook.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.frameLayout,homeFragment).commit();
        setTitle("DayBook");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addBook) {
            setTitle("Add DayBook");
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.frameLayout,homeFragment).commit();

        } else if (id == R.id.nav_dayBook) {
            setTitle("DayBook");
//            DisplayDayBookFragment dayBookFragment = new DisplayDayBookFragment();
//            fragmentManager.beginTransaction().replace(R.id.frameLayout,dayBookFragment).commit();
            startActivity(new Intent(MainActivity.this,DayBookActivity.class));
            overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
            return false;
        } else if (id == R.id.nav_addPerson) {
            setTitle("Contacts");
//            DisplayContactsFragment contactsFragment = new DisplayContactsFragment();
//            fragmentManager.beginTransaction().replace(R.id.frameLayout,contactsFragment).commit();
            startActivity(new Intent(MainActivity.this,ContactActivity.class));
            overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
            return false;

        } else if (id == R.id.nav_addDept) {
            setTitle("Department");
            DepartmentsFragment departmentsFragment = new DepartmentsFragment();
            fragmentManager.beginTransaction().replace(R.id.frameLayout,departmentsFragment).commit();

//        }else if (id == R.id.nav_addPerson1) {
//            setTitle("Add Contact");
//            AddPersonsFragment personsFragment = new AddPersonsFragment();
//            fragmentManager.beginTransaction().replace(R.id.frameLayout,personsFragment).commit();

        }
        else if (id == R.id.nav_Logout) {


            editor.clear();
            editor.commit();
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();

        }
//nav_addPerson1
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}

