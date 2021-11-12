package com.example.core.testing

import com.example.core.domain.model.Caster
import com.example.core.domain.model.Movie

object DataDummy {
    fun generateDummyCaster(): List<Caster> =
        arrayListOf(
            Caster("Tester", "Tester", "Tester"),
            Caster("Tester1", "Tester1", "Tester1"),
        )

    fun generateDummyMovie(): List<Movie> = arrayListOf(
        Movie(id = 0, "Tester", "Tester", "Tester", "Tester"),
        Movie(id = 1, "Tester1", "Tester1", "Tester1", "Tester1"),
    )
}