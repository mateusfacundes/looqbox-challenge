package com.mateus.pokedex.microservice.controller

import com.mateus.pokedex.microservice.helper.pokemonQuery.Companion.request
import com.mateus.pokedex.microservice.helper.pokemonQuery.Companion.withLimit
import com.mateus.pokedex.microservice.helper.pokemonQuery.Companion.withOffset
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.mateus.pokedex.microservice.helper.pokemonQuery

@RestController
class PokemonController {


    @GetMapping("/teste")
    fun index(): String {
        val data = pokemonQuery.createQuery()
            .withLimit(3)
            .withOffset(3)
            .request()

        return data
    }

}