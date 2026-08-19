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
    val defaultCustomFieldsJson: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getCustomList(): List<Pair<String, String>> {
        if (defaultCustomFieldsJson.isBlank()) return emptyList()
        return defaultCustomFieldsJson.split(";;").mapNotNull { entry ->
            val parts = entry.split("::")
            if (parts.size >= 2 && parts[0].isNotBlank()) {
                parts[0].trim() to parts[1].trim()
            } else null
        }
    }
}
