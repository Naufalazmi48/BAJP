package com.example.core.data.source.remote.utils

import com.example.core.data.source.remote.response.CastItem
import com.example.core.data.source.remote.response.ResultsItem
import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie

object RemoteMapper {

    fun mapResponseMovieToDomainMovie(listMovie: List<ResultsItem>): List<Movie> =
        listMovie.map {
            Movie(
                id = it.id ?: -1,
                title = it.name ?: "-",
                synopsis = it.overview ?: "-",
                releaseDate = it.firstAirDate ?: "-",
                poster =it.posterPath ?: "-"
            )
        }

    fun mapResponseCasterToDomainCaster(listCaster: List<CastItem>): List<Caster> =
        listCaster.map {
            Caster(
                name = it.name ?: "-",
                job = it.character ?: "-",
                avatar = it.profilePath ?: "-"
            )
        }
}