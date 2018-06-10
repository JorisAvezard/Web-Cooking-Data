package com.example.joris.webcookingdatawcd.offline;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.joris.webcookingdatawcd.object.Data;
import com.example.joris.webcookingdatawcd.R;
import com.example.joris.webcookingdatawcd.online.MainActivityOnline;
import com.example.joris.webcookingdatawcd.sendRequest.SendRequest;
import com.example.joris.webcookingdatawcd.window.PopConnectionActivity;
import com.example.joris.webcookingdatawcd.window.PopInscriptionActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class InscriptionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();
    Spinner spinner_genre, spinner_age, spinner_taille, spinner_poids, spinner_activite, spinner_maladie, spinner_regime, spinner_allergie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        List<String> list_genre = new ArrayList<String>();
        list_genre.add("Femme");
        list_genre.add("Homme");
        ArrayAdapter adapter_genre = new ArrayAdapter(InscriptionActivity.this, R.layout.spinner_item, list_genre);
        spinner_genre = (Spinner) findViewById(R.id.spinner_genre);
        spinner_genre.setAdapter(adapter_genre);

        List<String> list_age = new ArrayList<String>();
        for(int i=18; i<100; i++) {
            list_age.add(i+"");
        }
        ArrayAdapter adapter_age = new ArrayAdapter(InscriptionActivity.this, R.layout.spinner_item, list_age);
        spinner_age = (Spinner) findViewById(R.id.spinner_age);
        spinner_age.setAdapter(adapter_age);

        List<String> list_taille = new ArrayList<String>();
        for(int j=140; j<211; j++) {
            list_taille.add(j+"");
        }
        ArrayAdapter adapter_taille = new ArrayAdapter(InscriptionActivity.this, R.layout.spinner_item, list_taille);
        spinner_taille = (Spinner) findViewById(R.id.spinner_taille);
        spinner_taille.setAdapter(adapter_taille);

        List<String> list_poids = new ArrayList<String>();
        for(int k=40; k<200; k++) {
            list_poids.add(k+"");
        }
        ArrayAdapter adapter_poids = new ArrayAdapter(InscriptionActivity.this, R.layout.spinner_item, list_poids);
        spinner_poids = (Spinner) findViewById(R.id.spinner_poids);
        spinner_poids.setAdapter(adapter_poids);

        spinner_activite = (Spinner) findViewById(R.id.spinner_activite);
        spinner_maladie = (Spinner) findViewById(R.id.spinner_maladie);
        spinner_regime = (Spinner) findViewById(R.id.spinner_regime);
        spinner_allergie = (Spinner) findViewById(R.id.spinner_allergie);

        MyAsynTaskInfosUsers myAsynTaskInfosUsers = new MyAsynTaskInfosUsers();
        myAsynTaskInfosUsers.execute();

        Button bu_inscription = (Button) findViewById(R.id.bu_inscription);
        bu_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ConnectionActivity.this,MainActivityOnline.class));
                EditText et_login_inscription = (EditText) findViewById(R.id.et_login_inscription);
                String login = et_login_inscription.getText().toString();
                EditText et_password_inscription = (EditText) findViewById(R.id.et_password_inscription);
                String password = et_password_inscription.getText().toString();
                EditText et_confirm_inscription = (EditText) findViewById(R.id.et_confirm_inscription);
                String confirm = et_confirm_inscription.getText().toString();
                MyAsynTask myAsyncTask = new MyAsynTask();
                if(login.equals("")) {
                    startActivity(new Intent(InscriptionActivity.this,PopInscriptionActivity.class));
                } else if (password.equals("")) {
                    startActivity(new Intent(InscriptionActivity.this,PopInscriptionActivity.class));
                }
                else if(confirm.equals(password)) {
                    String genre = spinner_genre.getSelectedItem().toString();
                    String age = spinner_age.getSelectedItem().toString();
                    String taille = spinner_taille.getSelectedItem().toString();
                    String poids = spinner_poids.getSelectedItem().toString();
                    String activite = spinner_activite.getSelectedItem().toString();
                    String maladie = spinner_maladie.getSelectedItem().toString();
                    String regime = spinner_regime.getSelectedItem().toString();
                    String allergie = spinner_allergie.getSelectedItem().toString();
                    myAsyncTask.execute(login, password, genre, age, taille, poids, activite, maladie, regime, allergie);
                } else {
                    startActivity(new Intent(InscriptionActivity.this,PopInscriptionActivity.class));
                }
            }
        });

    }

    /*AsyncTask qui lance l'inscription*/
    public class MyAsynTask extends AsyncTask<String, Integer, Data> {

        @Override
        protected Data doInBackground(String... data) {
            String login = data[0];
            String password = data[1];
            String genre = data[2];
            String age = data[3];
            String taille = data[4];
            String poids = data[5];
            String activite = data[6];
            String maladie = data[7];
            String regime = data[8];
            String allergie = data[9];
            Data object = null;
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/inscription/" + login + "/" + password
                        + "/" + URLEncoder.encode(genre, "UTF-8") + "/" + URLEncoder.encode(age, "UTF-8")
                        + "/" + URLEncoder.encode(taille, "UTF-8") + "/" + URLEncoder.encode(poids, "UTF-8")
                        + "/" + URLEncoder.encode(activite, "UTF-8") + "/" + URLEncoder.encode(maladie, "UTF-8")
                        + "/" + URLEncoder.encode(regime, "UTF-8") + "/" + URLEncoder.encode(allergie, "UTF-8"));
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
            if(data != null) {
                if (data.getData().equals("yes")) {
                    startActivity(new Intent(InscriptionActivity.this, ConnectionActivity.class));
                } else if (data.getData().equals("login pris")) {
                    startActivity(new Intent(InscriptionActivity.this, PopInscriptionActivity.class));
                } else if (data.getData().equals("no")) {
                    startActivity(new Intent(InscriptionActivity.this, PopInscriptionActivity.class));
                }
            } else {
                startActivity(new Intent(InscriptionActivity.this, PopInscriptionActivity.class));
            }
        }
    }

    public class MyAsynTaskInfosUsers extends AsyncTask<Void, Integer, Data> {

        @Override
        protected Data doInBackground(Void... data) {
            Data object = null;
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/getInfosUsers");
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
            List<String> activites = new ArrayList<String>();
            for(int i=0; i<data.getActivites().size(); i++) {
                activites.add(data.getActivites().get(i));
            }
            ArrayAdapter adapter_activite = new ArrayAdapter(InscriptionActivity.this, R.layout.spinner_item, activites);
            spinner_activite.setAdapter(adapter_activite);

            List<String> maladies = new ArrayList<String>();
            maladies.add("Aucun");
            for(int i=0; i<data.getMaladies().size(); i++) {
                maladies.add(data.getMaladies().get(i));
            }
            ArrayAdapter adapter_maladie = new ArrayAdapter(InscriptionActivity.this, R.layout.spinner_item, maladies);
            spinner_maladie.setAdapter(adapter_maladie);

            List<String> regimes = new ArrayList<String>();
            for(int i=0; i<data.getRegimes().size(); i++) {
                regimes.add(data.getRegimes().get(i));
            }
            ArrayAdapter adapter_regime = new ArrayAdapter(InscriptionActivity.this, R.layout.spinner_item, regimes);
            spinner_regime.setAdapter(adapter_regime);

            List<String> allergies = new ArrayList<String>();
            allergies.add("Aucune");
            for(int j=0; j<data.getAllergies().size(); j++) {
                allergies.add(data.getAllergies().get(j));
            }
            ArrayAdapter adapter_allergie = new ArrayAdapter(InscriptionActivity.this, R.layout.spinner_item, allergies);
            spinner_allergie.setAdapter(adapter_allergie);
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
        if (id == R.id.nav_receipe) {
            startActivity(new Intent(InscriptionActivity.this,ResearchActivity.class));
        } else if (id == R.id.nav_home) {
            startActivity(new Intent(InscriptionActivity.this,MainActivity.class));
        } else if (id == R.id.nav_connection) {
            startActivity(new Intent(InscriptionActivity.this,ConnectionActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
