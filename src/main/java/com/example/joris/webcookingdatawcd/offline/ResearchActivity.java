package com.example.joris.webcookingdatawcd.offline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joris.webcookingdatawcd.R;
import com.example.joris.webcookingdatawcd.online.MainActivityOnline;

import java.util.ArrayList;
import java.util.List;

public class ResearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button bu_research = (Button) findViewById(R.id.bu_research);
        bu_research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ResearchActivity.this,ReceipeActivity.class));
                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout layoutOfDynamicContent = (LinearLayout) findViewById(R.id.layoutOfDynamicContent);
                layoutOfDynamicContent.removeAllViewsInLayout();
                List<String> list = new ArrayList<String>();
                list.add("Ravioli aux tomates séchées, ricotta et jambon italien");
                list.add("Ravioli aux crevettes");
                for (int i = 0; i < 2; i++) {
                    TextView textView = new TextView(getBaseContext());
                    textView.setText(list.get(i));
                    textView.setTextColor(Color.parseColor("#000096"));
                    textView.setClickable(true);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ResearchActivity.this,ReceipeActivity.class));
                        }
                    });
                    layoutOfDynamicContent.addView(textView, layoutParam);
                    System.out.println("ETAPE " + i + " : Ajout de " + list.get(i));
                }
            }
        });
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
            startActivity(new Intent(ResearchActivity.this,ResearchActivity.class));
        } else if (id == R.id.nav_home) {
            startActivity(new Intent(ResearchActivity.this,MainActivity.class));
        } else if (id == R.id.nav_connection) {
            startActivity(new Intent(ResearchActivity.this,ConnectionActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
