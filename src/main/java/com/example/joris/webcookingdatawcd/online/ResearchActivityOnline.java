package com.example.joris.webcookingdatawcd.online;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.joris.webcookingdatawcd.R;
import com.example.joris.webcookingdatawcd.object.ListeRecette;
import com.example.joris.webcookingdatawcd.offline.ConnectionActivity;
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
import java.util.List;

public class ResearchActivityOnline extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();

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

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView tw = (TextView) header.findViewById(R.id.nav_header);
        tw.setText("Connecté : " + login);
        navigationView.setNavigationItemSelectedListener(this);

        Button bu_research = (Button) findViewById(R.id.bu_research);
        bu_research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ResearchActivity.this,ReceipeActivity.class));

                EditText et_name_research = (EditText) findViewById(R.id.et_name_research);
                String name_research = et_name_research.getText().toString().trim().replaceAll(" ", "_");
                RadioButton rb_byName = (RadioButton) findViewById(R.id.rb_byName);
                RadioButton rb_byType = (RadioButton) findViewById(R.id.rb_byType);
                RadioButton rb_byNote = (RadioButton) findViewById(R.id.rb_byNote);
                RadioButton rb_byDifficulty = (RadioButton) findViewById(R.id.rb_byDifficulty);
                RadioButton rb_byContentFood = (RadioButton) findViewById(R.id.rb_byContentFood);
                MyAsynTask myAsyncTask = new MyAsynTask();
                String research_by = "";
                Intent intent = getIntent();
                String login = intent.getStringExtra("login");
                if (rb_byName.isChecked() == true) {
                    research_by = "recetteParNom/" + name_research;
                } else if (rb_byType.isChecked() == true) {
                    research_by = "recetteParCategorie/" + name_research;
                } else if (rb_byNote.isChecked() == true) {
                    research_by = "recetteParNote/" + name_research;
                } else if (rb_byDifficulty.isChecked() == true) {
                    research_by = "recetteParDifficulte/" + name_research;
                } else if (rb_byContentFood.isChecked() == true) {
                    research_by = "recetteParContenu/" + login;
                }
                myAsyncTask.execute(research_by);
            }
        });
    }


    public class MyAsynTask extends AsyncTask<String, Integer, ListeRecette> {

        @Override
        protected ListeRecette doInBackground(String... data) {
            String research_by = data[0];
            ListeRecette object = new ListeRecette();
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/" + research_by);
                InputStream inputStream = request.sendRequest(url);
                String result = "";

                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    object = gson.fromJson(reader, ListeRecette.class);
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
        protected void onPostExecute(ListeRecette recipes) {
            LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout layoutOfDynamicContent = (LinearLayout) findViewById(R.id.layoutOfDynamicContent);
            layoutOfDynamicContent.removeAllViewsInLayout();
            List<String> list = recipes.getRecettes();
            for (int i = 0; i < list.size(); i++) {
                final TextView textView = new TextView(getBaseContext());
                Drawable drawable = getResources().getDrawable(R.drawable.border_recipe);
                textView.setText(list.get(i));
                textView.setTextSize(18);
                textView.setTextColor(Color.parseColor("#ffffff"));
                textView.setClickable(true);
                textView.setPadding(30, 30, 30, 30);
                textView.setBackground(drawable);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String login = getIntent().getStringExtra("login");
                        TextView tw = (TextView) v;
                        Intent intent = new Intent(ResearchActivityOnline.this, RecipeActivityOnline.class);
                        intent.putExtra("recette", tw.getText());
                        intent.putExtra("login", login);
                        startActivity(intent);
                    }
                });
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
            Intent nextIntent = new Intent(ResearchActivityOnline.this,ResearchActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_manage) {
            Intent nextIntent = new Intent(ResearchActivityOnline.this,ParametersActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_home) {
            Intent nextIntent = new Intent(ResearchActivityOnline.this,MainActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_content) {
            Intent nextIntent = new Intent(ResearchActivityOnline.this,FoodActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_deconnection) {
            Intent nextIntent = new Intent(ResearchActivityOnline.this,ConnectionActivity.class);
            startActivity(nextIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
