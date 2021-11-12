package com.example.core.data.source.local.utils

import com.example.core.data.source.local.entity.CasterEntity
import com.example.core.data.source.local.entity.MovieEntity
import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie

object LocalMapper {
    fun movieDomainToMovieEntity(movie: Movie, categoryMovie: String): MovieEntity =
        MovieEntity(
            id = movie.id,
            firstAirDate = movie.releaseDate,
            overview = movie.synopsis,
            posterPath = movie.poster,
            name = movie.title,
            category = categoryMovie
        )

    fun casterDomainToCasterEntity(caster: Caster, idMovie: Int): CasterEntity =
        CasterEntity(
            idMovie = idMovie,
            character = caster.job,
            name = caster.name,
            profilePath = caster.avatar,
        )

    fun movieEntityToMovieDomain(listMovieEntity: List<MovieEntity>): List<Movie> =
        listMovieEntity.map {
            Movie(
                id = it.id,
                title = it.name,
                synopsis = it.overview,
                releaseDate = it.firstAirDate,
                poster = it.posterPath
            )
        }

    fun casterEntityToCasterDomain(listCasterEntity: List<CasterEntity>): List<Caster> =
        listCasterEntity.map {
            Caster(
                name = it.name,
                job = it.character,
                avatar = it.profilePath
            )
        }
}