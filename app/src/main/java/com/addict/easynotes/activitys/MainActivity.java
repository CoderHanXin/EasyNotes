package com.addict.easynotes.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.addict.easynotes.R;
import com.addict.easynotes.utils.ToastUtils;
import com.addict.easynotes.views.adapters.MyViewPagerAdapter;
import com.astuetz.PagerSlidingTabStrip;


public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabs;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
//        toolbar.setNavigationIcon(R.drawable.ic_action_menu);

        initViewPager();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void initViewPager(){
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewPage);
        mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(mViewPager);
        mTabs.setTextColor(getResources().getColor(R.color.brand_text));
        mTabs.setTextSize(getResources().getDimensionPixelOffset(R.dimen.font_normal));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.action_add:
                    intent = new Intent(MainActivity.this, NewNoteActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_settings:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
            }

            if(!msg.equals("")) {
                ToastUtils.showShort(msg);
            }
            return true;
        }
    };
}
