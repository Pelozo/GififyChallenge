package net.pelozo.gifify.di

import net.pelozo.gifify.BuildConfig
import net.pelozo.gifify.model.giphyApi.AuthInterceptor
import net.pelozo.gifify.model.giphyApi.GiphyApi
import net.pelozo.gifify.model.repositories.GifRepository
import net.pelozo.gifify.ui.home.HomeViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    factory { AuthInterceptor() }
    single { provideGiphyApi(get()) }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single {GifRepository(get())}
}

val viewModelModule = module {
    viewModel{HomeViewModel(get())}
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val API_URL = "https://api.giphy.com/v1/gifs/" //TODO move this
    return Retrofit.Builder().baseUrl(API_URL).client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create()).build()
}


fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}

fun provideGiphyApi(retrofit: Retrofit): GiphyApi = retrofit.create(GiphyApi::class.java)
