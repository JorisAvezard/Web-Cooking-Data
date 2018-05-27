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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.joris.webcookingdatawcd.R;
import com.example.joris.webcookingdatawcd.object.RecipeList;
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
                RadioButton rb_byNutritionalContribution = (RadioButton) findViewById(R.id.rb_byNutritionalContribution);
                RadioButton rb_byProtein = (RadioButton) findViewById(R.id.rb_byProtein);
                RadioButton rb_byLipid = (RadioButton) findViewById(R.id.rb_byLipid);
                RadioButton rb_byCarbohydrat = (RadioButton) findViewById(R.id.rb_byCarbohydrat);
                MyAsynTask myAsyncTask = new MyAsynTask();
                String research_by = "";
                if (rb_byName.isChecked() == true) {
                    research_by = "recetteParNom";
                } else if (rb_byType.isChecked() == true) {
                    research_by = "recetteParCategorie";
                } else if (rb_byNote.isChecked() == true) {
                    research_by = "recetteParNote";
                } else if (rb_byDifficulty.isChecked() == true) {
                    research_by = "recetteParDifficulte";
                } else if (rb_byNutritionalContribution.isChecked() == true) {
                    if (rb_byProtein.isChecked() == true) {
                        research_by = "";
                    } else if (rb_byLipid.isChecked() == true) {
                        research_by = "";
                    } else if (rb_byCarbohydrat.isChecked() == true) {
                        research_by = "";
                    }
                }
                myAsyncTask.execute(name_research, research_by);
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
                        TextView textView = new TextView(getBaseContext());
                        textView.setText(list.get(i));
                        textView.setTextColor(Color.parseColor("#000096"));
                        textView.setClickable(true);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //startActivity(new Intent(ResearchActivity.this,ReceipeActivity.class));
                            }
                        });
                        layoutOfDynamicContent.addView(textView, layoutParam);
                    }
                }
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
        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        if (id == R.id.nav_receipe) {
            Intent nextIntent = new Intent(ResearchActivityOnline.this,ResearchActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_home) {
            Intent nextIntent = new Intent(ResearchActivityOnline.this,MainActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_content) {
            Intent nextIntent = new Intent(ResearchActivityOnline.this,FoodActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        } else if (id == R.id.nav_deconnection) {
            Intent nextIntent = new Intent(ResearchActivityOnline.this,MainActivity.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
