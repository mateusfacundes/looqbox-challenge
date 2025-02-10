package com.mateus.pokedex.microservice.config

import com.mateus.pokedex.microservice.helper.CacheHelper
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AppInitializer() {

    @Bean
    fun clearCacheOnStartup(): ApplicationRunner {
        return ApplicationRunner {
            CacheHelper().clearCache()
        }
    }

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun cacheHelper(): CacheHelper = CacheHelper()

}