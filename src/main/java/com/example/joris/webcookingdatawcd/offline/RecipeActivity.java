package com.example.joris.webcookingdatawcd.offline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joris.webcookingdatawcd.R;
import com.example.joris.webcookingdatawcd.object.Data;
import com.example.joris.webcookingdatawcd.object.RecetteCuisine;
import com.example.joris.webcookingdatawcd.sendRequest.SendRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class RecipeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String recette = intent.getStringExtra("recette");
        System.out.println("[RECIPE ACTIVITY] recette : " + recette);

        MyAsynTask myAsynTask = new MyAsynTask();
        myAsynTask.execute(recette);
    }

    public class MyAsynTask extends AsyncTask<String, Integer, RecetteCuisine> {

        public String refactorRecipe(String recipe) {
            String newRecipe = recipe.replaceAll(" ", "_");
            System.out.println("[NEW RECIPE] " + newRecipe);
            return newRecipe;
        }

        @Override
        protected RecetteCuisine doInBackground(String... data) {
            String recipe = refactorRecipe(data[0]);
            RecetteCuisine object = new RecetteCuisine();
            System.err.println("DO IN BACKGROUND " + recipe);
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/recetteCuisine/" + URLEncoder.encode(recipe, "UTF-8"));
                InputStream inputStream = request.sendRequest(url);

                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    object = gson.fromJson(reader, RecetteCuisine.class);
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
        protected void onPostExecute(RecetteCuisine recipe) {
            TextView tw_name_receipe = (TextView) findViewById(R.id.tw_name_receipe);
            String name_receipe = recipe.getNom();
            if(!name_receipe.equals("")) {
                tw_name_receipe.setText(name_receipe);
            }
            else {
                tw_name_receipe.setText("Nom de recette non disponible");
            }

            imageView = (ImageView) findViewById(R.id.imageView);
            Picasso.with(getBaseContext()).load("https://image.afcdn.com/recipe/20151023/63919_w420h344c1cx2808cy1872.jpg").fit().into(imageView);

            TextView tw_author = (TextView) findViewById(R.id.tw_author);
            List<String> author = recipe.getAuteur();

            if(author.size()>0) {
                if (!author.get(0).equals("")) {
                    tw_author.setText("Par " + author.get(0));
                } else {
                    tw_author.setText("Auteur non disponible");
                }
            }
            else {
                tw_author.setText("Auteur non disponible");
            }

            TextView tw_note = (TextView) findViewById(R.id.tw_note);
            String note = "";
            if(!note.equals("")) {
                tw_note.setText("Note : " + note + "/5");
            }
            else {
                tw_note.setText("Note non disponible");
            }

            TextView tw_persons = (TextView) findViewById(R.id.tw_persons);
            List<String> persons = recipe.getPersonnes();
            if(persons.size()>0) {
                if (!persons.get(0).equals("")) {
                    tw_persons.setText("Pour " + persons.get(0) + " personnes");
                } else {
                    tw_persons.setText("Nombre de personnes non disponible");
                }
            } else {
                tw_persons.setText("Nombre de personnes non disponible");
            }

            TextView tw_preparation_time = (TextView) findViewById(R.id.tw_preparation_time);
            List<String> preparation_time = recipe.getTempsPreparation();
            if(preparation_time.size()>0) {
                if (!preparation_time.get(0).equals("")) {
                    tw_preparation_time.setText("Temps de préparation : " + preparation_time.get(0));
                } else {
                    tw_preparation_time.setText("Temps de préparation non disponible");
                }
            } else {
                tw_preparation_time.setText("Temps de préparation non disponible");
            }

            TextView tw_baking = (TextView) findViewById(R.id.tw_baking);
            List<String> baking_time = recipe.getTempsCuisson();
            if(baking_time.size()>0) {
                if (!baking_time.get(0).equals("")) {
                    tw_baking.setText("Temps de cuisson : " + baking_time.get(0));
                } else {
                    tw_baking.setText("Temps de cuisson non disponible");
                }
            } else {
                tw_baking.setText("Temps de cuisson non disponible");
            }

            TextView tw_ingredients_list = (TextView) findViewById(R.id.tw_ingredients_list);
            List<String> ingredients_list = recipe.getIngredients();
            if(ingredients_list.size() > 0) {
                tw_ingredients_list.setText("");
                for(int i=0; i<ingredients_list.size(); i++) {
                    String get_ingredients_list = tw_ingredients_list.getText().toString();
                    tw_ingredients_list.setText(get_ingredients_list + ingredients_list.get(i));
                    if(i != ingredients_list.size()-1) {
                        get_ingredients_list = tw_ingredients_list.getText().toString();
                        tw_ingredients_list.setText(get_ingredients_list + "\n\n");
                    }
                }
            }

            TextView tw_preparation_list = (TextView) findViewById(R.id.tw_preparation_list);
            List<String> preparation_list = recipe.getEtapes();
            if(preparation_list.size() > 0) {
                tw_preparation_list.setText("");
                for(int i=0; i<preparation_list.size(); i++) {
                    String get_preparation_list = tw_preparation_list.getText().toString();
                    if(i+1 < preparation_list.size())
                        tw_preparation_list.setText(get_preparation_list + preparation_list.get(i) + "\n\n");
                    else
                        tw_preparation_list.setText(get_preparation_list + preparation_list.get(i));
                }
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
            startActivity(new Intent(RecipeActivity.this,ResearchActivity.class));
        } else if (id == R.id.nav_home) {
            startActivity(new Intent(RecipeActivity.this,MainActivity.class));
        } else if (id == R.id.nav_connection) {
            startActivity(new Intent(RecipeActivity.this,ConnectionActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
