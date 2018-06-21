package com.example.joris.webcookingdatawcd.online;

import android.content.Intent;
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
import android.widget.TextView;

import com.example.joris.webcookingdatawcd.R;
import com.example.joris.webcookingdatawcd.object.Data;
import com.example.joris.webcookingdatawcd.offline.ConnectionActivity;
import com.example.joris.webcookingdatawcd.offline.MainActivity;
import com.example.joris.webcookingdatawcd.sendRequest.SendRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public class ParametersActivityOnline extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters_online);

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

        MyAsynTask myAsynTask = new MyAsynTask();
        myAsynTask.execute(login);
    }

    public class MyAsynTask extends AsyncTask<String, Integer, Data> {

        @Override
        protected Data doInBackground(String... data) {
            String login = data[0];
            Data object = null;
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/infosUsers/" + login);
                InputStream inputStream = request.sendRequest(url);
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    object = gson.fromJson(reader, Data.class);
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
        protected void onPostExecute(Data data) {
            TextView tw_genre_valeur = (TextView) findViewById(R.id.tw_genre_valeur);
            TextView tw_age_valeur = (TextView) findViewById(R.id.tw_age_valeur);
            TextView tw_taille_valeur = (TextView) findViewById(R.id.tw_taille_valeur);
            TextView tw_poids_valeur = (TextView) findViewById(R.id.tw_poids_valeur);
            TextView tw_activite_valeur = (TextView) findViewById(R.id.tw_activite_valeur);
            TextView tw_maladie_valeur = (TextView) findViewById(R.id.tw_maladie_valeur);
            TextView tw_regime_valeur = (TextView) findViewById(R.id.tw_regime_valeur);
            TextView tw_allergie_valeur = (TextView) findViewById(R.id.tw_allergie_valeur);
            TextView tw_besoins_valeur = (TextView) findViewById(R.id.tw_besoins_valeur);

            if(!data.getSexe().equals("")) {
                tw_genre_valeur.setText(data.getSexe());
            }
            if(!data.getAge().equals("")) {
                tw_age_valeur.setText(data.getAge());
            }
            if(!data.getTaille().equals("")) {
                String[] bs = data.getTaille().split("\\.");
                String taille = bs[0];
                tw_taille_valeur.setText(taille);
            }
            if(!data.getPoids().equals("")) {
                tw_poids_valeur.setText(data.getPoids());
            }
            if(!data.getActivite().equals("")) {
                tw_activite_valeur.setText(data.getActivite());
            }
            if(!data.getMaladie().equals("")) {
                tw_maladie_valeur.setText(data.getMaladie());
            }
            if(!data.getRegime().equals("")) {
                tw_regime_valeur.setText(data.getRegime());
            }
            if(!data.getAllergie().equals("")) {
                tw_allergie_valeur.setText(data.getAllergie());
            }

            if(!data.getBesoin().equals("")) {
                String[] bs = data.getBesoin().split("\\.");
                String besoin = bs[0];
                tw_besoins_valeur.setText(besoin);
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
            Intent nextIntent = new Intent(ParametersActivityOnline.this,ResearchActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_manage) {
            Intent nextIntent = new Intent(ParametersActivityOnline.this,ParametersActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_home) {
            Intent nextIntent = new Intent(ParametersActivityOnline.this,MainActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_content) {
            Intent nextIntent = new Intent(ParametersActivityOnline.this,FoodActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_deconnection) {
            Intent nextIntent = new Intent(ParametersActivityOnline.this,ConnectionActivity.class);
            startActivity(nextIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
