package com.mateus.pokedex.microservice.model

class Pokemons {
    var name: String = ""
    var highlight: String = ""

    fun getHighlight(query: String?): String {
        if (query.isNullOrEmpty()){
            return this.name
        }
        return this.name.replace(query, "<pre>" + query + "</pre>")
    }

}