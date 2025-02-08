package com.mateus.pokedex.microservice.service

import com.mateus.pokedex.microservice.helper.pokemonQuery
import com.mateus.pokedex.microservice.helper.pokemonQuery.Companion.getUrl
import com.mateus.pokedex.microservice.helper.pokemonQuery.Companion.withLimit
import com.mateus.pokedex.microservice.model.PokemonApiResponse
import com.mateus.pokedex.microservice.model.Pokemons
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


@Service
class PokemonService {

    private val restTemplate = RestTemplate()

    private lateinit var pokemonsList: List<Pokemons>

    fun getPokemons(url: String): PokemonApiResponse {
        val data = this.restTemplate.getForObject<PokemonApiResponse>(url)
        this.pokemonsList = data.results
        return data
    }

    fun getPokemonsByName(name: String?, sort: String?): List<Pokemons> {
        // TODO: Make cache logic
        if (!::pokemonsList.isInitialized || pokemonsList.isEmpty()) {
            val url = pokemonQuery.createQuery().withLimit(1304).getUrl()
            println(url)
            this.getPokemons(url)
        }

        //TODO: Filter pokemon list by name an sort it
        println(name)
        println(sort)
        return this.pokemonsList
    }

}