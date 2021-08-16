package net.pelozo.gifify.networking.giphyApi.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.pelozo.gifify.model.GiphyResponse
import net.pelozo.gifify.model.GifDto
import retrofit2.HttpException
import java.io.IOException




abstract class BaseGifPagingSource(
) : PagingSource<Int, GifDto>() {

    protected val initialPosition = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GifDto> {
        val position = params.key ?: initialPosition
        return try {
            val response = getResponse(params)
            println(response)

            val nextKey =
                if (response.data.isEmpty()) {
                    null
                } else {
                    position + (params.loadSize / params.loadSize)
                }
            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, GifDto>): Int? {
        TODO("Not yet implemented") //return state.anchorPosition
    }

    abstract suspend fun getResponse(params:LoadParams<Int>): GiphyResponse

}