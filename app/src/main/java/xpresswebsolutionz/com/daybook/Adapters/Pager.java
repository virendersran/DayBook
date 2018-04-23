package xpresswebsolutionz.com.daybook.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import xpresswebsolutionz.com.daybook.Fragments.DisplayContactsFragment;
import xpresswebsolutionz.com.daybook.Fragments.DisplayDayBookFragment;
import xpresswebsolutionz.com.daybook.Fragments.PayFragment;
import xpresswebsolutionz.com.daybook.Fragments.ReceiveFragment;

public class Pager extends FragmentStatePagerAdapter {
    int tabCount;
    String[] tabTitle = new String[]{"Pay","Receive"};
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount, Context context) {
        super(fm);
        this.context=context;
        preferences= PreferenceManager.getDefaultSharedPreferences(context);
        editor=preferences.edit();
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                PayFragment tab1 = new PayFragment();
                return tab1;
            case 1:
                ReceiveFragment tab2 = new ReceiveFragment();
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
