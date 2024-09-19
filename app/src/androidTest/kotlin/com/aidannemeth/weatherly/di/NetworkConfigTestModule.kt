package com.aidannemeth.weatherly.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkConfigModule::class]
)
object NetworkConfigTestModule {
    @Provides
    fun provideBaseHttpUrl(mockWebServer: MockWebServer): HttpUrl =
        mockWebServer.url("/test/")
}
