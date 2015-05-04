package com.alexstyl.touchcontrol.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.alexstyl.touchcontrol.R;
import com.alexstyl.touchcontrol.ui.BaseActivity;
import com.alexstyl.touchcontrol.ui.dialog.AboutDialog;
import com.alexstyl.touchcontrol.ui.fragment.PhysicalGesturesListFragment;
import com.alexstyl.touchcontrol.ui.fragment.TouchGesturesListFragment;
import com.melnykov.fab.FloatingActionButton;

import de.psdev.licensesdialog.LicensesDialog;


public class MainActivity extends BaseActivity {

    private FloatingActionButton mFab;


    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFab = (FloatingActionButton) findViewById(R.id.add_gesture);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateAGestureActivity.class);
                startActivity(i);
            }
        });

        ViewPager vPager = (ViewPager) findViewById(R.id.central_pager);

        vPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != MainPagerAdapter.POS_GESTURES) {
                    mFab.hide();
                } else {
                    mFab.show();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private class MainPagerAdapter extends FragmentPagerAdapter {


        private static final int PAGE_COUNT = 2;
        private static final int POS_GESTURES = 0;
        private static final int POS_PHYSICAL_GESTURES = 1;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == POS_GESTURES) {
                return new TouchGesturesListFragment();
            } else if (position == POS_PHYSICAL_GESTURES) {
                return new PhysicalGesturesListFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == POS_GESTURES) {
                return getString(R.string.touch_gestures);
            } else if (position == POS_PHYSICAL_GESTURES) {
                return getString(R.string.physical_gestures);
            }
            return "";
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater a = getMenuInflater();
        a.inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_licences:
                showLicencesDialog();
                return true;
            case R.id.action_about:
                showAboutDialog();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        new AboutDialog().show(getSupportFragmentManager(),"alexstyl:about");
    }

    private void showLicencesDialog() {
        new LicensesDialog(this, R.raw.gesture_noticies, false, true).show();
    }
}
