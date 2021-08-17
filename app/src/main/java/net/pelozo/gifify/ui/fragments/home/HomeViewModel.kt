package net.pelozo.gifify.ui.fragments.home

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.pelozo.gifify.repositories.GifRepository
import net.pelozo.gifify.model.Gif

class HomeViewModel(private val gifRepo: GifRepository, private val analytics: FirebaseAnalytics) : ViewModel() {

    sealed class Event {
        object ShowLoading: Event()
        object DismissLoading: Event()
        object ShowMsgAddedToFavs: Event()
        data class OpenShareDialog(val url: String): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun getGifs(query: String? = null): Flow<PagingData<Gif>> {
        return  if(query == null){
            gifRepo.getTrends().cachedIn(viewModelScope)
        }else{
            gifRepo.getSearch(query).cachedIn(viewModelScope)
        }.map {pagingData -> pagingData.map { gifDto -> Gif.fromDto(gifDto)}}
    }


    fun gifClicked(gif: Gif){
        viewModelScope.launch {
            eventChannel.send(Event.OpenShareDialog(gif.urlImageOriginal))
        }
    }

    fun gifLongClicked(gif: Gif){
        viewModelScope.launch {
            gifRepo.saveFavorite(gif)
            sendAnalytic(gif)
            eventChannel.send(Event.ShowMsgAddedToFavs)
        }
    }
    private fun sendAnalytic(gif: Gif){
        val data = Bundle()
        data.putString("id", gif.id)
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, data)
    }

}