package com.mateus.pokedex.microservice.helper

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

class HttpHelper {
    companion object {
        fun createRequest(url:String): String {
            val client = HttpClient.newHttpClient()
            val request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build()
            val response = client.send(request, BodyHandlers.ofString())

            return response.body()
        }

    }
}