package com.example.joris.webcookingdatawcd.online;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.joris.webcookingdatawcd.R;
import com.example.joris.webcookingdatawcd.offline.MainActivity;

public class ResearchActivityOnline extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_online);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void clicked_NC(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        RadioButton rb_P = (RadioButton) findViewById(R.id.rb_byProtein);
        RadioButton rb_L = (RadioButton) findViewById(R.id.rb_byLipid);
        RadioButton rb_C = (RadioButton) findViewById(R.id.rb_byCarbohydrat);
        switch(view.getId()) {
            case R.id.rb_byName:
                if(checked) checkedIt(rb_P, rb_L, rb_C);
                break;
            case R.id.rb_byType:
                if(checked) checkedIt(rb_P, rb_L, rb_C);
                break;
            case R.id.rb_byNote:
                if(checked) checkedIt(rb_P, rb_L, rb_C);
                break;
            case R.id.rb_byDifficulty:
                if(checked) checkedIt(rb_P, rb_L, rb_C);
                break;
            case R.id.rb_byNutritionalContribution:
                if(checked) {
                    rb_P.setEnabled(true);
                    rb_L.setEnabled(true);
                    rb_C.setEnabled(true);
                    rb_C.setChecked(true);
                    rb_L.setChecked(true);
                    rb_P.setChecked(true);
                }
                break;
            case R.id.rb_byContentFood:
                if(checked) checkedIt(rb_P, rb_L, rb_C);
                break;
        }
    }

    public void checkedIt(RadioButton rb_P, RadioButton rb_L, RadioButton rb_C) {
        if(rb_P.isChecked()) rb_P.setChecked(false);
        if(rb_L.isChecked()) rb_L.setChecked(false);
        if(rb_C.isChecked()) rb_C.setChecked(false);
        rb_P.setEnabled(false);
        rb_L.setEnabled(false);
        rb_C.setEnabled(false);
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

    /*    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
    */
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_receipe) {
            startActivity(new Intent(ResearchActivityOnline.this,ResearchActivityOnline.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_home) {
            startActivity(new Intent(ResearchActivityOnline.this,MainActivityOnline.class));
        } else if (id == R.id.nav_content) {
            startActivity(new Intent(ResearchActivityOnline.this,FoodActivityOnline.class));
        } else if (id == R.id.nav_deconnection) {
            startActivity(new Intent(ResearchActivityOnline.this,MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
