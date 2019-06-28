package dev.android.moviecatalog.api

import dev.android.moviecatalog.utils.Constants.Companion.baseURL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {

    companion object {
        fun getClient(): MovieApiService {
            return provideMuseumApi()
        }

        private fun provideMuseumApi(): MovieApiService {
            return provideRetrofit().create(MovieApiService::class.java)
        }

        private fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        private fun provideOkHttpClient(): OkHttpClient {

            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original!!.newBuilder()
                        .build()
                    return@addInterceptor chain.proceed(request)
                }
                .addInterceptor(provideHttpLoggingInterceptor())
                .build()
        }


        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return loggingInterceptor
        }
    }

}