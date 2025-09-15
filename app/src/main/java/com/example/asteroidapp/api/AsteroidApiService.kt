package com.example.asteroidapp.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.asteroidapp.BuildConfig
import com.example.asteroidapp.api.models.ImageOfTodayModel
import com.example.asteroidapp.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidApiStatus { LOADING, ERROR, DONE }

private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

val client = OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor { it ->
    val url =
        it.request().url.newBuilder().addQueryParameter("api_key", BuildConfig.NASA_API_KEY).build()
    it.proceed(it.request().newBuilder().url(url).build())
}

private val retrofit = Retrofit.Builder().client(client.build()).baseUrl(Constants.BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroid(
        @Query("start_date") startDate: String, @Query("end_date") endDate: String
    ): String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(): ImageOfTodayModel
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}

enum class AsteroidApiFilter(val value: String) {
    SHOW_WEEK("week"), SHOW_TODAY("today"), SHOW_SAVED(
        "saved"
    )
}

