package com.example.navigationdrawtest.ui.misTrayectos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class misTrayectosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public misTrayectosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is misTrayectos fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}