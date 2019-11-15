package com.example.navigationdrawtest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 123;

    private String correo = "";
    private String contraseña = "";
    private ViewGroup contenedor;
    private EditText etCorreo, etContraseña;
    private Button botonResetPassword;
    private static final AtomicInteger count = new AtomicInteger(0);
    private double idAdmin;
    private String emailUsuario;
    double idNueva;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // login();
        /*botonResetPassword = (Button) findViewById(R.id.btnResetPassword);
        botonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, cambiarContraseña.class));
            }


        });*/
        db.collection("Usuarios").document("Admin").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    idAdmin = task.getResult().getDouble("IDdefecto");


                }
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // el usuario está signed in
                    Log.i(TAG, "usuario es != null");
                    estaLogueado(firebaseUser);

                } else {
                    // el usuario está signed out
                    // finalizo cualquier activity previo de sign in
                    finishActivity(RC_SIGN_IN);
                    Log.i(TAG, "usuario es == null y no esta logueado");
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true).build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
                                            new AuthUI.IdpConfig.TwitterBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .setIsSmartLockEnabled(false)
                                    .build()

                            , RC_SIGN_IN);

                }
            }
        };
        /*FirebaseUser usuario = firebaseAuth.getCurrentUser();
        db.collection("Usuarios").document(usuario.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    emailUsuario = task.getResult().getString("Email");


                }
            }
        });*/


    }

    private void estaLogueado(final FirebaseUser usuario) {

        idNueva = idAdmin + 1;
        Map<String, Object> datos = new HashMap<>();
        datos.put("Nombre", usuario.getDisplayName());
        datos.put("Email", usuario.getEmail());
        //datos.put("ID",1);
        datos.put("ID", idNueva);
        db.collection("Usuarios").document(usuario.getEmail()).set(datos);

        Map<String, Object> datosadmin = new HashMap<>();
        datosadmin.put("IDdefecto", idNueva);
        db.collection("Usuarios").document("Admin").set(datosadmin);
        Log.i(TAG, "Estoy en estaLogueado y Usuario =" + usuario);

      /*  usuario.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                       // findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Log.i(TAG, "emailde verificacion enviado"+ usuario.isEmailVerified());
                            Toast.makeText(LoginActivity.this,"Verification email sent to " + usuario.getEmail(),Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this,"Failed to send verification email.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
*/
        if (!usuario.isEmailVerified()) {
            Log.i(TAG, "Estoy en estaLogueado y usuario.isEmailVerified() =  " + usuario.isEmailVerified());
            Toast.makeText(this, "Verifica el correo " + usuario.getEmail()+" para poder usar las funcionalidades de la APP", Toast.LENGTH_LONG).show();
            usuario.sendEmailVerification();
            finish();



        } else {
            //login();
            Log.i(TAG, "Estoy en estaLogueado else y usuario.isEmailVerified() =  " + usuario.isEmailVerified());

            /*Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);*/
            //usuario.reload();

            Intent a = new Intent(this,MainActivity.class);
            startActivity(a);
        }
    }


   /* private void login() {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            //FirebaseUser usuario = firebaseAuth.getCurrentUser();

            //Toast.makeText(this, "inicia sesión: " + usuario.getDisplayName() + " - " + usuario.getEmail() + " - " + usuario.getProviders().get(0), Toast.LENGTH_LONG).show();

            idNueva = idAdmin + 1;
            Map<String, Object> datos = new HashMap<>();
            datos.put("Nombre", usuario.getDisplayName());
            datos.put("Email", usuario.getEmail());
            //datos.put("ID",1);
            datos.put("ID", idNueva);
            db.collection("Usuarios").document(usuario.getEmail()).set(datos);

            Map<String, Object> datosadmin = new HashMap<>();
            datosadmin.put("IDdefecto", idNueva);
            db.collection("Usuarios").document("Admin").set(datosadmin);


            if (!usuario.isEmailVerified()) {
                Toast.makeText(this, "Verifica el correo: " + usuario.getEmail(), Toast.LENGTH_LONG).show();


            } else {
                //login();
                Log.i(TAG, "Estoy verificado");
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }


            finish();

        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true).build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build()
                    //.setIsSmartLockEnabled(false)
                    , RC_SIGN_IN);


        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final FirebaseUser usuario = firebaseAuth.getCurrentUser();

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                estaLogueado(usuario);
            }

        } else if (resultCode == RESULT_CANCELED) {
            Intent a = new Intent(this,MainActivity.class);
            startActivity(a);
              /*  IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Sin conexión a Internet",
                            Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error desconocido",
                            Toast.LENGTH_LONG).show();
                    return;
                }*/
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseAuth.addAuthStateListener(authStateListener);
    }


}