package net.pelozo.gifify.model.giphyApi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi{

    @GET("trending")
    suspend fun getTrends(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<GiphyResponse>

    @GET("search")
    suspend fun getBySearch(
        @Query("q") q: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<GiphyResponse>
}