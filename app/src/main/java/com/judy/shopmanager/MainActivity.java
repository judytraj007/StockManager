package com.judy.shopmanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    SharedPreferences sp;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;
        mFragmentManager = getSupportFragmentManager();

        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(R.id.containerView,new tabsPageAdapter()).commit();
        View header = mNavigationView.getHeaderView(0);
        sp=getSharedPreferences("myPref",MODE_PRIVATE);
        TextView text = (TextView) header.findViewById(R.id.email);
        text.setText(sp.getString("username",null));


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                if(menuItem.getItemId()==R.id.nav_item_home){

                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new tabsPageAdapter()).commit();

                }
                if(menuItem.getItemId()==R.id.nav_item_item){

                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new AddItemFragment()).commit();

                }

               if(menuItem.getItemId() == R.id.nav_account) {
                   FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                   xfragmentTransaction.replace(R.id.containerView,new AccountSettings()).commit();

                }
                if(menuItem.getItemId() == R.id.nav_item_user) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new User()).commit();

                }
                if(menuItem.getItemId() == R.id.nav_item_report) {
                    FragmentTransaction pfragmentTransaction = mFragmentManager.beginTransaction();
                    pfragmentTransaction.replace(R.id.containerView,new ReportFragment()).commit();

                }
                if(menuItem.getItemId() == R.id.nav_item_brand) {
                    FragmentTransaction yfragmentTransaction = mFragmentManager.beginTransaction();
                    yfragmentTransaction.replace(R.id.containerView,new BrandFragment()).commit();

                }
                return false;
            }

        });


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

}
