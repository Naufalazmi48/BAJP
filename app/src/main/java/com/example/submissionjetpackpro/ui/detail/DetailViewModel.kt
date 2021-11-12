package com.example.submissionjetpackpro.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.core.data.Resource
import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie
import com.example.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel(private val useCase: MovieUseCase): ViewModel() {
    private val _casters = MutableLiveData<Resource<PagingData<Caster>>>()
    val casters: LiveData<Resource<PagingData<Caster>>> = _casters

    private val _favorite = MutableLiveData<Resource<Boolean>>()
    val favorite: LiveData<Resource<Boolean>> = _favorite

    fun getCasters(tag:String, idMovie: Int) {
        viewModelScope.launch {
            useCase.getCaster(tag, idMovie).collect {
                _casters.postValue(it)
            }
        }
    }

    fun setFavoriteMovie(movie: Movie, categoryMovie: String, listCaster: List<Caster>) {
        viewModelScope.launch {
            useCase.setFavoriteMovie(movie, categoryMovie, listCaster).collect {
                _favorite.postValue(it)
            }
        }
    }

    fun isFavoriteMovie(idMovie: Int){
        viewModelScope.launch {
            useCase.isFavoriteMovie(idMovie).collect {
                _favorite.postValue(it)
            }
        }
    }

}