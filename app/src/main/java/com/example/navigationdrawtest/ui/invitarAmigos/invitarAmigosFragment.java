package com.example.navigationdrawtest.ui.invitarAmigos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.navigationdrawtest.R;

public class invitarAmigosFragment extends Fragment {

    private invitarAmigosViewModel invitarAmigosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        invitarAmigosViewModel =
                ViewModelProviders.of(this).get(invitarAmigosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_invitaramigos, container, false);
        final TextView textView = root.findViewById(R.id.text_share);
        invitarAmigosViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
    public void lanzarInvitarAmigos(View view){
        //Intent i = new Intent(this, invitarAmigos.class);
        //startActivity(i);
    }
}