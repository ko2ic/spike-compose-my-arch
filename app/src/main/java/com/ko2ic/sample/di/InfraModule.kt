package com.ko2ic.sample.di

import com.ko2ic.sample.model.valueobject.HttpLocateType
import com.ko2ic.sample.repository.GitHubRepository
import com.ko2ic.sample.repository.GitHubRepositoryImpl
import com.ko2ic.sample.repository.JsonPlaceholderRepository
import com.ko2ic.sample.repository.JsonPlaceholderRepositoryImpl
import com.ko2ic.sample.repository.http.JsonPlaceHolderHttpClient
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
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
class InfraModule {

    @HttpLocate(HttpLocateType.GitHubApi)
    @Provides
    internal fun provideGitHubRetrofit(
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

    @HttpLocate(HttpLocateType.JsonPlaceholderApi)
    @Provides
    internal fun provideJsonPlaceholderRetrofit(
        httpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
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
        @HttpLocate(HttpLocateType.GitHubApi) retrofit: Retrofit
    ): GitHubHttpClient {
        return retrofit.create(GitHubHttpClient::class.java)
    }

    @Provides
    internal fun provideJsonPlaceholderHttpClient(
        @HttpLocate(HttpLocateType.JsonPlaceholderApi) retrofit: Retrofit
    ): JsonPlaceHolderHttpClient {
        return retrofit.create(JsonPlaceHolderHttpClient::class.java)
    }

}

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindGithubRepository(impl: GitHubRepositoryImpl): GitHubRepository

    @Binds
    abstract fun bindJsonPlaceholderRepository(impl: JsonPlaceholderRepositoryImpl): JsonPlaceholderRepository
}

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class HttpLocate(val value: HttpLocateType = HttpLocateType.GitHubApi)