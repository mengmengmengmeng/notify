package me.notify;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Created by David Rommel, B. on 8/8/15.
 */
public class MainActivity extends BaseActivity implements OnClickListener{
    private ViewPager viewpager;
    TextView all, subscribed, hosted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.pager);
        MainActivityViewPager padapter = new MainActivityViewPager(getSupportFragmentManager());
        viewpager.setAdapter(padapter);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setIcon(R.drawable.channel_header_icn);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grayy)));

        actionBar.setTitle("Channel");

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        all = (TextView) findViewById(R.id.all);
        subscribed = (TextView) findViewById(R.id.subscribed);
        hosted = (TextView) findViewById(R.id.hosted);

        all.setOnClickListener(this);
        subscribed.setOnClickListener(this);
        hosted.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.all:
                viewpager.setCurrentItem(1);
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    all.setBackgroundDrawable( getResources().getDrawable(R.drawable.tab_style_background_with_indicator));
                    subscribed.setBackgroundDrawable( getResources().getDrawable(R.drawable.tab_style_background));
                    hosted.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_style_background));
                } else {
                    all.setBackground( getResources().getDrawable(R.drawable.tab_style_background_with_indicator));
                    subscribed.setBackground(getResources().getDrawable(R.drawable.tab_style_background));
                    hosted.setBackground( getResources().getDrawable(R.drawable.tab_style_background));
                }
                break;

            case R.id.hosted:
                viewpager.setCurrentItem(0);
                sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    hosted.setBackgroundDrawable( getResources().getDrawable(R.drawable.tab_style_background_with_indicator));
                    subscribed.setBackgroundDrawable( getResources().getDrawable(R.drawable.tab_style_background));
                    all.setBackgroundDrawable( getResources().getDrawable(R.drawable.tab_style_background));
                } else {
                    hosted.setBackground( getResources().getDrawable(R.drawable.tab_style_background_with_indicator));
                    subscribed.setBackground( getResources().getDrawable(R.drawable.tab_style_background));
                    all.setBackground( getResources().getDrawable(R.drawable.tab_style_background));
                }
                break;

            case R.id.subscribed:
                viewpager.setCurrentItem(1);
                sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    subscribed.setBackgroundDrawable( getResources().getDrawable(R.drawable.tab_style_background_with_indicator));
                    all.setBackgroundDrawable( getResources().getDrawable(R.drawable.tab_style_background));
                    hosted.setBackgroundDrawable( getResources().getDrawable(R.drawable.tab_style_background));
                } else {
                    subscribed.setBackground( getResources().getDrawable(R.drawable.tab_style_background_with_indicator));
                    all.setBackground( getResources().getDrawable(R.drawable.tab_style_background));
                    hosted.setBackground( getResources().getDrawable(R.drawable.tab_style_background));
                }
                break;

            default:
                break;
        }
    }
}