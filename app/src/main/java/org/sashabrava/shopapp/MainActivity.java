package org.sashabrava.shopapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.sashabrava.shopapp.server.ItemsRequest;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            ItemsRequest itemsRequest = ItemsRequest.getInstance(view.getContext());
            try {
                itemsRequest.templateRequest(this,
                        "api/check-alive",
                        ItemsRequest.class.getMethod("checkServerAlive", String.class),
                        view,
                        MainActivity.class.getMethod("fabGreen", View.class, Object.class),
                        MainActivity.class.getMethod("fabRed", View.class, String.class)
                );
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_item, R.id.nav_single_item)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_settings) {
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(myIntent);
            /*Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "You have clicked on settings", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

     public void fabGreen(View view, Object object) {
        view.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        Snackbar.make(view, "Successfully received a response from Shop Server", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
         ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_item).setEnabled(true);
    }

     public void fabRed(View view, String errorText) {
        view.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        Snackbar.make(view, errorText, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
         ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_item).setEnabled(false);
    }
}