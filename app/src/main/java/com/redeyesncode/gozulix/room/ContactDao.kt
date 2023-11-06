package com.redeyesncode.gozulix.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
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
}
