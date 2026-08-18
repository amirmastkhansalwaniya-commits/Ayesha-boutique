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
    val measurementNotes: String = ""
)
