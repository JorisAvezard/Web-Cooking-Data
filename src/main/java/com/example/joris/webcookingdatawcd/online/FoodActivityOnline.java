package com.example.joris.webcookingdatawcd.online;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.joris.webcookingdatawcd.R;
import com.example.joris.webcookingdatawcd.object.AlimentWS;
import com.example.joris.webcookingdatawcd.object.Data;
import com.example.joris.webcookingdatawcd.object.GardeManger;
import com.example.joris.webcookingdatawcd.offline.MainActivity;
import com.example.joris.webcookingdatawcd.sendRequest.SendRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FoodActivityOnline extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Spinner spinner;
    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_online);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView tw = (TextView) header.findViewById(R.id.nav_header);
        tw.setText("Connecté : " + login);
        navigationView.setNavigationItemSelectedListener(this);

        spinner = (Spinner) findViewById(R.id.spinner);
        MyAsynTask myAsynTask = new MyAsynTask();
        myAsynTask.execute();

        MyAsynTaskFood myAsynTaskFood = new MyAsynTaskFood();
        myAsynTaskFood.execute(login);

}
    public class MyAsynTask extends AsyncTask<Void, Integer, AlimentWS> {

        @Override
        protected AlimentWS doInBackground(Void... data) {
            AlimentWS object = null;
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/alimentBase");
                InputStream inputStream = request.sendRequest(url);

                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    object = gson.fromJson(reader, AlimentWS.class);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(AlimentWS data) {
            List<String> list = data.getAliments();
            ArrayAdapter adapter = new ArrayAdapter(FoodActivityOnline.this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }

    public class MyAsynTaskFood extends AsyncTask<String, Integer, GardeManger> {

        @Override
        protected GardeManger doInBackground(String... data) {
            GardeManger object = null;
            String login = data[0];
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/contenuGardeManger/" + login);
                InputStream inputStream = request.sendRequest(url);

                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    object = gson.fromJson(reader, GardeManger.class);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(GardeManger gardeManger) {
            LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout layoutOfDynamicContent = (LinearLayout) findViewById(R.id.layoutOfDynamicFood);
            layoutOfDynamicContent.removeAllViewsInLayout();
            List<String> list = gardeManger.getContenu();
            for(int i=0; i<list.size(); i++) {
                TextView textView = new TextView(getBaseContext());
                textView.setText(list.get(i));
                layoutOfDynamicContent.addView(textView, layoutParam);
            }
        }
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
        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        if (id == R.id.nav_receipe) {
                Intent nextIntent = new Intent(FoodActivityOnline.this,ResearchActivityOnline.class);
                nextIntent.putExtra("login", login);
                startActivity(nextIntent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_home) {
                Intent nextIntent = new Intent(FoodActivityOnline.this,MainActivityOnline.class);
                nextIntent.putExtra("login", login);
                startActivity(nextIntent);
        } else if (id == R.id.nav_content) {
                Intent nextIntent = new Intent(FoodActivityOnline.this,FoodActivityOnline.class);
                nextIntent.putExtra("login", login);
                startActivity(nextIntent);
        } else if (id == R.id.nav_deconnection) {
                Intent nextIntent = new Intent(FoodActivityOnline.this,MainActivity.class);
                nextIntent.putExtra("login", login);
                startActivity(nextIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        }
}
