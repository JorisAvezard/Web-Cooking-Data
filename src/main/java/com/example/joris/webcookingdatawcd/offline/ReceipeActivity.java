package com.example.joris.webcookingdatawcd.offline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.joris.webcookingdatawcd.R;

public class ReceipeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        JSONArray ingredients = ingredients();
        JSONArray etapes = etapes();
        JSONObject recette = recettes(ingredients, etapes);

        TextView tw_name_receipe = (TextView) findViewById(R.id.tw_name_receipe);
        String name_receipe = "";
        try {
            name_receipe = recette.getString("nom");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!name_receipe.equals("")) {
            tw_name_receipe.setText(name_receipe);
        }

        TextView tw_author = (TextView) findViewById(R.id.tw_author);
        String author = "";
        try {
            author = recette.getString("auteur");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!author.equals("")) {
            tw_author.setText("Par " + author);
        }

        TextView tw_note = (TextView) findViewById(R.id.tw_note);
        String note = "";
        try {
            note = recette.getString("note");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!note.equals("")) {
            tw_note.setText("Note : " + note + "/5");
        }

        TextView tw_persons = (TextView) findViewById(R.id.tw_persons);
        String persons = "";
        try {
            persons = recette.getString("personnes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!persons.equals("")) {
            tw_persons.setText("Pour " + persons + " personnes");
        }

        TextView tw_preparation_time = (TextView) findViewById(R.id.tw_preparation_time);
        String preparation_time = "";
        try {
            preparation_time = recette.getString("preparation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!preparation_time.equals("")) {
            tw_preparation_time.setText("Temps de préparation : " + preparation_time);
        }

        TextView tw_baking = (TextView) findViewById(R.id.tw_baking);
        String baking_time = "";
        try {
            baking_time = recette.getString("cuisson");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!baking_time.equals("")) {
            tw_baking.setText("Temps de cuisson : " + baking_time);
        }

        TextView tw_ingredients_list = (TextView) findViewById(R.id.tw_ingredients_list);
        JSONArray ingredients_list = new JSONArray();
        try {
            ingredients_list = (JSONArray) recette.get("ingredients");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(ingredients_list.length() > 0) {
                tw_ingredients_list.setText("");
                for(int i=0; i<ingredients_list.length(); i++) {
                    String get_ingredients_list = tw_ingredients_list.getText().toString();
                    tw_ingredients_list.setText(get_ingredients_list + ingredients_list.get(i));
                    if(i != ingredients_list.length()-1) {
                        get_ingredients_list = tw_ingredients_list.getText().toString();
                        tw_ingredients_list.setText(get_ingredients_list + "\n");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tw_preparation_list = (TextView) findViewById(R.id.tw_preparation_list);
        JSONArray preparation_list = new JSONArray();
        int etape = 1;
        try {
            preparation_list = (JSONArray) recette.get("etapes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(preparation_list.length() > 0) {
                tw_preparation_list.setText("");
                for(int i=0; i<preparation_list.length(); i++) {
                    String get_preparation_list = tw_preparation_list.getText().toString();
                    tw_preparation_list.setText(get_preparation_list + etape + ") " + preparation_list.get(i));
                    etape++;
                    if(etape != preparation_list.length()) {
                        get_preparation_list = tw_preparation_list.getText().toString();
                        tw_preparation_list.setText(get_preparation_list + "\n");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray ingredients() {
        JSONArray ingredients = new JSONArray();
        ingredients.put("200 g de farine");
        ingredients.put("2 oeufs");
        ingredients.put("250 g de ricotta");
        ingredients.put("40 g de parmesan râpé");
        ingredients.put("8 tomates séchées");
        ingredients.put("3 tranches de jambon de parme ou prosciutto");
        ingredients.put("8 feuilles de basilic frais");
        return ingredients;
    }

    public JSONArray etapes() {
        JSONArray etapes = new JSONArray();
        etapes.put("Préparer la pâte en pétrissant bien la farine et les oeufs, laisser reposer au frais pendant 1/2 heure.");
        etapes.put("Couper les tomates et le jambon en petits morceaux.");
        etapes.put("Hacher le basilic.");
        etapes.put("Mélanger tous les éléments de la farce. Il est inutile de saler, les tomates, le jambon et le parmesan s'en chargent.");
        etapes.put("Etaler la pâte très finement, ne pas hésiter à fariner souvent pour éviter qu'elle ne colle. Avec un laminoir, c'est plus facile ....");
        etapes.put("Au final, il faut avoir 2 rectangles de pâte identiques. Déposer des petits tas de farce régulièrement disposés sur un des 2 rectangles. Plus on est patient, plus les tas sont petits....");
        etapes.put("Badigeonner d'eau tout autour de chaque tas et poser dessus le second rectangle. Bien souder chaque ravioli en prenant soin de chasser l'air. Cela évitera l'explosion à la cuisson.");
        etapes.put("Avec une roulette à pâtisserie, découper chaque ravioli. Ne pas les entasser (ou alors fariner abondamment) car ils vont vouloir coller entre eux.");
        etapes.put("Faire cuire à l'eau bouillante salée (ou mieux dans du bouillon) pendant 3 mn.");
        return etapes;
    }

    public JSONObject recettes(JSONArray ingredients, JSONArray etapes) {
        JSONObject recette = new JSONObject();
        try {
            recette.put("nom","Ravioli aux tomates séchées, ricotta et jambon italien");
            recette.put("auteur", "marmiton");
            recette.put("personnes", "4");
            recette.put("note", "5");
            recette.put("cuisson", "3 minutes");
            recette.put("preparation", "1 heure");
            recette.put("ingredients", ingredients);
            recette.put("etapes", etapes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recette;
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
            startActivity(new Intent(ReceipeActivity.this,ResearchActivity.class));
        } else if (id == R.id.nav_home) {
            startActivity(new Intent(ReceipeActivity.this,MainActivity.class));
        } else if (id == R.id.nav_connection) {
            startActivity(new Intent(ReceipeActivity.this,ConnectionActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
