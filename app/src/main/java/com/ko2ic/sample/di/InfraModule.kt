package com.ko2ic.sample.di

import com.ko2ic.sample.repository.GitHubRepository
import com.ko2ic.sample.repository.GitHubRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ko2ic.sample.repository.http.GitHubHttpClient
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class InfraModule {

    @Provides
    internal fun provideRetrofit(
        httpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(httpClient)
            .build()
    }

    @Provides
    internal fun provideOkHttp(
        logInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .build()
    }

    @Provides
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    internal fun provideGitHubHttpClient(
        retrofit: Retrofit
    ): GitHubHttpClient {
        return retrofit.create(GitHubHttpClient::class.java)
    }

}

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindGithubRepository(impl: GitHubRepositoryImpl): GitHubRepository

}
