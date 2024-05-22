package com.swent.assos.model.localDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swent.assos.model.data.Association
import kotlinx.coroutines.flow.StateFlow

@Database(entities = [Association::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDb : RoomDatabase() {
  abstract fun associationDao(): AssociationDao
}

@Dao
interface AssociationDao {
  @Query("SELECT * FROM associations") fun getAllAssociations(): List<Association>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAssociation(vararg associations: Association)

  @Delete
  suspend fun deleteAssociation(association: Association)
}
