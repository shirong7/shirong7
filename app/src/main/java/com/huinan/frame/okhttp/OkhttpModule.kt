package com.huinan.frame.okhttp
import com.google.gson.GsonBuilder
import com.huinan.frame.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkhttpModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BaseRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NormalOkHttp

    @Provides
    @Singleton
    @NormalOkHttp
    fun provideOkHttpClient(): OkHttpClient {
        val defaultOutTime = 30L
        val builder = OkHttpClient.Builder().connectTimeout(defaultOutTime, TimeUnit.SECONDS)
            .writeTimeout(defaultOutTime, TimeUnit.SECONDS)
            .readTimeout(defaultOutTime, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @BaseRetrofit
    fun provideBaseRetrofit(@NormalOkHttp okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(RequestPath.BASE_API_URL())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .serializeNulls()
                        .create()
                )
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideBaseService(@BaseRetrofit retrofit: Retrofit): IAPIService {
        return retrofit.create(IAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideBaseRepository(mIAPIService: IAPIService): BaseRepository {
        return BaseRepositoryImpl(mIAPIService)
    }


}