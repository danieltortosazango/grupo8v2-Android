package com.example.navigationdrawtest.ui.miperfil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navigationdrawtest.MainActivity;
import com.example.navigationdrawtest.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MiPerfilFragment extends Fragment {

    private miPerfilViewModel miPerfilViewModel;
    public Button bCambiarCorreo;
    public View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        miPerfilViewModel =
                ViewModelProviders.of(this).get(miPerfilViewModel.class);
        root = inflater.inflate(R.layout.fragment_usuario, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        updateInfo(root);
        /*miPerfilViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        Button cerrarSesion = (Button) root.findViewById(R.id.btn_cerrar_sesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    }
                });

            }
        });
        bCambiarCorreo = root.findViewById(R.id.btn_cambiar_correo);
        bCambiarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!= null){
                    final EditText entrada = new EditText(view.getContext());
                    entrada.setText("");
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Cambiar Correo electrónico")
                            .setMessage("Introduce tu nuevo Correo electrónico")
                            .setView(entrada)
                            .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String correo = entrada.getText().toString();
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    user.updateEmail(correo)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User email address updated.");
                                                        updateInfo(root);

                                                    }
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                }

            }
        });

        return root;
    }

    public void updateInfo(View root) {
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if(usuario != null){
            TextView nombre = (TextView) root.findViewById(R.id.nombre);
            nombre.setText(usuario.getDisplayName());
            TextView email = (TextView) root.findViewById(R.id.email);
            email.setText(usuario.getEmail());
        }

    }

}