package net.pelozo.gifify.networking.giphyApi.paging

import net.pelozo.gifify.model.GiphyResponse
import net.pelozo.gifify.networking.giphyApi.GiphyApi

class SearchPagingSource(private val service: GiphyApi, private val search: String) : BaseGifPagingSource(){

    override suspend fun getResponse(params: LoadParams<Int>): GiphyResponse {
        val position = params.key ?: initialPosition
        val offset = if (params.key != null) ((position * params.loadSize))  else initialPosition
        return service.getBySearch(
            q = search,
            limit = params.loadSize,
            offset = offset
        ).body()!!
    }



}