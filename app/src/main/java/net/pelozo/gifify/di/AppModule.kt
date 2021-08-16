package net.pelozo.gifify.di

import android.app.Application
import androidx.room.Room
import net.pelozo.gifify.BuildConfig
import net.pelozo.gifify.networking.giphyApi.AuthInterceptor
import net.pelozo.gifify.networking.giphyApi.GiphyApi
import net.pelozo.gifify.repositories.GifRepository
import net.pelozo.gifify.database.GifDao
import net.pelozo.gifify.database.GifDatabase
import net.pelozo.gifify.ui.fragments.favorites.FavoritesViewModel
import net.pelozo.gifify.ui.fragments.home.HomeViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single {GifRepository(get(), get())}
}

val viewModelModule = module {
    viewModel{HomeViewModel(get())}
    viewModel{FavoritesViewModel(get())}
}

val retrofitModule = module {

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create()).build()
    }
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
    }
    fun provideGiphyApi(retrofit: Retrofit): GiphyApi = retrofit.create(GiphyApi::class.java)

    factory { AuthInterceptor() }
    single { provideGiphyApi(get()) }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }

}

val roomModule = module {
    fun provideDatabase(application: Application): GifDatabase {
        return Room.databaseBuilder(application, GifDatabase::class.java, "gifs")
            .build()
    }

    fun provideCountriesDao(database: GifDatabase): GifDao {
        return database.gifDao
    }

    single { provideDatabase(androidApplication()) }
    single { provideCountriesDao(get()) }

}




