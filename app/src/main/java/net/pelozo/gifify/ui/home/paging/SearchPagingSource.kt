package net.pelozo.gifify.ui.home.paging

import net.pelozo.gifify.model.giphyApi.GiphyApi
import net.pelozo.gifify.model.giphyApi.GiphyResponse

class SearchPagingSource(private val service: GiphyApi, private val search: String) : BaseGifPagingSource(service){

    override suspend fun getResponse(params: LoadParams<Int>): GiphyResponse {
        val position = params.key ?: INTTIAL_POSITION
        val offset = if (params.key != null) ((position * params.loadSize))  else INTTIAL_POSITION
        return service.getBySearch(
            q = search,
            limit = params.loadSize,
            offset = offset
        ).body()!!
    }
}