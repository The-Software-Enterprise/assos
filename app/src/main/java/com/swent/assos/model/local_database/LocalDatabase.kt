package com.swent.assos.model.local_database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swent.assos.model.data.Association
import com.swent.assos.model.data.Ticket

@Database(entities = [Association::class, Ticket::class], version = 2)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
  abstract fun associationDao(): AssociationDao
}

@Dao
interface AssociationDao {
  @Query("SELECT * FROM associations ORDER BY acronym ASC")
  fun getAllAssociations(): List<Association>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAssociation(vararg associations: Association)

  @Delete suspend fun deleteAssociation(association: Association)
}

@Dao
interface TicketDao {
  @Query("SELECT * FROM tickets") fun getTickets(associationId: Int): List<Ticket>

  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertTicket(vararg tickets: Ticket)

  @Delete suspend fun deleteTicket(ticket: Ticket)
}
