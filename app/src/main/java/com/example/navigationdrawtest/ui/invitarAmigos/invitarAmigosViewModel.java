package com.example.navigationdrawtest.ui.invitarAmigos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class invitarAmigosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public invitarAmigosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ¡Invita a tus amigos y consigue 1€ cuando completen su primer trayecto!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}