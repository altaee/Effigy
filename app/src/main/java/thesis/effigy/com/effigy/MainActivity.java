package thesis.effigy.com.effigy;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import static thesis.effigy.com.effigy.config.ConfigConstants.PREFS_NAME;
import static thesis.effigy.com.effigy.helpers.SimpleDialogCreator.createInfoDialog;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Tab1Main tab1;
    private Tab2UploadImage tab2;
//    ViewPager viewPager;
//    SimilarImagesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_main);
        layout.addView(textView);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.effigylogo);

        checkPrefs();

        tab1 = new Tab1Main();
        tab2 = new Tab2UploadImage();

//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            // This method will be invoked when a new page becomes selected.
//            @Override
//            public void onPageSelected(int position) {
//                Toast.makeText(MainActivity.this,
//                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
//            }
//
//            // This method will be invoked when the current page is scrolled
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                // Code goes here
//                Log.d("PAGES_ADAPTER", "Scrolling");
//            }
//
//            // Called when the scroll state changes:
//            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                // Code goes here
//                Log.d("PAGES_ADAPTER", "Scrolling changed");
//            }
//        });
    }

    private void checkPrefs() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userName = sharedPref.getString("USER_NAME", "");
        if(userName.isEmpty()){
            startActivity(new Intent (MainActivity.this, LoginActivity.class));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                Intent intent = new Intent();
                intent.setClassName(this, "thesis.effigy.com.effigy.SettingsActivity");
                startActivity(intent);
                return true;
            }
            case R.id.action_info: {
                    DialogFragment dialog = new InformationDialog();
                    dialog.show(getFragmentManager(), "information");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //returns the current tabs
            switch (position) {
                case 0:
                    return tab1;
                case 1:
                    return tab2;
                default:
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "EVALUATE";
                case 1:
                    return "UPLOAD AN IMAGE";
            }
            return null;
        }
    }
}



