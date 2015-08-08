package me.notify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by David Rommel, B. on 8/9/15.
 */
public class MainActivityViewPager extends FragmentPagerAdapter {

    public MainActivityViewPager(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        switch (index) {
            case 0:
                return new ChannelAll();
            case 1:
                return new ChannelSubscribed();
            case 2:
                return new ChannelHosted();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

}