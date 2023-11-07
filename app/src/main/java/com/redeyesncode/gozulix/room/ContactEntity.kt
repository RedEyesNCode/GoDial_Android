package com.redeyesncode.gozulix.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "contact_name") val contactName: String,
    @ColumnInfo(name = "contact_number") val contactNumber: String,
    @ColumnInfo(name = "status") val status: String, // Pending or Done
    @ColumnInfo(name = "note") var note: String,
    @ColumnInfo(name = "call_event") var callEvent:String,
    @ColumnInfo(name = "lead_score") var leadScore:Int

)
