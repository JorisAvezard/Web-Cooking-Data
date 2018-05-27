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
import android.widget.Button;
import android.widget.EditText;
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

public class InscriptionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();

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
                    myAsyncTask.execute(login, password, confirm);
                } else {
                    startActivity(new Intent(InscriptionActivity.this,PopInscriptionActivity.class));
                }
            }
        });

    }

    public class MyAsynTask extends AsyncTask<String, Integer, Data> {

        @Override
        protected Data doInBackground(String... data) {
            String login = data[0];
            String password = data[1];
            Data object = null;
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/inscription/" + login + "/" + password);
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
