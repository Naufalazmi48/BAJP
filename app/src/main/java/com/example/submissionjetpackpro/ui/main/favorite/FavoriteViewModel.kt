package com.example.submissionjetpackpro.ui.main.favorite

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

class FavoriteViewModel(private val useCase: MovieUseCase) : ViewModel() {
    private val _movies = MutableLiveData<Resource<PagingData<Movie>>>()
    val movies: LiveData<Resource<PagingData<Movie>>> = _movies

    fun getMovies(tag:String) {
        viewModelScope.launch {
            useCase.getFavoriteMovies(tag).collect {
                _movies.postValue(it)
            }
        }
    }
}