package com.example.healtyapp.data.remote.api
import com.example.healtyapp.data.remote.dto.LoginRequest
import com.example.healtyapp.data.remote.dto.PageResponse
import com.example.healtyapp.data.remote.dto.Patient
import com.example.healtyapp.data.remote.dto.TokenResponse
import com.example.healtyapp.data.remote.dto.Appointment
import com.example.healtyapp.data.remote.dto.Registro
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login/")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    @GET("pacientes/")
    suspend fun getPatients(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10
    ): PageResponse<Patient>

    @GET("citas/")
    suspend fun getCitas(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10,
        @Query("paciente") pacienteId: Int
    ): PageResponse<Appointment>

    @POST("citas/")
    suspend fun crearCita(@Body body: Appointment): Appointment

    @GET("registros/")
    fun getRegistros(
        @Query("cita") citaId: Int,
        @Query("page") page: Int = 1
    ): Call<PageResponse<Registro>>

    @POST("registros/")
    suspend fun crearRegistro(@Body body: Registro): Registro

}
