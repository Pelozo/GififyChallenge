package net.pelozo.gifify.networking.giphyApi

import net.pelozo.gifify.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * intercepts request to add api key *
 */
class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val url = req.url().newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY).build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}