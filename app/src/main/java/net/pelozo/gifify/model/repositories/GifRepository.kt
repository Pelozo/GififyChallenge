package net.pelozo.gifify.model.repositories

import net.pelozo.gifify.model.giphyApi.GiphyApi

class GifRepository(private val giphyApi: GiphyApi){
    suspend fun getTrends(limit: Int = 10, offset: Int = 0) = giphyApi.getTrends().body()!!.data

    suspend fun getBySearch(search: String, limit: Int = 10, offset: Int = 0) = giphyApi.getBySearch(search, limit, offset).body()
}