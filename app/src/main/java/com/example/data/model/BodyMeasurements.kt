package com.example.data.model

data class BodyMeasurements(
    val chest: Double? = null,
    val waist: Double? = null,
    val hips: Double? = null,
    val shoulder: Double? = null,
    val sleeve: Double? = null,
    val trouserLength: Double? = null,
    val neck: Double? = null,
    val inseam: Double? = null,
    val armhole: Double? = null,
    val thigh: Double? = null,
    val unit: String = "in", // "in" or "cm"
    val measurementNotes: String = "",
    val customFieldsJson: String = "" // Delimited storage for added/custom measurement options
) {
    fun getCustomList(): List<Pair<String, String>> {
        if (customFieldsJson.isBlank()) return emptyList()
        return customFieldsJson.split(";;").mapNotNull { entry ->
            val parts = entry.split("::")
            if (parts.size >= 2 && parts[0].isNotBlank()) {
                parts[0].trim() to parts[1].trim()
            } else null
        }
    }

    companion object {
        fun encodeCustomList(items: List<Pair<String, String>>): String {
            return items.filter { it.first.isNotBlank() && it.second.isNotBlank() }
                .joinToString(";;") { "${it.first.trim()}::${it.second.trim()}" }
        }
    }
}
