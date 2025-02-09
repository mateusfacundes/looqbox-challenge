package com.mateus.pokedex.microservice.config

import com.mateus.pokedex.microservice.helper.CacheHelper
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppInitializer() {

    @Bean
    fun clearCacheOnStartup(): ApplicationRunner {
        return ApplicationRunner {
            CacheHelper().clearCache()
        }
    }
}