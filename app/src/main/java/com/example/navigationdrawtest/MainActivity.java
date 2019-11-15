package com.example.navigationdrawtest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TrayectosActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser usuario;

    ArrayList<Trayectos> listaDocumentos = new ArrayList<>();

    private AppBarConfiguration mAppBarConfiguration;
    private boolean flag;

    private static final long MIN_TIME = 0;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag = isUserLogged();

        usuario = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarUsuarios();
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

    public void iniciarTrayectos(String correo, double idBici, String base) {


        Map<String, Object> datos = new HashMap<>();
        datos.put("IDbici", idBici);
        datos.put("Email", correo);
        datos.put("Base", base);
        datos.put("Movimiento", true);

        db.collection("Trayectos").document().set(datos);

        Toast.makeText(this, "Trayecto empezado", Toast.LENGTH_LONG).show();

    }
    public void comprobarUsuarios() {
        boolean encontrado = false;
        final CollectionReference trayectos = db.collection("Trayectos");
        trayectos.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                String str = document.get("IDbici").toString();
                                double IDbici = Double.valueOf(str).doubleValue();

                                Trayectos trayecto = new Trayectos(usuario.getEmail(),
                                        IDbici,
                                        ((Boolean) document.get("Movimiento")),
                                        document.get("Base").toString());
                                Log.i(TAG, "He introducido el trayecto " + trayecto);
                                listaDocumentos.add(trayecto);
                                Log.i(TAG, "El documento " + document.getId() + " -->" + document.getData());
                            }
                        } else {
                            Log.i(TAG, "Error obteniendo documento " + task.getException());
                        }
                    }
                });
        int posicion = -1;
        for (int i = 0; !encontrado && i < listaDocumentos.size(); i++) {
            if (listaDocumentos.get(i).getEmail().equals(usuario.getEmail())) {
                encontrado = true;
                posicion = i;

            }
        }
        if (encontrado) {
            if (listaDocumentos.get(posicion).isMovimiento()) {
                //movimiento es true
                Toast.makeText(MainActivity.this, "Ya tienes un trayecto activo", Toast.LENGTH_LONG).show();
            } else {
                //Se debe activar cuando se deje la bicicleta
                fab.setEnabled(false);

                iniciarTrayectos(usuario.getEmail(), listaDocumentos.get(posicion).getIdBici(), listaDocumentos.get(posicion).getBase());


            }
        }


    }
    public void lanzarInvitarAmigos(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Te invito a que descargues la APP de bicicletas: https://drive.google.com/file/d/1o7wEyF8kxrmyB43lCD4-0608xYL6rAFU/view?usp=sharing");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void llamarTelefono(MenuItem view) {
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:962849347"));
        startActivity(intent);
    }

}
