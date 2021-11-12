package com.example.submissionjetpackpro.ui.main.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.model.Movie
import com.example.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TvViewModel(private val useCase: MovieUseCase) : ViewModel() {
    private val _tv = MutableLiveData<Resource<PagingData<Movie>>>()
    val tv: LiveData<Resource<PagingData<Movie>>> = _tv

    fun getTv(tag: String) {
        viewModelScope.launch {
            useCase.getMovies(tag).collect {
                _tv.postValue(it)
            }
        }
    }

    fun searchTv(tag: String, query: String){
        viewModelScope.launch {
            useCase.searchMovie(tag, query).collect {
                _tv.postValue(it)
            }
        }
    }
}