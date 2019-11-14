package com.example.navigationdrawtest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NormasActvity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normas_layout);
        this.setTitle(R.string.titulo_normas);
    }
}
