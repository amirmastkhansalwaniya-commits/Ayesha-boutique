package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val address: String,
    val email: String = "",
    val notes: String = "",
    // Saved baseline measurements for returning clients
    val defaultChest: Double? = null,
    val defaultWaist: Double? = null,
    val defaultHips: Double? = null,
    val defaultShoulder: Double? = null,
    val defaultSleeve: Double? = null,
    val defaultTrouserLength: Double? = null,
    val defaultMeasurementUnit: String = "in",
    val createdAt: Long = System.currentTimeMillis()
)
