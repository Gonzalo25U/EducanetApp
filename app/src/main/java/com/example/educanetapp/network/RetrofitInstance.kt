package com.example.educanetapp.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://backend-educanet.onrender.com/"

    // Cliente compartido (solo se crea una vez)
    private fun getClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

    // Retrofit compartido — se inicializa solo una vez
    private fun getRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // APIs normales (requieren Context)
    fun getLoginApi(context: Context): LoginApi =
        getRetrofit(context).create(LoginApi::class.java)

    fun getProfileApi(context: Context): ProfileApi =
        getRetrofit(context).create(ProfileApi::class.java)

    fun getResourceApi(context: Context): ResourceApi =
        getRetrofit(context).create(ResourceApi::class.java)
}
