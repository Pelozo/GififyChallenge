package net.pelozo.gifify.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.pelozo.gifify.model.giphyApi.GiphyResponse
import net.pelozo.gifify.model.giphyApi.model.GifDto
import net.pelozo.gifify.model.repositories.GifRepository

class HomeViewModel(private val gifRepo: GifRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            println(gifRepo.getTrends())
            println(gifRepo.getBySearch("lol", 1))
        }
    }


}