package com.mateus.pokedex.microservice.model

class PokemonApiResponse {
    var count: Int? = 0
    var next: String? = ""
    var previous: String? = ""
    lateinit var results: List<Pokemons>

}