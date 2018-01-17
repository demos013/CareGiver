package com.caregiver;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.caregiver.CustomListview.navigationlistview;
import com.google.firebase.auth.FirebaseAuth;

public class Listview_Show extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String[] mDrawerTitle = {"Add Name","Home","Hidden", "My Buddy", "Buddy Requests", "Sign out"};
    private ActionBarDrawerToggle mDrawerToggle;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview__show);
        setupNavigationBar();
    }




    public void setupNavigationBar(){
        //navigationbar
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mListView = (ListView) findViewById(R.id.drawer);
        navigationlistview adapter = new navigationlistview(this,mDrawerTitle);
        mListView.setAdapter(adapter);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {


            }
        });

        //toggle

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Care Giver</font>"));
        mDrawerToggle = new ActionBarDrawerToggle(
                this,   // Context
                mDrawerLayout,  // DrawerLayout
                R.drawable.ic_drawer, // รูปภาพที่จะใช้
                R.string.drawer_open // ค่า String ในไฟล์ strings.xml

        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("open", "onDrawerOpened: ");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.location_map:
                Intent intent = new Intent(Listview_Show.this,Map_Show.class);
                startActivity(intent);
                return true;
            case R.id.filter:
                Log.d("filter", "onOptionsItemSelected: ");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
         menu.getItem(1).setIcon(R.drawable.view_list);
        return true;
    }
}
