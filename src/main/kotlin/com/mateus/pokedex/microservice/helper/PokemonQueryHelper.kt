package com.mateus.pokedex.microservice.helper

class PokemonQueryHelper {

    private var queryUrl = ""

    private constructor(url: String) {
        queryUrl = url
    }

    companion object {
        private final val baseUrl = "https://pokeapi.co/api/v2/"
        private val pokemonEndpoint = baseUrl + "pokemon?"

        fun createQuery(): PokemonQueryHelper {
            return PokemonQueryHelper(pokemonEndpoint)
        }

        fun PokemonQueryHelper.getUrl(): String {
            return this.queryUrl
        }

        fun PokemonQueryHelper.withLimit(limit: Int): PokemonQueryHelper {
            this.queryUrl += "limit=$limit&"
            return this
        }

        fun PokemonQueryHelper.withOffset(offset: Int): PokemonQueryHelper {
            this.queryUrl += "?offset=$offset&"
            return this
        }
    }
}