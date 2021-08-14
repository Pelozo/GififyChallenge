package net.pelozo.gifify.ui.home.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.pelozo.gifify.model.giphyApi.GiphyApi
import net.pelozo.gifify.model.giphyApi.GiphyResponse
import net.pelozo.gifify.model.giphyApi.model.Gif
import retrofit2.HttpException
import java.io.IOException




abstract class BaseGifPagingSource(
    private val service: GiphyApi
) : PagingSource<Int, Gif>() {

    protected val INTTIAL_POSITION = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Gif> {
        val position = params.key ?: INTTIAL_POSITION
        val offset = if (params.key != null) ((position * params.loadSize))  else INTTIAL_POSITION
        println("Position: $position, Limit: ${params.loadSize}, offset: $offset")

        return try {
            val response = getResponse(params)
            println(response)

            val nextKey =
                if (response.data.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
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

    abstract suspend fun getResponse(params:LoadParams<Int>): GiphyResponse

}