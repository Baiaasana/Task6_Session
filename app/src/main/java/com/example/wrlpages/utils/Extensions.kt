package com.example.wrlpages.utils
import android.content.Context
import android.widget.Toast
import com.example.wrlpages.App

class Extensions {

    fun Toast.toast(context: Context, text: String){
        Toast.makeText(context, text , Toast.LENGTH_SHORT).show()
    }
}