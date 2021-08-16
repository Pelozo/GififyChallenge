package net.pelozo.gifify.networking.giphyApi.paging

import net.pelozo.gifify.model.GiphyResponse
import net.pelozo.gifify.networking.giphyApi.GiphyApi

class TrendsPagingSource(private val service: GiphyApi): BaseGifPagingSource() {

    override suspend fun getResponse(params: LoadParams<Int>): GiphyResponse {
        val position = params.key ?: initialPosition
        val offset = if (params.key != null) ((position * params.loadSize))  else initialPosition
        val response =  service.fetchTrends(
            limit = params.loadSize,
            offset = offset
        ).body()!!
        return response

    }

}