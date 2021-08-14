package net.pelozo.gifify.ui.home.paging

import net.pelozo.gifify.model.giphyApi.GiphyApi
import net.pelozo.gifify.model.giphyApi.GiphyResponse

class TrendsPagingSource(private val service: GiphyApi): BaseGifPagingSource(service) {

    override suspend fun getResponse(params: LoadParams<Int>): GiphyResponse {
        val position = params.key ?: INTTIAL_POSITION
        val offset = if (params.key != null) ((position * params.loadSize))  else INTTIAL_POSITION
        return service.fetchTrends(
            limit = params.loadSize,
            offset = offset
        ).body()!!

    }
}