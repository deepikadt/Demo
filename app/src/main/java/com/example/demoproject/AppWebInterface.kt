package com.example.demoproject

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast

class AppWebInterface(private val ctx: Context){

    @JavascriptInterface
    fun postMessage(msg: String){
        Log.d("POST_MESSAGE",msg)
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun showToastInNative(toast: String){
       Toast.makeText(ctx,toast, Toast.LENGTH_SHORT).show()
    }
}