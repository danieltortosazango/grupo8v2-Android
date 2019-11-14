package com.example.navigationdrawtest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private boolean flag;
    private FirebaseUser usuario;
    private static final long MIN_TIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag = isUserLogged();

        usuario = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        if (flag) {
            TextView name = hView.findViewById(R.id.headerName);
            name.setText(usuario.getDisplayName());
            TextView email = hView.findViewById(R.id.headerEmail);
            email.setText(usuario.getEmail());
        } else {
            TextView name = hView.findViewById(R.id.headerName);
            name.setText("Anónimo");
            TextView email = hView.findViewById(R.id.headerEmail);
            email.setText("");
        }


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

        } else {
            iniciarLocalizacion();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item1 = menu.findItem(R.id.menu_login);
        MenuItem item2 = menu.findItem(R.id.menu_usuario1);
        if (flag) {
            item1.setVisible(false);
            item2.setVisible(false);
        } else {
            item2.setVisible(false);
            item1.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_login:
                lanzarLogin(null);
                return true;
            case R.id.action_settings:
                lanzarNormas(null);
                return true;
            case R.id.boton_mapa:
                lanzarMapa(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void lanzarMapa(View view) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static boolean isUserLogged() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return true;
        }
        return false;
    }

    public void lanzarLogin(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void lanzarNormas(View view) {
        Intent i = new Intent(this, NormasActvity.class);
        startActivity(i);
    }
    public void iniciarLocalizacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Localizacion local = new Localizacion();


        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 0, local);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, 0, local);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]grantResults) {
        if(requestCode == 1000) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarLocalizacion();
                return;
            }
        }
    }
}
