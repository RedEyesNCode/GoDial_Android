package com.redeyesncode.gozulix.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDao {
    @androidx.room.Query("SELECT * FROM contacts")
    fun getAllContacts(): List<ContactEntity>

    @Insert
    suspend fun insert(contact: ContactEntity)

    @Update
    suspend fun update(contact: ContactEntity)

    @Delete
    suspend fun delete(contact: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(contact: ContactEntity)

    @Query("SELECT * FROM contacts WHERE status = 'Pending'")
    fun getPendingContacts(): List<ContactEntity>
    @Query("UPDATE contacts SET status = 'Done' WHERE contact_number = :number")
    suspend fun updateStatusToDone(number: String)
    @Query("SELECT * FROM contacts WHERE status = 'Done'")
    fun getDoneStatusContacts():List<ContactEntity>

    @Query("SELECT * FROM contacts WHERE contact_number = :number")
    fun findContactByNumber(number: String): ContactEntity
}
