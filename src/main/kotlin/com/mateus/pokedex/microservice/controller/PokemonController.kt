package com.mateus.pokedex.microservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import com.mateus.pokedex.microservice.model.Pokemons
import com.mateus.pokedex.microservice.service.PokemonService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity

@RestController
class PokemonController {

    @GetMapping("/pokemons")
    fun getPokemons(
        @RequestParam(defaultValue = "") query: String,
        @RequestParam(defaultValue = "") sort: String,
        response: HttpServletResponse
    ): ResponseEntity<List<Pokemons>> {
        val data = PokemonService().getPokemonsByName(query, sort)

        return ResponseEntity.ok(data)
    }

}