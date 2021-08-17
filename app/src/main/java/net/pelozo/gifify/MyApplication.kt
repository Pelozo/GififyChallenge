package net.pelozo.gifify

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import net.pelozo.gifify.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule, viewModelModule, retrofitModule, roomModule, firebaseModule)
        }
    }
}