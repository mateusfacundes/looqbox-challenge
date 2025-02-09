package com.mateus.pokedex.microservice.service

import com.mateus.pokedex.microservice.helper.pokemonQuery
import com.mateus.pokedex.microservice.helper.pokemonQuery.Companion.getUrl
import com.mateus.pokedex.microservice.helper.pokemonQuery.Companion.withLimit
import com.mateus.pokedex.microservice.model.PokemonApiResponse
import com.mateus.pokedex.microservice.model.Pokemons
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


@Service
class PokemonService {

    private val restTemplate = RestTemplate()

    private lateinit var pokemonsList: List<Pokemons>

    private var sortByAlphabetical = "alphabetical"
    private var sortByLength = "length"
    private val allowedSort: List<String> = listOf(sortByAlphabetical, sortByLength)
    private var sortType = sortByAlphabetical // alphabetical as default


    fun getPokemons(url: String): PokemonApiResponse {
        val data = this.restTemplate.getForObject<PokemonApiResponse>(url)
        this.pokemonsList = data.results
        return data
    }

    fun getPokemonsByName(name: String?, sort: String?): List<Pokemons> {
        // TODO: Make cache logic
        if (!::pokemonsList.isInitialized || pokemonsList.isEmpty()) {
            // Using 1304 as limit to bring all pokemons
            val url = pokemonQuery.createQuery().withLimit(1304).getUrl()
            this.getPokemons(url)
        }
        if (!sort.isNullOrEmpty() && sort in this.allowedSort) {
            this.sortType = sort
        }

        this.pokemonsList = this.mergeSortPokemonList(this.pokemonsList)
        this.pokemonsList = this.filterPokemonList(this.pokemonsList, name)

        return this.pokemonsList
    }

    // Time Complexity: always O(n log n)
    fun mergeSortPokemonList(pokeList: List<Pokemons>): List<Pokemons> {
        if (pokeList.size <= 1)
            return pokeList

        val half: Int = pokeList.size / 2

        var leftHalf = this.mergeSortPokemonList(pokeList.subList(0, half))
        var rightHalf = this.mergeSortPokemonList(pokeList.subList(half, pokeList.size))

        return this.merge(leftHalf, rightHalf)
    }

    fun merge(leftHalf: List<Pokemons>, rightHalf: List<Pokemons>): List<Pokemons> {
        val results: MutableList<Pokemons> = emptyList<Pokemons>().toMutableList()
        var i: Int = 0
        var j: Int = 0

        while (i < leftHalf.size && j < rightHalf.size) {
            var compare: Boolean = leftHalf[i].name!! <= rightHalf[j].name!!
            if (sortType == sortByLength) {
                compare = leftHalf[i].name!!.length <= rightHalf[j].name!!.length
            }
            if (compare) {
                results.add(leftHalf[i])
                i++
            } else {
                results.add(rightHalf[j])
                j++
            }
        }

        while (i < leftHalf.size) {
            results.add(leftHalf[i])
            i++
        }

        while (j < rightHalf.size) {
            results.add(rightHalf[j])
            j++
        }

        return results
    }

    fun filterPokemonList(pokeList: List<Pokemons>, query: String?): List<Pokemons> {
        if (query.isNullOrEmpty()) {
            return pokeList
        }

        return pokeList.filter { pokemon ->
            pokemon.name?.contains(query, ignoreCase = true) == true
        }
    }

}