package com.example.submissionjetpackpro.di

import com.example.core.domain.usecase.MovieInteractor
import com.example.core.domain.usecase.MovieUseCase
import com.example.submissionjetpackpro.ui.detail.DetailViewModel
import com.example.submissionjetpackpro.ui.main.favorite.FavoriteViewModel
import com.example.submissionjetpackpro.ui.main.film.FilmViewModel
import com.example.submissionjetpackpro.ui.main.tv.TvViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}

val viewModelModule = module {
    viewModel { FilmViewModel(get()) }
    viewModel { TvViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}