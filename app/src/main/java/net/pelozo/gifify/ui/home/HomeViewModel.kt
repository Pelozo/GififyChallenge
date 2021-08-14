package net.pelozo.gifify.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.pelozo.gifify.model.giphyApi.model.Gif
import net.pelozo.gifify.model.repositories.GifRepository

class HomeViewModel(private val gifRepo: GifRepository) : ViewModel() {

    sealed class Event {
        object ShowLoading: Event()
        object DismissLoading: Event()
        data class OpenShareDialog(val url: String): Event()
        data class ShowSnackBar(val text: String): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun getGifs(query: String? = null): Flow<PagingData<Gif>> {
        return  if(query == null){
            gifRepo.getTrends().cachedIn(viewModelScope)
        }else{
            gifRepo.getSearch(query).cachedIn(viewModelScope)
        }
    }


    fun gifClicked(gif: Gif){
        viewModelScope.launch {
            eventChannel.send(Event.OpenShareDialog(gif.images.original.url))
        }
    }

    fun gifLongClicked(gif: Gif){
        viewModelScope.launch {
            eventChannel.send(Event.ShowSnackBar("Added to favs"))//TODO replace this string
        }
    }

}