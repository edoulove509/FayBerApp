package activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Models.Services;
import adapters.ServiceArrayAdapter;
import codepath.fayberapp.AproposActivity;
import codepath.fayberapp.MyBrowser;
import codepath.fayberapp.PartenaireActivity;
import codepath.fayberapp.R;
import cz.msebera.android.httpclient.Header;

public class FayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressBar progress;
    TextView tvName, tvPhone;
    private SwipeRefreshLayout swiperefresh;
    // call the adapter,the listview and the model
    // call the adapter,the listview and the model
    ServiceArrayAdapter serviceAdapter;
    ArrayList<Services> aServices;
    ListView lvServices;
    //  Button btnItems, btnItems1, btnItems2, btnItems3;

    JSONArray serviceJsonResults;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    String saveListService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fay);
        //Display the toobar

        setContentView(R.layout.activity_fay);

        //Display the toobar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("PreferencesTAG", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progress = (ProgressBar ) findViewById(R.id.progress);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        // call the listview
        lvServices = (ListView) findViewById(R.id.lvServices);
        // onclick in the listview for seeing the details
        lvServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Services services = (Services) lvServices.getItemAtPosition(position);
                //   Toast.makeText(FayActivity.this, "Pas data to see details of service", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FayActivity.this, DetailsActivity.class);
                i.putExtra("services", services);
                i.putExtra("position", position);
                startActivity(i);
            }
        });

        getListService();

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progress.setVisibility(View.VISIBLE);
                getListService();
                swiperefresh.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swiperefresh.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);

        progress.setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        tvName=(TextView)header.findViewById(R.id.tvName);
        tvPhone=(TextView)header.findViewById(R.id.tvPhone);

        if(sharedPreferences.getString("id_client", null)!=null){
            tvName.setVisibility(View.VISIBLE);
            tvPhone.setVisibility(View.VISIBLE);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            tvName.setText(sharedPreferences.getString("username_client", null));
            tvPhone.setText(sharedPreferences.getString("telephone_client", null));
        }else{
            tvName.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
    }

    private void getListService() {
        aServices = new ArrayList<>();
        serviceAdapter = new ServiceArrayAdapter(FayActivity.this, aServices);
        lvServices.setAdapter(serviceAdapter);

        String url = "http://fayberagency.com/v1/app/liste_service.php";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                serviceJsonResults = null;
                try{
                    serviceJsonResults = response.getJSONArray("response");
                    aServices.addAll(Services.fromJSONArray(serviceJsonResults));
                    serviceAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                    Log.d("DEBUG", aServices.toString());
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }


            public void onFailure(int statusCode, Header[] headers,String responseString,Throwable throwable)
            {
                super.onFailure(statusCode, headers, responseString, throwable);
                getListService();
            }
        });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fay, menu);
        // return true;
        //inflate the button search
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Expand the search view and request focus
        //searchItem.expandActionView();
        // searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                if(Services.searchFromJSONArray(serviceJsonResults,query).size()==1){
                    Toast.makeText(FayActivity.this, "" +query, Toast.LENGTH_SHORT).show();
                    serviceAdapter.clear();
                    serviceAdapter.addAll(Services.searchFromJSONArray(serviceJsonResults,query));
                    serviceAdapter.notifyDataSetChanged();
                    // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                    // see https://code.google.com/p/android/issues/detail?id=24599
                    searchView.clearFocus();
                    //finish();
                }else{
                    Toast.makeText(FayActivity.this, "Service non disponible: " +query, Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    /*
      public void onLogSuccess() {
          Intent i = new Intent(this, DetailsActivity.class);
          startActivity(i);
      }*/
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
        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent i = new Intent(getApplicationContext(), FayActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_localisation)
        {
            Intent i = new Intent(FayActivity.this, MyBrowser.class);
            startActivity(i);
        }
        else if (id == R.id.nav_group) {
            Intent i = new Intent(FayActivity.this, TeamFayBerActivity.class);
            startActivity(i);
            // Toast.makeText(FayActivity.this, "Just click on the list and get back to do other choice", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            editor.clear().apply();
            startActivity(new Intent(getApplicationContext(), FayActivity.class));
            finish();
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_contact) {
            Intent i = new Intent(FayActivity.this, ResponseActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_info) {
            Intent i = new Intent(FayActivity.this, AproposActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_partenaire) {
            Intent i = new Intent(FayActivity.this, PartenaireActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

/*
**Step 1**(Save JSONArray to SharedPreferences

editor.putString("jsonListServ", serviceJsonResults.toString());
                    editor.apply();

**Step 2** (Get JSONArray from SharedPreferences)

saveListService = sharedPreferences.getString("jsonListServ", "0");
        if (saveListService != null) {
            Toast.makeText(this, "it is...", Toast.LENGTH_SHORT).show();
            JSONArray jsonData = null;
            try {
                jsonData = new JSONArray(saveListService);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            aServices.addAll(Services.fromJSONArray(jsonData));
            serviceAdapter.notifyDataSetChanged();
            progress.setVisibility(View.GONE);
        }

 */