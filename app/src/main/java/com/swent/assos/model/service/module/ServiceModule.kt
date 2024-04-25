package com.swent.assos.model.service.module

import com.swent.assos.model.service.AuthService
import com.swent.assos.model.service.DbService
import com.swent.assos.model.service.StorageService
import com.swent.assos.model.service.impl.AuthServiceImpl
import com.swent.assos.model.service.impl.DbServiceImpl
import com.swent.assos.model.service.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
  @Binds abstract fun provideAuthService(impl: AuthServiceImpl): AuthService

  @Binds abstract fun provideDbService(impl: DbServiceImpl): DbService

  @Binds abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
}
