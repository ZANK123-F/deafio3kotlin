package com.example.desafio3

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RecursoApiService {
    @GET("recursos")
    suspend fun getRecursos(): List<Modelo>

    @POST("recursos")
    suspend fun createRecurso(@Body recurso: Modelo): Modelo

    @PUT("recursos/{id}")
    suspend fun updateRecurso(@Path("id") id: Long, @Body recurso: Modelo)

    @DELETE("recursos/{id}")
    suspend fun deleteRecurso(@Path("id") id: Long)
}
