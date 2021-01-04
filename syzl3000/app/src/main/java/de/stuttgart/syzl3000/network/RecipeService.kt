package de.stuttgart.syzl3000.network

import de.stuttgart.syzl3000.network.model.RecipeDto
import de.stuttgart.syzl3000.network.repsonses.RecipeSearchResponseNew
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface RecipeService {

    @GET("search")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponseNew

    @GET("get")
    suspend fun get(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): RecipeDto
}











