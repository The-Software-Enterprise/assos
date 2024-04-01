package com.swent.assos.model.service.module

import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import com.swent.assos.model.service.impl.AuthServiceImpl
import com.swent.assos.model.service.impl.DbServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AuthServiceImpl): AuthService

    @Binds
    abstract fun provideStorageService(impl: DbServiceImpl): DbService
}
