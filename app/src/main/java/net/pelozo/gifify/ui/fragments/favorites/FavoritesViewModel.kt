package net.pelozo.gifify.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.pelozo.gifify.repositories.GifRepository
import net.pelozo.gifify.model.Gif

class FavoritesViewModel(private val gifRepo: GifRepository) : ViewModel() {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    sealed class Event {
        data class OpenShareDialog(val url: String): Event()
        data class OpenDeleteDialog(val gif: Gif): Event()
    }

    fun getGifs(): Flow<List<Gif>> {
        return gifRepo.getFavorites()
    }

    fun gifClicked(gif: Gif) {
        viewModelScope.launch {
            eventChannel.send(Event.OpenShareDialog(gif.urlImageOriginal))
        }
    }

    fun gifLongClicked(gif: Gif) {
        viewModelScope.launch {
            eventChannel.send(Event.OpenDeleteDialog(gif))
        }
    }

    fun deleteGif(gif: Gif){
        viewModelScope.launch {
            gifRepo.deleteFavorite(gif)
        }
    }


}