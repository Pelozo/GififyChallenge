package net.pelozo.gifify.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.pelozo.gifify.model.giphyApi.model.Gif
import net.pelozo.gifify.model.repositories.GifRepository

class HomeViewModel(private val gifRepo: GifRepository) : ViewModel() {


    //list of possible events
    sealed class Event {
        object ShowLoading: Event()
        object DismissLoading: Event()
        object EmptyTrends: Event();
        data class ShowGifs(val gifs: List<Gif>): Event()
        data class OpenShareDialog(val url: String): Event()
        data class ShowSnackBar(val text: String): Event()
        object Free: Event()
    }

    private val _uiState : MutableStateFlow<Event> = MutableStateFlow(Event.Free)
    val uiState: StateFlow<Event> = _uiState


    fun loadTrends(limit: Int = 10, offset: Int = 0){
        viewModelScope.launch {
            _uiState.value = Event.ShowLoading
            val newGifs = gifRepo.getTrends(limit, offset)
            _uiState.value = Event.ShowGifs(newGifs)
            _uiState.value = Event.DismissLoading
        }
    }

    fun gifClicked(gif: Gif) {
        _uiState.value = Event.OpenShareDialog(gif.images.original.url)
        _uiState.value = Event.Free
    }

    fun gifLongClicked(gif: Gif){
        _uiState.value = Event.ShowSnackBar("Added to favs")//TODO replace this string
        _uiState.value = Event.Free
    }

    fun searchGif(query: String) {

        viewModelScope.launch {
            _uiState.value = Event.ShowLoading
            val newGifs = gifRepo.getBySearch(query)
            if(newGifs.isEmpty()){
                _uiState.value = Event.EmptyTrends
            }
            _uiState.value = Event.ShowGifs(newGifs)
            _uiState.value = Event.DismissLoading
        }

    }


}