package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(
	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null,
)

data class ResultsItem(

	@field:SerializedName("first_air_date", alternate = ["release_date"])
	val firstAirDate: String? = null,

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("name", alternate = ["title"])
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,
)
