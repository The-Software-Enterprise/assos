package com.swent.assos.model.local_database

import android.content.Context
import androidx.room.Room

object LocalDatabaseProvider {
  private var instance: LocalDatabase? = null

  fun getLocalDatabase(context: Context): LocalDatabase {
    if (instance == null) {
      instance =
          Room.databaseBuilder(
                  context.applicationContext, LocalDatabase::class.java, "local-database")
              .build()
    }
    return instance!!
  }
}
