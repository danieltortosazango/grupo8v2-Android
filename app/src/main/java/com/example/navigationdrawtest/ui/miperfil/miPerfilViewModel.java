package com.example.navigationdrawtest.ui.miperfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class miPerfilViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public miPerfilViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}