package com.mateus.pokedex.microservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import com.mateus.pokedex.microservice.model.Pokemons
import com.mateus.pokedex.microservice.service.PokemonService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity

@RestController
class PokemonController (private val pokemonService: PokemonService) {

    @GetMapping("/pokemons")
    fun getPokemons(
        @RequestParam(defaultValue = "") query: String,
        @RequestParam(defaultValue = "") sort: String,
        response: HttpServletResponse
    ): ResponseEntity<Map<String, List<String?>>> {
        val data = pokemonService.getPokemonsByName(query, sort)

        val pokemons = data.map { it.name }
        val result = mapOf("result" to pokemons)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/pokemons/highlight")
    fun getPokemonsHighlight(
        @RequestParam(defaultValue = "") query: String,
        @RequestParam(defaultValue = "") sort: String,
        response: HttpServletResponse
    ): ResponseEntity<Map<String, List<Pokemons>>> {
        val data = pokemonService.getPokemonsByName(query, sort)
            .onEach { pokemon ->
                pokemon.highlight = pokemon.getHighlight(query)
            }

        val result = mapOf("result" to data)
        return ResponseEntity.ok(result)
    }

}