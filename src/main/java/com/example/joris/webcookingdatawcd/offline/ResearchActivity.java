package com.example.joris.webcookingdatawcd.offline;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.example.joris.webcookingdatawcd.object.RecipeList;
import com.example.joris.webcookingdatawcd.online.MainActivityOnline;
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

public class ResearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();

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

                EditText et_name_research = (EditText) findViewById(R.id.et_name_research);
                String name_research = et_name_research.getText().toString().trim().replaceAll(" ", "_");
                RadioButton rb_byName = (RadioButton) findViewById(R.id.rb_byName);
                RadioButton rb_byType = (RadioButton) findViewById(R.id.rb_byType);
                RadioButton rb_byNote = (RadioButton) findViewById(R.id.rb_byNote);
                RadioButton rb_byDifficulty = (RadioButton) findViewById(R.id.rb_byDifficulty);
                MyAsynTask myAsyncTask = new MyAsynTask();
                String research_by = "";
                if(rb_byName.isChecked() == true) {
                    research_by = "recetteParNom";
                }
                else if(rb_byType.isChecked() == true) {
                    research_by = "recetteParCategorie";
                }
                else if(rb_byNote.isChecked() == true) {
                    research_by = "recetteParNote";
                }
                else if(rb_byDifficulty.isChecked() == true) {
                    research_by = "recetteParDifficulte";
                }
                //myAsyncTask.execute(name_research, research_by);


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
                            TextView tw = (TextView) v;
                            Intent intent = new Intent(ResearchActivity.this,RecipeActivity.class);
                            intent.putExtra("recette", tw.getText());
                            System.out.println("[RESEARCH ACTIVITY] recette : " + tw.getText());
                            startActivity(intent);
                        }
                    });
                    layoutOfDynamicContent.addView(textView, layoutParam);
                }

            }
        });
    }

    public class MyAsynTask extends AsyncTask<String, Integer, RecipeList> {

        @Override
        protected RecipeList doInBackground(String... data) {
            String name_research = data[0];
            String research_by = data[1];
            RecipeList object = new RecipeList();
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/" + research_by + "/" + name_research);
                InputStream inputStream = request.sendRequest(url);
                String result = "";

                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    object = gson.fromJson(reader, RecipeList.class);
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
        protected void onPostExecute(RecipeList recipes) {
            LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout layoutOfDynamicContent = (LinearLayout) findViewById(R.id.layoutOfDynamicContent);
            layoutOfDynamicContent.removeAllViewsInLayout();
            List<String> list = recipes.getRecipes();
            for(int i=0; i<list.size(); i++) {
                final TextView textView = new TextView(getBaseContext());
                textView.setText(list.get(i));
                textView.setTextColor(Color.parseColor("#000096"));
                textView.setClickable(true);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tw = (TextView) v;
                        Intent intent = new Intent(ResearchActivity.this,RecipeActivity.class);
                        intent.putExtra("recette", tw.getText());
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
