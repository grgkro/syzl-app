package de.stuttgart.syzl3000.network

import de.stuttgart.syzl3000.network.repsonses.MovieSearchResponse
import de.stuttgart.syzl3000.network.repsonses.UserSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("search")
    suspend fun search(
            @Header("Authorization") token: String,
            @Query("query") query: String
    ) : UserSearchResponse

    @GET("users/{id}")
    suspend fun get(
            @Header("Authorization") token: String,
            @Path("id") id: String
    ) : UserSearchResponse
}