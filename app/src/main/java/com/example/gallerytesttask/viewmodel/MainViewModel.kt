package com.example.gallerytesttask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() :ViewModel(){
    private val checkPermission = MutableLiveData<Boolean>(false)
    fun isCheckPermission(isGranted: Boolean){
        Log.d("ViewModel","ViewModel")
        checkPermission.postValue(isGranted)
    }

    fun getCheckPermission():LiveData<Boolean>{
        return checkPermission
    }
}