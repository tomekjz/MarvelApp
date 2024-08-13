package com.example.presentation

import com.example.data.MarvelAPI
import com.example.data.CharactersRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    factory { com.example.domain.GetCharactersUseCase(get()) }
    factory { CharactersRepository(get()) }
    viewModel { CharacterViewModel(get()) }


    //network
    single {
        Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com:443")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor((HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)))
                    .addInterceptor { chain ->
                        var request = chain.request()
                        request = request.newBuilder()
                            .header("accept", "application/json")
                            .build()
                        chain.proceed(request)
                    }
                    .connectTimeout(0L, TimeUnit.MILLISECONDS)
                    .readTimeout(0L, TimeUnit.MILLISECONDS)
                    .writeTimeout(0L, TimeUnit.MILLISECONDS)
                    .build()
            )
            .build()
    }

    single { get<Retrofit>().create(MarvelAPI::class.java) }
}
