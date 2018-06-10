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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FoodActivityOnline extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Spinner spinner, spinner_qte, spinner_capteur1, spinner_capteur2;
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
        final String login = intent.getStringExtra("login");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView tw = (TextView) header.findViewById(R.id.nav_header);
        tw.setText("Connecté : " + login);
        navigationView.setNavigationItemSelectedListener(this);

        spinner_qte = (Spinner) findViewById(R.id.spinner_qte);
        List<String> list_qte = new ArrayList<String>();
        list_qte.add("grammes");
        list_qte.add("entité(s)");
        list_qte.add("mL");
        ArrayAdapter adapter_qte = new ArrayAdapter(FoodActivityOnline.this, R.layout.spinner_item, list_qte);
        //adapter_qte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_qte.setAdapter(adapter_qte);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner_capteur1 = (Spinner) findViewById(R.id.spinner_capteur1);
        spinner_capteur2 = (Spinner) findViewById(R.id.spinner_capteur2);
        MyAsynTask myAsynTask = new MyAsynTask();
        myAsynTask.execute();

        MyAsynTaskFood myAsynTaskFood = new MyAsynTaskFood();
        myAsynTaskFood.execute(login);

        Button bu_add_gm = (Button) findViewById(R.id.bu_add_gm);
        bu_add_gm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb_byApp = (RadioButton) findViewById(R.id.rb_byApp);
            RadioButton rb_byGMC = (RadioButton) findViewById(R.id.rb_byGMC);
            if(rb_byApp.isChecked()) {
                EditText et_selection_spinner_qte = (EditText) findViewById(R.id.et_selection_spinner_qte);
                String selection_spinner_qte = et_selection_spinner_qte.getText().toString();
                String qte = spinner_qte.getSelectedItem().toString();
                String aliment = spinner.getSelectedItem().toString();
                MyAsynTaskAddFood myAsynTaskAddFood = new MyAsynTaskAddFood();
                myAsynTaskAddFood.execute(aliment, selection_spinner_qte, login);
            }
            else if(rb_byGMC.isChecked()) {
                String aliment1 = spinner_capteur1.getSelectedItem().toString();
                String aliment2 = spinner_capteur2.getSelectedItem().toString();
                MyAsynTaskAddFoodByGardeManger myAsynTaskAddFoodByGardeManger = new MyAsynTaskAddFoodByGardeManger();
                myAsynTaskAddFoodByGardeManger.execute(aliment1, aliment2, login);
            }
        }
    });

}

    public void clicked_food(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        TextView tw_capteur1 = (TextView) findViewById(R.id.tw_capteur1);
        Spinner et_capteur1 = (Spinner) findViewById(R.id.spinner_capteur1);
        TextView tw_capteur2 = (TextView) findViewById(R.id.tw_capteur2);
        Spinner et_capteur2 = (Spinner) findViewById(R.id.spinner_capteur2);
        TextView tw_selection_spinner = (TextView) findViewById(R.id.tw_selection_spinner);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        TextView tw_selection_spinner_qte = (TextView) findViewById(R.id.tw_selection_spinner_qte);
        LinearLayout layout_qte = (LinearLayout) findViewById(R.id.layout_qte);

        switch(view.getId()) {
            case R.id.rb_byApp:
                if(checked) {
                    tw_capteur1.setVisibility(View.INVISIBLE);
                    et_capteur1.setVisibility(View.INVISIBLE);
                    tw_capteur2.setVisibility(View.INVISIBLE);
                    et_capteur2.setVisibility(View.INVISIBLE);
                    tw_selection_spinner.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    tw_selection_spinner_qte.setVisibility(View.VISIBLE);
                    layout_qte.setVisibility(View.VISIBLE);
                };
                break;
            case R.id.rb_byGMC:
                if(checked) {
                    tw_capteur1.setVisibility(View.VISIBLE);
                    et_capteur1.setVisibility(View.VISIBLE);
                    tw_capteur2.setVisibility(View.VISIBLE);
                    et_capteur2.setVisibility(View.VISIBLE);
                    tw_selection_spinner.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                    tw_selection_spinner_qte.setVisibility(View.INVISIBLE);
                    layout_qte.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    /*AsyncTask pour chercher les aliments dans la base*/
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
            List<String> list_capteurs = new ArrayList<String>();

            list_capteurs.add("");
            for(int i=0; i<list.size(); i++) {
                list_capteurs.add(list.get(i));
            }



            ArrayAdapter adapter = new ArrayAdapter(FoodActivityOnline.this, R.layout.spinner_item, list);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            ArrayAdapter adapter_capteurs = new ArrayAdapter(FoodActivityOnline.this, R.layout.spinner_item, list_capteurs);
            //adapter_capteurs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_capteur1.setAdapter(adapter_capteurs);
            spinner_capteur2.setAdapter(adapter_capteurs);
        }
    }

    /*AsyncTask pour chercher le contenu du garde manger de l'utilisateur connecte*/
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
            Drawable drawable = getResources().getDrawable(R.drawable.border_recipe);
            layoutOfDynamicContent.removeAllViewsInLayout();
            layoutOfDynamicContent.setGravity(Gravity.LEFT);
            layoutOfDynamicContent.setBackground(drawable);
            layoutOfDynamicContent.setPadding(50, 50, 50, 50);
            List<String> list;
            if(gardeManger == null) {
                list = new ArrayList<String>();
            }
            else {
                list = gardeManger.getContenu();
            }
            for(int i=0; i<list.size(); i+=2) {
                TextView textView = new TextView(getBaseContext());
                textView.setText(list.get(i) + " (" + list.get(i+1) +")");
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                layoutOfDynamicContent.addView(textView, layoutParam);
            }
        }
    }

    /*Ajout d'un aliment dans le garde manger par l'application sans passer par les capteurs*/
    public class MyAsynTaskAddFood extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... data) {
            String aliment = data[0].replaceAll(" ", "_").trim();;
            String quantite = data[1];
            String login = data[2];
            System.out.println("aliment : " + aliment + ", quantite : " + quantite + ", login : " + login);
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/addAlimentBase/" + URLEncoder.encode(aliment, "UTF-8") + "/" + quantite + "/" + login);
                InputStream inputStream = request.sendRequest(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void data) {
            Intent nextIntent = new Intent(FoodActivityOnline.this,FoodActivityOnline.class);
            Intent intent = getIntent();
            String login = intent.getStringExtra("login");
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
        }
    }

    /*Ajout d'un aliment dans le garde manger par l'application en passant par les capteurs*/

    public class MyAsynTaskAddFoodByGardeManger extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... data) {
            String aliment1 = data[0].replaceAll(" ", "_").trim();
            String aliment2 = data[1].replaceAll(" ", "_").trim();
            String login = data[2];

            System.out.println("[ENVOI GARDE MANGER] aliment 1 : " + aliment1 + ", aliment 2 : " + aliment2 + ", login : " + login);
            try {
                URL url = new URL("http://192.168.137.1:8080/BigCookingData/service/chargerContenuGardeManger/" + login + "/" + URLEncoder.encode(aliment1, "UTF-8") + "/" + URLEncoder.encode(aliment2, "UTF-8"));
                InputStream inputStream = request.sendRequest(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void data) {
            Intent nextIntent = new Intent(FoodActivityOnline.this, FoodActivityOnline.class);
            Intent intent = getIntent();
            String login = intent.getStringExtra("login");
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
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
            Intent nextIntent = new Intent(FoodActivityOnline.this,ParametersActivityOnline.class);
            nextIntent.putExtra("login", login);
            startActivity(nextIntent);
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
        startActivity(nextIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        }
}
