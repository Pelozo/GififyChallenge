package net.pelozo.gifify.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.pelozo.gifify.networking.giphyApi.GiphyApi
import net.pelozo.gifify.model.GifDto
import net.pelozo.gifify.database.GifDao
import net.pelozo.gifify.networking.giphyApi.paging.SearchPagingSource
import net.pelozo.gifify.networking.giphyApi.paging.TrendsPagingSource
import net.pelozo.gifify.model.Gif

class GifRepository(private val giphyApi: GiphyApi, private val db: GifDao){

    companion object{
        val pagingConfig = PagingConfig(
            pageSize = 50,
            prefetchDistance = 50,
            enablePlaceholders = true
        )
    }

    fun getSearch(query: String): Flow<PagingData<GifDto>>{
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { SearchPagingSource(giphyApi, query) }
        ).flow
    }

    fun getTrends(): Flow<PagingData<GifDto>>{
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { TrendsPagingSource(giphyApi) }
        ).flow
    }


    fun getFavorites(): Flow<List<Gif>>{
        return db.getAll()
    }


    suspend fun saveFavorite(gif: Gif){
        db.addGif(gif)
    }

    suspend fun deleteFavorite(gif: Gif){
        db.delete(gif)
    }
}