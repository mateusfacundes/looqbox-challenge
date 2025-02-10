package com.mateus.pokedex.microservice.service

import com.mateus.pokedex.microservice.helper.PokemonQueryHelper
import com.mateus.pokedex.microservice.helper.PokemonQueryHelper.Companion.getUrl
import com.mateus.pokedex.microservice.helper.PokemonQueryHelper.Companion.withLimit
import com.mateus.pokedex.microservice.model.PokemonApiResponse
import com.mateus.pokedex.microservice.model.Pokemons
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import com.mateus.pokedex.microservice.helper.CacheHelper

@Service
class PokemonService(
    private val restTemplate: RestTemplate,
    private val cacheHelper: CacheHelper
) {


    private lateinit var pokemonsList: List<Pokemons>

    private var sortByAlphabetical = "alphabetical"
    private var sortByLength = "length"
    private val allowedSort: List<String> = listOf(sortByAlphabetical, sortByLength)
    private var sortType = sortByAlphabetical // alphabetical as default


    fun getPokemons(url: String): PokemonApiResponse {
        val cachedData = cacheHelper.loadFromCache()
        if (cachedData != null) {
            this.pokemonsList = cachedData.results
            return cachedData
        }

        val data = this.restTemplate.getForObject<PokemonApiResponse>(url)
        this.pokemonsList = data.results
        cacheHelper.saveToCache(data)

        return data
    }

    fun getPokemonsByName(name: String?, sort: String?): List<Pokemons> {

        // Using 10000 as limit to bring all pokemons
        val url = PokemonQueryHelper.createQuery().withLimit(10000).getUrl()
        this.getPokemons(url)

        if (!sort.isNullOrEmpty() && sort in this.allowedSort) {
            this.sortType = sort
        }

        this.pokemonsList = this.mergeSortPokemonList(this.pokemonsList)
        this.pokemonsList = this.filterPokemonList(this.pokemonsList, name)

        return this.pokemonsList
    }

    // Algorithm used: Merge sort
    // Time Complexity: always O(n log n)
    // I use the merge sort because it's time complexity is some for both best and worst case for the alphabetical and length sorting
    fun mergeSortPokemonList(pokeList: List<Pokemons>): List<Pokemons> {
        // check if the list has only one element to return for the recursion
        if (pokeList.size <= 1)
            return pokeList

        // them we find the middle of the list on the stack to them split it in left and right half
        val half: Int = pokeList.size / 2

        // them we split recursively the list until we get only one element at a time
        var leftHalf = this.mergeSortPokemonList(pokeList.subList(0, half))
        var rightHalf = this.mergeSortPokemonList(pokeList.subList(half, pokeList.size))

        // and proceed to the compare and merge the smaller lists
        return this.merge(leftHalf, rightHalf)
    }

    // in this method is where the arrays are compared, sorted and merged
    fun merge(leftHalf: List<Pokemons>, rightHalf: List<Pokemons>): List<Pokemons> {
        val results: MutableList<Pokemons> = emptyList<Pokemons>().toMutableList()
        var i: Int = 0
        var j: Int = 0

        // it compares the both left and right lists and add the sorted element to the results list
        while (i < leftHalf.size && j < rightHalf.size) {
            // this logic is made to define if it's sorting by pokemon's name length our by alphabetical order
            var compare: Boolean = leftHalf[i].name <= rightHalf[j].name
            if (sortType == sortByLength) {
                compare = leftHalf[i].name.length <= rightHalf[j].name.length
            }
            // if the left item is <= them the right item, it's added to the results, as it is the smallest of the item, if not, the other is and continue searching in the next position of the found item's list and so on
            if (compare) {
                results.add(leftHalf[i])
                i++
            } else {
                results.add(rightHalf[j])
                j++
            }
        }

        // them if after the comparison is made and there's still item in both lists they are added to result list because they would already be sorted
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

        return pokeList.filter { pokemon -> pokemon.name.contains(query, ignoreCase = true) }
    }

}