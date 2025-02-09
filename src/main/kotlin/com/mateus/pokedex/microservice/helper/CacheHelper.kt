package com.mateus.pokedex.microservice.helper

import com.mateus.pokedex.microservice.model.PokemonApiResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class CacheHelper {

    private val cacheDir = "cache"
    private val cacheFile = File("$cacheDir/pokemon_cache.json")
    private val objectMapper = jacksonObjectMapper()

    init {
        File(cacheDir).mkdirs()
    }

    fun saveToCache(data: PokemonApiResponse) {
        try {
            objectMapper.writeValue(cacheFile, data)
            println("Data saved to cache at: ${cacheFile.absolutePath}")
        } catch (e: Exception) {
            println("Failed to save data to cache: ${e.message}")
        }
    }

    fun loadFromCache(): PokemonApiResponse? {
        return if (cacheFile.exists()) {
            try {
                val data = objectMapper.readValue<PokemonApiResponse>(cacheFile)
                data
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    fun clearCache() {
        if (cacheFile.exists()) {
            cacheFile.delete()
            println("Cache cleared at: ${cacheFile.absolutePath}")
        }
    }
}