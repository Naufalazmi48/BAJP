package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CasterResponse(

	@field:SerializedName("cast")
	val cast: List<CastItem?>? = null,
)

data class CastItem(

	@field:SerializedName("character")
	val character: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("profile_path")
	val profilePath: String? = null,
)
