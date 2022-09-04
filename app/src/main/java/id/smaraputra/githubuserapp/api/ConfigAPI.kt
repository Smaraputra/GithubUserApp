package id.smaraputra.githubuserapp.api

import id.smaraputra.githubuserapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigAPI {
    companion object {
        fun getApiService(): ServicesAPI {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).addNetworkInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header("Authorization", "token " + BuildConfig.API_KEY)
                    chain.proceed(requestBuilder.build())
                    }
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ServicesAPI::class.java)
        }
    }
}