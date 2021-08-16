package net.pelozo.gifify

import android.app.Application
import net.pelozo.gifify.di.appModule
import net.pelozo.gifify.di.retrofitModule
import net.pelozo.gifify.di.roomModule
import net.pelozo.gifify.di.viewModelModule
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
            modules(appModule, viewModelModule, retrofitModule, roomModule)
        }
    }
}