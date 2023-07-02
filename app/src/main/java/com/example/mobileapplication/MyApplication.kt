package com.example.mobileapplication

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: MultiDexApplication() {
    companion object {
        // Firebase 관련 모든 변수 사용
        lateinit var db: FirebaseFirestore
        lateinit var storage: FirebaseStorage

        lateinit var auth: FirebaseAuth
        var email: String? = null
        fun checkAuth(): Boolean {
            var currentuser = auth.currentUser
            return currentuser?.let {
                email = currentuser.email
                if(currentuser.isEmailVerified) true
                else false
            } ?: false
        }

    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth

        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage
    }
}