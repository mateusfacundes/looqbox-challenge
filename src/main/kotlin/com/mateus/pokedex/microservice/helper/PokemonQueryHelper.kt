package com.mateus.pokedex.microservice.helper

class pokemonQuery {

    private var queryUrl = ""

    private constructor(url: String) {
        queryUrl = url
    }

    companion object {
        private final val baseUrl = "https://pokeapi.co/api/v2/"
        private val pokemonEndpoint = baseUrl + "pokemon?"

        fun createQuery(): pokemonQuery {
            return pokemonQuery(pokemonEndpoint)
        }

        fun pokemonQuery.request(): String {
            val data = HttpHelper.createRequest(this.queryUrl)
            return data
        }

        fun pokemonQuery.withLimit(limit: Int): pokemonQuery {
            this.queryUrl += "limit=$limit&"
            return this
        }

        fun pokemonQuery.withOffset(offset: Int): pokemonQuery {
            this.queryUrl += "?offset=$offset&"
            return this
        }
    }
}