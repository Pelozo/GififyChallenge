package net.pelozo.gifify.model.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.pelozo.gifify.model.giphyApi.GiphyApi
import net.pelozo.gifify.model.giphyApi.model.Gif
import net.pelozo.gifify.ui.home.paging.SearchPagingSource
import net.pelozo.gifify.ui.home.paging.TrendsPagingSource

class GifRepository(private val giphyApi: GiphyApi){

    fun getSearch(query: String): Flow<PagingData<Gif>>{
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {SearchPagingSource(giphyApi, query)}
        ).flow
    }

    fun getTrends(): Flow<PagingData<Gif>>{
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { TrendsPagingSource(giphyApi) }
        ).flow

    }


    private val pagingConfig = PagingConfig(
        initialLoadSize = 15,
        pageSize = 15,
        prefetchDistance = 50,
        enablePlaceholders = true

    )



}