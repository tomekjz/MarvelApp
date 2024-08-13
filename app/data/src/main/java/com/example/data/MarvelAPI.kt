package com.example.data

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelAPI {

    companion object {
        const val PUBLIC_API_KEY: String = "645915394fddddd9be844ba172c4fbdf"
        const val TS: String = "1723293746"
        const val HASH: String = "a64ad5e297727b8235258e307ddbeb81"
    }

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apikey: String = PUBLIC_API_KEY,
        @Query("ts") ts: String = TS,
        @Query("hash") hash: String = HASH,
        @Query("limit") limit: String = "30"
        // This API is pretty slow so I have limited the results to 30
    ): JsonObject?
}
